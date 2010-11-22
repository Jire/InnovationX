package src.innovationx.classic.content.minigames;
import java.util.ArrayList;

import src.innovationx.classic.Server;
import src.innovationx.classic.model.npc.NPC;
import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.Player;
import src.innovationx.classic.util.Misc;

/**
 * It's a freaking zombie minigame
 * durrr hurrr
 * @author Canownueasy
 */

public class ZombieMinigame {
	
	/**
	 * Holds all of the player's in-game names.
	 */
	public static ArrayList<String> players = new ArrayList<String>();
	
	/**
	 * The wave the game is on.
	 */
	public static int WAVE = 1;
	
	/**
	 * How many zombies are left.
	 */
	public static int ZOMBIES = 0;
	
	/**
	 * The cycle of the game takes this long to end.
	 */
	public static int CYCLE = 500; //5 mins
	
	/**
	 * For waiting room time.
	 */
	public static int TIME = 60; //1 min
	
	/**
	 * Is the game started or not.
	 */
	public static boolean STARTED;
	
	public static void killAllZombies() {
		for(NPC npcs : Server.s.npcHandler.npcs) {
			if(npcs != null) {
				npcs.CurrentHp = 0;
			}
		}
	}
	
	/**
	 * Will transfer the players to the zombie room and startup the game.
	 */
	public static void transferToGame() {
		/*
		 * Let's add the player into the game.
		 */
		if(!players.contains(ingame().playerName)) {
			players.add(ingame().playerName);
		}
		/*
		 * Let's teleport the players to either one of
		 * the both sides.
		 */
		/*if(Misc.random(1) != 1) {
			ingame().teleport(2807, 10105);
		} else {
			ingame().teleport(2759, 10064);
		}*/
		/*
		 * Now let's spawn the zombies.
		 */
		spawnZombies(WAVE);
	}
	
	public static Client ingame() {
		for(Player allz : Server.s.playerHandler.players) {
			if(allz != null) {
				Client all = (Client) allz;
				if(players.contains(all.playerName)) {
					return all;
				}
			}
		}
		return null;
	}
	
	public static void moveFromGame(Client p) {
		players.remove(p.playerName);
		//TODO: Teleport them to the place we enter the game at.
	}
	
