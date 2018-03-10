package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import chessGame.Chess;
import chessGame.GameInformation;


public class MainMenuScene implements Screen {
	
	private Chess game;
	private static final int buttonHeight = 55;
	private static final int buttonWidth = 230;
	private Skin skin; 
	private Stage stage;
	private Image buttonRegister, buttonLogIn, imgBackground;

	
	public MainMenuScene (Chess mainGame){
		game = mainGame;
		initialize();
		addListeners(); 
		addActorsToStage();
		Gdx.input.setInputProcessor(stage);
		
	}
	
	
	private void initialize (){
		skin = new Skin ();
		stage = new Stage(new ScreenViewport());
		imgBackground = new Image(new Texture("menu_background.jpg"));
		imgBackground.setSize(GameInformation.WIDTH, GameInformation.HEIGHT);
		
		
		buttonLogIn = new Image(new Texture("log_in.png"));
		buttonLogIn.setSize(buttonWidth, buttonHeight);
		buttonLogIn.setPosition(GameInformation.WIDTH/20, GameInformation.HEIGHT/1.7f);
		
		buttonRegister = new Image(new Texture("register.png"));
		buttonRegister.setSize(buttonWidth, buttonHeight);
		buttonRegister.setPosition(GameInformation.WIDTH/20, GameInformation.HEIGHT/3);
		
	}
	
	private void addListeners(){
		buttonLogIn.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button)
			{
				System.out.print("Log in clicked!");
			}
			
		});
		
		buttonRegister.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent e, float x, float y, int point, int button)
			{
				System.out.print("Register clicked!");
			}
			
		});
		
	}
	
	private void addActorsToStage(){
		stage.addActor(imgBackground);
		stage.addActor(buttonLogIn);
		stage.addActor(buttonRegister);
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
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
