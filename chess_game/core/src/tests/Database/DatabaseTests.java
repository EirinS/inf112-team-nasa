package tests.Database;

import db.Database;
import org.junit.Before;
import org.junit.Test;
import db.Player;

import java.sql.SQLException;

import static org.junit.Assert.fail;

public class DatabaseTests {

    private static final String TEST_USER = "test_user_1337";
    private Database db;

    @Before
    public void setUp() {
        db = new Database();
    }

    @Test
    public void listPlayersTest() {
        try {
            db.listPlayers();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Should not fail");
        }
    }

    private void registerPlayerTest() {
        try {
            db.registerPlayer(new Player(TEST_USER));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Should not fail");
        }
    }

    @Test
    public void registerAndUpdatePlayerTest() {

        // Make sure test-user exists.
        registerPlayerTest();

        // Update test-user
        try {
            db.updatePlayer(TEST_USER, 2000, 1);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Should not fail");
        }
    }
}
