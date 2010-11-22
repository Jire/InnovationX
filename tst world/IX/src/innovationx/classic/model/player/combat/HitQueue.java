package src.innovationx.classic.model.player.combat;

import java.util.LinkedList;
import java.util.Queue;

import src.innovationx.classic.Server;
import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.util.newevent.GameLogicTask;

public class HitQueue {

	/*
	 * Client instance
	 */
	private Client c;

	/*
	 * Queue for hits
	 */
	public Queue<Hit> hitQueue;

	/*
	 * Constructor
	 */
	public HitQueue(Client c) {
		setC(c);
		hitQueue = new LinkedList<Hit>();
	}

	/*
	 * Adds a new hit
	 */
	public void queueDamage(Hit hit) {
		hitQueue.add(hit);
	}

	/**
	 * Applies the damage dealt by melee
	 * 
	 * @param dealt
	 *            Who dealt it
	 * @param hit
	 *            How much to hit
	 */
	public void applyMeleeDamage(final Client dealt, final int hit) {
		Client p = getC();
		if (p.combat.getBlockDelay() < 1) {
			p.startAnimation(p.GetBlockEmote(p
					.getItemName(p.playerEquipment[p.playerWeapon])), 0);
		}
		//getC().combat.setCombatDelay(getC().combat.getCombatDelay() + 1);
		queueDamage(new Hit(hit, dealt.playerId));
	}
	
	public void applyMagicDamage(final Client dealt, int hit) {
		Client p = getC();
		if(p.HeadPray == 1) {
			hit = (int)Math.floor(hit * 0.65);
		}
		if (p.combat.getBlockDelay() < 1) {
			p.startAnimation(p.GetBlockEmote(p
					.getItemName(p.playerEquipment[p.playerWeapon])), 0);
		}
		queueDamage(new Hit(hit, dealt.playerId));
	}

	/**
	 * Applies the damage dealt by ranged combat
	 * 
	 * @param dealt
	 *            Who dealt it
	 * @param hit
	 *            How much to hit
	 */
	public void applyRangeDamage(final Client dealt, final int hit, int delay,
			final boolean dropsArrows) {
		final Client p = getC();
		if (delay > 0) {
			Server.s.eventManager.schedule(new GameLogicTask() {
				@Override
				public void run() {
					if (p.combat.getBlockDelay() < 1) {
						p.startAnimation(
								p.GetBlockEmote(p
										.getItemName(p.playerEquipment[p.playerWeapon])),
								0);
					}
					if (dropsArrows) {
						Server.s.itemHandler.NpcDropItem(
								dealt.playerEquipment[dealt.playerArrows], 1,
								getC().absX, getC().absY, dealt.playerId);
					}
			//		getC().combat.setCombatDelay(getC().combat.getCombatDelay() + 1);
					queueDamage(new Hit(hit, dealt.playerId));
					this.stop();
				}
			}, delay, 0);
		} else {
			if (p.combat.getBlockDelay() < 1) {
				p.startAnimation(p.GetBlockEmote(p
						.getItemName(p.playerEquipment[p.playerWeapon])), 0);
			}
			if (dropsArrows) {
				Server.s.itemHandler.NpcDropItem(
						dealt.playerEquipment[dealt.playerArrows], 1,
						getC().absX, getC().absY, dealt.playerId);
			}
			//getC().combat.setCombatDelay(getC().combat.getCombatDelay() + 1);
			queueDamage(new Hit(hit, dealt.playerId));
		}
	}

	/*
	 * Applys damage
	 */
	public void applyDamage(int hit, int slot, int dealt) {
		getC().updateHp(hit, false);
		if (slot == 1) {
			getC().updateRequired = true;
			getC().hitUpdateRequired = true;
			getC().hitDiff = hit;
			getC().KilledBy[dealt] += hit;
		}
		if (slot == 2) {
			getC().updateRequired = true;
			getC().hitUpdateRequired2 = true;
			getC().hitDiff2 = hit;
			getC().KilledBy[dealt] += hit;
		}
	}

	public void processHitQueue() {
		if (hitQueue.isEmpty())
			return;
		if(getC().IsDead) {
			hitQueue.clear();
			return;
		}
		if (!getC().hitUpdateRequired) {
			if (hitQueue.size() > 0) {
				Hit h = hitQueue.poll();
				int hit = h.getHit();
				if(hit > getC().playerLevel[3]) {
					hit = getC().playerLevel[3];
				}
				applyDamage(hit, 1, h.getDealt());
			}
		}
		if (!getC().hitUpdateRequired2) {
			if (hitQueue.size() > 0) {
				Hit h = hitQueue.poll();
				int hit = h.getHit();
				if(hit > getC().playerLevel[3]) {
					hit = getC().playerLevel[3];
				}
				applyDamage(hit, 2, h.getDealt());
			}
		}
	}

	/*
	 * Setter
	 */
	public void setC(Client c) {
		this.c = c;
	}

	/*
	 * Getter
	 */
	public Client getC() {
		return c;
	}

}
