package src.innovationx.classic.model.player.task.impl;

import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.task.PlayerTask;

public class LoadAreaTask extends PlayerTask {
	
	/**
	 * Constructor
	 * @param plr
	 */
	public LoadAreaTask(Client plr) {
		this.plr = plr;
	}
	
	/*
	 * Instance
	 */
	public Client plr;
	
	/**
	 * Getter
	 * @return
	 */
	public Client getPlr() {
		return plr;
	}

	@Override
	public void run() {
		getPlr().UpdateArea();
		setStop(true);
	}

}
