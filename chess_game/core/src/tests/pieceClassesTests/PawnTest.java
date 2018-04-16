package tests.pieceClassesTests;

import static org.junit.Assert.*;

import boardstructure.*;
import org.junit.Before;
import org.junit.Test;

import pieces.AbstractPiece;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.King;
import pieces.pieceClasses.Pawn;

import static pieces.PieceColor.WHITE;

import java.util.ArrayList;

import static pieces.PieceColor.BLACK;

public class PawnTest {
    PieceColor playerOne = WHITE;
    PieceColor playerTwo = BLACK;
    IBoard board = new Board(8, playerOne);
    IPiece whitePawn = new Pawn(playerOne);
    Square sq = board.getSquare(3, 6);

    @Before
    public void setUp() {
        sq.putPiece(whitePawn);
    }

    @Test
    public void movingTwoSquaresIsPawnJump() {
        IBoard b = new Board(8, PieceColor.WHITE);
        Square from = b.getSquare(0, 6);
        Pawn p = new Pawn(PieceColor.WHITE);
        from.putPiece(p);
        ArrayList<Move> moves = (p.getLegalMoves(from, b, PieceColor.WHITE));
        for (Move m : moves)
            if (m.getTo().getY() != 5)
                assertEquals(MoveType.PAWNJUMP, m.getMoveType());
    }

    @Test
    public void enPassantIsValidForEnPassant() {
        Board b = new Board(8, PieceColor.WHITE);
        Square from = b.getSquare(0, 6);
        Square to = b.getSquare(0, 4);
        Pawn o = new Pawn(PieceColor.BLACK);
        b.getSquare(1, 4).putPiece(o);
        Pawn p = new Pawn(PieceColor.WHITE);
        from.putPiece(p);
        b.move(from, to);
        Move m = (o.getPassantMove(1, 4, b));
        assertEquals(new Move(b.getSquare(1, 4), b.getSquare(0, 5), o, p, MoveType.ENPASSANT), m);
    }

    @Test
    public void pawnCanFindLegalEnPassantMoves() {
        Board b = new Board(8, PieceColor.WHITE, new BoardListener() {

            @Override
            public void promotionRequested(Move move) {}

            @Override
            public void movePerformed(Board board, ArrayList<Move> moves) {
                Pawn o = (Pawn) board.getSquare(1, 4).getPiece();
                for (Move m : o.reachableSquares(board.getSquare(1, 4), board, PieceColor.WHITE))
                    if (m.getMoveType() == MoveType.ENPASSANT)
                        return;
                fail("NUUUU");
            }

            @Override
            public void illegalMovePerformed(int fromX, int fromY) {
                fail("NUUUU");
            }
        });
        Pawn p = new Pawn(PieceColor.WHITE);
        Pawn o = new Pawn(PieceColor.BLACK);
        b.getSquare(1, 4).putPiece(o);
        Square from = b.getSquare(0, 6);
        Square to = b.getSquare(0, 4);
        from.putPiece(p);
        b.move(from, to);
    }

    @Test
    public void movingTwoSquaresIsPawnJumpWorksForMove() {
        IBoard b = new Board(8, PieceColor.WHITE, new BoardListener() {

            @Override
            public void promotionRequested(Move move) {}

            @Override
            public void movePerformed(Board board, ArrayList<Move> moves) {
                for (Move mov : moves)
                    assertEquals(MoveType.PAWNJUMP, mov.getMoveType());
            }

            @Override
            public void illegalMovePerformed(int fromX, int fromY) {

            }
        });
        Square from = b.getSquare(0, 6);
        Square to = b.getSquare(0, 4);
        Pawn p = new Pawn(PieceColor.WHITE);
        from.putPiece(p);
        b.move(from, to);
    }

    @Test
    public void blackPawnCanCaptureOpponentToTheEast() {
        IPiece blackPawn = new Pawn(playerTwo);
        Square blackSq = board.getSquare(2, 5);
        blackSq.putPiece(blackPawn);
        if (blackPawn.getLegalMoves(blackSq, board, playerOne)
                .stream().anyMatch(m -> m.getTo().equals(sq)))
            return;
        fail("Black pawn could not capture present opponent to the east");
    }

    @Test
    public void blackPawnCanCaptureOpponentToTheWest() {
        IPiece blackPawn = new Pawn(BLACK);
        Square blackSq = board.getSquare(4, 5);
        blackSq.putPiece(blackPawn);
        if (blackPawn.getLegalMoves(blackSq, board, playerOne)
                .stream().anyMatch(m -> m.getTo().equals(sq)))
            return;
        fail("Black pawn could not capture present opponent to the west");
    }

    @Test
    public void blackPawnOnRowOneCanFindPromotion() {
        IBoard board = new Board(8, playerTwo);
        Pawn p = new Pawn(playerTwo);
        Square sq = board.getSquare(0, 1);
        sq.putPiece(p);

        for (Move m : p.getLegalMoves(sq, board, playerTwo))
            if (m.getMoveType() == MoveType.PROMOTION)
                return;
        fail("No promotion move found.");
    }

    @Test
    public void whitePawnOnRowSixCanFindPromotion() {
        IBoard board = new Board(8, playerOne);
        Pawn p = new Pawn(playerTwo);
        Square sq = board.getSquare(0, 6);
        sq.putPiece(p);

        for (Move m : p.getLegalMoves(sq, board, playerTwo))
            if (m.getMoveType() == MoveType.PROMOTION)
                return;
        fail("No promotion move found.");
    }

