package db;

import register.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class Database implements IDatabase {

    // Database url connection to Amazon RDS.
    private String username = "team_nasa";
    private String password = "inf112ergoy!";
    private String dbUrl = "jdbc:postgresql://inf112-team-nasa.cy6mirlma7zd.eu-west-2.rds.amazonaws.com:5432/players";

    private Connection connection;

    private Connection connect() {
        Properties props = new Properties();
        props.put("user", username);
        props.put("password", password);

        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(dbUrl, props);
            System.out.println("Connected to database");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) connection = connect();
            if (connection == null) {
                System.out.println("Error! Could not connect to database.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public ArrayList<Player> listPlayers() throws SQLException {
        ArrayList<Player> players = new ArrayList<>();
        String query = Queries.ALL;
        Statement stmt = null;
        try {
            Connection connection = getConnection();
            if (connection == null) {
                System.out.println("Can not query empty connection!");
                return players;
            }

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                int rating = rs.getInt("rating");
                int wins = rs.getInt("wins");
                int losses = rs.getInt("losses");
                int draws = rs.getInt("draws");
                players.add(new Player(name, rating, wins, losses, draws));
            }
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return players;
    }

    @Override
    public boolean registerPlayer(Player player) throws SQLException {
        return false;
    }

    @Override
    public Player getPlayer(String playerName) throws SQLException {
        return null;
    }

    @Override
    public boolean updatePlayer(String playerName, int rating, int wins, int losses, int draws) throws SQLException {
        return false;
    }
}
