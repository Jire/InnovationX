package src.innovationx.classic.model.player.combat.types;

import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.combat.DefaultType;
import src.innovationx.classic.model.player.combat.Defence;
import src.innovationx.classic.model.player.combat.Defence.CombatType;
import src.innovationx.classic.util.Misc;

public class RangeCombat implements DefaultType {
	
	/*
	 * The pullback data for arrows
	 */
	public int[][] pullbackGraphics = {
			{882, 19}, {884, 18}, {886, 20},
			{888, 21}, {890, 22}, {892, 24}
	};
	/*
	 * The projectile data for arrows
	 */
	public int[][] projectileGraphics = {
			{882, 10}, {884, 9}, {886, 11},
			{888, 12}, {890, 13}, {892, 15}
	};
	/*
	 * Gets the pullback graphic
	 */
	public int getPullback(int arrow) {
		for(int[] i : pullbackGraphics) {
			if(i[0] == arrow)
				return i[1];
		}
		return 23;
	}
	/*
	 * Gets the projectile graphic
	 */
	public int getProjectile(int arrow) {
		for(int[] i : projectileGraphics) {
			if(i[0] == arrow)
				return i[1];
		}
		return 14;
	}
	
	/*
	 * Returns the arrows allowed to be used by the bow
	 */
	public int[] arrowsAllowed(int bow) {
		switch(bow) {
		case 839:
		case 841:
			return new int[] {882, 884};
		case 843:
		case 845:
			return new int[] {882, 884, 886};
		case 847:
		case 849:
			return new int[] {882, 884, 886, 888};
		case 851:
		case 853:
			return new int[] {882, 884, 886, 888, 890};
		case 859:
		case 861:
			return new int[] {882, 884, 886, 888, 890, 892};
		case 7979:
			return new int[] {7988, 7989};
		case 7958:
			return new int[] {882, 884, 886, 888, 890, 892};
		case 4734:
			return new int[] {4740};
		}
		return null;
	}
	
	/*
	 * Checks to see if the player can use the arrows with the bow
	 */
	public boolean canUseArrows(Client p) {
		int wep = p.playerEquipment[p.playerWeapon];
		int arrows = p.playerEquipment[p.playerArrows];
		int[] allowed = arrowsAllowed(wep);
		for(int i : allowed)
			if(i == arrows)
				return true;
		return false;
	}
	
	public boolean checkArrows(Client p) {
		int arrows = p.playerEquipment[p.playerArrows];
		if(p.playerEquipment[p.playerWeapon] >= 4210 && p.playerEquipment[p.playerWeapon] <= 4225){
			return true;
		}
		if(arrows == -1) {
			p.sendMessage("You don't have any arrows equipt to use this weapon.");
			return false;
		}
		if(!canUseArrows(p)) {
			p.sendMessage("You can't use these arrows in your bow.");
			return false;
		}
		return true;
	}
	
	public boolean calucateDropArrow(Client p) {
		boolean ava = p.playerEquipment[p.playerCape] == 8102;
		if(ava) {
			if(Misc.random(75) > 50) {
				return false;
			}
			return true;
		} else {
			if(Misc.random(100) < 50) {
				return false;
			}
 		}
		return true;
	}
	

	@Override
	public void executeCombat(Client p, Client opp) {
		CombatType type = CombatType.RANGE;
		int delay = p.combat.getDistance() > 2 ? 1 : 0;
		int wep = p.playerEquipment[p.playerWeapon];
		int arrows = p.playerEquipment[p.playerArrows];
		int projectileSpeed = 57;
		int drawBack = getPullback(arrows);
		int projectile = getProjectile(arrows);
		int pX = opp.absX;
		int pY = opp.absY;
		int offsetX = (p.absX - pX) * -1;
		int offsetY = (p.absY - pY) * -1;
		p.calculateRange();
		int hit = Defence.getHit(p, opp, type, Misc.random(p.playerMaxHit));
		boolean ava = p.playerEquipment[p.playerCape] == 8102;
		boolean drops = calucateDropArrow(p);
		if(!checkArrows(p)) {
			p.combat.stopAttack();
			return;
		}
		if(wep == 7958) {
			if(p.playerEquipmentN[p.playerArrows] < 2) {
				p.sendMessage("You need to have two or more arrows to fire this bow.");
				p.combat.stopAttack();
				return;
			}
		}
		if(!(wep >= 4212 && wep <= 4222)) {
			if(drops) {
				if(!ava && Misc.random(100) > 50) {
					p.DeleteArrow();
				} else {
					if(ava && Misc.random(100) < 75) {
						p.DeleteArrow();
					}
				}
			} else {
				if(Misc.random(100) < 50) {
					p.DeleteArrow();
				}
			}
		}
		switch(wep) {
		case 839:
		case 841:
		case 843:
		case 845:
		case 847:
		case 849:
		case 851:
		case 853:
		case 859:
		case 861:
			p.startAnimation(426, 0);
			p.playGraphic(drawBack, 0, 100);
			p.createRangeProjectile(p.absY, p.absX, offsetY, offsetX, projectile, 43, 31, projectileSpeed, -p.combat.getEnemyIndex()- 1);
			opp.hitQueue.applyRangeDamage(p, hit,delay, drops);
			break;
		case 4734:
			p.startAnimation(2075, 0);
			p.createRangeProjectile(p.absY, p.absX, offsetY, offsetX, 27, 43, 31, projectileSpeed, -p.combat.getEnemyIndex()- 1);
			opp.hitQueue.applyRangeDamage(p, hit,delay, drops);
			break;
		case 7979:
			p.startAnimation(4230, 0);
			p.createRangeProjectile(p.absY, p.absX, offsetY, offsetX, 27, 43, 31, projectileSpeed, -p.combat.getEnemyIndex()- 1);
			if(p.playerEquipmentN[p.playerArrows] == 7989) {
				if(Misc.random(50) < 25) {
					opp.playGraphic(756, 0, 0);
					hit *= 1.25;
				}
			}
			opp.hitQueue.applyRangeDamage(p, hit,delay, drops);
		break;
		case 7958:
			p.startAnimation(426, 0);
			p.playGraphic(1111, 0, 100);
			p.createRangeProjectile(p.absY, p.absX, offsetY, offsetX, 1115, 43, 31, projectileSpeed, -p.combat.getEnemyIndex()- 1, 17, 44);
			p.createRangeProjectile(p.absY, p.absX, offsetY, offsetX, 1115, 43, 31, projectileSpeed + 20, -p.combat.getEnemyIndex()- 1, 26, 43);
			opp.hitQueue.applyRangeDamage(p, hit,delay, drops);
			hit = Defence.getHit(p, opp, type, Misc.random(p.playerMaxHit));
			opp.hitQueue.applyRangeDamage(p, hit,delay + 1, drops);
		break;
		case 4212:
		case 4214:
		case 4215:
		case 4216:
		case 4217:
		case 4218:
		case 4219:
		case 4220:
		case 4221:
		case 4222:
			p.startAnimation(426, 0);
			p.playGraphic(250, 0, 100);
			p.createRangeProjectile(p.absY, p.absX, offsetY, offsetX, 249, 43, 31, projectileSpeed, -p.combat.getEnemyIndex()- 1);
			opp.hitQueue.applyRangeDamage(p, hit,delay, false);
		}
	}
}
