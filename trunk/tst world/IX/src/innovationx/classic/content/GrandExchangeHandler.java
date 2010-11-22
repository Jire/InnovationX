package src.innovationx.classic.content;
/**
 * Grand Exchange
 * @author Bulby Strife
 * @author Canownueasy
 */

import java.io.*;

import src.innovationx.classic.util.Misc;

public class GrandExchangeHandler {

	public int Item1 = 0;
	public int Item2 = 0;
	public int Item3 = 0;
	public int Item4 = 0;
	public int Item5 = 0;
	public int Item6 = 0;
	public int Item7 = 0;
	public int Item8 = 0;
	public int Item9 = 0;
	public int Item10 = 0;
	public int Item11 = 0;
	public int Item12 = 0;
	public int Item13 = 0;
	public int Item14 = 0;
	public int Item15 = 0;
	public int Item16 = 0;
	public int Item17 = 0;
	public int Item18 = 0;
	public int Item19 = 0;
	public int Item20 = 0;

	public int Item1Amount = 0;
	public int Item2Amount = 0;
	public int Item3Amount = 0;
	public int Item4Amount = 0;
	public int Item5Amount = 0;
	public int Item6Amount = 0;
	public int Item7Amount = 0;
	public int Item8Amount = 0;
	public int Item9Amount = 0;
	public int Item10Amount = 0;
	public int Item11Amount = 0;
	public int Item12Amount = 0;
	public int Item13Amount = 0;
	public int Item14Amount = 0;
	public int Item15Amount = 0;
	public int Item16Amount = 0;
	public int Item17Amount = 0;
	public int Item18Amount = 0;
	public int Item19Amount = 0;
	public int Item20Amount = 0;

	public String Owner = "";
	public String Item1Owner = "";
	public String Item2Owner = "";
	public String Item3Owner = "";
	public String Item4Owner = "";
	public String Item5Owner = "";
	public String Item6Owner = "";
	public String Item7Owner = "";
	public String Item8Owner = "";
	public String Item9Owner = "";
	public String Item10Owner = "";
	public String Item11Owner = "";
	public String Item12Owner = "";
	public String Item13Owner = "";
	public String Item14Owner = "";
	public String Item15Owner = "";
	public String Item16Owner = "";
	public String Item17Owner = "";
	public String Item18Owner = "";
	public String Item19Owner = "";
	public String Item20Owner = "";

	public int Spot1 = 995;
	public int Spot1Amount = 0;
	public int Spot2 = 0;
	public int Spot2Amount = 0;

