package tests.AITests;

import static org.junit.Assert.*;

import org.junit.Test;

import boardstructure.Board;
import boardstructure.Move;
import pieces.PieceColor;
import player.AIEasy;
import player.AIHard;
import player.AIMedium;
import setups.DefaultSetup;

public class AIHardTest {

	
	@Test
	public void testAIHardVSAIMediumFullGame() {
		DefaultSetup d = new DefaultSetup();
		Board boardT = d.getInitialPosition(PieceColor.WHITE);
		String msg1 = "The test HardVSMedium ended after ";
		String msg2 = " moves. ";
		String nr = "50";
		AIHard p1 = new AIHard(PieceColor.WHITE);
		AIMedium p2 = new AIMedium(PieceColor.BLACK);
		for (int i=0;i<50;i++) {
			if (boardT.getAvailableMoves(p1.getPieceColor()).isEmpty()) {
				nr = i+"";
				msg2 = msg2 + "White has no moves (is checkmate or there is a draw)";
				break;
			}else {
				Move p1Move = p1.calculateMove(boardT);
				boardT.move(p1Move.getFrom(), p1Move.getTo());
				boardT.printOutBoard();//
			}if (boardT.getAvailableMoves(p2.getPieceColor()).isEmpty()) {
				nr = i+"";
				msg2 = msg2 + "Black has no moves (is checkmate or there is a draw)";
				break;
			}else {
				Move p2Move = p2.calculateMove(boardT);
				boardT.move(p2Move.getFrom(), p2Move.getTo());
				boardT.printOutBoard();//
			}
		}
		System.out.println(msg1 + nr + msg2);
	}
	/*
	@Test
	public void testAIMadiumVSAIHardFullGame() {
		DefaultSetup d = new DefaultSetup();
		Board boardT = d.getInitialPosition(PieceColor.WHITE);
		String msg1 = "The test HardVSMedium ended after ";
		String msg2 = " moves. ";
		String nr = "50";
		AIMedium p1 = new AIMedium(PieceColor.WHITE);
		AIHard p2 = new AIHard(PieceColor.BLACK);
		for (int i=0;i<50;i++) {
			if (boardT.getAvailableMoves(p1.getPieceColor()).isEmpty()) {
				nr = i+"";
				msg2 = msg2 + "White has no moves (is checkmate or there is a draw)";
				break;
			}else {
				Move p1Move = p1.calculateMove(boardT);
				boardT.move(p1Move.getFrom(), p1Move.getTo());
				boardT.printOutBoard();//
			}if (boardT.getAvailableMoves(p2.getPieceColor()).isEmpty()) {
				nr = i+"";
				msg2 = msg2 + "Black has no moves (is checkmate or there is a draw)";
				break;
			}else {
				Move p2Move = p2.calculateMove(boardT);
				boardT.move(p2Move.getFrom(), p2Move.getTo());
				boardT.printOutBoard();//
			}
		}
		System.out.println(msg1 + nr + msg2);
	}
	*/
	/*
	@Test
	public void testAIHardVSAIEasyFullGame() {
		DefaultSetup d = new DefaultSetup();
		Board boardT = d.getInitialPosition(PieceColor.WHITE);
		String msg1 = "The test HardVSEasy ended after ";
		String msg2 = " moves. ";
		String nr = "50";
		AIHard p1 = new AIHard(PieceColor.WHITE);
		AIEasy p2 = new AIEasy(PieceColor.BLACK);
		for (int i=0;i<50;i++) {
			if (boardT.getAvailableMoves(p1.getPieceColor()).isEmpty()) {
				nr = i+"";
				msg2 = msg2 + "White has no moves (is checkmate or there is a draw)";
				break;
			}else {
				Move p1Move = p1.calculateMove(boardT);
				boardT.move(p1Move.getFrom(), p1Move.getTo());
				boardT.printOutBoard();//
			}if (boardT.getAvailableMoves(p2.getPieceColor()).isEmpty()) {
				nr = i+"";
				msg2 = msg2 + "Black has no moves (is checkmate or there is a draw)";
				break;
			}else {
				Move p2Move = p2.calculateMove(boardT);
				boardT.move(p2Move.getFrom(), p2Move.getTo());
				boardT.printOutBoard();//
			}
		}
		System.out.println(msg1 + nr + msg2);
	}
*/
}
