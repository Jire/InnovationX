package src.innovationx.classic.model.npc;

import src.innovationx.classic.Server;
import src.innovationx.classic.model.objects.Tile;
import src.innovationx.classic.model.objects.WalkingCheck;
import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.net.stream;
import src.innovationx.classic.util.Misc;
import src.innovationx.classic.util.event.Event;
import src.innovationx.classic.util.event.EventContainer;
import src.innovationx.classic.util.event.EventManager;
import src.innovationx.classic.util.newevent.GameLogicTask;

public class NPC {
	// public NPC npcCombatOpp;
	// public int npcCombatDelay = 0;
	public Client opppet;
	public boolean commandedAttack = false;
	public Client owner;
	public int NpcSlot, NpcType = 0;
	public int MaxHp, CurrentHp = 0;
	public int MaxHit, AtkType = 0;
	public int DefLvl, DefType = 0;
	public int absX, absY, makeX, makeY, moveX, moveY = 0;
	public int moverangeX1, moverangeY1, moverangeX2, moverangeY2 = 0;
	public int heightLevel = 0;
	public int NpcToKill = 0;
	public int direction = -1;
	public int hitDiff, AnimNumber, ActionTimer, StartKilling = 0;
	public boolean IsDead, DeadApply, NeedRespawn, IsUnderAttack = false,
			KillingNpc = false;
	public boolean RandomWalk = true;
	public int SpawnedFor = -1;
	public int EntangleDelay = 0;
	public int LastUsedAtk = -1;
	public int hpHealDelay = 92;
	public boolean NeedsRespawn = false;
	public int[] KilledBy = new int[Server.s.playerHandler.maxPlayers];
	public boolean updateRequired, dirUpdateRequired, animUpdateRequired,
			hitUpdateRequired, hitUpdateRequired2, textUpdateRequired = false;
	public String textUpdate = "";
	public int combatWith = 0;
	public double statDrain = 0;
	public int sheepDelay = -1;
	public int projectileHit = 0;
	public int height = 0, width = 0;
	public int[] kalphIndex = new int[5];
	public int[] kalphXIndex = new int[5];
	public int[] kalphYIndex = new int[5];
	public int kalphDelay = 0;
	public int finishSpawnDelay = 0;
	public int size = 0;
	public int monkeyWalkTime = 0;
	public int combatLevel = 0;
	private Tile currentTile;
	
	public int zarosDifficaulty = 1;

	public void zarosAttack() {
		final int projectile = 1623;
		final int endHeight = 100;
		final int emote = 710;
		final int endGFX = 1624;
		int maxs[] = {-1, 49, 54, 65};
		final int max = maxs[zarosDifficaulty];
		final Client p = owner;
		int offsetX = (absX - p.absX) * -1;
		int offsetY = (absY - p.absY) * -1;
		this.playAnimation(emote);
		p.createProjectile(absY, absX, offsetY, offsetX, projectile, 43, 31,
				60, -p.playerId - 1);
		Server.s.eventManager.schedule(new GameLogicTask() {
			@Override
			public void run() {
				int hit = Misc.random(max);
				if(p.HeadPray == 1) {
					hit = hit / 2;
				}
				p.hitQueue.applyMeleeDamage(p, hit);
				p.playGraphic(endGFX, 0, endHeight);
				this.stop();
			}
		}, 2, 0);
	}

	public boolean hasBeenKalphHit(int pIndex) {
		for (int i = 0; i < 5; i++) {
			if (kalphIndex[i] == pIndex) {
				return true;
			}
		}
		return false;
	}

	public int kalphIndex() {
		for (int i = 0; i < 5; i++) {
			if (kalphIndex[i] == 0) {
				return i;
			}
		}
		return -1;
	}

	public int getLastKalphIndex() {
		int idx = -1;
		for (int i = 0; i < 5; i++) {
			if (kalphIndex[i] != 0) {
				idx = i;
			}
		}
		return idx;
	}

	public void calcKalph() {
		boolean didHit = false;
		int idx = kalphIndex();
		if (idx == -1) {
			resetKalph();
			return;
		}
		for (int i = 1; i < Server.s.playerHandler.maxPlayers; i++) {
			Client p = Server.s.playerHandler.clients[i];
			if (p == null || hasBeenKalphHit(i) || p.NewHp <= 0) {
				continue;
			}
			if (WithinDistance(kalphXIndex[idx - 1], kalphYIndex[idx - 1],
					p.absX, p.absY, 4)) {
				kalphDelay = 2;
				didHit = true;
				kalphXIndex[idx] = p.absX;
				kalphYIndex[idx] = p.absY;
				kalphIndex[idx] = i;
				int offsetX = (kalphXIndex[idx - 1] - p.absX) * -1;
				int offsetY = (kalphYIndex[idx - 1] - p.absY) * -1;
				p.createProjectile(kalphYIndex[idx - 1], kalphXIndex[idx - 1],
						offsetY, offsetX, 280, 43, 31, 60, -i - 1);
				p.playGraphic(281, 60, 100);
				int dmg = Misc.random(30);
				int AtkStr = Misc.random(AtkPower());
				int mageDef = Misc.random(p.mageDef());
				if (p.HeadPray == 1) {
					dmg = 0;
				}
				if (AtkStr <= mageDef) {
					dmg = 0;
				}
				for (int q = 0; q < 5; q++) {
					if (p.delayedDmgSlot[q] == 0) {
						p.delayedDmgSlot[q] = p.playerId;
						p.delayedDmgTarget[q] = 1;
						p.delayedDmgType[q] = 2;
						p.delayedDmgHit[q] = dmg;
						p.delayedDmgTime[q] = 2;
						p.delayedSpellId[q] = -10;
						break;
					}
				}
				if (p.vengenceDelay > 0) {
					double vengDmg = (dmg / 1.2);
					vengDmg = Math.round(vengDmg - 0.5f);
					if (vengDmg <= 0) {
						vengDmg = 1;
					}
					hitDiff = (int) vengDmg;
					CurrentHp -= hitDiff;
					updateRequired = true;
					hitUpdateRequired = true;
					p.vengenceDelay = -1;
					p.displayText("Taste Vengeance!");

				}
				break;
			}
		}
		if (!didHit) {
			resetKalph();
		}
	}

	public void resetKalph() {
		for (int i = 0; i < 5; i++) {
			kalphXIndex[i] = 0;
			kalphYIndex[i] = 0;
			kalphIndex[i] = 0;
		}
	}

	public NPC(int _npcSlot, int _npcType) {
		NpcSlot = _npcSlot;
		NpcType = _npcType;
		for (int i = 0; i < KilledBy.length; i++) {
			KilledBy[i] = 0;
		}
	}

	public NPC() {
	}

	public boolean multiZone() {
		return (absX >= 3287 && absX <= 3298 && absY >= 3167 && absY <= 3178
				|| absX >= 3070 && absX <= 3095 && absY >= 3405 && absY <= 3448
				|| absX >= 2961 && absX <= 2981 && absY >= 3330 && absY <= 3354
				|| absX >= 2510 && absX <= 2537 && absY >= 4632 && absY <= 4660
				|| absX >= 3012 && absX <= 3066 && absY >= 4805 && absY <= 4858/* Abyss */
				|| absX >= 2794 && absX <= 2813 && absY >= 9281 && absY <= 9305
				|| absX >= 3546 && absX <= 3557 && absY >= 9689 && absY <= 9700
				|| absX >= 2708 && absX <= 2729 && absY >= 9801 && absY <= 9829
				|| absX >= 3450 && absX <= 3525 && absY >= 9470 && absY <= 9535
				|| absX >= 3207 && absX <= 3395 && absY >= 3904 && absY <= 3903
				|| absX >= 3006 && absX <= 3072 && absY >= 3611 && absY <= 3712
				|| absX >= 3149 && absX <= 3395 && absY >= 3520 && absY <= 4000
				|| absX >= 2365 && absX <= 2420 && absY >= 5065 && absY <= 5120
				|| absX >= 2890 && absX <= 2935 && absY >= 4425 && absY <= 4470
				|| absX >= 2250 && absX <= 2290 && absY >= 4675 && absY <= 4715
				|| absX >= 2690 && absX <= 2825 && absY >= 2680 && absY <= 2810
				|| NpcType == 73 || NpcType == 74 || NpcType == 75
				|| NpcType == 76 || NpcType == 77 || NpcType == 2058 || NpcType == 2843);
	}

	public void updateNPCMovement(stream str) {
		if (str != null) {
			if (direction == -1) {
				if (updateRequired) {
					str.writeBits(1, 1);
					str.writeBits(2, 0);
				} else {
					str.writeBits(1, 0);
				}
			} else {
				str.writeBits(1, 1);
				str.writeBits(2, 1);
				str.writeBits(3, Misc.xlateDirectionToClient[direction]);
				if (updateRequired) {
					str.writeBits(1, 1);
				} else {
					str.writeBits(1, 0);
				}
			}
		}
	}

	public void appendNPCUpdateBlock(stream str) {
		if (str != null) {
			if (!updateRequired) {
				return;
			}
			int updateMask = 0;
			if (animUpdateRequired) {
				updateMask |= 0x10;
			}
			if (GraphicsUpdateRequired) {
				updateMask |= 0x80;
			}
			if (hitUpdateRequired) {
				updateMask |= 0x40;
			}
			if (textUpdateRequired) {
				updateMask |= 1;
			}
			if (dirUpdateRequired) {
				updateMask |= 0x20;
			}
			if (FaceDirection) {
				updateMask |= 4;
			}
			if (hitUpdateRequired2) {
				updateMask |= 8;
			}
			str.writeByte(updateMask);
			if (animUpdateRequired) {
				appendAnimUpdate(str);
			}
			if (GraphicsUpdateRequired) {
				appendGraphicsUpdate(str);
			}
			if (hitUpdateRequired) {
				appendHitUpdate(str);
			}
			if (textUpdateRequired) {
				str.writeString(textUpdate);
			}
			if (dirUpdateRequired) {
				appendFaceEntity(str);
			}
			if (FaceDirection) {
				appendSetFocusDestination(str);
			}
			if (hitUpdateRequired2) {
				appendHitUpdate2(str);
			}
		}
	}

	public void showText(String text) {
		textUpdate = text;
		textUpdateRequired = true;
		updateRequired = true;
	}

	public void clearUpdateFlags() {
		updateRequired = false;
		animUpdateRequired = false;
		GraphicsUpdateRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		textUpdateRequired = false;
		dirUpdateRequired = false;
		FaceDirection = false;
		textUpdate = null;
		poisonHit = false;
		poisonHit2 = false;
		direction = -1;
		moveX = 0;
		moveY = 0;
	}

	public int poisonDelay = -1;
	public int poisonHitDelay = 20;
	public boolean poisonHit = false;
	public boolean poisonHit2 = false;
	public boolean FaceDirection = false;
	public int FocusPointX = 0, FocusPointY = 0;

	public void TurnNpcTo(int X, int Y) {
		FocusPointX = 2 * X + 1;
		FocusPointY = 2 * Y + 1;
		updateRequired = true;
		FaceDirection = true;
	}

	private void appendSetFocusDestination(stream str) {
		if (str != null) {
			str.writeWordBigEndian(FocusPointX);
			str.writeWordBigEndian(FocusPointY);
		}
	}

	public int GfxId, GfxDelay, GfxHeight = 0;
	public boolean GraphicsUpdateRequired = false;

	private void appendGraphicsUpdate(stream str) {
		if (str != null) {
			str.writeWord(GfxId);
			str.writeDWord(GfxDelay);
			str.writeDWord(GfxHeight);
		}
	}

	public void MoveBackToOrigin() {
		if (RandomWalk && moverangeX1 > 0 && moverangeY1 > 0 && moverangeX2 > 0
				&& moverangeY2 > 0) {
			if (absX > moverangeX1 || absX < moverangeX2 || absY > moverangeY1
					|| absY < moverangeY2) {
				moveX = GetMove(absX, makeX);
				moveY = GetMove(absY, makeY);
				getNextNPCMovement();
			}
		}
	}

	private int GetMove(int Place1, int Place2) {
		if ((Place1 - Place2) == 0) {
			return 0;
		} else if ((Place1 - Place2) < 0) {
			return 1;
		} else if ((Place1 - Place2) > 0) {
			return -1;
		}
		return 0;
	}

	public int getNextWalkingDirection() {
		currentTile = new Tile(absX + moveX, absY + moveY, heightLevel);
		if (!WalkingCheck.tiles.containsKey(currentTile.getH() << 28
				| currentTile.getX() << 14 | currentTile.getY())) {
			int dir;
			dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
			if (dir == -1)
				return -1;
			dir >>= 1;
			absX += moveX;
			absY += moveY;
			return dir;
		} else if (WalkingCheck.tiles.get(currentTile.getH() << 28
				| currentTile.getX() << 14 | currentTile.getY()) == true) {
			return -1;
		} else {
			return -1;
		}
	}

	/*
	 * public int getNextWalkingDirection() { int dir; dir =
	 * Misc.direction(absX, absY, (absX + moveX), (absY + moveY)); if (dir == -1
	 * || !WalkingHandler.getSingleton().traversable(absX + moveX, absY + moveY,
	 * dir)) return -1; dir >>= 1; absX += moveX; absY += moveY; return dir; }
	 */

	// 1738, 5222
	// 1761, 5185
	// 1777, 5238
	// 1753, 5151
	public void teleMole() {
		switch (Misc.random(3)) {
		case 0:
			absX = 1738;
			absY = 5222;
			break;
		case 1:
			absX = 1761;
			absY = 5185;
			break;
		case 2:
			absX = 1777;
			absY = 5238;
			break;
		case 3:
			absX = 1753;
			absY = 5151;
			break;
		}
		updateRequired = true;
	}

