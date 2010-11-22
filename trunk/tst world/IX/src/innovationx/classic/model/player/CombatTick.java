package src.innovationx.classic.model.player;

import src.innovationx.classic.Server;

public class CombatTick implements Runnable {

	/**
	 * Constructs a new combat tick.
	 */
	public CombatTick(int delay) {
		this.delay = delay;
	}
	
	/**
	 * The delay for the combat tick to parse.
	 */
	private int delay = 600;

	@Override
	public void run() {
		for(Player allz : Server.s.playerHandler.players) {
			if(allz != null) {
				Client all = (Client) allz;
			}
		}
		try {
			Thread.sleep(delay);
		} catch(Exception e) {System.out.println("Failed");}
	}

}