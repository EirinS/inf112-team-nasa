package register;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * @author Stian
 * <p>
 * Class for keeping track of players. Information is stored on a text file
 * with the following format: "playername" rating(int) wins(int) losses(int) draws(int)
 * One row per player
 */
public class PlayerRegister {
    private ArrayList<Player> players;

    private static final int defaultRating = 1500;
    private static final int highscoreLength = 10;
    private static String playerFile;

    public PlayerRegister(String fileName) {
        playerFile = fileName;
        File f = new File(fileName);
        if (!f.exists()) {
            try {
                if (f.createNewFile()) {
                    loadPlayers();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            loadPlayers();
        }
    }

    public void loadPlayers() {
        players = new ArrayList<>();
        File file = new File(playerFile);
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().split(" ");
                String name = line[0];
                int rating = Integer.parseInt(line[1]);
                int wins = Integer.parseInt(line[2]);
                int losses = Integer.parseInt(line[3]);
                int draws = Integer.parseInt(line[4]);
                Player p = new Player(name, rating, wins, losses, draws);
                players.add(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer(String playerName) {
        if (players == null) loadPlayers();
        for (Player p : players) {
            if (p.getName().equals(playerName)) {
                return p;
            }
        }
        return null;
    }

    /**
     * This method checks if the given name already occurs in the player file.
     * Reads from the player file
     *
     * @param playerName Name of the player we want to check
     * @return returns True if player is registered and False if not
     */
    public boolean playerIsRegistered(String playerName) {
        if (players == null) loadPlayers();
        for (Player p : players) {
            if (p.getName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Registers a player in the player file. Automatically sets his rating to the default rating given by "defaultRating"
     * Also sets wins/losses/draws to 0/0/0
     * Writes to the player file.
     *
     * @param playerName Name of the player
     */
    public void registerPlayer(String playerName) {
        if (players == null) loadPlayers();
        playerName = playerName.replaceAll("\\s+", "");

        if (playerIsRegistered(playerName)) {
            return;
        }

        PrintWriter printWriter = null;

        try {
            // Simply appends the new player to the player file with the default rating
            printWriter = new PrintWriter(new FileOutputStream(new File(playerFile), true));
            Player player = new Player(playerName, defaultRating, 0, 0, 0);
            printWriter.println(player);
            players.add(player);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    /**
     * Updates a given players rating.
     * Both reads and writes to the player file.
     *
     * @param playerName    Name of the player
     * @param newRating     The updated rating for the player
     * @param win_lose_draw 1 if the player wins, 2 if the player loses and 3 if the player draws
     */
    public void updatePlayerRating(String playerName, int newRating, int win_lose_draw) {
        if (players == null) loadPlayers();

        // Find and update player.
        for (Player p : players) {
            if (p.getName().equals(playerName)) {
                p.setRating(newRating);
                if (win_lose_draw == 1) {
                    p.setWins(p.getWins() + 1);
                } else if (win_lose_draw == 2) {
                    p.setLosses(p.getLosses() + 1);
                } else if (win_lose_draw == 3) {
                    p.setDraws(p.getDraws() + 1);
                }
                break;
            }
        }

        // Finally we write the new player list back to file (writing over the old file)
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream(new File(playerFile), false));
            for (Player p : players) {
                writer.println(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


    /**
     * Returns the highest rated players (or less if there's less than the number "highscoreLength" players registered) in text form(sorted).
     * Reads from the player file.
     *
     * @Return highscores This returns a sorted arrayList consisting of the highest rated players and their scores in string form
     */
    public ArrayList<String> getHighscores() {
        if (players == null) loadPlayers();
        ArrayList<String> highscores = new ArrayList<>();
        PriorityQueue<Player> pq = new PriorityQueue<>(players); // Adds all the players to the priority queue.

        // Add the highest rated players to the highscores-arrayList
        for (int i = 0; i < players.size() && i < highscoreLength; i++) {
            highscores.add(pq.poll().toString());
        }

        return highscores;
    }
}
