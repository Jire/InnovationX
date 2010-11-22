package src.innovationx.classic.content.minigames;

import src.innovationx.classic.Server;
import src.innovationx.classic.model.npc.NPC;
import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.Player;
import src.innovationx.classic.util.Misc;

public class PestControl {

    public final int GAME_LENGTH = 600;
    public int gameTime = 0;
    public boolean[] portalAlive = null;
    public int playersInGame = 0;
    public int gameStartDelay = 0;
    public int[] xNPC = new int[100];
    public int[] yNPC = new int[100];
    public int[] idNPC = new int[100];
    public int[] slotNPC = new int[100];

    public PestControl() {
        portalAlive = new boolean[4];
        for (int p = 0; p < 4; p++) {
            portalAlive[p] = true;
        }
        for (int p = 0; p < idNPC.length; p++) {
            xNPC[p] = 0;
            yNPC[p] = 0;
            idNPC[p] = 0;
        }
    }

    public void process() {
        gameTime--;
        gameStartDelay--;
        checkStatus();
        if (gameTime < -2) {
            attemptNewGame();
        }
    }

    public int getNPCHp(int type) {
        for (int n = 0; n < Server.s.npcHandler.maxNPCs; n++) {
            NPC npc = Server.s.npcHandler.npcs[n];
            if (npc != null) {
                if (npc.NpcType == type) {
                    return npc.CurrentHp;
                }
            }
        }
        return 0;
    }

    public void checkStatus() {
        if (gameTime > 0) {
            if (!portalAlive[0] && !portalAlive[1] && !portalAlive[2] && !portalAlive[3]) {
                gameWon();
                return;
            }
            checkDeadNPCs();
        }
        if (gameTime == 0) {
            gameLost();
        }
        if (gameTime < 0) {
            returnPlayers();
        }
    }

    public void returnPlayers() {
        for (int p = 0; p < Server.s.playerHandler.maxPlayers; p++) {
            Player i = Server.s.playerHandler.players[p];
            if (i != null) {
                if (i.pcStatus == 2 || pcGameArea2(i.absX, i.absY)) {
                    i.teleportToX = 2657;
                    i.teleportToY = 2639;
                }
            }
        }
    }

    public void gameLost() {
        killOffOldNPCs();
        gameTime = 0;
        gameStartDelay = 30;
        for (int p = 0; p < Server.s.playerHandler.maxPlayers; p++) {
            if (Server.s.playerHandler.players[p] != null) {
                if (Server.s.playerHandler.players[p].pcStatus == 2 || pcGameArea(Server.s.playerHandler.players[p].absX, Server.s.playerHandler.players[p].absY)) {
                    Client plr = (Client) Server.s.playerHandler.players[p];
                    plr.teleportToX = 2657;
                    plr.teleportToY = 2639;
                    plr.pcStatus = 0;
                    plr.sendMessage("Oh dear you lost in pest control!");
                    plr.SetWalkableFrame(4535);
                    plr.updateHp(plr.getLevelForXP(plr.playerXP[3]), true);
                    plr.resetAllPrayers();
                    for (int i = 0; i < 21; i++) {
                        plr.playerLevel[i] = plr.getLevelForXP(plr.playerXP[i]);
                    }
                    for (int i = 0; i < 21; i++) {
                        plr.setSkillLevel(i);
                    }
                    plr.specialAmount = 100;
                    if (plr.needsSpecBar(plr.playerEquipment[plr.playerWeapon])) {
                        plr.daggerBar();
                    }
                    plr.skullTimer = -1;
                    plr.SetPkHeadIcon(0);
                    plr.EntangleDelay = 0;
                    plr.poisonDelay = -1;
                }
            }
        }
    }

