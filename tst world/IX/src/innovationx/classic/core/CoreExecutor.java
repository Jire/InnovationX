package src.innovationx.classic.core;

import src.innovationx.classic.Server;
import src.innovationx.classic.content.minigames.ZombieMinigame;

public class CoreExecutor implements Runnable {

	@Override
	public void run() {
		long timeSpent = 0;
		long lastTick = System.currentTimeMillis();
		Thread.currentThread().setName("Processor Thread");
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY - 2);
		while (Server.s.serverRunning) {
			synchronized (Server.s) {
				try {
				Server.s.restartTime++;
				Server.s.playerHandler.process();
				Server.s.npcHandler.process();
				Server.s.itemHandler.process();
				Server.s.eventManager.processTasks();

				Server.s.itemHandler.process();
				Server.s.shopHandler.process();
				Server.s.pc.process();
				Server.s.worldO.process();
				if (ZombieMinigame.players.size() >= 3) {
					ZombieMinigame.tick();
				}
				lastTick = System.currentTimeMillis();
				timeSpent = System.currentTimeMillis() - lastTick;
				Server.s.msLag = (int) (timeSpent);
				if (timeSpent >= 600) {
					timeSpent = 0;
				}
					Thread.sleep(600 - timeSpent);
				} catch (Exception e) {		
					System.out.println("Error while sleeping");
				}
			}
		}
	}
}
