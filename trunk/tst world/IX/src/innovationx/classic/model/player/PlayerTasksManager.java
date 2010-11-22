package src.innovationx.classic.model.player;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import src.innovationx.classic.model.player.task.PlayerTask;

public class PlayerTasksManager {
	
	/**
	 * Constructor
	 */
	public PlayerTasksManager(Client plr) {
		setPlr(plr);
	}
	
	/**
	 * @param plr the plr to set
	 */
	public void setPlr(Client plr) {
		this.plr = plr;
	}

	/**
	 * @return the plr
	 */
	public Client getPlr() {
		return plr;
	}

	/**
	 * @return the tasks
	 */
	public BlockingQueue<PlayerTask> getTasks() {
		return tasks;
	}

	/**
	 * @return the workService
	 */
	public ExecutorService getWorkService() {
		return workService;
	}

	/*
	 * Player instance
	 */
	private Client plr;
	
	/*
	 * Ran every 600ms
	 */
	public void executeTasks() {
		PlayerTask task;
		try {
			task = getTasks().take();
			boolean needsRemoving = task.getStop();
			getWorkService().execute(task);
			if(!needsRemoving) {
				tasks.add(task);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
	
	/*
	 * Tasks queue 
	 */
	private final BlockingQueue<PlayerTask> tasks = new LinkedBlockingQueue<PlayerTask>();
	private final ExecutorService workService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
}