    public void gameWon() {
        gameTime = 0;
        gameStartDelay = 30;
        killOffOldNPCs();
        for (int p = 0; p < Server.s.playerHandler.maxPlayers; p++) {
            if (Server.s.playerHandler.players[p] != null) {
                if (Server.s.playerHandler.players[p].pcStatus == 2/* || pcGameArea(Server.s.playerHandler.players[p].absX, Server.s.playerHandler.players[p].absY)*/) {
                    Client plr = (Client) Server.s.playerHandler.players[p];
                    plr.teleportToX = 2657;
                    plr.teleportToY = 2639;
                    plr.showDialogue("You conquered pest control!");
                    plr.sendMessage("You conquered pest control!");
                    if (plr.damageDone >= 25 && plr.pcStatus == 2) {
						if (plr.isExtremeMember == 1)
							plr.pcPoints += 30;
						else if (plr.isMember == 1)
							plr.pcPoints += 20;
						else
							plr.pcPoints += 10;
                    } else {
                        plr.sendMessage("Next time please actually play to get points!");
                        plr.sendMessage("You must cause atleast a total damage of 50.");
                    }
                    plr.SetWalkableFrame(4535);
                    plr.updateHp(plr.getLevelForXP(plr.playerXP[3]), true);
                    plr.resetAllPrayers();
                    for (int i = 0; i < 21; i++) {
                        plr.playerLevel[i] = plr.getLevelForXP(plr.playerXP[i]);
                    }
                    for (int i = 0; i < 21; i++) {
                        plr.setSkillLevel(i);
                    }
                    plr.specialAmount = 100;
                    if (plr.needsSpecBar(plr.playerEquipment[plr.playerWeapon])) {
                        plr.daggerBar();
                    }
                    plr.skullTimer = -1;
                    plr.SetPkHeadIcon(0);
                    plr.EntangleDelay = 0;
                    plr.poisonDelay = -1;
                    plr.pcStatus = 0;
                }
            }
        }
    }

    public boolean pcGameArea(int x, int y) {
        return (x >= 2610 && x <= 2695 && y >= 2550 && y <= 2625 || x >= 2654 && x <= 2659 && y >= 2638 && y <= 2641);
    }

    public boolean pcGameArea2(int x, int y) {
        return (x >= 2610 && x <= 2695 && y >= 2550 && y <= 2625);
    }

    public void attemptNewGame() {
        playersInGame = 0;
        for (int p = 1; p < Server.s.playerHandler.maxPlayers; p++) {
            if (Server.s.playerHandler.players[p] != null) {
                if (Server.s.playerHandler.players[p].isActive && Server.s.playerHandler.players[p].pcStatus == 1) {
                    playersInGame++;
                }
            }
        }
        if (gameStartDelay <= 0 && playersInGame >= 5) {
            newGame();
        }
        if (gameStartDelay <= 0 && playersInGame < 5) {
            gameStartDelay = 30;
        }
    }

    public void newGame() {
        gameTime = GAME_LENGTH;
        for (int p = 0; p < 4; p++) {
            portalAlive[p] = true;
        }
        reloadNPCs();
        sendPlayers();
    }

