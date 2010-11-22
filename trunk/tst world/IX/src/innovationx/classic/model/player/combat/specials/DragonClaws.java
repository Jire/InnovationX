package src.innovationx.classic.model.player.combat.specials;

import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.combat.Defence;
import src.innovationx.classic.model.player.combat.Defence.CombatType;
import src.innovationx.classic.model.player.combat.SpecialAtk;
import src.innovationx.classic.util.Misc;

public class DragonClaws implements SpecialAtk {

	@Override
	public void execute(Client p, Client opp) {
		if(p.specialAmount < 50) {
			p.sendMessage("You don't have enough special energy left.");
			p.resetAtk();
			p.toggleSpec();
			return;
		}
		p.specialAmount -= 50;
		CombatType type = CombatType.MELEE;
		p.startAnimation(10961, 0);
		p.playGraphic(1950, 0, 0);
		p.calculateMelee();
		int maxHit = p.playerMaxHit;
		int hit = Defence.getHit(p, opp, type, Misc.random(maxHit));
		int hit2 = hit / 2;
		int hit3 = hit2 / 2;
		int hit4 = hit3 + 1;
		boolean failed = false;
		if(hit == 0) {
			hit2 = Defence.getHit(p, opp, type, Misc.random(maxHit));
			hit3 = hit2 / 2;
			hit4 = hit3 + 1;
		}
		if(hit2 == 0) {
			hit3 = Defence.getHit(p, opp, type, Misc.random(maxHit));
			hit4 = hit3 + 1;
		}
		if(hit3 == 0) {
			hit4 = Defence.getHit(p, opp, type, Misc.random(maxHit));
		}
		failed = hit4 == 0;
		if(failed) {
			opp.hitQueue.applyMeleeDamage(p, 0);
			opp.hitQueue.applyMeleeDamage(p, 1);
		} else {
			opp.hitQueue.applyMeleeDamage(p, hit);
			opp.hitQueue.applyMeleeDamage(p, hit2);
			opp.hitQueue.applyMeleeDamage(p, hit3);
			opp.hitQueue.applyMeleeDamage(p, hit4);
		}

	}

}
