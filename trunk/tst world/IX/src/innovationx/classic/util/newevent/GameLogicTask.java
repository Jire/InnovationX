package src.innovationx.classic.util.newevent;

public abstract class GameLogicTask implements Runnable {

	protected boolean needRemove;
	
	public final void stop() {
		needRemove = true;
	}
}
