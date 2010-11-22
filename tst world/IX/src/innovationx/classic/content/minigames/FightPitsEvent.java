package src.innovationx.classic.content.minigames;

import src.innovationx.classic.util.event.EventContainer;

public class FightPitsEvent {

	public FightPitsEvent() {
		//super(1000);
	}

	public void execute(EventContainer event) {
		FightPits.tick();
	}

}