    @Test
    public void pawnAppearsCorrectlyOnTheBoard() {
        assertEquals(whitePawn, sq.getPiece());
    }

    @Test
    public void pawnCannotMoveDiagonallyWithoutEnemiesPresent() {
        if (whitePawn.getLegalMoves(sq, board, playerOne)
                .stream().anyMatch(m -> m.getTo().getX() != sq.getX()))
            fail("Pawn should not be able to move diagonally without the presence of enemies"
                    + "on the immidiate diagonal squares");
    }

    @Test
    public void pawnCannotMoveThroughOtherPieces() {
        IPiece otherPawn = new Pawn(playerOne);
        Square otherSq = board.getSquare(3, 5);
        otherSq.putPiece(otherPawn);
        if (whitePawn.getLegalMoves(sq, board, playerOne)
                .stream().anyMatch(m -> m.getTo().equals(otherSq)))
            fail("A pawn should not be able to move to the square ahead if"
                    + " it is occupied by another piece of the same color");
    }

    @Test
    public void pawnCanOnlyMoveForwardsWhenNotCapturing() {
        if (whitePawn.getLegalMoves(sq, board, playerOne)
                .stream().anyMatch(m -> m.getTo().getX() != sq.getX()))
            fail("A pawn that does not threaten any opponents should"
                    + " either have no legal moves or only be able to move forward");
    }

    @Test
    public void playerOnePawnMovesNorth() {
        IPiece playerOnePawn = new Pawn(playerOne);
        Square playerOneSq = board.getSquare(1, 5);
        playerOneSq.putPiece(playerOnePawn);
        if (playerOnePawn.getLegalMoves(playerOneSq, board, playerOne)
                .stream().anyMatch(m -> m.getTo().getY() >= playerOneSq.getY()))
            fail("Pawns belonging to player one should move north");
    }

    @Test
    public void playerTwoPawnMovesSouth() {
        IPiece playerTwoPawn = new Pawn(playerTwo);
        Square playerTwoSq = board.getSquare(1, 1);
        playerTwoSq.putPiece(playerTwoPawn);
        if (playerTwoPawn.getLegalMoves(playerTwoSq, board, playerOne)
                .stream().anyMatch(m -> m.getTo().getY() <= playerTwoSq.getY()))
            fail("Pawns belonging to player two should move south");
    }

    @Test
    public void whitePawnCanCaptureOpponentToTheEast() {
        IPiece opponentPawn = new Pawn(playerTwo);
        Square opponentSq = board.getSquare(4, 5);
        opponentSq.putPiece(opponentPawn);
        if (whitePawn.getLegalMoves(sq, board, playerOne)
                .stream().anyMatch(m -> m.getTo().equals(opponentSq)))
            return;
        fail("White pawn could not capture present opponent to the east");
    }

    @Test
    public void whitePawnCanCaptureOpponentToTheWest() {
        IPiece opponentPawn = new Pawn(playerTwo);
        Square opponentSq = board.getSquare(2, 5);
        opponentSq.putPiece(opponentPawn);
        if (whitePawn.getLegalMoves(sq, board, playerOne)
                .stream().anyMatch(m -> m.getTo().equals(opponentSq)))
            return;
        fail("White pawn could not capture present opponent to the west");
    }

    @Test
    public void pawnCanThreatenKing() {
        IBoard newboard = new Board(8, PieceColor.WHITE);
        Pawn p = new Pawn(PieceColor.WHITE);
        King k = new King(PieceColor.BLACK);
        newboard.getSquare(0, 7).putPiece(p);
        newboard.getSquare(1, 6).putPiece(k);
        assertTrue(p.threatensKing(p.enemyPiecesReached(0, 7, newboard, PieceColor.BLACK)));
    }

    @Test
    public void kingIsThreatenedByPawn() {
        IBoard newboard = new Board(8, PieceColor.WHITE);
        Pawn p = new Pawn(PieceColor.WHITE);
        King k = new King(PieceColor.BLACK);
        //pawn in edge of board, second row from bottom, pawn moving upwards
        newboard.getSquare(0, 6).putPiece(p);
        //king in edge of board, third row from bottom
        newboard.getSquare(0, 5).putPiece(k);

        //king should not be allowed to move to a diagonal square from pawn
        boolean isLegal = false;
        for (Move m : k.getLegalMoves(newboard.getSquare(0, 5), newboard, PieceColor.WHITE)) {
            if (m.getTo().getX() == 1 && m.getTo().getY() == 5)
                isLegal = true;
        }
        assertFalse(isLegal);
    }

    @Test
    public void pawnCannotThreatenKingForward() {
        board.getSquare(3, 5).putPiece(new King(PieceColor.BLACK));
        assertFalse(((AbstractPiece) whitePawn).threatensKing(whitePawn.enemyPiecesReached(3, 6, board, PieceColor.BLACK)));
    }

    @Test
    public void pawnCanThreatenKingEast() {
        board.getSquare(4, 5).putPiece(new King(PieceColor.BLACK));
        assertTrue(((AbstractPiece) whitePawn).threatensKing(whitePawn.enemyPiecesReached(3, 6, board, PieceColor.BLACK)));
    }

    @Test
    public void pawnCanThreatenKingWest() {
        board.getSquare(2, 5).putPiece(new King(PieceColor.BLACK));
        assertTrue(((AbstractPiece) whitePawn).threatensKing(whitePawn.enemyPiecesReached(3, 6, board, PieceColor.BLACK)));
    }
}
