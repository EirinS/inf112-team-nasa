package tests.playerFileTests;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Test;

import register.RegisteredPlayers;

public class RegisteredPlayersTest {

	RegisteredPlayers rp = new RegisteredPlayers("testfile.txt");
	
	
	/**
	 * Tests may fail if run several times
	 */
	@Test
	public void registerAndCheckTest() 
	{
		// Clearing the file
		File file = new File("testfile.txt");
		PrintWriter writer;
		try {
			writer = new PrintWriter(file);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		RegisteredPlayers.registerPlayer("player1");		
		assertTrue(RegisteredPlayers.playerIsRegistered("player1"));

		assertFalse(RegisteredPlayers.playerIsRegistered("player2"));
		RegisteredPlayers.registerPlayer("player2");
		assertTrue(RegisteredPlayers.playerIsRegistered("player2"));
		

		assertFalse(RegisteredPlayers.playerIsRegistered("player3"));
		RegisteredPlayers.registerPlayer("player3");
		assertTrue(RegisteredPlayers.playerIsRegistered("player3"));
		
		assertFalse(RegisteredPlayers.playerIsRegistered("player4"));	
	}
	
	@Test 
	public void updateAndHighscoreTest()
	{
		
		// Clearing the file
		File file = new File("testfile.txt");
		PrintWriter writer;
		try {
			writer = new PrintWriter(file);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		RegisteredPlayers.registerPlayer("player1");
		RegisteredPlayers.registerPlayer("player2");
		
		RegisteredPlayers.updatePlayerRating("player1", 2000, 1);
		
		ArrayList<String> highScores = RegisteredPlayers.getHighscores();
		
		assertTrue(Integer.parseInt(highScores.get(0).split(" ")[1]) == 2000);
		assertTrue(Integer.parseInt(highScores.get(0).split(" ")[2]) > 0);
		assertTrue(Integer.parseInt(highScores.get(0).split(" ")[3]) == 0);
		assertTrue(Integer.parseInt(highScores.get(0).split(" ")[4]) == 0);
		
		assertTrue(Integer.parseInt(highScores.get(1).split(" ")[1]) == 1500);
		assertTrue(Integer.parseInt(highScores.get(1).split(" ")[2]) == 0);
		assertTrue(Integer.parseInt(highScores.get(1).split(" ")[3]) == 0);
		assertTrue(Integer.parseInt(highScores.get(1).split(" ")[4]) == 0);
		
	}

}
