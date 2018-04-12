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
	
	//https://freesound.org/people/fins/sounds/171497/
	static Sound noUndo = Gdx.audio.newSound(Gdx.files.internal("sound/error.wav"));
	
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
	 * Play sound for no undo possible
	 */
	public static void playNoUndo() {
		if (on) noUndo.play();
	}

	/**
	 * Toggles audio on/off.
	 */
	public static void toggle() {
		on = !on;
	}

	/**
	 * Gets the state of the audio
	 * @return audio on or off
	 */
	public static boolean audioOn() {
		return on;
	}
}
