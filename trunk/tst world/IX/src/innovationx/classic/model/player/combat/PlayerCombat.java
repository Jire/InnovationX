package src.innovationx.classic.model.player.combat;

import src.innovationx.classic.Constants;
import src.innovationx.classic.Server;
import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.combat.Defence.CombatType;
import src.innovationx.classic.util.Misc;

public class PlayerCombat {

	/**
	 * Player Instance
	 * 
	 * @author Harry Andreas <HarryTheComputerWizKid@msn.com>
	 */
	private Client c;

	/**
	 * Combat Type
	 */
	public CombatType combatType = CombatType.MELEE;

	/*
	 * Combat timer
	 */
	private int combatDelay;

	/*
	 * Block emote delay
	 */
	private int blockDelay;

	/*
	 * Wether the player is attacking
	 */
	private boolean isAttacking;

	/*
	 * Index of who the player is attacking
	 */
	private int enemyIndex;

	/*
	 * 1v1 Timers
	 */
	public int combatWith = 0;
	public int combatWithTimer = 0;

	/*
	 * Magic variables
	 */
	private int magicDelay, magicCastOn, magicSpellId;
	private boolean spellCasted;

	/*
	 * G maul spec
	 */
	public boolean usingGmaulSpec = false;

	/*
	 * Constructor
	 */
	public PlayerCombat(Client c) {
		setC(c);
	}

	/*
	 * Setter
	 */
	public void setC(Client c) {
		this.c = c;
	}

	/*
	 * Getter
	 */
	public Client getC() {
		return c;
	}

