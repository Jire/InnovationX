package src.innovationx.classic;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import src.innovationx.classic.content.RacismFilter;
import src.innovationx.classic.content.minigames.ClanWars;
import src.innovationx.classic.content.minigames.FightPits;
import src.innovationx.classic.content.minigames.PestControl;
import src.innovationx.classic.content.minigames.ZombieMinigame;
import src.innovationx.classic.core.CoreExecutor;
import src.innovationx.classic.model.items.ItemHandler;
import src.innovationx.classic.model.items.ShopHandler;
import src.innovationx.classic.model.npc.NPCHandler;
import src.innovationx.classic.model.objects.WalkingCheck;
import src.innovationx.classic.model.objects.WorldObjects;
import src.innovationx.classic.model.player.Client;
import src.innovationx.classic.model.player.CombatTick;
import src.innovationx.classic.model.player.MagicHandler;
import src.innovationx.classic.model.player.PlayerHandler;
import src.innovationx.classic.model.player.combat.CombatTypeManager;
import src.innovationx.classic.net.MultiThreadedCoreExecutor;
import src.innovationx.classic.net.MultiThreader;
import src.innovationx.classic.net.Network;
import src.innovationx.classic.net.forums.DatabaseConnection;
import src.innovationx.classic.net.forums.vBulletin;
import src.innovationx.classic.util.Tasks;
import src.innovationx.classic.util.newevent.GameLogicTaskManager;

public class Server {

	public GameLogicTaskManager eventManager;
	public ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	public Tasks tasks;
	public CoreExecutor coreExecutor = new CoreExecutor();
	public boolean serverRunning;
	public CombatTypeManager combatInstance;
	public RacismFilter RacismFilter = null;
	public static ClanWars ClanWars = null;
	public Connection myConnection = null;
	public Statement myStmt = null;
	public static int SERVER_PORT = 43594;
	public ServerSocket newSocket = null;
	public String[] connectedIps = new String[200];
	public int[] resetDelay = new int[200];
	public int restartTime = 0;
	public boolean keepServerOn = true;
	public int garbageCollectDelay = 40;
	public int msLag = 0;
	public PlayerHandler playerHandler = null;
	public NPCHandler npcHandler = null;
	public ItemHandler itemHandler = null;
	public ShopHandler shopHandler = null;
	public MagicHandler magicHandler = null;
	public PestControl pc = null;
	public static Server s = null;
	public CombatTick combatTick = null;
	public WorldObjects worldO = null;
	public static vBulletin vb = new vBulletin("localhost", "vbulletin",
			"vbulletin", "uberpass", vBulletin.Type.vBulletin);
	public static DatabaseConnection database = null;

	public Server() {
	}

