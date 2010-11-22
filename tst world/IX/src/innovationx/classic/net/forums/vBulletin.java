package src.innovationx.classic.net.forums;

/**
 * vBulletin class
 * @author Mad Turnip
 */
import java.sql.*;

import src.innovationx.classic.model.AntiDupe;

public class vBulletin implements Runnable {

    private static Connection connection = null;
    private static Statement statement = null;
    private static Thread thread = null;

    public enum Type {

        myBB,
        SMF,
        IPB,
        vBulletin,
        phpBB,
    }
    private String[] tableNames = new String[6];

    private void setTables() {
        if (forum == Type.myBB) {
            tableNames = new String[]{"mybb_users", "username", "password", "salt", "usergroupid",};
        } else if (forum == Type.SMF) {
            tableNames = new String[]{"smf_", "username", "password", "password", "usergroupid",};
        } else if (forum == Type.IPB) {
            tableNames = new String[]{"members", "name", "converge_pass_hash", "converge_pass_salt", "mgroup",};
        } else if (forum == Type.vBulletin) {//vbulletin
            tableNames = new String[]{"user", "username", "password", "salt", "usergroupid",};
        } else if (forum == Type.phpBB) {//phpBB
            tableNames = new String[]{"users", "username", "user_password", "user_password", "group_id",};
        }
    }

    public vBulletin(String url, String database, String username, String password, Type t) {
        this.hostAddress = "jdbc:mysql://" + url + "/" + database;
        this.username = username;
        this.password = password;
        this.forum = t;
        try {
            //connect();
            thread = new Thread(this);
            thread.start();
        } catch (Exception e) {
            connection = null;
            e.printStackTrace();
        }
    }
    private final String hostAddress;
    private final String username;
    private final String password;
    private final Type forum;

    private void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e2) {
            System.out.println("Cannot find mySql Driver.");
            return;
        }
        try {
            connection = DriverManager.getConnection(hostAddress, username, password);
            statement = connection.createStatement();
			System.out.println("Connected to SQL");
        } catch (Exception e) {
            System.out.println("Connetion rejected, Wrong username or password, or ip is banned, or host is down.");
            connection = null;
            e.printStackTrace();
        }
    }

    private void ping() {
        try {
            ResultSet results = null;
            String query = "SELECT * FROM " + tableNames[0] + " WHERE " + tableNames[2] + " LIKE 'null312'";
            results = statement.executeQuery(query);
        } catch (Exception e) {
            connection = null;
            connect();
            e.printStackTrace();
        }
    }

    public void run() {
        boolean allowRun = true;
        while (allowRun) {
            try {
                if (connection == null) {
                    setTables();
                    connect();
                } else {
                    ping();
                }
                thread.sleep(10000);
            } catch (Exception e) {
            }
        }
    }

    /**
     * returns 2 integers, the return code and the usergroup of the player
     */
    public int[] checkUser(String name, String password) {
        int i = 0;
        int[] returnCodes = {0, 0, 0, 0};//return code for client, group id

        try {
            ResultSet results = null;
            String query = "SELECT * FROM " + tableNames[0] + " WHERE " + tableNames[1] + " LIKE '" + name + "'";
            try {
                if (statement == null) {
                    statement = connection.createStatement();
                }
            } catch (Exception e5) {
                statement = null;
                connection = null;
                connect();
                statement = connection.createStatement();
            }
            results = statement.executeQuery(query);
            if (results.next()) {
                String salt = results.getString(tableNames[3]);
                String pass = results.getString(tableNames[2]);
                int group = results.getInt(tableNames[4]);
				String mgroup = results.getString("membergroupids");
                returnCodes[1] = group;
                String pass2 = "";
                if (forum == Type.myBB) {
                    pass2 = MD5.MD5(MD5.MD5(salt) + MD5.MD5(password));
                } else if (forum == Type.vBulletin) {
                    pass2 = MD5.MD5(password);
                    pass2 = MD5.MD5(pass2 + salt);
                } else if (forum == Type.SMF) {
                    pass2 = MD5.SHA((name.toLowerCase()) + password);
                } else if (forum == Type.phpBB) {
                    pass2 = MD5.MD5(password);
                } else if (forum == Type.IPB) {
                    pass2 = MD5.MD5(MD5.MD5(salt) + MD5.MD5(password));
                }
                // Premium/Extremes
				if (mgroup.contains("10"))
					returnCodes[2] = 1;
				else if (mgroup.contains("11"))
					returnCodes[2] = 2;
				// Purchasing items
				if (mgroup.contains("32"))
					returnCodes[3] = 1;
				if(AntiDupe.onlineNames.contains(name)) {
					returnCodes[0] = 5;
				}
                if (pass.equals(pass2)) {
                    returnCodes[0] = 2;
                    return returnCodes;
                } else {
                    returnCodes[0] = 3;
                    return returnCodes;
                }
            } else {
                //no user exists
                returnCodes[0] = 12;
                return returnCodes;
            }
        } catch (Exception e) {
            statement = null;
            returnCodes[0] = 8;
            return returnCodes;
        }
    }
}
