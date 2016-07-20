package test.test.IndeterministicChess.IO;

import java.util.Set;

import test.test.IndeterministicChess.Board.Chessboard;
import test.test.IndeterministicChess.Board.Square;
import test.test.IndeterministicChess.Piece.Piece;

public abstract class generalIO {
	protected int amountOfMoveLeft;
	
	protected int getAmountOfMoveLeft(){
		return amountOfMoveLeft;
	}
	
	protected void setAmountOfMoveLeft(int target){
		amountOfMoveLeft = target;
	}
	
	public abstract void getResponse();
	
	protected abstract Square selectOneOfTheseSquares(Set<Square> theseSquares);
	
	public abstract void showGUI();
	
	public abstract void showWin();
	
	public abstract void showLose();
	
	public abstract void showDraw();
	
	protected abstract Piece selectAPieceOn(Square square);	
}
