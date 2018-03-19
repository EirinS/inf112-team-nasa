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
 * 
 * @author Stian
 * 
 * Class for keeping track of players. Information is stored on a text file 
 * with the following format: "playername" rating(int) wins(int) losses(int) draws(int)
 * One row per player
 *
 */
public class RegisteredPlayers 
{
	private static final int defaultRating = 1500;
	private static final int highscoreLength = 10;
	private static String playerFile;
	
	public RegisteredPlayers(String fileName)
	{
		RegisteredPlayers.playerFile = fileName;
		
		File f = new File(fileName);
		if(!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * This method checks if the given name already occurs in the player file.
	 * Reads from the player file
	 * 
	 * @param playerName Name of the player we want to check
	 * @return returns True if player is registered and False if not
	 */
	public static boolean playerIsRegistered(String playerName) 
	{
		// Setup
		File file = new File(playerFile);		
		Scanner sc = null;
		
		try 
		{
			sc = new Scanner(file);
			
			// Reads through the player file and returns true if it finds the given name
			while (sc.hasNextLine()) 
			{
				if(sc.nextLine().split(" ")[0].equals(playerName))
				{
					return true;
				}
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if (sc != null) 
			{
				sc.close();
			}
		}
			
		// Return false if we have not found the name by now
		return false;
	}

	/**
	 * Registers a player in the player file. Automatically sets his rating to the default rating given by "defaultRating"
	 * Also sets wins/losses/draws to 0/0/0
	 * Writes to the player file. 
	 * 
	 * @param playerName Name of the player
	 * 
	 */
	public static void registerPlayer(String playerName) 
	{
		if(playerIsRegistered(playerName))
		{
			return;
		}
		
		PrintWriter printWriter = null;

		try 
		{
			// Simply appends the new player to the player file with the default rating
			printWriter = new PrintWriter(new FileOutputStream(new File(playerFile),true));
			printWriter.println(playerName + " " + defaultRating + " " + 0 + " " + 0 + " " + 0);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			if (printWriter != null) 
			{
				printWriter.close();
			}
		}
	}

	/**
	 *  Updates a given players rating. 
	 *  Both reads and writes to the player file. 
	 *  
	 *  @param playerName Name of the player
	 *  @param newRating The updated rating for the player
	 *  @param win_lose_draw 1 if the player wins, 2 if the player loses and 3 if the player draws
	 */
	public static void updatePlayerRating(String playerName, int newRating, int win_lose_draw) 
	{
		// Setup
		File file = new File(playerFile);		
		Scanner sc = null;		
		ArrayList<String> players = new ArrayList<String>();
		PrintWriter printWriter = null;
		
		try 
		{
			sc = new Scanner(file);
			int counter = 0;
			
			// Go through the file and add players to the arraylist
			while (sc.hasNextLine()) 
			{
				players.add(sc.nextLine());
				
				String[] playerAttributes = players.get(counter).split(" ");
				String name = playerAttributes[0];
				int wins = Integer.parseInt(playerAttributes[2]);
				int losses = Integer.parseInt(playerAttributes[3]);
				int draws = Integer.parseInt(playerAttributes[4]);
				
				// If we find the player we are searching for we update his/her rating in the arraylist
				if(name.equals(playerName))
				{
					if(win_lose_draw == 1)
					{
						players.set(counter, playerName + " " + newRating + " " + (wins+1) + " " + losses + " " + draws);
					}
					else if(win_lose_draw == 2)
					{
						players.set(counter, playerName + " " + newRating + " " + wins + " " + (losses+1) + " " + draws);
					}
					else if(win_lose_draw == 3)
					{
						players.set(counter, playerName + " " + newRating + " " + wins + " " + losses + " " + (draws+1));
					}	
				}
				
				counter++;
			}
			try 
			{				
				// Finally we write the new player list back to file (writing over the old file)
				printWriter = new PrintWriter(new FileOutputStream(new File(playerFile),false));
				for(int i = 0; i < players.size(); i++)
				{
					printWriter.println(players.get(i));
				}
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			} 
			finally 
			{
				if (printWriter != null) 
				{
					printWriter.close();
				}
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			if (sc != null) 
			{
				sc.close();
			}
		}
	}
	
	
	/**
	 * 
	 * Returns the highest rated players (or less if there's less than the number "highscoreLength" players registered) in text form(sorted). 
	 * Reads from the player file.
	 * 
	 * @Return highscores This returns a sorted arrayList consisting of the highest rated players and their scores in string form
	 */
	public static ArrayList<String> getHighscores()
	{
		// Setup
		File file = new File(playerFile);	
		Scanner sc = null;	
		ArrayList<String> highscores = new ArrayList<String>();	
		PriorityQueue<playerClass<String, Integer>> pq = new PriorityQueue<playerClass<String, Integer>>();
		
		try 
		{
			// Go through the file and read in players. Store them in the priority queue
			sc = new Scanner(file);		
			while (sc.hasNextLine()) 
			{
				String[] line = sc.nextLine().split(" ");
				String name = line[0];
				int rating = Integer.parseInt(line[1]);
				int wins = Integer.parseInt(line[2]);
				int losses = Integer.parseInt(line[3]);
				int draws = Integer.parseInt(line[4]);

				pq.add(new playerClass<String,Integer>(name, rating, wins, losses, draws));
			}
			
			// Add the highest rated players to the highscores-arrayList
			for(int i = 0; i < pq.size()+1 && i < highscoreLength; i++)
			{
				playerClass<String,Integer> p = pq.poll();
				highscores.add(p.getPlayerName() + " " + p.getRating() + " " + p.getWins() + " " + p.getLosses() + " " + p.getDraws());
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if (sc != null) 
			{
				sc.close();
			}
		}	

		return highscores;
	}
	
	
	/**
	 * 
	 * 	Simple helper-class for the getHighscores method, which allows the priority queue to be used. 
	 * 	Stores a player using his name and score. 
	 */
	@SuppressWarnings("hiding")
	private static class playerClass<String, Integer> implements Comparable<playerClass<String, Integer>>
	{
		public final String playerName;
		public final int rating;
		public final int wins;
		public final int losses;
		public final int draws;
	
		public playerClass(String playerName, int rating, int wins, int losses, int draws)
		{
			this.playerName = playerName;
			this.rating = rating;
			this.wins = wins;
			this.losses = losses;
			this.draws = draws;
		}

	    public String getPlayerName() {
	        return playerName;
	    }

	    public int getRating() {
	        return rating;
	    }
	    
	    public int getWins() {
	        return wins;
	    }
	    
	    public int getLosses() {
	        return losses;
	    }
	    
	    public int getDraws() {
	        return draws;
	    }
	
	    public int compareTo(playerClass<String, Integer> other) 
	    {
	    	int b = this.getRating();
	    	int a = other.getRating();
	    	int cmp = a > b ? +1 : a < b ? -1 : 0;
	    	return cmp;
	    }
	}
}