	public static void spawnZombies(int wave) {
		switch(wave) {
			case 1:
				createNPC(73, 2779, 10092);
				createNPC(73, 2773, 10094);
				createNPC(73, 2771, 10090);
				createNPC(73, 2776, 10088);
				createNPC(73, 2783, 10087);
				createNPC(73, 2786, 10075);
				createNPC(73, 2788, 10069);
				createNPC(73, 2785, 10061);
				createNPC(73, 2780, 10064);
				createNPC(73, 2777, 10071);
				break;
			case 2:
				createNPC(73, 2779, 10092);
				createNPC(73, 2773, 10094);
				createNPC(73, 2771, 10090);
				createNPC(73, 2776, 10088);
				createNPC(73, 2783, 10087);
				createNPC(73, 2786, 10075);
				createNPC(73, 2788, 10069);
				createNPC(73, 2785, 10061);
				createNPC(73, 2780, 10064);
				createNPC(73, 2777, 10071);
				createNPC(75, 2769, 10084);
				createNPC(75, 2771, 10078);
				createNPC(74, 2775, 10077);
				createNPC(74, 2780, 10074);
				createNPC(74, 2783, 10068);
				createNPC(75, 2789, 10082);
				createNPC(75, 2787, 10086);
				break;
			case 3:
				createNPC(73, 2779, 10092);
				createNPC(73, 2773, 10094);
				createNPC(73, 2771, 10090);
				createNPC(73, 2776, 10088);
				createNPC(73, 2783, 10087);
				createNPC(73, 2786, 10075);
				createNPC(73, 2788, 10069);
				createNPC(73, 2785, 10061);
				createNPC(73, 2780, 10064);
				createNPC(73, 2777, 10071);
				createNPC(75, 2769, 10084);
				createNPC(75, 2771, 10078);
				createNPC(74, 2775, 10077);
				createNPC(74, 2780, 10074);
				createNPC(74, 2783, 10068);
				createNPC(75, 2789, 10082);
				createNPC(75, 2787, 10086);
				createNPC(75, 2782, 10070);
				break;
			case 4:
				createNPC(73, 2779, 10092);
				createNPC(73, 2773, 10094);
				createNPC(73, 2771, 10090);
				createNPC(73, 2776, 10088);
				createNPC(73, 2783, 10087);
				createNPC(73, 2786, 10075);
				createNPC(73, 2788, 10069);
				createNPC(73, 2785, 10061);
				createNPC(73, 2780, 10064);
				createNPC(73, 2777, 10071);
				createNPC(75, 2769, 10084);
				createNPC(75, 2771, 10078);
				createNPC(74, 2775, 10077);
				createNPC(74, 2780, 10074);
				createNPC(74, 2783, 10068);
				createNPC(75, 2789, 10082);
				createNPC(75, 2787, 10086);
				createNPC(75, 2782, 10070);
				createNPC(75, 2785, 10066);
				createNPC(76, 2790, 10063);
				createNPC(75, 2789, 10080);
				createNPC(75, 2774, 10079);
				createNPC(76, 2770, 10098);
				createNPC(76, 2770, 10083);
				break;
			case 5:
				createNPC(73, 2779, 10092);
				createNPC(73, 2773, 10094);
				createNPC(73, 2771, 10090);
				createNPC(73, 2776, 10088);
				createNPC(73, 2783, 10087);
				createNPC(73, 2786, 10075);
				createNPC(73, 2788, 10069);
				createNPC(73, 2785, 10061);
				createNPC(73, 2780, 10064);
				createNPC(73, 2777, 10071);
				createNPC(75, 2769, 10084);
				createNPC(75, 2771, 10078);
				createNPC(74, 2775, 10077);
				createNPC(74, 2780, 10074);
				createNPC(74, 2783, 10068);
				createNPC(75, 2789, 10082);
				createNPC(75, 2787, 10086);
				createNPC(75, 2782, 10070);
				createNPC(75, 2785, 10066);
				createNPC(76, 2790, 10063);
				createNPC(75, 2789, 10080);
				createNPC(75, 2774, 10079);
				createNPC(76, 2770, 10098);
				createNPC(76, 2770, 10083);
				break;
			case 6:
				createNPC(73, 2779, 10092);
				createNPC(73, 2773, 10094);
				createNPC(73, 2771, 10090);
				createNPC(73, 2776, 10088);
				createNPC(73, 2783, 10087);
				createNPC(73, 2786, 10075);
				createNPC(73, 2788, 10069);
				createNPC(73, 2785, 10061);
				createNPC(73, 2780, 10064);
				createNPC(73, 2777, 10071);
				createNPC(75, 2769, 10084);
				createNPC(75, 2771, 10078);
				createNPC(74, 2775, 10077);
				createNPC(74, 2780, 10074);
				createNPC(74, 2783, 10068);
				createNPC(75, 2789, 10082);
				createNPC(75, 2787, 10086);
				createNPC(75, 2782, 10070);
				createNPC(75, 2785, 10066);
				createNPC(76, 2790, 10063);
				createNPC(75, 2789, 10080);
				createNPC(75, 2774, 10079);
				createNPC(76, 2770, 10098);
				createNPC(76, 2770, 10083);
				createNPC(76, 2799, 10068);
				createNPC(76, 2801, 10064);
				createNPC(76, 2798, 10060);
				createNPC(75, 2789, 10058);
				createNPC(76, 2774, 10055);
				break;
			case 7:
				createNPC(73, 2779, 10092);
				createNPC(73, 2773, 10094);
				createNPC(73, 2771, 10090);
				createNPC(73, 2776, 10088);
				createNPC(73, 2783, 10087);
				createNPC(73, 2786, 10075);
				createNPC(73, 2788, 10069);
				createNPC(73, 2785, 10061);
				createNPC(73, 2780, 10064);
				createNPC(73, 2777, 10071);
				createNPC(75, 2769, 10084);
				createNPC(75, 2771, 10078);
				createNPC(74, 2775, 10077);
				createNPC(74, 2780, 10074);
				createNPC(74, 2783, 10068);
				createNPC(75, 2789, 10082);
				createNPC(75, 2787, 10086);
				createNPC(75, 2782, 10070);
				createNPC(75, 2785, 10066);
				createNPC(76, 2790, 10063);
				createNPC(75, 2789, 10080);
				createNPC(75, 2774, 10079);
				createNPC(76, 2770, 10098);
				createNPC(76, 2770, 10083);
				createNPC(76, 2799, 10068);
				createNPC(76, 2801, 10064);
				createNPC(76, 2798, 10060);
				createNPC(75, 2789, 10058);
				createNPC(76, 2774, 10055);
				break;
			case 8:
				createNPC(73, 2779, 10092);
				createNPC(73, 2773, 10094);
				createNPC(73, 2771, 10090);
				createNPC(73, 2776, 10088);
				createNPC(73, 2783, 10087);
				createNPC(73, 2786, 10075);
				createNPC(73, 2788, 10069);
				createNPC(73, 2785, 10061);
				createNPC(73, 2780, 10064);
				createNPC(73, 2777, 10071);
				createNPC(75, 2769, 10084);
				createNPC(75, 2771, 10078);
				createNPC(74, 2775, 10077);
				createNPC(74, 2780, 10074);
				createNPC(74, 2783, 10068);
				createNPC(75, 2789, 10082);
				createNPC(75, 2787, 10086);
				createNPC(75, 2782, 10070);
				createNPC(75, 2785, 10066);
				createNPC(76, 2790, 10063);
				createNPC(75, 2789, 10080);
				createNPC(75, 2774, 10079);
				createNPC(76, 2770, 10098);
				createNPC(76, 2770, 10083);
				createNPC(76, 2799, 10068);
				createNPC(76, 2801, 10064);
				createNPC(76, 2798, 10060);
				createNPC(75, 2789, 10058);
				createNPC(76, 2774, 10055);
				createNPC(74, 2772, 10076);
				createNPC(75, 2783, 10103);
				createNPC(76, 2780, 10103);
				break;
			case 9:
				createNPC(73, 2779, 10092);
				createNPC(73, 2773, 10094);
				createNPC(73, 2771, 10090);
				createNPC(73, 2776, 10088);
				createNPC(73, 2783, 10087);
				createNPC(73, 2786, 10075);
				createNPC(73, 2788, 10069);
				createNPC(73, 2785, 10061);
				createNPC(73, 2780, 10064);
				createNPC(73, 2777, 10071);
				createNPC(75, 2769, 10084);
				createNPC(75, 2771, 10078);
				createNPC(74, 2775, 10077);
				createNPC(74, 2780, 10074);
				createNPC(74, 2783, 10068);
				createNPC(75, 2789, 10082);
				createNPC(75, 2787, 10086);
				createNPC(75, 2782, 10070);
				createNPC(75, 2785, 10066);
				createNPC(76, 2790, 10063);
				createNPC(75, 2789, 10080);
				createNPC(75, 2774, 10079);
				createNPC(76, 2770, 10098);
				createNPC(76, 2770, 10083);
				createNPC(76, 2799, 10068);
				createNPC(76, 2801, 10064);
				createNPC(76, 2798, 10060);
				createNPC(75, 2789, 10058);
				createNPC(76, 2774, 10055);
				createNPC(74, 2772, 10076);
				createNPC(75, 2783, 10103);
				createNPC(76, 2780, 10103);
				createNPC(2058, 2773, 10088);
				createNPC(2058, 2775, 10084);
				createNPC(2058, 2779, 10074);
				createNPC(2058, 2793, 10069);
				createNPC(2058, 2788, 10084);
				break;
			case 10: //Boss
				createNPC(73, 2779, 10092);
				createNPC(73, 2773, 10094);
				createNPC(73, 2771, 10090);
				createNPC(73, 2776, 10088);
				createNPC(73, 2783, 10087);
				createNPC(73, 2786, 10075);
				createNPC(73, 2788, 10069);
				createNPC(73, 2785, 10061);
				createNPC(73, 2780, 10064);
				createNPC(73, 2777, 10071);
				createNPC(75, 2769, 10084);
				createNPC(75, 2771, 10078);
				createNPC(74, 2775, 10077);
				createNPC(74, 2780, 10074);
				createNPC(74, 2783, 10068);
				createNPC(75, 2789, 10082);
				createNPC(75, 2787, 10086);
				createNPC(75, 2782, 10070);
				createNPC(75, 2785, 10066);
				createNPC(76, 2790, 10063);
				createNPC(75, 2789, 10080);
				createNPC(75, 2774, 10079);
				createNPC(76, 2770, 10098);
				createNPC(76, 2770, 10083);
				createNPC(76, 2799, 10068);
				createNPC(76, 2801, 10064);
				createNPC(76, 2798, 10060);
				createNPC(75, 2789, 10058);
				createNPC(76, 2774, 10055);
				createNPC(74, 2772, 10076);
				createNPC(75, 2783, 10103);
				createNPC(76, 2780, 10103);
				createNPC(2058, 2773, 10088);
				createNPC(2058, 2775, 10084);
				createNPC(2058, 2779, 10074);
				createNPC(2058, 2793, 10069);
				createNPC(2058, 2788, 10084);
				createNPC(2843, 2777, 10078);
				break;
			default:
				System.out.println("Unhandled wave: " + wave);
				break;
		}
	}
	
