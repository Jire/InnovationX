package src.innovationx.classic.model.player.combat.specials;

import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.combat.Defence;
import src.innovationx.classic.model.player.combat.Defence.CombatType;
import src.innovationx.classic.model.player.combat.SpecialAtk;
import src.innovationx.classic.util.Misc;

public class DragonDagger implements SpecialAtk {

	@Override
	public void execute(Client p, Client opp) {
		if(p.specialAmount < 25) {
			p.sendMessage("You don't have enough special energy left.");
			p.resetAtk();
			p.toggleSpec();
			return;
		}
		p.specialAmount -= 25;
		p.calculateMelee();
		int hit1 = Misc.random(p.playerMaxHit);
		int hit2 = Misc.random(p.playerMaxHit);
		int def1 = Defence.getHit(p, opp, CombatType.MELEE, hit1);
		int def2 = Defence.getHit(p, opp, CombatType.MELEE, hit2);
		p.startAnimation(0x426, 0);
		p.playGraphic(252, 0, 100);
		opp.hitQueue.applyMeleeDamage(p, def1);
		opp.hitQueue.applyMeleeDamage(p, def2);
		

	}

}
