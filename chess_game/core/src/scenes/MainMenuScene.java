package scenes;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import game.Chess;
import game.ChessGame;
import game.WindowInformation;
import register.RegisteredPlayers;

/**
 * This class sets up and manages the elements in the main menu of the user interface.
 * 
 * REFACTORING:
 * Note to dev: Should definitely be refactored into more classes (i.e. listeners and screenToggle) at some point. 
 * Will be easier to do once I know everything works.
 * @author sofia
 *
 */

public class MainMenuScene implements Screen {
	
	//necessary components
	private Chess game;	
	private Stage stage;
	private Skin skin;
	//graphical components
	private static final int defaultHeight = 55;
	private static final int defaultWidth = 250;
	private static final int centreWidth = (WindowInformation.WIDTH/2)-(defaultWidth/2);
	private ArrayList<Actor> actors;
	private Image imgBackground;
	private Label staticText, header, headerScore, error;
	private TextButton signIn, register, signUp, singleplayer, multiplayer, scores, startSingle,
		black, white, signInP2;
	private TextField username, registerUsername;
	private Button backToLogIn, backToChooseGame;
	private SelectBox<String> difficulty;
	private List<String> scoreList;
	private ScrollPane scorePane;
	//Navigation assistance
	private boolean playerOne;

	public MainMenuScene (Chess mainGame){
		game = mainGame;
		playerOne = true;
		initialize();
		setUpElements();
		setUpArrayList();
		setUpElementSizes();
		addListeners();
		addActorsToStage();
		screenSignIn();
		Gdx.input.setInputProcessor(stage);
		
	}	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getSpriteBatch().begin();
		stage.act(delta);
		stage.draw();
		game.getSpriteBatch().end();
	}
	
	//Section 1: Set up
	private void initialize (){
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.txt"));
		skin = new Skin (Gdx.files.internal("skin/uiskin.json"), atlas);
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
	}
	
	private void setUpElements(){
		imgBackground = new Image(new Texture("pictures/menu.jpg"));
		imgBackground.setSize(WindowInformation.WIDTH, WindowInformation.HEIGHT);
		
		//Elements in log in
		signIn = new TextButton("Sign in", skin, "default");
		signIn.setPosition(centreWidth, WindowInformation.HEIGHT/2.5f);
		staticText = new Label("Or if you haven't already,", skin, "optional");
		staticText.setPosition(centreWidth+30, WindowInformation.HEIGHT/3.1f);
		register = new TextButton("Register here", skin, "default");
		register.setPosition(centreWidth+50, WindowInformation.HEIGHT/3.4f);
		username = new TextField("username", skin, "default");
		username.setPosition(centreWidth, WindowInformation.HEIGHT/2);
		
		//Elements in register
		backToLogIn = new Button(skin, "left");
		backToLogIn.setPosition(centreWidth/3.8f, WindowInformation.HEIGHT/1.2f);
		signUp = new TextButton("Sign up", skin, "default");
		signUp.setPosition(centreWidth, WindowInformation.HEIGHT/2);
		registerUsername = new TextField(" desired username", skin, "default");
		registerUsername.setPosition(centreWidth, WindowInformation.HEIGHT/2.5f);
		error = new Label ("default", skin, "error");
		error.setPosition(centreWidth, WindowInformation.HEIGHT/1.5f);

		//Elements in gamemenu
		header = new Label ("Main menu", skin, "title-plain");
		header.setPosition((centreWidth+(defaultWidth/4)), WindowInformation.HEIGHT/1.6f);
		singleplayer = new TextButton("Singleplayer", skin, "default");
		singleplayer.setPosition(centreWidth, WindowInformation.HEIGHT/2);
		multiplayer = new TextButton("Multiplayer", skin, "default"); 
		multiplayer.setPosition(centreWidth, WindowInformation.HEIGHT/2.7f);
		scores = new TextButton("Highscore", skin, "default");
		scores.setPosition(centreWidth, WindowInformation.HEIGHT/4);
		
		//Elements in preferences (singleplayer)
		startSingle = new TextButton("Start game", skin, "default");
		startSingle.setPosition(centreWidth, WindowInformation.HEIGHT/3);
		difficulty = new SelectBox<String>(skin, "default");
		String[] options = {"Intermediate","Easy"};
		difficulty.setItems(options);
		difficulty.setPosition(centreWidth, WindowInformation.HEIGHT/2);
		black = new TextButton("Black", skin, "toggle");
		black.setPosition(centreWidth-(defaultWidth/4), WindowInformation.HEIGHT/1.6f);
		white = new TextButton("White", skin, "toggle");
		white.setPosition((centreWidth+(defaultWidth/2)), WindowInformation.HEIGHT/1.6f);
		white.toggle();
		
		//Elements in score screen
		headerScore = new Label ("High scores:", skin, "title-plain");
		scoreList = new List<String>(skin);
		String[] temporary = new String[] {"1", "2", "3"};
		scoreList.setItems(temporary);
		scorePane = new ScrollPane(scoreList, skin, "default");
		scorePane.setPosition(centreWidth/1.5f, WindowInformation.HEIGHT/6);
		headerScore.setPosition(centreWidth/1.5f + (defaultWidth/2), WindowInformation.HEIGHT/1.2f);
		
		//Elements in multiplayer
		signInP2 = new TextButton("Sign in", skin, "default");
		signInP2.setPosition(centreWidth, WindowInformation.HEIGHT/2.5f);


		
		//Multiscreen
		backToChooseGame = new Button(skin, "left");
		backToChooseGame.setPosition(centreWidth/3.8f, WindowInformation.HEIGHT/1.2f);
		
	}
	
	
	private void setUpArrayList(){
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
		actors.add(header);
		actors.add(startSingle);
		actors.add(difficulty);
		actors.add(black);
		actors.add(white);
		actors.add(scorePane);
		actors.add(headerScore);
		actors.add(error);
	}
	
	private void setUpElementSizes(){
		for (Actor element : actors){
			if(element instanceof TextButton || element instanceof TextField || element instanceof Label){
				element.setSize(defaultWidth, defaultHeight);
			}
		}
		register.setSize(defaultWidth/2, defaultHeight/2);
		backToLogIn.setSize(27, 27);
		backToChooseGame.setSize(27, 27);
		difficulty.setSize(defaultWidth, defaultHeight/1.5f);
		black.setSize(defaultWidth/1.5f, defaultHeight/1.5f);
		white.setSize(defaultWidth/1.5f, defaultHeight/1.5f);
		scorePane.setSize(defaultWidth*1.6f, defaultHeight*7);
	}
	
	
	private void addActorsToStage(){
		stage.addActor(imgBackground);
		for(Actor element : actors){
			stage.addActor(element);
		}
	}	
	
	//Section 2: ToggleRightScreens
	
	/**
	 * Help method. Sets all elements on screen to invisible. Will be used by
	 * screen methods that turn the correct elements back to visible.
	 */
	private void invisible(){
		
		for(Actor element : actors){
			element.setVisible(false);
		}
	}
	
	protected void screenSignIn(){
		invisible();
		signIn.setVisible(true);
		staticText.setVisible(true);
		register.setVisible(true);
		username.setVisible(true);
	}
	protected void screenRegister(){
		invisible();
		signUp.setVisible(true);
		backToLogIn.setVisible(true);
		registerUsername.setVisible(true);
		
	}
	protected void screenGameMenu(){
		invisible();
		playerOne = false;
		singleplayer.setVisible(true);
		header.setText("Main Menu");
		header.setVisible(true);
		multiplayer.setVisible(true);
		scores.setVisible(true);
	}
	protected void screenScore(){
		invisible();
		scorePane.setVisible(true);
		backToChooseGame.setVisible(true);
		headerScore.setVisible(true);
	}
	protected void screenPreferences(){
		invisible();
		startSingle.setVisible(true);
		difficulty.setVisible(true);
		backToChooseGame.setVisible(true);
		black.setVisible(true);
		white.setVisible(true);
	}
	private void screenMultiplayer(){
		invisible();
		signIn.setVisible(true);
		header.setText("Opponent");
		header.setVisible(true);
		staticText.setVisible(true);
		username.setText("Username");
		username.setVisible(true);
		registerUsername.setText(" desired username");
		register.setVisible(true);
		backToChooseGame.setVisible(true);
	}
	
	//Section 3: Buttonlisteners
	/**
	 * Will only be called upon initialization. Calling the button itself in each eventhandler
	 * will reset the functionality of the button.
	 * 
	 * Remaining buttons: 

	 * multiplayer: goes to multiplayerScreen screen (= signIn + "Log in or register as player 2")
	 * 
	 * 
	 *sign up button: If first player: True, if second -> false
	 * 
	 * textfield -> inputlistener
	 */
