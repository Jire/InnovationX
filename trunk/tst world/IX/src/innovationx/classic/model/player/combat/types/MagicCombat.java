package src.innovationx.classic.model.player.combat.types;


import src.innovationx.classic.Server;
import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.combat.DefaultType;
import src.innovationx.classic.model.player.combat.Defence;
import src.innovationx.classic.model.player.combat.Defence.CombatType;
import src.innovationx.classic.model.player.combat.types.magicdata.ModernMagic;
import src.innovationx.classic.util.Misc;
import src.innovationx.classic.util.newevent.GameLogicTask;

public class MagicCombat implements DefaultType {

	public Object[][] spellNames = {
			{1152, "windstrike"},
			{1154, "waterstrike"},
			{1156, "earthstrike"},
			{1158, "firestrike"},
			{1160, "windbolt"},
			{1163, "waterbolt"},
			{1166, "earthbolt"},
			{1169, "firebolt"},
			{1172, "windblast"},
			{1175, "waterblast"},
			{1177, "earthblast"},
			{1181, "fireblast"},
			{1183, "windwave"},
			{1185, "waterwave"},
			{1188, "earthwave"},
			{1189, "firewave"}
	};
	
	
	public String getName(int id) {
		for(Object[] d: spellNames) {
			if((Integer)d[0] == id) {
				return (String)d[1];
			}
		}
		return null;
	}
	
	@Override
	public void executeCombat(final Client p, final Client opp) {
		final MagicData magicData = Server.s.combatInstance.data;
		String name = getName(p.combat.getMagicSpellId());
		final int[] data = ModernMagic.getData(name);
		if(data == null) {
			return;
		}
		if(data[magicData.level] > p.playerLevel[6]) {
			p.sendMessage("You need a higher magic level to cast this spell");
			return;
		}
		
		int offsetX = (p.absX - opp.absX) * -1;
		int offsetY = (p.absY - opp.absY) * -1;
		int distance = Misc.getDistance(p.absX, p.absY, opp.absX,opp.absY);
		int delay = distance > 2 ? 2: 1;
		int proSpeed = distance > 2 ? 80 : 60;
		final int hit = Defence.getHit(p, opp, CombatType.MAGIC, data[magicData.max]);
		final boolean splash = hit == -1;
		p.startAnimation(data[magicData.emote], 0);
		p.playGraphic(data[magicData.start], 0, 100);
		p.createProjectile(p.absY, p.absX, offsetY, offsetX,data[magicData.projectile], 43, 31, proSpeed,	- opp.playerId - 1);
		Server.s.eventManager.schedule(new GameLogicTask() {
			@Override
			public void run() {
				this.stop();
				if(splash) {
					opp.playGraphic(85, 0, 100);
				} else {
					opp.playGraphic(data[magicData.end], 0, 100);
					opp.hitQueue.applyMagicDamage(p, hit);
				}
			}
		}, delay, 0);
	}

}