    public void reloadNPCs() {
        Server.s.npcHandler.newNPC(3782, 2656, 2592, 0, 0, 0, 0, 0, false, -1); //Void Knight
        Server.s.npcHandler.newNPC(3778, 2681, 2589, 0, 0, 0, 0, 0, false, -1); //Blue Portal
        Server.s.npcHandler.newNPC(3779, 2670, 2571, 0, 0, 0, 0, 0, false, -1); //Yellow Portal
        Server.s.npcHandler.newNPC(3780, 2646, 2570, 0, 0, 0, 0, 0, false, -1); //Pink/Red Portal
        Server.s.npcHandler.newNPC(3777, 2628, 2596, 0, 0, 0, 0, 0, false, -1); //Purple Portal
        createNPC(3761, 2629, 2598); //Torcher
        createNPC(3771, 2631, 2588); //Defiler
        createNPC(3776, 2638, 2592); //Brawler
        createNPC(3771, 2634, 2596); //Defiler
        createNPC(3761, 2638, 2586); //Torcher
        createNPC(3776, 2641, 2577); //Brawler
        createNPC(3771, 2643, 2565); //Defiler
        createNPC(3761, 2650, 2566); //Torcher
        createNPC(3776, 2651, 2570); //Brawler
        createNPC(3771, 2648, 2574); //Defiler
        createNPC(3761, 2640, 2572); //Torcher
        createNPC(3761, 2674, 2571); //Torcher
        createNPC(3771, 2666, 2573); //Defiler
        createNPC(3776, 2670, 2565); //Brawler
        createNPC(3776, 2673, 2577); //Brawler
        createNPC(3761, 2662, 2570); //Torcher
        createNPC(3771, 2657, 2573); //Defiler
        createNPC(3776, 2657, 2566); //Brawler
        createNPC(3771, 2680, 2575); //Defiler
        createNPC(3771, 2684, 2590); //Defiler
        createNPC(3776, 2678, 2593); //Brawler
        createNPC(3761, 2675, 2592); //Torcher
        createNPC(3776, 2678, 2582); //Brawler
        createNPC(3771, 2673, 2585); //Defiler
        createNPC(3771, 2678, 2597); //Defiler
        createNPC(3761, 2676, 2602); //Torcher
        createNPC(3761, 2678, 2589); //Torcher
        createNPC(3776, 2658, 2580); //Brawler
        createNPC(3761, 2645, 2574); //Torcher
        createNPC(3776, 2645, 2565); //Brawler
        createNPC(3771, 2635, 2576); //Defiler
        createNPC(3761, 2633, 2570); //Torcher
        createNPC(3761, 2627, 2589); //Torcher
        createNPC(3776, 2630, 2596); //Brawler
        createNPC(3751, 2627, 2594); //Spinner
        createNPC(3751, 2629, 2589); //Spinner
        createNPC(3751, 2648, 2568); //Spinner
        createNPC(3751, 2643, 2571); //Spinner
        createNPC(3751, 2672, 2574); //Spinner
        createNPC(3751, 2666, 2570); //Spinner
        createNPC(3751, 2681, 2593); //Spinner
        createNPC(3751, 2681, 2585); //Spinner
        createNPC(3741, 2654, 2590); //Shifter
        createNPC(3741, 2654, 2595); //Shifter
        createNPC(3741, 2659, 2595); //Shifter
        createNPC(3741, 2659, 2590); //Shifter
        createNPC(3741, 2659, 2592); //Shifter
        createNPC(3741, 2654, 2593); //Shifter
    }

    public void checkDeadNPCs() {
        for (int i = 0; i < idNPC.length; i++) {
            if (Server.s.npcHandler.npcs[slotNPC[i]] == null && idNPC[i] > 0) {
                if (Misc.random(100) == 50) {
                    createNPC(idNPC[i], xNPC[i], yNPC[i]);
                }
            }
        }
    }

    public void createNPC(int id, int x, int y) {
        int slot = Server.s.npcHandler.newNPC(id, x, y, 0, 0, 0, 0, 0, false, -1);
        addNPCToList(id, slot, x, y);
        createNpcGfx(188, 0, slot, 0);
    }

    public void removeFromList(int slot) {
        xNPC[slot] = 0;
        yNPC[slot] = 0;
        slotNPC[slot] = 0;
    }

    public void createNpcGfx(int Id, int Delay, int i, int Height) {
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

    public void addNPCToList(int id, int slot, int x, int y) {
        for (int i = 0; i < idNPC.length; i++) {
            if (Server.s.npcHandler.npcs[slotNPC[i]] == null) {
                xNPC[i] = x;
                yNPC[i] = y;
                idNPC[i] = id;
                slotNPC[i] = slot;
                break;
            }
        }
    }

    public void killOffOldNPCs() {
        for (int n = 0; n < Server.s.npcHandler.maxNPCs; n++) {
            NPC npc = Server.s.npcHandler.npcs[n];
            if (npc != null) {
                if (npc.NpcType >= 3730 && npc.NpcType <= 3782) {
                    npc.CurrentHp = 0;
                    npc.IsDead = true;
                }
            }
        }
    }

    public void sendPlayers()
    {
        for (int p = 0; p < Server.s.playerHandler.maxPlayers; p++)
        {
            if (Server.s.playerHandler.players[p] != null)
            {
                if (Server.s.playerHandler.players[p].isActive && Server.s.playerHandler.players[p].pcStatus == 1)
                {
                    Client plr = (Client) Server.s.playerHandler.players[p];
                    plr.damageDone = 0;
                    plr.pcStatus = 2;
                    plr.teleportToX = 2656+Misc.random(3);
                    plr.teleportToY = 2614-Misc.random(5);
                    plr.SetWalkableFrame(20000);
                }
            }
        }
    }
}