	public int intoHole = 3314;
	public int outHole = 3315;

	public int litTorch = 594;
	public int unlitTorch = 596;

	/**
	 * Stuff for Giant Mole By Canownueasy <tgpn1996@hotmail.com>
	 */
	public void moleAttack() {
		int style = Misc.random(3);
		boolean killLight = false;
		if (Misc.random(15) == 15) {
			killLight = true;
		}
		if (killLight) {
			killLight();
			return;
		}
		switch (style) {
		case 0:
		case 1:
		case 2:
			normalAttack();
			break;
		case 3:
			underground();
			break;
		}
	}

	public void normalAttack() {
		final Client p = (Client) Server.s.playerHandler.players[StartKilling];
		playAnimation(3312);
		int hit = 24;
		if (p.HeadPray == 1) {
			hit = (int) hit / 2;
		}
		p.appendHit(Misc.random(hit));
	}

	public void killLight() {
		Client p = (Client) Server.s.playerHandler.players[StartKilling];
		p.deleteItem(litTorch, p.getItemSlot(litTorch), 1);
		p.addItem(unlitTorch, 1);
		p.sendMessage("The mole extinguishes your light source!");
		if (!p.HasItemAmount(litTorch, 1)) {
			p.appendHit(4);
			p.appendHit(7);
			p.appendHit(22);
			// TODO: Teleport home
			p.sendMessage("You were swarmed by the moles!");
		}
	}

	public void underground() {
		Client p = (Client) Server.s.playerHandler.players[StartKilling];
		p.sendMessage("The mole digs underground to another area!");
		followPlayer(null);
		playAnimation(intoHole);
		final NPC mole = this;
		EventManager.getSingleton().addEvent(new Event() {
			public void execute(EventContainer event) {
				teleMole();
				goUp();
				followPlayer(null);
				event.stop();
			}
		}, 5000);
	}

	public void goUp() {
		playAnimation(outHole);
	}

	/**
	 * Stuff for Chaos Elemental By Canownueasy <tgpn1996@hotmail.com>
	 */
	public int greenCast = 550;
	public int greenProjectile = 551;
	public int greenEnd = 552;

	public int redCast = 553;
	public int redProjectile = 554;
	public int redEnd = 555;

	public int multiCast = 556;
	public int multiProjectile = 557;
	public int multiEnd = 557;

	public int teleotherEmote = 3149;

	public int sendPrimaryDelay = 0;
	public int sendTeleotherDelay = 0;
	public int sendDisarmingDelay = 0;

	public void doAttack() {
		int style = Misc.random(2);
		if (style == 0) {
			playAnimation(getMageEmote());
			playGraphic(multiCast, 0, NpcSlot, 100);
			// primary();
			sendPrimaryDelay = 3;
		} else if (style == 1) {
			playAnimation(getMageEmote());
			playGraphic(greenCast, 0, NpcSlot, 100);
			// disarming();
			sendDisarmingDelay = 3;
		} else if (style == 2) {
			playAnimation(teleotherEmote);
			playGraphic(redCast, 0, NpcSlot, 100);
			// teleother();
			sendTeleotherDelay = 3;
		} else {
			System.out
					.println("[CHAOS ELEMENTAL]: Qwerty llamas somethings wrong >.>");
		}
	}

