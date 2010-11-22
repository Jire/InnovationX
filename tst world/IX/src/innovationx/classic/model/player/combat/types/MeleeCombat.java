package src.innovationx.classic.model.player.combat.types;

import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.combat.DefaultType;
import src.innovationx.classic.model.player.combat.Defence;
import src.innovationx.classic.model.player.combat.Defence.CombatType;
import src.innovationx.classic.util.Misc;

public class MeleeCombat implements DefaultType {

	@Override
	public void executeCombat(Client p, Client opp) {
		p.calculateMelee();
		int maxHit = Misc.random(p.playerMaxHit);
		int hit = Defence.getHit(p, opp, CombatType.MELEE, maxHit);
		p.startAnimation(p.combat.getWeaponAnimation(), 0);
		opp.hitQueue.applyMeleeDamage(p, hit);
	}

}
