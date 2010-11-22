package src.innovationx.classic.model.player.task;

public abstract class PlayerTask implements Runnable {
	
	/*
	 * Needs to stop?
	 */
	public boolean needsStop;
	
	/*
	 * Sets the stop variable
	 */
	public void setStop(boolean flag) {
		needsStop = flag;
	}
	
	/*
	 * Gets the stop variable
	 */
	public boolean getStop() {
		return needsStop;
	}

}
