package src.innovationx.classic.model.player.combat.specials;

import src.innovationx.classic.Server;
import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.combat.Defence;
import src.innovationx.classic.model.player.combat.SpecialAtk;
import src.innovationx.classic.model.player.combat.Defence.CombatType;
import src.innovationx.classic.model.player.combat.types.RangeCombat;
import src.innovationx.classic.util.Misc;
import src.innovationx.classic.util.newevent.GameLogicTask;

public class MagicShortbow implements SpecialAtk {

	@Override
	public void execute(final Client p, Client opp) {
		if(p.specialAmount < 60) {
			p.sendMessage("You don't have enough special energy left.");
			p.resetAtk();
			p.toggleSpec();
			return;
		}
		if(!new RangeCombat().checkArrows(p)) {
			p.combat.stopAttack();
			return;
		}
		p.specialAmount -= 60;
		p.startAnimation(1074, 0);
		p.playGraphic(256, 0, 100);
		Server.s.eventManager.schedule(new GameLogicTask() {
			@Override
			public void run() {
				p.playGraphic(256, 0, 100);
				this.stop();				
			}
		}, 0, 0);
		int delay = p.combat.getDistance() > 2 ? 1 : 0;
		int projectile = 249;
		int pX = opp.absX;
		int pY = opp.absY;
		int offsetX = (p.absX - pX) * -1;
		int offsetY = (p.absY - pY) * -1;
		p.calculateRange();
		CombatType type = CombatType.RANGE;
		int max = (int)(p.playerMaxHit * 1.10);
		int hit = Defence.getHit(p, opp, type, Misc.random(max));
		for(int i = 0; i < 2; i++) {
			p.DeleteArrow();
		}
		p.createRangeProjectile(p.absY, p.absX, offsetY, offsetX, projectile, 43, 31, 70, -p.combat.getEnemyIndex()- 1, 16, 43);
		p.createRangeProjectile(p.absY, p.absX, offsetY, offsetX, projectile, 43, 31, 60, -p.combat.getEnemyIndex()- 1, 16, 50);
		opp.hitQueue.applyRangeDamage(p, hit, delay , false);
		hit = Defence.getHit(p, opp, type, Misc.random(max));
		opp.hitQueue.applyRangeDamage(p, hit, delay, false);
	}

}
