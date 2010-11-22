package src.innovationx.classic.model.items;

import java.io.*;

import src.innovationx.classic.Server;
import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.Player;
import src.innovationx.classic.util.Misc;

public class ItemHandler {

    public int MaxGroundItems = 9000;
    public int MaxListedItems = 9000;
    public int[] GroundItemId = new int[9001];
    public int[] GroundItemAmt = new int[9001];
    public int[] GroundItemX = new int[9001];
    public int[] GroundItemY = new int[9001];
    public int[] GroundItemTime = new int[9001];
    public int[] DroppedBy = new int[9001];
    public int[] heightLvl = new int[9001];
    public String[] DroppedByName = new String[9001];
    public ItemList ItemList[] = new ItemList[MaxListedItems];

    public ItemHandler() {
        for (int i = 0; i < MaxGroundItems; i++) {
            GroundItemId[i] = -1;
            GroundItemAmt[i] = 0;
            GroundItemX[i] = 0;
            GroundItemY[i] = 0;
            GroundItemTime[i] = 0;
            DroppedBy[i] = 0;
            heightLvl[i] = 0;
            DroppedByName[i] = "";
            ItemList[i] = null;
        }
        loadItemList("item.cfg");
    }

    public void destruct() {
        GroundItemId = null;
        GroundItemAmt = null;
        GroundItemX = null;
        GroundItemY = null;
        GroundItemTime = null;
        DroppedBy = null;
        heightLvl = null;
        DroppedByName = null;
        ItemList = null;
    }
    public int untradable[] = {7959, 7960, 7976, 3840, 3842, 3844, 6950, 2412, 2413, 2414, 2415, 2416, 2417, 8058, 8059, 8060, 8061, 8062, 8063, 2892, 2893, 7454, 7455, 7456, 7457, 7458, 7459, 7460, 7461, 7462, 8013, 8014, 8015, 8016, 8017, 8018, 8080, 8081, 8082, 8083, 8084, 8085, 771, 772, 4031};

    public boolean isUntradable(int item) {
        for (int i = 0; i < untradable.length; i++) {
            if (untradable[i] == item) {
                return true;
            }
        }
        return false;
    }
    public int spawntimer = 1;

    public void LoadItems() {
        spawntimer = 52;
        //CreateNewGroundItemAll(int ItemId, int ItemAmt, int ItemX, int ItemY);
        CreateNewGroundItemAll(8071, 1, 3567, 3412, 0);//Teletabs
        CreateNewGroundItemAll(8071, 1, 3572, 3412, 0);
        CreateNewGroundItemAll(8071, 1, 3241, 9361, 0);
        CreateNewGroundItemAll(8071, 1, 3241, 9367, 0);
        CreateNewGroundItemAll(8071, 1, 2579, 4456, 0);
        CreateNewGroundItemAll(8071, 1, 2808, 9330, 0);
        CreateNewGroundItemAll(8071, 1, 2775, 9304, 0);
        CreateNewGroundItemAll(8071, 1, 2797, 9328, 0);
        CreateNewGroundItemAll(8072, 1, 2209, 4945, 0);
        CreateNewGroundItemAll(8072, 1, 2206, 4945, 0);
        CreateNewGroundItemAll(8071, 1, 2808, 2705, 0);
        CreateNewGroundItemAll(8071, 1, 2802, 2704, 0);
        CreateNewGroundItemAll(8071, 1, 2418, 9617, 0);
    }

    public void process() {
        if (spawntimer > 0) {
            spawntimer -= 1;
        }
        if (spawntimer <= 0) {
            LoadItems();
        }
        for (int i = 0; i < MaxGroundItems; i++) {
            if (GroundItemId[i] > 0 && GroundItemId[i] <= 9000) {
                GroundItemTime[i]++;
                if (GroundItemTime[i] >= 240) {
                    RemoveGroundItemAll(GroundItemId[i], GroundItemX[i], GroundItemY[i]);
                    GroundItemId[i] = -1;
                    GroundItemAmt[i] = 0;
                    GroundItemX[i] = 0;
                    GroundItemY[i] = 0;
                    GroundItemTime[i] = 0;
                    DroppedBy[i] = 0;
                    heightLvl[i] = 0;
                    DroppedByName[i] = "";
                } else if (GroundItemTime[i] == 120) {
                    if (!isUntradable(GroundItemId[i])) {
                        RemoveGroundItemAll(GroundItemId[i], GroundItemX[i], GroundItemY[i]);
                        CreateGroundItemAll(GroundItemId[i], GroundItemAmt[i], GroundItemX[i], GroundItemY[i], i);
                    }
                }
            } else {
                GroundItemId[i] = -1;
            }
        }
    }

