package src.innovationx.classic.model.player;

import java.io.*;
import java.util.*;

import src.innovationx.classic.Server;
import src.innovationx.classic.content.minigames.FightPits;
import src.innovationx.classic.content.minigames.ZombieMinigame;
import src.innovationx.classic.model.items.Item;
import src.innovationx.classic.model.npc.NPC;
import src.innovationx.classic.net.stream;
import src.innovationx.classic.util.Misc;

public abstract class Player {

	public boolean updateRunning = false;
	public int updateSeconds = 0;

    public Player(int _playerId) {
        playerId = _playerId;
        playerRights = 0;
        for (int i = 0; i < playerItems.length; i++) {
            playerItems[i] = 0;
        }
        for (int i = 0; i < playerItemsN.length; i++) {
            playerItemsN[i] = 0;
        }
        for (int i = 0; i < playerEquipment.length; i++) {
            playerEquipment[i] = -1;
        }
        for (int i = 0; i < KilledBy.length; i++) {
            KilledBy[i] = 0;
        }
        for (int i = 0; i < playerLevel.length; i++) {
            if (i == 3) {
                playerLevel[i] = 10;
                playerXP[i] = 1155;
            } else {
                playerLevel[i] = 1;
                playerXP[i] = 0;
            }
        }
        for (int i = 0; i < playerBankSize; i++) {
            bankItems[i] = 0;
        }
        for (int i = 0; i < playerBankSize; i++) {
            bankItemsN[i] = 0;
        }
        playerLook[0] = 0;
        playerLook[1] = 0;
        playerLook[2] = 5;
        playerLook[3] = 2;
        playerLook[4] = 2;
        playerLook[5] = 0;
        pHead = 3;
        pTorso = 18;
        pArms = 26;
        pHands = 34;
        pLegs = 38;
        pFeet = 43;
        pBeard = 10;
        absX = absY = 0;
        mapRegionX = mapRegionY = -1;
        currentX = currentY = 0;
        resetWalkingQueue();
        heightLevel = 0;
        teleportToX = 2852;
        teleportToY = 3336;
    }
    //Purchasing items
    public int hasReceivedRing = -1;
    public boolean expRing = false;
    
    public int damageDone = 0;
    public int pcStatSelect = 0;
    public boolean inCombat = false;
    public boolean firstSpecDone = false;
    public int skillId = 0;
    public int packetDelay = 0;
    public int teleEffect;
    public int interfaceEffect;
    public int defenderTask = 0;
    public int defenderDelay = 60;
    public int forgeRing = 280;
    public int npcCombat = 0;
    public int teleX = -1;
    public int teleY = -1;
    public int doTeleDelay = 0;
    public int startTeleEmote = 0;
    public int endTeleEmote = 0;
    public int heightLvl = 0;
    public int legendQuest = 3;
    public int mageZQuest = 4;
    public int runeMysteriesQuest = 4;
    public int mageArenaQuest = 6;
    public int lunarQuest = 3;
    public int knightWavesQuest = 1;
    public int curseZarosQuest = 5;
    public int lostCityQuest = 5;
    public int monkeyMadnessQuest = 9;
    public int undergroundPassQuest = 3;
    public int knightWavesGame = 0;
    public int meleeFightStatus = -1;
    public int meleeFightWith = 0;
    public int actionButtonId = 0;
    public int clanRequest = -1;
    public String clanRequestName = "";
    public String whosRightsChanged = "";
    public int apeHitAmt = 0;

    public boolean undergroundPassNetsArea() {
        return (absX >= 2467 && absX <= 2476 && absY >= 9679 && absY <= 9682 || absX >= 2469 && absX <= 2474 && absY >= 9677 && absY <= 9678 || absX >= 2467 && absX <= 2468 && absY >= 9675 && absY <= 9676 || absX >= 2471 && absX <= 2472 && absY >= 9675 && absY <= 9676 || absX >= 2475 && absX <= 2476 && absY >= 9675 && absY <= 9676 || absX >= 2467 && absX <= 2470 && absY >= 9673 && absY <= 9674 || absX >= 2473 && absX <= 2476 && absY >= 9673 && absY <= 9674);
    }

    public boolean meleeFightArea() {
        return (absX >= 2200 && absX <= 2215 && absY >= 4936 && absY <= 4943 || absX >= 2184 && absX <= 2191 && absY >= 4944 && absY <= 4951);
    }

    public boolean apeAtollJailArea() {
        return (absX >= 2765 && absX <= 2776 && absY >= 2793 && absY <= 2800 || absX == 2783 && absY >= 2789 && absY <= 2795 || absX == 2783 && absY >= 2781 && absY <= 2785 || absX >= 2755 && absX <= 2758 && absY >= 2798 && absY <= 2802);
    }

    public boolean monkeyCaveSafeArea() {
        return (absX >= 2769 && absX <= 2774 && absY >= 9100 && absY <= 9103 || absX >= 2780 && absX <= 2783 && absY >= 9098 && absY <= 9103 || absX >= 2780 && absX <= 2783 && absY >= 9090 && absY <= 9103
                || absX >= 2785 && absX <= 2790 && absY >= 9105 && absY <= 9111 || absX >= 2769 && absX <= 2774 && absY >= 9108 && absY <= 9111 || absX >= 2758 && absX <= 2764 && absY >= 9116 && absY <= 9121
                || absX >= 2791 && absX <= 2795 && absY >= 9115 && absY <= 9119 || absX >= 2796 && absX <= 2801 && absY >= 9110 && absY <= 9113 || absX >= 2798 && absX <= 2805 && absY >= 9095 && absY <= 9098
                || absX >= 2809 && absX <= 2813 && absY >= 9109 && absY <= 9113 || absX >= 2791 && absX <= 2795 && absY >= 9128 && absY <= 9133 || absX >= 2761 && absX <= 2768 && absY >= 9129 && absY <= 9135
                || absX >= 2750 && absX <= 2754 && absY >= 9130 && absY <= 9134 || absX >= 2738 && absX <= 2742 && absY >= 9135 && absY <= 9140 || absX >= 2715 && absX <= 2721 && absY >= 9127 && absY <= 9103
                || absX >= 2731 && absX <= 2736 && absY >= 9114 && absY <= 9124 || absX >= 2700 && absX <= 2714 && absY >= 9112 && absY <= 9120 || absX >= 2730 && absX <= 2734 && absY >= 9104 && absY <= 9108
                || absX >= 2722 && absX <= 2731 && absY >= 9097 && absY <= 9099 || absX >= 2691 && absX <= 2697 && absY >= 9112 && absY <= 9117 || absX >= 2694 && absX <= 2701 && absY >= 9141 && absY <= 9148
                || absX >= 2742 && absX <= 2745 && absY >= 9144 && absY <= 9148 || absX >= 2751 && absX <= 2754 && absY >= 9141 && absY <= 9144 || absX >= 2770 && absX <= 2779 && absY >= 9140 && absY <= 9146
                || absX >= 2791 && absX <= 2796 && absY >= 9137 && absY <= 9142 || absX >= 2797 && absX <= 2808 && absY >= 9137 && absY <= 9148);
    }

