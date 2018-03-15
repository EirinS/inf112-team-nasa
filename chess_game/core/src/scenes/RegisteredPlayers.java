package scenes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;


/**
 * 
 * @author Stian
 * 
 */
public class RegisteredPlayers
{

	private static final int defaultRating = 1500;
	private static final int highscoreLength = 10;
	private static final String playerFile ="playerFile.txt";
	
	/**
	 * This method checks if the given name already occurs in the player file.
	 * Reads from the player file
	 * 
	 * @param playerName Name of the player we want to check
	 * @return bool returns True if player is registered and False if not
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
	 * Writes to the player file. 
	 * 
	 * @param playerName Name of the player
	 * 
	 */
	public static void registerPlayer(String playerName) 
	{
		PrintWriter printWriter = null;

		try 
		{
			// Simply appends the new player to the player file with the default rating
			printWriter = new PrintWriter(new FileOutputStream(new File(playerFile),true));
			printWriter.println(playerName + " " + defaultRating);
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
	 */
	public static void updatePlayerRating(String playerName, int newRating) 
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
				
				// If we find the player we are searching for we update his rating in the arraylist
				if(players.get(counter).split(" ")[0].equals(playerName))
				{
					players.set(counter, playerName + " " + newRating);
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
		PriorityQueue<playerScorePair<String, Integer>> pq = new PriorityQueue<playerScorePair<String, Integer>>();
		
		try 
		{
			// Go through the file and read in players. Store them in the priority queue
			sc = new Scanner(file);		
			while (sc.hasNextLine()) 
			{
				String[] line = sc.nextLine().split(" ");
				String name = line[0];
				int score = Integer.parseInt(line[1]);

				pq.add(new playerScorePair<String,Integer>(name, score));
			}
			
			// Add the highest rated players to the highscores-arrayList
			for(int i = 0; i < pq.size() && i < highscoreLength; i++)
			{
				playerScorePair<String,Integer> p = pq.poll();
				highscores.add(p.getPlayerName() + " " + p.getScore());
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
	private static class playerScorePair<String, Integer> implements Comparable<playerScorePair<String, Integer>>
	{
		public final String playerName;
		public final int score;
	
		public playerScorePair(String playerName, int score)
		{
			this.playerName = playerName;
			this.score = score;
		}

	    public String getPlayerName() {
	        return playerName;
	    }

	    public int getScore() {
	        return score;
	    }
	
	    public int compareTo(playerScorePair<String, Integer> other) 
	    {
	    	int b = this.getScore();
	    	int a = other.getScore();
	    	int cmp = a > b ? +1 : a < b ? -1 : 0;
	    	return cmp;
	    }
}
	
	
}
