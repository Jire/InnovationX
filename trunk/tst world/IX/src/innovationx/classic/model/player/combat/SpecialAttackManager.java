package src.innovationx.classic.model.player.combat;

import java.util.HashMap;
import java.util.Map;

import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.combat.specials.ArmadylGodsword;
import src.innovationx.classic.model.player.combat.specials.BandosGodsword;
import src.innovationx.classic.model.player.combat.specials.DarkBow;
import src.innovationx.classic.model.player.combat.specials.DragonClaws;
import src.innovationx.classic.model.player.combat.specials.DragonDagger;
import src.innovationx.classic.model.player.combat.specials.DragonLongsword;
import src.innovationx.classic.model.player.combat.specials.DragonMace;
import src.innovationx.classic.model.player.combat.specials.GraniteMaul;
import src.innovationx.classic.model.player.combat.specials.MagicShortbow;
import src.innovationx.classic.model.player.combat.specials.StatiusWarHammer;
import src.innovationx.classic.model.player.combat.specials.VestaLongsword;

public class SpecialAttackManager {
	
	/*
	 * Holds the instances of special classes
	 */
	public Map<String, SpecialAtk> specialAttacks = new HashMap<String, SpecialAtk>();

	/*
	 * Constructor
	 */
	public SpecialAttackManager() {
		specialAttacks.put("swh", new StatiusWarHammer());
		specialAttacks.put("dds", new DragonDagger());
		specialAttacks.put("dragon claws", new DragonClaws());
		specialAttacks.put("ags", new ArmadylGodsword());
		specialAttacks.put("bgs", new BandosGodsword());
		specialAttacks.put("dbow", new DarkBow());
		specialAttacks.put("gmaul", new GraniteMaul());
		specialAttacks.put("vls", new VestaLongsword());
		specialAttacks.put("dmace", new DragonMace());
		specialAttacks.put("dlong", new DragonLongsword());
		specialAttacks.put("msb", new MagicShortbow());
	}
	
	/*
	 * Executes the special attack specified.
	 */
	public void executeSpecial(Client p, Client opp, String name) {
		try {
			SpecialAtk special = specialAttacks.get(name);
			special.execute(p, opp);
			p.UsingSpecial = false;
			p.specialAttackBar();
		} catch(Exception e) {
		}
	}
	
}