    public void ReLoadGroundItems(int playerId) {
        if (playerId <= 0 || Server.s.playerHandler.players[playerId] == null) {
            return;
        }
        Client plr = (Client) Server.s.playerHandler.players[playerId];
        for (int i = 0; i < MaxGroundItems; i++) {
            if (GroundItemId[i] == -1) {
                continue;
            }
            if (plr.WithinDistance(GroundItemX[i], GroundItemY[i], plr.absX, plr.absY, 60) && plr.heightLevel == heightLvl[i]) {
                if (GroundItemTime[i] > 120 && GroundItemTime[i] < 240 && !isUntradable(GroundItemId[i])) {
                    RemoveGroundItemSingle(GroundItemId[i], GroundItemX[i], GroundItemY[i], plr.playerId);
                    CreateGroundItemUn(GroundItemId[i], GroundItemAmt[i], GroundItemX[i], GroundItemY[i], plr.playerId, i);
                }
            }
            if (DroppedByName[i].equalsIgnoreCase(plr.playerName)) {
                RemoveGroundItemSingle(GroundItemId[i], GroundItemX[i], GroundItemY[i], plr.playerId);
                CreateGroundItemUn(GroundItemId[i], GroundItemAmt[i], GroundItemX[i], GroundItemY[i], plr.playerId, i);
            }
        }
    }

    public void RemoveGroundItemSingle(int ItemId, int ItemX, int ItemY, int playerId) {
        if (playerId <= 0 || Server.s.playerHandler.players[playerId] == null) {
            return;
        }
        Client plr = (Client) Server.s.playerHandler.players[playerId];
        if (plr.WithinDistance(ItemX, ItemY, plr.absX, plr.absY, 60)) {
            if (plr.isActive && !plr.disconnected) {
                plr.outStream.createFrame(85);
                plr.outStream.writeByteC((ItemY - 8 * plr.mapRegionY));
                plr.outStream.writeByteC((ItemX - 8 * plr.mapRegionX));
                plr.outStream.createFrame(156);
                plr.outStream.writeByteS(0);
                plr.outStream.writeWord(ItemId);
            }
        }
    }

    public void RemoveGroundItemAll(int ItemId, int ItemX, int ItemY) {
        for (Player p : Server.s.playerHandler.players) {
            if (p != null) {
                Client person = (Client) p;
                if (person.WithinDistance(ItemX, ItemY, person.absX, person.absY, 60)) {
                    if (p != null && person.isActive && !person.disconnected) {
                        person.outStream.createFrame(85);
                        person.outStream.writeByteC((ItemY - 8 * person.mapRegionY));
                        person.outStream.writeByteC((ItemX - 8 * person.mapRegionX));
                        person.outStream.createFrame(156);
                        person.outStream.writeByteS(0);
                        person.outStream.writeWord(ItemId);
                    }
                }
            }
        }
    }

    public void CreateGroundItemAll(int ItemId, int ItemAmt, int ItemX, int ItemY, int i) {
        for (Player p : Server.s.playerHandler.players) {
            if (p != null) {
                Client person = (Client) p;
                if (person.WithinDistance(ItemX, ItemY, person.absX, person.absY, 60) && person.heightLevel == heightLvl[i]) {
                    if (p != null && person.isActive && !person.disconnected) {
                        person.outStream.createFrame(85);
                        person.outStream.writeByteC((ItemY - 8 * person.mapRegionY));
                        person.outStream.writeByteC((ItemX - 8 * person.mapRegionX));
                        person.outStream.createFrame(44);
                        person.outStream.writeWordBigEndianA(ItemId);
                        person.outStream.writeWord(ItemAmt);
                        person.outStream.writeByte(0);
                    }
                }
            }
        }
    }

