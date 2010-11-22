package src.innovationx.classic.model.player.combat;

public class Hit {
	
	public Hit(int hit, int dealt) {
		setHit(hit);
		setDealt(dealt);
	}
	
	public void setHit(int hit) {
		this.hit = hit;
	}

	public int getHit() {
		return hit;
	}

	public void setDealt(int dealt) {
		this.dealt = dealt;
	}

	public int getDealt() {
		return dealt;
	}

	private int hit, dealt;

}
