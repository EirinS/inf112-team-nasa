package boardstructure;

import java.util.ArrayList;
import java.util.List;

import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.King;

public class Board implements IBoard {

	private ArrayList<Move> history = new ArrayList<>();
	private int height;
	private int width;
	private ArrayList<Square> board;
	private PieceColor playerOne;

	/**
	 * Create new board.
	 * 
	 * @param dim
	 *            Board is always square. Dim is the height and width of board.
	 * @param playerOne pieceColor of player one (player in lower side of board)	
	 */
	public Board(int dim, PieceColor playerOne) {
		if (dim < 0)
			throw new IllegalArgumentException("Board must be larger than 0 in heigth and width");
		this.playerOne = playerOne;
		height = dim;
		width = dim;
		board = new ArrayList<Square>();
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				board.add(new Square(i, j));
			}
		}
	}
	

	@Override
	public List<Move> getAvailableMoves(PieceColor playerColor) {

		ArrayList<Move> moves = new ArrayList<>();

		for(Square s : board) {
			IPiece piece = s.getPiece();

			if(piece != null && piece.getColor() == playerColor) {
				List<Move> moveList = piece.getLegalMoves(s, this, playerOne);
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

		// TODO: 18/03/2018 Bishop/Knight/etc... bug here
		if (!withinBoard(x,y)) {
			throw new IllegalArgumentException("Cannot look for squares outside the board: (" + x + ", " + y + ")");
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
	public ArrayList<Square> getSquares() {
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
	 * @param opponent
	 *            threatening, color of the opponent
	 * @param player
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (board == null) {
			if (other.board != null)
				return false;
		} else if (!board.equals(other.board))
			return false;
		if (height != other.height)
			return false;
		if (history == null) {
			if (other.history != null)
				return false;
		} else if (!history.equals(other.history))
			return false;
		if (playerOne != other.playerOne)
			return false;
		if (width != other.width)
			return false;
		return true;
	}


	@Override
	public ArrayList<Move> getHistory() {
		return history;
	}

	@Override
	public Move getLastMove() {
		if (history.isEmpty()) return null;
		return history.get(history.size() - 1);
	}

	@Override
	public ArrayList<Move> getMove(int fromX, int fromY, int toX, int toY) {
		return move(getSquare(fromX, fromY), getSquare(toX, toY));
	}

	@Override
	public ArrayList<Move> move(Square from, Square to) {
		if (from == null || to == null) {
			return new ArrayList<>();
			//tell user this is illegal
		}
		
		IPiece moving = from.getPiece();
		ArrayList<Move> legalMoves = moving.getLegalMoves(from, this, playerOne);
		for(Move m : legalMoves) {
			if (m.getTo() == to) {
				return doMove(m);
			}
		}
		// error message to tell user move is illegal.
		return new ArrayList<>();
	}
	
	/**
	 * Finds and executes the chosen move.
	 * @param m, the move that you'll do
	 * @return the move done
	 */
	private ArrayList<Move> doMove(Move m) {
		ArrayList<Move> moves = new ArrayList<>();
		if(m.getMoveType() == MoveType.ENPASSANT) {
			//TODO:
		} else if (m.getMoveType() == MoveType.KINGSIDECASTLING) {
			IPiece moving = m.getMovingPiece();
			if(moving instanceof King) {
				Move rookMove = ((King) moving).moveCastling(m.getFrom(), m.getTo(), MoveType.KINGSIDECASTLING, this);
				if (rookMove != null) {
					moves.add(m);
					moves.add(rookMove);
				}
				history.add(m);
			}
		} else if(m.getMoveType() == MoveType.QUEENSIDECASTLING) {
			if(m.getMovingPiece() instanceof King) {
				Move rookMove = ((King) m.getMovingPiece()).moveCastling(m.getFrom(), m.getTo(), MoveType.QUEENSIDECASTLING, this);
				if (rookMove != null) {
					moves.add(m);
					moves.add(rookMove);
				}
				history.add(m);
			}
		} else if (m.getMoveType() == MoveType.PROMOTION) {
			//TODO:
			//regular moves
		} else if (!m.getTo().isEmpty()){ 
			//move and capture piece
			m.getFrom().getPiece().captureEnemyPieceAndMovePiece(m.getFrom(), m.getTo());
			printOutBoard();
			history.add(m);
			moves.add(m);
		} else {
			m.getFrom().getPiece().movePiece(m.getFrom(), m.getTo());
			printOutBoard();
			history.add(m);
			moves.add(m);
		}
		printOutBoard();
		return moves;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		result = prime * result + height;
		result = prime * result + ((history == null) ? 0 : history.hashCode());
		result = prime * result + ((playerOne == null) ? 0 : playerOne.hashCode());
		result = prime * result + width;
		return result;
	}


	public void printOutBoard(){
		System.out.println("--------");
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				IPiece p = getSquare(j,i).getPiece();

				if(p == null) {
					System.out.print("_");
				}else {
					System.out.print(p);
				}
			}
			System.out.println("");
		}
	}

	@Override
	public Square getKingPos(PieceColor kingColor) {
		for(Square sq : getBoard())
			if (sq.getPiece() instanceof King && sq.getPiece().getColor() == kingColor) {
				return sq;
			}
		return null;
	}

	@Override
	public ArrayList<Square> getBoard() {
		return board;
	}


	@Override
	public IBoard copy() {
		IBoard board = new Board(this.getDimension(), PieceColor.WHITE);
		for(Square sq : getBoard()) {
			int x = sq.getX(), y = sq.getY();
			if(sq.isEmpty())
				continue;
			IPiece p = sq.getPiece().copy();
			Square newSq = new Square(x,y);
			newSq.putPiece(p);
			board.addSquare(newSq);
		}
		return board;
	}

}
