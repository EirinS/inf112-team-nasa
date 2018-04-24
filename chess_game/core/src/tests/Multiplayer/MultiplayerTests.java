package tests.Multiplayer;

import models.MultiplayerGame;
import multiplayer.IMultiplayer;
import multiplayer.Multiplayer;
import multiplayer.MultiplayerListener;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MultiplayerTests {

    @Test
    public void gamesListTest() {

        // TODO: 25/04/2018 always working for some reason???
        IMultiplayer multiplayer = new Multiplayer(new MultiplayerListener() {

            @Override
            public void gamesListed(List<MultiplayerGame> games) {
                System.out.println(123);
                assertTrue(games != null);
            }

            @Override
            public void gameCreated() {}

            @Override
            public void gameJoined() {}

            @Override
            public void error(Throwable t) {
                t.printStackTrace();
                fail("Error: " + t.toString());
            }

            @Override
            public void unexpectedError() {
                fail("Should not happend");
            }
        });
        multiplayer.listGames();
    }
}