	/**
	 * Adds a player to the game.
	 * @param p The player to add.
	 */
	public static void addToGame(Client p) {
		players.add(p.playerName);
		if(Misc.random(1) == 1) {
			p.teleport(2807, 10105);
		} else {
			p.teleport(2759, 10064);
		}
		p.teleBlockDelay = 999999;
	}
	
	/**
	 * Creates a new NPC for the minigame.
	 * @param id NPC type.
	 * @param x The X tile coordinate.
	 * @param y The Y tile coordinate.
	 */
    public static void createNPC(int id, int x, int y) {
        int slot = Server.s.npcHandler.newNPC(id, x, y, 0, 0, 0, 0, 0, false, -1);
        ZOMBIES++;
    }
	
	public static void endGame(boolean won) {
		for(Player allz : Server.s.playerHandler.players) {
			if(allz != null) {
				Client all = (Client) allz;
				if(ZombieMinigame.players.contains(all.playerName)) {
					all.zombieKills = 0;
					all.teleBlockDelay = 0;
					all.teleport(2834, 3336);
					if(won) {
						//TODO: Stuff like rewards :P
						all.zombiePoints += WAVE;
						all.showDialogue("You defeated the zombies and you won!");
					} else {
						all.showDialogue("The zombies took over and you lost!");
					}
				}
			}
		}
		ZombieMinigame.players.clear();
		WAVE = 1;
		ZOMBIES = 0;
		CYCLE = 500;
		TIME = 60;
		STARTED = false;
		killAllZombies();
	}
	
