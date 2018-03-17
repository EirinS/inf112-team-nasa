package boardstructure;

import java.util.ArrayList;
import java.util.List;

import pieces.IPiece;
import pieces.PieceColor;

public class Board implements IBoard {
	private ArrayList<Move> history = new ArrayList<>();
	private int height;
	private int width;
	private ArrayList<Square> board;

	/**
	 * Create new board.
	 * 
	 * @param dim
	 *            Board is always square. Dim is the height and width of board.
	 */
	public Board(int dim) {
		if (dim < 0)
			throw new IllegalArgumentException("Board must be larger than 0 in heigth and width");
		height = dim;
		width = dim;
		board = new ArrayList<Square>();
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				board.add(new Square(i, j));
			}
		}
	}

	public List<Move> getAvailableMoves(PieceColor playerColor) {

		ArrayList<Move> moves = new ArrayList<>();

		for(Square s : board) {
			IPiece piece = s.getPiece();

			if(piece != null && piece.getColor() == playerColor) {
				List<Move> moveList = piece.getLegalMoves(s, this);
				moves.addAll(moveList);
			}
		}
		return moves;
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
		if (!withinBoard(x,y)) {
			throw new IllegalArgumentException("Cannot look for squares outside the board");
		}
		return board.get(x * width + y);
	}

	@Override
	public int getBoardPlacement(Square sq) {
		if (!withinBoard(sq))
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
		if (!withinBoard(sq))
			throw new IllegalArgumentException("Cannot place a square outside the board");
		else {
			board.add(getBoardPlacement(sq), sq);
		}
	}

	@Override
	public boolean withinBoard(Square sq) {
		return withinBoard(sq.getX(), sq.getY());
	}

	@Override
	public boolean withinBoard(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	@Override
	public ArrayList<IPiece> piecesThreatenedByOpponent(PieceColor player, PieceColor opponent) {
		return threatenedPieces(opponent, player);
	}

	/**
	 * Helper method to get threatened pieces.
	 * 
	 * @param PieceColor
	 *            threatening, color of the opponent
	 * @param PieceColor
	 *            gettingThreatened, your color
	 * @return ArrayList<IPiece> of your threatened pieces
	 */
	private ArrayList<IPiece> threatenedPieces(PieceColor opponent, PieceColor player) {
		ArrayList<IPiece> reached = new ArrayList<IPiece>();
		for (int i = 0; i < board.size(); i++) {
			Square sq = board.get(i);
			if (!sq.isEmpty()) {
				IPiece p = sq.getPiece();
				if (p.getColor() == opponent) {
					ArrayList<IPiece> pieces = p.enemyPiecesReached(sq.getX(), sq.getY(), this, player);
					if (pieces != null) {
						//check if this piece is already reached by another piece on the board.
						for(IPiece reachedPiece : pieces) {
							if (!reached.contains(reachedPiece)) {
								reached.add(reachedPiece);
							}
						}
					}
				}
			}
		}
		return reached;
	}


	@Override
	public ArrayList<Move> getHistory() {
		return history;
	}

	@Override
	public Move move(Square from, Square to) {
		if (from == null) {
			//tell user this is illegal
		}
		
		IPiece moving = from.getPiece();
		ArrayList<Move> legalMoves = moving.getLegalMoves(from, this);
		for(Move m : legalMoves) {
			if (m.getTo() == to) {
				return doMove(m);
			}
		}
		// error message to tell user move is illegal.
		return null;
	}
	
	/**
	 * Finds and executes the chosen move.
	 * @param Move m, the move that you'll do
	 * @return the move done
	 */
	private Move doMove(Move m) {
		if(m.getMoveType() == MoveType.ENPASSANT) {
			//TODO:
		} else if (m.getMoveType() == MoveType.KINGSIDECASTLING) {
			//TODO:
		} else if(m.getMoveType() == MoveType.QUEENSIDECASTLING) {
			//TODO:
		} else if (m.getMoveType() == MoveType.PROMOTION) {
			//TODO:
			
			//regular moves
		} else if (!m.getTo().isEmpty()){ 
			//move and capture piece
			m.getFrom().getPiece().captureEnemyPieceAndMovePiece(m.getFrom(), m.getTo());
			history.add(m);
			return m;
		} else {
			m.getFrom().getPiece().movePiece(m.getFrom(), m.getTo());
			history.add(m);
			return m;			
		}
		return null;
	}

	public void printOutBoard(){
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				IPiece p = getSquare(i,j).getPiece();

				if(p == null) {
					System.out.println(" ");
				}else {
					System.out.println(p);
				}
			}
		}
	}

}
