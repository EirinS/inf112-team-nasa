package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class Database implements IDatabase {

    // Database url connection to Amazon RDS.
    private String username = "team_nasa";
    private String password = "inf112ergoy!";
    private String dbUrl = "jdbc:postgresql://inf112-team-nasa.cy6mirlma7zd.eu-west-2.rds.amazonaws.com:5432/players";

    private Connection connection;

    public Database() {
        connection = getConnection();
    }

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
        String query = Queries.listPlayers();
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
            if (stmt != null) {
                stmt.close();
            }
        }
        return players;
    }

    private boolean executeQuery(String query) throws SQLException {
        Statement stmt = null;
        try {
            Connection connection = getConnection();
            if (connection == null) {
                System.out.println("Can not query empty connection!");
                return false;
            }

            stmt = connection.createStatement();
            int rowsAffected = stmt.executeUpdate(query);
            return rowsAffected > 0;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    @Override
    public boolean registerPlayer(Player player) throws SQLException {

        // Check if player already exists.
        if (getPlayer(player.getName()) != null) {
            System.out.println("Player already exists.");
            return false;
        }

        return executeQuery(Queries.registerPlayer(player));
    }

    @Override
    public Player getPlayer(String playerName) throws SQLException {
        String query = Queries.getPlayer(playerName);
        Statement stmt = null;
        try {
            Connection connection = getConnection();
            if (connection == null) {
                System.out.println("Can not query empty connection!");
                return null;
            }

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String name = rs.getString("name");
                int rating = rs.getInt("rating");
                int wins = rs.getInt("wins");
                int losses = rs.getInt("losses");
                int draws = rs.getInt("draws");
                return new Player(name, rating, wins, losses, draws);
            } else {
                return null;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    @Override
    public boolean isPlayerRegistered(String playerName) {
        try {
            return getPlayer(playerName) != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePlayer(String playerName, int rating, int win_lose_draw) throws SQLException {

        // Check if player exists.
        Player player = getPlayer(playerName);
        if (player == null) {
            System.out.println("Player must exist before updating.");
            return false;
        }

        return executeQuery(Queries.updatePlayer(player, rating, win_lose_draw));
    }
}
