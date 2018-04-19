package db;

import game.Chess;

import java.sql.SQLException;

/**
 * Simple helper-class for the getHighscores method, which allows the priority queue to be used.
 * Stores a player using his name and score.
 */
public class Player implements Comparable<Player> {

    private final String name;
    private int rating;
    private int wins;
    private int losses;
    private int draws;
    private String ratingChange;

    public Player(String name) {
        this(name, 1500, 0, 0, 0);
    }

    public Player(String name, int rating, int wins, int losses, int draws) {
        this.name = name;
        this.rating = rating;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.ratingChange = "default";
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void loadRating() {
        try {
            rating = Chess.getDatabase().getPlayer(name).getRating();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int compareTo(Player other) {
        int b = this.getRating();
        int a = other.getRating();
        return Integer.compare(a, b);
    }

    @Override
    public String toString() {
        return name + " " + rating + " " + wins + " " + losses + " " + draws;
    }

    public String getNameRating() {
        return name + " (" + rating + ")";
    }

    public String getHighscoreRow() {
        return String.format("%s\t\t%d\t%d\t%d", name, wins, losses, draws);
    }
    
    
    /*
    public void setRatingChange(int change)
    {
    	 if (change > 0) {
    		 ratingChange = "(+" + change + ")";
         } else if (change < 0) {
        	 ratingChange = "(" + change + ")";
         } else
         {
        	 ratingChange = "(+0)";
         }
    }
    
    public String getRatingChange()
    {
    	return ratingChange;
    }
    */
}