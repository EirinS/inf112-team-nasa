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
	//https://freesound.org/people/prucanada/sounds/415352/
	static Sound scream = Gdx.audio.newSound(Gdx.files.internal("sound/scream.wav"));
	//https://freesound.org/people/Zihris/sounds/324369/
	static Sound applause = Gdx.audio.newSound(Gdx.files.internal("sound/applause.mp3"));
	// https://freesound.org/people/TaranP/sounds/362204/
	static Sound lose = Gdx.audio.newSound(Gdx.files.internal("sound/lose.wav"));
	
	private static boolean on = true;
	
	/**
	 * Plays a sound for move
	 */
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
	 * Plays a sound for lost game.
	 */
	public static void playLoseSound() {
		if (on) lose.play();
	}
	
	/**
	 * Plays a sound for giving a hint.
	 */
	public static void playApplause() {
		if (on) applause.play();
	}
	
	/**
	 * Plays a high-pitched scream
	 */
	public static void playScream() {
		if (on) scream.play();
	}
	
	public static void stopApplause() {
		if (on) applause.stop();
	}
	
	public static void stopLose() {
		if (on) lose.stop();
	}
	
	public static void stop() {
		stopLose();
		stopApplause();
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
		if(on) {
			scream.stop();
			applause.stop();
			noUndo.stop();
			hintSound.stop();
			moveSound.stop();
			lose.stop();
		}
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
