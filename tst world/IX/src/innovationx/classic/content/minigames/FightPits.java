package src.innovationx.classic.content.minigames;
import java.util.ArrayList;
import java.util.Random;

import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.Player;
import src.innovationx.classic.model.player.PlayerHandler;
import src.innovationx.classic.util.Misc;

/**
 * Fight Pits
 * @author Canownueasy
 */

public class FightPits {
	
	/**
	 * How long each game lasts (10 mins).
	 */
	public static int CYCLE = 600;

	/**
	 * Represents the waiting time for
	 * the Fight Pits.
	 */
	public static int TIME = 120;
	
	/**
	 * Tells if the game is started or not.
	 */
	public static boolean STARTED;
	
	public static String LAST_WINNER = "";
	
	/**
	 * Holds all the player's names in the Fight Pits.
	 */
	public static ArrayList<String> players = new ArrayList<String>();
	
	/**
	 * Players that are still in the waiting room (non-ingame).
	 */
	public static ArrayList<String> waiters = new ArrayList<String>();
	
	/**
	 * Players that are still in the actual game (non-waiting room).
	 */
	public static ArrayList<String> fighters = new ArrayList<String>();
	
	/**
	 * Contains the IDs of rewards you could get from Fight Pits.
	 */
	public static int[] rewards = {7453, 7454, 7455, 7456, 7457, 7458, 7459, 7460, 7461, 7462};

	public static int randomReward() {
        return rewards[(int) (Math.random() * rewards.length)];
    }
	
	/**
	 * Adds a player to the Fight Pits in-game and removes them from the waitroom.
	 * @param player The player to do so.
	 */
	public void moveToGame(Client player) {
		waiters.remove(player.playerName);
		fighters.add(player.playerName);
	}
	
	/**
	 * Adds a player to the waiting room from the in-game.
	 * @param player The player to do so.
	 */
	public static void removeFromGame(Client player) {
		fighters.remove(player.playerName);
		waiters.add(player.playerName);
	}
	
	/**
	 * Changes all waiters coords and sends to game.
	 */
	public static void sendAllToGame() {
		for(Player allz : PlayerHandler.players) {
			if(allz != null) {
				Client all = (Client) allz;
				if(waiters.contains(all.playerName) && players.contains(all.playerName)) {
					fighters.add(all.playerName);
					waiters.remove(all.playerName);
					Random r = new Random();
					all.changeCoords(2396 + r.nextInt(5), 5159 + r.nextInt(5), 0);
				}
			}
		}
	}
	
	/**
	 * Changes all fighters coords and sends to game.
	 */
	public static void sendAllToWait() {
		for(Player allz : PlayerHandler.players) {
			if(allz != null) {
				Client all = (Client) allz;
				if(fighters.contains(all.playerName) && players.contains(all.playerName)) {
					if(LAST_WINNER != "999,999") {
						LAST_WINNER = all.playerName;
						waiters.add(all.playerName);
						fighters.remove(all.playerName);
						if(Misc.random(1) == 1) {
							all.changeCoords(2396, 5172, 0);
						} else {
							all.changeCoords(2402, 5172, 0);
						}
						all.showDialogue("You won Fight Pits!");
						all.addItem(randomReward(), 1);
						all.playerLevel[3] = all.getLevelForXP(3);
						all.updateRequired = true;
					} else {
						waiters.add(all.playerName);
						fighters.remove(all.playerName);
						if(Misc.random(1) == 1) {
							all.changeCoords(2396, 5172, 0);
						} else {
							all.changeCoords(2402, 5172, 0);
						}
						all.playerLevel[3] = all.getLevelForXP(3);
						all.updateRequired = true;
						all.showDialogue("Nobody won Fight Pits!");
					}
				} else if(players.contains(all.playerName)) {
					all.playerLevel[3] = all.getLevelForXP(3);
					if(LAST_WINNER.equals("999,999")) {
						all.showDialogue("Nobody won Fight Pits!");
						return;
					}
					all.showDialogue(LAST_WINNER.replace("(", "").replace(")", "") + " won Fight Pits!");
				}
			}
		}
	}
	
	/**
	 * Ticks the time of Fight Pits down.
	 */
	public static void tick() {
		for(Player allz : PlayerHandler.players) {
			if(allz != null) {
				Client all = (Client) allz;
				if(players.contains(all.playerName)) {
					if(all.playerLevel[3] < 1 || all.NewHp < 1) {
						all.NewHp = 0;
						all.IsDead = true;
						all.ApplyDead();
						//System.out.println("Applied dead for " + all.playerName);
					}
				}
			}
		}
		if(!STARTED) {
			if(players.size() > 2) {
				TIME--;
				if(TIME < 1) {
					try {
						sendAllToGame(); //launch the game
						TIME = 120;
						STARTED = true;
					} catch(Exception e) {
					}
				}
			}
		}
		if(STARTED) {
			CYCLE--;
			if(CYCLE < 1) {
				try {
					LAST_WINNER = "999,999";
					sendAllToWait(); //end the game
					CYCLE = 300;
					TIME = 120;
					STARTED = false;
				} catch(Exception e) {
				}
			}
			if(fighters.size() < 2) {
				LAST_WINNER = fighters.toString();
				sendAllToWait(); //end the game if there are less then 2 people remaining (in fighters).
				STARTED = false;
				CYCLE = 300;
				TIME = 120;
			}
		}
	}
	
}