package scenes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import animation.CustomAnimation;
import game.Chess;

import game.chessGame.GameInfo;
import game.chessGame.GameType;
import game.WindowInformation;

import models.MultiplayerGame;
import multiplayer.IMultiplayer;
import multiplayer.Multiplayer;
import multiplayer.MultiplayerListener;
import pieces.PieceColor;
import player.AILevel;
import db.Player;
import socket.SocketHandler;
import socket.SocketHandlerListener;

/**
 * This class initialize and manipulate the graphical elements in the main menu
 * of the user interface.
 *
 * @author sofia
 */

public class MainMenuScene extends AbstractScene {

    private static MainMenuScene instance;

    // necessary components
    private Chess game;
    private Skin skin;
    // graphical components
    private static final int defaultHeight = 55;
    private static final int defaultWidth = 250;
    private static final int centreWidth = (WindowInformation.WIDTH / 2) - (defaultWidth / 2);
    private ArrayList<Actor> actors;
    private Image imgBackground;
    private Label staticText;
    private Label mainMenu;
    private Label headerScore;
    private Label error;
    private Label headerMulti;
    private Label onlineName;
    private TextButton signIn, register, signUp, singleplayerBtn, multiplayerBtn, scores, startSingle, black, white,
            signInP2, multiOpponent, chooseOnline, chooseOffline, createGame, joinGame, createGameBtn;
    private TextField username, registerUsername, enterOnlineName;
    private Button backToLogIn, backToChooseGame;
    private SelectBox<String> difficulty, gameType, multiplayerOption;
    private ScrollPane scorePane, onlinePane;
    private Window window, onlineWindow;
    private VerticalGroup scoreGroup;
    private List<String> onlineList;

    // Navigation assistance
    private boolean playerOne;

    private GameInfo gameInfo;

    // Online multiplayer stuff
    private IMultiplayer multiplayer;
    private ArrayList<MultiplayerGame> multiplayerGames;

    /**
     * The constructor of the main menu scene.
     *
     * @param mainGame The chess game application instance
     */
    public MainMenuScene(Chess mainGame) {
        game = mainGame;
        playerOne = true;
        initialize();
    }

    /**
     * A get method for the chess game. Creates an instance if one does not already
     * exist.
     *
     * @param game The chess game application.
     * @return This instance of the main menu scene.
     */
    public static MainMenuScene getInstance(Chess game) {
        if (instance == null)
            instance = new MainMenuScene(game);
        return instance;
    }

