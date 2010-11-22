package src.innovationx.classic.model.player.combat;

import src.innovationx.classic.model.player.combat.types.MagicCombat;
import src.innovationx.classic.model.player.combat.types.MagicData;
import src.innovationx.classic.model.player.combat.types.MeleeCombat;
import src.innovationx.classic.model.player.combat.types.RangeCombat;

public class CombatTypeManager {
	
	/*
	 * Holds the combat classes in memory
	 */
	public DefaultType[] combatTypes = new DefaultType[3];
	
	/*
	 * Holds the instance of the special attack manager
	 */
	public SpecialAttackManager specialAttackManager;
	
	/*
	 * Holds the instance of magic data
	 */
	public MagicData data;
	
	public CombatTypeManager() {
		combatTypes[0] = new MeleeCombat();
		combatTypes[1] = new RangeCombat();
		combatTypes[2] = new MagicCombat();
		specialAttackManager = new SpecialAttackManager();
		data = new MagicData();
	}
	
	

}
