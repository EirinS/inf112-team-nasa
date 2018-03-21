package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.MoveType;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.IPiece;
import pieces.PieceColor;

import static pieces.PieceColor.WHITE;
import static pieces.PieceColor.BLACK;


/**
 * This class represents a pawn in the chess game.
 *
 * @author marianne
 */
public class Pawn extends AbstractPiece {

    /**
     * Constructs a pawn
     *
     * @param color the color of the pawn
     */
    public Pawn(PieceColor color) {
        this.color = color;
        this.hasMoved = false;
        this.inPlay = true;
    }

    //TODO en passant
    //TODO pawn promotion

    /**
     * Gets a list of all legal moves for this pawn
     */
    @Override
    protected ArrayList<Move> allFreeMoves(int x, int y, IBoard board, PieceColor playerOne) {
        ArrayList<Move> m = new ArrayList<>();
        Square sq = board.getSquare(x, y);
        m.addAll(reachableSquares(sq, board, playerOne));
        return m;
    }

    /**
     * Checks whether this pawn has moved yet.
     *
     * @return whether this pawn has moved
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * Builds a list of all legal moves for this pawn
     *
     * @param origin the square this pawn moves from
     * @param board  the board this pawn is placed on
     * @return the list of all currently legal moves
     */
    public ArrayList<Move> reachableSquares(Square origin, IBoard board, PieceColor playerOne) {
        ArrayList<Move> reachable = new ArrayList<Move>();
        int x = origin.getX();
        int y = origin.getY();
        int dy = (playerOne == WHITE && color == WHITE
                || playerOne == BLACK && color == BLACK) ? -1 : 1;
        PieceColor opponentColor = playerOne.getOpposite();

        // Check whether the vertical moves are valid
        if (board.withinBoard(x, y + dy)) {
            // Check square straight ahead
            Square oneAhead = board.getSquare(x, y + dy);
            if (oneAhead.isEmpty())
                if ((dy == -1 && (dy + y) == 0) || (dy == 1 && (dy + y) == board.getHeight() - 1))
                    reachable.add(new Move(origin, oneAhead, this, null, MoveType.PROMOTION));
                else
                    reachable.add(new Move(origin, oneAhead, this, null, MoveType.REGULAR));
            // Check whether this pawn can move two squares ahead
            if (board.withinBoard(x, y + 2 * dy)) {
                Square twoAhead = board.getSquare(x, y + 2 * dy);
                if (!hasMoved && oneAhead.isEmpty() && twoAhead.isEmpty())
                    reachable.add(new Move(origin, twoAhead, this, null, MoveType.REGULAR));
            }
        }

        // Check whether diagonal moves are valid
        //if (x != 0) {
        if (board.withinBoard(x - 1, y + dy)) {
            Square westAhead = board.getSquare(x - 1, y + dy);
            if (westAhead.getPiece() != null && westAhead.getPiece().getColor() == opponentColor) {
                if ((dy == -1 && (dy + y) == 0) || (dy == 1 && (dy + y) == board.getHeight() - 1))
                    reachable.add(new Move(origin, westAhead, this, null, MoveType.PROMOTION));
                reachable.add(new Move(origin, westAhead, this, westAhead.getPiece(), MoveType.REGULAR));
            }
        }

        if (board.withinBoard(x + 1, y + dy)) {
            Square eastAhead = board.getSquare(x + 1, y + dy);
            if (eastAhead.getPiece() != null && eastAhead.getPiece().getColor() == opponentColor) {
                if ((dy == -1 && (dy + y) == 0) || (dy == 1 && (dy + y) == board.getHeight() - 1))
                    reachable.add(new Move(origin, eastAhead, this, null, MoveType.PROMOTION));
                reachable.add(new Move(origin, eastAhead, this, eastAhead.getPiece(), MoveType.REGULAR));
            }
        }
        //}
        return reachable;
    }

    @Override
    public IPiece copy() {
        Pawn p = new Pawn(this.getColor());
        if (this.hasMoved())
            p.pieceMoved();
        return p;
    }

    /**
     * Returns a string that represents this pawn.
     */
    @Override
    public String toString() {
        return "P";
    }
}
