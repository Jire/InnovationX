package src.innovationx.classic.content.minigames;
import java.util.ArrayList;
import java.util.List;

import src.innovationx.classic.model.player.Client;

/**
* For that shitty minigame called Clan Wars :D
* @author Harry Andreas <harrythecomputerwizkid@msn.com>
* @author Canownueasy <tgpn1996@hotmail.com>
*/

public class ClanWars {

	/*
	* Players in the blue team
	*/
	public List<Client> teamBluePlayers = new ArrayList<Client>();

	/*
	* Players in the red team
	*/
	public List<Client> teamRedPlayers = new ArrayList<Client>();

	/*
	* Constants
	*/
	public final int OUT_X = 2576;
	public final int OUT_Y = 9518; 
	public final int BLUE_START_X = 2849; //2849, 9775 : 2937, 9783
	public final int BLUE_START_Y = 9775;
	public final int RED_START_X = 2937;
	public final int RED_START_Y = 9783;
	public boolean dangerousGame = false;
	public boolean gameStarted = false;
	public int waitTimer = 360;
	public int gameTimer = 1800;
	
	public int BLUE_HOOD = 4513;
	public int BLUE_CAPE = 4514;
	
	public int RED_HOOD = 4515;
	public int RED_CAPE = 4516;
	
	public int totalPlayers = teamBluePlayers.size() + teamRedPlayers.size();

	/*
	* Ticks at 500ms interval's
	*/
	public void tick() {
		totalPlayers = teamBluePlayers.size() + teamRedPlayers.size();
		if (teamBluePlayers.size() < 1 || teamRedPlayers.size() < 1) {
			return;
		}
		if (waitTimer > 0) {
			waitTimer--;
		}
		if (waitTimer == 0) {
			if (!gameStarted) {
				setGameStarted(true);
				startGame();
			}
			if (gameStarted) {
				if (gameTimer > 0) {
					gameTimer--;
				}
				if (gameTimer == 0) {
					waitTimer = 360;
					setGameStarted(false);
				}
			}
		}
	}
	
	/*
	* Sets if the game has started or not
	*/
	public void setGameStarted(boolean flag) {
		gameStarted = flag;
	}
	
	/*
	* Teleports the players waiting to there starting area
	*/
	public void startGame() {
		System.out.println("[CLAN WARS]: New game started...!");
		for(Client b : teamBluePlayers) {
			b.changeCoords(BLUE_START_X, BLUE_START_Y, 4);
		}
		for(Client r: teamRedPlayers) {
			r.changeCoords(RED_START_X, RED_START_Y, 4);
		}
	}
	
	/*
	* Deals with death in clanwars
	*/
	public void playerDied(Client p) {
		if(teamBluePlayers.contains(p)) {
			removePlayer(p, "Blue");
			checkWinner();
			return;
		}
		if(teamRedPlayers.contains(p)) {
			removePlayer(p, "Red");
			checkWinner();
			return;
		}
	}
	
	/*
	* Checks for a winning team
	*/
	public void checkWinner() {
		String teamWon = "none";
		boolean endGame = false;
		if(teamBluePlayers.size() < 1) {
			teamWon = "Red";
			endGame = true;
		}
		if(teamBluePlayers.size() < 1) {
			teamWon = "Blue";
			endGame = true;
		} 
		if(teamBluePlayers.size() < 1 && teamRedPlayers.size() < 1) {
			teamWon = "No-one";
			endGame = true;
		}	
		if(endGame) {
			for(Client b : teamBluePlayers) {
				if(teamWon.equalsIgnoreCase("Blue")) {
					b.showDialogue("Congratulations! In your valient efforts, your team conquered the opponents!");
					b.sendMessage("And for your efforts you recieve a reward of 3 tokens!");
					b.clanWarsPoints += 3;
				} else {
					b.showDialogue("Sadly, your opponents went above and beyond; you lost.");
					b.sendMessage("Dispite your loss, you recieve 1 token!");
					b.clanWarsPoints++;
				}
				b.changeCoords(OUT_X, OUT_Y, 0);
			}
			for(Client d : teamRedPlayers) {
				if(teamWon.equalsIgnoreCase("Red")) {
					d.showDialogue("Congratulations! In your valient efforts, your team conquered the opponents!");
					//b.sendMessage("And for your efforts you recieve a reward of 50 tokens!");
				} else {
					d.showDialogue("Sadly, your opponents went above and beyond; you lost.");
					//b.sendMessage("Dispite your loss, you recieve 15 tokens!");
				}
				d.changeCoords(OUT_X, OUT_Y, 0);	
			}
			teamBluePlayers.clear();
			teamRedPlayers.clear();
		}
	}
	
	/*
	* Removes the player from the game
	*/
	public void removePlayer(Client p, String team) {
		if(team.equalsIgnoreCase("Red")) {
			if(teamRedPlayers.contains(p)) {
				teamRedPlayers.remove(p);
			}
		}
		if(team.equalsIgnoreCase("Blue")) {
			if(teamBluePlayers.contains(p)) {
				teamBluePlayers.remove(p);
			}
		}
		p.setEquipment(-1, 0, p.playerCape);
		p.setEquipment(-1, 0, p.playerHat);
		//p.wear();
		p.teleBlockDelay = 0;
		p.updateRequired = true;
		p.teleport(2848, 3331);
		p.heightLevel = 0;
	}
	
	/*
	* Adds the player into the game
	*/
	public void addPlayer(Client p, String team) {
		if((p.playerEquipment[p.playerCape] != -1) || (p.playerEquipment[p.playerHat] != -1)) {
			p.showDialogue("You cannot enter Clan Wars with a cape or hood!");
			return;
		}
		if(team.equalsIgnoreCase("Red")) {
			if(!teamRedPlayers.contains(p)) {
				teamRedPlayers.add(p);
				p.changeCoords(2717, 9816, 4);
				p.setEquipment(RED_CAPE, 1, p.playerCape);
				p.setEquipment(RED_HOOD, 1, p.playerHat);
			}
		}
		if(team.equalsIgnoreCase("Blue")) {
			if(!teamBluePlayers.contains(p)) {
				teamBluePlayers.add(p);
				p.changeCoords(2717, 9816, 8);
				p.setEquipment(BLUE_CAPE, 1, p.playerCape);
				p.setEquipment(BLUE_HOOD, 1, p.playerHat);
			}
		}
		//p.wear();
		p.teleBlockDelay = 9999999;
		p.updateRequired = true;
		p.showDialogue("You join forces for the " + team + " team!");
	}
	
}