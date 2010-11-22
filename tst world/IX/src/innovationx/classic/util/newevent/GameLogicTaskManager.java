package src.innovationx.classic.util.newevent;

import java.util.ArrayList;
import java.util.List;

public class GameLogicTaskManager {

	private static List<GameLogicTaskInformation>[] currentTasks;
	
	@SuppressWarnings("unchecked")
	public GameLogicTaskManager() {
		currentTasks = new ArrayList[50];
		for(int index = 0; index < currentTasks.length; index++)
			currentTasks[index] = new ArrayList<GameLogicTaskInformation>();
	}
	
	/*
	 * Processed each 600ms at game logic thread
	 */
	public void processTasks() {
		synchronized(currentTasks) {
			for(int priority = 0; priority < currentTasks.length; priority++) {
				if(currentTasks[priority].isEmpty())
					continue;
				for(Object t : currentTasks[priority].toArray()) {
					GameLogicTaskInformation task = (GameLogicTaskInformation) t;
					if(task.continueCount > 0) {
						task.continueCount--;
						continue;
					}
					task.task.run();
					if(task.task.needRemove)
						currentTasks[priority].remove(task);
					else
						task.continueCount = task.continueMaxCount;
				}
			}
		}
	}
	
	public void schedule(GameLogicTask task, int delayCount, int periodCount, int priority) {
		if(task == null || delayCount < 0 || periodCount < 0 || priority < 0 || priority > currentTasks.length)
			return;
		synchronized(currentTasks) {
			currentTasks[priority].add(new GameLogicTaskInformation(task, delayCount, periodCount));
		}
	}
	public void schedule(GameLogicTask task, int delayCount, int priority) {
		if(task == null || delayCount < 0 || priority < 0 || priority > currentTasks.length)
			return;
		synchronized(currentTasks) {
			currentTasks[priority].add(new GameLogicTaskInformation(task, delayCount, -1));
		}
	}
	
	public void schedule(GameLogicTask task, int priority) {
		if(task == null || priority < 0 || priority > currentTasks.length)
			return;
		synchronized(currentTasks) {
			currentTasks[priority].add(new GameLogicTaskInformation(task, 0, -1));
		}
	}
	
}

class GameLogicTaskInformation {

	protected GameLogicTask task;
	protected int continueMaxCount;
	protected int continueCount;
	

	public GameLogicTaskInformation(GameLogicTask task, int continueCount, int continueMaxCount) {
		this.task = task;
		this.continueCount = continueCount;
		this.continueMaxCount = continueMaxCount;
		if(continueMaxCount == -1)
			task.needRemove = true;
	}
}