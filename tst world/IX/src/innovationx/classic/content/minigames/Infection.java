package src.innovationx.classic.content.minigames;
import java.util.ArrayList;

import src.innovationx.classic.model.player.Client;

/**
 * For the infection minigame.
 * @author Canownueasy
 */

public class Infection {
	
	/**
	 * All of the players in the game.
	 */
	public ArrayList<Client> allPlayers = new ArrayList<Client>();
	
	/**
	 * All of the remaining human players.
	 */
	public ArrayList<Client> humanPlayers = new ArrayList<Client>();
	
	/**
	 * The infected player "Michael Myers"
	 */
	public Client infected;
	
	public void chooseInfected() {
		for(Client all : allPlayers) {
			if(all != null) {
				
			}
		}
	}

}