	/**
	 * Ticks down for timing tasks.
	 */
	public static void tick() {
		if(!STARTED) {
			TIME--;
			if(TIME < 1) {
				transferToGame();
				TIME = 500;
				CYCLE = 500;
				STARTED = true;
			}
		}
		if(STARTED) {
			CYCLE--;
			if(WAVE > 10) {
				CYCLE = 500;
				TIME = 60;
				STARTED = false;
				endGame(true);
			}
			if(players.size() < 3) {
				//TODO: Same as below
				CYCLE = 500;
				TIME = 60;
				STARTED = false;
				endGame(false);
			}
			if(ZOMBIES < 1) {
				CYCLE += (WAVE * 50);
				for(Player allz : Server.s.playerHandler.players) {
					if(allz != null) {
						Client all = (Client) allz;
						if(players.contains(all.playerName)) {
							/*int premium = (int) (WAVE + (WAVE * 0.25)); 
							int extreme = (int) (WAVE + (WAVE * 0.5));
							if(all.isExtremeMember == 1) {
								all.zombiePoints += extreme;
							} 
							else if(all.isMember == 1) {
								all.zombiePoints += premium;  
							}
							else {
								all.zombiePoints += WAVE;
							}*/
							if(all.zombieKills > 0) {
								all.zombiePoints += (WAVE);
							} else {
								all.sendMessage("You receive no points for inactivity!");
							}
						}
					}
				}
				WAVE++;
				transferToGame();
			}
			if(CYCLE < 1) {
				CYCLE = 500;
				TIME = 60;
				STARTED = false;
				endGame(false);
			}
		}
	}

}
