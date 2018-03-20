package game;

import boardstructure.Move;
import boardstructure.Square;
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
    private Texture chessMoveTexture, selectedPieceTexture;
    private Group checkerGroup, pieceGroup, highlightGroup;

    public Checkerboard(Stage stage, HashMap<String, Texture> sprites, ArrayList<Square> initialSquares, CheckerboardListener listener) {
        this.stage = stage;
        this.sprites = sprites;
        this.initialSquares = initialSquares;
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
        return (((boardY - boardImg.getY() - TOP_MARIGN) / SQUARE_HEIGHT) - 7) * (-1);
    }

    private Vector2 calcBoardCoords(Actor actor) {
        Vector2 vector2 = actor.localToStageCoordinates(new Vector2(0,0));
        int x = Math.round(calcScreenX((int)vector2.x));
        int y = Math.round(calcScreenY((int)vector2.y));
        return new Vector2(x, y);
    }

    private void initPieces() {
        pieceGroup = new Group();
        pieceGroup.setZIndex(2);

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
        stage.addActor(pieceGroup);
    }

    @Override
    public void dragStart(InputEvent event, float x, float y, int pointer) {
        Actor actor = event.getTarget();
        Vector2 v = calcBoardCoords(actor);
        actor.setName((int)v.x + "," + (int)v.y);
        listener.onDragPieceStarted((int)v.x, (int)v.y);
        super.dragStart(event, x, y, pointer);
    }

    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
        Actor actor = event.getTarget();
        actor.moveBy(x - actor.getWidth() / 2, y - actor.getHeight() / 2);
        super.drag(event, x, y, pointer);
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer) {
        Actor actor = event.getTarget();
        String[] posSplit = actor.getName().split(",");
        int oldX = Integer.parseInt(posSplit[0]);
        int oldY = Integer.parseInt(posSplit[1]);
        Vector2 newPos = calcBoardCoords(actor);

        if (newPos.x >= 0 && newPos.x < 8 && newPos.y >= 0 && newPos.y < 8) {
            listener.onMoveRequested(oldX, oldY, (int)newPos.x, (int)newPos.y);
        } else {
            actor.setPosition(calcBoardX(oldX), calcBoardY(oldY));
        }
        highlightGroup.clear();
        super.dragStop(event, x, y, pointer);
    }

    private void movePieceTo(Actor actor, int toX, int toY) {
        int newX = (int)calcBoardX(toX);
        int newY = (int)calcBoardY(toY);
        actor.setPosition(newX, newY);
        actor.setName(newX + "," + newY);
    }

    public void movePieceFailed(int fromX, int fromY) {
        Image from = pieceGroup.findActor(fromX + "," + fromY);
        movePieceTo(from, fromX, fromY);
    }

    public void movePieces(ArrayList<Move> moves) {
        for (Move m : moves) {
            Image from = pieceGroup.findActor(m.getFrom().getX() + "," + m.getFrom().getY());
            Image to = pieceGroup.findActor(m.getTo().getX() + "," + m.getTo().getY());
            if (to != null) {
                to.remove();
            }
            movePieceTo(from, m.getTo().getX(), m.getTo().getY());
        }
    }

    public void showMoves(ArrayList<Move> moves) {
        highlightGroup.clear();
        for (Move m : moves) {
            Image highlight = new Image(chessMoveTexture);
            float boardX = calcBoardX(m.getTo().getX());
            float boardY = calcBoardY(m.getTo().getY());
            highlight.setPosition(boardX, boardY);
            highlightGroup.addActor(highlight);
        }
    }
    
    public int getPos() {
    		return (int)boardImg.getX();
    }
    
    public int getSize() {
    		return (int)boardImg.getWidth();
    }
}