    /**
     * Builds the stage upon where the graphical elements are drawn. Sets up the
     * elements. If it already exists (e.g. if a player wants to play another game
     * or resigns) the options of the menu are reset.
     */
    @Override
    public void buildStage() {
        if (built) {
            screenGameMenu();
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

    /**
     * Continuously updates the graphical user interface.
     */
    @Override
    public void render(float delta) {
        super.render(delta);
    }

    // Section 1: Set up

    /**
     * Initializes the graphical style texture from the assets folder.
     */
    private void initialize() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.txt"));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);
        initMultiplayer();
    }

    private void initMultiplayer() {
        multiplayer = new Multiplayer(new MultiplayerListener() {

            @Override
            public void gamesListed(java.util.List<MultiplayerGame> games) {
                multiplayerGames = new ArrayList<>();
                multiplayerGames.addAll(games);
                addMultiplayerActors();
            }

            @Override
            public void gameCreated(MultiplayerGame multiplayerGame) {
                System.out.println("Game created!");

                // We have created a game, start GameScene!
                gameInfo.setIsOnline(true);
                gameInfo.setMultiplayerGame(multiplayerGame);
                gameInfo.setPlayerColor(multiplayerGame.getPlayer().getColor());
                gameInfo.getPlayer().loadRating();
                gameInfo.setGameType(GameType.getGameType(multiplayerGame.getType()));
                Gdx.app.postRunnable(() -> SceneManager.getInstance().showScreen(SceneEnum.GAME, game, gameInfo));
            }

            @Override
            public void gameJoined(MultiplayerGame multiplayerGame) {
                System.out.println("Game joined!");

                // We have successfully joined a game, start GameScene!
                gameInfo.setIsOnline(true);
                gameInfo.setMultiplayerGame(multiplayerGame);
                gameInfo.setPlayerColor(multiplayerGame.getPlayer().getColor().getOpposite());
                gameInfo.getPlayer().loadRating();
                gameInfo.setGameType(GameType.getGameType(multiplayerGame.getType()));
                Gdx.app.postRunnable(() -> SceneManager.getInstance().showScreen(SceneEnum.GAME, game, gameInfo));
            }

            @Override
            public void error(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void unexpectedError() {
                System.out.println("Multiplayer: Unexpected error occured...");
            }
        });
    }

    private void addMultiplayerActors() {
        String[] strs = new String[multiplayerGames.size()];
        for (int i = 0; i < multiplayerGames.size(); i++) {
            strs[i] = multiplayerGames.get(i).toString();
        }
        onlineList.setItems(strs);
    }

    /**
     * Sets the backround and initialized and sets up the elements and their
     * positions.
     */
    private void setUpElements() {
        imgBackground = new Image(new Texture("pictures/menu.jpg"));
        imgBackground.setSize(WindowInformation.WIDTH, WindowInformation.HEIGHT);

        // Elements in log in
        signIn = new TextButton("Sign in", skin, "default");
        signIn.setPosition(centreWidth, WindowInformation.HEIGHT / 2.5f);
        staticText = new Label("Or if you haven't already,", skin, "optional");
        staticText.setPosition(centreWidth + 30, WindowInformation.HEIGHT / 3.1f);
        register = new TextButton("Register here", skin, "default");
        register.setPosition(centreWidth + 50, WindowInformation.HEIGHT / 3.4f);
        username = new TextField("testSpiller", skin, "default");
        username.setPosition(centreWidth, WindowInformation.HEIGHT / 2);

        // Elements in register
        backToLogIn = new Button(skin, "left");
        backToLogIn.setPosition(centreWidth / 3.8f, WindowInformation.HEIGHT / 1.2f);
        signUp = new TextButton("Sign up", skin, "default");
        signUp.setPosition(centreWidth, WindowInformation.HEIGHT / 2);
        registerUsername = new TextField(" desired username", skin, "default");
        registerUsername.setPosition(centreWidth, WindowInformation.HEIGHT / 2.5f);
        error = new Label("default", skin, "error");
        error.setPosition(centreWidth, WindowInformation.HEIGHT / 1.5f);

        // Elements in gamemenu
        mainMenu = new Label("Main menu", skin, "title-plain");
        mainMenu.setPosition((centreWidth + (defaultWidth / 4)), WindowInformation.HEIGHT / 1.6f);
        singleplayerBtn = new TextButton("Singleplayer", skin, "default");
        singleplayerBtn.setPosition(centreWidth, WindowInformation.HEIGHT / 2);
        multiplayerBtn = new TextButton("Multiplayer", skin, "default");
        multiplayerBtn.setPosition(centreWidth, WindowInformation.HEIGHT / 2.7f);
        scores = new TextButton("Highscore", skin, "default");
        scores.setPosition(centreWidth, WindowInformation.HEIGHT / 4);

        // Elements in preferences (singleplayerBtn)
        startSingle = new TextButton("Start game", skin, "default");
        startSingle.setPosition(centreWidth, WindowInformation.HEIGHT / 3.5f);

        difficulty = new SelectBox<String>(skin, "default");
        String[] optionsDifficulty = {AILevel.EASY.toString(), AILevel.INTERMEDIATE.toString(),
                AILevel.HARD.toString()};
        difficulty.setItems(optionsDifficulty);
        difficulty.setPosition(centreWidth, WindowInformation.HEIGHT / 2);

        gameType = new SelectBox<String>(skin, "default");
        String[] optionsGameType = {GameType.REGULAR.toString(), GameType.CHESS960.toString(),
                GameType.BULLET.toString(), GameType.BLITZ.toString(), GameType.RAPID.toString()};
        gameType.setItems(optionsGameType);
        gameType.setPosition(centreWidth, WindowInformation.HEIGHT / 2.4f);

        black = new TextButton("Black", skin, "toggle");
        black.setPosition(centreWidth - (defaultWidth / 4), WindowInformation.HEIGHT / 1.6f);
        white = new TextButton("White", skin, "toggle");
        white.setPosition((centreWidth + (defaultWidth / 2)), WindowInformation.HEIGHT / 1.6f);
        white.toggle();

        // Elements in score screen
        headerScore = new Label("High scores:", skin, "title-plain");
        // gamesList = new List<Actor>(skin);
        // scorePane = new ScrollPane(gamesList, skin, "default");
        scoreGroup = new VerticalGroup();
        scorePane = new ScrollPane(scoreGroup, skin, "default");
        scorePane.setPosition(defaultWidth / 1.7f, WindowInformation.HEIGHT / 13);
        headerScore.setPosition(centreWidth + (centreWidth / 3), WindowInformation.HEIGHT / 1.2f);

        Label name = new Label("Name", skin, "title");
        Label rating = new Label("Rating", skin, "title");
        Label win = new Label("Wins", skin, "title");
        Label lose = new Label("Losses", skin, "title");
        Label draw = new Label("Draws", skin, "title");
        window = new Window("", skin);
        window.add(name);
        window.add(rating);
        window.add(win);
        window.add(lose);
        window.add(draw);
        window.setPosition(defaultWidth / 1.7f, defaultHeight * 7.5f);
        window.setMovable(false);

        // Elements in multiplayerBtn

        // Screen one
        chooseOnline = new TextButton("Online", skin, "default");
        chooseOnline.setPosition(centreWidth, WindowInformation.HEIGHT / 1.9f);
        chooseOffline = new TextButton("Offline", skin, "default");
        chooseOffline.setPosition(centreWidth, WindowInformation.HEIGHT / 2.2f);

        // Screen two
        multiplayerOption = new SelectBox<String>(skin);
        multiplayerOption.setItems(optionsGameType);
        multiplayerOption.setPosition(centreWidth, WindowInformation.HEIGHT / 1.9f);
        multiOpponent = new TextButton("Next", skin, "default");
        multiOpponent.setPosition(centreWidth, WindowInformation.HEIGHT / 2.5f);

        // Screen three
        signInP2 = new TextButton("Sign in", skin, "default");
        signInP2.setPosition(centreWidth, WindowInformation.HEIGHT / 2.5f);

        // Multiscreen
        backToChooseGame = new Button(skin, "left");
        backToChooseGame.setPosition(centreWidth / 3.8f, WindowInformation.HEIGHT / 1.2f);

        headerMulti = new Label("Online", skin, "title-plain");
        headerMulti.setPosition(centreWidth + (centreWidth / 3), WindowInformation.HEIGHT / 1.2f);

        createGame = new TextButton("Create online game", skin, "default");
        createGame.setPosition(centreWidth - createGame.getWidth(), WindowInformation.HEIGHT / 6f);

        joinGame = new TextButton("Join game", skin, "default");
        joinGame.setPosition(centreWidth + createGame.getWidth(), WindowInformation.HEIGHT / 6f);

        Label lName = new Label("Name", skin, "title");
        Label type = new Label("Game type", skin, "title");
        Label opponent = new Label("Opponent name", skin, "title");
        Label ranking = new Label("Ranking", skin, "title");

        onlineWindow = new Window("", skin);
        onlineWindow.add(lName);
        onlineWindow.add(type);
        onlineWindow.add(opponent);
        onlineWindow.add(ranking);
        onlineWindow.setPosition(defaultWidth / 2.3f, defaultHeight * 7.5f);
        onlineWindow.setMovable(false);
        onlineWindow.setSize(defaultWidth * 2.3f + 40, defaultHeight * 1.8f);

        onlineList = new List<>(skin);
        onlinePane = new ScrollPane(onlineList, skin, "default");
        onlinePane.setPosition(defaultWidth / 2.3f + 10, WindowInformation.HEIGHT / 13 + 150);

        //create online game screen
        enterOnlineName = new TextField("Lobby navn", skin, "default");
        enterOnlineName.setPosition(centreWidth, WindowInformation.HEIGHT / 2);

        onlineName = new Label("Enter name: ", skin, "title-plain");
        onlineName.setPosition(centreWidth, WindowInformation.HEIGHT / 2 + enterOnlineName.getHeight() + 20);

        createGameBtn = new TextButton("Create", skin, "default");
        createGameBtn.setPosition(centreWidth, WindowInformation.HEIGHT / 4);
    }

    /**
     * A private helping method. Puts all the actors of the stage in an array so
     * that some aspects of setting them up can be automated.
     */
    private void setUpArrayList() {
        actors = new ArrayList<Actor>();
        actors.add(signIn);
        actors.add(username);
        actors.add(staticText);
        actors.add(register);
        actors.add(registerUsername);
        actors.add(backToLogIn);
        actors.add(backToChooseGame);
        actors.add(headerMulti);
        actors.add(createGame);
        actors.add(joinGame);
        actors.add(onlineWindow);
        actors.add(signUp);
        actors.add(singleplayerBtn);
        actors.add(multiplayerBtn);
        actors.add(scores);
        actors.add(mainMenu);
        actors.add(startSingle);
        actors.add(difficulty);
        actors.add(gameType);
        actors.add(black);
        actors.add(white);
        actors.add(scorePane);
        actors.add(headerScore);
        actors.add(error);
        actors.add(window);
        actors.add(chooseOnline);
        actors.add(chooseOffline);
        actors.add(multiplayerOption);
        actors.add(multiOpponent);
        actors.add(onlinePane);
        actors.add(onlineName);
        actors.add(enterOnlineName);
        actors.add(createGameBtn);
    }

    /**
     * Private helping method. Uses the array to set up sizes of the elements. Some
     * are set up manually.
     */
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
        chooseOnline.setSize(defaultWidth, defaultHeight / 1.5f);
        chooseOffline.setSize(defaultWidth, defaultHeight / 1.5f);
        multiplayerOption.setSize(defaultWidth, defaultHeight / 1.5f);
        gameType.setSize(defaultWidth, defaultHeight / 1.5f);
        black.setSize(defaultWidth / 1.5f, defaultHeight / 1.5f);
        white.setSize(defaultWidth / 1.5f, defaultHeight / 1.5f);
        onlinePane.setSize(defaultWidth * 2.3f, defaultHeight * 4);
        scorePane.setSize(defaultWidth * 2.3f, defaultHeight * 7);
        window.setSize(defaultWidth * 2.3f, defaultHeight * 1.8f);
    }

    /**
     * Private helping method. Uses the array to add all the actors to the stage.
     */
    private void addActorsToStage() {
        addActor(imgBackground);
        for (Actor element : actors) {
            addActor(element);
        }
    }

    // Section 2: ToggleRightScreens

    private void playLoadingAnimation(){
        //CustomAnimation animation = new CustomAnimation(game, gameInfo, "pictures/loading.png", 3, 474, 717, 5, 8, 10);
    }

    /**
     * Help method. Sets all elements on screen to invisible. Will be used by screen
     * methods that turn the correct elements back to visible.
     */
    private void invisible() {

        for (Actor element : actors) {
            element.setVisible(false);
        }
    }

    /**
     * Displays the screen where one signs in.
     */
    protected void screenSignIn() {
        invisible();
        signIn.setVisible(true);
        staticText.setVisible(true);
        register.setVisible(true);
        username.setVisible(true);
    }

    /**
     * Displays the screen where one registers as a new player.
     */
    protected void screenRegister() {
        invisible();
        signUp.setVisible(true);
        backToLogIn.setVisible(true);
        registerUsername.setVisible(true);

    }

    protected void screenCreateGame() {
        invisible();
        backToChooseGame.setVisible(true);
        gameType.setVisible(true);
        onlineName.setVisible(true);
        createGameBtn.setVisible(true);
        enterOnlineName.setVisible(true);
    }

    /**
     * Displays the screen of the main menu.
     */
    protected void screenGameMenu() {
        invisible();
        playerOne = false;
        singleplayerBtn.setVisible(true);
        mainMenu.setText("Main Menu");
        mainMenu.setVisible(true);
        multiplayerBtn.setVisible(true);
        scores.setVisible(true);
    }

    /**
     * Displays the screen of the score system.
     */
    protected void screenScore() {
        invisible();
        scorePane.setVisible(true);
        backToChooseGame.setVisible(true);
        headerScore.setVisible(true);
        window.setVisible(true);
    }

    /**
     * Displays the screen of the singleplayerBtn where one can choose one's
     * preferences.
     */
    protected void screenPreferences() {
        invisible();
        startSingle.setVisible(true);
        difficulty.setVisible(true);
        gameType.setVisible(true);
        backToChooseGame.setVisible(true);
        black.setVisible(true);
        white.setVisible(true);
    }

    /**
     * Displays the screen of the opponent multiplayerBtn.
     */
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

    /**
     * Displays the screen where the first player in a multiplayerBtn game chooses
     * between online/offline.
     */
    private void screenMultiplayerOption() {
        invisible();
        mainMenu.setText("Game type");
        mainMenu.setVisible(true);
        chooseOnline.setVisible(true);
        chooseOffline.setVisible(true);
    }

    /**
     * Displays the screen where the first player in a multiplayerBtn game chooses
     * what game type to play.
     */
    private void screenMultiplayerOnlineOption() {
        invisible();
        backToChooseGame.setVisible(true);
        headerMulti.setVisible(true);
        createGame.setVisible(true);
        onlinePane.setVisible(true);
        joinGame.setVisible(true);
        onlineWindow.setVisible(true);
        multiplayer.listGames();
    }

    /**
     * Displays the screen where the first player in a multiplayerBtn game chooses
     * what game type to play.
     */
    private void screenMultiplayerOfflineOption() {
        invisible();
        mainMenu.setText("Game type");
        mainMenu.setVisible(true);
        multiplayerOption.setVisible(true);
        backToChooseGame.setVisible(true);
        multiOpponent.setVisible(true);
    }

    // Section 3: Buttonlisteners

    /**
     * Initializes the buttons. Will only be called upon initialization. Calling the
     * button itself in each eventhandler will reset the functionality of the
     * button.
     */
    private void addListeners() {
        signInListener();
        registerListener();
        signUpListener();
        returnSignInListener();
        singleplayerListener();
        backToChooseGameListener();
        multiplayerOpponentListener();
        multiplayerOptionListener();
        onlineOptionListener();
        offlineOptionListener();
        scoreListener();
        startSingleListener();
        blackListener();
        whiteListener();
        multiplayerOpponentListener();
        createGameListener();
        createGameBtnListener();
        joinGameListener();
    }

    /**
     * Adds an action event to the start singleplayerBtn game button.
     */
    private void startSingleListener() {
        startSingle.addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                gameInfo.setLevel(AILevel.getAILevel(difficulty.getSelected()));
                gameInfo.setGameType(GameType.getGameType(gameType.getSelected()));
                gameInfo.setPlayerColor(white.isChecked() ? PieceColor.WHITE : PieceColor.BLACK);
                gameInfo.getPlayer().loadRating();
                gameInfo.setSinglePlayer(true);
                if (gameInfo.getOpponent() != null)
                    gameInfo.getOpponent().loadRating();
                SceneManager.getInstance().showScreen(SceneEnum.GAME, game, gameInfo);
                startSingleListener();
            }
        });
    }

    /**
     * Makes the white toggle button unchecked if the black color is pushed.
     */
    private void blackListener() {
        black.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                white.setChecked(false);
                blackListener();
            }
        });
    }

    /**
     * Makes the black toggle button unchecked if the black color is pushed.
     */
    private void whiteListener() {
        white.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                black.setChecked(false);
                whiteListener();
            }
        });
    }

    /**
     * Adds an action event to the sign in game button.
     */
    private void signInListener() {
        signIn.addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                error.setText("Alias not registered.");
                error.setVisible(false);

                if (playerOne) {
                    String name = username.getText().replaceAll("\\s+", "");
                    Boolean exists = Chess.getDatabase().isPlayerRegistered(name);
                    if (exists) {
                        try {
                            gameInfo = new GameInfo(Chess.getDatabase().getPlayer(name));
                            screenGameMenu();
                            signInListener();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        error.setVisible(true);
                        signInListener();
                    }
                } else {
                    String name = username.getText().replaceAll("\\s+", "");
                    Boolean exists = Chess.getDatabase().isPlayerRegistered(name);

                    if (name.equals(gameInfo.getPlayer().getName())) {
                        error.setText("Already signed in");
                        error.setVisible(true);
                        signInListener();
                    } else if (exists) {
                        try {
                            gameInfo.setOpponent(Chess.getDatabase().getPlayer(name));
                            gameInfo.setSinglePlayer(false);
                            gameInfo.setPlayerColor(PieceColor.WHITE);
                            gameInfo.getPlayer().loadRating();
                            gameInfo.setGameType(GameType.getGameType(multiplayerOption.getSelected()));
                            SceneManager.getInstance().showScreen(SceneEnum.GAME, game, gameInfo);
                            signInListener();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        error.setText("Alias not registered.");
                        error.setVisible(true);
                        signInListener();
                    }
                }
            }
        });
    }

    /**
     * Adds an action event to the register button.
     */
    private void registerListener() {
        register.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                screenRegister();
                registerListener();
            }

        });
    }

    /**
     * Adds an action event to the sign up button.
     */
    private void signUpListener() {
        signUp.addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                String name = registerUsername.getText().replaceAll("\\s+", "");
                Boolean exists = Chess.getDatabase().isPlayerRegistered(name);

                error.setText("This alias already exists! Please choose another one.");
                if (exists) {
                    error.setVisible(true);
                } else if (name.length() > 20) {
                    error.setText("Name must be less than 20 characters");
                    error.setVisible(true);
                    signUpListener();
                } else {
                    try {
                        Chess.getDatabase().registerPlayer(new Player(name));
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    screenSignIn();
                }
                signUpListener();
            }
        });
    }

    /**
     * Adds an action event to button that leads back to the sign in screen from the
     * register screen.
     */
    private void returnSignInListener() {
        backToLogIn.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                screenSignIn();
                returnSignInListener();
            }
        });
    }

    /**
     * Adds an action event to the button that leads back to the game menu screen.
     */
    private void backToChooseGameListener() {
        backToChooseGame.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                screenGameMenu();
                backToChooseGameListener();
            }
        });
    }

    /**
     * Adds an action event to the button that leads to the preferences
     * (singleplayerBtn) screen.
     */
    private void singleplayerListener() {
        singleplayerBtn.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                screenPreferences();
                singleplayerListener();
            }
        });
    }

    /**
     * Adds an action event to the button that leads to the multiplayerBtn options
     * screen from the game menu.
     */
    private void multiplayerOptionListener() {
        multiplayerBtn.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                screenMultiplayerOption();
                multiplayerOptionListener();
            }
        });
    }

    private void onlineOptionListener() {
        chooseOnline.addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                screenMultiplayerOnlineOption();
                onlineOptionListener();
            }
        });
    }

    private void offlineOptionListener() {
        chooseOffline.addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                screenMultiplayerOfflineOption();
                offlineOptionListener();
            }
        });
    }

    /**
     * Adds an action event to the button that leads to the multiplayerBtn opponent
     * sign in button.
     */
    private void multiplayerOpponentListener() {
        multiOpponent.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                screenMultiplayer();
                multiplayerOpponentListener();
            }
        });
    }

    private void createGameListener() {
        createGame.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                screenCreateGame();
                createGameListener();
            }

        });
    }

    private void createGameBtnListener() {
        createGameBtn.addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                multiplayer.createGame(
                        enterOnlineName.getText(),
                        GameType.getGameType(gameType.getSelected()),
                        gameInfo.getPlayer().getName(),
                        PieceColor.WHITE,
                        gameInfo.getPlayer().getRating()
                );
                createGameBtnListener();
            }
        });
    }

    private void joinGameListener() {
        joinGame.addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (onlineList.getSelectedIndex() > -1) {
                    MultiplayerGame game = multiplayerGames.get(onlineList.getSelectedIndex());
                    multiplayer.joinGame(game.getId());
                }
                joinGameListener();
            }
        });
    }

    /**
     * Adds an action event to the button that leads to the score screen from the
     * main game menu screen.
     */
    private void scoreListener() {
        scores.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                scoreGroup.clearChildren();

                try {
                    ArrayList<Player> players = Chess.getDatabase().listPlayers();
                    for (int i = 0; i < players.size(); i++) {
                        Player p = players.get(i);
                        Label name = new Label(p.getName(), skin, "title-plain");
                        Label rating = new Label(String.valueOf(p.getRating()), skin, "title-plain");
                        Label win = new Label(String.valueOf(p.getWins()), skin, "title-plain");
                        Label lose = new Label(String.valueOf(p.getLosses()), skin, "title-plain");
                        Label draw = new Label(String.valueOf(p.getDraws()), skin, "title-plain");
                        HorizontalGroup group = new HorizontalGroup();
                        group.rowAlign(Align.left);
                        group.space(50f).left();
                        group.addActor(name);
                        group.addActor(rating);
                        group.addActor(win);
                        group.addActor(lose);
                        group.addActor(draw);
                        scoreGroup.addActor(group);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                screenScore();
                scoreListener();
            }
        });
    }
}