	public static void main(String[] args) {
		System.out.println("[SHUTDOWN HOOKS]: Sucessfully started.");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@SuppressWarnings({ "static-access" })
			public void run() {

				for (Client p : Server.s.playerHandler.clients) {
					if (p != null) {
						p.declineTrade();
						p.declineDuel();
						Server.s.playerHandler.SaveChar(p);
						Server.s.playerHandler.SaveMoreInfo(p);
						System.out.println("[SHUTDOWN HOOK] Game saved for "
								+ p.playerName + ".");
					}
				}
			}

		});
		WalkingCheck.check();
		WalkingCheck.check2();
		WalkingCheck.check3();
		ClanWars = new ClanWars();
		s = new Server();
		System.out.println("Stating event manager...");
		s.eventManager = new GameLogicTaskManager();
		
		//System.out.println("Registering tasks...");
		//s.tasks = new Tasks();
		//s.tasks.registerTasks();
		System.out.println("Connecting to SQL database...");
		database = new DatabaseConnection();
		System.out.println("Loading racist word filter...");
		s.RacismFilter = new RacismFilter();
		System.out.println("Loading new player manager...");
		s.playerHandler = new PlayerHandler();
		System.out.println("Loading combat type manager...");
		Server.s.combatInstance = new CombatTypeManager();
		System.out.println("Creating game objects...");
		s.worldO = new WorldObjects();
		System.out.println("Loading new NPC manager...");
		s.npcHandler = new NPCHandler();
		System.out.println("Loading item definitions...");
		s.itemHandler = new ItemHandler();
		System.out.println("Creating pest control instance...");
		s.pc = new PestControl();
		System.out.println("Loading shops...");
		s.shopHandler = new ShopHandler();
		System.out.println("Loading magic information...");
		s.magicHandler = new MagicHandler();
		Server.s.serverRunning = true;
		System.out.println("Starting game engine...");

		long lastTicks = System.currentTimeMillis();
		long timeSpent = 0;
		System.out.println("Starting listeners...");
		(new Thread(new Network())).start();
		System.out.println("Game engine started!");
		while (true) {
			try {
				Server.s.restartTime++;
				s.playerHandler.process();
				s.npcHandler.process();
				Server.s.itemHandler.process();
				Server.s.shopHandler.process();
				Server.s.pc.process();
				Server.s.worldO.process();
				try {
					s.eventManager.processTasks();
				} catch(Exception e) {
					
				}
				if (ZombieMinigame.players.size() >= 3) {
					ZombieMinigame.tick();
				}
				timeSpent = System.currentTimeMillis() - lastTicks;
				s.msLag = (int) (timeSpent);
				if (timeSpent >= 600) {
					timeSpent = 0;
				}
			} catch (Exception _ex) {
				System.err.println("Server Exception");
				_ex.printStackTrace();
			}
			try {
				Thread.sleep(600 - timeSpent);
			} catch (Exception _ex) {
				System.err.println("Thread Sleep Error");
			}
			lastTicks = System.currentTimeMillis();
		}
	}

	public static MultiThreader MultiThreader = null;// new MultiThreader();

	public static MultiThreader getMultiThreaderCore() {
		return MultiThreader;
	}

	/*
	 * public void run() { try { newSocket = new ServerSocket(SERVER_PORT, 1,
	 * null); System.out.println("Starting InnovationX On Port: " +
	 * SERVER_PORT); } catch (Exception e) {
	 * System.out.println("Error Starting InnovationX On Port: " + SERVER_PORT);
	 * System.exit(0); } Socket s = null; String ConnectingIp = null; while
	 * (true) { try { s = newSocket.accept(); s.setTcpNoDelay(true);
	 * ConnectingIp = s.getInetAddress().getHostAddress();
	 * if(AntiCrash.addIP(ConnectingIp) && !bannedIp(ConnectingIp)) {
	 * System.out.println("[MONITOR]: Accepted connection from " +
	 * ConnectingIp); playerHandler.newPlayerClient(s, ConnectingIp); } else {
	 * System.out.println("[MONITOR]: Denied connection from " + ConnectingIp);
	 * } } catch (Exception ea) { ea.printStackTrace(); try { s.close(); } catch
	 * (Exception e) { } } try { Thread.sleep(1); } catch (Exception e) {
	 * e.printStackTrace(); } } }
	 */

	public void resetServer() {
	}

	public void appendConnection(String ip) {
	}

	public void addIpToList(String ip) {
	}

	@SuppressWarnings("static-access")
	public String isLoggedOn(String ipConnecting) {
		for (int p = 1; p < playerHandler.maxPlayers; p++) {
			if (playerHandler.players[p] != null) {
				if (playerHandler.players[p].PlayerIp
						.equalsIgnoreCase(ipConnecting)) {
					return playerHandler.players[p].playerName;
				}
			}
		}
		return "";
	}

	public static boolean bannedIp(String ConnectingIp) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"./Data/BannedIps.txt"));
			String loggedIps = null;
			while ((loggedIps = in.readLine()) != null) {
				if (ConnectingIp.startsWith(loggedIps)) {
					return true;
				}
			}
		} catch (IOException e) {
			System.out.println("Critical error while checking banned ips!");
		}
		return false;
	}

	public void appendDDOSer(String IP) {
	}

	public void appendtoIPBanned(String IP) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(
					new FileWriter("./Data/BannedIps.txt", true));
			bw.write(IP);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (IOException ioe) {
			System.out
					.println("Critical error while writing an IP banned player!");
		}
	}
}
