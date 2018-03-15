package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.boardstructureTests.BoardTest;
import tests.boardstructureTests.SquareTest;
import tests.pieceClassesTests.PieceTestsCollected;

@RunWith(Suite.class)
@SuiteClasses({ BoardTest.class, SquareTest.class, PieceTestsCollected.class})
public class AllTests {

}
