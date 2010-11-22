package src.innovationx.classic.model.player.combat.specials;

import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.combat.Defence;
import src.innovationx.classic.model.player.combat.Defence.CombatType;
import src.innovationx.classic.model.player.combat.SpecialAtk;
import src.innovationx.classic.util.Misc;

public class DragonMace implements SpecialAtk {

	@Override
	public void execute(Client p, Client opp) {
		if(p.specialAmount < 25) {
			p.sendMessage("You don't have enough special energy left.");
			p.resetAtk();
			p.toggleSpec();
			return;
		}
		p.specialAmount -= 25;
		p.startAnimation(1060, 0);
		p.playGraphic(251, 0, 100);
		CombatType type = CombatType.MELEE;
		p.calculateMelee();
		int max = (int)(p.playerMaxHit * 1.05);
		int hit = Defence.getHit(p, opp, type, Misc.random(max));
		opp.hitQueue.applyMeleeDamage(p, hit);
	}

}
