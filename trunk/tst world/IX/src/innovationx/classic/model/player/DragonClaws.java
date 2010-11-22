package src.innovationx.classic.model.player;

import src.innovationx.classic.model.npc.NPC;
import src.innovationx.classic.util.Misc;
import src.innovationx.classic.util.event.Event;
import src.innovationx.classic.util.event.EventContainer;
import src.innovationx.classic.util.event.EventManager;

/**
* DragonClaws class does the last 3 hits in a dragon claws special.
* @author Canownueasy
*
*/

public class DragonClaws {

	/**
		* Loading the player to this class.
		*/
	@SuppressWarnings("unused")
	private Client c;
	
	public DragonClaws(Client c) {
		this.c = c;
	}
	public boolean hits(Client p, Client opp) {
		return(Misc.random(p.meleeAtk(true)) > Misc.random(opp.meleeDef()));
	}
	public int getDamageOnDefence(Client p, Client opp) {
		int damageHit = 0;
		if(hits(p, opp)) {
			damageHit =  Misc.random(p.playerMaxHit);
			if (p.HeadPray == 3) {
				damageHit = Misc.random((p.playerMaxHit - (int)(p.playerMaxHit * 0.4)));
			}
			return damageHit;
		}
		return 0;
	}
	
	public void startClawsSpecial(Client p, Client opp) {
		if (p == null || opp == null) {
			return;
		}
		double calcHit1 = 0;
		double calcHit2 = 0;
		double calcHit3 = 0;
		double calcHit4 = 0;
		int playerHit = 0;
		int playerHit2 = 0;
		int playerHit3 = 0;
		int playerHit4 = 0;
		boolean fail = false;
		calcHit1 = getDamageOnDefence(p, opp);
		calcHit2 = calcHit1 / 2;
		calcHit3 = calcHit2 / 2;
		calcHit4 = calcHit3 + 1;
		if (calcHit1 == 0) {
			calcHit2 = getDamageOnDefence(p, opp); // defence method?
			calcHit3 = calcHit2 / 2;
			calcHit4 = calcHit3 + 1;
		}
		if (calcHit2 == 0) {
			calcHit3 = getDamageOnDefence(p, opp);
		}
		if (calcHit3 == 0) {
			fail = true;
		}
		playerHit = (int) calcHit1;
		playerHit2 = (int) calcHit2;
		playerHit3 = (int) calcHit3;
		if (!fail) {
			opp.appendDelayedHit(playerHit, 1);
			opp.appendDelayedHit(playerHit2, 1);
			opp.appendDelayedHit(playerHit3, 3);
			opp.appendDelayedHit(playerHit3+1, 3); // delays were 4 before i changed to 3 so compile and update
		} else {
			opp.appendDelayedHit(0, 1);
			opp.appendDelayedHit(1, 1);
		}
	}
	public boolean appendInstantHit(final int hit, final Client opp,
	final Client p) {
		try {
			EventManager.getSingleton().addEvent(new Event() {
				public void execute(EventContainer c) {
					opp.appendInstantHit(hit);
					c.stop();
				}
			}, 310);
		} catch (Exception E) {
		}
		return false;
	}
	public boolean appendClaws(final int hit, final int hit2,final Client p, final Client opp) {
		try {
			EventManager.getSingleton().addEvent(new Event() {
				public void execute(EventContainer c) {
					opp.setClaws(hit);
					opp.appendInstantHit(hit2);
					c.stop();
				}
			}, 310);
		} catch (Exception E) {
		}
		return false;
	}

	
	/**
		* Checks how many hits have been completed in a special event.
		*/
	private int completedHits = 0;
	
	/**
		* Used for calculating the last hit.
		*/
	private int thirdHit = 0;
	
	/**
		* Does the last 3 special hits on a player.
		* @param source The player attempting to do a dragon claws special hit.
		* @param victim The player getting hit by param source.
		* @param lastHit The last hit the source has dealt to the victim.
		*/
	public void clawPlayer(final Client source, final Client victim, int lastHit) {
		try {
			if (source == null) {
				completedHits = 0;
				return;
			}
			if (victim == null) {
				completedHits = 0;
				return;
			}
			if (victim.IsDead || source.IsDead) {
				completedHits = 0;
				return;
			}
			if (completedHits > 2) {
				completedHits = 0;
				return;
			}
			if (completedHits == 2) {
				lastHit = (thirdHit - lastHit);
			} else {
			} 		
			lastHit /= 2;
			if (lastHit > victim.currentHealth) {
				lastHit = victim.currentHealth;
			}
			final int damage = lastHit;
			if (completedHits == 1) {
				thirdHit = damage;
			}
			hitPlayer(source, victim, damage);
			completedHits++;
			if (completedHits == 1) {
				if (damage > 0) {
					new java.util.Timer().schedule(new java.util.TimerTask() {
						@Override
						public void run() {
							clawPlayer(source, victim, damage); //tom no. allow me xDD dude they are perfect look I just need spot to put the method
						}
					}, 180);
				}
			} else {
				clawPlayer(source, victim, damage);
			}
		} catch (Exception e) {
			System.out.println("Dragon claws special error on players");
		}
	}
	
