package src.innovationx.classic.model.player;
import java.io.*;
import java.util.Hashtable;

import src.innovationx.classic.util.Misc;

public class MagicHandler {

	public int[] SpellId = new int[50];
	public int[] PlayerGfx = new int[50];
	public int[] PlayerHeight = new int[50];
	public int[] ProGfx = new int[50];
	public int[] EndGfx = new int[50];
	public int[] EndHeight = new int[50];
	public int[] MaxHit = new int[50];
	public int[] Emote = new int[50];
	public int[] LvlNeeded = new int[50];
	public int[] Rune1 = new int[50];
	public int[] Rune1Am = new int[50];
	public int[] Rune2 = new int[50];
	public int[] Rune2Am = new int[50];
	public int[] Rune3 = new int[50];
	public int[] Rune3Am = new int[50];
	public int[] Rune4 = new int[50];
	public int[] Rune4Am = new int[50];

	public MagicHandler() {
		LoadMagicConfig("Magic.cfg");
	}

	public void destruct() {
		SpellId = null;
		PlayerGfx = null;
		PlayerHeight = null;
		ProGfx = null;
		EndGfx = null;
		EndHeight = null;
		MaxHit = null;
		Emote = null;
		LvlNeeded = null;
		Rune1 = null;
		Rune1Am = null;
		Rune2 = null;
		Rune2Am = null;
		Rune3 = null;
		Rune3Am = null;
		Rune4 = null;
		Rune4Am = null;
	}

	public int GetSpellAnim(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return Emote[i];
			}
		}
		return 711;
	}

	public int GetSpellStartGfx(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return PlayerGfx[i];
			}
		}
		return 0;
	}

	public int GetSpellProGfx(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return ProGfx[i];
			}
		}
		return 0;
	}

	public int GetSpellEndGfx(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return EndGfx[i];
			}
		}
		return 0;
	}

	public int GetSpellMaxHit(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return MaxHit[i];
			}
		}
		return 0;
	}

	public int GetSpellLvl(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return LvlNeeded[i];
			}
		}
		return 0;
	}

	public int GetSpellRune1(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return Rune1[i];
			}
		}
		return 0;
	}

	public int GetSpellRune1Am(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return Rune1Am[i];
			}
		}
		return 0;
	}

	public int GetSpellRune2(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return Rune2[i];
			}
		}
		return 0;
	}

	public int GetSpellRune2Am(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return Rune2Am[i];
			}
		}
		return 0;
	}

	public int GetSpellRune3(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return Rune3[i];
			}
		}
		return 0;
	}

	public int GetSpellRune3Am(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return Rune3Am[i];
			}
		}
		return 0;
	}

	public int GetSpellRune4(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return Rune4[i];
			}
		}
		return 0;
	}

	public int GetSpellRune4Am(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return Rune4Am[i];
			}
		}
		return 0;
	}

	public int GetSpellPlayerHeight(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return PlayerHeight[i];
			}
		}
		return 0;
	}

	public int GetSpellEndHeight(int spellId) {
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == spellId) {
				return EndHeight[i];
			}
		}
		return 0;
	}

	public void NewSpell(int spellId, int playerGfx, int playerHeight, int proGfx, int endGfx, int endHeight, int maxHit, int emote, int lvlNeeded, int rune1, int rune1Am, int rune2, int rune2Am, int rune3, int rune3Am, int rune4, int rune4Am) {
		int ThisSlot = -1;
		for (int i = 0; i < 50; i++) {
			if (SpellId[i] == 0) {
				ThisSlot = i;
				break;
			}
		}
		if(ThisSlot == -1) {
			return;
		}
		SpellId[ThisSlot] = spellId;
		PlayerGfx[ThisSlot] = playerGfx;
		PlayerHeight[ThisSlot] = playerHeight;
		ProGfx[ThisSlot] = proGfx;
		EndGfx[ThisSlot] = endGfx;
		EndHeight[ThisSlot] = endHeight;
		MaxHit[ThisSlot] = maxHit;
		Emote[ThisSlot] = emote;
		LvlNeeded[ThisSlot] = lvlNeeded;
		Rune1[ThisSlot] = rune1;
		Rune1Am[ThisSlot] = rune1Am;
		Rune2[ThisSlot] = rune2;
		Rune2Am[ThisSlot] = rune2Am;
		Rune3[ThisSlot] = rune3;
		Rune3Am[ThisSlot] = rune3Am;
		Rune4[ThisSlot] = rune4;
		Rune4Am[ThisSlot] = rune4Am;
	}
	public boolean LoadMagicConfig(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./CFG/"+FileName));
		} catch(FileNotFoundException fileex) {
			Misc.println(FileName+": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch(IOException ioexception) {
			Misc.println(FileName+": error loading file.");
			return false;
		}
		while(EndOfFile == false && line != null) {
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
				if (token.equals("Spell")) {
					NewSpell(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]), Integer.parseInt(token3[3]), Integer.parseInt(token3[4]), Integer.parseInt(token3[5]), Integer.parseInt(token3[6]), Integer.parseInt(token3[7]), Integer.parseInt(token3[8]), Integer.parseInt(token3[9]), Integer.parseInt(token3[10]), Integer.parseInt(token3[11]), Integer.parseInt(token3[12]), Integer.parseInt(token3[13]), Integer.parseInt(token3[14]), Integer.parseInt(token3[15]), Integer.parseInt(token3[16]));
				}
			} else {
				if (line.equals("[ENDOFMAGICLIST]")) {
					try { characterfile.close(); } catch(IOException ioexception) { }
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch(IOException ioexception1) { EndOfFile = true; }
		}
		try { characterfile.close(); } catch(IOException ioexception) { }
		return false;
	}
}