package scenes;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import db.Database;
import game.Chess;

import game.chessGame.GameInfo;
import game.GameType;
import game.WindowInformation;

import pieces.PieceColor;
import player.AILevel;

/**
 * 
 * This class initialize and manipulate the graphical elements in the main menu of the user interface.
 * 
 * @author sofia
 */

public class MainMenuScene extends AbstractScene {

    private static MainMenuScene instance;

    //necessary components
    private Chess game;
    private Skin skin;
    //graphical components
    private static final int defaultHeight = 55;
    private static final int defaultWidth = 250;
    private static final int centreWidth = (WindowInformation.WIDTH / 2) - (defaultWidth / 2);
    private ArrayList<Actor> actors;
    private Image imgBackground;
    private Label staticText, mainMenu, headerScore, error;
    private TextButton signIn, register, signUp, singleplayer, multiplayer, scores, startSingle,
            black, white, signInP2;
    private TextField username, registerUsername;
    private Button backToLogIn, backToChooseGame;
    private SelectBox<String> difficulty;
    private List<String> scoreList;
    private ScrollPane scorePane;
    //Navigation assistance
    private boolean playerOne;

    private GameInfo gameInfo;

    public MainMenuScene(Chess mainGame) {
        game = mainGame;
        playerOne = true;
        initialize();
    }

    public static MainMenuScene getInstance(Chess game) {
        if (instance == null)
            instance = new MainMenuScene(game);
        return instance;
    }

