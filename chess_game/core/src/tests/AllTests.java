package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.AITests.AIEasyTests;
import tests.AITests.AIMediumTests;
import tests.boardstructureTests.BoardTest;
import tests.boardstructureTests.BoardstructureTestsCollected;
import tests.boardstructureTests.SquareTest;
import tests.pieceClassesTests.PieceTestsCollected;
import tests.playerFileTests.RegisteredPlayersTest;

@RunWith(Suite.class)
@SuiteClasses({BoardstructureTestsCollected.class, PieceTestsCollected.class, 
	AIEasyTests.class, AIMediumTests.class, RegisteredPlayersTest.class,
	GameTest.class})

public class AllTests {

}
