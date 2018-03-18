package game;

import boardstructure.Move;
import boardstructure.Square;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import pieces.PieceColor;
import styling.Colors;
import sprites.SquareTextureLoader;

import java.util.ArrayList;

public class Checkerboard {

    private final int SQUARE_COUNT = 8;
    private final int SQUARE_WIDTH = 55;
    private final int SQUARE_HEIGHT = 58;

    // Top and left margins from the actual checkerboard png.
    private final int TOP_MARIGN = 24;
    private final int LEFT_MARIGN = 38;

    private Chess game;
    private Stage stage;
    private GameInfo gameInfo;
    private CheckerboardListener listener;

    private Image boardImg;
    private Image selectedPiece;
    private Texture chessMoveTexture, selectedPieceTexture;
    private Group checkerGroup, pieceGroup, highlightGroup;

    private PieceColor turn;

    public Checkerboard(Chess game, Stage stage, GameInfo gameInfo, CheckerboardListener listener) {
        this.game = game;
        this.stage = stage;
        this.gameInfo = gameInfo;
        this.listener = listener;
        addActors();
    }

    private void addActors() {

        // Contains background, board and pieces
        checkerGroup = new Group();

        Image imgBackground = new Image(new Texture("board/game_bg.png"));
        imgBackground.setSize(WindowInformation.WIDTH, WindowInformation.HEIGHT);
        checkerGroup.addActor(imgBackground);

        boardImg = new Image(new Texture("board/checkerboard.png"));
        boardImg.setSize(512, 512);
        float margin = (WindowInformation.HEIGHT - boardImg.getHeight()) / 2;
        boardImg.setPosition(margin, margin);
        checkerGroup.addActor(boardImg);
        stage.addActor(checkerGroup);

        initPieces();

        chessMoveTexture = SquareTextureLoader.createSquare(SQUARE_WIDTH, SQUARE_HEIGHT, Colors.chessMoveColor);
        selectedPieceTexture = SquareTextureLoader.createSquare(SQUARE_WIDTH, SQUARE_HEIGHT, Colors.selectedPieceColor);

        selectedPiece = new Image(selectedPieceTexture);
        selectedPiece.setZIndex(1);
        selectedPiece.setVisible(false);
        stage.addActor(selectedPiece);

        highlightGroup = new Group();
        pieceGroup.setZIndex(3);
        stage.addActor(highlightGroup);
    }

    private float calcBoardX(int squareX) {
        return boardImg.getX() + LEFT_MARIGN + squareX * SQUARE_WIDTH;
    }

    private float calcScreenX(int boardX) {
        return (boardX - boardImg.getX() - LEFT_MARIGN) / SQUARE_WIDTH;
    }

    private float calcBoardY(int squareY) {
        return boardImg.getY() + TOP_MARIGN + (7 - squareY) * SQUARE_HEIGHT;
    }

    private float calcScreenY(int boardY) {
        return ((boardY - boardImg.getX() - LEFT_MARIGN) / SQUARE_WIDTH - 7) * (-1);
    }

    private void initPieces() {
        pieceGroup = new Group();
        pieceGroup.setZIndex(2);

        for (Square square : gameInfo.getSquares()) {
            if (square.getPiece() == null) continue;
            String pieceColor = square.getPiece().getColor() == PieceColor.WHITE ? "w" : "b";

            Texture texture = gameInfo.getSprites().get(pieceColor + square.getPiece().toString().toLowerCase());
            Image img = new Image(texture);
            img.setSize(54, 54);
            img.setPosition(calcBoardX(square.getX()), calcBoardY(square.getY()));
            img.setName("" + square.getX() + square.getY());
            img.addListener(new ClickListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    super.touchDragged(event, x, y, pointer);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                }

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getButton() != 0) return; // Force left-click.
                    System.out.println(x + ", " + y);
                    Vector2 vector2 = img.localToStageCoordinates(new Vector2(x, y));
                    listener.onPieceClick((int)calcScreenX((int)vector2.x), (int)calcScreenY(54-(int)vector2.y));
                }
            });

            pieceGroup.addActor(img);
        }
        stage.addActor(pieceGroup);
    }

    public void showMoves(Square selectedSquare, ArrayList<Move> moves) {
        if (selectedSquare == null && moves == null) {
            selectedPiece.setVisible(false);
            highlightGroup.clear();
            return;
        }
        selectedPiece.setPosition(calcBoardX(selectedSquare.getX()), calcBoardY(selectedSquare.getY()));
        selectedPiece.setVisible(true);
        highlightGroup.clear();
        for (Move m : moves) {
            Image highlight = new Image(chessMoveTexture);
            float boardX = calcBoardX(m.getTo().getX());
            float boardY = calcBoardY(m.getTo().getY());
            highlight.setPosition(boardX, boardY);
            highlight.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getButton() != 0) return; // Force left-click.
                    listener.onMoveRequested(m);
                }
            });
            highlightGroup.addActor(highlight);
        }
    }

    public void movePiece(Move move) {
        Image from = pieceGroup.findActor("" + move.getFrom().getX() + move.getFrom().getY());
        Image to = pieceGroup.findActor("" + move.getTo().getX() + move.getTo().getY());
        if (to != null) {
            to.remove();
        }
        from.setPosition(calcBoardX(move.getTo().getX()), calcBoardY(move.getTo().getY()));
    }
}