	/**
		* Does the last 3 special hits on a npc.
		* @param source The player attempting to do a dragon claws special hit.
		* @param victim The npc getting hit by param source.
		* @param lastHit The last hit the source has dealt to the victim.
		*/
	public void clawNPC(final Client source, final NPC victim, int lastHit) {
		try {
			if (source == null) {
				completedHits = 0;
				return;
			}
			if (victim == null) {
				completedHits = 0;
				return;
			}
			if (victim.IsDead || victim.CurrentHp == 0) {
				completedHits = 0;
				return;
			}
			if (completedHits > 2) {
				completedHits = 0;
				return;
			}
			if (completedHits == 2) {
				lastHit = (thirdHit - lastHit);
			} else {
				lastHit /= 2;
			}
			if (lastHit > victim.CurrentHp) {
				lastHit = victim.CurrentHp;
			}
			final int damage = lastHit;
			if (completedHits == 1) {
				thirdHit = damage;
			}
			hitNPC(source, victim, damage);
			completedHits++;
			if (completedHits == 1) {
				if (damage > 0) {
					new java.util.Timer().schedule(new java.util.TimerTask() {
						@Override
						public void run() {
							clawNPC(source, victim, damage);
						}
					}, 180);
				}
			} else {
				clawNPC(source, victim, damage);
			}
		} catch (Exception e) {
			System.out.println("Dragon claws special error on npc's");
		}
	}
	
	/**
		* Does the hitupdate masks for a player.
		* @param source The player executing the hit.
		* @param victim The player receiving the hit.
		* @param damage The damage the victim should receive.
		*/
	public void hitPlayer(Client source, Client victim, int damage) {
		try {
			if (source == null) {
				return;
			}
			if (victim == null) {
				return;
			}
			if (!victim.hitUpdateRequired) {
				victim.hitUpdateRequired = true;
				victim.hitDiff = damage;
			} else {
				victim.hitUpdateRequired2 = true;
				victim.hitDiff2 = damage;
			}
			victim.appendHit(damage);
			victim.updateRequired = true;
			victim.appearanceUpdateRequired = true;
			victim.combatWith = source.playerId;
			victim.attackingPlayerId = source.playerId;
			//source.addSkillXP(damage / 3* source.combatXpInc, 0);
			//source.addSkillXP(damage / 3 * source.combatXpInc, 1);
			//source.addSkillXP(damage /3 * source.combatXpInc, 2);
			//source.addSkillXP(damage / 4 * source.combatXpInc, source.playerHitpoints);
		} catch (Exception e) {
			System.out.println("Error in hitupdate masks, class DragonClaws.");
		}
	}
	
	/**
		* Does the hitupdate masks for a npc.
		* @param source The player executing the hit.
		* @param victim The npc receiving the hit.
		* @param damage The damage the victim should receive.
		*/
	public void hitNPC(Client source, NPC victim, int damage) {
		try {
			if (source == null) {
				return;
			}
			if (victim == null) {
				return;
			}
			if (!victim.hitUpdateRequired) {
				victim.hitDiff = damage;
				victim.hitUpdateRequired = true;
			} else {
				victim.hitDiff2 = damage;
				victim.hitUpdateRequired2 = true;
			}	
			victim.IsUnderAttack = true;
			victim.RandomWalk = false;
			victim.updateRequired = true;
			victim.combatWith = source.playerId;
			victim.IsUnderAttack = true;
			//source.addSkillXP(damage / 3* source.combatXpInc, 0);
			//source.addSkillXP(damage / 3 * source.combatXpInc, 1);
			//source.addSkillXP(damage /3 * source.combatXpInc, 2);
			//source.addSkillXP(damage / 4 * source.combatXpInc, source.playerHitpoints);
		} catch (Exception e) {
			System.out.println("Error in hitupdate masks, class DragonClaws.");
		}
	}
	
}