private void addListeners(){
		signInListener();
		registerListener();
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
				/*Pseudokode:
				 * if (white.isChecked){
				 * GameScene game = new GameScene(game, filehandler.getPlayerOne, difficulty.getSelected(), PieceColor.WHITE);
				 * }
				 * else{
				 * GameScene game = new GameScene(game, filehandler.getPlayerOne, difficulty.getSelected(), PieceColor.BLACK);
				 */
				game.setScreen(new GameScene(game));
			}
		});
	}
	
	private void blackListener(){
		black.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				white.setChecked(false);
				blackListener();
			}
		});
	}
	private void whiteListener(){
		white.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				black.setChecked(false);
				whiteListener();
			}
		});
	}

	private void signInListener(){
		signIn.addListener(new ClickListener(){
			
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button)
	{
				error.setText("Alias not registered.");
				String name = registerUsername.getText();
				Boolean exists = RegisteredPlayers.playerIsRegistered(name);

				if (playerOne)
				{					
					  if (exists)
					  {
					  		// TODO - setPlayerOne(name);
					  		// TODO - game.setPlayerOne(name);
					  }
					 else
					 {
						 error.setVisible(true);
					 }
				}
				else 
				{				
					  if (exists)
					  {
					 		// TODO - setPlayerTwo(name);
					  		// TODO - game.setPlayerTwo(name);
					  		// TODO - GameScene game = new GameScene(game, getPlayerOne(),getPlayerTwo());
					  }
					  else
					  {
						  error.setVisible(true);
					  }						 
				}
				
				if (playerOne){
				screenGameMenu();
				signInListener();
				}
			}
			
		});
	}
	private void registerListener(){
		register.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button)
			{
				screenRegister();
				registerListener();
			}
			
		});
	}
	private void registerUserNameListener(){
		registerUsername.addListener(new ClickListener(){
			
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button)
			{
				 String name = registerUsername.getText();
				 Boolean exists = RegisteredPlayers.playerIsRegistered(name);
				 
				error.setText("This alias already exists! Please choose another one.");
				if (playerOne){
					

					 if (exists)
					 {
						 error.setVisible(true);
					 }
					 else
					 { 
					  		RegisteredPlayers.registerPlayer(name);
					  		// TODO - Game.setPlayerOne(name);
					 }
					 
				}
				else {
					
					  if (exists)	
					  {
						  error.setVisible(true);
					  }
					  else
					  {
					  		RegisteredPlayers.registerPlayer(name);
					  		// TODO - game.setPlayerTwo(name);
					  }
				}	
				registerUserNameListener();
			}
			
		});
	}
	private void returnSignInListener(){
		backToLogIn.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button){
				screenSignIn();
				returnSignInListener();
			}
		});
	}
	
	private void singleplayerListener(){
		singleplayer.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button){
				screenPreferences();
				singleplayerListener();
			}
		});
	}
	private void backToChooseGameListener(){
		backToChooseGame.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button){
				screenGameMenu();
				backToChooseGameListener();
			}
		});
	}
	private void multiplayerListener(){
		multiplayer.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button){
				screenMultiplayer();
				multiplayerListener();
			}
		});
	}
	private void scoreListener(){
		scores.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button){
				
				ArrayList<String> highscores = RegisteredPlayers.getHighscores();
				
				String[] scores = new String[highscores.size()+1];
				scores[0] = "Name/Rating/W/L/D";
				for(int i = 0; i < highscores.size(); i++)
				{
					scores[i+1] = highscores.get(i);
				}
				scoreList.setItems(scores);
				screenScore();
				multiplayerListener();
			}
		});
	}
		
		@Override
	public void hide() {}

	@Override
	public void resize(int width, int height) {}
	
	@Override
	public void pause() {}
	
	@Override
	public void resume() {}
	
	@Override
	public void dispose() {}
	
	@Override
	public void show() {}
}
