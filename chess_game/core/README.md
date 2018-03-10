NOT TESTED/DONE YET:
NOT added implementation for capturing pieces


ABOUT THE CODE
NOTE: Please read through this (especially the part you're working with, and update and add needed parts about your code)
NOTE: ALL pieces should extend AbstractPiece
NOTE: Remember to test

IBoard - Board - Square:
- IBoard is just the interface for board.
- Positions are only kept track of in board/squares, and not inside pieces, to avoid code duplication (getX() and getY() are available for Square class).

Board:
* The board has a number of squares, that can be put anywhere inside the bounds of the board. You must always have a square in each position of the board.
* The board is always square (operates with dimension variable, instead of x and y, because x==y).
* Board can find out if king is in check, using the piecesThreatenedBy(...)() methods.

Square:
* When you add a square, you have to make sure that it is within the dimensions of the board (NOT CHECKED in square constructor).
* Logic for moving pieces, putting pieces and taking pieces.


IPiece - AbstractPiece - Rook
NOTE: ALL pieces should extend AbstractPiece - this class already has parts of the code you need.
IPiece:
* The interface for the code. Read through to see what methods does.

AbstractPiece:
* Has implementation for some of the methods in IPiece, that are (as far as I know now) common for all pieces. 
* Contains an abstract method allReachableSquares(int x, int y, IBoard board), it is crucial for this method to be 
implemented correctly and by the description, in order for the implemented methods getEmptySquares() and EnemyPiecesReached() to work.

Rook:
* The first piece added. Look to rook if you have trouble understanding what the methods from IPiece and AbstractPiece are doing, and/or the tests.