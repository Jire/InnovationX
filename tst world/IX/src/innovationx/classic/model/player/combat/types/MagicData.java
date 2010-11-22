package src.innovationx.classic.model.player.combat.types;

import java.util.HashMap;
import java.util.Map;

public class MagicData {

	/*
	 * Array place holders
	 */
	public int spellId = 0;
	public int level = 1;
	public int max = 2;
	public int xp = 3;
	public int emote = 4;
	public int start = 5;
	public int projectile = 6;
	public int end = 7;

	/*
	 * Modern spell enum
	 */
	public enum ModernMagic {
		/*
		 * Strike spells (low)
		 */
		windstrike(new int[] { 1152, 1, 2, 6, 711, 90, 91, 92 }), waterstrike(
				new int[] { 1154, 5, 4, 7, 711, 93, 94, 95 }), earthstrike(
				new int[] { 1156, 9, 6, 10, 711, 96, 97, 98 }), firestrike(
				new int[] { 1158, 13, 8, 12, 711, 99, 100, 101 }),

		/*
		 * Bolt spells (low med)
		 */
		windbolt(new int[] { 1160, 17, 9, 14, 711, 117, 118, 119 }), waterbolt(
				new int[] { 1163, 23, 10, 17, 711, 120, 121, 122 }), earthbolt(
				new int[] { 1166, 29, 11, 20, 711, 123, 124, 125 }), firebolt(
				new int[] { 1169, 35, 12, 22, 711, 126, 127, 128 }),

		/*
		 * Blast spells (medium)
		 */
		windblast(new int[] { 1172, 41, 13, 26, 711, 132, 133, 134 }), waterblast(
				new int[] { 1175, 47, 14, 29, 711, 135, 136, 137 }), earthblast(
				new int[] { 1177, 53, 15, 32, 711, 138, 139, 140 }), fireblast(
				new int[] { 1181, 59, 16, 35, 711, 129, 130, 131 }),

		/*
		 * Wave spells (high)
		 */
		windwave(new int[] { 1183, 62, 17, 36, 710, 158, 159, 160 }), waterwave(
				new int[] { 1185, 65, 18, 38, 710, 161, 162, 163 }), earthwave(
				new int[] { 1188, 70, 19, 40, 710, 164, 165, 166 }), firewave(
				new int[] { 1189, 75, 20, 43, 710, 155, 156, 157 });

		/*
		 * Constructor for the enum
		 */
		ModernMagic(int[] data) {
			magicData = data;
			getData().put(this.toString(), magicData);
		}

		/*
		 * Holds the magic data for each enum type
		 */
		int[] magicData;
		/*
		 * Hashmap containing the data
		 */
		public Map<String, int[]> data = new HashMap<String, int[]>();

		/*
		 * Getter for the hashmap
		 */
		public Map<String, int[]> getData() {
			return data;
		}
	};
	
	public Map<String, int[]> data = new HashMap<String, int[]>();

	/*
	 * Getter for the hashmap
	 */
	public Map<String, int[]> getData() {
		return data;
	}

}
