package boardstructure;

import java.util.ArrayList;

import pieces.IPiece;
import pieces.PieceColor;

public class Board implements IBoard {
	private int height;
	private int width;
	private ArrayList<Square> board;

	/**
	 * Create new board.
	 * @param dim Board is always square. Dim is the height and width of board.
	 */
	public Board (int dim) {
		if(dim < 0)
			throw new IllegalArgumentException("Board must be larger than 0 in heigth and width");
		height = dim;
		width = dim;
		board = new ArrayList<Square>();
		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				board.add(new Square(i,j));
			}
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Square getSquare(int x, int y) {
		if(x < 0 || x > getDimension() || y < 0 || y > getDimension()) {
			throw new IllegalArgumentException("Cannot look for squares outside the board");
		}
		return board.get(x * width + y);
	}

	@Override
	public int getBoardPlacement(Square sq) {
		if(!withinBoard(sq))
			throw new IllegalArgumentException("Piece must be inside board to find");
		return (sq.getX() * width + sq.getY());
	}

	@Override
	public ArrayList<Square> getBoard() {
		return board;
	}

	@Override
	public boolean movable(Square sq) {
		if (!withinBoard(sq) || !sq.isEmpty())
			return false;
		return true;
	}

	@Override
	public int getDimension() {
		return height;
	}

	@Override
	public void addSquare(Square sq) {
		if(!withinBoard(sq))
			throw new IllegalArgumentException("Cannot place a square outside the board");
		else {
			board.add(getBoardPlacement(sq), sq);
		}
	}

	@Override
	public boolean withinBoard(Square sq) {
		if(sq.getX() >= width || sq.getY() >= height || sq.getX() < 0 || sq.getY() < 0)
			return false;
		return true;
	}

	@Override
	public ArrayList<IPiece> piecesThreatenedByOpponent(PieceColor player, PieceColor opponent){
		return threatenedPieces(opponent, player);
	}

	/**
	 * Helper method to get threatened pieces.
	 * @param PieceColor threatening, color of the opponent
	 * @param PieceColor gettingThreatened, your color
	 * @return ArrayList<IPiece> of your threatened pieces
	 */
	private ArrayList<IPiece> threatenedPieces(PieceColor opponent, PieceColor player) {
		ArrayList<IPiece> reached = new ArrayList<IPiece>();
		for(int i = 0; i < board.size(); i++) {
			Square sq = board.get(i);
			if(!sq.isEmpty()) {
				IPiece p = sq.getPiece();
				if(p.getColor() == opponent) {
					ArrayList<IPiece> pieces = p.enemyPiecesReached(sq.getX(), sq.getY(), this, player);
					if (pieces != null)
						reached.addAll(pieces);
				}
			}
		}
		return reached;
	}

}