    public boolean damageArea(int x, int y) {
        return (x == 2743 && y == 9146 || x == 2701 && y == 9142 || x == 2691 && y == 9115 || x == 2693 && y == 9109 || x == 2725 && y == 9097 || x == 2742 && y == 9097 || x == 2740 && y == 9098 || x == 2744 && y == 9098 || x == 2746 && y == 9099 || x == 2743 && y == 9100 || x == 2746 && y == 9101 || x == 2742 && y == 9103 || x == 2745 && y == 9104 || x == 2740 && y == 9105 || x == 2745 && y == 9106 || x == 2742 && y == 9107 || x == 2733 && y == 9105 || x == 2705 && y == 9102 || x == 2707 && y == 9103 || x == 2711 && y == 9104 || x == 2703 && y == 9106 || x == 2706 && y == 9106 || x == 2710 && y == 9106 || x == 2713 && y == 9107 || x == 2703 && y == 9108 || x == 2706 && y == 9109 || x == 2713 && y == 9109 || x == 2709 && y == 9110 || x == 2711 && y == 9111 || x == 2704 && y == 9113 || x == 2707 && y == 9113 || x == 2710 && y == 9114 || x == 2715 && y == 9119 || x == 2735 && y == 9112 || x == 2736 && y == 9120 || x == 2737 && y == 9123 || x == 2738 && y == 9117
                || x == 2739 && y == 9142 || x == 2742 && y == 9121 || x == 2740 && y == 9122 || x == 2743 && y == 9123 || x == 2741 && y == 9125 || x == 2739 && y == 9127 || x == 2719 && y == 9130 || x == 2713 && y == 9130 || x == 2711 && y == 9131 || x == 2716 && y == 9131 || x == 2708 && y == 9132 || x == 2712 && y == 9133 || x == 2717 && y == 9133 || x == 2709 && y == 9134 || x == 2715 && y == 9134 || x == 2708 && y == 9136 || x == 2712 && y == 9136 || x == 2715 && y == 9137 || x == 2740 && y == 9135 || x == 2752 && y == 9134 || x == 2809 && y == 9111 || x == 2813 && y == 9111 || x == 2796 && y == 9112 || x == 2801 && y == 9112 || x == 2793 && y == 9119 || x == 2793 && y == 9115 || x == 2443 && y == 9677 || x == 2440 && y == 9677 || x == 2435 && y == 9676 || x == 2432 && y == 9676 || x == 2430 && y == 9676 || x == 2418 && y == 9681 || x == 2418 && y == 9685 || x == 2416 && y == 9689 || x == 2408 && y == 9674 || x == 2404 && y == 9675 || x == 2401 && y == 9675
                || x == 2396 && y == 9677 || x == 2393 && y == 9676 || x == 2383 && y == 9668 || x == 2382 && y == 9669 || x == 2382 && y == 9667);
    }

    public boolean monkeyCaveArea() {
        return (absX >= 2660 && absX <= 2820 && absY >= 9070 && absY <= 9160);
    }
    public int slayerId = 0;
    public int slayerAmt = 0;

    public int RandomCyclops() {
        int[] cyclops = {1313, 1309, 1311, 1283, 1281, 1217, defenderTask, 1207, 1209, 1295, 1297, 1299, 1424, defenderTask, 1430, 1317, 1365, 1249, 1153, 1141, 1101, 1191, defenderTask, 1105, 225, 1623, 1621, 1619, 249, 251, 253, defenderTask, 255};
        return cyclops[(int) (Math.random() * cyclops.length)];
    }

    public int randomFightWarrior() {
        int[] cyclops = {239, 240, 241, 242, 243, 244, 245, 246};
        return cyclops[(int) (Math.random() * cyclops.length)];
    }

    public int armourTypeDefence() {
        if (playerLevel[playerDefence] >= 40) {
            return 155;
        } else if (playerLevel[playerDefence] >= 30) {
            return 154;
        } else if (playerLevel[playerDefence] >= 20) {
            return 153;
        } else if (playerLevel[playerDefence] >= 15) {
            return 152;
        } else if (playerLevel[playerDefence] >= 10) {
            return 151;
        } else {
            return 160;
        }
    }

    public boolean legendArea() {
        return (absX >= 3188 && absX <= 3194 && absY >= 3355 && absY <= 3362);
    }

    public boolean knightWavesArea() {
        return (absX >= 2184 && absX <= 2193 && absY >= 4952 && absY <= 4959);
    }
    public int[] slayerTask20 = {105, 93, 1153, 117, 18, 88, 187, 181, 103, 81, 100, 12, 17};
    public int[] slayerTask40 = {1153, 2263, 15, 20, 19, 127, 82, 941, 93, 117, 9, 81};
    public int[] slayerTask70 = {1200, 1183, 1590, 55, 941, 1342, 1592, 20, 84, 54, 53, 49, 82};
    public int[] slayerTaskAmt = {30, 35, 40, 45, 50, 55};
    public boolean gameLoaded = false;

    public void stopMovement() {
        if (teleportToX <= 0 && teleportToY <= 0) {
            teleportToX = absX;
            teleportToY = absY;
        }
        newWalkCmdSteps = 0;
        newWalkCmdX[0] = newWalkCmdY[0] = tmpNWCX[0] = tmpNWCY[0] = 0;
        getNextPlayerMovement();
    }

