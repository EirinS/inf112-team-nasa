package sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;


/**
 * 
 * @author 
 *
 */
public class AudioManager 
{
	// fetched from https://freesound.org/people/mh2o/sounds/351518/
	static Sound moveSound = Gdx.audio.newSound(Gdx.files.internal("sound/chessMove.wav"));
	static Sound hintSound = Gdx.audio.newSound(Gdx.files.internal("sound/hint.wav"));
	
	
	
	public static void playMoveSound()
	{
		moveSound.play();
	}
	
	/**
	 * Plays a sound for giving a hint.
	 */
	public static void playHintSound() {
		hintSound.play();
	}
}
