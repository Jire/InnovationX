package src.innovationx.classic.model.player.combat.specials;

import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.combat.Defence;
import src.innovationx.classic.model.player.combat.SpecialAtk;
import src.innovationx.classic.model.player.combat.Defence.CombatType;
import src.innovationx.classic.util.Misc;

public class VestaLongsword implements SpecialAtk {

	@Override
	public void execute(Client p, Client opp) {
		if(p.specialAmount < 25) {
			p.sendMessage("You don't have enough special energy left.");
			p.resetAtk();
			p.toggleSpec();
			return;
		}
		p.specialAmount -= 25;
		p.startAnimation(10502, 0);
		CombatType type = CombatType.MELEE;
		p.calculateMelee();
		int maxHit = (int)(p.playerMaxHit * 1.15);
		int hit = Defence.getHit(p, opp, type, Misc.random(maxHit));
		opp.hitQueue.applyMeleeDamage(p, hit);
	}

}
