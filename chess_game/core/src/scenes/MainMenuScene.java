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
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import chessGame.Chess;
import chessGame.GameInformation;

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
	private static final int defaultHeight = 55;
	private static final int defaultWidth = 250;
	private static final int centreWidth = (GameInformation.WIDTH/2)-(defaultWidth/2);
	private ArrayList<Actor> actors;
	private Image imgBackground;
	private Label staticText, header;
	private TextButton signIn, register, signUp, singleplayer, multiplayer, scores, startSingle, black, white;
	private TextField username, registerUsername;
	private Button backToLogIn, backToChooseGame;
	private SelectBox<String> difficulty;

	public MainMenuScene (Chess mainGame){
		game = mainGame;
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
		imgBackground.setSize(GameInformation.WIDTH, GameInformation.HEIGHT);
		
		//Elements in log in
		signIn = new TextButton("Sign in", skin, "default");
		signIn.setPosition(centreWidth, GameInformation.HEIGHT/2.5f);
		staticText = new Label("Or if you haven't already,", skin, "optional");
		staticText.setPosition(centreWidth+30, GameInformation.HEIGHT/3.1f);
		register = new TextButton("Register here", skin, "default");
		register.setPosition(centreWidth+50, GameInformation.HEIGHT/3.4f);
		username = new TextField("username", skin, "default");
		username.setPosition(centreWidth, GameInformation.HEIGHT/2);
		
		//Elements in register
		backToLogIn = new Button(skin, "left");
		backToLogIn.setPosition(centreWidth/3.8f, GameInformation.HEIGHT/1.2f);
		signUp = new TextButton("Sign up", skin, "default");
		signUp.setPosition(centreWidth, GameInformation.HEIGHT/2);
		registerUsername = new TextField(" desired username", skin, "default");
		registerUsername.setPosition(centreWidth, GameInformation.HEIGHT/2.5f);

		//Elements in gamemenu
		header = new Label ("Main menu", skin, "title-plain");
		header.setPosition((centreWidth+(defaultWidth/4)), GameInformation.HEIGHT/1.6f);
		singleplayer = new TextButton("Singleplayer", skin, "default");
		singleplayer.setPosition(centreWidth, GameInformation.HEIGHT/2);
		multiplayer = new TextButton("Multiplayer", skin, "default"); 
		multiplayer.setPosition(centreWidth, GameInformation.HEIGHT/2.7f);
		scores = new TextButton("Highscore", skin, "default");
		scores.setPosition(centreWidth, GameInformation.HEIGHT/4);
		
		//Elements in preferences (singleplayer)
		startSingle = new TextButton("Start game", skin, "default");
		startSingle.setPosition(centreWidth, GameInformation.HEIGHT/3);
		difficulty = new SelectBox<String>(skin, "default");
		String[] options = {"Intermediate","Easy"};
		difficulty.setItems(options);
		difficulty.setPosition(centreWidth, GameInformation.HEIGHT/2);
		black = new TextButton("Black", skin, "toggle");
		black.setPosition(centreWidth-(defaultWidth/4), GameInformation.HEIGHT/1.6f);
		white = new TextButton("White", skin, "toggle");
		white.setPosition((centreWidth+(defaultWidth/2)), GameInformation.HEIGHT/1.6f);
		
		//Elements in multiplayer
		
		//Multiscreen
		backToChooseGame = new Button(skin, "left");
		backToChooseGame.setPosition(centreWidth/3.8f, GameInformation.HEIGHT/1.2f);
		
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
//		actors.add();
//		actors.add();
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
		singleplayer.setVisible(true);
		header.setVisible(true);
		multiplayer.setVisible(true);
		scores.setVisible(true);
	}
	protected void screenScore(){
		invisible();
		backToChooseGame.setVisible(true);
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
	}

	private void startSingleListener() {
		startSingle.addListener(new ClickListener() {

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new GameScene(game));
			}
		});
	}

	private void signInListener(){
		signIn.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button)
			{
				//Stian ->  if (valid input){
				screenGameMenu();
				//}
				//else -> errormessage
				signInListener();
			}
			
		});
	}
	private void registerListener(){
		register.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button)
			{
				//Stian -> add username
				screenRegister();
				registerListener();
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
