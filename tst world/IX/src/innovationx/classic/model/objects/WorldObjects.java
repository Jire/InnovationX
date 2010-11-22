package src.innovationx.classic.model.objects;

import java.io.*;

import src.innovationx.classic.Server;
import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.util.Misc;

public class WorldObjects {

    public WorldObjects() {
        objectId = new int[MAX_OBJECTS];
        objectX = new int[MAX_OBJECTS];
        objectY = new int[MAX_OBJECTS];
        objectHealth = new int[MAX_OBJECTS];
        replaceObject = new int[MAX_OBJECTS];
        objRestoreDelay = new int[MAX_OBJECTS];
        objectFace = new int[MAX_OBJECTS];
        objectType = new int[MAX_OBJECTS];
        for (int i = 0; i < MAX_OBJECTS; i++) {
            objectId[i] = 0;
            objectX[i] = 0;
            objectY[i] = 0;
            objectHealth[i] = 0;
            replaceObject[i] = 0;
            objRestoreDelay[i] = 0;
            objectType[i] = 10;
            objectFace[i] = 0;
        }
        loadobjectList("Objects.cfg");
    }

    public void process() {
        for (int i = 0; i < MAX_OBJECTS; i++) {
            if (objectId[i] != 0) {
                if (objRestoreDelay[i] == 0) {
                    resetObject(i);
                    continue;
                } else if (objectHealth[i] == 0) {
                    objectHealth[i] = -1;
                    createDeadObject(i);
                    continue;
                } else if (objectHealth[i] == -1) {
                    objRestoreDelay[i]--;
                }
            }
        }
    }

    public void loadObjects(int playerId) {
        if (playerId <= 0 || Server.s.playerHandler.players[playerId] == null) {
            return;
        }
        Client plr = (Client) Server.s.playerHandler.players[playerId];
        for (int i = 0; i < MAX_OBJECTS; i++) {
            if (plr.WithinDistance(objectX[i], objectY[i], plr.absX, plr.absY, 60)) {
                if (objectHealth[i] < 0) {
                    plr.CreateObject(objectX[i], objectY[i], replaceObject[i], objectType[i], objectFace[i], 0);
                }
            }
        }
    }

    public void resetObject(int i) {
        for (int p = 1; p < Server.s.playerHandler.maxPlayers; p++) {
            if (Server.s.playerHandler.players[p] != null) {
                Client plr = (Client) Server.s.playerHandler.players[p];
                plr.CreateObject(objectX[i], objectY[i], objectId[i], objectType[i], objectFace[i], 0);
            }
        }
        objectId[i] = 0;
        objectX[i] = 0;
        objectY[i] = 0;
        objectHealth[i] = 0;
        replaceObject[i] = 0;
        objRestoreDelay[i] = 0;
        objectType[i] = 0;
        objectFace[i] = 10;
    }

    public void createDeadObject(int i) {
        for (int p = 1; p < Server.s.playerHandler.maxPlayers; p++) {
            if (Server.s.playerHandler.players[p] != null) {
                Client plr = (Client) Server.s.playerHandler.players[p];
                plr.CreateObject(objectX[i], objectY[i], replaceObject[i], objectType[i], objectFace[i], 0);
            }
        }
    }

    public void lowerHealth(int damage, int objectId2, int x, int y) {
        for (int i = 0; i < MAX_OBJECTS; i++) {
            if (objectId[i] == objectId2) {
                if (objectX[i] == x && objectY[i] == y) {
                    objectHealth[i] -= damage;
                    if (objectHealth[i] < 0) {
                        objectHealth[i] = 0;
                    }
                    break;
                }
            }
        }
    }

