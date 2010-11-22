package src.innovationx.classic.model.npc;

import java.io.*;

import src.innovationx.classic.Server;
import src.innovationx.classic.content.minigames.BossManager;
import src.innovationx.classic.content.minigames.ZombieMinigame;
import src.innovationx.classic.model.items.Item;
import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.util.Misc;

public class NPCHandler {

    public int maxNPCs = 5000;
    public int maxListedNPCs = 300;
    public NPC npcs[] = new NPC[maxNPCs];
    public int[] NpcType = new int[maxListedNPCs];
    public String[] Name = new String[maxListedNPCs];
    public int[] MaxHp = new int[maxListedNPCs];
    public int[] MaxHit = new int[maxListedNPCs];
    public int[] AtkType = new int[maxListedNPCs];
    public int[] DefLvl = new int[maxListedNPCs];
    public int[] DefType = new int[maxListedNPCs];
    public int[] size = new int[maxListedNPCs];
    public int[] level = new int[maxListedNPCs];

    public NPCHandler() {
        for (int i = 0; i < maxNPCs; i++) {
            npcs[i] = null;
        }
        loadNPCList("Npc.cfg");
        loadAutoSpawn("Autospawn.cfg");
    }

    public void newNPCList(int npcType, String name, int combat, int maxHp, int maxHit, int atkType, int defLvl, int defType, int _size) {
        int ThisSlot = -1;
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcType[i] == 0) {
                ThisSlot = i;
                break;
            }
        }
        if (ThisSlot == -1) {
            return;
        }
        NpcType[ThisSlot] = npcType;
        Name[ThisSlot] = name;
        MaxHp[ThisSlot] = maxHp;
        MaxHit[ThisSlot] = maxHit;
        AtkType[ThisSlot] = atkType;
        DefLvl[ThisSlot] = defLvl;
        DefType[ThisSlot] = defType;
        size[ThisSlot] = _size;
        level[ThisSlot] = combat;
    }

    public void destruct() {
        for (int i = 0; i < maxNPCs; i++) {
            if (npcs[i] != null) {
                npcs[i] = null;
            }
        }
    }

    public int newNPC(int NpcType, int absX, int absY, int heightLevel, int rangeX1, int rangeY1, int rangeX2, int rangeY2, boolean bol, int SpawnedFor) {
        int ThisSlot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                ThisSlot = i;
                break;
            }
        }
        if (ThisSlot == -1) {
            System.out.println("[NPC HANDLER]: Unable to spawn Id: " + NpcType);
            return -1;
        }
        int MaxHp = GetNpcListHp(NpcType);
        int DefLvl = GetNpcListDefLvl(NpcType);
        int DefType = GetNpcListDefType(NpcType);
        int MaxHit = GetNpcListMaxHit(NpcType);
        int AtkType = GetNpcListAtkType(NpcType);
        int _size = GetNpcListSize(NpcType);
        NPC newNPC = new NPC(ThisSlot, NpcType);
        newNPC.MaxHp = MaxHp;
        newNPC.absX = absX;
        newNPC.absY = absY;
        newNPC.makeX = absX;
        newNPC.makeY = absY;
        newNPC.moverangeX1 = rangeX1;
        newNPC.moverangeY1 = rangeY1;
        newNPC.moverangeX2 = rangeX2;
        newNPC.moverangeY2 = rangeY2;
        newNPC.CurrentHp = MaxHp;
        newNPC.DefLvl = DefLvl;
        newNPC.DefType = DefType;
        newNPC.MaxHit = MaxHit;
        newNPC.AtkType = AtkType;
        newNPC.heightLevel = heightLevel;
        newNPC.NeedsRespawn = bol;
        newNPC.SpawnedFor = SpawnedFor;
        newNPC.size = _size;
        newNPC.combatLevel = GetNpcListLevel(NpcType);
        if (newNPC.NpcType == 1160) {
            newNPC.playAnimation(1181);
        }
        if (SpawnedFor != -1) {
            try {
                Client c = (Client) Server.s.playerHandler.players[SpawnedFor];
                c.barrowBroSlot = ThisSlot;
                if (NpcType < 2627 || NpcType > 2745) {
                    c.drawHeadicon(1, ThisSlot, 0, 0);
                }
                newNPC.RandomWalk = false;
                newNPC.IsUnderAttack = true;
                newNPC.StartKilling = SpawnedFor;
            } catch (Exception e) {
            }
        }
        npcs[ThisSlot] = newNPC;
        return ThisSlot;
    }

    public void appendDroppedItem(String itemName, String playerName, String npcName) {
        /*BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("./Logs/NpcDrops.txt", true));
            bw.write(playerName + ": obtained item " + itemName + " from " + npcName);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException ioe) {
            System.out.println("[NPC HANDLER]: Critical error while writing players IP!");
        }*/
    }

    public void kalphCreate(int NpcId) {
        newNPC(1160, npcs[NpcId].absX, npcs[NpcId].absY, npcs[NpcId].heightLevel, npcs[NpcId].moverangeX1, npcs[NpcId].moverangeY1, npcs[NpcId].moverangeX2, npcs[NpcId].moverangeY2, true, -1);
        int kalph1Slot = 0;
        int kalph2Slot = 0;
        for (int n = 0; n < maxNPCs; n++) {
            if (npcs[n] != null) {
                if (npcs[n].NpcType == 1158) {
                    kalph1Slot = n;
                    break;
                }
            }
        }
        for (int d = 0; d < maxNPCs; d++) {
            if (npcs[d] != null) {
                if (npcs[d].NpcType == 1160) {
                    kalph2Slot = d;
                    break;
                }
            }
        }
        npcs[kalph2Slot].KilledBy = npcs[kalph1Slot].KilledBy;
    }

    public void DropItem(int NpcId) {
        int whoGets = GetNpcKiller(NpcId);
        int barrows1 = NpcDrops.RandomBarrows();
        int barrows2 = NpcDrops.RandomBarrows2();
        int itemDropped = 0;
        int itemAmt = 1;
        try {
            if (npcs[NpcId].NpcType == 3777) {
                Server.s.pc.portalAlive[0] = false;
                Server.s.pc.checkStatus();
            }
            if (npcs[NpcId].NpcType == 3778) {
                Server.s.pc.portalAlive[1] = false;
                Server.s.pc.checkStatus();
            }
            if (npcs[NpcId].NpcType == 3779) {
                Server.s.pc.portalAlive[2] = false;
                Server.s.pc.checkStatus();
            }
            if (npcs[NpcId].NpcType == 3780) {
                Server.s.pc.portalAlive[3] = false;
                Server.s.pc.checkStatus();
            }
            if (npcs[NpcId].NpcType == 3782) {
                Server.s.pc.gameLost();
            }
            if (npcs[NpcId].NpcType >= 3740 && npcs[NpcId].NpcType <= 3785) {
                Server.s.pc.removeFromList(npcs[NpcId].NpcSlot);
                return;
            }
            if (Server.s.playerHandler.players[whoGets] != null) {
                Client p = (Client) Server.s.playerHandler.players[whoGets];
                if (Misc.random(40) == 25 && npcs[NpcId].MaxHp > 40 && !p.IsInFightCave()) {
                    if (!p.hasClue()) {
                        int clueSlot = 0, clue = 0;
                        if (npcs[NpcId].MaxHp >= 100) {
                            clueSlot = Misc.random3(p.newClue3.length);
                            clue = p.newClue3[clueSlot];
                        } else if (npcs[NpcId].MaxHp >= 50) {
                            clueSlot = Misc.random3(p.newClue2.length);
                            clue = p.newClue2[clueSlot];
                        } else if (npcs[NpcId].MaxHp >= 10) {
                            clueSlot = Misc.random3(p.newClue1.length);
                            clue = p.newClue1[clueSlot];
                        }
                        if (clue > 0) {
                            Server.s.itemHandler.NpcDropItem(clue, 0, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                    }
                }
                if (npcs[NpcId].SpawnedFor != -1) {
                    p.drawHeadicon(1, 0, 0, 0);
                }
                p.slayerNPCKilled(npcs[NpcId].NpcType, npcs[NpcId].MaxHp);
				for(int ids : BossManager.bosses) {
					if(npcs[NpcId].NpcType == ids) {
						int chance = Misc.random(2000);
						if(p.isExtremeMember == 1) {
							chance = Misc.random(500);
						}
						else if (p.isMember == 1) {
							chance = Misc.random(1000);
						}
						if(chance == 500) {
							Server.s.itemHandler.NpcDropItem(962, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
						}
					}
				}
                switch (npcs[NpcId].NpcType) {
                    case 997:
                        newNPC(998, npcs[NpcId].absX, npcs[NpcId].absY, p.heightLevel, 0, 0, 0, 0, false, whoGets);
                        break;
                    case 998:
                        newNPC(999, npcs[NpcId].absX, npcs[NpcId].absY, p.heightLevel, 0, 0, 0, 0, false, whoGets);
                        break;
                    case 999:
                        newNPC(1000, npcs[NpcId].absX, npcs[NpcId].absY, p.heightLevel, 0, 0, 0, 0, false, whoGets);
                        break;
                    case 1000:
                        if (p.undergroundPassQuest >= 2) {
                            if (p.undergroundPassQuest == 2) {
                                p.undergroundPassQuestComplete();
                            }
                            Server.s.itemHandler.NpcDropItem(1409, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        break;
                    case 159:
                        if (p.meleeFightStatus == -1) {
                            Server.s.itemHandler.NpcDropItem(7774, 10, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1155, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1117, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1075, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        break;
                    case 160:
                        if (p.meleeFightStatus == -1) {
                            Server.s.itemHandler.NpcDropItem(7774, 11, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1153, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1115, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1067, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        break;
                    case 151:
                        if (p.meleeFightStatus == -1) {
                            Server.s.itemHandler.NpcDropItem(7774, 12, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1157, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1119, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1069, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        break;
                    case 152:
                        if (p.meleeFightStatus == -1) {
                            Server.s.itemHandler.NpcDropItem(7774, 13, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1165, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1125, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1077, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        break;
                    case 153:
                        if (p.meleeFightStatus == -1) {
                            Server.s.itemHandler.NpcDropItem(7774, 14, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1159, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1121, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1071, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        break;
                    case 154:
                        if (p.meleeFightStatus == -1) {
                            Server.s.itemHandler.NpcDropItem(7774, 15, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1161, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1123, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1073, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        break;
                    case 155:
                        if (p.meleeFightStatus == -1) {
                            Server.s.itemHandler.NpcDropItem(7774, 16, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1163, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1127, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(1079, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        break;
                    case 110:
                        itemDropped = NpcDrops.RandomMossGiant();
                        if (Item.itemStackable[itemDropped] || Item.itemIsNote[itemDropped]) {
                            itemAmt = 40;
                        }
                        Server.s.itemHandler.NpcDropItem(itemDropped, itemAmt, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(532, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 112:
                        itemDropped = NpcDrops.RandomFireGiant();
                        if (Item.itemStackable[itemDropped] || Item.itemIsNote[itemDropped]) {
                            itemAmt = 40;
                        }
                        Server.s.itemHandler.NpcDropItem(itemDropped, itemAmt, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(532, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 907:
                    case 908:
                    case 909:
                    case 910:
                    case 911:
                        p.mageArenaQuest++;
                        p.startMageArena();
                        break;
                    case 1264:
                        if (p.IsInFightArena()) {
                            p.FightArenaKills += 3;
                        }
                        break;
                    case 2627:
                    case 2629:
                    case 2631:
                    case 2742:
                    case 2744:
                        if (p.IsInFightCave()) {
                            p.KilledTz++;
                            if (p.KilledTz == p.NeededKills) {
                                p.tzWave++;
                                p.WaveDelay = 20;
                            }
                        }
                        break;
                    case 2745:
                        if (p.IsInFightCave()) {
                            p.KilledJad();
                        }
                        break;
                    case 124:
                        if (p.IsInFightArena()) {
                            p.FightArenaKills += 1;
                        }
                        break;
                    case 912:
                    case 913:
                    case 914:
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        p.mageArenaKills += 6;
                        break;
                    case 2025:
                        p.ahrimSummoned = 2;
                        if (p.ahrimSummoned == 2 && p.dharokSummoned == 2 && p.guthanSummoned == 2 && p.karilSummoned == 2 && p.toragSummoned == 2 && p.veracSummoned == 2) {
                            p.resetBarrows();
                            p.sendMessage("Congratulations, you have completed the barrows minigame!");
                            Server.s.itemHandler.NpcDropItem(barrows1, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(barrows2, 100, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            itemDropped = barrows1;
                        }
                        break;
                    case 2026:
                        p.dharokSummoned = 2;
                        if (p.ahrimSummoned == 2 && p.dharokSummoned == 2 && p.guthanSummoned == 2 && p.karilSummoned == 2 && p.toragSummoned == 2 && p.veracSummoned == 2) {
                            p.resetBarrows();
                            p.sendMessage("Congratulations, you have completed the barrows minigame!");
                            Server.s.itemHandler.NpcDropItem(barrows1, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(barrows2, 100, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            itemDropped = barrows1;
                        }
                        break;
                    case 2027:
                        p.guthanSummoned = 2;
                        if (p.ahrimSummoned == 2 && p.dharokSummoned == 2 && p.guthanSummoned == 2 && p.karilSummoned == 2 && p.toragSummoned == 2 && p.veracSummoned == 2) {
                            p.resetBarrows();
                            p.sendMessage("Congratulations, you have completed the barrows minigame!");
                            Server.s.itemHandler.NpcDropItem(barrows1, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(barrows2, 100, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            itemDropped = barrows1;
                        }
                        break;
                    case 2028:
                        p.karilSummoned = 2;
                        if (p.ahrimSummoned == 2 && p.dharokSummoned == 2 && p.guthanSummoned == 2 && p.karilSummoned == 2 && p.toragSummoned == 2 && p.veracSummoned == 2) {
                            p.resetBarrows();
                            p.sendMessage("Congratulations, you have completed the barrows minigame!");
                            Server.s.itemHandler.NpcDropItem(barrows1, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(barrows2, 100, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            itemDropped = barrows1;
                        }
                        break;
                    case 2029:
                        p.toragSummoned = 2;
                        if (p.ahrimSummoned == 2 && p.dharokSummoned == 2 && p.guthanSummoned == 2 && p.karilSummoned == 2 && p.toragSummoned == 2 && p.veracSummoned == 2) {
                            p.resetBarrows();
                            p.sendMessage("Congratulations, you have completed the barrows minigame!");
                            Server.s.itemHandler.NpcDropItem(barrows1, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(barrows2, 100, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            itemDropped = barrows1;
                        }
                        break;
                    case 2030:
                        p.veracSummoned = 2;
                        if (p.ahrimSummoned == 2 && p.dharokSummoned == 2 && p.guthanSummoned == 2 && p.karilSummoned == 2 && p.toragSummoned == 2 && p.veracSummoned == 2) {
                            p.resetBarrows();
                            p.sendMessage("Congratulations, you have completed the barrows minigame!");
                            Server.s.itemHandler.NpcDropItem(barrows1, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(barrows2, 100, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            itemDropped = barrows1;
                        }
                        break;
                    case 2598:
                    case 2607:
                    case 2595:
                        itemDropped = NpcDrops.RandomTzhaar();
                        if (itemDropped == 6522) {
                            Server.s.itemHandler.NpcDropItem(itemDropped, 25, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        } else {
                            Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        Server.s.itemHandler.NpcDropItem(592, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 2616:
                        itemDropped = NpcDrops.RandomTzhaar2();
                        if (itemDropped == 6522) {
                            Server.s.itemHandler.NpcDropItem(itemDropped, 25, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        } else {
                            Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        Server.s.itemHandler.NpcDropItem(592, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1183:
                        itemDropped = NpcDrops.RandomElf();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1200:
                        itemDropped = NpcDrops.RandomTyra();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
					case 420:
                        itemDropped = NpcDrops.RandomZombie();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
					case 421:
                        itemDropped = NpcDrops.RandomZombie1();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
					case 422:
                        itemDropped = NpcDrops.RandomZombie2();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
					case 423:
                        itemDropped = NpcDrops.RandomZombie3();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
					case 424:
                        itemDropped = NpcDrops.RandomZombie4();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1610:
                    case 1611:
                        itemDropped = NpcDrops.RandomGargoyle();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 87:
                    case 88:
                        itemDropped = NpcDrops.RandomRat();
                        if (itemDropped == 995) {
                            Server.s.itemHandler.NpcDropItem(itemDropped, 1000, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        } else {
                            Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 2440:
                    case 2443:
                    case 2446:
                        Server.s.worldO.checkIfExists(8967, npcs[NpcId].absX, npcs[NpcId].absY, 10, 0);
                        Server.s.worldO.lowerHealth(1, 8967, npcs[NpcId].absX, npcs[NpcId].absY);
                        break;
                    case 1615:
                        itemDropped = NpcDrops.RandomAbyssalDemon();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(592, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1153:
                    case 1154:
                    case 1155:
                        itemDropped = NpcDrops.RandomKalphiteWorker();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 2452:
                        itemDropped = NpcDrops.RandomGiantRockCrab();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 2457:
                        itemDropped = NpcDrops.RandomWallasalki();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 126:
                        itemDropped = NpcDrops.RandomOtherworldlyBeing();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 100:
                        itemDropped = NpcDrops.RandomGoblin();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 116:
                        if (p.defenderTask == 0) {
                            break;
                        }
                        itemDropped = p.RandomCyclops();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(532, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 414:
                        itemDropped = NpcDrops.RandomRockGolem();
                        if (itemDropped != 837 && itemDropped != 859 && itemDropped != 861 && itemDropped != 7842 || itemDropped == 7841) {
                            itemAmt = 20;
                        }
                        if (itemDropped == 7842) {
                            itemAmt = 10;
                        }
                        Server.s.itemHandler.NpcDropItem(itemDropped, itemAmt, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 2263:
                        itemDropped = NpcDrops.RandomAbyssalLeach();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 18:
                        itemDropped = NpcDrops.RandomAlkaridWarrior();
                        if (itemDropped == 250) {
                            itemAmt = 8;
                        } else if (itemDropped == 252) {
                            itemAmt = 7;
                        } else if (itemDropped == 254) {
                            itemAmt = 6;
                        } else if (itemDropped == 256) {
                            itemAmt = 5;
                        } else if (itemDropped == 258) {
                            itemAmt = 4;
                        } else if (itemDropped == 260 || itemDropped == 3001) {
                            itemAmt = 3;
                        } else if (itemDropped == 262 || itemDropped == 264) {
                            itemAmt = 2;
                        }
                        Server.s.itemHandler.NpcDropItem(itemDropped, itemAmt, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 172:
                        itemDropped = NpcDrops.RandomDarkWizard();
                        if (itemDropped >= 554 && itemDropped <= 565) {
                            Server.s.itemHandler.NpcDropItem(itemDropped, Misc.random(10), npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        } else {
                            Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 256:
                        Server.s.itemHandler.NpcDropItem(75, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(74, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1:
                    case 4:
                    case 7:
                    case 15:
                    case 187:
                    case 20:
                    case 49:
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1160:
                        itemDropped = NpcDrops.RandomKalphiteQueen();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 2783:
                        itemDropped = NpcDrops.RandomDarkBeast();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 19:
                        itemDropped = NpcDrops.RandomWhiteKnight();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1590:
                        itemDropped = NpcDrops.RandomBronzeDragon();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(536, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1592:
                        itemDropped = NpcDrops.RandomSteelDragon();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(536, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1613:
                        itemDropped = NpcDrops.RandomNechryael();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(532, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 55:
                        itemDropped = NpcDrops.RandomBlueDragon();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(536, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(1751, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 53:
                        itemDropped = NpcDrops.RandomBlueDragon();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(536, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(1749, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 941:
                        itemDropped = NpcDrops.RandomGreenDrag();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(536, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(1753, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 54:
                        itemDropped = NpcDrops.RandomBlackDragon();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(536, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(1747, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1472:
                        itemDropped = NpcDrops.RandomJungleDemon();
                        if (p.monkeyMadnessQuest == 8) {
                            p.monkeyMadnessQuest = 9;
                            p.changeText126("You have completed the Rune Monkey Madness Quest!", 301);
                            p.showInterface(297);
                            Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(592, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            Server.s.itemHandler.NpcDropItem(8071, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                            p.loadQuestTab();
                        }
                        break;
                    case 1342:
                        itemDropped = NpcDrops.RandomDagannoth();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(532, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 2883:
                        itemDropped = NpcDrops.RandomDagannothRex();
                        Server.s.itemHandler.NpcDropItem(NpcDrops.RandomDagannothRex(), 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(6729, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 2882:
                        itemDropped = NpcDrops.RandomDagannothPrime();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(6729, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 2881:
                        itemDropped = NpcDrops.RandomDagannothSupreme();
                        if (Item.itemStackable[itemDropped] || Item.itemIsNote[itemDropped]) {
                            itemAmt = 40;
                        }
                        Server.s.itemHandler.NpcDropItem(itemDropped, itemAmt, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(6729, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 93:
                        itemDropped = NpcDrops.RandomSkeleton93();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(532, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 50:
                        itemDropped = NpcDrops.RandomKBD();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(536, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 73:
                    case 74:
                    case 75:
                    case 76:
                    case 77:
					case 78:
                    	p.sendMessage("You killed a zombie!");
                    	p.zombieKills++;
                    	ZombieMinigame.ZOMBIES--;
                    	break;
					case 2058:
						p.zombieKills++;
						ZombieMinigame.createNPC(73, npcs[NpcId].absX, npcs[NpcId].absY);
						ZombieMinigame.createNPC(73, npcs[NpcId].absX + 1, npcs[NpcId].absY);
						ZombieMinigame.ZOMBIES--;
						p.sendMessage("The zombie veteran splits!");
						break;
                    case 2843:
                    	p.zombieKills++;
						ZombieMinigame.createNPC(2058, npcs[NpcId].absX, npcs[NpcId].absY + 1);
						ZombieMinigame.createNPC(2058, npcs[NpcId].absX + 1, npcs[NpcId].absY);
						ZombieMinigame.createNPC(2058, npcs[NpcId].absX, npcs[NpcId].absY - 1);
						ZombieMinigame.ZOMBIES--;
                    	p.sendMessage("You killed the zombie commander!");
						p.sendMessage("The zombie commander splits!");
                    	break;
																case 3200: //chaos elemental
					int randomAGSz = Misc.random(30);
					int randomBGSz = Misc.random(20);
					int randomSGSz = Misc.random(20);
					int randomZGSz = Misc.random(20);
					if (randomAGSz == 1) {
						Server.s.itemHandler.NpcDropItem(8086, 1, 2810, 3357, whoGets);
					}
					else if (randomBGSz == 1) {
						Server.s.itemHandler.NpcDropItem(7993, 1, 2810, 3357, whoGets);
					}
					else if (randomSGSz == 1) {
						Server.s.itemHandler.NpcDropItem(8040, 1, 2810, 3357, whoGets);
					}
					else if (randomZGSz == 1) {
						Server.s.itemHandler.NpcDropItem(8087, 1, 2810, 3357, whoGets);
					}
					break;
										case 3847: //stq
					int randomAGS = Misc.random(30);
					int randomBGS = Misc.random(20);
					int randomSGS = Misc.random(20);
					int randomZGS = Misc.random(20);
					if (randomAGS == 1) {
						Server.s.itemHandler.NpcDropItem(8086, 1, 2810, 3357, whoGets);
					}
					else if (randomBGS == 1) {
						Server.s.itemHandler.NpcDropItem(7993, 1, 2810, 3357, whoGets);
					}
					else if (randomSGS == 1) {
						Server.s.itemHandler.NpcDropItem(8040, 1, 2810, 3357, whoGets);
					}
					else if (randomZGS == 1) {
						Server.s.itemHandler.NpcDropItem(8087, 1, 2810, 3357, whoGets);
					}
					break;
                    case 1973:
                    case 2037:
                        itemDropped = NpcDrops.RandomGiantSkeleton();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(532, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 117:
                        itemDropped = NpcDrops.RandomHillGiant();
                        if (itemDropped == 250) {
                            itemAmt = 8;
                        } else if (itemDropped == 252) {
                            itemAmt = 7;
                        } else if (itemDropped == 254) {
                            itemAmt = 6;
                        } else if (itemDropped == 256) {
                            itemAmt = 5;
                        } else if (itemDropped == 258) {
                            itemAmt = 4;
                        } else if (itemDropped == 260 || itemDropped == 3001) {
                            itemAmt = 3;
                        } else if (itemDropped == 262 || itemDropped == 264) {
                            itemAmt = 2;
                        }
                        Server.s.itemHandler.NpcDropItem(itemDropped, itemAmt, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(532, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 181:
                        itemDropped = NpcDrops.RandomChaosDruid();
                        if (itemDropped == 250) {
                            itemAmt = 8;
                        } else if (itemDropped == 252) {
                            itemAmt = 7;
                        } else if (itemDropped == 254) {
                            itemAmt = 6;
                        } else if (itemDropped == 256) {
                            itemAmt = 5;
                        } else if (itemDropped == 258) {
                            itemAmt = 4;
                        } else if (itemDropped == 260 || itemDropped == 3001) {
                            itemAmt = 3;
                        } else if (itemDropped == 262 || itemDropped == 264) {
                            itemAmt = 2;
                        }
                        Server.s.itemHandler.NpcDropItem(itemDropped, itemAmt, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 89:
                        Server.s.itemHandler.NpcDropItem(235, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 84:
                        itemDropped = NpcDrops.RandomBlackDemon();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(592, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 82:
                        itemDropped = NpcDrops.RandomLesserDemon();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(592, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1607:
                        itemDropped = NpcDrops.RandomAberrantSpecter();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1355:
                        itemDropped = NpcDrops.RandomDagMother();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 12:
                    case 17:
                        itemDropped = NpcDrops.RandomBarbarian();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 3070:
                        itemDropped = NpcDrops.RandomSkeletalWyvern();
                        if (Item.itemStackable[itemDropped] || Item.itemIsNote[itemDropped]) {
                            itemAmt = 40;
                        }
                        Server.s.itemHandler.NpcDropItem(itemDropped, itemAmt, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1643:
                        itemDropped = NpcDrops.RandomInfernalMage();
                        if (Item.itemStackable[itemDropped] || Item.itemIsNote[itemDropped]) {
                            itemAmt = 40;
                        }
                        Server.s.itemHandler.NpcDropItem(itemDropped, itemAmt, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 188:
                        itemDropped = NpcDrops.RandomZamorakMonk();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 127:
                        Server.s.itemHandler.NpcDropItem(1363, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 41:
                        Server.s.itemHandler.NpcDropItem(314, 35, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 81:
                        Server.s.itemHandler.NpcDropItem(1739, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 9:
                        itemDropped = NpcDrops.RandomGuard();
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 114:
                        Server.s.itemHandler.NpcDropItem(1482, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1913:
                        itemDropped = NpcDrops.Randomkamil();
                        if (p.ancQuest == 0) {
                            p.sendMessage("You advance a stage in unlocking Ancient Magicks!");
                            p.ancQuest = 1;
                            p.loadQuestTab();
                        }
                        if (p.ancQuest != 4) {
                            Server.s.itemHandler.NpcDropItem(6950, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        Server.s.itemHandler.NpcDropItem(itemDropped, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1914:
                        newNPC(1915, p.absX + 4, p.absY + 4, p.heightLevel, 0, 0, 0, 0, false, whoGets);
                        break;
                    case 239:
                    case 240:
                    case 241:
                    case 242:
                    case 243:
                    case 244:
                    case 245:
                        newNPC(npcs[NpcId].NpcType + 1, 2187, 4954, p.heightLevel, 0, 0, 0, 0, false, whoGets);
                        break;
                    case 246:
                        p.knightWavesMenu();
                        break;
                    case 1915:
                        if (p.ancQuest == 1) {
                            p.sendMessage("You advance a stage in unlocking Ancient Magicks!");
                            p.ancQuest = 2;
                            p.loadQuestTab();
                        }
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1977:
                        if (p.ancQuest == 2) {
                            p.sendMessage("You advance a stage in unlocking Ancient Magicks!");
                            p.ancQuest = 3;
                            p.loadQuestTab();
                        }
                        p.hitDiff = 20;
                        p.updateHp(20, false);
                        p.updateRequired = true;
                        p.hitUpdateRequired = true;
                        p.playGraphic(346, 0, 100);
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 1974:
                        newNPC(1975, 2579, 4461, p.heightLevel, 0, 0, 0, 0, false, whoGets);
                        break;
                    case 2566:
                        p.lunarQuestComplete();
                        break;
                    case 1975:
                        if (p.ancQuest == 3) {
                            p.sendMessage("You can now switch to Ancient Magicks!");
                            p.ancQuest = 4;
                            p.addItem(4675, 1);
                            p.loadQuestTab();
                        }
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 105:
                        Server.s.itemHandler.NpcDropItem(526, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                    case 677:
                        if (p.legendQuest == 1) {
                            p.legendQuest = 2;
                            p.loadQuestTab();
                        }
                        break;
                    case 103:
                        break;
                    case 655:
                        if (p.playerId == npcs[NpcId].SpawnedFor) {
                            if (p.lostCityQuest == 3) {
                                p.lostCityQuest = 4;
                            }
                            Server.s.itemHandler.NpcDropItem(771, 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        }
                        break;
                    default:
                        System.out.println("[NPC HANDLER]: No Drop Set For NpcId: " + npcs[NpcId].NpcType);
						Server.s.itemHandler.NpcDropItem(526 , 1, npcs[NpcId].absX, npcs[NpcId].absY, whoGets);
                        break;
                }
                if (itemDropped > 0) {
                    appendDroppedItem(p.getItemName(itemDropped), p.playerName, p.getNpcName(npcs[NpcId].NpcType));
                }
            }
        } catch (Exception e) {
        }
    }

    public int GetNpcListHp(int npcType) {
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcType[i] == npcType) {
                return MaxHp[i];
            }
        }
        return 0;
    }

    public int GetNpcListLevel(int npcType) {
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcType[i] == npcType) {
                return level[i];
            }
        }
        return 0;
    }

    public int GetNpcListDefLvl(int npcType) {
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcType[i] == npcType) {
                return DefLvl[i];
            }
        }
        return 0;
    }

    public int GetNpcListDefType(int npcType) {
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcType[i] == npcType) {
                return DefType[i];
            }
        }
        return 0;
    }

    public int GetNpcListMaxHit(int npcType) {
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcType[i] == npcType) {
                return MaxHit[i];
            }
        }
        return 0;
    }

    public int GetNpcListAtkType(int npcType) {
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcType[i] == npcType) {
                return AtkType[i];
            }
        }
        return 0;
    }

    public int GetNpcListSize(int npcType) {
        for (int i = 0; i < maxListedNPCs; i++) {
            if (NpcType[i] == npcType) {
                return size[i];
            }
        }
        return 0;
    }

    public boolean IsInRange(int NPCID, int MoveX, int MoveY) {
        int NewMoveX = (npcs[NPCID].absX + MoveX);
        int NewMoveY = (npcs[NPCID].absY + MoveY);
        if (NewMoveX <= npcs[NPCID].moverangeX1 && NewMoveX >= npcs[NPCID].moverangeX2 && NewMoveY <= npcs[NPCID].moverangeY1 && NewMoveY >= npcs[NPCID].moverangeY2) {
            return true;
        } else {
            return false;
        }
    }

    public void process() {
        for (int i = 0; i < maxNPCs; i++) {
            if (npcs[i] != null) {
                npcs[i].clearUpdateFlags();
            }
        }
        for (int i = 1; i < maxNPCs; i++) {
            try {
                if (npcs[i] != null) {
                    npcs[i].ActionTimer--;
                    npcs[i].process();
                    if (!npcs[i].IsDead) {
                        if (npcs[i].RandomWalk && (npcs[i].absX > npcs[i].moverangeX1 || npcs[i].absX < npcs[i].moverangeX2 || npcs[i].absY > npcs[i].moverangeY1 || npcs[i].absY < npcs[i].moverangeY2)) {
                            if (npcs[i].NpcType != 1459) {
                                npcs[i].MoveBackToOrigin();
                                npcs[i].getNextNPCMovement();
                            }
                        } else if (npcs[i].RandomWalk && Misc.random2(5) == 1 && npcs[i].moverangeX1 > 0 && npcs[i].moverangeY1 > 0 && npcs[i].moverangeX2 > 0 && npcs[i].moverangeY2 > 0) {
                            int MoveX = Misc.random(1);
                            int MoveY = Misc.random(1);
                            int Rnd = Misc.random2(4);
                            if (Rnd == 1) {
                                MoveX = -(MoveX);
                                MoveY = -(MoveY);
                            } else if (Rnd == 2) {
                                MoveX = -(MoveX);
                            } else if (Rnd == 3) {
                                MoveY = -(MoveY);
                            }
                            if (IsInRange(i, MoveX, MoveY)) {
                                npcs[i].moveX = MoveX;
                                npcs[i].moveY = MoveY;
                                npcs[i].getNextNPCMovement();
                            }
                        }
                        if (!npcs[i].RandomWalk && npcs[i].IsUnderAttack) {
                            npcs[i].AttackPlayer();
                        } else if (!npcs[i].RandomWalk && npcs[i].KillingNpc) {
                            npcs[i].AttackNpc();
                        }
                        int dis = 5;
                        switch (npcs[i].NpcType) {
                            case 1158:
                                dis = 16;
                            case 1160:
                                dis = 16;
                            case 2892:
                                dis = 8;
                            case 1465:
                                dis = 8;
                            case 1455:
                                dis = 8;
                            case 1456:
                                dis = 8;
                            case 1460:
                                dis = 8;
                            case 1461:
                                dis = 8;
                            case 1457:
                                dis = 6;
                            case 2457:
                            case 2452:
                            case 1153:
                            case 2263:
                            case 3776:
                            case 3761:
                            case 3771:
                            case 50:
                            case 1342:
                            case 2037:
                            case 127:
                            case 84:
                            case 55:
                            case 54:
                            case 49:
                            case 82:
                            case 1614:
                            case 1607:
							case 3200:
							case 3847:
                            case 2783:
                            case 2883:
                            case 2882:
                            case 2881:
                            case 941:
                            case 1154:
                            case 1155:
                                if (!npcs[i].IsUnderAttack) {
                                    for (int j = 0; j < Server.s.playerHandler.maxPlayers; j++) {
                                        Client p = Server.s.playerHandler.clients[j];
                                        if (p != null) {
                                            if (npcs[i].WithinDistance(p.absX, p.absY, npcs[i].absX, npcs[i].absY, dis) && p.heightLevel == npcs[i].heightLevel) {
                                                if (p.combatWith2 != npcs[i].NpcSlot && p.combatWith2 > 0 && !npcs[i].multiZone()) {
                                                    npcs[i].StartKilling = 0;
                                                    npcs[i].RandomWalk = true;
                                                    npcs[i].IsUnderAttack = false;
                                                    break;
                                                }
                                                if (p.npcId > 0 && (npcs[i].NpcType == 1455 || npcs[i].NpcType == 1456 || npcs[i].NpcType == 1457 || npcs[i].NpcType == 1460 || npcs[i].NpcType == 1465 || npcs[i].NpcType == 1461)) {
                                                    break;
                                                }
                                                npcs[i].StartKilling = j;
                                                npcs[i].RandomWalk = false;
                                                npcs[i].IsUnderAttack = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                break;
							case 73:
							case 74:
							case 75:
							case 76:
							case 77:
							case 78:
							case 2058:
							case 2843:
							    if (!npcs[i].IsUnderAttack) {
                                    for (int j = 0; j < Server.s.playerHandler.maxPlayers; j++) {
                                        Client p = Server.s.playerHandler.clients[j];
                                        if (p != null) {
                                            if (npcs[i].WithinDistance(p.absX, p.absY, npcs[i].absX, npcs[i].absY, 15) && p.heightLevel == npcs[i].heightLevel) {
                                                npcs[i].StartKilling = j;
                                                npcs[i].RandomWalk = false;
                                                npcs[i].IsUnderAttack = true;
                                                break;
                                            }
                                        }
                                    }
                                }
								break;
                            default:
                                break;
                        }
                    } else if (npcs[i].IsDead) {
                        if (npcs[i].ActionTimer <= 0 && !npcs[i].DeadApply && !npcs[i].NeedRespawn) {
                            if (npcs[i].NpcType == 1353 || npcs[i].NpcType == 1354) {
                                npcs[i].CurrentHp = GetNpcListHp(npcs[i].NpcType);
                                npcs[i].IsDead = false;
                                npcs[i].NpcType++;
                                for (int j = 1; j < Server.s.playerHandler.maxPlayers; j++) {
                                    if (Server.s.playerHandler.players[j] != null) {
                                        Server.s.playerHandler.players[j].RebuildNPCList = true;
                                    }
                                }
                                continue;
                            } else if (npcs[i].NpcType == 1158) {
                                kalphCreate(i);
                            } else if (npcs[i].NpcType == 1610) {
                                npcs[i].NpcType++;
                                for (int j = 1; j < Server.s.playerHandler.maxPlayers; j++) {
                                    if (Server.s.playerHandler.players[j] != null) {
                                        Server.s.playerHandler.players[j].RebuildNPCList = true;
                                    }
                                }
                            }
                            npcs[i].playAnimation(npcs[i].GetDeathEmote());
                            npcs[i].DeadApply = true;
                            npcs[i].ActionTimer = 4;
                        } else if (npcs[i] != null && npcs[i].ActionTimer <= 0 && npcs[i].DeadApply && !npcs[i].NeedRespawn) {
                            if (npcs[i] != null) {
                                DropItem(i);
                                if (npcs[i] != null && npcs[i].NpcType != 1158) {
                                    if (!npcs[i].NeedsRespawn) {
                                        npcs[i] = null;
                                        for (int j = 1; j < Server.s.playerHandler.maxPlayers; j++) {
                                            if (Server.s.playerHandler.players[j] != null) {
                                                Server.s.playerHandler.players[j].RebuildNPCList = true;
                                            }
                                        }
                                        continue;
                                    }
                                    npcs[i].NeedRespawn = true;
                                    npcs[i].ActionTimer = 30;
                                    npcs[i].absX = npcs[i].makeX;
                                    npcs[i].absY = npcs[i].makeY;
                                } else if (npcs[i] != null && npcs[i].NpcType == 1158) {
                                    npcs[i] = null;
                                    for (int j = 1; j < Server.s.playerHandler.maxPlayers; j++) {
                                        if (Server.s.playerHandler.players[j] != null) {
                                            Server.s.playerHandler.players[j].RebuildNPCList = true;
                                        }
                                    }
                                }
                            }
                        } else if (npcs[i] != null) {
                            if (npcs[i].ActionTimer <= 0 && npcs[i].NeedRespawn && npcs[i].NeedsRespawn) {
                                for (int j = 1; j < Server.s.playerHandler.maxPlayers; j++) {
                                    if (Server.s.playerHandler.players[j] != null) {
                                        Server.s.playerHandler.players[j].RebuildNPCList = true;
                                    }
                                }
                                int type = npcs[i].NpcType;
                                if (type == 1160) {
                                    type = 1158;
                                }
                                if (type == 1355) {
                                    type = 1353;
                                }
                                if (type == 1611) {
                                    type = 1610;
                                }
                                int old1 = npcs[i].makeX;
                                int old2 = npcs[i].makeY;
                                int old3 = npcs[i].heightLevel;
                                int old4 = npcs[i].moverangeX1;
                                int old5 = npcs[i].moverangeY1;
                                int old6 = npcs[i].moverangeX2;
                                int old7 = npcs[i].moverangeY2;
                                npcs[i] = null;
                                newNPC(type, old1, old2, old3, old4, old5, old6, old7, true, -1);
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public int GetNpcKiller(int NPCID) {
        int Killer = 0;
        int Count = 0;
        for (int i = 1; i < Server.s.playerHandler.maxPlayers; i++) {
            if (Killer == 0) {
                Killer = i;
                Count = 1;
            } else {
                if (npcs[NPCID].KilledBy[i] > npcs[NPCID].KilledBy[Killer]) {
                    Killer = i;
                    Count = 1;
                } else if (npcs[NPCID].KilledBy[i] == npcs[NPCID].KilledBy[Killer]) {
                    Count++;
                }
            }
        }
        if (Count > 1) {
            Killer = npcs[NPCID].StartKilling;
        }
        return Killer;
    }

    public boolean loadAutoSpawn(String FileName) {
        String line = "";
        String token = "";
        String token2 = "";
        String token2_2 = "";
        String[] token3 = new String[10];
        boolean EndOfFile = false;
        int ReadMode = 0;
        BufferedReader characterfile = null;
        try {
            characterfile = new BufferedReader(new FileReader("./CFG/" + FileName));
        } catch (FileNotFoundException fileex) {
            Misc.println(FileName + ": file not found.");
            return false;
        }
        try {
            line = characterfile.readLine();
        } catch (IOException ioexception) {
            Misc.println(FileName + ": error loading file.");
            return false;
        }
        while (EndOfFile == false && line != null) {
            line = line.trim();
            int spot = line.indexOf("=");
            if (spot > -1) {
                token = line.substring(0, spot);
                token = token.trim();
                token2 = line.substring(spot + 1);
                token2 = token2.trim();
                token2_2 = token2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token3 = token2_2.split("\t");
                if (token.equals("Spawn")) {
                    newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]), Integer.parseInt(token3[3]), Integer.parseInt(token3[4]), Integer.parseInt(token3[5]), Integer.parseInt(token3[6]), Integer.parseInt(token3[7]), true, -1);
                }
            } else {
                if (line.equals("[ENDOFSPAWNLIST]")) {
                    try {
                        characterfile.close();
                    } catch (IOException ioexception) {
                    }
                    return true;
                }
            }
            try {
                line = characterfile.readLine();
            } catch (IOException ioexception1) {
                EndOfFile = true;
            }
        }
        try {
            characterfile.close();
        } catch (IOException ioexception) {
        }
        return false;
    }

    public boolean loadNPCList(String FileName) {
        String line = "";
        String token = "";
        String token2 = "";
        String token2_2 = "";
        String[] token3 = new String[10];
        boolean EndOfFile = false;
        int ReadMode = 0;
        BufferedReader characterfile = null;
        try {
            characterfile = new BufferedReader(new FileReader("./CFG/" + FileName));
        } catch (FileNotFoundException fileex) {
            Misc.println(FileName + ": file not found.");
            return false;
        }
        try {
            line = characterfile.readLine();
        } catch (IOException ioexception) {
            Misc.println(FileName + ": error loading file.");
            return false;
        }
        while (EndOfFile == false && line != null) {
            line = line.trim();
            int spot = line.indexOf("=");
            if (spot > -1) {
                token = line.substring(0, spot);
                token = token.trim();
                token2 = line.substring(spot + 1);
                token2 = token2.trim();
                token2_2 = token2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token3 = token2_2.split("\t");
                if (token.equals("NPC")) {
                    newNPCList(Integer.parseInt(token3[0]), token3[1], Integer.parseInt(token3[2]), Integer.parseInt(token3[3]), Integer.parseInt(token3[4]), Integer.parseInt(token3[5]), Integer.parseInt(token3[6]), Integer.parseInt(token3[7]), Integer.parseInt(token3[8]));
                }
            } else {
                if (line.equals("[ENDOFNPCLIST]")) {
                    try {
                        characterfile.close();
                    } catch (IOException ioexception) {
                    }
                    return true;
                }
            }
            try {
                line = characterfile.readLine();
            } catch (IOException ioexception1) {
                EndOfFile = true;
            }
        }
        try {
            characterfile.close();
        } catch (IOException ioexception) {
        }
        return false;
    }
}
