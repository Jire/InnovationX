package src.innovationx.classic.net.forums;
import java.sql.ResultSet;
import java.sql.SQLException;

import src.innovationx.classic.Server;

public class DatabaseFunctions {

	private static DatabaseConnection getDatabase() {
		if (Server.database != null)
			return Server.database;
		return null;
	}

	public static boolean checkVotes(String playerName) {
		try {
			ResultSet results = getDatabase().getQuery("SELECT * FROM `votes` WHERE `playername` = '" + playerName + "'");
			while(results.next()) {
				int recieved = results.getInt("recieved");
				if(recieved == 0) {
					getDatabase().updateQuery("UPDATE Votes SET recieved = 1 WHERE playername = '" + playerName + "'");
					return true;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}