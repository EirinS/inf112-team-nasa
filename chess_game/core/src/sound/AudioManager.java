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
	// https://freesound.org/people/harrietniamh/sounds/415083/
	static Sound hintSound = Gdx.audio.newSound(Gdx.files.internal("sound/hint.wav"));
	
	private static boolean on = true;
	
	public static void playMoveSound()
	{
		if (on) moveSound.play();
	}
	
	/**
	 * Plays a sound for giving a hint.
	 */
	public static void playHintSound() {
		if (on) hintSound.play();
	}

	/**
	 * Toggles audio on/off.
	 */
	public static void toggle() {
		on = !on;
	}
}