	/*
	 * Ticks every 600ms
	 */
	public void tick() {
		try {
			if (combatWithTimer > 0) {
				combatWithTimer--;
			}
			if (combatWithTimer == 0) {
				combatWith = 0;
			}
			if (isAttacking()) {
				attackPlayer();
			}
			if (isSpellCasted()) {
				executeMagicCasting();
			}
			if (getBlockDelay() > 0) {
				blockDelay--;
			}
			if (getCombatDelay() > 0) {
				combatDelay--;
			}
			if(this.getMagicDelay() > 0) {
				magicDelay--;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCombatDelay(int combatDelay) {
		this.combatDelay = combatDelay;
	}

	public int getCombatDelay() {
		return combatDelay;
	}

	/**
	 * @param isAttacking
	 *            the isAttacking to set
	 */
	public void setAttacking(boolean isAttacking) {
		this.isAttacking = isAttacking;
	}

	/**
	 * @return the isAttacking
	 */
	public boolean isAttacking() {
		return isAttacking;
	}

	/**
	 * @param enemyIndex
	 *            the enemyIndex to set
	 */
	public void setEnemyIndex(int enemyIndex) {
		this.enemyIndex = enemyIndex;
	}

	/**
	 * @return the enemyIndex
	 */
	public int getEnemyIndex() {
		return enemyIndex;
	}

	/**
	 * @param blockDelay
	 *            the blockDelay to set
	 */
	public void setBlockDelay(int blockDelay) {
		this.blockDelay = blockDelay;
	}

	/**
	 * @return the blockDelay
	 */
	public int getBlockDelay() {
		return blockDelay;
	}

	/**
	 * Determines the combat type
	 */
	public int getWeaponType() {
		for (int i : Constants.RANGED_WEAPONS)
			if (getC().playerEquipment[getC().playerWeapon] == i)
				return 1;
		for (int i : Constants.MAGIC_WEAPONS)
			if (getC().playerEquipment[getC().playerWeapon] == i)
				return 2;
		return 0;
	}

	/*
	 * The overalls in the hit queue
	 */
	public int getHits() {
		int hits = 0;
		if (getC().hitQueue.hitQueue.isEmpty()) {
			return 0;
		}
		for (Hit h : getC().hitQueue.hitQueue) {
			hits += h.getHit();
		}
		return hits;
	}

	/**
	 * Starts the main combat cycle
	 */
	@SuppressWarnings("static-access")
	public void attackPlayer() {
		Client opp = (Client) Server.s.playerHandler.players[this.enemyIndex];
		boolean inDuel = getC().duelStatus == 3;
		if (getC().IsDead) {
			stopAttack();
			return;
		}
		if (opp == null || opp.IsDead) {
			stopAttack();
			return;
		}
		if (getHits() > getC().playerLevel[3]) {
			opp.combat.setBlockDelay(0);
			stopAttack();
			return;
		}
		if (!inDuel) {
			if (!getC().checkWildRange(opp.combatLevel)) {
				getC().sendMessage(
						"You need to travel deeper into the wilderness to attack this player.");
				stopAttack();
				return;
			}
			if (!getC().WildArea() || !opp.WildArea()) {
				stopAttack();
				getC().sendMessage("You cannot attack this player.");
				return;
			}
			if (!combatCheck()) {
				stopAttack();
				return;
			}
		}
		if (getC().playerEquipment[3] == 4153 && getC().UsingSpecial) {
			startSpecial();
			return;
		}
		applyFollow();
		if (getCombatDelay() > 0 || !canAttack()) {
			return;
		}
		getC().xLogDelay = 20;
		opp.xLogDelay = 20;
		appendPlayerFacing();
		determineCombatType();
		setCombatDelay(getSpeed());
		setBlockDelay(2);
		if (getC().UsingSpecial) {
			startSpecial();
			return;
		}
		Server.s.combatInstance.combatTypes[combatType.type].executeCombat(
				getC(), opp);
	}

	@SuppressWarnings("static-access")
	public void executeMagicCasting() {
		if (getCombatDelay() > 0) {
			return;
		}
		if (getMagicDelay() > 0) {
			return;
		}
		combatType = CombatType.MAGIC;
		setEnemyIndex(this.getMagicCastOn());
		Client opp = Server.s.playerHandler.clients[getEnemyIndex()];
		boolean inDuel = getC().duelStatus == 3;
		if (getC() == null || opp == null || getC().IsDead || opp.IsDead) {
			this.setSpellCasted(false);
			this.stopAttack();
			return;
		}
		if (getC().IsDead) {
			this.setSpellCasted(false);
			stopAttack();
			return;
		}
		if (opp == null || opp.IsDead) {
			stopAttack();
			this.setSpellCasted(false);
			return;
		}
		if (getHits() > getC().playerLevel[3]) {
			this.setSpellCasted(false);
			setBlockDelay(0);
			stopAttack();
			return;
		}
		if (!inDuel) {
			if (!getC().checkWildRange(opp.combatLevel)) {
				getC().sendMessage(
						"You need to travel deeper into the wilderness to attack this player.");
				stopAttack();
				this.setSpellCasted(false);
				return;
			}
			if (!getC().WildArea() || !opp.WildArea()) {
				stopAttack();
				this.setSpellCasted(false);
				getC().sendMessage("You cannot attack this player.");
				return;
			}
			if (!combatCheck()) {
				this.setSpellCasted(false);
				stopAttack();
				return;
			}
		}
		if (!inDuel) {
			if (getC().lastHit != getEnemyIndex() && getC().duelStatus != 3) {
				getC().lastHit = getEnemyIndex();
				getC().skullTimer = 360;
				if (getC().HeadIconPk != 1)
					getC().SetPkHeadIcon(1);
			}
		}
		if (inDuel) {
			if (getC().duelStartDelay > 0) {
				this.stopAttack();
				this.setSpellCasted(false);
				return;
			}
			if (getC().duelWith != this.getEnemyIndex()) {
				getC().sendMessage("You are not dueling with this person!");
				this.stopAttack();
				this.setSpellCasted(false);
				return;

			}
		}
		if (Misc.getDistance(getC().absX, getC().absY, opp.absX, opp.absY) > 8) {
			this.applyFollow();
			return;
		}
		getC().stopMovement();
		this.appendPlayerFacing();
		this.stopAttack();
		this.setBlockDelay(2);
		this.setCombatDelay(4);
		this.setMagicDelay(6);
		this.setSpellCasted(false);
		Server.s.combatInstance.combatTypes[2].executeCombat(getC(), opp);
	}

	/*
	 * Sets the combat type
	 */
	public void determineCombatType() {
		int combatStyle = getWeaponType();
		switch (combatStyle) {
		case 1:
			combatType = CombatType.RANGE;
			break;
		case 2:
			combatType = CombatType.MAGIC;
			break;
		default:
			combatType = CombatType.MELEE;
			break;
		}
	}

	/**
	 * Determines the distance needed
	 */
	public int getDistance() {
		return (getWeaponType() > 0 ? 8 : 1);
	}

	/*
	 * Applys following if needed
	 */
	@SuppressWarnings("static-access")
	public void applyFollow() {
		Client opp = (Client) Server.s.playerHandler.players[this
				.getEnemyIndex()];
		int distance = Misc.getDistance(getC().absX, getC().absY, opp.absX,
				opp.absY);
		int distReq = getDistance();
		if (distance > distReq) {
			getC().follow(getEnemyIndex(), 1, 1);
		} else {
			if (getWeaponType() > 0) {
				getC().stopMovement();
			}
		}
	}

	@SuppressWarnings("static-access")
	public boolean combatCheck() {
		Client opp = (Client) Server.s.playerHandler.players[this
				.getEnemyIndex()];
		if (!getC().multiZone()) {
			if (getC().combat.combatWith != getEnemyIndex()
					&& getC().combat.combatWith != 0) {
				getC().sendMessage("You are already in combat!");
				return false;
			}
			if (opp.combat.combatWith != getC().playerId
					&& opp.combat.combatWith != 0) {
				getC().sendMessage("This player is already in combat.");
				return false;
			}
			opp.combat.combatWith = getC().playerId;
			opp.combat.combatWithTimer = 10;
		}
		return true;
	}

	/**
	 * Determines if you can attack the opponant
	 */
	public boolean canAttack() {
		@SuppressWarnings("static-access")
		Client opp = (Client) Server.s.playerHandler.players[this
				.getEnemyIndex()];
		boolean inDuel = getC().duelStatus == 3;
		int distance = Misc.getDistance(getC().absX, getC().absY, opp.absX,
				opp.absY);
		if (!inDuel) {
			if (getC().lastHit != getEnemyIndex() && getC().duelStatus != 3) {
				getC().lastHit = getEnemyIndex();
				getC().skullTimer = 360;
				if (getC().HeadIconPk != 1)
					getC().SetPkHeadIcon(1);
			}
		}
		if (inDuel) {
			if (getC().duelStartDelay > 0) {
				this.stopAttack();
				return false;
			}
			if (getC().duelWith != this.getEnemyIndex()) {
				getC().sendMessage("You are not dueling with this person!");
				this.stopAttack();
				return false;
			}
		}
		int distReq = getDistance();
		if (distance > distReq) {
			return false;
		}
		return true;
	}

	/*
	 * Gets the weapon speed
	 */
	public int getSpeed() {
		int attackStyle = getC().skillId;
		int wep = getC().playerEquipment[getC().playerWeapon];
		if (wep == 7979) {
			return 5;
		}
		if (wep == 4734) {
			return 3;
		}
		for (int i : Constants.DARTS_KNIFES) {
			if (i == wep) {
				if (attackStyle == 1) {
					return 2;
				}
				return 3;
			}
		}
		for (int i : Constants.SHORT_BOWS) {
			if (i == wep) {
				return 3;
			}
		}
		for (int i : Constants.DAGGERS) {
			if (i == wep) {
				return 4;
			}
		}
		for (int i : Constants.SWORDS) {
			if (i == wep) {
				return 4;
			}
		}
		for (int i : Constants.SCIMIATARS) {
			if (i == wep) {
				return 4;
			}
		}
		for (int i : Constants.CLAWS) {
			if (i == wep) {
				return 4;
			}
		}
		for (int i : Constants.LONGSWORDS) {
			if (i == wep) {
				return 5;
			}
		}
		for (int i : Constants.MACES) {
			if (i == wep) {
				return 5;
			}
		}
		for (int i : Constants.HATCHETS) {
			if (i == wep) {
				return 5;
			}
		}
		for (int i : Constants.PICKAXES) {
			if (i == wep) {
				return 5;
			}
		}
		for (int i : Constants.SPEARS) {
			if (i == wep) {
				return 5;
			}
		}
		for (int i : Constants.TORAG_VERAC_GUTHAN) {
			if (i == wep) {
				return 5;
			}
		}
		for (int i : Constants.LONG_BOWS) {
			if (i == wep) {
				if (attackStyle == 1) {
					return 5;
				}
				return 6;
			}
		}
		for (int i : Constants.BATTLE_AXES) {
			if (i == wep) {
				return 6;
			}
		}
		for (int i : Constants.WAR_HAMMERS) {
			if (i == wep) {
				return 6;
			}
		}
		for (int i : Constants.GODSWORDS) {
			if (i == wep) {
				return 6;
			}
		}
		for (int i : Constants.TWO_HANDED) {
			if (i == wep) {
				return 7;
			}
		}
		for (int i : Constants.HALBERDS) {
			if (i == wep) {
				return 7;
			}
		}
		for (int i : Constants.MAULS) {
			if (i == wep) {
				return 7;
			}
		}
		if (wep == 7958) {
			return 9;
		}
		if (wep == 4718) {
			return 7;
		}
		return 4;
	}

	/**
	 * Gets the weapon animation
	 * 
	 * @return The weapon animation
	 */
	public int getWeaponAnimation() {
		int attackStyle = getC().GSWeaponAttackStyle;
		int wep = getC().playerEquipment[getC().playerWeapon];
		String name = getC().getItemName(wep);
		return getC().GetWeaponAtkEmote(name, attackStyle);
	}

	/*
	 * Stops the player attacking
	 */
	public void stopAttack() {
		isAttacking = false;
		enemyIndex = -1;
	}

	/*
	 * Faces the player
	 */
	public void appendPlayerFacing() {
		getC().TurnPlayerTo(32768 + getEnemyIndex());
	}

	public void startSpecial() {
		int wep = getC().playerEquipment[getC().playerWeapon];
		@SuppressWarnings("static-access")
		Client opp = (Client) Server.s.playerHandler.players[getEnemyIndex()];
		switch (wep) {
		case 8916:
			Server.s.combatInstance.specialAttackManager.executeSpecial(getC(), opp, "swh");
			break;
		case 861:
			Server.s.combatInstance.specialAttackManager.executeSpecial(getC(),
					opp, "msb");
			break;
		case 1305:
			Server.s.combatInstance.specialAttackManager.executeSpecial(getC(),
					opp, "dlong");
			break;
		case 1434:
			Server.s.combatInstance.specialAttackManager.executeSpecial(getC(),
					opp, "dmace");
			break;
		case 8907:
			Server.s.combatInstance.specialAttackManager.executeSpecial(getC(),
					opp, "vls");
			break;
		case 4153:
			Server.s.combatInstance.specialAttackManager.executeSpecial(getC(),
					opp, "gmaul");
			break;
		case 7958:
			Server.s.combatInstance.specialAttackManager.executeSpecial(getC(),
					opp, "dbow");
			break;
		case 7993:
			Server.s.combatInstance.specialAttackManager.executeSpecial(getC(),
					opp, "bgs");
			break;
		case 8086:
			Server.s.combatInstance.specialAttackManager.executeSpecial(getC(),
					opp, "ags");
			break;
		case 8922:
			Server.s.combatInstance.specialAttackManager.executeSpecial(getC(),
					opp, "dragon claws");
			break;
		case 5698:
			Server.s.combatInstance.specialAttackManager.executeSpecial(getC(),
					opp, "dds");
			break;
		}
	}

	/**
	 * @param magicDelay
	 *            the magicDelay to set
	 */
	public void setMagicDelay(int magicDelay) {
		this.magicDelay = magicDelay;
	}

	/**
	 * @return the magicDelay
	 */
	public int getMagicDelay() {
		return magicDelay;
	}

	/**
	 * @param magicCastOn
	 *            the magicCastOn to set
	 */
	public void setMagicCastOn(int magicCastOn) {
		this.magicCastOn = magicCastOn;
	}

	/**
	 * @return the magicCastOn
	 */
	public int getMagicCastOn() {
		return magicCastOn;
	}

	/**
	 * @param magicSpellId
	 *            the magicSpellId to set
	 */
	public void setMagicSpellId(int magicSpellId) {
		this.magicSpellId = magicSpellId;
	}

	/**
	 * @return the magicSpellId
	 */
	public int getMagicSpellId() {
		return magicSpellId;
	}

	/**
	 * @param spellCasted
	 *            the spellCasted to set
	 */
	public void setSpellCasted(boolean spellCasted) {
		this.spellCasted = spellCasted;
	}

	/**
	 * @return the spellCasted
	 */
	public boolean isSpellCasted() {
		return spellCasted;
	}
}
