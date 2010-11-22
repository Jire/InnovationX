package src.innovationx.classic.model.player;

import java.io.*;
import java.util.*;
import java.net.*;
import java.sql.*;

import src.innovationx.classic.Server;
import src.innovationx.classic.content.minigames.FightPits;
import src.innovationx.classic.content.minigames.ZombieMinigame;
import src.innovationx.classic.model.AntiCrash;
import src.innovationx.classic.model.npc.NPC;
import src.innovationx.classic.net.stream;
import src.innovationx.classic.util.Misc;

public class PlayerHandler {

	public int updateSeconds = 0;
	public boolean updateRunning = false;

	public void loadAllClans() {
		BufferedReader br = null;
		String line = "";
		File clanFolder = new File("Clans");
		File[] clanFile = clanFolder.listFiles();
		int lnCnt = 0;
		int i4 = 0;
		for (int i = 0; i < clanFile.length; i++) {
			try {
				br = new BufferedReader(new FileReader(clanFile[i]));
				line = br.readLine();
				clanIds[Integer.parseInt(line)] = 1;
				int cId = Integer.parseInt(line);
				String clnName = clanFile[i].getName();
				clanName[cId] = clnName.substring(0, clnName.lastIndexOf("."));
				lnCnt = 0;
				while ((line = br.readLine()) != null) {
					i4 = line.lastIndexOf(",");
					clanPlayerName[cId][lnCnt] = line.substring(0, i4);
					clanPlayerRights[cId][lnCnt] = Integer.parseInt(line
							.substring(i4 + 1));
					lnCnt++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean saveClan(int clanId) {
		if (clanIds[clanId] == 0) {
			return false;
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("./Clans/"
					+ clanName[clanId] + ".txt"));
			bw.write(Integer.toString(clanId), 0, Integer.toString(clanId)
					.length());
			bw.newLine();
			for (int i = 0; i < maxClanSlots; i++) {
				if (clanPlayerName[clanId][i] != null
						&& !clanPlayerName[clanId][i].equalsIgnoreCase("")) {
					bw.write(clanPlayerName[clanId][i] + ","
							+ clanPlayerRights[clanId][i], 0,
							clanPlayerName[clanId][i].length() + 2);
					bw.newLine();
				}
			}
			bw.flush();
			bw.close();
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public boolean createClan(String cName, int pId) {
		if (players[pId] != null) {
			int clanId = getClanId(players[pId].playerName);
			if (clanId != -1) {
				return false;
			}
			if (!clanExists(cName)) {
				for (int i = 0; i < maxClans; i++) {
					if (clanIds[i] == 0) {
						clanIds[i] = 1;
						clanName[i] = cName;
						clanPlayerName[i][0] = players[pId].playerName;
						clanPlayerRights[i][0] = 3;
						buildClanInterface(i);
						saveClan(i);
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean deleteClan(String pName, int cId) {
		int clanId = -1;
		if (!pName.equals("NULL")) {
			clanId = getClanId(pName);
			if (clanId == -1) {
				return false;
			}
		} else {
			clanId = cId;
		}
		for (int i = 0; i < maxClanSlots; i++) {
			if (clanPlayerName[clanId][i].equalsIgnoreCase(pName)
					|| pName.equals("NULL")) {
				if (clanPlayerRights[clanId][i] == 3 || pName.equals("NULL")) {
					boolean success = (new File("./Clans/" + clanName[clanId]
							+ ".txt")).delete();
					for (int q = 0; q < maxClanSlots; q++) {
						clanPlayerName[clanId][q] = "";
						clanPlayerRights[clanId][q] = 0;
					}
					clanIds[clanId] = 0;
					clanName[clanId] = "";
					return true;
				}
			}
		}
		return false;
	}

	public void fixClanNames(int clanId) {
		if (clanId < 0) {
			return;
		}
		String[] usedSlot = new String[maxClanSlots];
		int[] usedSlotRights = new int[maxClanSlots];
		int slotUsedId = 0;
		boolean clanInUse = false;
		for (int i = 0; i < maxClanSlots; i++) {
			if (clanPlayerName[clanId][i] != null
					&& !clanPlayerName[clanId][i].equalsIgnoreCase("")) {
				usedSlot[slotUsedId] = clanPlayerName[clanId][i];
				usedSlotRights[slotUsedId] = clanPlayerRights[clanId][i];
				clanInUse = true;
				slotUsedId++;
			}
		}
		if (!clanInUse) {
			deleteClan("NULL", clanId);
			return;
		}
		for (int i = 0; i < maxClanSlots; i++) {
			clanPlayerName[clanId][i] = "";
			clanPlayerRights[clanId][i] = 0;
		}
		for (int i = 0; i < maxClanSlots; i++) {
			if (usedSlot[i] == null) {
				usedSlot[i] = "";
				usedSlotRights[i] = 0;
			}
		}
		clanPlayerName[clanId] = usedSlot;
		clanPlayerRights[clanId] = usedSlotRights;
	}

	public void buildClanInterface(int clanId) {
		for (int i = 0; i < maxClanSlots; i++) {
			if (clanPlayerName[clanId][i] != null
					&& !clanPlayerName[clanId][i].equalsIgnoreCase("")) {
				int id = GetIdFromName(clanPlayerName[clanId][i]);
				if (id != -1) {
					Client p = clients[id];
					p.buildClanInterface();
				}
			}
		}
	}

	public boolean addClanMember(String newPName, String pName) {
		int clanId = getClanId(pName);
		if (clanId == -1) {
			return false;
		}
		int otherClanId = getClanId(newPName);
		if (otherClanId != -1) {
			return false;
		}
		for (int i = 0; i < maxClanSlots; i++) {
			if (clanPlayerName[clanId][i] == null
					|| clanPlayerName[clanId][i].equalsIgnoreCase("")) {
				clanPlayerName[clanId][i] = newPName;
				clanPlayerRights[clanId][i] = 1;
				fixClanNames(clanId);
				buildClanInterface(clanId);
				saveClan(clanId);
				return true;
			}
		}
		return false;
	}

	public boolean removeClanMember(String newPName, String pName) {
		int clanId = getClanId(pName);
		if (clanId == -1) {
			return false;
		}
		int otherClanId = getClanId(newPName);
		if (otherClanId == -1) {
			return false;
		}
		if (otherClanId != clanId) {
			return false;
		}
		for (int i = 0; i < maxClanSlots; i++) {
			if (clanPlayerName[clanId][i] != null
					&& !clanPlayerName[clanId][i].equalsIgnoreCase("")) {
				if (clanPlayerName[clanId][i].equalsIgnoreCase(newPName)) {
					clanPlayerName[clanId][i] = "";
					clanPlayerRights[clanId][i] = 0;
					fixClanNames(clanId);
					buildClanInterface(clanId);
					saveClan(clanId);
					return true;
				}
			}
		}
		return false;
	}

	public boolean changeClanMemberRights(String newPName, String pName,
			int newRights) {
		int clanId = getClanId(pName);
		if (clanId == -1) {
			return false;
		}
		int otherClanId = getClanId(newPName);
		if (otherClanId == -1) {
			return false;
		}
		if (otherClanId != clanId) {
			return false;
		}
		for (int i = 0; i < maxClanSlots; i++) {
			if (clanPlayerName[clanId][i] != null
					&& !clanPlayerName[clanId][i].equalsIgnoreCase("")) {
				if (clanPlayerName[clanId][i].equalsIgnoreCase(newPName)) {
					clanPlayerRights[clanId][i] = newRights;
					buildClanInterface(clanId);
					saveClan(clanId);
					return true;
				}
			}
		}
		return false;
	}

	public int getClanId(String pName) {
		for (int i = 0; i < maxClans; i++) {
			if (clanIds[i] == 1) {
				for (int q = 0; q < maxClanSlots; q++) {
					if (clanPlayerName[i][q] != null
							&& !clanPlayerName[i][q].equalsIgnoreCase("")) {
						if (clanPlayerName[i][q].equalsIgnoreCase(pName)) {
							return i;
						}
					}
				}
			}
		}
		return -1;
	}

	public int getClanRights(String pName) {
		for (int i = 0; i < maxClans; i++) {
			if (clanIds[i] == 1) {
				for (int q = 0; q < maxClanSlots; q++) {
					if (clanPlayerName[i][q] != null
							&& !clanPlayerName[i][q].equalsIgnoreCase("")) {
						if (clanPlayerName[i][q].equalsIgnoreCase(pName)) {
							return clanPlayerRights[i][q];
						}
					}
				}
			}
		}
		return -1;
	}

	public boolean clanExists(String cName) {
		for (int i = 0; i < maxClans; i++) {
			if (clanName[i] != null && !clanName[i].equalsIgnoreCase("")) {
				if (clanName[i].equalsIgnoreCase(cName)) {
					return true;
				}
			}
		}
		return false;
	}

	public PlayerHandler() {
		for (int i = 0; i < maxClans; i++) {
			clanIds[i] = 0;
			clanName[i] = "";
			for (int q = 0; q < maxClanSlots; q++) {
				clanPlayerName[i][q] = "";
				clanPlayerRights[i][q] = 0;
			}
		}
		loadAllClans();
	}

	public int GetIdFromName(String playerName) {
		for (Client p : clients) {
			if (p == null)
				continue;
			if (p.playerName.equalsIgnoreCase(playerName)) {
				return p.playerId;
			}
		}
		return -1;
	}

	public void newPlayerClient(Socket s, String hostAddress) {
		int index = -1;
		for (int i = 1; i < maxPlayers; i++) {
			if (clients[i] == null || players[i] == null) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			try {
				s.close();
			} catch (Exception e) {
			}
			return;
		}
		clients[index] = new Client(s, index);
		clients[index].handler = this;
		players[index] = clients[index];
		players[index].PlayerIp = hostAddress;
		// Server.s.cachedThreadPool.execute(new Thread(clients[index]));
		new Thread(clients[index]).start();
	}

	public boolean bannedPlayer(String playerName) {
		String name = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"./Data/BannedUsers.txt"));
			while ((name = in.readLine()) != null) {
				if (playerName.startsWith(name)) {
					in.close();
					return true;
				}
			}
			in.close();
		} catch (IOException e) {
		}
		return false;
	}

	public boolean bannedHost(String host) {
		String loggedHosts = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"./Data/BannedIps.txt"));
			while ((loggedHosts = in.readLine()) != null) {
				if (host.startsWith(loggedHosts)) {
					in.close();
					return true;
				}
			}
			in.close();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		return false;
	}

	public void destruct() {
	}

	public void updatePlayerNames() {
		playerCount = 0;
		int slot = 0;
		for (int i = 0; i < maxPlayers; i++) {
			if (players[i] != null) {
				playersCurrentlyOn[slot] = players[i].playerName;
				playerCount++;
			} else {
				playersCurrentlyOn[slot] = "";
			}
			slot++;
		}
	}

	public boolean isPlayerOn(String playerName) {
		for (Player p : players) {
			if (p != null) {
				if (p.playerName.equalsIgnoreCase(playerName)) {
					return true;
				}
			}
		}
		return false;
	}

	public int getPlayerID(String playerName) {
		for (int i = 1; i < maxPlayers; i++) {
			if (playersCurrentlyOn[i] != null) {
				if (playersCurrentlyOn[i].equalsIgnoreCase(playerName)) {
					return i;
				}
			}
		}
		return -1;
	}

	public void process() {
		try {
			if (messageToAll.length() != 0) {
				int msgTo = 0;
				do {
					if (players[msgTo] != null) {
						players[msgTo].globalMessage = messageToAll;
					}
					msgTo++;
				} while (msgTo < maxPlayers);
				messageToAll = "";
			}
			rockFallDelay--;
			for (Client c : clients) {
				try {
					if (c == null || (c.disconnected) || !c.isActive) {
						continue;
					}
					for (int pk = 0; pk < 15; pk++) {
						if (!c.PacketProcess()) {
							break;
						}
					}
					c.process();
					c.postProcessing();
					c.getNextPlayerMovement();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (Client c : clients) {
				try {
					if (c == null || !c.isActive) {
						continue;
					}
					if (!c.initialized) {
						c.initialize();
						c.initialized = true;
					} else {
						c.UpdateAll();
					}
				} catch (Exception e) {
				}
			}
			for (Client c : clients) {
				if (c == null) {
					continue;
				}
				if (c.isActive && !c.disconnected) {
					c.clearUpdateFlags();
				}
			}
			for (Client c : clients) {
				if (c == null || !c.disconnected) {
					continue;
				}
				for (int p = 1; p < Server.s.npcHandler.maxNPCs; p++) {
					if (Server.s.npcHandler.npcs[p] != null) {
						if (Server.s.npcHandler.npcs[p].SpawnedFor == c.playerId) {
							Server.s.npcHandler.npcs[p].absX = 0;
							Server.s.npcHandler.npcs[p].absY = 0;
							Server.s.npcHandler.npcs[p].CurrentHp = 0;
							Server.s.npcHandler.npcs[p] = null;
						}
					}
				}
				if (c.savefile && c.isActive) {
					SaveChar(c);
					SaveMoreInfo(c);
					c.savefile = false;
				}
				c.declineTrade();
				c.declineDuel();
				removePlayer(c.playerId);
				continue;
			}
			updatePlayerNames();
			if (rockFallDelay == 0) {
				rockFallDelay = 24;
			}
		} catch (Exception e) {
			System.out.println("Error caused in processing player");
			e.printStackTrace();
		}
	}

	public void updateNPC(Player plr, stream str) {
		if (plr != null && str != null) {
			updateBlock.currentOffset = 0;
			str.createFrameVarSizeWord(65);
			str.initBitAccess();
			str.writeBits(8, plr.npcList.size());
			ListIterator<NPC> npcArray = plr.npcList.listIterator();
			while (npcArray.hasNext()) {
				NPC n = npcArray.next();
				if (n == null || !plr.withinDistance(n) || plr.RebuildNPCList) {
					npcArray.remove();
					str.writeBits(1, 1);
					str.writeBits(2, 3);
				} else {
					n.updateNPCMovement(str);
					n.appendNPCUpdateBlock(updateBlock);
				}
			}
			for (NPC n : Server.s.npcHandler.npcs) {
				if (n == null || plr.npcList.contains(n)
						|| !plr.withinDistance(n)) {
					continue;
				}
				plr.addNewNPC(n, str, updateBlock);
			}
			plr.RebuildNPCList = false;
			if (updateBlock.currentOffset > 0) {
				str.writeBits(14, 16383);
				str.finishBitAccess();
				str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
			} else {
				str.finishBitAccess();
			}
			str.endFrameVarSizeWord();
		}
	}

	public stream updateBlock = new stream(new byte[20000]);

	/*
	 * public void updatePlayer(Player plr, stream str) { if (plr != null && str
	 * != null && updateBlock != null) { updateBlock.currentOffset = 0;
	 * plr.updateThisPlayerMovement(str); boolean saveChatTextUpdate =
	 * plr.chatTextUpdateRequired; plr.chatTextUpdateRequired = false;
	 * plr.appendPlayerUpdateBlock(updateBlock); plr.chatTextUpdateRequired =
	 * saveChatTextUpdate; str.writeBits(8, plr.playerList.size());
	 * ListIterator<Player> playerArray = plr.playerList.listIterator(); while
	 * (playerArray.hasNext()) { Player p2 = playerArray.next(); if (p2 == null
	 * || !plr.withinDistance(p2) || p2.didTeleport) { playerArray.remove();
	 * str.writeBits(1, 1); str.writeBits(2, 3); } else {
	 * p2.updatePlayerMovement(str); p2.appendPlayerUpdateBlock(updateBlock); }
	 * } for (Player p2 : players) { if (p2 == null || !p2.isActive ||
	 * plr.equals(p2) || !plr.withinDistance(p2) || plr.playerList.contains(p2))
	 * { continue; } plr.addNewPlayer(p2, str, updateBlock); } if
	 * (updateBlock.currentOffset > 0) { str.writeBits(11, 2047);
	 * str.finishBitAccess(); str.writeBytes(updateBlock.buffer,
	 * updateBlock.currentOffset, 0); } else { str.finishBitAccess(); }
	 * str.endFrameVarSizeWord(); } }
	 */
	public void updatePlayer(Player plr, stream str) {
		updateBlock.currentOffset = 0;

		if (updateRunning) {
			str.createFrame(114);
			str.writeWordBigEndian(updateSeconds * 50 / 30);
		}

		// update thisPlayer
		plr.updateThisPlayerMovement(str); // handles walking/running and
		// teleporting
		// do NOT send chat text back to thisPlayer!
		boolean saveChatTextUpdate = plr.chatTextUpdateRequired;
		plr.chatTextUpdateRequired = false;
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.chatTextUpdateRequired = saveChatTextUpdate;

		str.writeBits(8, plr.playerListSize);
		int size = plr.playerListSize;

		// update/remove players that are already in the playerList
		plr.playerListSize = 0; // we're going to rebuild the list right away
		for (int i = 0; i < size; i++) {
			// this update packet does not support teleporting of other players
			// directly
			// instead we're going to remove this player here and readd it right
			// away below
			if ((plr.playerList[i].didTeleport == false)
					&& (plr.didTeleport == false)
					&& (plr.withinDistance(plr.playerList[i]) == true)) {
				plr.playerList[i].updatePlayerMovement(str);
				plr.playerList[i].appendPlayerUpdateBlock(updateBlock);
				plr.playerList[plr.playerListSize++] = plr.playerList[i];
			} else {
				int id = plr.playerList[i].playerId;
				plr.playerInListBitmap[id >> 3] &= ~(1 << (id & 7)); // clear
				// the
				// flag
				str.writeBits(1, 1);
				str.writeBits(2, 3); // tells client to remove this char
				// from list
			}
		}

		// iterate through all players to check whether there's new players to
		// add
		for (int i = 0; i < maxPlayers; i++) {
			if ((players[i] == null) || (players[i].isActive == false)
					|| (players[i] == plr)) {
				// not existing, not active or you are that player
			} else {
				int id = players[i].playerId;
				if ((plr.didTeleport == false)
						&& ((plr.playerInListBitmap[id >> 3] & (1 << (id & 7))) != 0)) {
					// player already in playerList
				} else if (plr.withinDistance(players[i]) == false) {
					// out of sight
				} else {
					plr.addNewPlayer(players[i], str, updateBlock);
				}
			}
		}

		if (updateBlock.currentOffset > 0) {
			str.writeBits(11, 2047); // magic EOF - needed only when player
			// updateblock follows
			str.finishBitAccess();

			// append update block
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}
		str.endFrameVarSizeWord();
	}

	public int lastchatid = 1;

	public void removePlayer(int i) {
		if (clients[i] == null) {
			return;
		}
		clients[i].playerListSize = 0;
		clients[i].npcListSize = 0;
		clients[i].removePet(true);
		AntiCrash.removeIP(clients[i].PlayerIp);
		FightPits.players.remove(clients[i].playerName);
		FightPits.fighters.remove(clients[i].playerName);
		FightPits.waiters.remove(clients[i].playerName);
		ZombieMinigame.players.remove(clients[i].playerName);
		Server.s.ClanWars.teamBluePlayers.remove(clients[i]);
		Server.s.ClanWars.teamRedPlayers.remove(clients[i]);
		clients[i].isActive = false;
		/*
		 * if(clients[i].playerName != "null") {
		 * System.out.println(clients[i].playerName + " has logged out."); }
		 */
		clients[i].destruct();
		clients[i].playerName = "";
		if (clients[i].TradingWith != 0) {
			clients[i].declineTrade();
		}
		clients[i].declineDuel();
		players[i] = null;
		clients[i] = null;
	}

	public boolean SaveMoreInfo(Player plr) {
		BufferedWriter characterfile = null;
		try {
			characterfile = new BufferedWriter(new FileWriter(
					"C:/Users/Administrator/Desktop/BattleScape/Characters/MoreInfo/"
							+ plr.playerName + ".txt"));
			characterfile.write("[Player-Clues]", 0, 14);
			characterfile.newLine();
			characterfile.write("Clue-Delay = ", 0, 13);
			characterfile.write(Integer.toString(plr.clueRewardDelay), 0,
					Integer.toString(plr.clueRewardDelay).length());
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("[Player-Kills]", 0, 14);
			characterfile.newLine();
			characterfile.write("Fight-Arena-Kills = ", 0, 20);
			characterfile.write(Integer.toString(plr.FightArenaKills), 0,
					Integer.toString(plr.FightArenaKills).length());
			characterfile.newLine();
			characterfile.write("Mage-Arena-Kills = ", 0, 19);
			characterfile.write(Integer.toString(plr.mageArenaKills), 0,
					Integer.toString(plr.mageArenaKills).length());
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("[RecoilRing]", 0, 11);
			characterfile.newLine();
			characterfile.write("Recoil-Ring = ", 0, 14);
			characterfile.write(Integer.toString(plr.RecoilRing), 0, Integer
					.toString(plr.RecoilRing).length());
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("[Barrows]", 0, 9);
			characterfile.newLine();
			characterfile.write("dharok-at = ", 0, 12);
			characterfile.write(Integer.toString(plr.dharokSummoned), 0,
					Integer.toString(plr.dharokSummoned).length());
			characterfile.newLine();
			characterfile.write("ahrim-at = ", 0, 11);
			characterfile.write(Integer.toString(plr.ahrimSummoned), 0, Integer
					.toString(plr.ahrimSummoned).length());
			characterfile.newLine();
			characterfile.write("guthan-at = ", 0, 12);
			characterfile.write(Integer.toString(plr.guthanSummoned), 0,
					Integer.toString(plr.guthanSummoned).length());
			characterfile.newLine();
			characterfile.write("karil-at = ", 0, 11);
			characterfile.write(Integer.toString(plr.karilSummoned), 0, Integer
					.toString(plr.karilSummoned).length());
			characterfile.newLine();
			characterfile.write("torag-at = ", 0, 11);
			characterfile.write(Integer.toString(plr.toragSummoned), 0, Integer
					.toString(plr.toragSummoned).length());
			characterfile.newLine();
			characterfile.write("verac-at = ", 0, 11);
			characterfile.write(Integer.toString(plr.veracSummoned), 0, Integer
					.toString(plr.veracSummoned).length());
			characterfile.newLine();
			characterfile.write("hiddenB = ", 0, 10);
			characterfile.write(Integer.toString(plr.hiddenBarrowBro), 0,
					Integer.toString(plr.hiddenBarrowBro).length());
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("[Other]", 0, 7);
			characterfile.newLine();
			characterfile.write("poisonDelay = ", 0, 14);
			characterfile.write(Integer.toString(plr.poisonDelay), 0, Integer
					.toString(plr.poisonDelay).length());
			characterfile.newLine();
			characterfile.write("receivedExpRing2 = ", 0, 19);
			characterfile.write(Integer.toString(plr.hasReceivedRing), 0, Integer
					.toString(plr.hasReceivedRing).length());
			characterfile.newLine();
			characterfile.write("poisonDamage = ", 0, 15);
			characterfile.write(Integer.toString(plr.poisonDamage), 0, Integer
					.toString(plr.poisonDamage).length());
			characterfile.newLine();
			characterfile.write("specialamt = ", 0, 13);
			characterfile.write(Integer.toString(plr.specialAmount), 0, Integer
					.toString(plr.specialAmount).length());
			characterfile.newLine();
			characterfile.write("spellset = ", 0, 11);
			characterfile.write(Integer.toString(plr.spellSet), 0, Integer
					.toString(plr.spellSet).length());
			characterfile.newLine();
			// if (plr.vengenceDelay < -60)
			// plr.vengenceDelay = -60;
			// characterfile.write("vengence = ", 0, 11);
			// characterfile.write(Integer.toString(plr.vengenceDelay), 0,
			// Integer.toString(plr.vengenceDelay).length());
			// characterfile.newLine();
			characterfile.write("muted = ", 0, 8);
			characterfile.write(Integer.toString(plr.muted), 0, Integer
					.toString(plr.muted).length());
			characterfile.newLine();
			characterfile.write("pc = ", 0, 5);
			characterfile.write(Integer.toString(plr.pcPoints), 0, Integer
					.toString(plr.pcPoints).length());
			characterfile.newLine();
			characterfile.write("pk = ", 0, 5);
			characterfile.write(Integer.toString(plr.pkPoints), 0, Integer
					.toString(plr.pkPoints).length());
			characterfile.newLine();
			characterfile.write("zombie = ", 0, 9);
			characterfile.write(Integer.toString(plr.zombiePoints), 0, Integer
					.toString(plr.zombiePoints).length());
			characterfile.newLine();
			characterfile.write("clanwar = ", 0, 10);
			characterfile.write(Integer.toString(plr.clanWarsPoints), 0,
					Integer.toString(plr.clanWarsPoints).length());
			characterfile.newLine();
			if (plr.skullTimer < 0) {
				plr.skullTimer = 0;
			}
			characterfile.write("skull = ", 0, 8);
			characterfile.write(Integer.toString(plr.skullTimer), 0, Integer
					.toString(plr.skullTimer).length());
			characterfile.newLine();
			characterfile.write("tzwave = ", 0, 9);
			characterfile.write(Integer.toString(plr.tzWave), 0, Integer
					.toString(plr.tzWave).length());
			characterfile.newLine();
			characterfile.write("cbowShots = ", 0, 12);
			characterfile.write(Integer.toString(plr.crystalBowShots), 0,
					Integer.toString(plr.crystalBowShots).length());
			characterfile.newLine();
			characterfile.write("ancQuest = ", 0, 11);
			characterfile.write(Integer.toString(plr.ancQuest), 0, Integer
					.toString(plr.ancQuest).length());
			characterfile.newLine();
			characterfile.write("legendQuest = ", 0, 14);
			characterfile.write(Integer.toString(plr.legendQuest), 0, Integer
					.toString(plr.legendQuest).length());
			characterfile.newLine();
			characterfile.write("mageZQuest = ", 0, 13);
			characterfile.write(Integer.toString(plr.mageZQuest), 0, Integer
					.toString(plr.mageZQuest).length());
			characterfile.newLine();
			characterfile.write("slayerId = ", 0, 11);
			characterfile.write(Integer.toString(plr.slayerId), 0, Integer
					.toString(plr.slayerId).length());
			characterfile.newLine();
			characterfile.write("slayerAmt = ", 0, 12);
			characterfile.write(Integer.toString(plr.slayerAmt), 0, Integer
					.toString(plr.slayerAmt).length());
			characterfile.newLine();
			characterfile.write("runeMysteriesQuest = ", 0, 21);
			characterfile.write(Integer.toString(plr.runeMysteriesQuest), 0,
					Integer.toString(plr.runeMysteriesQuest).length());
			characterfile.newLine();
			characterfile.write("mageArenaQuest = ", 0, 17);
			characterfile.write(Integer.toString(plr.mageArenaQuest), 0,
					Integer.toString(plr.mageArenaQuest).length());
			characterfile.newLine();
			characterfile.write("lunarQuest = ", 0, 13);
			characterfile.write(Integer.toString(plr.lunarQuest), 0, Integer
					.toString(plr.lunarQuest).length());
			characterfile.newLine();
			characterfile.write("knightWavesQuest = ", 0, 19);
			characterfile.write(Integer.toString(plr.knightWavesQuest), 0,
					Integer.toString(plr.knightWavesQuest).length());
			characterfile.newLine();
			characterfile.write("skillId = ", 0, 10);
			characterfile.write(Integer.toString(plr.skillId), 0, Integer
					.toString(plr.skillId).length());
			characterfile.newLine();
			characterfile.write("lostCityQuest = ", 0, 16);
			characterfile.write(Integer.toString(plr.lostCityQuest), 0, Integer
					.toString(plr.lostCityQuest).length());
			characterfile.newLine();
			characterfile.write("forgeRing = ", 0, 12);
			characterfile.write(Integer.toString(plr.forgeRing), 0, Integer
					.toString(plr.forgeRing).length());
			characterfile.newLine();
			characterfile.write("monkeyMadnessQuest = ", 0, 21);
			characterfile.write(Integer.toString(plr.monkeyMadnessQuest), 0,
					Integer.toString(plr.monkeyMadnessQuest).length());
			characterfile.newLine();
			characterfile.write("undergroundPassQuest = ", 0, 23);
			characterfile.write(Integer.toString(plr.undergroundPassQuest), 0,
					Integer.toString(plr.undergroundPassQuest).length());
			characterfile.newLine();
			characterfile.write("bankPin = ", 0, 10);
			characterfile.write(Integer.toString(plr.bankPin), 0, Integer
					.toString(plr.bankPin).length());
			characterfile.newLine();
			characterfile.write("[EOF]", 0, 5);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.flush();
			characterfile.close();
		} catch (IOException ioexception) {
			Misc.println(plr.playerName + ": error writing more info file.");
			return false;
		}
		return true;
	}

	public boolean SaveChar(Player plr) {
		BufferedWriter characterfile = null;
		try {
			if (plr.disconnected) {
				if (plr.duelStatus > 0) {
					if (Server.s.playerHandler.players[plr.duelWith] != null) {
						Client P = (Client) Server.s.playerHandler.players[plr.duelWith];
						if (P.duelStatus == 3) {
							P.DuelVictory();
						} else {
							Client p = (Client) plr;
							p.declineDuel();
						}
					}
				}
				if (plr.meleeFightStatus == 1) {
					if (Server.s.playerHandler.players[plr.meleeFightWith] != null) {
						Client P = (Client) Server.s.playerHandler.players[plr.meleeFightWith];
						if (P.meleeFightStatus == 1) {
							P.meleeFightVictory();
						} else {
							P.RemoveAllWindows();
						}
					}
				}
			}
			characterfile = new BufferedWriter(new FileWriter(
					"C:/Users/Administrator/Desktop/BattleScape/Characters/"
							+ plr.playerName + ".txt"));
			characterfile.write("[ACCOUNT]", 0, 9);
			characterfile.newLine();
			characterfile.write("character-username = ", 0, 21);
			characterfile.write(plr.playerName, 0, plr.playerName.length());
			characterfile.newLine();
			characterfile.write("character-password = ", 0, 21);
			characterfile.write(plr.playerPass, 0, plr.playerPass.length());
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("[CHARACTER]", 0, 11);
			characterfile.newLine();
			characterfile.write("character-height = ", 0, 19);
			characterfile.write(Integer.toString(plr.heightLevel), 0, Integer
					.toString(plr.heightLevel).length());
			characterfile.newLine();
			characterfile.write("character-posx = ", 0, 17);
			characterfile.write(Integer.toString(plr.absX), 0, Integer
					.toString(plr.absX).length());
			characterfile.newLine();
			characterfile.write("character-posy = ", 0, 17);
			characterfile.write(Integer.toString(plr.absY), 0, Integer
					.toString(plr.absY).length());
			characterfile.newLine();
			characterfile.write("character-rights = ", 0, 19);
			characterfile.write(Integer.toString(plr.playerRights), 0, Integer
					.toString(plr.playerRights).length());
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("[EQUIPMENT]", 0, 11);
			characterfile.newLine();
			for (int i = 0; i < plr.playerEquipment.length; i++) {
				characterfile.write("character-equip = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("    ", 0, 1);
				characterfile.write(Integer.toString(plr.playerEquipment[i]),
						0, Integer.toString(plr.playerEquipment[i]).length());
				characterfile.write("    ", 0, 1);
				characterfile.write(Integer.toString(plr.playerEquipmentN[i]),
						0, Integer.toString(plr.playerEquipmentN[i]).length());
				characterfile.write("    ", 0, 1);
				characterfile.newLine();
			}
			characterfile.newLine();
			characterfile.write("[SKILLS]", 0, 8);
			characterfile.newLine();
			for (int i = 0; i < plr.playerLevel.length; i++) {
				characterfile.write("character-skill = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("    ", 0, 1);
				characterfile.write(Integer.toString(plr.playerLevel[i]), 0,
						Integer.toString(plr.playerLevel[i]).length());
				characterfile.write("    ", 0, 1);
				characterfile.write(Integer.toString(plr.playerXP[i]), 0,
						Integer.toString(plr.playerXP[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();
			characterfile.write("[ITEMS]", 0, 7);
			characterfile.newLine();
			for (int i = 0; i < plr.playerItems.length; i++) {
				if (plr.playerItems[i] > 0) {
					characterfile.write("character-item = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("    ", 0, 1);
					characterfile.write(Integer.toString(plr.playerItems[i]),
							0, Integer.toString(plr.playerItems[i]).length());
					characterfile.write("    ", 0, 1);
					characterfile.write(Integer.toString(plr.playerItemsN[i]),
							0, Integer.toString(plr.playerItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			characterfile.write("[BANK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < plr.bankItems.length; i++) {
				if (plr.bankItems[i] > 0) {
					characterfile.write("character-bank = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("    ", 0, 1);
					characterfile.write(Integer.toString(plr.bankItems[i]), 0,
							Integer.toString(plr.bankItems[i]).length());
					characterfile.write("    ", 0, 1);
					characterfile.write(Integer.toString(plr.bankItemsN[i]), 0,
							Integer.toString(plr.bankItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			characterfile.write("[FRIENDS]", 0, 9);
			characterfile.newLine();
			for (int i = 0; i < plr.friends.length; i++) {
				if (plr.friends[i] > 0) {
					characterfile.write("character-friend = ", 0, 19);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("    ", 0, 1);
					characterfile.write(Long.toString(plr.friends[i]), 0, Long
							.toString(plr.friends[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			characterfile.write("[IGNORES]", 0, 9);
			characterfile.newLine();
			for (int i = 0; i < plr.ignores.length; i++) {
				if (plr.ignores[i] > 0) {
					characterfile.write("character-ignore = ", 0, 19);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("    ", 0, 1);
					characterfile.write(Long.toString(plr.ignores[i]), 0, Long
							.toString(plr.ignores[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			characterfile.write("[LOOK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < plr.playerLook.length; i++) {
				characterfile.write("character-look = ", 0, 17);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("    ", 0, 1);
				characterfile.write(Integer.toString(plr.playerLook[i]), 0,
						Integer.toString(plr.playerLook[i]).length());
				characterfile.newLine();
			}
			characterfile.write("character-head = ", 0, 17);
			characterfile.write(Integer.toString(plr.pHead), 0, Integer
					.toString(plr.pHead).length());
			characterfile.newLine();
			characterfile.write("character-torso = ", 0, 18);
			characterfile.write(Integer.toString(plr.pTorso), 0, Integer
					.toString(plr.pTorso).length());
			characterfile.newLine();
			characterfile.write("character-arms = ", 0, 17);
			characterfile.write(Integer.toString(plr.pArms), 0, Integer
					.toString(plr.pArms).length());
			characterfile.newLine();
			characterfile.write("character-hands = ", 0, 18);
			characterfile.write(Integer.toString(plr.pHands), 0, Integer
					.toString(plr.pHands).length());
			characterfile.newLine();
			characterfile.write("character-legs = ", 0, 17);
			characterfile.write(Integer.toString(plr.pLegs), 0, Integer
					.toString(plr.pLegs).length());
			characterfile.newLine();
			characterfile.write("character-feet = ", 0, 17);
			characterfile.write(Integer.toString(plr.pFeet), 0, Integer
					.toString(plr.pFeet).length());
			characterfile.newLine();
			characterfile.write("character-beard = ", 0, 18);
			characterfile.write(Integer.toString(plr.pBeard), 0, Integer
					.toString(plr.pBeard).length());
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("[EOF]", 0, 5);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.flush();
			characterfile.close();
		} catch (IOException ioexception) {
			Misc.println(plr.playerName + ": error writing file.");
			return false;
		}
		return true;
	}

	public static final int maxPlayers = 600;
	// public static List<Player> players = new ArrayList<Player>();
	public static Player players[] = new Player[maxPlayers];
	public static Client clients[] = new Client[maxPlayers];
	public String messageToAll = "";
	public int playerCount = 0;
	public String playersCurrentlyOn[] = new String[maxPlayers];
	public boolean serverUpdater = false;
	public int playerRedSkull = 0;
	public int rockFallDelay = 24;
	public int maxClans = 1000;
	public int maxClanSlots = 99;
	public int[] clanIds = new int[maxClans];
	public String[] clanName = new String[maxClans];
	public String[][] clanPlayerName = new String[maxClans][maxClanSlots];
	public int[][] clanPlayerRights = new int[maxClans][maxClanSlots];
}
