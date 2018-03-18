package game;

import boardstructure.Square;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import pieces.PieceColor;

public class Checkerboard {

    private final int SQUARE_COUNT = 8;
    private final int SQUARE_WIDTH = 55;
    private final int SQUARE_HEIGHT = 58;
    private final int BOARD_SIZE = SQUARE_WIDTH * SQUARE_HEIGHT * SQUARE_COUNT;

    // Top and left margins from the actual checkerboard png.
    private final int TOP_MARIGN = 23;
    private final int LEFT_MARIGN = 38;

    private Chess game;
    private Stage stage;
    private GameInfo gameInfo;
    private CheckerboardListener listener;

    private Image boardImg;
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

        highlightGroup = new Group();
        stage.addActor(highlightGroup);
    }

    private void initPieces() {
        pieceGroup = new Group();
        pieceGroup.setZIndex(2);
        turn = gameInfo.getPlayerColor();

        for (Square square : gameInfo.getSquares()) {
            if (square.getPiece() == null) continue;
            String pieceColor = square.getPiece().getColor() == PieceColor.WHITE ? "w" : "b";

            Texture texture = gameInfo.getSprites().get(pieceColor + square.getPiece().toString().toLowerCase());
            final Image img = new Image(texture);
            img.setSize(54, 54);
            img.setPosition(boardImg.getX() + LEFT_MARIGN + square.getX() * SQUARE_WIDTH, boardImg.getY() + TOP_MARIGN + square.getY() * SQUARE_HEIGHT);
            img.setName("" + square.getX() + square.getY());
            img.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (event.getButton() == 0) {
                        listener.onPieceClick(Integer.parseInt("" + img.getName().charAt(0)), Integer.parseInt("" + img.getName().charAt(1)));
                    }
                }
            });

            pieceGroup.addActor(img);
        }
        stage.addActor(pieceGroup);
    }
}