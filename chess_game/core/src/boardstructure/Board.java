package boardstructure;

import java.util.ArrayList;

public class Board implements IBoard {
	private int height;
	private int width;
	private ArrayList<Square> board;

	public Board (int h, int w) {
		if(h <= 0 || w <= 0)
			throw new IllegalArgumentException("Board must be larger than 0 in heigth and width");
		height = h;
		width = w;
		board = new ArrayList<>();
		//TODO: standard piece setup??
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
		return board.get(y * width + x);
	}

	@Override
	public ArrayList<Square> getBoard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean moveable(Square sq) {
		if (sq.getX() >= width || sq.getY() >= height || sq.getX() < 0 || sq.getY() < 0 || !sq.isEmpty())
			return false;
		return true;
		
	}

}