    public void CreateNewGroundItemAll(int ItemId, int ItemAmt, int ItemX, int ItemY, int height) {
        int slot = 0;
        for (int i = 0; i < MaxGroundItems; i++) {
            if (GroundItemId[i] == -1) {
                GroundItemId[i] = ItemId;
                GroundItemAmt[i] = ItemAmt;
                GroundItemX[i] = ItemX;
                GroundItemY[i] = ItemY;
                GroundItemTime[i] = 120;
                heightLvl[i] = height;
                DroppedBy[i] = 0;
                DroppedByName[i] = "";
                slot = i;
                break;
            }
        }
        for (Player p : Server.s.playerHandler.players) {
            if (p != null) {
                Client person = (Client) p;
                if (person.WithinDistance(ItemX, ItemY, person.absX, person.absY, 60) && person.heightLevel == height) {
                    if (p != null && person.isActive && !person.disconnected) {
                        person.outStream.createFrame(85);
                        person.outStream.writeByteC((ItemY - 8 * person.mapRegionY));
                        person.outStream.writeByteC((ItemX - 8 * person.mapRegionX));
                        person.outStream.createFrame(44);
                        person.outStream.writeWordBigEndianA(ItemId);
                        person.outStream.writeWord(ItemAmt);
                        person.outStream.writeByte(0);
                    }
                }
            }
        }
    }

    public void CreateGroundItemUn(int ItemId, int ItemAmt, int ItemX, int ItemY, int plrId, int i) {
        if (plrId <= 0 || Server.s.playerHandler.players[plrId] == null) {
            return;
        }
        Client person = (Client) Server.s.playerHandler.players[plrId];
        if (person.WithinDistance(ItemX, ItemY, person.absX, person.absY, 60) && person.heightLevel == heightLvl[i]) {
            if (person != null && person.isActive && !person.disconnected) {
                person.outStream.createFrame(85);
                person.outStream.writeByteC((ItemY - 8 * person.mapRegionY));
                person.outStream.writeByteC((ItemX - 8 * person.mapRegionX));
                person.outStream.createFrame(44);
                person.outStream.writeWordBigEndianA(ItemId);
                person.outStream.writeWord(ItemAmt);
                person.outStream.writeByte(0);
            }
        }
    }

    public void PlayerDropItem(int ItemId, int ItemAmt, int ItemX, int ItemY, int Slot, int playerId) {
        if (playerId <= 0 || Server.s.playerHandler.players[playerId] == null) {
            return;
        }
        Client person = (Client) Server.s.playerHandler.players[playerId];
        if ((person.playerItems[Slot] - 1) == ItemId) {
            boolean hasDropped = false;
            for (int i = 0; i < MaxGroundItems; i++) {
                if (GroundItemId[i] == ItemId && GroundItemX[i] == ItemX && GroundItemY[i] == ItemY && Item.itemStackable[ItemId]) {
                    GroundItemAmt[i] += ItemAmt;
                    DroppedBy[i] = playerId;
                    DroppedByName[i] = person.playerName;
                    GroundItemTime[i] = 0;
                    hasDropped = true;
                    break;
                }
            }
            if (!hasDropped) {
                for (int i = 0; i < MaxGroundItems; i++) {
                    if (GroundItemId[i] == -1) {
                        GroundItemId[i] = ItemId;
                        GroundItemAmt[i] = ItemAmt;
                        GroundItemX[i] = ItemX;
                        GroundItemY[i] = ItemY;
                        GroundItemTime[i] = 0;
                        heightLvl[i] = person.heightLevel;
                        DroppedBy[i] = playerId;
                        DroppedByName[i] = person.playerName;
                        break;
                    }
                }
            }
            if (Server.s.playerHandler.players[person.playerId] != null && person.playerId > 0 && person.isActive && !person.disconnected && !hasDropped) {
                person.outStream.createFrame(85);
                person.outStream.writeByteC((ItemY - 8 * person.mapRegionY));
                person.outStream.writeByteC((ItemX - 8 * person.mapRegionX));
                person.outStream.createFrame(44);
                person.outStream.writeWordBigEndianA(ItemId);
                person.outStream.writeWord(ItemAmt);
                person.outStream.writeByte(0);
            }
            person.playerItems[Slot] = 0;
            person.playerItemsN[Slot] = 0;
            person.resetItems(3214);
        }
    }