    public boolean checkIfAlive(int objectId2, int x, int y) {
        for (int i = 0; i < MAX_OBJECTS; i++) {
            if (objectId[i] == objectId2) {
                if (objectX[i] == x && objectY[i] == y) {
                    if (objectHealth[i] > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void checkIfExists(int objectId2, int x, int y, int type, int face) {
        for (int i = 0; i < MAX_OBJECTS; i++) {
            if (objectId[i] == objectId2) {
                if (objectX[i] == x && objectY[i] == y) {
                    return;
                }
            }
        }
        createNewObject(objectId2, x, y, getMaxHealth(objectId2), getNewObj(objectId2), getRestoreDelay(objectId2), type, face);
    }

    public int getMaxHealth(int objectId2) {
        switch (objectId2) {
            case 8967:
                return 1;
            case 8958:
            case 8959:
            case 8960:
                return 1;
            case 733:
                return 1;
            case 2646:
                return 16;
            case 5959:
            case 5960:
            case 1815:
            case 1814:
                return 1;
            case 1315:
            case 1316:
            case 1307:
            case 1281:
            case 1308:
            case 1309:
            case 1306:
            case 5551:
            case 5552:
            case 5553:
            case 1278:
            case 1276:
                return 10;
            case 2095:
            case 2090:
            case 2091:
            case 9708:
            case 2094:
            case 9714:
            case 2093:
            case 9717:
            case 2096:
            case 2097:
            case 2092:
            case 14850:
            case 452:
            case 450:
            case 2103:
            case 2102:
            case 14853:
            case 2104:
            case 2105:
            case 14862:
            case 14859:
            case 9720:
            case 2098:
            case 2099:
            case 14860:
            case 3403:
            case 2106:
            case 2107:
                return 2;
        }
        System.out.println(objectId2 + " not supported.");
        return 0;
    }

    public int getNewObj(int objectId2) {
        switch (objectId2) {
            case 8967:
                return 6951;
            case 8958:
            case 8959:
            case 8960:
                return 8962;
            case 733:
                return 734;
            case 2646:
                return 6951;
            case 5959:
            case 5960:
            case 1815:
            case 1814:
                return 161;
            case 1315:
            case 1316:
            case 1307:
            case 1281:
            case 1308:
            case 1309:
            case 1306:
            case 5551:
            case 5552:
            case 5553:
            case 1278:
            case 1276:
                return 1341;
            case 2095:
            case 2090:
            case 2091:
            case 9708:
            case 2094:
            case 9714:
            case 2093:
            case 9717:
            case 2096:
            case 2097:
            case 2092:
            case 14850:
            case 452:
            case 450:
            case 2103:
            case 2102:
            case 14853:
            case 2104:
            case 2105:
            case 14862:
            case 14859:
            case 9720:
            case 2098:
            case 2099:
            case 14860:
            case 3403:
            case 2106:
            case 2107:
                return 14832;
        }
        System.out.println(objectId2 + " not supported.");
        return 0;
    }

    public int getRestoreDelay(int objectId2) {
        switch (objectId2) {
            case 8967:
                return 60;
            case 8958:
            case 8959:
            case 8960:
                return 16;
            case 733:
                return 30;
            case 2646:
                return 10;
            case 5959:
            case 5960:
            case 1815:
            case 1814:
                return 8;
            case 1315:
            case 1316:
            case 3403:
                return 40;
            case 1307:
                return 80;
            case 1281:
            case 1308:
            case 5551:
            case 5552:
            case 5553:
            case 1278:
            case 1276:
                return 26;
            case 1309:
                return 180;
            case 1306:
                return 380;
            case 2090:
            case 2091:
            case 2094:
            case 2095:
            case 9708:
            case 9714:
                return 7;
            case 2093:
            case 2092:
            case 9717:
                return 18;
            case 2096:
            case 2097:
            case 14850:
            case 452:
            case 450:
                return 30;
            case 9720:
            case 2098:
            case 2099:
                return 50;
            case 2103:
            case 2102:
            case 14853:
                return 85;
            case 2104:
            case 2105:
            case 14862:
                return 120;
            case 14859:
            case 14860:
            case 2106:
            case 2107:
                return 200;
        }
        System.out.println(objectId2 + " not supported.");
        return 0;
    }

    public void createNewObject(int objectId2, int x, int y, int health, int turnsInto, int restoreDelay, int type, int face) {
        int slot = -1;
        for (int i = 0; i < MAX_OBJECTS; i++) {
            if (objectId[i] == 0) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            return;
        }
        objectId[slot] = objectId2;
        objectX[slot] = x;
        objectY[slot] = y;
        objectHealth[slot] = health;
        replaceObject[slot] = turnsInto;
        objRestoreDelay[slot] = restoreDelay;
        objectType[slot] = type;
        objectFace[slot] = face;
    }

    public boolean loadobjectList(String FileName) {
        String line = "";
        String token = "";
        String token2 = "";
        String token2_2 = "";
        String[] token3 = new String[10];
        boolean EndOfFile = false;
        int ReadMode = 0;
        int slot = 0;
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
        while (!EndOfFile && line != null) {
            line = line.trim();
            int spot = line.indexOf("=");
            if (spot > -1) {
                token = line.substring(0, spot);
                token = token.trim();
                token2 = line.substring(spot + 1);
                token2 = token2.trim();
                token2_2 = token2.replaceAll("\t\t", "\t");
                token3 = token2_2.split("\t");
                if (token.equals("object")) {
                    blockX[slot] = Integer.parseInt(token3[0]);
                    blockY[slot] = Integer.parseInt(token3[1]);
                    slot++;
                }
            } else {
                if (line.equals("[ENDOFOBJECTLIST]")) {
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
                ioexception1.printStackTrace();
            }
        }
        try {
            characterfile.close();
        } catch (IOException ioexception1) {
        }
        return false;
    }

    public boolean canWalkTo(int x, int y, int xInc, int yInc) {
        for (int i = 0; i < MAX_CFG_OBJECTS; i++) {
            if (x == blockX[i] && y == blockY[i] || (x + xInc) == blockX[i] && (y + yInc) == blockY[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean canShootThrough(int x, int y, int ox, int oy) {
        /*int disX = 0, disY = 0;
        if (x > ox)
        {
        disX = x - ox;
        }
        else if (ox > x)
        {
        disX = -(ox - x);
        }
        if (y > oy)
        {
        disY = y - oy;
        }
        else if (oy > y)
        {
        disY = -(oy - y);
        }
        for (int i = 0; i < MAX_CFG_OBJECTS; i++)
        {
        int indexX = disX;
        int indexY = disY;
        for (int j = 0; j < disX + disY; j++)
        {
        System.out.println("x = " + (x + indexX) + ", y = " + (y + indexY));
        if ((x + indexX) == blockX[i] && (y + indexY) == blockY[i])
        {
        return false;
        }
        if (disX == 0)
        indexX--;
        if (disY == 0)
        indexY--;
        }
        }*/
        return true;
    }
    public final int MAX_OBJECTS = 200;
    public final int MAX_CFG_OBJECTS = 350;
    public int[] blockX = new int[MAX_CFG_OBJECTS];
    public int[] blockY = new int[MAX_CFG_OBJECTS];
    public int[] objectId = null;
    public int[] objectX = null;
    public int[] objectY = null;
    public int[] objectFace = null;
    public int[] objectType = null;
    public int[] objectHealth = null;
    public int[] replaceObject = null;
    public int[] objRestoreDelay = null;
}
