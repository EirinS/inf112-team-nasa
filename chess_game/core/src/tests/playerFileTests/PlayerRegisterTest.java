package tests.playerFileTests;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import register.PlayerRegister;

public class PlayerRegisterTest {

    private PlayerRegister playerRegister = new PlayerRegister("testfile.txt");

    @Before
    public void cleanFile() {
        File file = new File("testfile.txt");
        PrintWriter writer;
        try {
            writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        playerRegister.loadPlayers();
    }

    /**
     * Tests may fail if run several times
     */
    @Test
    public void registerAndCheckTest() {
        playerRegister.registerPlayer("player1");
        assertTrue(playerRegister.playerIsRegistered("player1"));

        assertFalse(playerRegister.playerIsRegistered("player2"));
        playerRegister.registerPlayer("player2");
        assertTrue(playerRegister.playerIsRegistered("player2"));


        assertFalse(playerRegister.playerIsRegistered("player3"));
        playerRegister.registerPlayer("player3");
        assertTrue(playerRegister.playerIsRegistered("player3"));

        assertFalse(playerRegister.playerIsRegistered("player4"));
    }

    @Test
    public void updateAndHighscoreTest() {
        playerRegister.registerPlayer("player1");
        playerRegister.registerPlayer("player2");

        playerRegister.updatePlayerRating("player1", 2000, 1);

        ArrayList<String> highScores = playerRegister.getHighscores();

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
