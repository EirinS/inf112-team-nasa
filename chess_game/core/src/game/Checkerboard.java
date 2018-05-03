package game;

import boardstructure.IBoard;
import boardstructure.Move;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import sound.AudioManager;

import boardstructure.MoveType;
import boardstructure.Square;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import game.listeners.CheckerboardListener;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import pieces.PieceColor;
import styling.Colors;
import sprites.SquareTextureLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class Checkerboard extends DragListener {

    private final int SQUARE_WIDTH = 55;
    private final int SQUARE_HEIGHT = 58;

    // Top and left margins from the actual checkerboard png.
    private final int TOP_MARIGN = 24;
    private final int LEFT_MARIGN = 38;

    private Stage stage;
    private HashMap<String, Texture> sprites;
    private ArrayList<Square> initialSquares;
    private CheckerboardListener listener;

    private Image boardImg;
    private Image selectedPiece;
    private Texture chessMoveTexture, specialMoveTexture, selectedPieceTexture, prevMoveTexture, captureTexture, hintTexture;
    private Group checkerGroup, pieceGroup, highlightPossibleMovesGroup, prevMovesGroup;

    private boolean enabled;

    public Checkerboard(Stage stage, HashMap<String, Texture> sprites, ArrayList<Square> initialSquares, CheckerboardListener listener) {
        this.stage = stage;
        this.sprites = sprites;
        this.initialSquares = initialSquares;
        this.listener = listener;
        addActors();
        enabled = true;
    }

    private void addActors() {

        // Contains board and pieces
        checkerGroup = new Group();

        boardImg = new Image(new Texture("board/checkerboard.png"));
        boardImg.setSize(512, 512);
        float margin = (WindowInformation.HEIGHT - boardImg.getHeight()) / 2;
        boardImg.setPosition(margin, margin);
        checkerGroup.addActor(boardImg);
        stage.addActor(checkerGroup);

        getAllSquareTextures();

        selectedPiece = new Image(selectedPieceTexture);
        selectedPiece.setZIndex(1);
        selectedPiece.setVisible(false);
        stage.addActor(selectedPiece);

        prevMovesGroup = new Group();
        prevMovesGroup.setZIndex(2);
        stage.addActor(prevMovesGroup);

        highlightPossibleMovesGroup = new Group();
        highlightPossibleMovesGroup.setZIndex(3);
        stage.addActor(highlightPossibleMovesGroup);

        pieceGroup = new Group();
        pieceGroup.setZIndex(5);
        initPieces();
        stage.addActor(pieceGroup);
    }

    /**
     * Loads all the squareTextures into the game.
     */
    private void getAllSquareTextures() {
        chessMoveTexture = getTexture(Colors.chessMoveColor);
        specialMoveTexture = getTexture(Colors.specialMoveColor);
        selectedPieceTexture = getTexture(Colors.selectedPieceColor);
        prevMoveTexture = getTexture(Colors.prevMoveColor);
        captureTexture = getTexture(Colors.captureColor);
        hintTexture = getTexture(Colors.hintColor);
    }

    /**
     * Get a texture
     *
     * @param color
     * @return the texture of given color.
     */
    private Texture getTexture(Color color) {
        return SquareTextureLoader.createSquare(SQUARE_WIDTH, SQUARE_HEIGHT, color);
    }

    private float calcBoardX(float squareX) {
        return boardImg.getX() + LEFT_MARIGN + squareX * SQUARE_WIDTH;
    }

    private float calcScreenX(float boardX) {
        return (boardX - boardImg.getX() - LEFT_MARIGN) / SQUARE_WIDTH;
    }

    private float calcBoardY(float squareY) {
        return boardImg.getY() + TOP_MARIGN + (7 - squareY) * SQUARE_HEIGHT;
    }

    private float calcScreenY(float boardY) {
        return (((boardY - boardImg.getY() - TOP_MARIGN) / SQUARE_HEIGHT) - 7) * (-1);
    }

    private Vector2 calcBoardCoords(Actor actor) {
        Vector2 vector2 = actor.localToStageCoordinates(new Vector2(0, 0));
        int x = Math.round(calcScreenX(vector2.x));
        int y = Math.round(calcScreenY(vector2.y));
        return new Vector2(x, y);
    }

    private void initPieces() {
        for (Square square : initialSquares) {
            if (square.getPiece() == null) continue;
            String pieceColor = square.getPiece().getColor() == PieceColor.WHITE ? "w" : "b";

            Texture texture = sprites.get(pieceColor + square.getPiece().toString().toLowerCase());
            Image img = new Image(texture);
            img.setSize(54, 54);
            img.setPosition(calcBoardX(square.getX()), calcBoardY(square.getY()));
            img.setName(square.getX() + "," + square.getY());
            img.addListener(this);
            pieceGroup.addActor(img);
        }
    }

    public void setThisCheckerBoard(IBoard board) {
        pieceGroup.clear();
        initialSquares = board.getSquares();
        prevMovesGroup.clear();
        initPieces();
    }

    @Override
    public void dragStart(InputEvent event, float x, float y, int pointer) {

        // If checkerboard is not enabled, deny move.
        if (!enabled) {
            return;
        }
        Actor actor = event.getTarget();
        Vector2 v = calcBoardCoords(actor);
        actor.setName((int) v.x + "," + (int) v.y);
        listener.onDragPieceStarted((int) v.x, (int) v.y);
        super.dragStart(event, x, y, pointer);
    }

    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
        if (!enabled) {
            return;
        }
        Actor actor = event.getTarget();
        actor.moveBy(x - actor.getWidth() / 2, y - actor.getHeight() / 2);
        super.drag(event, x, y, pointer);
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer) {// If checkerboard is not enabled, deny move.
        Actor actor = event.getTarget();
        String[] posSplit = actor.getName().split(",");
        int oldX = Integer.parseInt(posSplit[0]);
        int oldY = Integer.parseInt(posSplit[1]);
        Vector2 newPos = calcBoardCoords(actor);

        if (newPos.x >= 0 && newPos.x < 8 && newPos.y >= 0 && newPos.y < 8) {
            listener.onMoveRequested(oldX, oldY, (int) newPos.x, (int) newPos.y);
        } else {
            actor.setPosition(calcBoardX(oldX), calcBoardY(oldY));
        }
        highlightPossibleMovesGroup.clear();

        super.dragStop(event, x, y, pointer);
    }

    private void movePieceTo(Actor actor, int toX, int toY, MoveType moveType, PieceColor color, boolean animate) {
        int boardX = (int) calcBoardX(toX);
        int boardY = (int) calcBoardY(toY);
        if (animate) {
            actor.addAction(Actions.moveTo(boardX, boardY, .25f));
        } else {
            actor.setPosition(boardX, boardY);
        }
        actor.setName(toX + "," + toY);
        if (moveType != null && moveType == MoveType.PROMOTION) {
            Texture queenTexture = sprites.get((color == PieceColor.WHITE ? "w" : "b") + moveType.getMetadata());
            ((Image) actor).setDrawable(new SpriteDrawable(new Sprite(queenTexture)));
        }
    }

    public void movePieceFailed(int fromX, int fromY, boolean animate) {
        Image from = pieceGroup.findActor(fromX + "," + fromY);
        movePieceTo(from, fromX, fromY, null, null, animate);
    }

    public void movePieces(ArrayList<Move> moves, boolean animate) {
        for (Move m : moves) {
            Image from = pieceGroup.findActor(m.getFrom().getX() + "," + m.getFrom().getY());
            Image to = pieceGroup.findActor(m.getTo().getX() + "," + m.getTo().getY());
            if (to != null) {
                to.remove();
            }
            if (m.getMoveType() == MoveType.ENPASSANT) {
                int x = m.getTo().getX();
                int y = m.getFrom().getY();
                pieceGroup.findActor(x + "," + y).remove();
            }
            movePieceTo(from, m.getTo().getX(), m.getTo().getY(), m.getMoveType(), m.getMovingPiece().getColor(), animate);
        }
    }

    /**
     * Shows the previous move
     *
     * @param m, the move that was made.
     */
    public void showPrevMove(Move m) {
        if (!enabled) return;

        prevMovesGroup.clear();
        Image highlightFrom = new Image(prevMoveTexture);
        Image highlightTo = new Image(prevMoveTexture);

        highlightTo.setPosition(calcBoardX(m.getTo().getX()), calcBoardY(m.getTo().getY()));
        prevMovesGroup.addActor(highlightTo);

        highlightFrom.setPosition(calcBoardX(m.getFrom().getX()), calcBoardY(m.getFrom().getY()));
        prevMovesGroup.addActor(highlightFrom);
    }

    /**
     * Shows a move-hint for user on the board.
     *
     * @param m, the move you'll highlight
     */
    public void showHint(Move m) {
        if (!enabled) return;

        highlightPossibleMovesGroup.clear();
        Texture texture = hintTexture;

        Image highlightTo = new Image(texture);
        highlightTo.setPosition(calcBoardX(m.getTo().getX()), calcBoardY(m.getTo().getY()));
        highlightPossibleMovesGroup.addActor(highlightTo);

        Image highlightFrom = new Image(texture);
        highlightFrom.setPosition(calcBoardX(m.getFrom().getX()), calcBoardY(m.getFrom().getY()));
        highlightPossibleMovesGroup.addActor(highlightFrom);

        AudioManager.playHintSound();
    }

    public void showMoves(ArrayList<Move> moves) {
        if (!enabled) return;

        highlightPossibleMovesGroup.clear();
        Texture texture;
        for (Move m : moves) {
            if (m.getCapturedPiece() != null) {
                texture = captureTexture;
            } else if (m.getMoveType() != MoveType.REGULAR && m.getMoveType() != MoveType.PAWNJUMP) {
                texture = specialMoveTexture;
            } else
                texture = chessMoveTexture;
            Image highlight = new Image(texture);
            float boardX = calcBoardX(m.getTo().getX());
            float boardY = calcBoardY(m.getTo().getY());
            highlight.setPosition(boardX, boardY);
            highlightPossibleMovesGroup.addActor(highlight);
        }
    }

    public int getPos() {
        return (int) boardImg.getX();
    }

    public int getSize() {
        return (int) boardImg.getWidth();
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}