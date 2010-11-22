package src.innovationx.classic.model.player.content;

public class EquipmentData {
	
	private int id, slot, unknown;
	
	public EquipmentData(int id, int slot, int unknown) {
		this.setId(id);
		this.setSlot(slot);
		this.setUnknown(unknown);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public int getSlot() {
		return slot;
	}

	public void setUnknown(int unknown) {
		this.unknown = unknown;
	}

	public int getUnknown() {
		return unknown;
	}

}
