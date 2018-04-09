package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.AITests.AIEasyTests;
import tests.AITests.AIMediumTests;
import tests.Database.DatabaseTests;
import tests.boardstructureTests.BoardstructureTestsCollected;
import tests.pieceClassesTests.PieceTestsCollected;

@RunWith(Suite.class)
@SuiteClasses({BoardstructureTestsCollected.class, PieceTestsCollected.class, 
	AIEasyTests.class, AIMediumTests.class, GameTest.class, DatabaseTests.class})

public class AllTests {

}
