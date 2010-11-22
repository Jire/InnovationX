package src.innovationx.classic.model.player.combat.specials;

import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.combat.Defence;
import src.innovationx.classic.model.player.combat.SpecialAtk;
import src.innovationx.classic.model.player.combat.Defence.CombatType;
import src.innovationx.classic.model.player.combat.types.RangeCombat;
import src.innovationx.classic.util.Misc;

public class DarkBow implements SpecialAtk {

	@Override
	public void execute(Client p, Client opp) {
		CombatType type = CombatType.RANGE;
		if(p.specialAmount < 75) {
			p.sendMessage("You don't have enough special energy left.");
			p.resetAtk();
			p.toggleSpec();
			return;
		}
		if(!new RangeCombat().checkArrows(p)) {
			p.combat.stopAttack();
			return;
		}
		if(p.playerEquipmentN[p.playerArrows] < 2) {
			p.sendMessage("You need to have two or more arrows to fire this bow.");
			p.combat.stopAttack();
			return;
		}
		p.specialAmount -= 75;
		int projectile =  1099;
		int pX = opp.absX;
		int pY = opp.absY;
		int offsetX = (p.absX - pX) * -1;
		int offsetY = (p.absY - pY) * -1;
		p.calculateRange();
		int max = (int)(p.playerMaxHit * 1.10) + 8;
		int hit = Defence.getHit(p, opp, type, Misc.random(max));
		for(int i = 0; i < 2; i++) {
			p.DeleteArrow();
		}
		p.startAnimation(426, 0);
		p.playGraphic(1111, 0, 100);
		int delay = p.combat.getDistance() > 2 ? 1 : 0;
		p.createRangeProjectile(p.absY, p.absX, offsetY, offsetX, projectile, 43, 31, 80, -p.combat.getEnemyIndex()- 1, 16, 44);
		p.createRangeProjectile(p.absY, p.absX, offsetY, offsetX, projectile, 43, 31, 105, -p.combat.getEnemyIndex()- 1, 23, 43);
		if(hit < 8)
			hit = 8;
		opp.hitQueue.applyRangeDamage(p, hit, delay , false);
		hit = Defence.getHit(p, opp, type, Misc.random(max));
		if(hit < 8)
			hit = 8;
		opp.hitQueue.applyRangeDamage(p, hit, delay + 1, false);
	}

}