    @Override
    public void buildStage() {
        if (built) {
            screenGameMenu();
            Chess.getPlayerRegister().loadPlayers();
            if (gameInfo != null) {
                gameInfo.setPlayerColor(null);
                gameInfo.setOpponent(null);
                gameInfo.setLevel(null);
            }
        } else {
            built = true;
            setUpElements();
            setUpArrayList();
            setUpElementSizes();
            addListeners();
            addActorsToStage();
            screenSignIn();
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    //Section 1: Set up
    private void initialize() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.txt"));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);
    }

    private void setUpElements() {
        imgBackground = new Image(new Texture("pictures/menu.jpg"));
        imgBackground.setSize(WindowInformation.WIDTH, WindowInformation.HEIGHT);

        //Elements in log in
        signIn = new TextButton("Sign in", skin, "default");
        signIn.setPosition(centreWidth, WindowInformation.HEIGHT / 2.5f);
        staticText = new Label("Or if you haven't already,", skin, "optional");
        staticText.setPosition(centreWidth + 30, WindowInformation.HEIGHT / 3.1f);
        register = new TextButton("Register here", skin, "default");
        register.setPosition(centreWidth + 50, WindowInformation.HEIGHT / 3.4f);
        username = new TextField("testSpiller", skin, "default");
        username.setPosition(centreWidth, WindowInformation.HEIGHT / 2);

        //Elements in register
        backToLogIn = new Button(skin, "left");
        backToLogIn.setPosition(centreWidth / 3.8f, WindowInformation.HEIGHT / 1.2f);
        signUp = new TextButton("Sign up", skin, "default");
        signUp.setPosition(centreWidth, WindowInformation.HEIGHT / 2);
        registerUsername = new TextField(" desired username", skin, "default");
        registerUsername.setPosition(centreWidth, WindowInformation.HEIGHT / 2.5f);
        error = new Label("default", skin, "error");
        error.setPosition(centreWidth, WindowInformation.HEIGHT / 1.5f);

        //Elements in gamemenu
        mainMenu = new Label("Main menu", skin, "title-plain");
        mainMenu.setPosition((centreWidth + (defaultWidth / 4)), WindowInformation.HEIGHT / 1.6f);
        singleplayer = new TextButton("Singleplayer", skin, "default");
        singleplayer.setPosition(centreWidth, WindowInformation.HEIGHT / 2);
        multiplayer = new TextButton("Multiplayer", skin, "default");
        multiplayer.setPosition(centreWidth, WindowInformation.HEIGHT / 2.7f);
        scores = new TextButton("Highscore", skin, "default");
        scores.setPosition(centreWidth, WindowInformation.HEIGHT / 4);

        //Elements in preferences (singleplayer)
        startSingle = new TextButton("Start game", skin, "default");
        startSingle.setPosition(centreWidth, WindowInformation.HEIGHT / 3);
        difficulty = new SelectBox<String>(skin, "default");
        String[] options = {AILevel.EASY.toString(), AILevel.INTERMEDIATE.toString(), AILevel.HARD.toString()};
        difficulty.setItems(options);
        difficulty.setPosition(centreWidth, WindowInformation.HEIGHT / 2);
        black = new TextButton("Black", skin, "toggle");
        black.setPosition(centreWidth - (defaultWidth / 4), WindowInformation.HEIGHT / 1.6f);
        white = new TextButton("White", skin, "toggle");
        white.setPosition((centreWidth + (defaultWidth / 2)), WindowInformation.HEIGHT / 1.6f);
        white.toggle();

        //Elements in score screen
        headerScore = new Label("High scores:", skin, "title-plain");
        scoreList = new List<String>(skin);
        String[] temporary = new String[]{"1", "2", "3"};
        scoreList.setItems(temporary);
        scorePane = new ScrollPane(scoreList, skin, "default");
        scorePane.setPosition(centreWidth / 1.5f, WindowInformation.HEIGHT / 6);
        headerScore.setPosition(centreWidth / 1.5f + (defaultWidth / 2), WindowInformation.HEIGHT / 1.2f);

        //Elements in multiplayer
        signInP2 = new TextButton("Sign in", skin, "default");
        signInP2.setPosition(centreWidth, WindowInformation.HEIGHT / 2.5f);


        //Multiscreen
        backToChooseGame = new Button(skin, "left");
        backToChooseGame.setPosition(centreWidth / 3.8f, WindowInformation.HEIGHT / 1.2f);

    }


    private void setUpArrayList() {
        actors = new ArrayList<Actor>();
        actors.add(signIn);
        actors.add(username);
        actors.add(staticText);
        actors.add(register);
        actors.add(registerUsername);
        actors.add(backToLogIn);
        actors.add(backToChooseGame);
        actors.add(signUp);
        actors.add(singleplayer);
        actors.add(multiplayer);
        actors.add(scores);
        actors.add(mainMenu);
        actors.add(startSingle);
        actors.add(difficulty);
        actors.add(black);
        actors.add(white);
        actors.add(scorePane);
        actors.add(headerScore);
        actors.add(error);
    }

    private void setUpElementSizes() {
        for (Actor element : actors) {
            if (element instanceof TextButton || element instanceof TextField || element instanceof Label) {
                element.setSize(defaultWidth, defaultHeight);
            }
        }
        register.setSize(defaultWidth / 2, defaultHeight / 2);
        backToLogIn.setSize(27, 27);
        backToChooseGame.setSize(27, 27);
        difficulty.setSize(defaultWidth, defaultHeight / 1.5f);
        black.setSize(defaultWidth / 1.5f, defaultHeight / 1.5f);
        white.setSize(defaultWidth / 1.5f, defaultHeight / 1.5f);
        scorePane.setSize(defaultWidth * 1.6f, defaultHeight * 7);
    }


    private void addActorsToStage() {
        addActor(imgBackground);
        for (Actor element : actors) {
            addActor(element);
        }
    }

    //Section 2: ToggleRightScreens

    /**
     * Help method. Sets all elements on screen to invisible. Will be used by
     * screen methods that turn the correct elements back to visible.
     */
    private void invisible() {

        for (Actor element : actors) {
            element.setVisible(false);
        }
    }

    protected void screenSignIn() {
        invisible();
        signIn.setVisible(true);
        staticText.setVisible(true);
        register.setVisible(true);
        username.setVisible(true);
    }

    protected void screenRegister() {
        invisible();
        signUp.setVisible(true);
        backToLogIn.setVisible(true);
        registerUsername.setVisible(true);

    }

    protected void screenGameMenu() {
        invisible();
        playerOne = false;
        singleplayer.setVisible(true);
        mainMenu.setText("Main Menu");
        mainMenu.setVisible(true);
        multiplayer.setVisible(true);
        scores.setVisible(true);
    }

    protected void screenScore() {
        invisible();
        scorePane.setVisible(true);
        backToChooseGame.setVisible(true);
        headerScore.setVisible(true);
    }

    protected void screenPreferences() {
        invisible();
        startSingle.setVisible(true);
        difficulty.setVisible(true);
        backToChooseGame.setVisible(true);
        black.setVisible(true);
        white.setVisible(true);
    }

    private void screenMultiplayer() {
        invisible();
        signIn.setVisible(true);
        mainMenu.setText("Opponent");
        mainMenu.setVisible(true);
        staticText.setVisible(true);
        username.setText("testSpiller2");
        username.setVisible(true);
        registerUsername.setText(" desired username");
        register.setVisible(true);
        backToChooseGame.setVisible(true);
        playerOne = false;
    }

    //Section 3: Buttonlisteners

    /**
     * Will only be called upon initialization. Calling the button itself in each eventhandler
     * will reset the functionality of the button.
     * <p>
     * Remaining buttons:
     * <p>
     * multiplayer: goes to multiplayerScreen screen (= signIn + "Log in or register as player 2")
     * <p>
     * <p>
     * sign up button: If first player: True, if second -> false
     * <p>
     * textfield -> inputlistener
     */
    private void addListeners() {
        signInListener();
        registerListener();
        signUpListener();
        returnSignInListener();
        singleplayerListener();
        backToChooseGameListener();
        multiplayerListener();
        scoreListener();
        startSingleListener();
        blackListener();
        whiteListener();
    }

    private void startSingleListener() {
        startSingle.addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Chess.getPlayerRegister().loadPlayers(); // Force-reload.
                gameInfo.setLevel(AILevel.getAILevel(difficulty.getSelected()));
                gameInfo.setPlayerColor(white.isChecked() ? PieceColor.WHITE : PieceColor.BLACK);
                gameInfo.getPlayer().loadRating();
                if (gameInfo.getOpponent() != null) gameInfo.getOpponent().loadRating();
                SceneManager.getInstance().showScreen(SceneEnum.GAME, game, gameInfo);
                startSingleListener();
            }
        });
    }

    private void blackListener() {
        black.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                white.setChecked(false);
                blackListener();
            }
        });
    }

    private void whiteListener() {
        white.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                black.setChecked(false);
                whiteListener();
            }
        });
    }

    private void signInListener() {
        signIn.addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                error.setText("Alias not registered.");
                error.setVisible(false);

                if (playerOne) {
                    String name = username.getText().replaceAll("\\s+", "");;
                    Boolean exists = Chess.getPlayerRegister().playerIsRegistered(name);
                    if (exists) {
                        gameInfo = new GameInfo(Chess.getPlayerRegister().getPlayer(name));
                        screenGameMenu();
                        signInListener();
                    } else {
                        error.setVisible(true);
                        signInListener();
                        return;
                    }
                } else {
                    String name = username.getText().replaceAll("\\s+", "");;
                    Boolean exists = Chess.getPlayerRegister().playerIsRegistered(name);
                    
                    if(name.equals(gameInfo.getPlayer().getName())){
                    	error.setText("Already signed in");
                        error.setVisible(true);
                        signInListener();
                        return;
                    }
                    else if (exists) {
                        Chess.getPlayerRegister().loadPlayers(); // Force-reload.
                        gameInfo.setOpponent(Chess.getPlayerRegister().getPlayer(name));
                        gameInfo.setGameType(GameType.MULTIPLAYER);
                        gameInfo.setPlayerColor(PieceColor.WHITE);
                        gameInfo.getPlayer().loadRating();
                        SceneManager.getInstance().showScreen(SceneEnum.GAME, game, gameInfo);
                        signInListener();
                    } else {
                    	error.setText("Alias not registered.");
                        error.setVisible(true);
                        signInListener();
                        return;
                    }
                }
            }
        });
    }

    private void registerListener() {
        register.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                screenRegister();
                registerListener();
            }

        });
    }


    private void signUpListener() {
        signUp.addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                String name = registerUsername.getText().replaceAll("\\s+", "");;
                Boolean exists = Chess.getPlayerRegister().playerIsRegistered(name);
                    
                error.setText("This alias already exists! Please choose another one.");
                if (playerOne) {
                    if (exists) {
                        error.setVisible(true);
                    } else {
                        Chess.getPlayerRegister().registerPlayer(name);
                        screenSignIn();
                    }

                } else {

                    if (exists) {
                        error.setVisible(true);
                    } else {
                        Chess.getPlayerRegister().registerPlayer(name);
                        screenSignIn();
                    }
                }
                signUpListener();
            }
        });
    }

    private void returnSignInListener() {
        backToLogIn.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                screenSignIn();
                returnSignInListener();
            }
        });
    }

    private void singleplayerListener() {
        singleplayer.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                screenPreferences();
                singleplayerListener();
            }
        });
    }

    private void backToChooseGameListener() {
        backToChooseGame.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                screenGameMenu();
                backToChooseGameListener();
            }
        });
    }

    private void multiplayerListener() {
        multiplayer.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                screenMultiplayer();
                multiplayerListener();
            }
        });
    }

    private void scoreListener() {
        scores.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                Chess.getPlayerRegister().loadPlayers(); // Force-reload.
                ArrayList<String> highscores = Chess.getPlayerRegister().getHighscores();

                String[] scores = new String[highscores.size() + 1];
                scores[0] = "Name/Rating/W/L/D";
                for (int i = 0; i < highscores.size(); i++) {
                    scores[i + 1] = highscores.get(i);
                }
                scoreList.setItems(scores);
                screenScore();
                scoreListener();
            }
        });
    }
}