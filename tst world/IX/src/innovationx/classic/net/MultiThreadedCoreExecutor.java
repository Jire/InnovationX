package src.innovationx.classic.net;

import src.innovationx.classic.Server;

 /**
    *@author Harry Andreas <harrythecomputerwizkid@msn.com>
    * MultiThreadedCoreExecutor Class
    * Handles what should be ran into a seperate thread pool
    */
public class MultiThreadedCoreExecutor implements Runnable {

 	public void run() {
		try {
			synchronized(Server.s) {
				Server.s.shopHandler.process();
				Server.s.pc.process();
				Server.s.worldO.process();
				Server.s.ClanWars.tick();
		        Server.s.combatTick.run();
			}
		} catch(Exception e) {
		}
  	}

}