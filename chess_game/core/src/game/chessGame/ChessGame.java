package game.chessGame;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import boardstructure.*;
import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import db.Database;
import game.Chess;
import game.listeners.ChessGameListener;
import models.GameState;
import org.json.JSONArray;
import org.json.JSONObject;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Bishop;
import pieces.pieceClasses.King;
import pieces.pieceClasses.Knight;
import pieces.pieceClasses.Pawn;
import pieces.pieceClasses.Queen;
import player.AI;
import db.Player;
import player.AIThreadMove;
import setups.Chess960Setup;
import setups.DefaultSetup;
import socket.SocketHandler;
import socket.SocketHandlerListener;
import sound.AudioManager;

/**
 * The ChessGame class ties together and keeps track of logic surrounding the current game of chess.
 * This includes an implementation of the game clock,
 * deciding when the game is over and updating player statistics after a game.
 */
public class ChessGame implements IChessGame, BoardListener, SocketHandlerListener {

    private GameInfo gameInfo;
    private IBoard board;
    private AI computerAI;

    private String gameOverString;

    private ChessGameListener listener;

    private ArrayList<IBoard> boardHistory = new ArrayList<>();

    //TODO: make undo care about the timers.
    private int playerSeconds, opponentSeconds;

    private Timer playerTimer, opponentTimer;
    private boolean playerTimerRunning, opponentTimerRunning;

    private SocketHandler socketHandler;

    public ChessGame(GameInfo gameInfo, ChessGameListener listener) {
        this.gameInfo = gameInfo;
        this.listener = listener;
        this.gameOverString = "";

        if (gameInfo.getGameType() == GameType.BULLET) {
            playerSeconds = opponentSeconds = 60;
        } else if (gameInfo.getGameType() == GameType.BLITZ) {
            playerSeconds = opponentSeconds = 180;
        } else if (gameInfo.getGameType() == GameType.RAPID) {
            playerSeconds = opponentSeconds = 60 * 15;
        } else {
            playerSeconds = opponentSeconds = 60 * 60;
        }
        // Set first turn and board for standard chess
        if (gameInfo.getGameType() != GameType.CHESS960)
            this.board = (new DefaultSetup()).getInitialPosition(gameInfo.getPlayerColor(), this);
        else
            this.board = (new Chess960Setup()).getInitialPosition(gameInfo.getPlayerColor(), this);

        this.boardHistory.add(board.copy());

        // Check if we are creating/joining a multiplayer game
        if (gameInfo.isOnline()) {
            socketHandler = new SocketHandler(this);
            socketHandler.connect();
        } else {

            // Load AI
            if (gameInfo.getLevel() != null) {
                computerAI = gameInfo.getLevel().getAI(gameInfo.getPlayerColor().getOpposite());
                board.setAI(computerAI);
            }

            // Start timer!
            turnTimer();
        }
    }

