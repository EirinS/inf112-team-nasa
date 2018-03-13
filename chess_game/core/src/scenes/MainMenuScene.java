package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
	
	private Chess game;
	private static final int buttonHeight = 55;
	private static final int buttonWidth = 250;
	private static final int centreWidth = (GameInformation.WIDTH/2)-(buttonWidth/2);
	private Stage stage;
	private Image imgBackground;
	private Label staticText;
	private TextButton signIn, register, signUp, singleplayer, multiplayer, scores;
	private TextField username, registerUsername;
	private Button backToLogIn, backToChooseGame;
	private Skin skin;
	


	public MainMenuScene (Chess mainGame){
		game = mainGame;
		initialize();
		setUpElements();
		addListeners();
		addActorsToStage();
		screenSignIn();
		Gdx.input.setInputProcessor(stage);
		
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
		signIn.setSize(buttonWidth, buttonHeight);
		signIn.setPosition(centreWidth, GameInformation.HEIGHT/2.5f);
		
		staticText = new Label("Or if you haven't already,", skin, "optional");
		staticText.setSize(buttonWidth, buttonHeight);
		staticText.setPosition(centreWidth+30, GameInformation.HEIGHT/3.1f);
		
		register = new TextButton("Register here", skin, "default");
		register.setSize(buttonWidth/2, buttonHeight/2);
		register.setPosition(centreWidth+50, GameInformation.HEIGHT/3.4f);
		
		username = new TextField("username", skin, "default");
		username.setSize(buttonWidth, buttonHeight);
		username.setPosition(centreWidth, GameInformation.HEIGHT/2);
		
		//Elements in register
		backToLogIn = new Button(skin, "left");
		backToLogIn.setSize(27, 27);
		backToLogIn.setPosition(centreWidth/3.8f, GameInformation.HEIGHT/1.2f);
		
		signUp = new TextButton("Sign up", skin, "default");
		signUp.setSize(buttonWidth, buttonHeight);
		signUp.setPosition(centreWidth, GameInformation.HEIGHT/2);
		
		registerUsername = new TextField(" desired username", skin, "default");
		registerUsername.setSize(buttonWidth, buttonHeight);
		registerUsername.setPosition(centreWidth, GameInformation.HEIGHT/2.5f);
		
		
		//Elements in gamemenu
		//singleplayer, multiplayer, score
		
		//Elements in preferences (singleplayer)
		backToChooseGame = new Button(skin, "left");
		backToChooseGame.setSize(27, 27);
		backToChooseGame.setPosition(centreWidth/3.8f, GameInformation.HEIGHT/1.2f);
	}
	
	
	private void addActorsToStage(){
		stage.addActor(imgBackground);
		stage.addActor(signIn);
		stage.addActor(username);
		stage.addActor(staticText);
		stage.addActor(register);
		stage.addActor(registerUsername);
		stage.addActor(backToLogIn);
		stage.addActor(backToChooseGame);
		stage.addActor(signUp);
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

	
	
	//Section 2: ToggleRightScreens
	
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
		
	}
	protected void screenScore(){
		invisible();
	}
	protected void screenPreferences(){
		invisible();
	}
	prvoid screenMultiplayer(){
		invisible();
	}
	
	/**
	 * Help method. Sets all elements on screen to invisible. Will be used by
	 * screen methods that turn the correct elements back to visible.
	 */
	private void invisible(){
		signIn.setVisible(false);
		username.setVisible(false);
		staticText.setVisible(false);
		register.setVisible(false);
		backToLogIn.setVisible(false);
		backToChooseGame.setVisible(false);
		signUp.setVisible(false);
		registerUsername.setVisible(false);
	}
	
	
	
	
	//Section 3: Buttonlisteners
	
	
	/**
	 * Will only be called upon initialization. Calling the button itself in each eventhandler
	 * will reset the functionality of the button.
	 * 
	 * Remaining buttons: 
	 * backToLogIn: goes back to screenLogIn
	 * Singleplayer: go to screenPreferences
	 * backToChooseGame: used in multiplayer- and singleplayerscreen. Goes back to main game menu
	 * multiplayer: goes to multiplayerScreen screen (= signIn + "Log in or register as player 2")
	 * 
	 */
private void addListeners(){
		addSignInListener();
		addRegisterListener();
	}
	
	private void addSignInListener(){
		signIn.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button)
			{
				System.out.print("Log in clicked!");
				//Stian ->  if (valid input){
				screenGameMenu();
				//}
				//else -> errormessage
				addSignInListener();
			}
			
		});
	}
	private void addRegisterListener(){
		register.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button)
			{
				//Stian -> add username
				screenRegister();
				addSignInListener();
			}
			
		});
	}
	
	
		
		@Override
	public void hide() {
	}

	@Override
	public void resize(int width, int height) {	
	}
	
	@Override
	public void pause() {	
	}
	
	@Override
	public void resume() {	
	}
	
	@Override
	public void dispose() {
	}
	
	@Override
	public void show() {
	}
}
