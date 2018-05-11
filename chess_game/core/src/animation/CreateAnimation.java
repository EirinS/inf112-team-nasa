package animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CreateAnimation {
	
	private String file; 

	//animation
	private Texture gif;
	private TextureRegion[] animationFrames;
	private Animation<TextureRegion> animation;
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
	public CreateAnimation(String pngDestination, int timeoutDuration,
			int pixelWidth, int pixelHeigth, int x, int y, int fps) {
		file = pngDestination;
		speed = 1f/fps;
		height = pixelHeigth;
		width = pixelWidth;
		horisontal = x;
		vertical = y;
		makeAnimation();
		//CustomAnimation animation = new Animation("pictures/loading.png, 3, 474, 717, 5, 8, 10");
	}
	
	
	private void makeAnimation(){
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
	
	public Animation<TextureRegion> getAnimation(){
		return animation;
	}
	
}