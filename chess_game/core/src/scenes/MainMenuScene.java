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


public class MainMenuScene implements Screen {
	
	private Chess game;
	private static final int buttonHeight = 55;
	private static final int buttonWidth = 250;
	private static final int centreWidth = (GameInformation.WIDTH/2)-(buttonWidth/2);
	private Stage stage;
	private Image imgBackground;
	
	private Label staticText;
	private TextButton signIn, register;
	private TextField username;
	private Button backToLogIn, backToChooseGame;
	private Skin skin;
	
	private boolean signScreen, registerScreen, gameScreen, preferencesScreen, multiScreen, scoreScreen;
	

	public MainMenuScene (Chess mainGame){
		game = mainGame;
		initialize();
		addListeners();
		addActorsToStage();
		Gdx.input.setInputProcessor(stage);
	}
	
	private void initialize (){
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.txt")); 
		skin = new Skin (Gdx.files.internal("skin/uiskin.json"), atlas);
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		
		imgBackground = new Image(new Texture("pictures/menu.jpg"));
		imgBackground.setSize(GameInformation.WIDTH, GameInformation.HEIGHT);
		
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
		
		backToLogIn = new Button(skin, "left");
		backToLogIn.setSize(27, 27);
		backToLogIn.setPosition(centreWidth/3.8f, GameInformation.HEIGHT/1.2f);
		
		backToChooseGame = new Button(skin, "left");
		
	}
	
	
	private void addActorsToStage(){
		stage.addActor(imgBackground);
		stage.addActor(signIn);
		stage.addActor(username);
		stage.addActor(staticText);
		stage.addActor(register);
		stage.addActor(backToLogIn);
	}
	

	@Override
	public void show() {
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
	@Override
	public void hide() {
	}
	
	/**
	 * Will only be called upon initialization. Calling the button itself in each eventhandler
	 * will reset the functionality of the button.
	 */
private void addListeners(){
		addSignInListener();
		//addRegisterListener();
	}
	
	private void addSignInListener(){
		signIn.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button)
			{
				System.out.print("Log in clicked!");
				// if (valid input){ <-Stian
				signScreen = false;
				//}
				//else -> errormessage
				addSignInListener();
			}
			
		});
	}
	
	
	private void toggleVisibility (){
		if (signScreen){
			signIn.setVisible(true);
			username.setVisible(true);
			
		}
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
}

	/**
	 * 

	private void addMultiplayerListener(){
		Stage buttonMultiplayer;
		buttonMultiplayer.addListener(new ClickListener(){
			public void touchUp(InputEvent e, float x, float y, int point, int button)
			{
				buttonLogIn.setVisible(false);
				System.out.print("Multiplayer clicked");
				addRegisterListener();
			}
		});
	}
	private void addSinglePlayerListener(){
		
	}

	private void addBackToFirstScreenListener(){
	
	}
}
*/