	@SuppressWarnings("static-access")
	public void disarming() {
		for (int i = 1; i < Server.s.playerHandler.maxPlayers; i++) {
			final Client p = Server.s.playerHandler.clients[i];
			if (p != null) {
				if (WithinDistance(absX, absY, p.absX, p.absY, 15)
						&& p.heightLevel == heightLevel) {
					int offsetX = (absX - p.absX) * -1;
					int offsetY = (absY - p.absY) * -1;
					p.createProjectile(absY, absX, offsetY, offsetX,
							greenProjectile, 65, 31, 100, -p.playerId - 1);
					EventManager.getSingleton().addEvent(new Event() {
						public void execute(EventContainer event) {
							p.removeAllItemsChaosElemental();
							p.playGraphic(greenEnd, 0, 100);
							event.stop();
						}
					}, 2000);
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	public void teleother() {
		for (int i = 1; i < Server.s.playerHandler.maxPlayers; i++) {
			final Client p = Server.s.playerHandler.clients[i];
			if (p != null) {
				if (WithinDistance(absX, absY, p.absX, p.absY, 15)
						&& p.heightLevel == heightLevel) {
					int offsetX = (absX - p.absX) * -1;
					int offsetY = (absY - p.absY) * -1;
					p.createProjectile(absY, absX, offsetY, offsetX,
							redProjectile, 65, 31, 100, -p.playerId - 1);
					EventManager.getSingleton().addEvent(new Event() {
						public void execute(EventContainer event) {
							if (Misc.random(1) == 1) {
								p.teleport(3276, 3913);
							} else {
								p.teleport(3226, 3917);
							}
							p.playGraphic(redEnd, 0, 100);
							event.stop();
						}
					}, 2000);
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	public void primary() {
		for (int i = 1; i < Server.s.playerHandler.maxPlayers; i++) {
			final Client p = Server.s.playerHandler.clients[i];
			if (p != null) {
				if (WithinDistance(absX, absY, p.absX, p.absY, 15)
						&& p.heightLevel == heightLevel) {
					int offsetX = (absX - p.absX) * -1;
					int offsetY = (absY - p.absY) * -1;
					p.createProjectile(absY, absX, offsetY, offsetX,
							multiProjectile, 65, 31, 100, -p.playerId - 1);
					final int max = MaxHit;
					EventManager.getSingleton().addEvent(new Event() {
						public void execute(EventContainer event) {
							int hit = max;
							if (p.HeadPray == 3) {
								hit /= 2;
							}
							p.playGraphic(multiEnd, 0, 100);
							p.appendHit(Misc.random(hit));
							event.stop();
						}
					}, 2000);
				}
			}
		}
	}

	private void npcPos() {
		for (int i = 0; i < Server.s.npcHandler.maxNPCs; i++) {
			if (Server.s.npcHandler.npcs[i] != null && NpcSlot != i) {
				NPC npc = Server.s.npcHandler.npcs[i];
				int disNeeded = 0;
				if (npc.size > 1) {
					if (absX < npc.absX && absY > npc.absY) {
						if (absY - npc.absY > 1 && npc.size >= 3) {
							disNeeded += (int) (npc.size);
						} else {
							disNeeded += (int) (npc.size / 2);
						}
					} else if (absX > npc.absX && absY > npc.absY) {
						if (absY - npc.absY > 1 && npc.size >= 3) {
							disNeeded += (int) (npc.size / 2) + 1;
						} else {
							disNeeded += (int) (npc.size / 2);
						}
					} else if (absX > npc.absX && absY <= npc.absY) {
						if (absX - npc.absX > 1 && npc.size >= 3) {
							disNeeded += (int) (npc.size / 2) + 1;
						} else {
							disNeeded += (int) (npc.size / 2);
						}
					}
				}
				if ((absX + moveX) == npc.absX
						&& (absY + moveY) == npc.absY
						&& npc.size == 0
						|| WithinDistance((absX + moveX), (absY + moveY),
								npc.absX, npc.absY, disNeeded)
						&& (npc.size != 1 || size != 1)) {
					moveX = 0;
					moveY = 0;
				}
			}
		}
	}

	private void playerPos() {
		for (int i = 0; i < Server.s.playerHandler.maxPlayers; i++) {
			if (Server.s.playerHandler.players[i] != null) {
				if ((absX + moveX) == Server.s.playerHandler.players[i].absX
						&& (absY + moveY) == Server.s.playerHandler.players[i].absY) {
					moveX = 0;
					moveY = 0;
				}
			}
		}
	}

	private void largeNPCPos() {
		/*
		 * for (int i = 0; i < Server.s.npcHandler.maxNPCs; i++) { if
		 * (Server.s.npcHandler.npcs[i] != null && NpcSlot != i) { if
		 * (absX+moveX == Server.s.npcHandler.npcs[i].absX && absY+moveY ==
		 * Server.s.npcHandler.npcs[i].absY || WithinDistance(absX+moveX,
		 * absY+moveY, Server.s.npcHandler.npcs[i].absX,
		 * Server.s.npcHandler.npcs[i].absY, 3)) { moveX = 0; moveY = 0; } } } }
		 */
	}

	private void ahrimRun() {
		if (heightLevel != -1) {
			return;
		}
		if (SpawnedFor <= 0) {
			return;
		}
		if (Server.s.playerHandler.players[SpawnedFor] == null) {
			return;
		}
		Client p = (Client) Server.s.playerHandler.players[SpawnedFor];
		if (WithinDistance(absX, absY, p.absX, p.absY, 1)) {
			if (p.absY > absY) {
				if (absX >= 3552 && absX <= 3554 && absY >= 9698
						&& absY <= 9703) {
					moveX = 0;
					moveY = -1;
				} else {
					moveX = 0;
					moveY = 1;
				}
			} else if (p.absY < absY) {
				if (absX >= 3552 && absX <= 3554 && absY >= 9698
						&& absY <= 9702) {
					moveX = 0;
					moveY = 1;
				} else {
					moveX = 0;
					moveY = -1;
				}
			} else if (p.absY == absY) {
				if (absX >= 3552 && absX <= 3554 && absY >= 9698
						&& absY <= 9702) {
					moveX = 0;
					moveY = 1;
				} else {
					moveX = 0;
					moveY = -1;
				}
			}
			getNextNPCMovement();
		}
	}

	public void followPlayer(Client player) {
		int playerX = player.absX;
		int playerY = player.absY;
		if (player != null) {
			if (WithinDistance(absX, absY, playerX, playerY, 8)
					&& SpawnedFor == -1 || SpawnedFor != -1) {
				TurnNpcTo(playerX, playerY);
				if (playerY < absY && playerX == absX) {
					moveX = 0;
					moveY = GetMove(absY, playerY + 1);
				} else if (playerY > absY && playerX == absX) {
					moveX = 0;
					moveY = GetMove(absY, playerY - 1);
				} else if (playerX < absX && playerY == absY) {
					moveX = GetMove(absX, playerX + 1);
					moveY = 0;
				} else if (playerX > absX && playerY == absY) {
					moveX = GetMove(absX, playerX - 1);
					moveY = 0;
				} else if (playerX < absX && playerY < absY) {
					moveX = GetMove(absX, playerX + 1);
					moveY = GetMove(absY, playerY + 1);
				} else if (playerX > absX && playerY > absY) {
					moveX = GetMove(absX, playerX - 1);
					moveY = GetMove(absY, playerY - 1);
				} else if (playerX < absX && playerY > absY) {
					moveX = GetMove(absX, playerX + 1);
					moveY = GetMove(absY, playerY - 1);
				} else if (playerX > absX && playerY < absY) {
					moveX = GetMove(absX, playerX - 1);
					moveY = GetMove(absY, playerY + 1);
				}
				getNextNPCMovement();
			}
		}
	}

	public void followPlayer() {
		int playerX = Server.s.playerHandler.players[StartKilling].absX;
		int playerY = Server.s.playerHandler.players[StartKilling].absY;
		if (Server.s.playerHandler.players[StartKilling] != null) {
			if (WithinDistance(absX, absY, playerX, playerY, 8)
					&& SpawnedFor == -1 || SpawnedFor != -1) {
				TurnNpcTo(playerX, playerY);
				if (playerY < absY && playerX == absX) {
					moveX = 0;
					moveY = GetMove(absY, playerY + 1);
				} else if (playerY > absY && playerX == absX) {
					moveX = 0;
					moveY = GetMove(absY, playerY - 1);
				} else if (playerX < absX && playerY == absY) {
					moveX = GetMove(absX, playerX + 1);
					moveY = 0;
				} else if (playerX > absX && playerY == absY) {
					moveX = GetMove(absX, playerX - 1);
					moveY = 0;
				} else if (playerX < absX && playerY < absY) {
					moveX = GetMove(absX, playerX + 1);
					moveY = GetMove(absY, playerY + 1);
				} else if (playerX > absX && playerY > absY) {
					moveX = GetMove(absX, playerX - 1);
					moveY = GetMove(absY, playerY - 1);
				} else if (playerX < absX && playerY > absY) {
					moveX = GetMove(absX, playerX + 1);
					moveY = GetMove(absY, playerY - 1);
				} else if (playerX > absX && playerY < absY) {
					moveX = GetMove(absX, playerX - 1);
					moveY = GetMove(absY, playerY + 1);
				}
				getNextNPCMovement();
			}
		}
	}

	public void FollowNpc() {
		int Follow = NpcToKill;
		int playerX = Server.s.npcHandler.npcs[Follow].absX;
		int playerY = Server.s.npcHandler.npcs[Follow].absY;
		if (Server.s.npcHandler.npcs[Follow] != null) {
			if (WithinDistance(absX, absY, playerX, playerY, 8)
					&& SpawnedFor == -1) {
				TurnNpcTo(playerX, playerY);
				if (playerY < absY) {
					moveX = GetMove(absX, playerX);
					moveY = GetMove(absY, playerY + 1);
				} else if (playerY > absY) {
					moveX = GetMove(absX, playerX);
					moveY = GetMove(absY, playerY - 1);
				} else if (playerX < absX) {
					moveX = GetMove(absX, playerX + 1);
					moveY = GetMove(absY, playerY);
				} else if (playerX > absX) {
					moveX = GetMove(absX, playerX - 1);
					moveY = GetMove(absY, playerY);
				}
				getNextNPCMovement();
			}
		}
	}

	public void getNextNPCMovement() {
		npcPos();
		playerPos();
		largeNPCPos();
		if (!Server.s.worldO.canWalkTo(absX, absY, moveX, moveY)) {
			moveX = 0;
			moveY = 0;
		}
		if (moveX != 0 || moveY != 0) {
			if (EntangleDelay <= 0) {
				direction = getNextWalkingDirection();
			}
		}
	}

	public boolean WithinDistance(int objectX, int objectY, int playerX,
			int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY
								|| (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY
								|| (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY
								|| (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public void AttackNpc() {
		if (Server.s.npcHandler.npcs[NpcToKill] == null) {
			ResetAttack();
			return;
		}
		final NPC npc = Server.s.npcHandler.npcs[NpcToKill];
		int PlayerX = npc.absX;
		int PlayerY = npc.absY;
		final int playerHp = npc.CurrentHp;
		int offsetX = (absX - PlayerX) * -1;
		int offsetY = (absY - PlayerY) * -1;
		if (playerHp <= 0 || npc.IsDead) {
			ResetAttack();
			return;
		}
		if (!WithinDistance(absX, absY, PlayerX, PlayerY, 8)
				&& SpawnedFor == -1) {
			ResetAttack();
			return;
		}
		if (AtkType == 0 && !WithinDistance(absX, absY, PlayerX, PlayerY, 1)) {
			FollowNpc();
			return;
		}
		if (ActionTimer <= 0) {
			final int AtkStr = Misc.random(AtkPower());
			final int OtherDef = Misc.random(npc.DefPower());
			if (AtkType == 0) {
				if (WithinDistance(absX, absY, PlayerX, PlayerY, 1)) {
					hitDiff2 = Misc.random(MaxHit);
					if (AtkStr <= OtherDef) {
						hitDiff2 = 0;
					}
					npc.playAnimation(npc.GetBlockEmote());
					playAnimation(getMeleeEmote());
					if (playerHp - hitDiff2 < 0) {
						hitDiff2 = playerHp;
					}
					if (NpcType == 3751) {
						if (npc.CurrentHp < npc.MaxHp) {
							npc.CurrentHp += hitDiff2;
						}
					} else {
						npc.CurrentHp -= hitDiff2;
					}
					if (npc.hitUpdateRequired) {
						npc.hitDiff2 = hitDiff2;
						npc.hitUpdateRequired2 = true;
						npc.updateRequired = true;
					} else {
						npc.hitDiff = hitDiff2;
						npc.hitUpdateRequired = true;
						npc.updateRequired = true;
					}
					if (NpcType == 3751) {
						ActionTimer = 4;
					} else {
						ActionTimer = 7;
					}
				}
			} else if (AtkType == 1) {
				if (WithinDistance(absX, absY, PlayerX, PlayerY, 8)) {
					hitDiff2 = Misc.random(MaxHit);
					if (AtkStr <= OtherDef) {
						hitDiff2 = 0;
					}
					npc.playAnimation(npc.GetBlockEmote());
					playAnimation(getMeleeEmote());
					if (playerHp - hitDiff2 < 0) {
						hitDiff2 = playerHp;
					}
					npc.CurrentHp -= hitDiff2;
					if (npc.hitUpdateRequired) {
						npc.hitDiff2 = hitDiff2;
						npc.hitUpdateRequired2 = true;
						npc.updateRequired = true;
					} else {
						npc.hitDiff = hitDiff2;
						npc.hitUpdateRequired = true;
						npc.updateRequired = true;
					}
					ActionTimer = 4;
				}
			} else if (AtkType == 2) {
				if (WithinDistance(absX, absY, PlayerX, PlayerY, 8)) {
					if (commandedAttack) {
						for (int i = 1; i < Server.s.playerHandler.maxPlayers; i++) {
							final Client p = Server.s.playerHandler.clients[i];
							if (p != null) {
								playAnimation(getMageEmote());
								playGraphic(getCastGfx(), 0, i, 65);
								p.createProjectile(absY, absX, offsetY,
										offsetX, getProjectile(), 65, 31, 80,
										NpcToKill + 1);
								EventManager.getSingleton().addEvent(
										new Event() {
											public void execute(
													EventContainer event) {
												hitDiff2 = Misc.random(22);
												if (AtkStr <= OtherDef) {
													hitDiff2 = 0;
												}
												npc.playAnimation(npc
														.GetBlockEmote());
												npc.playGraphic(157, 0,
														npc.NpcSlot, 100);
												if (playerHp - hitDiff2 < 0) {
													hitDiff2 = playerHp;
												}
												if (NpcType == 3751) {
													if (npc.CurrentHp < npc.MaxHp) {
														npc.CurrentHp += hitDiff2;
													}
												} else {
													npc.CurrentHp -= hitDiff2;
												}
												if (npc.hitUpdateRequired) {
													npc.hitDiff2 = hitDiff2;
													npc.hitUpdateRequired2 = true;
													npc.updateRequired = true;
												} else {
													npc.hitDiff = hitDiff2;
													npc.hitUpdateRequired = true;
													npc.updateRequired = true;
												}
												if (NpcType == 3751) {
													ActionTimer = 4;
												} else {
													ActionTimer = 9999999;
												}
												event.stop();
											}
										}, 1750);
							}
						}
					}
				}
			}
			TurnNpcTo(PlayerX, PlayerY);
		}
	}

	public int hitDiff2 = 0;
	public int AtkDelay = -1;

	public int getCastGfx() {
		switch (NpcType) {
		case 3850:
			return 155;
		}
		return -1;
	}

	public int getProjectile() {
		switch (NpcType) {
		case 3850:
			return 156;
		}
		return -1;
	}

	public int getEndGfx() {
		switch (NpcType) {
		case 3850:
			return 157;
		}
		return -1;
	}

	public void playGraphic(int Id, int Delay, int i, int Height) {
		if (i > 0) {
			NPC npc = Server.s.npcHandler.npcs[i];
			if (npc != null) {
				if (Id >= 0) {
					npc.GfxId = Id;
					npc.GfxDelay = Delay;
					npc.GfxHeight = Height;
					npc.updateRequired = true;
					npc.GraphicsUpdateRequired = true;
				}
			}
		}
	}

	public void getSpeed() {
		if (NpcType == 1200) {
			ActionTimer = 6;
		} else if (NpcType == 3200) {
			ActionTimer = 10;
		} else if (NpcType == 2607) {
			ActionTimer = 4;
		} else if (NpcType == 2616) {
			ActionTimer = 7;
		} else if (NpcType == 2627) {
			ActionTimer = 4;
		} else if (NpcType == 2783) {
			ActionTimer = 6;
		} else if (NpcType == 1973) {
			ActionTimer = 6;
		} else if (NpcType == 2037) {
			ActionTimer = 6;
		} else if (NpcType == 158) {
			ActionTimer = 6;
		} else if (NpcType == 256) {
			ActionTimer = 6;
		} else if (NpcType == 19) {
			ActionTimer = 6;
		} else if (NpcType == 2263) {
			ActionTimer = 4;
		} else if (NpcType == 1977) {
			ActionTimer = 4;
		} else {
			ActionTimer = 5;
		}
	}

	public boolean isInRange() {
		if (absX <= moverangeX1 && absX >= moverangeX2 && absY <= moverangeY1
				&& absY >= moverangeY2) {
			return true;
		} else {
			return false;
		}
	}

	public void AttackPlayer() {
		if (NpcType >= 3777 && NpcType <= 3780) {
			return;
		}
		if (NpcType >= 2440 && NpcType <= 2448) {
			return;
		}
		if (IsDead || StartKilling <= 0) {
			ResetAttack();
			return;
		}
		Client p = (Client) Server.s.playerHandler.players[StartKilling];
		if (!Server.s.worldO.canShootThrough(absX, absY, p.absX, p.absY)) {
			return;
		}
		if (Server.s.playerHandler.players[StartKilling] == null/*
																 * ||
																 * !isInRange()
																 * &&
																 * p.attacknpc
																 * != NpcSlot
																 */) {
			ResetAttack();
			return;
		}
		if (!WithinDistance(absX, absY, p.absX, p.absY, 8) && SpawnedFor != -1) {
			followPlayer();
			return;
		} else if (!WithinDistance(absX, absY, p.absX, p.absY, 8)
				&& SpawnedFor == -1) {
			ResetAttack();
			return;
		}
		int disNeeded = 1;
		if (size > 1) {
			if (p.absX < absX && p.absY > absY) {
				if (p.absY - absY > 1 && size >= 3) {
					disNeeded += (int) (size);
				} else {
					disNeeded += (int) (size / 2);
				}
			} else if (p.absX > absX && p.absY > absY) {
				if (p.absY - absY > 1 && size >= 3) {
					disNeeded += (int) (size);
				} else {
					disNeeded += (int) (size / 2);
				}
			} else if (p.absX > absX && p.absY <= absY) {
				if (p.absX - absX > 1 && size >= 3) {
					disNeeded += (int) (size / 2);
				} else {
					disNeeded += (int) (size / 2) - 1;
				}
			}
		}
		if (AtkType == 0
				&& !WithinDistance(absX, absY, p.absX, p.absY, disNeeded)) {
			followPlayer();
			return;
		} else if (ActionTimer <= 0) {
			int offsetX = ((absX + (int) (size / 2)) - p.absX) * -1;
			int offsetY = ((absY + (int) (size / 2)) - p.absY) * -1;
			if (p.heightLevel != heightLevel) {
				ResetAttack();
				return;
			} else if (p.NewHp <= 0
					|| Server.s.playerHandler.players[StartKilling].IsDead) {
				ResetAttack();
				return;
			} else if (p.combatWith2 != NpcSlot && p.combatWith2 > 0
					&& !multiZone()) {
				ResetAttack();
				return;
			} else if (p.combatWith > 0 && !multiZone()) {
				ResetAttack();
				return;
			}
			if (p.attacknpc == 0 && p.autoAtk) {
				if (WithinDistance(absX, absY, p.absX, p.absY, 1)
						|| WithinDistance(absX, absY, p.absX, p.absY, 8)
						&& (p.useBow || p.useSpell)) {
					p.TurnPlayerTo(NpcSlot);
					p.attacknpc = NpcSlot;
					p.IsAttackingNPC = true;
					p.HasSecondHit = false;
				}
			}
			if (p.apeAtollJailArea()) {
				ResetAttack();
				return;
			}
			int Random = 0;
			p.combatDelay = 30;
			int AtkStr = Misc.random(AtkPower());
			int meleeDef = Misc.random(p.meleeDef());
			int rangeDef = Misc.random(p.rangeDef());
			int mageDef = Misc.random(p.mageDef());
			projectileHit = StartKilling;
			if (AtkType == 0) {
				if (WithinDistance(absX, absY, p.absX, p.absY, disNeeded)) {
					p.combatWith2 = NpcSlot;
					switch (NpcType) {
					case 239:
					case 240:
					case 241:
					case 242:
					case 243:
					case 244:
					case 245:
					case 246:
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= meleeDef) {
							hitDiff2 = 0;
						}
						p.startAnimation(
								p.GetBlockEmote(p
										.getItemName(p.playerEquipment[p.playerWeapon])),
								0);
						playAnimation(getMeleeEmote());
						if (p.NewHp - hitDiff2 < 0) {
							hitDiff2 = p.NewHp;
						}
						if (NpcType == 239 || NpcType == 241) {
							p.changeStat(2, hitDiff2, 0, false);
						} else if (NpcType == 240 || NpcType == 243
								|| NpcType == 245) {
							p.changeStat(1, hitDiff2, 0, false);
						} else if (NpcType == 242 || NpcType == 244) {
							p.changeStat(0, hitDiff2, 0, false);
						} else if (NpcType == 246) {
							p.changeStat(2, hitDiff2, 0, false);
							p.changeStat(1, hitDiff2, 0, false);
						}
						afterEffect();
						p.updateHp(hitDiff2, false);
						if (p.hitUpdateRequired) {
							p.hitDiff2 = hitDiff2;
							p.hitUpdateRequired2 = true;
							p.updateRequired = true;
						} else {
							p.hitDiff = hitDiff2;
							p.hitUpdateRequired = true;
							p.updateRequired = true;
						}
						ActionTimer = 6;
						break;
					case 2026:
						int bonusHit = (MaxHp - CurrentHp) / 2;
						hitDiff2 = Misc.random(MaxHit + bonusHit);
						if (AtkStr <= meleeDef) {
							hitDiff2 = 0;
						}
						if (p.HeadPray == 3 && Misc.random(100) >= 50) {
							hitDiff2 = 0;
						}
						p.startAnimation(
								p.GetBlockEmote(p
										.getItemName(p.playerEquipment[p.playerWeapon])),
								0);
						playAnimation(getMeleeEmote());
						if (p.NewHp - hitDiff2 < 0) {
							hitDiff2 = p.NewHp;
						}
						afterEffect();
						p.updateHp(hitDiff2, false);
						if (p.hitUpdateRequired) {
							p.hitDiff2 = hitDiff2;
							p.hitUpdateRequired2 = true;
							p.updateRequired = true;
						} else {
							p.hitDiff = hitDiff2;
							p.hitUpdateRequired = true;
							p.updateRequired = true;
						}
						ActionTimer = 7;
						break;
					case 2027:
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= meleeDef) {
							hitDiff2 = 0;
						}
						if (p.HeadPray == 3 && Misc.random(100) >= 50) {
							hitDiff2 = 0;
						}
						p.startAnimation(
								p.GetBlockEmote(p
										.getItemName(p.playerEquipment[p.playerWeapon])),
								0);
						playAnimation(getMeleeEmote());
						if (p.NewHp - hitDiff2 < 0) {
							hitDiff2 = p.NewHp;
						}
						if (Misc.random(6) >= 4) {
							CurrentHp += hitDiff2;
							if (CurrentHp > MaxHp) {
								CurrentHp = MaxHp;
							}
							p.playGraphic(398, 0, 100);
						}
						afterEffect();
						p.updateHp(hitDiff2, false);
						if (p.hitUpdateRequired) {
							p.hitDiff2 = hitDiff2;
							p.hitUpdateRequired2 = true;
							p.updateRequired = true;
						} else {
							p.hitDiff = hitDiff2;
							p.hitUpdateRequired = true;
							p.updateRequired = true;
						}
						ActionTimer = 5;
						break;
					case 2029:
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= meleeDef) {
							hitDiff2 = 0;
						}
						if (p.HeadPray == 3 && Misc.random(100) >= 50) {
							hitDiff2 = 0;
						}
						p.startAnimation(
								p.GetBlockEmote(p
										.getItemName(p.playerEquipment[p.playerWeapon])),
								0);
						playAnimation(getMeleeEmote());
						if (p.NewHp - hitDiff2 < 0) {
							hitDiff2 = p.NewHp;
						}
						afterEffect();
						p.updateHp(hitDiff2, false);
						if (p.hitUpdateRequired) {
							p.hitDiff2 = hitDiff2;
							p.hitUpdateRequired2 = true;
							p.updateRequired = true;
						} else {
							p.hitDiff = hitDiff2;
							p.hitUpdateRequired = true;
							p.updateRequired = true;
						}
						ActionTimer = 5;
						break;
					case 2627:
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= meleeDef) {
							hitDiff2 = 0;
						}
						if (p.HeadPray == 3) {
							hitDiff2 = 0;
						}
						p.startAnimation(
								p.GetBlockEmote(p
										.getItemName(p.playerEquipment[p.playerWeapon])),
								0);
						playAnimation(getMeleeEmote());
						if (p.NewHp - hitDiff2 < 0) {
							hitDiff2 = p.NewHp;
						}
						afterEffect();
						p.updateHp(hitDiff2, false);
						p.changeStat(5, 1, 0, false);
						if (p.hitUpdateRequired) {
							p.hitDiff2 = hitDiff2;
							p.hitUpdateRequired2 = true;
							p.updateRequired = true;
						} else {
							p.hitDiff = hitDiff2;
							p.hitUpdateRequired = true;
							p.updateRequired = true;
						}
						ActionTimer = 5;
						break;
					case 2030:
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= meleeDef && Misc.random(100) >= 50) {
							hitDiff2 = 0;
						}
						if (p.HeadPray == 3 && Misc.random(100) >= 50) {
							hitDiff2 = 0;
						}
						p.startAnimation(
								p.GetBlockEmote(p
										.getItemName(p.playerEquipment[p.playerWeapon])),
								0);
						playAnimation(getMeleeEmote());
						if (p.NewHp - hitDiff2 < 0) {
							hitDiff2 = p.NewHp;
						}
						afterEffect();
						p.updateHp(hitDiff2, false);
						if (p.hitUpdateRequired) {
							p.hitDiff2 = hitDiff2;
							p.hitUpdateRequired2 = true;
							p.updateRequired = true;
						} else {
							p.hitDiff = hitDiff2;
							p.hitUpdateRequired = true;
							p.updateRequired = true;
						}
						ActionTimer = 5;
						break;
					default:
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= meleeDef) {
							hitDiff2 = 0;
						}
						if (p.HeadPray == 3) {
							hitDiff2 = 0;
						}
						p.startAnimation(
								p.GetBlockEmote(p
										.getItemName(p.playerEquipment[p.playerWeapon])),
								0);
						playAnimation(getMeleeEmote());
						if (p.NewHp - hitDiff2 < 0) {
							hitDiff2 = p.NewHp;
						}
						afterEffect();
						p.updateHp(hitDiff2, false);
						if (p.hitUpdateRequired) {
							p.hitDiff2 = hitDiff2;
							p.hitUpdateRequired2 = true;
							p.updateRequired = true;
						} else {
							p.hitDiff = hitDiff2;
							p.hitUpdateRequired = true;
							p.updateRequired = true;
						}
						getSpeed();
						if (NpcType == 1461) {
							p.startTele(2772, 2794, 0, 2304, -1, -1, 5, 8677);
							p.apeHitAmt = 0;
						}
						break;
					}
				}
			} else if (AtkType == 1) {
				if (WithinDistance(absX, absY, p.absX, p.absY, 8)) {
					p.combatWith2 = NpcSlot;
					switch (NpcType) {
					case 2631:
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 443, 43,
								31, 65, -StartKilling - 1);
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= rangeDef) {
							hitDiff2 = 0;
						}
						playAnimation(getRangeEmote());
						AtkDelay = 3;
						ActionTimer = 5;
						break;
					case 1183:
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 249, 43,
								31, 65, -StartKilling - 1);
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= rangeDef) {
							hitDiff2 = 0;
						}
						playAnimation(getRangeEmote());
						p.createNpcGfx(250, 0, NpcSlot, 100);
						AtkDelay = 3;
						ActionTimer = 5;
						break;
					case 2881:
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 475, 85,
								31, 65, -StartKilling - 1);
						p.playGraphic(305, 65, 100);
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= rangeDef) {
							hitDiff2 = 0;
						}
						playAnimation(getRangeEmote());
						AtkDelay = 3;
						ActionTimer = 6;
						break;
					case 3850:
						/*
						 * p.createProjectile(absY + (int) (size / 2), absX +
						 * (int) (size / 2), offsetY, offsetX, 156, 85, 31, 65,
						 * -StartKilling - 1); hitDiff2 = Misc.random(22); if
						 * (AtkStr <= mageDef || p.HeadPray == 1) { hitDiff2 =
						 * 0; p.CreatePlayerGfx(85, 100, 100); } else {
						 * p.CreatePlayerGfx(157, 100, 100); }
						 * StartAnimation(getMageEmote()); p.createNpcGfx(155,
						 * 0, NpcSlot, 65); AtkDelay = 3; ActionTimer = 6;
						 */
						break;
					case 2028:
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 27, 43,
								31, 65, -StartKilling - 1);
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= rangeDef && Misc.random(100) >= 50) {
							hitDiff2 = 0;
						}
						playAnimation(getRangeEmote());
						AtkDelay = 3;
						ActionTimer = 5;
						break;
					case 1456:
					case 1457:
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 10, 86,
								31, 65, -StartKilling - 1);
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= rangeDef) {
							hitDiff2 = 0;
						} else {
							p.apeHitAmt += hitDiff2;
							if (p.apeHitAmt >= 10) {
								p.startTele(2772, 2794, 0, 2304, -1, -1, 5,
										8677);
								p.apeHitAmt = 0;
							}
						}
						int rnd = Misc.random(100);
						if (rnd >= 25 && rnd <= 40) {
							p.startTele(2772, 2794, 0, 2304, -1, -1, 5, 8677);
							p.apeHitAmt = 0;
						}
						playAnimation(getRangeEmote());
						AtkDelay = 3;
						ActionTimer = 5;
						break;
					case 1915:
						hitDiff2 = Misc.random(MaxHit);
						playAnimation(getRangeEmote());
						p.playGraphic(451, 0, 100);
						p.createNpcGfx(145, 0, NpcSlot, 100);
						AtkDelay = 2;
						ActionTimer = 4;
						if (AtkStr <= mageDef) {
							hitDiff2 = 0;
						}
						break;
					case 1342:
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 294, 43,
								31, 65, -StartKilling - 1);
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= rangeDef) {
							hitDiff2 = 0;
						}
						playAnimation(getRangeEmote());
						AtkDelay = 3;
						ActionTimer = 5;
						break;
					default:
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= rangeDef) {
							hitDiff2 = 0;
						}
						playAnimation(getRangeEmote());
						AtkDelay = 3;
						ActionTimer = 6;
						break;
					}
				}
			} else if (AtkType == 2) {
				if (WithinDistance(absX, absY, p.absX, p.absY, 8)
						|| (NpcType == 1158 || NpcType == 1160)
						&& WithinDistance(absX, absY, p.absX, p.absY, 16)) {
					p.combatWith2 = NpcSlot;
					switch (NpcType) {
					case 2892:
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 94, 43,
								31, 65, -StartKilling - 1);
						if (hitDiff2 > 0 && p.HeadPray != 1) {
							p.playGraphic(95, 65, 100);
						} else {
							p.playGraphic(85, 65, 100);
						}
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= mageDef) {
							hitDiff2 = 0;
						}
						playAnimation(getMageEmote());
						AtkDelay = 3;
						ActionTimer = 5;
						break;
					case 2744:
						hitDiff2 = Misc.random(MaxHit);
						Random = Misc.random(2);
						if (Random == 1) {
							AtkDelay = 3;
							if (AtkStr <= mageDef) {
								hitDiff2 = 0;
							}
						} else {
							AtkDelay = 2;
							if (AtkStr <= meleeDef) {
								hitDiff2 = 0;
							}
						}
						if (WithinDistance(absX, absY, p.absX, p.absY, 1)) {
							if (Random == 1) {
								playAnimation(getMageEmote());
								p.createProjectile(absY + (int) (size / 2),
										absX + (int) (size / 2), offsetY,
										offsetX, 445, 100, 31, 65,
										-StartKilling - 1);
								if (hitDiff2 > 0 && p.HeadPray != 1) {
									p.playGraphic(446, 65, 0);
								} else {
									p.playGraphic(85, 65, 100);
								}
							} else {
								playAnimation(getMeleeEmote());
							}
						} else {
							playAnimation(getMageEmote());
							p.createProjectile(absY + (int) (size / 2), absX
									+ (int) (size / 2), offsetY, offsetX, 445,
									100, 31, 65, -StartKilling - 1);
							if (hitDiff2 > 0 && p.HeadPray != 1) {
								p.playGraphic(446, 65, 0);
							} else {
								p.playGraphic(85, 65, 100);
							}
						}
						ActionTimer = 5;
						break;
					case 907:
					case 908:
					case 909:
					case 910:
					case 911:
						hitDiff2 = Misc.random(MaxHit);
						playAnimation(getMageEmote());
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 500, 50,
								31, 65, -StartKilling - 1);
						AtkDelay = 3;
						ActionTimer = 5;
						if (AtkStr <= mageDef || p.HeadPray == 1) {
							hitDiff2 = 0;
							p.playGraphic(85, 65, 100);
						}
						break;
					case 2745:
						Random = Misc.random(2);
						hitDiff2 = Misc.random(MaxHit);
						if (WithinDistance(absX, absY, p.absX, p.absY, 1)) {
							if (Random == 1) {
								playAnimation(getRangeEmote());
							} else if (Random == 2) {
								playAnimation(getMageEmote());
							} else {
								playAnimation(getMeleeEmote());
							}
						} else {
							if (Random == 1 || Random == 0) {
								playAnimation(getRangeEmote());
								Random = 1;
							} else {
								playAnimation(getMageEmote());
							}
						}
						if (Random == 1) {
							p.playGraphic(451, 100, 100);
							AtkDelay = 7;
							ActionTimer = 12;
							if (AtkStr <= rangeDef) {
								hitDiff2 = 0;
							}
						} else if (Random == 2) {
							p.createNpcGfx(447, 100, NpcSlot, 400);
							p.createProjectile(absY + (int) (size / 2), absX
									+ (int) (size / 2), offsetY, offsetX, 448,
									150, 31, 100, -StartKilling - 1);
							p.playGraphic(157, 100, 100);
							AtkDelay = 7;
							ActionTimer = 12;
							if (AtkStr <= mageDef) {
								hitDiff2 = 0;
							}
						} else {
							AtkDelay = 2;
							ActionTimer = 8;
							if (AtkStr <= meleeDef) {
								hitDiff2 = 0;
							}
						}
						break;
					case 2566:
						Random = Misc.random(2);
						if (WithinDistance(absX, absY, p.absX, p.absY, 1)) {
							if (Random == 1) {
								playAnimation(getRangeEmote());
								hitDiff2 = Misc.random(15);
							} else if (Random == 2) {
								playAnimation(getMageEmote());
								hitDiff2 = Misc.random(20);
							} else {
								playAnimation(getMeleeEmote());
								hitDiff2 = Misc.random(25);
							}
						} else {
							if (Random == 1 || Random == 0) {
								playAnimation(getRangeEmote());
								hitDiff2 = Misc.random(15);
								Random = 1;
							} else {
								playAnimation(getMageEmote());
								hitDiff2 = Misc.random(20);
							}
						}
						if (Random == 1) {
							p.playGraphic(432, 65, 0);
							AtkDelay = 3;
							ActionTimer = 5;
							if (AtkStr <= rangeDef) {
								hitDiff2 = 0;
							}
						} else if (Random == 2) {
							AtkDelay = 3;
							ActionTimer = 5;
							if (AtkStr <= mageDef) {
								hitDiff2 = 0;
							}
							if (hitDiff2 > 0 && p.HeadPray != 1) {
								p.playGraphic(76, 65, 100);
							} else {
								p.playGraphic(85, 65, 100);
							}
						} else {
							AtkDelay = 1;
							ActionTimer = 5;
							if (AtkStr <= meleeDef) {
								hitDiff2 = 0;
							}
						}
						break;
					case 677:
						Random = Misc.random(1);
						hitDiff2 = Misc.random(MaxHit);
						if (WithinDistance(absX, absY, p.absX, p.absY, 1)) {
							if (Random == 1) {
								playAnimation(getMageEmote());
							} else {
								playAnimation(getMeleeEmote());
							}
						} else {
							playAnimation(getMageEmote());
							Random = 1;
						}
						if (Random == 1) {
							AtkDelay = 3;
							ActionTimer = 6;
							if (AtkStr <= mageDef) {
								hitDiff2 = 0;
							}
							if (hitDiff2 > 0 && p.HeadPray != 1) {
								p.playGraphic(196, 65, 100);
							} else {
								p.playGraphic(85, 65, 100);
							}
						} else {
							AtkDelay = 1;
							ActionTimer = 5;
							if (AtkStr <= meleeDef) {
								hitDiff2 = 0;
							}
						}
						break;
					case 1158:
					case 1160:
						Random = Misc.random(2);
						if (Random == 1) {
							playAnimation(getRangeEmote());
							if (NpcType == 1158) {
								kalphRangedAtk(288, 31, 43);
							} else if (NpcType == 1160) {
								kalphRangedAtk(289, 31, 80);
							}
						} else if (Random == 2) {
							playAnimation(getMageEmote());
							if (NpcType == 1158) {
								p.createProjectile(absY + (int) (size / 2),
										absX + (int) (size / 2), offsetY,
										offsetX, 280, 43, 31, 60,
										-p.playerId - 1);
							} else if (NpcType == 1160) {
								p.createProjectile(absY + (int) (size / 2),
										absX + (int) (size / 2), offsetY,
										offsetX, 280, 80, 31, 60,
										-p.playerId - 1);
							}
							p.playGraphic(281, 60, 100);
							if (NpcType == 1158) {
								p.createNpcGfx(278, 0, NpcSlot, 0);
							} else if (NpcType == 1160) {
								p.createNpcGfx(279, 0, NpcSlot, 100);
							}
							kalphXIndex[0] = p.absX;
							kalphYIndex[0] = p.absY;
							kalphIndex[0] = p.playerId;
							kalphDelay = 2;
							AtkDelay = 3;
						} else {
							playAnimation(getMeleeEmote());
							hitDiff2 = Misc.random(31);
							if (AtkStr <= meleeDef) {
								hitDiff2 = 0;
							}
							AtkDelay = 1;
						}
						ActionTimer = 6;
						break;
					case 50:
						Random = Misc.random(2);
						if (Random == 1) {
							playAnimation(getMageEmote());
							multiIceAtk(363, 0, 60, 25, 18);
							p.createNpcGfx(2, 0, NpcSlot, 100);
						} else if (Random == 2) {
							playAnimation(getRangeEmote());
							multiAtk(453, 100, 99, 18);
							p.createNpcGfx(0, 0, NpcSlot, 100);
						} else {
							playAnimation(getMeleeEmote());
							multiAtk(405, 100, 40, 18);
						}
						ActionTimer = 7;
						break;
					case 73:
					case 74:
						hitDiff2 = Misc.random(3);
						break;
					case 75:
					case 76:
						hitDiff2 = Misc.random(5);
						break;
					case 77:
						hitDiff2 = Misc.random(7);
						break;
					case 2058:
						hitDiff2 = Misc.random(16);
						break;
					case 2843:
						playAnimation(getMageEmote());
						AtkDelay = 2147000000;
						ActionTimer = 8;
						castWait = 2;
						// multiAtk(89, 65, 26, 10);
						break;
					case 3200: // chaos ele
						doAttack();
						ActionTimer = 10;
						break;
					case 3340: // giant mole
						moleAttack();
						ActionTimer = 9;
						break;
					case 3847: // stq
						// int atkStyle = misc.random(1);
						/*
						 * if (atkStyle != 1) { // p.createProjectile(absY +
						 * (int)(size / 2), absX + // (int)(size / 2), offsetY,
						 * offsetX, 162, 85, 31, // 65, -StartKilling-1);
						 * StartAnimation(GetMageEmote()); AtkDelay = 4;
						 * ActionTimer = 6; p.createProjectile(2957, 2755,
						 * offsetY, offsetX, 162, 75, 31, 65, -StartKilling -
						 * 1); hitDiff2 = misc.random(42); if (AtkStr <= mageDef
						 * || p.HeadPray == 1) { hitDiff2 = 0;
						 * p.CreatePlayerGfx(85, 65, 100); } p.playerLevel[5] -=
						 * ((int) (hitDiff2 / 8)); p.updateRequired = true; if
						 * (hitDiff2 < 1) { p.CreatePlayerGfx(85, 65, 100); }
						 * else // p.CreatePlayerGfx(477, 65, 100);
						 * p.CreatePlayerGfx(163, 65, 100); } else {
						 */
						playAnimation(getMeleeEmote());
						AtkDelay = 2147000000; // 4
						ActionTimer = 9;
						// multiAtk(8, 0, 24, 10);
						multiAtk(163, 65, 34, 10);
						// }
						break;
					case 1353:
					case 1354:
					case 1355:
						Random = Misc.random(2);
						if (Random == 1) {
							playAnimation(getMageEmote());
							multiAtk(451, 0, 30, 8);
						} else if (Random == 2) {
							playAnimation(getMageEmote());
							multiAtk(482, 100, 30, 8);
						} else {
							playAnimation(getMeleeEmote());
							multiAtk(606, 100, 30, 4);
						}
						ActionTimer = 7;
						break;
					case 2595:
						hitDiff2 = Misc.random(MaxHit);
						playAnimation(getMageEmote());
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 440, 50,
								31, 65, -StartKilling - 1);
						AtkDelay = 3;
						ActionTimer = 5;
						if (AtkStr <= mageDef) {
							hitDiff2 = 0;
						}
						break;
					case 1643:
						hitDiff2 = Misc.random(MaxHit);
						playAnimation(getMageEmote());
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 156, 50,
								31, 65, -StartKilling - 1);
						p.createNpcGfx(155, 0, NpcSlot, 100);
						AtkDelay = 3;
						ActionTimer = 5;
						if (AtkStr <= mageDef || p.HeadPray == 1) {
							hitDiff2 = 0;
							p.playGraphic(85, 65, 100);
						} else {
							p.playGraphic(157, 65, 100);
						}
						break;
					case 1607:
						hitDiff2 = Misc.random(MaxHit);
						if (p.playerEquipment[p.playerHat] != 4168) {
							p.sendMessage("The Aberrant spectre's max is above normal!");
							hitDiff2 = Misc.random(MaxHit + 10);
						}
						playAnimation(getMageEmote());
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 429, 50,
								31, 65, -StartKilling - 1);
						AtkDelay = 3;
						ActionTimer = 5;
						if (AtkStr <= mageDef) {
							hitDiff2 = 0;
						}
						break;
					case 1914:
						hitDiff2 = Misc.random(MaxHit);
						playAnimation(getMageEmote());
						p.createNpcGfx(145, 0, NpcSlot, 100);
						AtkDelay = 2;
						ActionTimer = 4;
						if (AtkStr <= mageDef || p.HeadPray == 1) {
							hitDiff2 = 0;
							p.playGraphic(85, 0, 100);
						} else {
							p.playGraphic(571, 0, 50);
						}
						break;
					case 172:
						hitDiff2 = Misc.random(MaxHit);
						playAnimation(getMageEmote());
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 378, 50,
								31, 65, -StartKilling - 1);
						AtkDelay = 3;
						ActionTimer = 5;
						if (AtkStr <= mageDef || p.HeadPray == 1) {
							hitDiff2 = 0;
							p.playGraphic(85, 65, 100);
						} else {
							p.playGraphic(381, 65, 0);
						}
						break;
					case 1590:
					case 1592:
					case 55:
					case 941:
					case 54:
					case 53:
					case 3070:
						hitDiff2 = Misc.random(MaxHit);
						Random = Misc.random(2);
						if (WithinDistance(absX, absY, p.absX, p.absY, 1)) {
							if (Random == 1) {
								playAnimation(getMageEmote());
								p.createProjectile(absY + (int) (size / 2),
										absX + (int) (size / 2), offsetY,
										offsetX, 130, 85, 31, 65,
										-StartKilling - 1);
								p.playGraphic(131, 65, 100);
							} else {
								playAnimation(getMeleeEmote());
								if (NpcType == 1590) {
									hitDiff2 = Misc.random(14);
								} else if (NpcType == 55) {
									hitDiff2 = Misc.random(7);
								} else if (NpcType == 941) {
									hitDiff2 = Misc.random(6);
								} else if (NpcType == 53) {
									hitDiff2 = Misc.random(8);
								} else if (NpcType == 54) {
									hitDiff2 = Misc.random(9);
								} else if (NpcType == 1592) {
									hitDiff2 = Misc.random(15);
								} else if (NpcType == 3070) {
									hitDiff2 = Misc.random(10);
								}
							}
						} else {
							playAnimation(getMageEmote());
							p.createProjectile(absY + (int) (size / 2), absX
									+ (int) (size / 2), offsetY, offsetX, 130,
									85, 31, 65, -StartKilling - 1);
							p.playGraphic(131, 65, 100);
							Random = 1;
						}
						if (Random == 1) {
							AtkDelay = 3;
							if (AtkStr <= mageDef) {
								hitDiff2 = 0;
							}
						} else {
							AtkDelay = 2;
							if (AtkStr <= meleeDef) {
								hitDiff2 = 0;
							}
						}
						ActionTimer = 7;
						break;
					case 2882:
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 162, 85,
								31, 65, -StartKilling - 1);
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= mageDef || p.HeadPray == 1) {
							hitDiff2 = 0;
							p.playGraphic(85, 65, 100);
						} else {
							p.playGraphic(477, 65, 100);
						}
						playAnimation(getMageEmote());
						AtkDelay = 3;
						ActionTimer = 6;
						break;
					case 2457:
						p.createProjectile(absY + (int) (size / 2), absX
								+ (int) (size / 2), offsetY, offsetX, 162, 35,
								31, 65, -StartKilling - 1);
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= mageDef || p.HeadPray == 1) {
							hitDiff2 = 0;
							p.playGraphic(85, 65, 100);
						} else {
							p.playGraphic(163, 65, 100);
						}
						playAnimation(getMageEmote());
						AtkDelay = 3;
						ActionTimer = 6;
						break;
					case 1913:
						multiIceAtk(369, 0, 30, 40, 3);
						playAnimation(getMageEmote());
						ActionTimer = 6;
						break;
					case 912:
					case 913:
					case 914:
						hitDiff2 = Misc.random(20);
						if (AtkStr <= mageDef || p.HeadPray == 1) {
							hitDiff2 = 0;
							p.playGraphic(85, 65, 100);
						} else {
							if (NpcType == 912) {
								p.playGraphic(78, 65, 100);
							}
							if (NpcType == 913) {
								p.playGraphic(76, 65, 100);
							}
							if (NpcType == 914) {
								p.playGraphic(77, 65, 100);
							}
						}
						playAnimation(getMageEmote());
						AtkDelay = 3;
						ActionTimer = 6;
						break;
					case 2025:
						int spell = Misc.random(5);
						if (spell == 5) {
							p.playGraphic(369, 0, 0);
							hitDiff2 = Misc.random(30);
							if (p.EntangleDelay <= 0) {
								p.EntangleDelay = 40;
							}
						} else if (spell == 4) {
							p.playGraphic(377, 0, 0);
							hitDiff2 = Misc.random(29);
							CurrentHp += hitDiff2 / 3;
							if (CurrentHp > MaxHp) {
								CurrentHp = MaxHp;
							}
						} else if (spell == 3) {
							p.playGraphic(383, 0, 0);
							hitDiff2 = Misc.random(28);
						} else if (spell == 2) {
							p.playGraphic(391, 0, 0);
							hitDiff2 = Misc.random(27);
							if (p.poisonDelay == -1) {
								p.poisonDelay = 100;
								p.poisonDamage = 6;
							}
						} else if (spell == 1) {
							p.playGraphic(363, 0, 0);
							hitDiff2 = Misc.random(22);
							if (p.EntangleDelay <= 0) {
								p.EntangleDelay = 20;
							}
						}
						if (AtkStr <= mageDef && Misc.random(100) >= 50) {
							hitDiff2 = 0;
						}
						playAnimation(getMageEmote());
						if (heightLevel == -1) {
							ahrimRun();
						}
						AtkDelay = 1;
						ActionTimer = 7;
						break;
					case 1264:
						if (WithinDistance(absX, absY, p.absX, p.absY, 1)) {
							if (Misc.random(3) == 1 && !HasSecondHit) {
								HasSecondHit = false;
								playAnimation(getMageEmote());
								p.playGraphic(76, 0, 100);
								hitDiff2 = Misc.random(20);
								if (AtkStr <= mageDef) {
									hitDiff2 = 0;
								}
							} else {
								hitDiff2 = Misc.random(MaxHit);
								if (HasSecondHit) {
									HasSecondHit = false;
								} else if (!HasSecondHit) {
									playAnimation(getMeleeEmote());
									p.createNpcGfx(252, 0, NpcSlot, 100);
									HasSecondHit = true;
								}
								if (AtkStr <= meleeDef) {
									hitDiff2 = 0;
								}
							}
						} else {
							playAnimation(getMageEmote());
							p.playGraphic(76, 0, 100);
							hitDiff2 = Misc.random(20);
							if (AtkStr <= mageDef) {
								hitDiff2 = 0;
							}
						}
						if (p.HeadPray == 1 && LastUsedAtk == 2) {
							hitDiff2 = 0;
						} else if (p.HeadPray == 3 && LastUsedAtk == 0) {
							hitDiff2 = 0;
						}
						p.updateHp(hitDiff2, false);
						if (p.hitUpdateRequired) {
							p.hitDiff2 = hitDiff2;
							p.hitUpdateRequired2 = true;
							p.updateRequired = true;
						} else {
							p.hitDiff = hitDiff2;
							p.hitUpdateRequired = true;
							p.updateRequired = true;
						}
						if (HasSecondHit) {
							ActionTimer = 1;
						} else {
							ActionTimer = 6;
						}
						break;
					default:
						hitDiff2 = Misc.random(MaxHit);
						if (AtkStr <= mageDef) {
							hitDiff2 = 0;
						}
						playAnimation(getMageEmote());
						if (p.NewHp - hitDiff2 < 0) {
							hitDiff2 = p.NewHp;
						}
						AtkDelay = 3;
						ActionTimer = 6;
						break;
					}
				}
			}
		}
		TurnNpcTo(p.absX, p.absY);
	}

	public void kalphRangedAtk(int Gfx, int Dmg, int height) {
		for (int i = 1; i < Server.s.playerHandler.maxPlayers; i++) {
			Client p = Server.s.playerHandler.clients[i];
			if (p != null) {
				if (WithinDistance(absX, absY, p.absX, p.absY, 9)
						&& p.heightLevel == heightLevel) {
					int offsetX = ((absX + (int) (size / 2)) - p.absX) * -1;
					int offsetY = ((absY + (int) (size / 2)) - p.absY) * -1;
					int Dmg2 = Misc.random(Dmg);
					p.createProjectile(absY + (int) (size / 2), absX
							+ (int) (size / 2), offsetY, offsetX, Gfx, height,
							31, 65, -i - 1);
					if (p.HeadPray == 2 && LastUsedAtk == 1) {
						Dmg2 = 0;
					}
					for (int q = 0; q < 5; q++) {
						if (p.delayedDmgSlot[q] == 0) {
							p.delayedDmgSlot[q] = p.playerId;
							p.delayedDmgTarget[q] = 1;
							p.delayedDmgType[q] = 1;
							p.delayedDmgHit[q] = Dmg2;
							p.delayedDmgTime[q] = 3;
							p.delayedSpellId[q] = -10;
							break;
						}
					}
					if (p.vengenceDelay > 0 && Dmg2 > 0) {
						double vengDmg = (Dmg2 / 1.2);
						vengDmg = Math.round(vengDmg - 0.5f);
						if (vengDmg <= 0) {
							vengDmg = 1;
						}
						hitDiff = (int) vengDmg;
						CurrentHp -= hitDiff;
						updateRequired = true;
						hitUpdateRequired = true;
						p.vengenceDelay = -1;
						p.displayText("Taste Vengeance!");
					}
				}
			}
		}
	}

	public void multiAtk(final int Gfx, final int height, final int Dmg,
			int distance) {
		for (int i = 1; i < Server.s.playerHandler.maxPlayers; i++) {
			final Client p = Server.s.playerHandler.clients[i];
			if (p != null) {
				if (WithinDistance(absX, absY, p.absX, p.absY, distance)
						&& p.heightLevel == heightLevel) {
					int offsetX = (absX - p.absX) * -1;
					int offsetY = (absY - p.absY) * -1;
					if (NpcType == 50) {
						int Dmgz = 40;
						if (p.playerEquipment[p.playerShield] == 1540
								|| p.playerEquipment[p.playerShield] == 8078) {
							Dmgz = 25;
						}
						int Dmg2 = Misc.random(Dmgz);
						p.playGraphic(Gfx, 0, height);
						if (p.HeadPray == 1 && LastUsedAtk == 2)
							Dmg2 = 0;
						else if (p.HeadPray == 2 && LastUsedAtk == 1)
							Dmg2 = 0;
						else if (p.HeadPray == 3 && LastUsedAtk == 0)
							Dmg2 = 0;
						p.updateHp(Dmg2, false);
						if (p.hitUpdateRequired) {
							p.hitDiff2 = Dmg2;
							p.hitUpdateRequired2 = true;
							p.updateRequired = true;
						} else {
							p.hitDiff = Dmg2;
							p.hitUpdateRequired = true;
							p.updateRequired = true;
						}
						if (p.vengenceDelay > 0 && Dmg2 > 0) {
							double vengDmg = (Dmg2 / 1.2);
							vengDmg = Math.round(vengDmg - 0.5f);
							if (vengDmg <= 0)
								vengDmg = 1;
							hitDiff = (int) vengDmg;
							CurrentHp -= hitDiff;
							updateRequired = true;
							hitUpdateRequired = true;
							p.vengenceDelay = -1;
							p.displayText("Taste Vengeance!");
						}
						return;
					}
					if (NpcType == 2843) {
						p.createProjectile(absY, absX, offsetY, offsetX, 88,
								65, 31, 120, -p.playerId - 1);
						EventManager.getSingleton().addEvent(new Event() {
							public void execute(EventContainer event) {
								p.playGraphic(Gfx, 0, height);
								p.appendHit(Misc.random(Dmg));
								event.stop();
							}
						}, 2500);
					}
					if (NpcType == 3847) {
						final int combatChoice = Misc.random(2);
						int end = 1;
						int pro = 1;
						int hit = 1;
						int pray = 1;
						if (combatChoice == 0) {
							pro = 159;
							end = 160;
							hit = 35;
							pray = 1;
						}
						if (combatChoice == 1) {
							pro = 165;
							end = 166;
							hit = 40;
							pray = 2;
						}
						if (combatChoice == 2) {
							pro = 156;
							end = 157;
							hit = 45;
							pray = 3;
						}
						final int endGFX = end;
						final int maxHit = hit;
						final int prayer = pray;
						p.createProjectile(3362, 2807, offsetY, offsetX, pro,
								70, 31, 80, -p.playerId - 1);
						Server.s.eventManager.schedule(new GameLogicTask() {
							@Override
							public void run() {
								try {
									int max = maxHit;
									if (p.HeadPray != prayer) {
										max += 5;
									}
									if (p.HeadPray == prayer) {
										max /= 2;
									}
									p.appendHit(Misc.random(max));
									p.playGraphic(endGFX, 0, 100);
									this.stop();
								} catch (Exception e) {

								}
							}
						}, 4, 0);
						// p.appendHit(Misc.random(Dmg));
					}
					/*
					 * EventManager.getSingleton().addEvent(new Event() { public
					 * void execute(EventContainer event) {
					 * p.CreatePlayerGfx(Gfx, 0, height);
					 * p.appendHit(misc.random(Dmg)); event.stop(); } }, 600);
					 */
				}
			}
		}
	}

	public void multiIceAtk(int Gfx, int height, int Dmg, int entDelay,
			int distance) {
		for (int i = 1; i < Server.s.playerHandler.maxPlayers; i++) {
			Client p = Server.s.playerHandler.clients[i];
			if (p != null) {
				if (WithinDistance(absX, absY, p.absX, p.absY, distance)
						&& p.heightLevel == heightLevel) {
					if (NpcType == 50
							&& (p.playerEquipment[p.playerShield] == 1540 || p.playerEquipment[p.playerShield] == 8078)) {
						Dmg = 40;
					}
					if (p.EntangleDelay <= 0 && NpcType != 50) {
						p.EntangleDelay = entDelay;
					} else if (p.EntangleDelay <= 0 && NpcType == 50
							&& p.HeadPray != 1) {
						p.EntangleDelay = entDelay;
					}
					int Dmg2 = Misc.random(Dmg);
					p.playGraphic(Gfx, 0, height);
					if (p.HeadPray == 1 && LastUsedAtk == 2) {
						Dmg2 = 0;
					} else if (p.HeadPray == 2 && LastUsedAtk == 1) {
						Dmg2 = 0;
					} else if (p.HeadPray == 3 && LastUsedAtk == 0) {
						Dmg2 = 0;
					}
					p.updateHp(Dmg2, false);
					if (p.hitUpdateRequired) {
						p.hitDiff2 = Dmg2;
						p.hitUpdateRequired2 = true;
						p.updateRequired = true;
					} else {
						p.hitDiff = Dmg2;
						p.hitUpdateRequired = true;
						p.updateRequired = true;
					}
					if (p.vengenceDelay > 0 && Dmg2 > 0) {
						double vengDmg = (Dmg2 / 1.2);
						vengDmg = Math.round(vengDmg - 0.5f);
						if (vengDmg <= 0) {
							vengDmg = 1;
						}
						hitDiff = (int) vengDmg;
						CurrentHp -= hitDiff;
						updateRequired = true;
						hitUpdateRequired = true;
						p.vengenceDelay = -1;
						p.displayText("Taste Vengeance!");
					}
				}
			}
		}
	}

	public boolean HasSecondHit = false;

	public int DefPower() {
		/*
		 * int MyDef = 0; MyDef = (int)((DefLvl + DefLvl) + (int)(DefLvl * 0.1))
		 * - (int)(statDrain); return MyDef;
		 */
		return (combatLevel + (int) (combatLevel * 0.75));
	}

	public int AtkPower() {
		/*
		 * int MyAtk = 0; MyAtk = (int)((DefLvl + DefLvl) + (int)(DefLvl * 0.1))
		 * - (int)(statDrain); return MyAtk;
		 */
		return (combatLevel + (int) (combatLevel * 0.1));
	}

	public void playAnimation(int id) {
		if (NpcType >= 3777 && NpcType <= 3780) {
			return;
		}
		if (NpcType >= 2440 && NpcType <= 2448) {
			return;
		}
		if (id < 0) {
			return;
		}
		AnimNumber = id;
		updateRequired = true;
		animUpdateRequired = true;
	}

	public boolean gargoyleDead = false;

	public void forceChat(String text) {
		textUpdate = text;
		textUpdateRequired = true;
	}

	public int castWait = 0;

	public void process() {
		if (sendDisarmingDelay > 0) {
			if (sendDisarmingDelay == 1) {
				disarming();
			}
			sendDisarmingDelay--;
		}
		if (sendPrimaryDelay > 0) {
			if (sendPrimaryDelay == 1) {
				primary();
			}
			sendPrimaryDelay--;
		}
		if (sendTeleotherDelay > 0) {
			if (sendTeleotherDelay == 1) {
				teleother();
			}
			sendTeleotherDelay--;
		}
		if (castWait > 0) {
			castWait--;
			if (castWait == 1) {
				multiAtk(89, 65, 26, 10);
			}
		}
		if (NpcType == 1152) {
			forceChat("Help! Zombies are taking over our land!");
		}
		/**
		 * Pets by Canownueasy tgpn1996@hotmail.com no mfing leeching
		 */
		if (owner != null) {
			if (commandedAttack == false) {
				SpawnedFor = owner.playerId;
				RandomWalk = false;
				TurnNpcTo(owner.absX, owner.absY);
				StartKilling = owner.playerId;
				followPlayer();
				/*
				 * Checks if the user is far away from the pet. If they are,
				 * teleports the pet to the owner.
				 */
			} else if (commandedAttack == true) {
				RandomWalk = false;
				if ((opppet.playerLevel[3] < 1)
						|| (owner.attackingPlayerId != opppet.playerId)) {
					commandedAttack = true;
					this.heightLevel = 10;
					this.height = 10;
					owner.respawnPet(NpcType);
					// owner.respawnPet();
					// System.out.println("STOPPED CUZ UNSATISTORY FACTORS!");
					/*
					 * TurnNpcTo(owner.absX, owner.absY); StartKilling =
					 * owner.playerId; followPlayer();
					 */
					// commandedAttack = false;
					// opppet = null;
					return;
				}
				// System.out.println("CONTINUED!!!");
				TurnNpcTo(opppet.absX, opppet.absY);
				SpawnedFor = opppet.playerId;
				StartKilling = opppet.playerId;
				followPlayer();
			}
			if (!WithinDistance(absX, absY, owner.absX, owner.absY, 8)) {
				absX = owner.absX;
				absY = owner.absY;
				updateRequired = true;
				forceChat("Hey " + owner.playerName + ", wait up!");
			}
		}
		AtkDelay--;
		EntangleDelay--;
		hpHealDelay--;
		kalphDelay--;
		if (Server.s.playerHandler.players[combatWith] == null) {
			combatWith = 0;
		} else if (Server.s.playerHandler.players[combatWith] != null) {
			Client cbPlr = Server.s.playerHandler.clients[combatWith];
			if (cbPlr.meleeDelay <= 0 && !cbPlr.projectileDmgExists()
					|| cbPlr.attacknpc != NpcSlot) {
				combatWith = 0;
			}
		}
		if (NpcType == 1459 && makeX == 2768 && makeY == 2801) {
			monkeyWalkTime--;
			if (monkeyWalkTime <= 0) {
				if (absX != makeX || absY != makeY) {
					moveX = GetMove(absX, makeX);
					moveY = GetMove(absY, makeY);
					getNextNPCMovement();
				} else {
					monkeyWalkTime = 999;
				}
			} else if (monkeyWalkTime == 998 || monkeyWalkTime == 997
					|| monkeyWalkTime == 996 || monkeyWalkTime == 987
					|| monkeyWalkTime == 986 || monkeyWalkTime == 985
					|| monkeyWalkTime == 984 || monkeyWalkTime == 983
					|| monkeyWalkTime == 966 || monkeyWalkTime == 965
					|| monkeyWalkTime == 964) {
				moveX = 0;
				moveY = -1;
			} else if (monkeyWalkTime == 993 || monkeyWalkTime == 992
					|| monkeyWalkTime == 991 || monkeyWalkTime == 973
					|| monkeyWalkTime == 972 || monkeyWalkTime == 971
					|| monkeyWalkTime == 970 || monkeyWalkTime == 969
					|| monkeyWalkTime == 961 || monkeyWalkTime == 960
					|| monkeyWalkTime == 959) {
				if (monkeyWalkTime == 959) {
					monkeyWalkTime = 14;
				}
				moveX = 0;
				moveY = 1;
			} else if (monkeyWalkTime == 982 || monkeyWalkTime == 968
					|| monkeyWalkTime == 967 || monkeyWalkTime == 963
					|| monkeyWalkTime == 962) {
				moveX = -1;
				moveY = 0;
			} else if (monkeyWalkTime == 995 || monkeyWalkTime == 994
					|| monkeyWalkTime == 990 || monkeyWalkTime == 989
					|| monkeyWalkTime == 974) {
				moveX = 1;
				moveY = 0;
			}
			getNextNPCMovement();
			for (int i = 0; i < Server.s.playerHandler.maxPlayers; i++) {
				Client p = Server.s.playerHandler.clients[i];
				if (p == null) {
					continue;
				}
				if (p.teleX <= 0 && p.teleY <= 0
						&& WithinDistance(absX, absY, p.absX, p.absY, 1)
						&& absX != 2768 && absY != 2801) {
					p.hitDiff2 = Misc.random(8);
					p.updateHp(p.hitDiff2, false);
					p.updateRequired = true;
					p.hitUpdateRequired2 = true;
					p.startTele(2772, 2793, 0, 2304, -1, -1, 5, 8677);
					p.apeHitAmt = 0;
					showText("ESCAPE IS FUTILE!!!");
					// StartAnimation(1402);
					// TurnNpcTo(p.absX, p.absY);
				}
			}
		}
		if (hpHealDelay == 0) {
			hpHealDelay = 92;
			if (CurrentHp < MaxHp) {
				CurrentHp++;
			}
		}
		if (kalphDelay == 0) {
			calcKalph();
		}
		if (poisonDelay >= 0) {
			poisonDelay--;
			poisonHitDelay--;
			if (poisonHitDelay == 0) {
				poisonHitDelay = Misc.random(4) + 16;
				int dmg = 0;
				if (poisonDelay >= 180) {
					dmg = 5;
				} else if (poisonDelay >= 150) {
					dmg = 4;
				} else if (poisonDelay >= 120) {
					dmg = 3;
				} else if (poisonDelay >= 60) {
					dmg = 2;
				}
				if (hitUpdateRequired) {
					poisonHit2 = true;
					hitDiff2 = Misc.random(dmg) + 1;
					CurrentHp -= hitDiff2;
					hitUpdateRequired2 = true;
					updateRequired = true;
				} else {
					poisonHit = true;
					hitDiff = Misc.random(dmg) + 1;
					CurrentHp -= hitDiff;
					hitUpdateRequired = true;
					updateRequired = true;
				}
			}
		}
		if (sheepDelay == 60 && NpcType == 43) {
			NpcType = 42;
			for (int j = 1; j < Server.s.playerHandler.maxPlayers; j++) {
				if (Server.s.playerHandler.players[j] != null) {
					Server.s.playerHandler.players[j].RebuildNPCList = true;
				}
			}
		} else if (sheepDelay == 0 && NpcType == 42) {
			NpcType = 43;
			for (int j = 1; j < Server.s.playerHandler.maxPlayers; j++) {
				if (Server.s.playerHandler.players[j] != null) {
					Server.s.playerHandler.players[j].RebuildNPCList = true;
				}
			}
		}
		sheepDelay--;
		if (CurrentHp <= 0 && MaxHp > 0) {
			if (NpcType != 1610 && NpcType != 1611) {
				IsDead = true;
			} else if (gargoyleDead) {
				IsDead = true;
			}
		}
		if (AtkDelay == 0) {
			Client p = Server.s.playerHandler.clients[projectileHit];
			if (p != null && !p.IsDead && !IsDead) {
				if (p.HeadPray == 1 && LastUsedAtk == 2 && NpcType != 2025
						&& NpcType != 1590 && NpcType != 55 && NpcType != 941
						&& NpcType != 53 && NpcType != 54 && NpcType != 1592
						&& NpcType != 3070) {
					hitDiff2 = 0;
				} else if (p.HeadPray == 1 && NpcType == 2025
						&& Misc.random(2) == 1) {
					hitDiff2 = 0;
				} else if (p.HeadPray == 2 && LastUsedAtk == 1
						&& NpcType != 2028) {
					hitDiff2 = 0;
				} else if (p.HeadPray == 2 && NpcType == 2028
						&& Misc.random(2) == 1) {
					hitDiff2 = 0;
				} else if (p.HeadPray == 3 && LastUsedAtk == 0) {
					hitDiff2 = 0;
				}
				if ((NpcType == 1590 || NpcType == 55 || NpcType == 941
						|| NpcType == 53 || NpcType == 54 || NpcType == 1592 || NpcType == 3070)
						&& LastUsedAtk == 2) {
					int AtkStr = Misc.random(AtkPower());
					int mageDef = Misc.random(p.mageDef());
					if (p.playerEquipment[5] == 1540
							|| p.playerEquipment[5] == 8078) {
						if (NpcType == 1590) {
							hitDiff2 = Misc.random(14);
						} else if (NpcType == 55) {
							hitDiff2 = Misc.random(7);
						} else if (NpcType == 941) {
							hitDiff2 = Misc.random(6);
						} else if (NpcType == 53) {
							hitDiff2 = Misc.random(8);
						} else if (NpcType == 54) {
							hitDiff2 = Misc.random(9);
						} else if (NpcType == 1592) {
							hitDiff2 = Misc.random(15);
						} else if (NpcType == 3070) {
							hitDiff2 = Misc.random(10);
						}
					}
					if (AtkStr <= mageDef) {
						hitDiff2 = 0;
					}
				}
				if (p.NewHp - hitDiff2 < 0) {
					hitDiff2 = p.NewHp;
				}
				afterEffect();
				p.startAnimation(p.GetBlockEmote(p
						.getItemName(p.playerEquipment[p.playerWeapon])), 0);
				p.updateHp(hitDiff2, false);
				if (p.hitUpdateRequired) {
					p.hitDiff2 = hitDiff2;
					p.hitUpdateRequired2 = true;
					p.updateRequired = true;
				} else {
					p.hitDiff = hitDiff2;
					p.hitUpdateRequired = true;
					p.updateRequired = true;
				}
			} else {
				ResetAttack();
			}
		}
		if (SpawnedFor != -1) {
			if (Server.s.playerHandler.players[SpawnedFor] == null
					|| Server.s.playerHandler.players[SpawnedFor].IsDead
					|| Server.s.playerHandler.players[SpawnedFor].playerLevel[3] <= 0) {
				absX = 0;
				absY = 0;
				CurrentHp = 0;
				Server.s.npcHandler.npcs[NpcSlot] = null;
				return;
			} else if (!WithinDistance(
					Server.s.playerHandler.players[SpawnedFor].absX,
					Server.s.playerHandler.players[SpawnedFor].absY, absX,
					absY, 80)) {
				absX = 0;
				absY = 0;
				CurrentHp = 0;
				Server.s.npcHandler.npcs[NpcSlot] = null;
				return;
			}
		}
		if (NpcToKill != 0) {
			if (Server.s.npcHandler.npcs[NpcToKill] == null) {
				NpcToKill = 0;
			} else if (Server.s.npcHandler.npcs[NpcToKill].IsDead
					|| Server.s.npcHandler.npcs[NpcToKill].CurrentHp <= 0) {
				ResetAttack();
			}
		}
		if (NpcType == 158 || NpcType == 3741 || NpcType == 3751
				|| NpcType == 479) {
			for (int i = 0; i < Server.s.npcHandler.maxNPCs; i++) {
				if (Server.s.npcHandler.npcs[i] != null) {
					if (NpcType == 158) {
						if (Server.s.npcHandler.npcs[i].NpcType == 158) {
							if (NpcSlot != i && RandomWalk) {
								if (WithinDistance(absX, absY,
										Server.s.npcHandler.npcs[i].absX,
										Server.s.npcHandler.npcs[i].absY, 3)) {
									RandomWalk = false;
									NpcToKill = i;
									KillingNpc = true;
									break;
								}
							}
						}
					} else if (NpcType == 3741) {
						if (Server.s.npcHandler.npcs[i].NpcType == 3782) {
							if (NpcSlot != i && RandomWalk) {
								if (WithinDistance(absX, absY,
										Server.s.npcHandler.npcs[i].absX,
										Server.s.npcHandler.npcs[i].absY, 3)) {
									RandomWalk = false;
									NpcToKill = i;
									KillingNpc = true;
									break;
								}
							}
						}
					} else if (NpcType == 479) {
						if (Server.s.npcHandler.npcs[i].NpcType == 1472) {
							if (NpcSlot != i && RandomWalk) {
								if (WithinDistance(absX, absY,
										Server.s.npcHandler.npcs[i].absX,
										Server.s.npcHandler.npcs[i].absY, 8)) {
									RandomWalk = false;
									NpcToKill = i;
									KillingNpc = true;
									break;
								}
							}
						}
					} else if (NpcType == 3751) {
						if (Server.s.npcHandler.npcs[i].NpcType >= 3777
								&& Server.s.npcHandler.npcs[i].NpcType <= 3780) {
							if (NpcSlot != i && RandomWalk) {
								if (WithinDistance(absX, absY,
										Server.s.npcHandler.npcs[i].absX,
										Server.s.npcHandler.npcs[i].absY, 3)) {
									RandomWalk = false;
									NpcToKill = i;
									KillingNpc = true;
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	public void afterEffect() {
		Client P = (Client) Server.s.playerHandler.players[projectileHit];
		if (P.playerEquipment[P.playerRing] == 2550) {
			double RecoilDmg = (hitDiff2 * 0.1);
			RecoilDmg = Math.round(RecoilDmg - 0.5f);
			if (RecoilDmg <= 0) {
				RecoilDmg = 1;
			}
			hitDiff = 0;
			hitDiff += RecoilDmg;
			CurrentHp -= hitDiff;
			updateRequired = true;
			hitUpdateRequired = true;
			P.Recoil();
		}
		if (P.vengenceDelay > 0 && hitDiff2 > 0) {
			double vengDmg = (hitDiff2 / 1.2);
			vengDmg = Math.round(vengDmg - 0.5f);
			if (vengDmg <= 0) {
				vengDmg = 1;
			}
			hitDiff = 0;
			hitDiff += vengDmg;
			CurrentHp -= hitDiff;
			updateRequired = true;
			hitUpdateRequired = true;
			P.vengenceDelay = -1;
			P.displayText("Taste Vengeance!");
		}
	}

	public int getMeleeEmote() {
		LastUsedAtk = 0;
		switch (NpcType) {
		case 3340:
			return 3312;
		case 73:
		case 74:
		case 75:
		case 76:
		case 77:
		case 2058:
			return 299;
		case 1265:
			return 1312;
		case 997:
			return 147;
		case 3847:
			return 3991;
		case 2263:
			return 2181;
		case 1460:
		case 1461:
			return 1402;
		case 1465:
			return 1383;
		case 2452:
			return 1312;
		case 2566:
			return 423;
		case 3070:
			return 2986;
		case 159:
		case 160:
		case 151:
		case 152:
		case 153:
		case 154:
		case 155:
		case 1974:
		case 1975:
			return 406;
		case 18:
		case 1977:
		case 239:
		case 241:
		case 243:
		case 244:
		case 245:
		case 246:
			return 451;
		case 12:
			return 1833;
		case 17:
			return 405;
		case 1353:
		case 1354:
		case 1355:
			return 1341;
		case 81:
			return 59;
		case 105:
			return 41;
		case 89:
			return 289;
		case 100:
			return 309;
		case 117:
		case 110:
		case 111:
		case 112:
		case 116:
			return 128;
		case 2745:
			return 2655;
		case 127:
			return 138;
		case 49:
			return 158;
		case 3741:
			return 3901;
		case 103:
		case 655:
			return 123;
		case 181:
		case 188:
		case 1:
			return 422;
		case 19:
			return 407;
		case 3776:
			return 3897;
		case 4:
			return 423;
		case 7:
		case 240:
		case 242:
			return 440;
		case 3751:
			return 3908;
		case 2744:
			return 2644;
		case 1613:
			return 1528;
		case 1158:
		case 1153:
		case 1154:
		case 1155:
			return 1184;
		case 1160:
			return 1178;
		case 414:
			return 153;
		case 2026:
			return 2067;
		case 2027:
			return 2080;
		case 2029:
			return 2068;
		case 2030:
			return 2062;
		case 2742:
			return 2637;
		case 2631:
			return 2628;
		case 2629:
			return 2625;
		case 2627:
			return 2621;
		case 2598:
			return 2609;
		case 2607:
			return 2611;
		case 2616:
			return 2610;
		case 1200:
			return 440;
		case 1610:
			return 1517;
		case 1615:
			return 1537;
		case 2783:
			return 2733;
		case 1590:
		case 1592:
			return 80;
		case 55:
		case 54:
		case 53:
		case 941:
		case 50:
			return 80;
		case 41:
			return 55;
		case 1470:
			return 64;
		case 87:
		case 88:
			return 138;
		case 2883:
			return 2853;
		case 93:
		case 1973:
		case 2037:
			return 260;
		case 1264:
			return 1062;
		case 1472:
		case 84:
		case 83:
		case 82:
		case 677:
		case 998:
		case 999:
		case 1000:
			return 64;
		case 158:
			return 391;
		default:
			return 0x326;
		}
	}

	public int getMageEmote() {
		LastUsedAtk = 2;
		switch (NpcType) {
		case 3200:
			return 3146;
		case 2843:
			return 1978;
		case 3850:
			return 711;
		case 2457:
			return 2365;
		case 2566:
			return 811;
		case 1643:
			return 1162;
		case 3070:
			return 2985;
		case 2892:
			return 2868;
		case 677:
		case 911:
			return 69;
		case 1607:
			return 1507;
		case 908:
			return 132;
		case 909:
			return 147;
		case 1914:
			return 724;
		case 2744:
			return 2647;
		case 2745:
			return 2656;
		case 3761:
			return 3882;
		case 1158:
			return 1185;
		case 1160:
			return 1170;
		case 2025:
			return 1979;
		case 912:
		case 913:
		case 1264:
			return 811;
		case 914:
			return 197;
		case 1913:
			return 1979;
		case 2595:
			return 2613;
		case 1590:
		case 1592:
		case 55:
		case 54:
		case 53:
		case 941:
		case 50:
			return 81;
		case 2882:
			return 2854;
		case 1353:
			return 1343;
		case 1354:
			return 1343;
		case 1355:
			return 1343;
		default:
			return 811;
		}
	}

	public int getRangeEmote() {
		LastUsedAtk = 1;
		switch (NpcType) {
		case 479:
			return 190;
		case 50:
			return 81;
		case 2566:
			return 811;
		case 1915:
			return 724;
		case 1353:
		case 1354:
		case 1355:
			return 1343;
		case 2631:
			return 2633;
		case 1158:
			return 1184;
		case 3771:
			return 3920;
		case 1160:
			return 1177;
		case 2028:
			return 427;
		case 2745:
			return 2652;
		case 1183:
			return 426;
		case 1342:
			return 1343;
		case 2881:
			return 2855;
		default:
			return 426;
		}
	}

	public int GetDeathEmote() {
		switch (NpcType) {
		case 3200:
			return 3147;
		case 3340:
			return 3310;
		case 73:
		case 74:
		case 75:
		case 76:
		case 77:
		case 2058:
			return 302;
		}
		if (NpcType == 1200) {
			return 0x900;
		} else if (NpcType == 3847) {
			return 3993;
		} else if (NpcType == 1353) {
			return 1342;
		} else if (NpcType == 1265) {
			return 1314;
		} else if (NpcType == 2263) {
			return 2183;
		} else if (NpcType == 1465) {
			return 1384;
		} else if (NpcType == 3070) {
			return 2987;
		} else if (NpcType == 2452) {
			return 1314;
		} else if (NpcType == 909) {
			return 146;
		} else if (NpcType == 997) {
			return 146;
		} else if (NpcType == 1460 || NpcType == 1461) {
			return 1404;
		} else if (NpcType == 105) {
			return 44;
		} else if (NpcType == 1607) {
			return 1508;
		} else if (NpcType == 914) {
			return 196;
		} else if (NpcType == 479) {
			return 196;
		} else if (NpcType == 81) {
			return 62;
		} else if (NpcType == 100) {
			return 313;
		} else if (NpcType == 1354) {
			return 1342;
		} else if (NpcType == 89) {
			return 292;
		} else if (NpcType == 1355) {
			return 1342;
		} else if (NpcType == 103) {
			return 126;
		} else if (NpcType == 655) {
			return 126;
		} else if (NpcType == 41) {
			return 57;
		} else if (NpcType == 117) {
			return 131;
		} else if (NpcType == 110) {
			return 131;
		} else if (NpcType == 111) {
			return 131;
		} else if (NpcType == 112) {
			return 131;
		} else if (NpcType == 116) {
			return 131;
		} else if (NpcType == 908) {
			return 131;
		} else if (NpcType == 49) {
			return 161;
		} else if (NpcType == 127) {
			return 141;
		} else if (NpcType == 3761) {
			return 3881;
		} else if (NpcType == 3741) {
			return 3903;
		} else if (NpcType == 3776) {
			return 3894;
		} else if (NpcType == 3751) {
			return 3910;
		} else if (NpcType == 3771) {
			return 3922;
		} else if (NpcType == 1183) {
			return 0x900;
		} else if (NpcType == 1613) {
			return 1529;
		} else if (NpcType == 1610) {
			return 1520;
		} else if (NpcType == 1611) {
			return 1520;
		} else if (NpcType == 1158) {
			return 1187;
		} else if (NpcType == 1153) {
			return 1187;
		} else if (NpcType == 1154) {
			return 1187;
		} else if (NpcType == 1155) {
			return 1187;
		} else if (NpcType == 414) {
			return 156;
		} else if (NpcType == 1160) {
			return 1182;
		} else if (NpcType == 2598 || NpcType == 2607 || NpcType == 2595
				|| NpcType == 2616) {
			return 2607;
		} else if (NpcType == 2627) {
			return 2620;
		} else if (NpcType == 2629) {
			return 2627;
		} else if (NpcType == 2631) {
			return 2630;
		} else if (NpcType == 2742) {
			return 2638;
		} else if (NpcType == 2744) {
			return 2646;
		} else if (NpcType == 2745) {
			return 2654;
		} else if (NpcType == 1615) {
			return 1538;
		} else if (NpcType == 2783) {
			return 2732;
		} else if (NpcType == 1590 || NpcType == 1592) {
			return 92;
		} else if (NpcType == 55) {
			return 92;
		} else if (NpcType == 54) {
			return 92;
		} else if (NpcType == 53) {
			return 92;
		} else if (NpcType == 941) {
			return 92;
		} else if (NpcType == 50) {
			return 92;
		} else if (NpcType == 87 || NpcType == 88) {
			return 141;
		} else if (NpcType == 1470) {
			return 67;
		} else if (NpcType == 1342) {
			return 1342;
		} else if (NpcType == 2881 || NpcType == 2882 || NpcType == 2883) {
			return 2856;
		} else if (NpcType == 93) {
			return 263;
		} else if (NpcType == 1973) {
			return 263;
		} else if (NpcType == 2037) {
			return 263;
		} else if (NpcType == 1472) {
			return 67;
		} else if (NpcType == 84) {
			return 67;
		} else if (NpcType == 83) {
			return 67;
		} else if (NpcType == 82) {
			return 67;
		} else if (NpcType == 998) {
			return 67;
		} else if (NpcType == 999) {
			return 67;
		} else if (NpcType == 1000) {
			return 67;
		} else if (NpcType == 677) {
			return 67;
		} else if (NpcType == 911) {
			return 67;
		} else if (NpcType == 2892) {
			return 2866;
		} else if (NpcType == 2457) {
			return 2367;
		} else {
			return 0x900;
		}
	}

	public int GetBlockEmote() {
		switch (NpcType) {
		case 3200:
			return 3145;
		case 3340:
			return 3311;
		case 73:
		case 74:
		case 75:
		case 76:
		case 77:
		case 2058:
			return 300;
		}
		if (NpcType == 1200) {
			return 403;
		} else if (NpcType == 3847) {
			return 3990;
		} else if (NpcType == 1265) {
			return 1313;
		} else if (NpcType == 2263) {
			return 2182;
		} else if (NpcType == 89) {
			return 290;
		} else if (NpcType == 1460 || NpcType == 1461) {
			return 1403;
		} else if (NpcType == 2457) {
			return 2366;
		} else if (NpcType == 105) {
			return 42;
		} else if (NpcType == 1465) {
			return 1393;
		} else if (NpcType == 2452) {
			return 1313;
		} else if (NpcType == 909) {
			return 144;
		} else if (NpcType == 997) {
			return 144;
		} else if (NpcType == 2892) {
			return 2869;
		} else if (NpcType == 1353) {
			return 1340;
		} else if (NpcType == 81) {
			return 60;
		} else if (NpcType == 914) {
			return 193;
		} else if (NpcType == 479) {
			return 193;
		} else if (NpcType == 3070) {
			return 2983;
		} else if (NpcType == 100) {
			return 312;
		} else if (NpcType == 1607) {
			return 1509;
		} else if (NpcType == 1354) {
			return 1340;
		} else if (NpcType == 1355) {
			return 1340;
		} else if (NpcType == 41) {
			return 56;
		} else if (NpcType == 117) {
			return 129;
		} else if (NpcType == 110) {
			return 129;
		} else if (NpcType == 111) {
			return 129;
		} else if (NpcType == 112) {
			return 129;
		} else if (NpcType == 116) {
			return 129;
		} else if (NpcType == 908) {
			return 129;
		} else if (NpcType == 127) {
			return 139;
		} else if (NpcType == 49) {
			return 159;
		} else if (NpcType == 103) {
			return 124;
		} else if (NpcType == 655) {
			return 124;
		} else if (NpcType == 19) {
			return 410;
		} else if (NpcType == 3776) {
			return 3895;
		} else if (NpcType == 1) {
			return 404;
		} else if (NpcType == 4) {
			return 404;
		} else if (NpcType == 3761) {
			return 3880;
		} else if (NpcType == 3771) {
			return 3921;
		} else if (NpcType == 1613) {
			return 1531;
		} else if (NpcType == 3751) {
			return 3909;
		} else if (NpcType == 3741) {
			return 3902;
		} else if (NpcType == 1183) {
			return 404;
		} else if (NpcType == 158) {
			return 399;
		} else if (NpcType == 414) {
			return 154;
		} else if (NpcType == 1158) {
			return 1186;
		} else if (NpcType == 1153) {
			return 1186;
		} else if (NpcType == 1154) {
			return 1186;
		} else if (NpcType == 1155) {
			return 1186;
		} else if (NpcType == 1160) {
			return 1179;
		} else if (NpcType == 1610) {
			return 1519;
		} else if (NpcType == 2598 || NpcType == 2607 || NpcType == 2595
				|| NpcType == 2616) {
			return 2606;
		} else if (NpcType == 2627) {
			return 2622;
		} else if (NpcType == 2629) {
			return 2626;
		} else if (NpcType == 2631) {
			return 2629;
		} else if (NpcType == 2742) {
			return 2635;
		} else if (NpcType == 2744) {
			return 2645;
		} else if (NpcType == 2745) {
			return 2653;
		} else if (NpcType == 1615) {
			return 1536;
		} else if (NpcType == 2783) {
			return 2730;
		} else if (NpcType == 1590 || NpcType == 1592) {
			return 89;
		} else if (NpcType == 55) {
			return 89;
		} else if (NpcType == 54) {
			return 89;
		} else if (NpcType == 53) {
			return 89;
		} else if (NpcType == 941) {
			return 89;
		} else if (NpcType == 50) {
			return 89;
		} else if (NpcType == 1470) {
			return 65;
		} else if (NpcType == 87 || NpcType == 88) {
			return 139;
		} else if (NpcType == 1342) {
			return 1340;
		} else if (NpcType == 2881 || NpcType == 2882 || NpcType == 2883) {
			return 2852;
		} else if (NpcType == 93) {
			return 261;
		} else if (NpcType == 1973) {
			return 261;
		} else if (NpcType == 2037) {
			return 261;
		} else if (NpcType == 1472) {
			return 65;
		} else if (NpcType == 84) {
			return 65;
		} else if (NpcType == 83) {
			return 65;
		} else if (NpcType == 82) {
			return 65;
		} else if (NpcType == 998) {
			return 65;
		} else if (NpcType == 999) {
			return 65;
		} else if (NpcType == 1000) {
			return 65;
		} else if (NpcType == 677) {
			return 65;
		} else if (NpcType == 911) {
			return 65;
		} else {
			return 404;
		}
	}

	public int attackSound() {
		switch (NpcType) {
		case 1200:
			return 5;
		case 1183:
			return 6;
		case 1:
		case 4:
			return 21;
		case 2025:
			return 16;
		case 2026:
			return 8;
		case 2027:
			return 10;
		case 2028:
			return 6;
		case 2029:
			return 7;
		case 2030:
			return 9;
		default:
			return 14;
		}
	}

	public void ResetAttack() {
		IsUnderAttack = false;
		KillingNpc = false;
		NpcToKill = 0;
		RandomWalk = true;
		StartKilling = 0;
	}

	private void appendHitUpdate(stream str) {
		if (str != null) {
			str.writeByteC(hitDiff);
			if (CurrentHp <= 0 && NpcType != 1610 && NpcType != 1611) {
				IsDead = true;
			}
			if (CurrentHp < 0) {
				CurrentHp = 0;
			}
			if (!poisonHit) {
				if (hitDiff > 0) {
					str.writeByteS(1);
				} else {
					str.writeByteS(0);
				}
			} else {
				str.writeByteS(2);
			}
			str.writeByteS(Misc.getCurrentHP(CurrentHp, MaxHp, 100));
			str.writeByteC(100);
		}
	}

	private void appendHitUpdate2(stream str) {
		if (str != null) {
			if (CurrentHp < 0) {
				CurrentHp = 0;
			}
			str.writeByteA(hitDiff2);
			if (CurrentHp <= 0 && NpcType != 1610 && NpcType != 1611) {
				IsDead = true;
			}
			if (!poisonHit2) {
				if (hitDiff2 > 0) {
					str.writeByteC(1);
				} else {
					str.writeByteC(0);
				}
			} else {
				str.writeByteC(2);
			}
			str.writeByteA(Misc.getCurrentHP(CurrentHp, MaxHp, 100));
			str.writeByte(100);
		}
	}

	private void appendAnimUpdate(stream str) {
		if (str != null) {
			str.writeWordBigEndian(AnimNumber);
			str.writeByte(1);
		}
	}

	private void appendFaceEntity(stream str) {
		if (str != null) {
			str.writeWord(direction);
		}
	}
}
