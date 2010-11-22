package src.innovationx.classic.model.player.combat.specials;

import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.combat.Defence;
import src.innovationx.classic.model.player.combat.SpecialAtk;
import src.innovationx.classic.model.player.combat.Defence.CombatType;
import src.innovationx.classic.util.Misc;

public class StatiusWarHammer implements SpecialAtk {

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
		p.startAnimation(10505, 0);
		p.playGraphic(1840, 0, 0);
		p.calculateMelee();
		int maxHit = p.playerMaxHit;
		int hit = Defence.getHit(p, opp, type, Misc.random(maxHit));
		opp.hitQueue.applyMeleeDamage(p, hit);

	}

}