    private void turnTimer() {
        if (board.getTurn() == gameInfo.getPlayerColor()) {
            if (!playerTimerRunning) {
                playerTimer = new Timer();
                playerTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        playerSeconds -= 1;
                        if (listener != null) listener.turnTimerElapsed();
                        if (playerSeconds == 0) {
                            Gdx.app.postRunnable(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            gameInfo.setGameOverString("Time's up");
                                            finishGame(board.getTurn());
                                        }
                                    }
                            );
                        }
                    }
                }, 0, 1000);
                playerTimerRunning = true;
            }
            if (opponentTimerRunning) {
                new Thread(() -> Gdx.app.postRunnable(
                        () -> {
                            opponentTimer.cancel();
                            opponentTimerRunning = false;
                        }
                )).start();
                opponentTimer.cancel();
                opponentTimerRunning = false;
                //time in time-sensitive games.
                //the actual time addition is seconds - 1.
                switch (gameInfo.getGameType()) {
                    case BLITZ:
                    case BULLET:
                        opponentSeconds += 2;
                        break;
                    case RAPID:
                        opponentSeconds += 10;
                        break;
                    default:
                }
                listener.turnTimerElapsed();
            }
        } else {
            if (!opponentTimerRunning) {
                opponentTimer = new Timer();
                opponentTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        opponentSeconds -= 1;
                        if (listener != null) listener.turnTimerElapsed();
                        if (opponentSeconds == 0) {
                            opponentTimer.cancel();
                            gameInfo.setGameOverString("Time's up");
                            finishGame(gameInfo.getPlayerColor().getOpposite());
                        }
                    }
                }, 0, 1000);
                opponentTimerRunning = true;
            }
            if (playerTimerRunning) {
                //time in time-sensitive games.
                playerTimer.cancel();
                playerTimerRunning = false;
                switch (gameInfo.getGameType()) {
                    case BLITZ:
                    case BULLET:
                        playerSeconds += 2;
                        break;
                    case RAPID:
                        playerSeconds += 10;
                        break;
                    default:
                }
                listener.turnTimerElapsed();
            }
        }
    }

    @Override
    public void performPromotion(Move move, PromotionPiece piece) {
        board.performPromotion(move, piece);
    }

    @Override
    public ArrayList<Move> getLegalMoves(int x, int y) {
        Square square = board.getSquare(x, y);
        if (square.getPiece().getColor() != board.getTurn()) return new ArrayList<>();
        return square.getPiece().getLegalMoves(square, board, gameInfo.getPlayerColor());
    }

    @Override
    public void doTurn(int fromX, int fromY, int toX, int toY) {
        board.move(fromX, fromY, toX, toY);
    }

    @Override
    public boolean undoTurn() {
        if (getBoardHistory().size() <= 1) {
            AudioManager.playNoUndo();
            return false;
        }
        if (getTurn() == gameInfo.getPlayerColor()) {
            boardHistory.remove(boardHistory.size() - 1);
        }
        boardHistory.remove(boardHistory.size() - 1);
        IBoard board = boardHistory.get(boardHistory.size() - 1).copy();
        setBoard(board);
        return true;
    }

    public void aiMove() {
        if (computerAI == null) return;
        if (computerAI.getPieceColor() == board.getTurn()) {
            //Move move = computerAI.calculateMove(board);

            AIThreadMove ai = new AIThreadMove(computerAI, board, this);
            Thread thread = new Thread(ai);
            thread.start();
        }
    }

    @Override
    public void finishGame(PieceColor turn) {
        //stop clock
        if (playerTimerRunning) playerTimer.cancel();
        if (opponentTimerRunning) opponentTimer.cancel();
        Player p = gameInfo.getPlayer();
        Player o = gameInfo.getOpponent();
        if (turn == null) {
            if (updateRatings(p, o, 3)) {
                listener.gameOver(3);
            }
        } else if (turn == gameInfo.getPlayerColor()) {
            //player, whose color is turn, lost
            if (updateRatings(p, o, 2)) {
                listener.gameOver(2);
            }
        } else {

            //player, whose color is turn, won
            if (updateRatings(p, o, 1)) {

                listener.gameOver(1);
            }
        }
    }

    // WAYS TO END GAMES ---------------------------------------------------------

    @Override
    public boolean isTie() {
        return fiftyMoves() || impossibleCheckmate() || stalemate() || threefoldRepetition();
    }

    @Override
    public boolean threefoldRepetition() {
        //no threefoldrepetition if no player made 3 moves
        if (boardHistory.size() < 5) {
            return false;
        }

        //current board;
        IBoard current = board;
        int count = 1;
        //assumes all boards exists and have same size
        for (int i = boardHistory.size() - 3; i >= 0; i -= 2) {
            if (isSame(current, boardHistory.get(i))) {
                //found equal board.
                count++;
                //found threefold-repetition
                if (count >= 3) {
                    System.out.println("Draw by threefoldrepetition");
                    gameInfo.setGameOverString("Draw by threefoldrepetition");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Two boards are equal
     *
     * @param board
     * @param other
     * @return
     */
    public boolean isSame(IBoard board, IBoard other) {
        if (board.getSquares().size() != other.getSquares().size()) {
            return false;
        }
        for (Square sq : board.getSquares())
            if (!contains(other, sq)) {
                return false;
            }

        for (Square sq : other.getSquares())
            if (!contains(board, sq))
                return false;
        return true;
    }

    @Override
    public boolean contains(IBoard board, Square sq) {
        for (Square other : board.getSquares()) {
            if (sq.getX() == other.getX() && sq.getY() == other.getY()) {
                if (sq.isEmpty() && other.isEmpty())
                    return true;
                if (!sq.isEmpty() && !other.isEmpty())
                    if (sq.getClass() == other.getClass() && sq.getPiece().getColor() == other.getPiece().getColor())
                        return true;
            }
        }
        return false;
    }

    @Override
    public boolean fiftyMoves() {
        ArrayList<Move> moves = board.getHistory();
        int count = 0;
        for (int i = moves.size() - 1; i >= 0; i--) {
            //if a piece was captured, or pawn moved, no draw.
            if (moves.get(i).getCapturedPiece() != null || moves.get(i).getMovingPiece() instanceof Pawn) {
                return false;
            }
            count++;
            if (count >= 50) {
                System.out.println("Draw by 50-move-rule.");
                gameInfo.setGameOverString("Draw by 50-move-rule");
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean checkmate() {
        if (board.getAvailableMoves(board.getTurn()).isEmpty()) {
            ArrayList<IPiece> threat = board.piecesThreatenedByOpponent(board.getTurn(), board.getTurn().getOpposite());
            for (IPiece p : threat) {
                if (p instanceof King) {
                    gameInfo.setGameOverString("Checkmate");
                    return true;
                }
            }
        }
        return false;

    }


    @Override
    public boolean impossibleCheckmate() {
        ArrayList<Square> pieceSqs = new ArrayList<>();
        for (Square sq : board.getSquares()) {
            if (!sq.isEmpty())
                pieceSqs.add(sq);
        }

        //only way to have 2 pieces left, is 2 kings.
        if (pieceSqs.size() == 2) {
            return true;
        } else if (pieceSqs.size() == 3) {
            for (Square p : pieceSqs)
                //if last piece is bishop or knight, no check-mate can be reached. Automatic draw.
                if (p.getPiece() instanceof Bishop || p.getPiece() instanceof Knight) {
                    gameInfo.setGameOverString("Impossible checkmate");
                    return true;
                }
        } else if (pieceSqs.size() == 4) {
            return fourPiecesCausesAutomaticDraw(pieceSqs);
        }
        return false;
    }

    /**
     * Checks if the two pieces besides the king are two
     * bishops that are on the same color-squares, but for
     * different players.
     *
     * @param pieceSqs
     * @return true if draw, false else
     */
    public boolean fourPiecesCausesAutomaticDraw(ArrayList<Square> pieceSqs) {
        ArrayList<Square> bishops = new ArrayList<>();
        //find bishops
        for (Square sq : pieceSqs) {
            if (!(sq.getPiece() instanceof Bishop) && !(sq.getPiece() instanceof King))
                return false;
            if (sq.getPiece() instanceof Bishop) {
                bishops.add(sq);
            }
        }
        //need two bishops for further checks
        if (bishops.size() != 2)
            return false;
        if (bishops.get(0).getPiece().getColor() == bishops.get(1).getPiece().getColor())
            return false;
        if (bishops.get(0).squareIsWhite() != bishops.get(1).squareIsWhite()) {
            return false;
        }

        gameInfo.setGameOverString("Four piece automatic draw");

        return true;
    }

    @Override
    public boolean stalemate() {
        if (board.getAvailableMoves(board.getTurn()).isEmpty()) {
            //put in if you need check for stale-mate (king not in check)
			/*
            ArrayList<IPiece> threat = board.piecesThreatenedByOpponent(turn, getOtherPieceColor(turn));
			for(IPiece p : threat) {
				if (p instanceof King) {
					return false;
				}
			} */
            gameInfo.setGameOverString("Stalemate");
            return true;
        }
        return false;
    }

    @Override
    public void resign() {
        gameInfo.setGameOverString("Game resigned");
        finishGame(board.getTurn());
    }

    // WAYS TO END GAMES - END ---------------------------------------------------------


    /**
     * Method that calculates the new rating for a player after a game
     *
     * @param rating1       Rating of the player
     * @param rating2       Rating of the adversary
     * @param win_lose_draw 1 if player wins, 2 if player loses and 3 if player draws
     * @return newRating The players new rating
     */
    public static int calculateNewRating(int rating1, int rating2, int win_lose_draw) {
        if (win_lose_draw > 3 || win_lose_draw < 1) {
            throw new IllegalArgumentException("Illegal input on win_lose_draw - Must be either 1, 2 or 3");
        }

        // Sensitivity parameter
        int K = 32;

        // Intermediary calculations
        double r1 = Math.pow(10, rating1 / 400.0);
        double r2 = Math.pow(10, rating2 / 400.0);
        double expectedScore1 = r1 / (r1 + r2);
        double s;

        if (win_lose_draw == 1)         // win
        {
            s = 1;
        } else if (win_lose_draw == 2)  // loss
        {
            s = 0;
        } else                         // Draw
        {
            s = 0.5;
        }

        double newRating = rating1 + (K * (s - expectedScore1));

        return (int) newRating;
    }

    /**
     * Precondition: game is against AI.
     * Update for single player game.
     *
     * @param p
     * @param win_lose_draw
     */
    private boolean updateSinglePlayerRating(Player p, int win_lose_draw) {
        int newRating = calculateNewRating(p.getRating(), computerAI.getRating(), win_lose_draw);
        gameInfo.setPlayerRatingChange(newRating - p.getRating());
        try {
            return Chess.getDatabase().updatePlayer(p.getName(), newRating, win_lose_draw);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateRatings(Player p, Player o, int win_lose_draw) {
        if (o == null) {
            return updateSinglePlayerRating(p, win_lose_draw);
        }

        String pName = p.getName();
        String oName = o.getName();
        int pRating = p.getRating();
        int oRating = o.getRating();

        int op_win_lose_draw;
        if (win_lose_draw == 1)
            op_win_lose_draw = win_lose_draw + 1;
        else if (win_lose_draw == 2)
            op_win_lose_draw = win_lose_draw - 1;
        else
            op_win_lose_draw = win_lose_draw;

        int pNewRating = calculateNewRating(pRating, oRating, win_lose_draw);
        gameInfo.setPlayerRatingChange(pNewRating - pRating);


        int oNewRating = calculateNewRating(oRating, pRating, op_win_lose_draw);
        gameInfo.setOpponentRatingChange(oNewRating - oRating);

        try {
            return Chess.getDatabase().updatePlayer(pName, pNewRating, win_lose_draw)
                    && Chess.getDatabase().updatePlayer(oName, oNewRating, op_win_lose_draw);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public IBoard getBoard() {
        return this.board;
    }

    @Override
    public ArrayList<Square> getSquares() {
        return board.getSquares();
    }

    @Override
    public PieceColor getTurn() {
        return board.getTurn();
    }

    @Override
    public void setBoard(IBoard board) {
        this.board = board;
    }

    public Move getLastMove() {
        return board.getLastMove();
    }

    @Override
    public void setBoardHistory(ArrayList<IBoard> boardHistory) {
        this.boardHistory = boardHistory;
    }

    public int getPlayerSeconds() {
        return playerSeconds;
    }

    public int getOpponentSeconds() {
        return opponentSeconds;
    }

    public String formatTime(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    @Override
    public ArrayList<IBoard> getBoardHistory() {
        return boardHistory;
    }

    @Override
    public GameInfo getGameInfo() {
        return this.gameInfo;
    }

    @Override
    public void disconnectSocket() {
        if (socketHandler != null) {
            socketHandler.disconnect();
        }
    }

    @Override
    public void promotionRequested(Move move) {
        listener.promotionRequested(move);
    }

    @Override
    public void movePerformed(Board board, ArrayList<Move> moves) {
        listener.moveOk(moves);

        playMoveSound(board.getLastMove().getCapturedPiece());

        turnTimer();
        boardHistory.add(board.copy());

        if (gameInfo.isOnline() && board.getTurn() != gameInfo.getPlayerColor()) {
            if (checkmate()) {
                // TODO: 03/05/2018 send game-over to opponent with checkmate

            } else if (isTie()) {
                // TODO: 03/05/2018 send game-over to opponent with tie

            } else {

                // Emit move-data to opponent.
                JSONObject obj = new JSONObject();
                obj.put("type", "moves");

                JSONArray arrMoves = new JSONArray();
                for (Move m : moves) {

                    // We need to serialize the object ourselves...
                    // NOTE: 7 - ... to flip the move for opponent.
                    JSONObject move = new JSONObject();
                    move.put("fromX", 7 - m.getFrom().getX());
                    move.put("fromY", 7 - m.getFrom().getY());
                    move.put("toX", 7 - m.getTo().getX());
                    move.put("toY", 7 - m.getTo().getY());
                    arrMoves.put(move);
                }
                obj.put("moves", arrMoves);
                socketHandler.emitData(obj);
            }
        } else {

            //this player is in checkmate, game is finished
            if (checkmate()) {
                finishGame(board.getTurn());
                return;
            }
            if (isTie()) {
                finishGame(null);
                return;
            }

            // Check if AI should do move
            aiMove();
        }
    }

    private void playMoveSound(IPiece captured) {
        if (captured instanceof Queen) {
            AudioManager.playScream();
        } else {
            AudioManager.playMoveSound();
        }
    }

    @Override
    public void illegalMovePerformed(int fromX, int fromY) {
        listener.illegalMovePerformed(fromX, fromY);
    }

    public String getGameOverString() {
        return gameOverString;
    }

    @Override
    public void onConnected() {

        // Join game!
        socketHandler.joinGame(gameInfo.getMultiplayerGame().getId(), gameInfo.getPlayer().getName());
    }


    @Override
    public void onData(JSONObject data) {
        String type = (String) data.get("type");
        switch (type) {
            case "moves":
                JSONArray moves = (JSONArray) data.get("moves");
                for (Object object : moves) {
                    JSONObject obj = (JSONObject) object;
                    int fromX = obj.getInt("fromX");
                    int fromY = obj.getInt("fromY");
                    int toX = obj.getInt("toX");
                    int toY = obj.getInt("toY");
                    board.move(fromX, fromY, toX, toY);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onState(GameState state) {
        switch (state.getMessage()) {
            case READY:

                // When ready state is called, we should receive an array with all playernames, where first string is player 1, or creator of the game.
                if (state.getData() instanceof ArrayList) {
                    ArrayList<String> players = (ArrayList<String>) state.getData();
                    String opponentName;
                    if (players.get(0).equals(gameInfo.getPlayer().getName())) {
                        opponentName = players.get(1);

                        // Player has created game and another player has joined => players turn.
                        board.setTurn(gameInfo.getPlayerColor());
                    } else {
                        opponentName = players.get(0);

                        // Player has joined game and other player is the host => opponents turn.
                        board.setTurn(gameInfo.getPlayerColor().getOpposite());
                    }
                    try {
                        gameInfo.setOpponent(Chess.getDatabase().getPlayer(opponentName));

                        // Start timer and set game status to ready!
                        turnTimer();
                        listener.multiplayerGameReady(opponentName);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case OPPONENT_RESIGN:
            case OPPONENT_DC:

                // Opponent either resigned or disonnected; either way we won!
                Gdx.app.postRunnable(() -> finishGame(gameInfo.getPlayerColor().getOpposite()));
                break;
            case UNKNOWN:
                System.out.println("Unknown game state received... check api logs");
                break;
        }
    }

    @Override
    public void onDisconnected() {

    }
}
