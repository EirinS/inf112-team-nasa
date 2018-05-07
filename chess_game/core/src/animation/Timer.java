
package animation;

import java.util.concurrent.TimeUnit;

public class Timer {
	
	private int seconds;
	
	public Timer (int duration){
		stopCodeExecution();
		seconds = duration;
	}
	

	public void stopCodeExecution(){
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
