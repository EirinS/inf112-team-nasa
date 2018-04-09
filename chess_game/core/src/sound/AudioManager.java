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
	
	
	
	public static void playMoveSound()
	{
		moveSound.play();
	}
}
