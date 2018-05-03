package animation;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.Chess;
import scenes.AbstractScene;
import scenes.MainMenuScene;

public class CustomAnimation extends Game {
	
	private String file; 
	private int duration;

	//animation
	private SpriteBatch batch;
	private Texture gif;
	private TextureRegion[] animationFrames;
	private Animation<TextureRegion> animation;
	private float elapsedTime;
	private float speed;
	
	private int height, width, vertical, horisontal;
	
	/**
	 * Creates an animation manually. It is important that each individual picture in the spritesheet has
	 * an int dimension. 
	 * 
	 * @param pngDestination	The file location of the png spritesheet in our assets folder
	 * @param timeoutDuration	The amount of seconds the animation is to play
	 * @param pixelWidth		The pixel width of each individual picture in spritesheet.
	 * @param pixelLength		The pixel length of each individual picture in spritesheet.
	 * @param x					The amount of pictures in spritesheet horisontally.
	 * @param y					The amount of pictures in spritesheet vertically.
	 * @param fps				The desired speed for the animation, in frames per second.
	 */
	public CustomAnimation(String pngDestination, int timeoutDuration,
			int pixelWidth, int pixelHeigth, int x, int y, int fps) {
		file = pngDestination;
		duration = timeoutDuration;
		speed = 1f/fps;
		height = pixelHeigth;
		width = pixelWidth;
		horisontal = x;
		vertical = y;
		//CustomAnimation animation = new Animation("pictures/loading.png, 3, 474, 717, 5, 8, 10");
	}
	
	
	public void create(){
		batch = new SpriteBatch();
		gif = new Texture(file);
		TextureRegion [][] tempFrames = TextureRegion.split(gif, width, height);
		animationFrames = new TextureRegion[40];
	
		int index = 0;
	
		for (int i = 0; i < vertical; i++){
			for(int j = 0; j < horisontal; j++){
				animationFrames[index++] = tempFrames[i] [j];
			}
		}
		animation = new Animation<TextureRegion>(speed, animationFrames);
	}
	
	public void render() {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		elapsedTime += Gdx.graphics.getDeltaTime();
		System.out.println(elapsedTime);
		TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);
		batch.begin();
		if (Math.round(elapsedTime) < duration){
			batch.draw(currentFrame, 120, -80, width, height);
		}
		batch.end();
   }

	
	public void dispose(){
		batch.dispose();
	}

}