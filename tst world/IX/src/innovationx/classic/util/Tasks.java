package src.innovationx.classic.util;

import src.innovationx.classic.Server;
import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.Player;
import src.innovationx.classic.util.newevent.GameLogicTask;

public class Tasks {
	
	public void registerTasks() {
		System.out.println("Registering Garbage collection task...");
		Server.s.eventManager.schedule(new GameLogicTask() {
			@Override
			public void run() {
				System.gc();
				System.out.println("Garbage collected.");
			}
		}, 200);
		System.out.println("Registering player saving task...");
		Server.s.eventManager.schedule(new GameLogicTask() {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				for(Player p: Server.s.playerHandler.players) {
					if(p == null)
						continue;
					Server.s.playerHandler.SaveChar(p);
		            Server.s.playerHandler.SaveMoreInfo(p);
		          
				}
				System.out.println("All players saved.");
			}
		}, 200);
	}

}