    public void Deaditems(int ItemId, int ItemAmt, int ItemX, int ItemY, int playerId) {
        boolean hasDropped = false;
        int slot = 0;
        for (int i = 0; i < MaxGroundItems; i++) {
            if (GroundItemId[i] == ItemId && GroundItemX[i] == ItemX && GroundItemY[i] == ItemY && Item.itemStackable[ItemId] && !isUntradable(ItemId)) {
                GroundItemAmt[i] += ItemAmt;
                slot = i;
                GroundItemTime[i] = 0;
                hasDropped = true;
                break;
            }
        }
        if (!hasDropped) {
            for (int i = 0; i < MaxGroundItems; i++) {
                if (GroundItemId[i] == -1) {
                    GroundItemId[i] = ItemId;
                    GroundItemAmt[i] = ItemAmt;
                    GroundItemX[i] = ItemX;
                    GroundItemY[i] = ItemY;
                    GroundItemTime[i] = 0;
                    slot = i;
                    break;
                }
            }
        }
        if (playerId <= 0 || Server.s.playerHandler.players[playerId] == null) {
            return;
        }
        if (playerId > 0 && playerId != -1) {
            Client person = (Client) Server.s.playerHandler.players[playerId];
            if (Server.s.playerHandler.players[person.playerId] != null && person.playerId > 0 && person.isActive && !person.disconnected && !hasDropped) {
                person.outStream.createFrame(85);
                person.outStream.writeByteC((ItemY - 8 * person.mapRegionY));
                person.outStream.writeByteC((ItemX - 8 * person.mapRegionX));
                person.outStream.createFrame(44);
                person.outStream.writeWordBigEndianA(ItemId);
                person.outStream.writeWord(ItemAmt);
                person.outStream.writeByte(0);
                heightLvl[slot] = person.heightLevel;
                DroppedBy[slot] = playerId;
                DroppedByName[slot] = person.playerName;
            }
        }
    }

    public void NpcDropItem(int ItemId, int ItemAmt, int ItemX, int ItemY, int playerId) {
        boolean hasDropped = false;
        int slot = 0;
        for (int i = 0; i < MaxGroundItems; i++) {
            if (GroundItemId[i] == ItemId && GroundItemX[i] == ItemX && GroundItemY[i] == ItemY && Item.itemStackable[ItemId]) {
                RemoveGroundItemAll(ItemId, ItemX, ItemY);
                GroundItemAmt[i] += ItemAmt;
                ItemAmt = GroundItemAmt[i];
                GroundItemTime[i] = 0;
                slot = i;
                hasDropped = true;
                break;
            }
        }
        if (!hasDropped) {
            for (int i = 0; i < MaxGroundItems; i++) {
                if (GroundItemId[i] == -1) {
                    GroundItemId[i] = ItemId;
                    GroundItemAmt[i] = ItemAmt;
                    GroundItemX[i] = ItemX;
                    GroundItemY[i] = ItemY;
                    GroundItemTime[i] = 0;
                    slot = i;
                    break;
                }
            }
        }
        if (playerId <= 0 || Server.s.playerHandler.players[playerId] == null) {
            return;
        }
        Client person = (Client) Server.s.playerHandler.players[playerId];
        if (Server.s.playerHandler.players[person.playerId] != null && person.playerId > 0 && person.isActive && !person.disconnected) {
            person.outStream.createFrame(85);
            person.outStream.writeByteC((ItemY - 8 * person.mapRegionY));
            person.outStream.writeByteC((ItemX - 8 * person.mapRegionX));
            person.outStream.createFrame(44);
            person.outStream.writeWordBigEndianA(ItemId);
            person.outStream.writeWord(ItemAmt);
            person.outStream.writeByte(0);
            DroppedBy[slot] = playerId;
            heightLvl[slot] = person.heightLevel;
            DroppedByName[slot] = person.playerName;
        }
    }

    private void newItemList(int ItemId, String ItemName, String ItemDescription, double ShopValue, double LowAlch, double HighAlch, int Bonuses[]) {
        int slot = -1;
        for (int i = 0; i < MaxListedItems; i++) {
            if (ItemList[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            return;
        }
        ItemList newItemList = new ItemList(ItemId);
        newItemList.itemName = ItemName;
        newItemList.itemDescription = ItemDescription;
        newItemList.ShopValue = ShopValue;
        newItemList.LowAlch = LowAlch;
        newItemList.HighAlch = HighAlch;
        newItemList.Bonuses = Bonuses;
        ItemList[slot] = newItemList;
    }

    public boolean loadItemList(String FileName) {
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
                if (token.equals("item")) {
                    int[] Bonuses = new int[12];
                    for (int i = 0; i < 12; i++) {
                        if (token3[(6 + i)] != null) {
                            Bonuses[i] = Integer.parseInt(token3[(6 + i)]);
                        } else {
                            break;
                        }
                    }
                    newItemList(Integer.parseInt(token3[0]), token3[1].replaceAll("_", " "), token3[2].replaceAll("_", " "), Double.parseDouble(token3[3]), Double.parseDouble(token3[4]), Double.parseDouble(token3[5]), Bonuses);
                }
            } else {
                if (line.equals("[ENDOFITEMLIST]")) {
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
