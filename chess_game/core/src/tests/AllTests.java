package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.AITests.AIEasyTests;
import tests.AITests.AIMediumTests;
import tests.boardstructureTests.BoardstructureTestsCollected;
import tests.pieceClassesTests.PieceTestsCollected;
import tests.playerFileTests.PlayerRegisterTest;

@RunWith(Suite.class)
@SuiteClasses({BoardstructureTestsCollected.class, PieceTestsCollected.class, 
	AIEasyTests.class, AIMediumTests.class, PlayerRegisterTest.class,
	GameTest.class})

public class AllTests {

}
