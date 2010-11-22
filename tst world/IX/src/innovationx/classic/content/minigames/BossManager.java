package src.innovationx.classic.content.minigames;
/**
 * Has all the bosses.
 * @author Canownueasy
 */

public class BossManager {

	/**
	 * Ryan you can just add more bosses here ok?
	 */
	public static int[] bosses = {
		50, //kbd 
		3847, //sea troll queen
		3200 //chaos elemental
		};
		
	/**
	 * Contains all the random items you could get.
	 */
    public static int items[] = {1038, 1040, 1042, 1044, 1046, 1048, 1050, 1037, 1053, 1055, 1057};

    public static int randomRare() {
        return items[(int) (Math.random() * items.length)];
    }
}