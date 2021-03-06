package test.test.IndeterministicChess.Piece;

import java.util.*;

import test.test.IndeterministicChess.Board.Chessboard;
import test.test.IndeterministicChess.Board.Square;

public class Bishop extends Piece {
	public String getTypeName() {
		return "Bishop";
	}
	
	public String getSymbol() {
		return getPieceColor() == PieceColor.WHITE ? "♗" : "♝";
	}
	
	public Bishop(Square square, PieceColor owner, Chessboard chessboard) {
		super(square, owner, chessboard);
	}

	public Bishop(Square square, PieceColor owner, ExistenceProbability existenceProbability, Chessboard chessboard) {
		super(square, owner, existenceProbability, chessboard);
	}

	public Bishop(Piece piece) {
		super(piece);
	}

	protected Bishop myClone() {
		return new Bishop(this);
	}

	public Set<Square> getPossibleNextSquares() {
		Set<Square> results = new HashSet<Square>();
		int[] signs = { -1, 0, 1 };
		for (int dx : signs) {
			for (int dy : signs) {
				if (Math.abs(dx) + Math.abs(dy) == 2) {
					results.addAll(getAccesableSquaresOnRay(dx, dy));
				}
			}
		}
		return results;
	}

}