    public int loadGame() {
        BufferedReader characterfile = null;
        String line = "";
        String token = "";
        String token2 = "";
        String[] token3 = new String[3];
        boolean EndOfFile = false;
        int CurrentRead = 0;
        if (playerName == "" || playerPass == "") {
            return 1;
        }
        try {
            characterfile = new BufferedReader(new FileReader("C:/Users/Administrator/Desktop/BattleScape/Characters/" + playerName + ".txt"));
        } catch (Exception e) {
            return 0;
        }
        try {
            while ((line = characterfile.readLine()) != null) {
                int spot = -1;
                if (line != null) {
                    line = line.trim();
                    spot = line.indexOf("=");
                }
                if (spot > -1) {
                    try {
                        token = line.substring(0, spot);
                        token = token.trim();
                        token2 = line.substring(spot + 1);
                        token2 = token2.trim();
                        token2 = token2.replaceAll("\t", " ");
                        token3 = token2.split(" ");
                        switch (CurrentRead) {
                            case 1:
                                if (token.equals("character-username")) {
                                    if (!playerName.equalsIgnoreCase(token2)) {
                                        return 1;
                                    }
                                }
                                if (token.equals("character-password")) {
                                    //if (!playerPass.equalsIgnoreCase(token2)) {
                                     //   return 1;
                                    //}
                                }
                                break;
                            case 2:
                                if (token.equals("character-height")) {
                                    heightLevel = Integer.parseInt(token2);
                                } else if (token.equals("character-posx")) {
                                    teleportToX = Integer.parseInt(token2);
                                    if (teleportToX == -1) {
                                        teleportToX = 0;
                                    }
                                } else if (token.equals("character-posy")) {
                                    teleportToY = Integer.parseInt(token2);
                                    if (teleportToY == -1) {
                                        teleportToY = 0;
                                    }
                                } else if (token.equals("character-rights")) {
                                    //playerRights = Integer.parseInt(token2);
                                }
                                break;
                            case 3:
                                if (token.equals("character-equip")) {
                                    playerEquipment[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                                    playerEquipmentN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
                                }
                                break;
                            case 4:
                                if (token.equals("character-skill")) {
                                    playerLevel[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                                    playerXP[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
                                }
                                break;
                            case 5:
                                if (token.equals("character-item")) {
                                    playerItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                                    playerItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
                                }
                                break;
                            case 6:
                                if (token.equals("character-bank")) {
                                    bankItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                                    bankItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
                                }
                                break;
                            case 7:
                                if (token.equals("character-friend")) {
                                    friends[Integer.parseInt(token3[0])] = Long.parseLong(token3[1]);
                                }
                                break;
                            case 8:
                                if (token.equals("character-ignore")) {
                                    ignores[Integer.parseInt(token3[0])] = Long.parseLong(token3[1]);
                                }
                                break;
                            case 9:
                                if (token.equals("character-look")) {
                                    playerLook[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                                }
                                if (token.equals("character-head")) {
                                    pHead = Integer.parseInt(token2);
                                }
                                if (token.equals("character-torso")) {
                                    pTorso = Integer.parseInt(token2);
                                }
                                if (token.equals("character-arms")) {
                                    pArms = Integer.parseInt(token2);
                                }
                                if (token.equals("character-hands")) {
                                    pHands = Integer.parseInt(token2);
                                }
                                if (token.equals("character-legs")) {
                                    pLegs = Integer.parseInt(token2);
                                }
                                if (token.equals("character-feet")) {
                                    pFeet = Integer.parseInt(token2);
                                }
                                if (token.equals("character-beard")) {
                                    pBeard = Integer.parseInt(token2);
                                }
                                break;
                        }
                    } catch (Exception e) {
                    }
                } else {
                    if (line.equals("[ACCOUNT]")) {
                        CurrentRead = 1;
                    } else if (line.equals("[CHARACTER]")) {
                        CurrentRead = 2;
                    } else if (line.equals("[EQUIPMENT]")) {
                        CurrentRead = 3;
                    } else if (line.equals("[SKILLS]")) {
                        CurrentRead = 4;
                    } else if (line.equals("[ITEMS]")) {
                        CurrentRead = 5;
                    } else if (line.equals("[BANK]")) {
                        CurrentRead = 6;
                    } else if (line.equals("[FRIENDS]")) {
                        CurrentRead = 7;
                    } else if (line.equals("[IGNORES]")) {
                        CurrentRead = 8;
                    } else if (line.equals("[LOOK]")) {
                        CurrentRead = 9;
                    } else if (line.equals("[EOF]")) {
                        characterfile.close();
                        break;
                    }
                }
            }
            characterfile.close();
        } catch (Exception e5) {
        }
        gameLoaded = true;
        return 3;
    }

    public void loadMoreInfo() {
        BufferedReader characterfile = null;
        String line = "";
        String token = "";
        String token2 = "";
        String[] token3 = new String[3];
        boolean EndOfFile = false;
        int CurrentRead = 0;
        try {
            characterfile = new BufferedReader(new FileReader("C:/Users/Administrator/Desktop/BattleScape/Characters/MoreInfo/" + playerName + ".txt"));
        } catch (Exception e1) {
            return;
        }
        try {
            line = characterfile.readLine();
        } catch (Exception e2) {
            return;
        }
        while (!EndOfFile && line != null) {
            try {
                line = line.trim();
                int spot = line.indexOf("=");
                if (spot > -1) {
                    token = line.substring(0, spot);
                    token = token.trim();
                    token2 = line.substring(spot + 1);
                    token2 = token2.trim();
                    token3 = token2.split("\t");
                    switch (CurrentRead) {
                        case 1:
                            if (token.equals("Clue-Delay")) {
                                clueRewardDelay = Integer.parseInt(token2);
                            }
                            break;
                        case 2:
                            if (token.equals("Fight-Arena-Kills")) {
                                FightArenaKills = Integer.parseInt(token2);
                            }
                            if (token.equals("Mage-Arena-Kills")) {
                                mageArenaKills = Integer.parseInt(token2);
                            }
                            break;
                        case 3:
                            if (token.equals("Recoil-Ring")) {
                                RecoilRing = Integer.parseInt(token2);
                            }
                            break;
                        case 4:
                            if (token.equals("dharok-at")) {
                                dharokSummoned = Integer.parseInt(token2);
                            }
                            if (token.equals("ahrim-at")) {
                                ahrimSummoned = Integer.parseInt(token2);
                            }
                            if (token.equals("guthan-at")) {
                                guthanSummoned = Integer.parseInt(token2);
                            }
                            if (token.equals("karil-at")) {
                                karilSummoned = Integer.parseInt(token2);
                            }
                            if (token.equals("torag-at")) {
                                toragSummoned = Integer.parseInt(token2);
                            }
                            if (token.equals("verac-at")) {
                                veracSummoned = Integer.parseInt(token2);
                            }
                            if (token.equals("hiddenB")) {
                                hiddenBarrowBro = Integer.parseInt(token2);
                            }
                            break;
                        case 5:
                            if (token.equals("poisonDelay")) {
                                poisonDelay = Integer.parseInt(token2);
                                if (poisonDelay > 300) {
                                    poisonDelay = -1;
                                }
                            }
                            if (token.equals("receivedExpRing2")) {
                            	hasReceivedRing = Integer.parseInt(token2);
                            }
                            if (token.equals("poisonDamage")) {
                                poisonDamage = Integer.parseInt(token2);
                            }
                            if (token.equals("specialamt")) {
                                specialAmount = Integer.parseInt(token2);
                            }
                            if (token.equals("spellset")) {
                                spellSet = Integer.parseInt(token2);
                            }
                            if (token.equals("vengence")) {
                                vengenceDelay = Integer.parseInt(token2);
                            }
                            if (token.equals("muted")) {
                                muted = Integer.parseInt(token2);
                            }
                            if (token.equals("pc")) {
                                pcPoints = Integer.parseInt(token2);
                            }
                            if (token.equals("pk")) {
                                pkPoints = Integer.parseInt(token2);
                            }
							if (token.equals("zombie")) {
								zombiePoints = Integer.parseInt(token2);
							}
							if (token.equals("clanwar")) {
								clanWarsPoints = Integer.parseInt(token2);
							}
                            if (token.equals("skull")) {
                                skullTimer = Integer.parseInt(token2);
                            }
                            if (token.equals("tzwave")) {
                                tzWave = Integer.parseInt(token2);
                            }
                            if (token.equals("cbowShots")) {
                                crystalBowShots = Integer.parseInt(token2);
                            }
                            if (token.equals("ancQuest")) {
                                ancQuest = Integer.parseInt(token2);
                            }
                            if (token.equals("legendQuest")) {
                                legendQuest = Integer.parseInt(token2);
                            }
                            if (token.equals("mageZQuest")) {
                                mageZQuest = Integer.parseInt(token2);
                            }
                            if (token.equals("slayerId")) {
                                slayerId = Integer.parseInt(token2);
                            }
                            if (token.equals("slayerAmt")) {
                                slayerAmt = Integer.parseInt(token2);
                            }
                            if (token.equals("runeMysteriesQuest")) {
                                runeMysteriesQuest = Integer.parseInt(token2);
                            }
                            if (token.equals("mageArenaQuest")) {
                                mageArenaQuest = Integer.parseInt(token2);
                            }
                            if (token.equals("lunarQuest")) {
                                lunarQuest = Integer.parseInt(token2);
                            }
                            if (token.equals("knightWavesQuest")) {
                                knightWavesQuest = Integer.parseInt(token2);
                            }
                            if (token.equals("skillId")) {
                                skillId = Integer.parseInt(token2);
                            }
                            if (token.equals("lostCityQuest")) {
                                lostCityQuest = Integer.parseInt(token2);
                            }
                            if (token.equals("forgeRing")) {
                                forgeRing = Integer.parseInt(token2);
                            }
                            if (token.equals("monkeyMadnessQuest")) {
                                monkeyMadnessQuest = Integer.parseInt(token2);
                            }
                            if (token.equals("undergroundPassQuest")) {
                                undergroundPassQuest = Integer.parseInt(token2);
                            }
                            if (token.equals("bankPin")) {
                                bankPin = Integer.parseInt(token2);
                            }
                            break;
                    }
                } else {
                    if (line.equals("[Player-Clues]")) {
                        CurrentRead = 1;
                    } else if (line.equals("[Player-Kills]")) {
                        CurrentRead = 2;
                    } else if (line.equals("[RecoilRing]")) {
                        CurrentRead = 3;
                    } else if (line.equals("[Barrows]")) {
                        CurrentRead = 4;
                    } else if (line.equals("[Other]")) {
                        CurrentRead = 5;
                    } else if (line.equals("[EOF]")) {
                        try {
                            characterfile.close();
                        } catch (Exception e3) {
                        }
                        EndOfFile = true;
                    }
                }
            } catch (Exception e6) {
            }
            try {
                line = characterfile.readLine();
            } catch (Exception e4) {
                EndOfFile = true;
            }
        }
        try {
            characterfile.close();
        } catch (Exception e5) {
        }
    }
    public boolean autoAtk = false;
    public boolean isConnected = false;
    public int coverageId = -1;
    public int isMember = 0;
	public int isExtremeMember = 0;
	public boolean expLock = false;
    public int[] memberItems = {8027, 8028, 8029, 7961, 7962, 7963, 7964, 7965, 7966, 7967, 7968, 7969, 7970, 7971, 7972};
	public int[] extremeItems = {8922, 7777};
    public int teleBlockDelay = 0;
    public int jNPoints = 0;
    public int crystalBowShots = 2000;
    public int combatWith = 0;
    public int combatWith2 = 0;
    public int duelWith = 0;
    public boolean runStream = true;
    public boolean duelRule[] = new boolean[12];
    public int duelItems[] = new int[28];
    public int duelItemsN[] = new int[28];
    public int otherDuelItems[] = new int[28];
    public int otherDuelItemsN[] = new int[28];
    public int duelStatus = -1;
    public int ancQuest = 4;

    public boolean multiZone() {
        return (absX >= 3287 && absX <= 3298 && absY >= 3167 && absY <= 3178 || absX >= 3070 && absX <= 3095 && absY >= 3405 && absY <= 3448 || absX >= 2961 && absX <= 2981 && absY >= 3330 && absY <= 3354
                || absX >= 2510 && absX <= 2537 && absY >= 4632 && absY <= 4660 || absX >= 3012 && absX <= 3066 && absY >= 4805 && absY <= 4858/*Abyss*/ || absX >= 2794 && absX <= 2813 && absY >= 9281 && absY <= 9305
                || absX >= 3546 && absX <= 3557 && absY >= 9689 && absY <= 9700 || absX >= 2708 && absX <= 2729 && absY >= 9801 && absY <= 9829 || absX >= 3450 && absX <= 3525 && absY >= 9470 && absY <= 9535
                || absX >= 3207 && absX <= 3395 && absY >= 3904 && absY <= 3903 || absX >= 3006 && absX <= 3072 && absY >= 3611 && absY <= 3712 || absX >= 3149 && absX <= 3395 && absY >= 3520 && absY <= 4000
                || pcStatus == 2 || Server.s.pc.pcGameArea(absX, absY) || absX >= 2365 && absX <= 2420 && absY >= 5065 && absY <= 5120 || absX >= 2890 && absX <= 2935 && absY >= 4425 && absY <= 4470
                || absX >= 2250 && absX <= 2290 && absY >= 4675 && absY <= 4715 || absX >= 2690 && absX <= 2825 && absY >= 2680 && absY <= 2810
                || absX <= 2380 && absX >= 2423 && absY <= 5167 && absY >= 5127 || ZombieMinigame.players.contains(playerName) || Server.s.ClanWars.teamBluePlayers.contains(this) || Server.s.ClanWars.teamRedPlayers.contains(this));
    }

    public boolean WildArea() {
        return (absX >= 2940 && absX <= 3395 && absY >= 3520 && absY <= 4000) || FightPits.fighters.contains(playerName);
    }
	
	public boolean FightPits() {
		return (absX <= 2380 && absX >= 2423 && absY <= 5167 && absY >= 5127) || FightPits.fighters.contains(playerName);
	}

    public boolean duelArea() {
        return (absX >= 3318 && absX <= 3381 && absY >= 3201 && absY <= 3288 || duelStatus == 3);
    }

    public boolean defenderArea() {
        return (absX >= 2190 && absX <= 2223 && absY >= 4935 && absY <= 4980 && heightLevel == 1);
    }
    public int deathDelay = 0;
    public int followId = 0;
    public int followId2 = 0;
    public int skullTimer = -1, lastHit = 0;
    public int combatDelay = 0;
    public boolean logOutButton = false;
    public int spellSet = 0;
    public int muted = 0;
    public int pcStatus = 0, pcPoints = 0;
    public int pkPoints = 0;
	public int zombiePoints = 0;
	public int clanWarsPoints = 0;

    public boolean IsInFightCave() {
        return (absX >= 2360 && absX <= 2445 && absY >= 5045 && absY <= 5125);
    }

    public boolean IsInFightArena() {
        if (absX >= 2583 && absY >= 3153 && absX <= 2606 && absY <= 3170) {
            return true;
        } else {
            return false;
        }
    }

    public boolean pestControlZone() {
        return (absX >= 2610 && absX <= 2695 && absY >= 2550 && absY <= 2625 || absX == 2657 && absY <= 2641 && absY >= 2638 || absX >= 2610 && absX <= 2695 && absY >= 2550 && absY <= 2625 || pcStatus == 2);
    }
    public int vengenceDelay = -1;
    public int EntangleDelay = 0;
    public int[] KilledBy = new int[Server.s.playerHandler.maxPlayers];
    public int FightArenaKills = 0;
    public int mageArenaKills = 0;
    public boolean protectItem = false;

    public void checkDrain() {
        if (NewDrain > CurrentDrain && DrainDelay > 0) {
            return;
        }
        int highest = highestDrainRate();
        double drainBonusDelay = (playerBonus[11] * 0.4);
        drainBonusDelay = Math.round(drainBonusDelay + 0.5f);
        if (CurrentDrain != highest) {
            CurrentDrain = highest;
        }
        if (CurrentDrain < 0) {
            CurrentDrain = 1;
        }
        if (DrainDelay > 0) {
            DrainDelay = NewDrain - (DrainDelay - (int) (drainBonusDelay));
        } else {
            DrainDelay = CurrentDrain + (int) (drainBonusDelay);
        }
    }

    public int highestDrainRate() {
        if (AtkPray == 7 && DefPray == 7 && StrPray == 7) {
            return 4;
        } else if (AtkPray == 6 && DefPray == 6 && StrPray == 6) {
            return 4;
        } else if (HeadPray == 6) {
            return 4;
        } else if (HeadPray == 3) {
            return 6;
        } else if (HeadPray == 2) {
            return 6;
        } else if (HeadPray == 1) {
            return 6;
        } else if (AtkPray == 5) {
            return 6;
        } else if (AtkPray == 4) {
            return 6;
        } else if (AtkPray == 3) {
            return 6;
        } else if (StrPray == 3) {
            return 6;
        } else if (DefPray == 3) {
            return 6;
        } else if (HeadPray == 5) {
            return 12;
        } else if (AtkPray == 2) {
            return 12;
        } else if (StrPray == 2) {
            return 12;
        } else if (DefPray == 2) {
            return 12;
        } else if (HeadPray == 4) {
            return 24;
        } else if (AtkPray == 1) {
            return 24;
        } else if (StrPray == 1) {
            return 24;
        } else if (DefPray == 1) {
            return 24;
        } else if (protectItem) {
            return 36;
        } else {
            return 0;
        }
    }

    public boolean CheckIfPray() {
        if (DefPray == 0 && StrPray == 0 && AtkPray == 0 && HeadPray == 0 && !protectItem) {
            CurrentDrain = 0;
            NewDrain = 0;
            DrainDelay = 0;
            return false;
        }
        return true;
    }

    public int getDis(int coordX1, int coordY1, int coordX2, int coordY2) {
        int deltaX = coordX2 - coordX1;
        int deltaY = coordY2 - coordY1;
        return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
    }
    public boolean soundEffects = true;
    /*Start of Barrows*/
    public int dharokSummoned = 0;
    public int ahrimSummoned = 0;
    public int guthanSummoned = 0;
    public int karilSummoned = 0;
    public int toragSummoned = 0;
    public int veracSummoned = 0;
    public int barrowBroSlot = 0;
    public int hiddenBarrowBro = Misc.random(6);

    public boolean dharoksMound() {
        return (absX >= 3570 && absX <= 3576 && absY >= 3293 && absY <= 3299);
    }

    public boolean veracsMound() {
        return (absX >= 3554 && absX <= 3559 && absY >= 3294 && absY <= 3300);
    }

    public boolean ahrimsMound() {
        return (absX >= 3561 && absX <= 3567 && absY >= 3286 && absY <= 3292);
    }

    public boolean toragsMound() {
        return (absX >= 3550 && absX <= 3555 && absY >= 3280 && absY <= 3285);
    }

    public boolean karilsMound() {
        return (absX >= 3563 && absX <= 3568 && absY >= 3273 && absY <= 3279);
    }

    public boolean guthansMound() {
        return (absX >= 3574 && absX <= 3579 && absY >= 3279 && absY <= 3284);
    }

    public void resetBarrows() {
        dharokSummoned = 0;
        ahrimSummoned = 0;
        guthanSummoned = 0;
        karilSummoned = 0;
        toragSummoned = 0;
        veracSummoned = 0;
        barrowBroSlot = 0;
        hiddenBarrowBro = Misc.random(6);
    }
    /*End of Barrows*/
    public int tzWave = -1;
    public int WaveDelay = 40;
    public int KilledTz = 0;
    public int NewHp = 10;
    public int NewDrain = 0, CurrentDrain = 0, DrainDelay = 0;
    public int DefPray = 0;
    public int StrPray = 0;
    public int AtkPray = 0;
    public int HeadPray = 0;
    public int clueRewardDelay = Misc.random5(3);
    public int meleeDelay = 0;
    public boolean isNpc;
    public int npcId = 0;
    public boolean initialized = false, disconnected = false, savefile = false;
    public boolean isActive = false;
    public boolean isKicked = false;
    public String PlayerIp = "0.0.0.0";
    public String globalMessage = "";
    public int[] playerTItems = new int[28];
    public int[] playerTItemsN = new int[28];
    public int[] playerOTItems = new int[28];
    public int[] playerOTItemsN = new int[28];
    public boolean takeAsNote = false;

    public abstract void initialize();

    public abstract void UpdateArea();

    public abstract void sendMessage(String str);

    public abstract void updateHp(int NewHp, boolean bol);

    public abstract void UpdateAll();

    public abstract void ApplyDead();

    public abstract void ShowOption(int i, String s);
    public int playerId = -1;
    public String playerName = "null";
    public String playerPass = "null";
    public boolean isRunning2 = false;
    public int playerRights;
	public int xLogDelay = 0;
	public boolean jrMod = false;
	public boolean seniorMod = false;
	public boolean jrAdmin = false;
	public boolean admin = false;
	public boolean developer = false;
	public boolean founder = false;
    public PlayerHandler handler = null;
    public int maxItemAmount = 999999999;
    public int[] playerItems = new int[28];
    public int[] playerItemsN = new int[28];
    public int playerBankSize = 352;
    public int[] bankItems = new int[800];
    public int[] bankItemsN = new int[800];
    public boolean bankNotes = false;
    public int pGender;
    public int pHairC;
    public int pTorsoC;
    public int pLegsC;
    public int pFeetC;
    public int pSkinC;
    public boolean apset;
    public int pHead;
    public int pTorso;
    public int pArms;
    public int pHands;
    public int pLegs;
    public int pFeet;
    public int pBeard;
    public int[] playerEquipment = new int[14];
    public int[] playerEquipmentN = new int[14];
    public int playerHat = 0;
    public int playerCape = 1;
    public int playerAmulet = 2;
    public int playerWeapon = 3;
    public int playerChest = 4;
    public int playerShield = 5;
    public int playerLegs = 7;
    public int playerHands = 9;
    public int playerFeet = 10;
    public int playerRing = 12;
    public int playerArrows = 13;
    public int playerAttack = 0;
    public int playerDefence = 1;
    public int playerStrength = 2;
    public int playerHitpoints = 3;
    public int playerRanged = 4;
    public int playerPrayer = 5;
    public int playerMagic = 6;
    public int playerCooking = 7;
    public int playerWoodcutting = 8;
    public int playerFletching = 9;
    public int playerFishing = 10;
    public int playerFiremaking = 11;
    public int playerCrafting = 12;
    public int playerSmithing = 13;
    public int playerMining = 14;
    public int playerHerblore = 15;
    public int playerAgility = 16;
    public int playerThieving = 17;
    public int playerSlayer = 18;
    public int playerFarming = 19;
    public int playerRunecrafting = 20;
    public int i = 0;

    public abstract boolean isinpm(long l);

    public abstract void loadpm(long l, int world);

    public abstract void pmupdate(int pmid, int world);
    public int Privatechat = 0;
    public int Publicchat = 0;
    public int Tradecompete = 0;

    public abstract void sendpm(long name, int rights, byte[] chatmessage, int messagesize);
    public long friends[] = new long[200];
    public long ignores[] = new long[100];
    public boolean IsPMLoaded = false;
    public int[] playerLevel = new int[25];
    public int[] playerXP = new int[25];
    public int currentHealth = playerLevel[playerHitpoints];
    public int maxHealth = playerLevel[playerHitpoints];
    public final int maxPlayerListSize = Server.s.playerHandler.maxPlayers;
    public Player playerList[] = new Player[maxPlayerListSize];
    //public List<Player> playerList = new LinkedList<Player>();
    public int playerListSize = 0;
    public byte playerInListBitmap[] = new byte[(Server.s.playerHandler.maxPlayers + 7) >> 3];
    public final int maxNPCListSize = Server.s.npcHandler.maxNPCs;
    //public NPC npcList[] = new NPC[maxNPCListSize];
    public List<NPC> npcList = new LinkedList<NPC>();
    public int npcListSize = 0;
    public byte npcInListBitmap[] = new byte[(Server.s.npcHandler.maxNPCs + 7) >> 3];

    public boolean withinDistance(Player otherPlr) {
        if (otherPlr != null) {
            if (heightLevel != otherPlr.heightLevel || otherPlr.disconnected || disconnected) {
                return false;
            }
            int deltaX = otherPlr.absX - absX, deltaY = otherPlr.absY - absY;
            int deltaX2 = otherPlr.teleportToX - absX, deltaY2 = otherPlr.teleportToY - absY;
            return (deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16);
        }
        return false;
    }

    public boolean withinDistance(NPC npc) {
        if (npc != null) {
            if (heightLevel != npc.heightLevel) {
                return false;
            }
            if (npc.NeedRespawn) {
                return false;
            }
            int deltaX = npc.absX - absX, deltaY = npc.absY - absY;
            return (deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16);
        }
        return false;
    }
    public int mapRegionX, mapRegionY;
    public int absX, absY;
    public int currentX, currentY;
    public int heightLevel;
    public boolean updateRequired = true;
    public final int walkingQueueSize = 50;
    public int walkingQueueX[] = new int[walkingQueueSize], walkingQueueY[] = new int[walkingQueueSize];
    public int wQueueReadPtr = 0;
    public int wQueueWritePtr = 0;
    public boolean isRunning = false;
    public int teleportToX = -1, teleportToY = -1;

    public void resetWalkingQueue() {
        wQueueReadPtr = wQueueWritePtr = 0;
        for (int i = 0; i < walkingQueueSize; i++) {
            walkingQueueX[i] = currentX;
            walkingQueueY[i] = currentY;
        }
    }

    public void addToWalkingQueue(int x, int y) {
        int next = (wQueueWritePtr + 1) % walkingQueueSize;
        if (next == wQueueWritePtr) {
            return;
        }
        walkingQueueX[wQueueWritePtr] = x;
        walkingQueueY[wQueueWritePtr] = y;
        wQueueWritePtr = next;
    }

    public int getPlayerKiller() {
        int Killer = 0;
        int Count = 0;
        for (int i = 1; i < Server.s.playerHandler.maxPlayers; i++) {
            if (Killer == 0) {
                Killer = i;
                Count = 1;
            } else {
                if (KilledBy[i] > KilledBy[Killer]) {
                    Killer = i;
                    Count = 1;
                } else if (KilledBy[i] == KilledBy[Killer]) {
                    Count++;
                }
            }
        }
        if (Count > 1) {
            Killer = playerId;
        }
        return Killer;
    }

    public int getNextWalkingDirection() {
        if (wQueueReadPtr == wQueueWritePtr) {
            return -1;
        }
        int dir;
        do {
            dir = Misc.direction(currentX, currentY, walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);
            if (dir == -1) {
                wQueueReadPtr = (wQueueReadPtr + 1) % walkingQueueSize;
            } else if ((dir & 1) != 0) {
                resetWalkingQueue();
                return -1;
            }
        } while (dir == -1 && wQueueReadPtr != wQueueWritePtr);
        if (dir == -1) {
            return -1;
        }
        dir >>= 1;
        currentX += Misc.directionDeltaX[dir];
        currentY += Misc.directionDeltaY[dir];
        absX += Misc.directionDeltaX[dir];
        absY += Misc.directionDeltaY[dir];
        return dir;
    }
    public boolean didTeleport = false;
    public boolean mapRegionDidChange = false;
    public int dir1 = -1, dir2 = -1;
    public int poimiX = 0, poimiY = 0;

    public boolean WithinDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
        for (int i = 0; i <= distance; i++) {
            for (int j = 0; j <= distance; j++) {
                if ((objectX + i) == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                } else if ((objectX - i) == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                } else if (objectX == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void getNextPlayerMovement() {
        mapRegionDidChange = false;
        didTeleport = false;
        dir1 = dir2 = -1;
        if (teleportToX != -1 && teleportToY != -1) {
            mapRegionDidChange = true;
            if (mapRegionX != -1 && mapRegionY != -1) {
                int relX = teleportToX - mapRegionX * 8, relY = teleportToY - mapRegionY * 8;
                if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8 && relY < 11 * 8) {
                    mapRegionDidChange = false;
                }
            }
            if (mapRegionDidChange) {
                mapRegionX = (teleportToX >> 3) - 6;
                mapRegionY = (teleportToY >> 3) - 6;
            }
            currentX = teleportToX - 8 * mapRegionX;
            currentY = teleportToY - 8 * mapRegionY;
            absX = teleportToX;
            absY = teleportToY;
            resetWalkingQueue();
            teleportToX = teleportToY = -1;
            didTeleport = true;
        } else {
            dir1 = getNextWalkingDirection();
            if (dir1 == -1) {
                return;
            }
            if (isRunning) {
                dir2 = getNextWalkingDirection();
            }
            int deltaX = 0, deltaY = 0;
            if (currentX < 2 * 8) {
                deltaX = 4 * 8;
                mapRegionX -= 4;
                mapRegionDidChange = true;
            } else if (currentX >= 11 * 8) {
                deltaX = -4 * 8;
                mapRegionX += 4;
                mapRegionDidChange = true;
            }
            if (currentY < 2 * 8) {
                deltaY = 4 * 8;
                mapRegionY -= 4;
                mapRegionDidChange = true;
            } else if (currentY >= 11 * 8) {
                deltaY = -4 * 8;
                mapRegionY += 4;
                mapRegionDidChange = true;
            }
            if (mapRegionDidChange) {
                currentX += deltaX;
                currentY += deltaY;
                for (int i = 0; i < walkingQueueSize; i++) {
                    walkingQueueX[i] += deltaX;
                    walkingQueueY[i] += deltaY;
                }
            }
        }
    }

    public void updateThisPlayerMovement(stream str) {
        if (str != null) {
            if (mapRegionDidChange) {
                str.createFrame(73);
                str.writeWordA(mapRegionX + 6);
                str.writeWord(mapRegionY + 6);
            }
            if (didTeleport) {
                str.createFrameVarSizeWord(81);
                str.initBitAccess();
                str.writeBits(1, 1);
                str.writeBits(2, 3);
                str.writeBits(2, heightLevel);
                str.writeBits(1, 1);
                str.writeBits(1, (updateRequired) ? 1 : 0);
                str.writeBits(7, currentY);
                str.writeBits(7, currentX);
                if (IsDead && IsDeadTimer && IsDeadTeleporting) {
                    IsDead = false;
                    IsDeadTimer = false;
                }
                return;
            }
            if (dir1 == -1) {
                str.createFrameVarSizeWord(81);
                str.initBitAccess();
                if (updateRequired) {
                    str.writeBits(1, 1);
                    str.writeBits(2, 0);
                } else {
                    str.writeBits(1, 0);
                }
                if (DirectionCount < 50) {
                    DirectionCount++;
                }
            } else {
                DirectionCount = 0;
                str.createFrameVarSizeWord(81);
                str.initBitAccess();
                str.writeBits(1, 1);
                if (dir2 == -1) {
                    str.writeBits(2, 1);
                    str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
                    if (updateRequired) {
                        str.writeBits(1, 1);
                    } else {
                        str.writeBits(1, 0);
                    }
                } else {
                    str.writeBits(2, 2);
                    str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
                    str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
                    if (updateRequired) {
                        str.writeBits(1, 1);
                    } else {
                        str.writeBits(1, 0);
                    }
                }
            }
        }
    }

    public void updatePlayerMovement(stream str) {
        if (str != null) {
            if (dir1 == -1) {
                if (updateRequired || chatTextUpdateRequired) {
                    str.writeBits(1, 1);
                    str.writeBits(2, 0);
                } else {
                    str.writeBits(1, 0);
                }
            } else if (dir2 == -1) {
                str.writeBits(1, 1);
                str.writeBits(2, 1);
                str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
                str.writeBits(1, (updateRequired || chatTextUpdateRequired) ? 1 : 0);
            } else {
                str.writeBits(1, 1);
                str.writeBits(2, 2);
                str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
                str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
                str.writeBits(1, (updateRequired || chatTextUpdateRequired) ? 1 : 0);
            }
        }
    }
    public byte cachedPropertiesBitmap[] = new byte[(Server.s.playerHandler.maxPlayers + 7) >> 3];

    public void addNewNPC(NPC npc, stream str, stream updateBlock) {
        if (npc != null && str != null && updateBlock != null) {
            int id = npc.NpcSlot;
            npcList.add(npc);
            str.writeBits(14, id);
            int z = npc.absY - absY;
            if (z < 0) {
                z += 32;
            }
            str.writeBits(5, z);
            z = npc.absX - absX;
            if (z < 0) {
                z += 32;
            }
            str.writeBits(5, z);
            str.writeBits(1, 0);
            str.writeBits(12, npc.NpcType);
            boolean savedUpdateRequired = npc.updateRequired;
            npc.updateRequired = true;
            npc.appendNPCUpdateBlock(updateBlock);
            npc.updateRequired = savedUpdateRequired;
            str.writeBits(1, 1);
        }
    }

    /*public void addNewPlayer(Player plr, stream str, stream updateBlock) {
        if (plr != null && str != null && updateBlock != null) {
            int id = plr.playerId;
            playerList.add(plr);
            str.writeBits(11, id);
            str.writeBits(1, 1);
            boolean savedFlag = plr.appearanceUpdateRequired;
            boolean savedUpdateRequired = plr.updateRequired;
            plr.appearanceUpdateRequired = true;
            plr.updateRequired = true;
            plr.appendPlayerUpdateBlock(updateBlock);
            plr.appearanceUpdateRequired = savedFlag;
            plr.updateRequired = savedUpdateRequired;
            str.writeBits(1, 1);
            int z = plr.absY - absY;
            str.writeBits(5, z);
            z = plr.absX - absX;
            str.writeBits(5, z);
        }
    }*/
	public void addNewPlayer(Player plr, stream str, stream updateBlock) {
		if (plr != null && str != null && updateBlock != null) {
		int id = plr.playerId;
		playerInListBitmap[id >> 3] |= 1 << (id & 7); // set the flag
		playerList[playerListSize++] = plr;

		str.writeBits(11, id); // client doesn't seem to like id=0

		// TODO: properly implement the character appearance handling
		// send this every time for now and don't make use of the cached ones in
		// client
		str.writeBits(1, 1); // set to true, if player definitions follow below
		boolean savedFlag = plr.appearanceUpdateRequired;
		boolean savedUpdateRequired = plr.updateRequired;
		plr.appearanceUpdateRequired = true;
		plr.updateRequired = true;
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.appearanceUpdateRequired = savedFlag;
		plr.updateRequired = savedUpdateRequired;

		str.writeBits(1, 1); // set to true, if we want to discard the
		// (client side) walking queue
		// no idea what this might be useful for yet
		int z = plr.absY - absY;
		if (z < 0)
			z += 32;
		str.writeBits(5, z); // y coordinate relative to thisPlayer
		z = plr.absX - absX;
		if (z < 0)
			z += 32;
		str.writeBits(5, z); // x coordinate relative to thisPlayer
		}
	}
    public int HeadIconPrayer = 0;
    public int HeadIconPk = 0;
    public boolean appearanceUpdateRequired = true;
    public static stream playerProps;

    static {
        playerProps = new stream(new byte[150]);
    }

    private void appendPlayerAppearance(stream str) {
        if (str != null) {
            playerProps.currentOffset = 0;
            if (playerLook[0] != 0 && playerLook[0] != 1) {
                playerLook[0] = 0;
            }
            playerProps.writeByte(playerLook[0]);
            playerProps.writeByte(HeadIconPrayer);
            playerProps.writeByte(HeadIconPk);
            if (!isNpc) {
                if (playerEquipment[playerHat] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerHat]);
                } else {
                    playerProps.writeByte(0);
                }
                if (playerEquipment[playerCape] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerCape]);
                } else {
                    playerProps.writeByte(0);
                }
                if (playerEquipment[playerAmulet] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerAmulet]);
                } else {
                    playerProps.writeByte(0);
                }
                if (playerEquipment[playerWeapon] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerWeapon]);
                } else {
                    playerProps.writeByte(0);
                }
                if (playerEquipment[playerChest] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerChest]);
                } else {
                    if (pTorso < 0 || pTorso > 100) {
                        pTorso = 18;
                    }
                    playerProps.writeWord(0x100 + pTorso);
                }
                if (playerEquipment[playerShield] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerShield]);
                } else {
                    playerProps.writeByte(0);
                }
                if (!Item.isPlate(playerEquipment[playerChest])) {
                    if (pArms < 0 || pArms > 100) {
                        pArms = 26;
                    }
                    playerProps.writeWord(0x100 + pArms);
                } else {
                    playerProps.writeByte(0);
                }
                if (playerEquipment[playerLegs] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerLegs]);
                } else {
                    if (pLegs < 0 || pLegs > 100) {
                        pLegs = 38;
                    }
                    playerProps.writeWord(0x100 + pLegs);
                }
                if (!Item.isFullHelm(playerEquipment[playerHat]) && !Item.isFullMask(playerEquipment[playerHat])) {
                    if (pHead < 0 || pHead > 100) {
                        pHead = 3;
                    }
                    playerProps.writeWord(0x100 + pHead);
                } else {
                    playerProps.writeByte(0);
                }
                if (playerEquipment[playerHands] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerHands]);
                } else {
                    if (pHands < 0 || pHands > 100) {
                        pHands = 34;
                    }
                    playerProps.writeWord(0x100 + pHands);
                }
                if (playerEquipment[playerFeet] > 1) {
                    playerProps.writeWord(0x200 + playerEquipment[playerFeet]);
                } else {
                    if (pFeet < 0 || pFeet > 100) {
                        pFeet = 43;
                    }
                    playerProps.writeWord(0x100 + pFeet);
                }
                if (!Item.isFullMask(playerEquipment[playerHat]) && (playerLook[0] != 1)) {
                    if (pBeard < 0 || pBeard > 100) {
                        pBeard = 10;
                    }
                    playerProps.writeWord(0x100 + pBeard);
                } else {
                    playerProps.writeByte(0);
                }
            } else {
                playerProps.writeWord(-1);
                playerProps.writeWord(npcId);

            }
            for (int l = 1; l < 6; l++) {
                if (playerLook[l] < 0 || playerLook[l] > 100) {
                    playerLook[l] = 0;
                }
            }
            playerProps.writeByte(playerLook[1]);
            playerProps.writeByte(playerLook[2]);
            playerProps.writeByte(playerLook[3]);
            playerProps.writeByte(playerLook[4]);
            playerProps.writeByte(playerLook[5]);
            playerProps.writeWord(playerSE);
            //playerProps.writeWord(0x337);
			playerProps.writeWord(playerSW);
            playerProps.writeWord(playerSEW);
            /*playerProps.writeWord(0x334);
            playerProps.writeWord(0x335);
            playerProps.writeWord(0x336);*/
			playerProps.writeWord(playerSW);
			playerProps.writeWord(playerSW);
			playerProps.writeWord(playerSW);
            playerProps.writeWord(playerSER);
            playerProps.writeQWord(Misc.playerNameToInt64(playerName));
            getCombat();
            playerProps.writeByte(combatLevel);
            playerProps.writeWord(0);
            str.writeByteC(playerProps.currentOffset);
            str.writeBytes(playerProps.buffer, playerProps.currentOffset, 0);
        }
    }

    public void getCombat() {
        int attack = getLevelForXP(playerXP[playerAttack]);
        int defence = getLevelForXP(playerXP[playerDefence]);
        int strength = getLevelForXP(playerXP[playerStrength]);
        int hp = getLevelForXP(playerXP[playerHitpoints]);
        int prayer = getLevelForXP(playerXP[playerPrayer]);
        int ranged = getLevelForXP(playerXP[playerRanged]);
        int magic = getLevelForXP(playerXP[playerMagic]);

        combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1;

        double melee = (attack + strength) * 0.325;
        double ranger = Math.floor(ranged * 1.5) * 0.325;
        double mage = Math.floor(magic * 1.5) * 0.325;

        if (melee >= ranger && melee >= mage) {
            combatLevel += melee;
        } else if (ranger >= melee && ranger >= mage) {
            combatLevel += ranger;
        } else if (mage >= melee && mage >= ranger) {
            combatLevel += mage;
        }
    }
    public int combatLevel = 0;
    protected boolean chatTextUpdateRequired = false;
    protected byte chatText[] = new byte[4096], chatTextSize = 0;
    protected int chatTextEffects = 0, chatTextColor = 0;
    public int bankPin = -1;
    public int onPin = 0;
    public int enteredPin = 0;
    public boolean pinChanged = false;

    protected void appendPlayerChatText(stream str) {
        if (str != null) {
            str.writeWordBigEndian(((chatTextColor & 0xFF) << 8) + (chatTextEffects & 0xFF));
            str.writeByte(playerRights);
            str.writeByteC(chatTextSize);
            str.writeBytes_reverse(chatText, chatTextSize, 0);
        }
    }

    public void drawDamage(stream str) {
        if (str != null) {
            str.writeByte(0x20);
            appendHitUpdate(str);
        }
    }

    public void appendPlayerUpdateBlock(stream str) {
        if (str != null) {
            if (!updateRequired && !chatTextUpdateRequired) {
                return;
            }
            int updateMask = 0;
            if (FaceUpdateRequired) {
                updateMask |= 1;
            }
            if (animationUpdateRequired) {
                updateMask |= 8;
            }
            if (GraphicsUpdateRequired) {
                updateMask |= 0x100;
            }
            if (forcedChatUpdateRequired) {
                updateMask |= 4;
            }
            if (chatTextUpdateRequired) {
                updateMask |= 0x80;
            }
            if (appearanceUpdateRequired) {
                updateMask |= 0x10;
            }
            if (hitUpdateRequired) {
                updateMask |= 0x20;
            }
            if (dirUpdateRequired) {
                updateMask |= 0x40;
            }
            if (faceObjectRequired) {
                updateMask |= 2;
            }
            if (hitUpdateRequired2) {
                updateMask |= 0x200;
            }
            if (updateMask >= 0x100) {
                updateMask |= 0x40;
                str.writeByte(updateMask & 0xFF);
                str.writeByte(updateMask >> 8);
            } else {
                str.writeByte(updateMask);
            }
            if (FaceUpdateRequired) {
                appendSetFocusDestination(str);
            }
            if (animationUpdateRequired) {
                appendAnimationRequest(str);
            }
            if (GraphicsUpdateRequired) {
                appendGraphicsUpdate(str);
            }
            if (forcedChatUpdateRequired) {
                str.writeString(forcedPlayerText);
            }
            if (chatTextUpdateRequired) {
                appendPlayerChatText(str);
            }
            if (appearanceUpdateRequired) {
                appendPlayerAppearance(str);
            }
            if (hitUpdateRequired) {
                appendHitUpdate(str);
            }
            if (dirUpdateRequired) {
                appendDirUpdate(str);
            }
            if (faceObjectRequired) {
                appendObjectUpdate(str);
            }
            if (hitUpdateRequired2) {
                appendHitUpdate2(str);
            }
        }
    }
    public String forcedPlayerText = "";
    public boolean forcedChatUpdateRequired = false;

    public void clearUpdateFlags() {
        updateRequired = false;
        FaceUpdateRequired = false;
        animationUpdateRequired = false;
        GraphicsUpdateRequired = false;
        forcedChatUpdateRequired = false;
        chatTextUpdateRequired = false;
        appearanceUpdateRequired = false;
        hitUpdateRequired = false;
        hitUpdateRequired2 = false;
        hitUpdateRequired3 = false;
        dirUpdateRequired = false;
        faceObjectRequired = false;
        poisonHit = false;
        poisonHit2 = false;
    }
    public int specialAmount = 100;
    public int poisonDelay = -1;
    public int poisonDamage = 0;
    public int poisonHitDelay = 20;
    public boolean poisonHit = false;
    public boolean poisonHit2 = false;

    public void displayText(String text) {
        forcedPlayerText = text;
        updateRequired = true;
        forcedChatUpdateRequired = true;
    }
    public int TurnTo = 65535;
    public boolean FaceUpdateRequired = false;

    public void TurnPlayerTo(int _TurnTo) {
        TurnTo = _TurnTo;
        updateRequired = true;
        FaceUpdateRequired = true;
    }

    private void appendSetFocusDestination(stream str) {
        if (str != null) {
            str.writeWordBigEndian(TurnTo);
        }
    }
    public int GfxId, GfxDelay, GfxHeight = 0;
    public boolean GraphicsUpdateRequired = false;

    public void appendGraphicsUpdate(stream str) {
        if (str != null) {
            str.writeWordBigEndian(GfxId);
            str.writeDWord(GfxDelay);
            str.writeDWord(GfxHeight);
        }
    }
	public void requestNewGraphic(int graphic, int height, int delay) {
		GfxId = graphic;
		GfxDelay = delay;
		GfxHeight = height;
		GraphicsUpdateRequired = true;
	}	
    public int animationRequest = -1, animationWaitCycles = 0;
    protected boolean animationUpdateRequired = false;

    public void startAnimation(int animIdx, int delay) { //here? ye thats where its called yeah just call that lol no it freezes the person look
        animationRequest = animIdx;
        animationWaitCycles = delay;
        updateRequired = true;
        animationUpdateRequired = true;
    }
    
    public void requestAnimation(int animIdx, int delay) {
        animationRequest = animIdx;
        animationWaitCycles = delay;
        updateRequired = true;
        animationUpdateRequired = true;
    }
    
    public void playAnimation(int animIdx, int delay) {
        animationRequest = animIdx;
        animationWaitCycles = delay;
        updateRequired = true;
        animationUpdateRequired = true;
    }

    public void appendAnimationRequest(stream str) {
        if (str != null) {
            str.writeWordBigEndian((animationRequest == -1) ? 65535 : animationRequest);
            str.writeByteC(animationWaitCycles);
        }
    }

    protected int newWalkCmdX[] = new int[walkingQueueSize];
    protected int newWalkCmdY[] = new int[walkingQueueSize];
    protected int tmpNWCX[] = new int[walkingQueueSize];
    protected int tmpNWCY[] = new int[walkingQueueSize];
    protected int newWalkCmdSteps = 0;
    protected boolean newWalkCmdIsRunning = false;
    protected int travelBackX[] = new int[walkingQueueSize];
    protected int travelBackY[] = new int[walkingQueueSize];
    protected int numTravelBackSteps = 0;

    public abstract void process();

    public abstract boolean PacketProcess();

    public void postProcessing() {
        if (newWalkCmdSteps > 0) {
            int firstX = newWalkCmdX[0], firstY = newWalkCmdY[0];
            int lastDir = 0;
            boolean found = false;
            numTravelBackSteps = 0;
            int OldcurrentX = currentX;
            int OldcurrentY = currentY;
            int ptr = wQueueReadPtr;
            int wayPointX1 = 0, wayPointY1 = 0;
            int dir = Misc.direction(currentX, currentY, firstX, firstY);
            int fails = 0;
            if (dir != -1 && (dir & 1) != 0) {
                do {
                    lastDir = dir;
                    if (--ptr < 0) {
                        ptr = walkingQueueSize - 1;
                    }
                    travelBackX[numTravelBackSteps] = walkingQueueX[ptr];
                    travelBackY[numTravelBackSteps++] = walkingQueueY[ptr];
                    dir = Misc.direction(walkingQueueX[ptr], walkingQueueY[ptr], firstX, firstY);
                    if (lastDir != dir) {
                        found = true;
                        break;
                    }
                } while (ptr != wQueueWritePtr && fails++ < 500);
            } else {
                found = true;
            }
            if (!found) {
                //System.out.println("d");
                teleportToX = absX;
                teleportToY = absY;
                resetWalkingQueue();
                return;
            } else {
                wQueueWritePtr = wQueueReadPtr;
                addToWalkingQueue(currentX, currentY);
                if (dir != -1 && (dir & 1) != 0) {
                    for (int i = 0; i < numTravelBackSteps - 1; i++) {
                        addToWalkingQueue(travelBackX[i], travelBackY[i]);
                    }
                    int wayPointX2 = travelBackX[numTravelBackSteps - 1], wayPointY2 = travelBackY[numTravelBackSteps - 1];
                    if (numTravelBackSteps == 1) {
                        wayPointX1 = currentX;
                        wayPointY1 = currentY;
                    } else {
                        wayPointX1 = travelBackX[numTravelBackSteps - 2];
                        wayPointY1 = travelBackY[numTravelBackSteps - 2];
                    }
                    dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2, wayPointY2);
                    dir >>= 1;
                    found = false;
                    int x = wayPointX1, y = wayPointY1;
                    while (x != wayPointX2 || y != wayPointY2) {
                        x += Misc.directionDeltaX[dir];
                        y += Misc.directionDeltaY[dir];
                        if ((Misc.direction(x, y, firstX, firstY) & 1) == 0) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        addToWalkingQueue(wayPointX1, wayPointY1);
                    }
                } else {
                    for (int i = 0; i < numTravelBackSteps; i++) {
                        addToWalkingQueue(travelBackX[i], travelBackY[i]);
                    }
                }
                for (int i = 0; i < newWalkCmdSteps; i++) {
                    addToWalkingQueue(newWalkCmdX[i], newWalkCmdY[i]);
                }
                isRunning = newWalkCmdIsRunning || isRunning2;
            }
            newWalkCmdSteps = 0;
        }
    }
    public int hitDiff = 0;
    public int hitDiff2 = 0;
    public int hitDiff3 = 0;
    public boolean hitUpdateRequired = false;
    public boolean hitUpdateRequired2 = false;
    public boolean hitUpdateRequired3 = false;
    public boolean IsDead = false;
    public boolean IsDeadTeleporting = false;
    public boolean IsDeadTimer = false;

    private void appendHitUpdate(stream str) {
        if (str != null) {
            if (NewHp < 0) {
                NewHp = 0;
            }
            str.writeByte(hitDiff);
            if (!poisonHit) {
                if (hitDiff > 0) {
                    str.writeByteA(1);
                } else {
                    str.writeByteA(0);
                }
            } else {
                str.writeByteA(2);
            }
            str.writeByteC(NewHp);
            str.writeByte(getLevelForXP(playerXP[3]));
            if (NewHp <= 0) {
                NewHp = 0;
                IsDead = true;
                deathDelay = 4;
                ApplyDead();
            }
        }
    }

    private void appendHitUpdate2(stream str) {
        if (str != null) {
            if (NewHp < 0) {
                NewHp = 0;
            }
            str.writeByte(hitDiff2);
            if (!poisonHit2) {
                if (hitDiff2 > 0) {
                    str.writeByteS(1);
                } else {
                    str.writeByteS(0);
                }
            } else {
                str.writeByteS(2);
            }
            str.writeByte(NewHp);
            str.writeByteC(getLevelForXP(playerXP[3]));
            if (NewHp <= 0) {
                NewHp = 0;
                IsDead = true;
                deathDelay = 4;
                ApplyDead();
            }
        }
    }

    public int getLevelForXP(int exp) {
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl <= 99; lvl++) {
            points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
            output = (int) Math.floor(points / 4);
            if (output >= exp) {
                return lvl;
            }
        }
        return 99;
    }

    public void appendDirUpdate(stream str) {
        if (str != null) {
            if (playerMD != -1) {
                str.writeWord(playerMD);
                playerMD = -1;
            }
        }
    }
    public int viewToX = 0, viewToY = 0;
    public boolean faceObjectRequired = false;

    public void appendObjectUpdate(stream str) {
        str.writeWordBigEndianA(viewToX);
        str.writeWordBigEndian(viewToY);
    }

    public void objectFace(int x, int y) {
        viewToX = ((2 * x) + 1);
        viewToY = ((2 * y) + 1);
        updateRequired = true;
        faceObjectRequired = true;
    }
    public int playerLook[] = new int[6];
    public int playerBonus[] = new int[12];
    public int RecoilRing = 40;
    public int playerMaxHit = 0;
    public int playerSE = 0x328;
    public int playerSEW = 0x333;
    public int playerSER = 0x338;
    public int playerSEA = 0x326;
	public int playerSW = 0x337;
    public int playerMD = -1;
    public int KillerId = 0;
    protected boolean dirUpdateRequired = false;
    public boolean WannePickUp = false;
    public boolean attackingPlayer = false;
    public boolean IsAttackingNPC = false;
    public int attackingPlayerId = -1;
    public int attacknpc = -1;
    public boolean IsShopping = false;
    public int MyShopID = 0;
    public boolean UpdateShop = false;
    public boolean RebuildNPCList = false;
    public int NpcDialogue = 0;
    public int NpcTalkTo = 0;
    public boolean NpcDialogueSend = false;
    public int NpcWanneTalk = 0;
    public int DirectionCount = 0;
    public int TradingWith = 0, TradeStatus = -1;
}
