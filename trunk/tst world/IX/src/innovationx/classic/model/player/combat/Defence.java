package src.innovationx.classic.model.player.combat;

import java.util.Random;

import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.util.Misc;

public class Defence {
	
	/*
	 * Defence modifier
	 */
	public static final double MODIFIER = 0.905;
	
	/*
	 * Enum containing the combat type's
	 */
	public enum CombatType {
		MELEE(0),
		RANGE(1),
		MAGIC(2);
		int type;
		CombatType(int type) {
			this.type = type;
		}
	}
	
	/*
	 * Gets the accuracy of each hit
	 */
	public static double getAccuracy(CombatType type, Client atker, Client opp) {
		int bonus = 1;
		int level = 0;
		if(type == CombatType.MELEE) {
			level = 0;
			bonus = atker.skillId - 1;
		}
		if(type == CombatType.RANGE) {
			level = 4;
			bonus = 4;
		}
		if(type == CombatType.MAGIC) {
			level = 6;
			bonus = 5;
		}
		if(bonus < 0) {
			bonus = 0;
		}
		double atkBonus = atker.playerBonus[bonus] < 1 ? 0 : atker.playerBonus[bonus];
		double defBonus = opp.playerBonus[bonus + 5] < 1 ? 0 : opp.playerBonus[bonus + 5];
		double atk = (atkBonus * atker.playerLevel[level]);
		double def = (defBonus * opp.playerLevel[1]);
		atk += 3;
		int wep = atker.playerEquipment[atker.playerWeapon];
		if(wep == 8907) {
			atk = atk * 0.45;
		}
		if(atker.UsingSpecial) {
			switch(wep) {
			case 1434:
			case 5698 :
				atk = atk * 1.55;
			break;
			case 8086:
			case 4153:
				atk = atk * 1.30;
				break;
			}
		}
		double prayAtkBonus =  1 + (atker.AtkPray / 100);
		double prayDefBonus = 1 + (opp.DefPray / 100);
		if(prayAtkBonus < 0) {
			prayAtkBonus = 1.0;
		}
		if(prayDefBonus < 0) {
			prayDefBonus = 1.0;
		}
		if(type == CombatType.MELEE) {
			if(opp.HeadPray == 3) {
				def = def * 1.5;
			}
		}
		if(type == CombatType.RANGE) {
			if(opp.HeadPray == 2) {
				def = def * 1.5;
			}
		}
		atk = atk * prayAtkBonus;
		def = def * prayDefBonus;
		double outCome = MODIFIER * (atk/def);
		return outCome;
	}
	
	/*
	 * Determines wether the attack will hit or not
	 */
	public static int getHit(Client atker, Client opp, CombatType type, int hit) {
		double accuracy = getAccuracy(type, atker, opp);
		Random random = new Random();
		if(type == CombatType.MELEE) {
			if(opp.HeadPray == 3) {
				hit = (int)(hit * 0.45);
			}
		}
		if(type == CombatType.RANGE) {
			if(opp.HeadPray == 2) {
				hit = (int) (hit * 0.45);
			}
		}
		if(accuracy > 1.0) {
			accuracy = 1;
		}
		if(accuracy < random.nextDouble()) {
			if(type == CombatType.MAGIC) {
				return -1;
			}
			return 0;
		} else {
			return hit;
		}
	}

}