	public void GiveMoney(int ItemID, String Ownerr, int Amount)
	{
		Owner = Ownerr;
		if(ItemID == Item1)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item1 = 0;
			Item1Amount = 0;
			savegrandex();
		}
		if(ItemID == Item2)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item2 = 0;
			Item2Amount = 0;
			savegrandex();
		}
		if(ItemID == Item3)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item3 = 0;
			Item3Amount = 0;
			savegrandex();
		}
		if(ItemID == Item4)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item4 = 0;
			Item4Amount = 0;
			savegrandex();
		}
		if(ItemID == Item5)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item5 = 0;
			Item5Amount = 0;
			savegrandex();
		}
		if(ItemID == Item6)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item6 = 0;
			Item6Amount = 0;
			savegrandex();
		}
		if(ItemID == Item7)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item7 = 0;
			Item7Amount = 0;
			savegrandex();
		}
		if(ItemID == Item8)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item8 = 0;
			Item8Amount = 0;
			savegrandex();
		}
		if(ItemID == Item9)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item9 = 0;
			Item9Amount = 0;
			savegrandex();
		}
		if(ItemID == Item10)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item10 = 0;
			Item10Amount = 0;
			savegrandex();
		}
		if(ItemID == Item11)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item11 = 0;
			Item11Amount = 0;
			savegrandex();
		}
		if(ItemID == Item12)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item12 = 0;
			Item12Amount = 0;
			savegrandex();
		}
		if(ItemID == Item13)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item13 = 0;
			Item13Amount = 0;
			savegrandex();
		}
		if(ItemID == Item14)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item14 = 0;
			Item14Amount = 0;
			savegrandex();
		}
		if(ItemID == Item15)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item15 = 0;
			Item15Amount = 0;
			savegrandex();
		}
		if(ItemID == Item16)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item16 = 0;
			Item16Amount = 0;
			savegrandex();
		}
		if(ItemID == Item17)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item17 = 0;
			Item17Amount = 0;
			savegrandex();
		}
		if(ItemID == Item18)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item18 = 0;
			Item18Amount = 0;
			savegrandex();
		}
		if(ItemID == Item19)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item1 = 0;
			Item19Amount = 0;
			savegrandex();
		}
		if(ItemID == Item20)
		{
			Spot1 = 995;
			Spot1Amount = Amount;
			Item20 = 0;
			Item20Amount = 0;
			savegrandex();
		}
		SaveItems();
	}

	public boolean savegrandex() {
		BufferedWriter characterfile = null;
		try {
			characterfile = new BufferedWriter(new FileWriter("./PlayerGrand/"+Owner+".txt"));
			characterfile.write("[GRAND EXCHANGE]", 0, 16);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("Spot1 = ", 0, 8);
			characterfile.write(Integer.toString(Spot1), 0, Integer.toString(Spot1).length());
			characterfile.newLine();
			characterfile.write("Spot2 = ", 0, 8);
			characterfile.write(Integer.toString(Spot2), 0, Integer.toString(Spot2).length());
			characterfile.newLine();
			characterfile.write("Spot1Amount = ", 0, 14);
			characterfile.write(Integer.toString(Spot1Amount), 0, Integer.toString(Spot1Amount).length());
			characterfile.newLine();
			characterfile.write("Spot2Amount = ", 0, 14);
			characterfile.write(Integer.toString(Spot2Amount), 0, Integer.toString(Spot2Amount).length());
			
			characterfile.close();
		} catch(IOException ioexception) {
			Misc.println("[GRAND EXCHANGE]: Error writing file for " + Owner + "!");
			return false;
		}
		return true;
	}

	public void addnewitem(int ItemID, int ItemAmount, String Owner)
	{
		if(Item1 == 0)
		{
			Item1 = ItemID;
			Item1Amount = ItemAmount;
			Item1Owner = Owner;
		}
		else
		if(Item2 == 0)
		{
			Item2 = ItemID;
			Item2Amount = ItemAmount;
			Item2Owner = Owner;
		}
		else
		if(Item3 == 0)
		{
			Item3 = ItemID;
			Item3Amount = ItemAmount;
			Item3Owner = Owner;
		}
		else
		if(Item4 == 0)
		{
			Item4 = ItemID;
			Item4Amount = ItemAmount;
			Item4Owner = Owner;
		}
		else
		if(Item5 == 0)
		{
			Item5 = ItemID;
			Item5Amount = ItemAmount;
			Item5Owner = Owner;
		}
		else
		if(Item6 == 0)
		{
			Item6 = ItemID;
			Item6Amount = ItemAmount;
			Item6Owner = Owner;
		}
		else
		if(Item7 == 0)
		{
			Item7 = ItemID;
			Item7Amount = ItemAmount;
			Item7Owner = Owner;
		}
		else
		if(Item8 == 0)
		{
			Item8 = ItemID;
			Item8Amount = ItemAmount;
			Item8Owner = Owner;
		}
		else
		if(Item9 == 0)
		{
			Item9 = ItemID;
			Item9Amount = ItemAmount;
			Item9Owner = Owner;
		}
		else
		if(Item10 == 0)
		{
			Item10 = ItemID;
			Item10Amount = ItemAmount;
			Item10Owner = Owner;
		}
		else
		if(Item11 == 0)
		{
			Item11 = ItemID;
			Item11Amount = ItemAmount;
			Item11Owner = Owner;
		}
		else
		if(Item12 == 0)
		{
			Item12 = ItemID;
			Item12Amount = ItemAmount;
			Item12Owner = Owner;
		}
		else
		if(Item13 == 0)
		{
			Item13 = ItemID;
			Item13Amount = ItemAmount;
			Item13Owner = Owner;
		}
		else
		if(Item14 == 0)
		{
			Item14 = ItemID;
			Item14Amount = ItemAmount;
			Item14Owner = Owner;
		}
		else
		if(Item15 == 0)
		{
			Item15 = ItemID;
			Item15Amount = ItemAmount;
			Item15Owner = Owner;
		}
		else
		if(Item16 == 0)
		{
			Item16 = ItemID;
			Item16Amount = ItemAmount;
			Item16Owner = Owner;
		}
		else
		if(Item17 == 0)
		{
			Item17 = ItemID;
			Item17Amount = ItemAmount;
			Item17Owner = Owner;
		}
		else
		if(Item18 == 0)
		{
			Item18 = ItemID;
			Item18Amount = ItemAmount;
			Item18Owner = Owner;
		}
		else
		if(Item19 == 0)
		{
			Item19 = ItemID;
			Item19Amount = ItemAmount;
			Item19Owner = Owner;
		}
		else
		if(Item20 == 0)
		{
			Item20 = ItemID;
			Item20Amount = ItemAmount;
			Item20Owner = Owner;
		}

		SaveItems();
	}


	public void removenewitem(int ItemID)
	{
		if(Item1 == ItemID)
		{
			Item1 = 0;
			Item1Amount = 0;
		}
		else
		if(Item2 == ItemID)
		{
			Item2 = 0;
			Item2Amount = 0;
		}
		else
		if(Item3 == ItemID)
		{
			Item3 = 0;
			Item3Amount = 0;
		}
		else
		if(Item4 == ItemID)
		{
			Item4 = 0;
			Item4Amount = 0;
		}
		else
		if(Item5 == ItemID)
		{
			Item5 = 0;
			Item5Amount = 0;
		}
		else
		if(Item6 == ItemID)
		{
			Item6 = 0;
			Item6Amount = 0;
		}
		else
		if(Item7 == ItemID)
		{
			Item7 = 0;
			Item7Amount = 0;
		}
		else
		if(Item8 == ItemID)
		{
			Item8 = 0;
			Item8Amount = 0;
		}
		else
		if(Item9 == ItemID)
		{
			Item9 = 0;
			Item9Amount = 0;
		}
		else
		if(Item10 == ItemID)
		{
			Item10 = 0;
			Item10Amount = 0;
		}
		else
		if(Item11 == ItemID)
		{
			Item11 = 0;
			Item11Amount = 0;
		}
		else
		if(Item12 == ItemID)
		{
			Item12 = 0;
			Item12Amount = 0;
		}
		else
		if(Item13 == ItemID)
		{
			Item13 = 0;
			Item13Amount = 0;
		}
		else
		if(Item14 == ItemID)
		{
			Item14 = 0;
			Item14Amount = 0;
		}
		else
		if(Item15 == ItemID)
		{
			Item15 = 0;
			Item15Amount = 0;
		}
		else
		if(Item16 == ItemID)
		{
			Item16 = 0;
			Item16Amount = 0;
		}
		else
		if(Item17 == ItemID)
		{
			Item17 = 0;
			Item17Amount = 0;
		}
		else
		if(Item18 == ItemID)
		{
			Item18 = 0;
			Item18Amount = 0;
		}
		else
		if(Item19 == ItemID)
		{
			Item19 = 0;
			Item19Amount = 0;
		}
		else
		if(Item20 == ItemID)
		{
			Item20 = 0;
			Item20Amount = 0;
		}
		SaveItems();
	}

	public boolean SaveItems()
	{
		BufferedWriter characterfile = null;
		try {
			characterfile = new BufferedWriter(new FileWriter("./GrandExchange.txt"));
			characterfile.write("[ITEMSFORSALE]", 0, 14);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("Item1 = ", 0, 8);
			characterfile.write(Integer.toString(Item1), 0, Integer.toString(Item1).length());
			characterfile.newLine();
			characterfile.write("Item2 = ", 0, 8);
			characterfile.write(Integer.toString(Item2), 0, Integer.toString(Item2).length());
			characterfile.newLine();
			characterfile.write("Item3 = ", 0, 8);
			characterfile.write(Integer.toString(Item3), 0, Integer.toString(Item3).length());
			characterfile.newLine();
			characterfile.write("Item4 = ", 0, 8);
			characterfile.write(Integer.toString(Item4), 0, Integer.toString(Item4).length());
			characterfile.newLine();
			characterfile.write("Item5 = ", 0, 8);
			characterfile.write(Integer.toString(Item5), 0, Integer.toString(Item5).length());
			characterfile.newLine();
			characterfile.write("Item6 = ", 0, 8);
			characterfile.write(Integer.toString(Item6), 0, Integer.toString(Item6).length());
			characterfile.newLine();
			characterfile.write("Item7 = ", 0, 8);
			characterfile.write(Integer.toString(Item7), 0, Integer.toString(Item7).length());
			characterfile.newLine();
			characterfile.write("Item8 = ", 0, 8);
			characterfile.write(Integer.toString(Item8), 0, Integer.toString(Item8).length());
			characterfile.newLine();
			characterfile.write("Item9 = ", 0, 8);
			characterfile.write(Integer.toString(Item9), 0, Integer.toString(Item9).length());
			characterfile.newLine();
			characterfile.write("Item10 = ", 0, 9);
			characterfile.write(Integer.toString(Item10), 0, Integer.toString(Item10).length());
			characterfile.newLine();
			characterfile.write("Item11 = ", 0, 9);
			characterfile.write(Integer.toString(Item11), 0, Integer.toString(Item11).length());
			characterfile.newLine();
			characterfile.write("Item12 = ", 0, 9);
			characterfile.write(Integer.toString(Item12), 0, Integer.toString(Item12).length());
			characterfile.newLine();
			characterfile.write("Item13 = ", 0, 9);
			characterfile.write(Integer.toString(Item13), 0, Integer.toString(Item13).length());
			characterfile.newLine();
			characterfile.write("Item14 = ", 0, 9);
			characterfile.write(Integer.toString(Item14), 0, Integer.toString(Item14).length());
			characterfile.newLine();
			characterfile.write("Item15 = ", 0, 9);
			characterfile.write(Integer.toString(Item15), 0, Integer.toString(Item15).length());
			characterfile.newLine();
			characterfile.write("Item16 = ", 0, 9);
			characterfile.write(Integer.toString(Item16), 0, Integer.toString(Item16).length());
			characterfile.newLine();
			characterfile.write("Item17 = ", 0, 9);
			characterfile.write(Integer.toString(Item17), 0, Integer.toString(Item17).length());
			characterfile.newLine();
			characterfile.write("Item18 = ", 0, 9);
			characterfile.write(Integer.toString(Item18), 0, Integer.toString(Item18).length());
			characterfile.newLine();
			characterfile.write("Item19 = ", 0, 9);
			characterfile.write(Integer.toString(Item19), 0, Integer.toString(Item19).length());
			characterfile.newLine();
			characterfile.write("Item20 = ", 0, 9);
			characterfile.write(Integer.toString(Item20), 0, Integer.toString(Item20).length());
			characterfile.newLine();
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("[ITEMCOSTAMOUNTS]", 0, 17);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("Item1Amount = ", 0, 14);
			characterfile.write(Integer.toString(Item1Amount), 0, Integer.toString(Item1Amount).length());
			characterfile.newLine();
			characterfile.write("Item2Amount = ", 0, 14);
			characterfile.write(Integer.toString(Item2Amount), 0, Integer.toString(Item2Amount).length());
			characterfile.newLine();
			characterfile.write("Item3Amount = ", 0, 14);
			characterfile.write(Integer.toString(Item3Amount), 0, Integer.toString(Item3Amount).length());
			characterfile.newLine();
			characterfile.write("Item4Amount = ", 0, 14);
			characterfile.write(Integer.toString(Item4Amount), 0, Integer.toString(Item4Amount).length());
			characterfile.newLine();
			characterfile.write("Item5Amount = ", 0, 14);
			characterfile.write(Integer.toString(Item5Amount), 0, Integer.toString(Item5Amount).length());
			characterfile.newLine();
			characterfile.write("Item6Amount = ", 0, 14);
			characterfile.write(Integer.toString(Item6Amount), 0, Integer.toString(Item6Amount).length());
			characterfile.newLine();
			characterfile.write("Item7Amount = ", 0, 14);
			characterfile.write(Integer.toString(Item7Amount), 0, Integer.toString(Item7Amount).length());
			characterfile.newLine();
			characterfile.write("Item8Amount = ", 0, 14);
			characterfile.write(Integer.toString(Item8Amount), 0, Integer.toString(Item8Amount).length());
			characterfile.newLine();
			characterfile.write("Item9Amount = ", 0, 14);
			characterfile.write(Integer.toString(Item9Amount), 0, Integer.toString(Item9Amount).length());
			characterfile.newLine();
			characterfile.write("Item10Amount = ", 0, 15);
			characterfile.write(Integer.toString(Item10Amount), 0, Integer.toString(Item10Amount).length());
			characterfile.newLine();
			characterfile.write("Item11Amount = ", 0, 15);
			characterfile.write(Integer.toString(Item11Amount), 0, Integer.toString(Item11Amount).length());
			characterfile.newLine();
			characterfile.write("Item12Amount = ", 0, 15);
			characterfile.write(Integer.toString(Item12Amount), 0, Integer.toString(Item12Amount).length());
			characterfile.newLine();
			characterfile.write("Item13Amount = ", 0, 15);
			characterfile.write(Integer.toString(Item13Amount), 0, Integer.toString(Item13Amount).length());
			characterfile.newLine();
			characterfile.write("Item14Amount = ", 0, 15);
			characterfile.write(Integer.toString(Item14Amount), 0, Integer.toString(Item14Amount).length());
			characterfile.newLine();
			characterfile.write("Item15Amount = ", 0, 15);
			characterfile.write(Integer.toString(Item15Amount), 0, Integer.toString(Item15Amount).length());
			characterfile.newLine();
			characterfile.write("Item16Amount = ", 0, 15);
			characterfile.write(Integer.toString(Item16Amount), 0, Integer.toString(Item16Amount).length());
			characterfile.newLine();
			characterfile.write("Item17Amount = ", 0, 15);
			characterfile.write(Integer.toString(Item17Amount), 0, Integer.toString(Item17Amount).length());
			characterfile.newLine();
			characterfile.write("Item18Amount = ", 0, 15);
			characterfile.write(Integer.toString(Item18Amount), 0, Integer.toString(Item18Amount).length());
			characterfile.newLine();
			characterfile.write("Item19Amount = ", 0, 15);
			characterfile.write(Integer.toString(Item19Amount), 0, Integer.toString(Item19Amount).length());
			characterfile.newLine();
			characterfile.write("Item20Amount = ", 0, 15);
			characterfile.write(Integer.toString(Item20Amount), 0, Integer.toString(Item20Amount).length());
			characterfile.newLine();
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("[ITEMOWNERS]", 0, 12);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("Item1Owner = ", 0, 13);
			characterfile.write(Item1Owner, 0, Item1Owner.length());
			characterfile.newLine();
			characterfile.write("Item2Owner = ", 0, 13);
			characterfile.write(Item2Owner, 0, Item2Owner.length());
			characterfile.newLine();
			characterfile.write("Item3Owner = ", 0, 13);
			characterfile.write(Item3Owner, 0, Item3Owner.length());
			characterfile.newLine();
			characterfile.write("Item4Owner = ", 0, 13);
			characterfile.write(Item4Owner, 0, Item4Owner.length());
			characterfile.newLine();
			characterfile.write("Item5Owner = ", 0, 13);
			characterfile.write(Item5Owner, 0, Item5Owner.length());
			characterfile.newLine();
			characterfile.write("Item6Owner = ", 0, 13);
			characterfile.write(Item6Owner, 0, Item6Owner.length());
			characterfile.newLine();
			characterfile.write("Item7Owner = ", 0, 13);
			characterfile.write(Item7Owner, 0, Item7Owner.length());
			characterfile.newLine();
			characterfile.write("Item8Owner = ", 0, 13);
			characterfile.write(Item8Owner, 0, Item8Owner.length());
			characterfile.newLine();
			characterfile.write("Item9Owner = ", 0, 13);
			characterfile.write(Item9Owner, 0, Item9Owner.length());
			characterfile.newLine();
			characterfile.write("Item10Owner = ", 0, 14);
			characterfile.write(Item10Owner, 0, Item10Owner.length());
			characterfile.newLine();
			characterfile.write("Item11Owner = ", 0, 14);
			characterfile.write(Item11Owner, 0, Item11Owner.length());
			characterfile.newLine();
			characterfile.write("Item12Owner = ", 0, 14);
			characterfile.write(Item12Owner, 0, Item12Owner.length());
			characterfile.newLine();
			characterfile.write("Item13Owner = ", 0, 14);
			characterfile.write(Item13Owner, 0, Item13Owner.length());
			characterfile.newLine();
			characterfile.write("Item14Owner = ", 0, 14);
			characterfile.write(Item14Owner, 0, Item14Owner.length());
			characterfile.newLine();
			characterfile.write("Item15Owner = ", 0, 14);
			characterfile.write(Item15Owner, 0, Item15Owner.length());
			characterfile.newLine();
			characterfile.write("Item16Owner = ", 0, 14);
			characterfile.write(Item16Owner, 0, Item16Owner.length());
			characterfile.newLine();
			characterfile.write("Item17Owner = ", 0, 14);
			characterfile.write(Item17Owner, 0, Item17Owner.length());
			characterfile.newLine();
			characterfile.write("Item18Owner = ", 0, 14);
			characterfile.write(Item18Owner, 0, Item18Owner.length());
			characterfile.newLine();
			characterfile.write("Item19Owner = ", 0, 14);
			characterfile.write(Item19Owner, 0, Item19Owner.length());
			characterfile.newLine();
			characterfile.write("Item20Owner = ", 0, 14);
			characterfile.write(Item20Owner, 0, Item20Owner.length());
			characterfile.newLine();
			characterfile.close();
		} catch(IOException ioexception) {
			Misc.println("[GRAND EXCHANGE]: Error writing file!");
			return false;
		}
		return true;
	}

	public int LoadGrandExchange() {
		String line = "";
		String token = "";
		String token2 = "";
		@SuppressWarnings("unused")
		String[] token3 = new String[3];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		BufferedReader characterfile2 = null;
		boolean File1 = false;
		boolean File2 = false;
		try {
			characterfile = new BufferedReader(new FileReader("./GrandExchange.txt"));
			File1 = true;
		} catch(FileNotFoundException fileex1) {
			Misc.println("[GRAND EXCHANGE]: CRITICAL ERROR!");
		}
		if (File1 == true && File2 == true) {
			File myfile1 = new File ("./GrandExchange.txt");
			File myfile2 = new File ("./GrandExchange.txt");
			if (myfile1.lastModified() < myfile2.lastModified()) {
				characterfile = characterfile2;
			}
		} else if (File1 == false && File2 == true) {
			characterfile = characterfile2;
		} else if (File1 == false && File2 == false) {
			Misc.println("[GRAND EXCHANGE]: File not found.");
			return 3;
		}
		try {
			line = characterfile.readLine();
		} catch(IOException ioexception) {
			Misc.println("[GRAND EXCHANGE]: Error loading file.");
		}
		while(EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token3 = token2.split("\t");
				switch (ReadMode) {
				case 1:
					if (token.equals("Item1")) {
						Item1 = Integer.parseInt(token2);
					} else if (token.equals("Item2")) {
						Item2 = Integer.parseInt(token2);
					} else if (token.equals("Item3")) {
						Item3 = Integer.parseInt(token2);
					} else if (token.equals("Item4")) {
						Item4 = Integer.parseInt(token2);
					} else if (token.equals("Item5")) {
						Item5 = Integer.parseInt(token2);
					} else if (token.equals("Item6")) {
						Item6 = Integer.parseInt(token2);
					} else if (token.equals("Item7")) {
						Item7 = Integer.parseInt(token2);
					} else if (token.equals("Item8")) {
						Item8 = Integer.parseInt(token2);
					} else if (token.equals("Item9")) {
						Item9 = Integer.parseInt(token2);
					} else if (token.equals("Item10")) {
						Item10 = Integer.parseInt(token2);
					} else if (token.equals("Item11")) {
						Item11 = Integer.parseInt(token2);
					} else if (token.equals("Item12")) {
						Item12 = Integer.parseInt(token2);
					} else if (token.equals("Item13")) {
						Item13 = Integer.parseInt(token2);
					} else if (token.equals("Item14")) {
						Item14 = Integer.parseInt(token2);
					} else if (token.equals("Item15")) {
						Item15 = Integer.parseInt(token2);
					} else if (token.equals("Item16")) {
						Item16 = Integer.parseInt(token2);
					} else if (token.equals("Item17")) {
						Item17 = Integer.parseInt(token2);
					} else if (token.equals("Item18")) {
						Item18 = Integer.parseInt(token2);
					} else if (token.equals("Item19")) {
						Item19 = Integer.parseInt(token2);
					} else if (token.equals("Item20")) {
						Item20 = Integer.parseInt(token2);
					}
					break;
				case 2:
					if (token.equals("Item1Amount")) {
						Item1Amount = Integer.parseInt(token2);
					} else if (token.equals("Item2Amount")) {
						Item2Amount = Integer.parseInt(token2);
					} else if (token.equals("Item3Amount")) {
						Item3Amount = Integer.parseInt(token2);
					} else if (token.equals("Item4Amount")) {
						Item4Amount = Integer.parseInt(token2);
					} else if (token.equals("Item5Amount")) {
						Item5Amount = Integer.parseInt(token2);
					} else if (token.equals("Item6Amount")) {
						Item6Amount = Integer.parseInt(token2);
					} else if (token.equals("Item7Amount")) {
						Item7Amount = Integer.parseInt(token2);
					} else if (token.equals("Item8Amount")) {
						Item8Amount = Integer.parseInt(token2);
					} else if (token.equals("Item9Amount")) {
						Item9Amount = Integer.parseInt(token2);
					} else if (token.equals("Item10Amount")) {
						Item10Amount = Integer.parseInt(token2);
					} else if (token.equals("Item11Amount")) {
						Item11Amount = Integer.parseInt(token2);
					} else if (token.equals("Item12Amount")) {
						Item12Amount = Integer.parseInt(token2);
					} else if (token.equals("Item13Amount")) {
						Item13 = Integer.parseInt(token2);
					} else if (token.equals("Item14Amount")) {
						Item14Amount = Integer.parseInt(token2);
					} else if (token.equals("Item15Amount")) {
						Item15Amount = Integer.parseInt(token2);
					} else if (token.equals("Item16Amount")) {
						Item16Amount = Integer.parseInt(token2);
					} else if (token.equals("Item17Amount")) {
						Item17Amount = Integer.parseInt(token2);
					} else if (token.equals("Item18Amount")) {
						Item18Amount = Integer.parseInt(token2);
					} else if (token.equals("Item19Amount")) {
						Item19Amount = Integer.parseInt(token2);
					} else if (token.equals("Item20Amount")) {
						Item20Amount = Integer.parseInt(token2);
					}
					break;
				case 3:
					if (token.equals("Item1Owner")) {
						Item1Owner = (token2);
					}
					if (token.equals("Item2Owner")) {
						Item2Owner = (token2);
					}
					if (token.equals("Item3Owner")) {
						Item3Owner = (token2);
					}
					if (token.equals("Item4Owner")) {
						Item4Owner = (token2);
					}
					if (token.equals("Item5Owner")) {
						Item5Owner = (token2);
					}
					if (token.equals("Item6Owner")) {
						Item6Owner = (token2);
					}
					if (token.equals("Item7Owner")) {
						Item7Owner = (token2);
					}
					if (token.equals("Item8Owner")) {
						Item8Owner = (token2);
					}
					if (token.equals("Item9Owner")) {
						Item9Owner = (token2);
					}
					if (token.equals("Item10Owner")) {
						Item10Owner = (token2);
					}
					if (token.equals("Item11Owner")) {
						Item11Owner = (token2);
					}
					if (token.equals("Item12Owner")) {
						Item12Owner = (token2);
					}
					if (token.equals("Item13Owner")) {
						Item13Owner = (token2);
					}
					if (token.equals("Item14Owner")) {
						Item14Owner = (token2);
					}
					if (token.equals("Item15Owner")) {
						Item15Owner = (token2);
					}
					if (token.equals("Item16Owner")) {
						Item16Owner = (token2);
					}
					if (token.equals("Item17Owner")) {
						Item17Owner = (token2);
					}
					if (token.equals("Item18Owner")) {
						Item18Owner = (token2);
					}
					if (token.equals("Item19Owner")) {
						Item19Owner = (token2);
					}
					if (token.equals("Item20Owner")) {
						Item20Owner = (token2);
					}
					break;
				}
			} else {
				if (line.equals("[ITEMSFORSALE]")) {		
					ReadMode = 1;
				}
				if (line.equals("[ITEMCOSTAMOUNTS]")) {		
					ReadMode = 2;
				}
				if (line.equals("[ITEMOWNERS]")) {		
					ReadMode = 3;
				}
				else if (line.equals("[EOF]")) {		
					try {
						characterfile.close();
					} catch(IOException ioexception) { 
						
					} 
					return 1;
				}
			}
			try {
				line = characterfile.readLine();
			} catch(IOException ioexception1) { 
				EndOfFile = true; 
			}
		}
		try {
			characterfile.close();
		} catch(IOException ioexception) {
			Misc.println("[GRAND EXCHANGE]: Failed to close data.");
		}
		return 0;
	}
}