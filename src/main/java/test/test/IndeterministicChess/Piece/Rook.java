package test.test.IndeterministicChess.Piece;

import java.util.*;

import test.test.IndeterministicChess.Board.Chessboard;
import test.test.IndeterministicChess.Board.Square;

public class Rook extends Piece {
	public String getTypeName() {
		return "Rook";
	}
	
	public String getSymbol() {
		return getPieceColor() == PieceColor.WHITE ? "♖" : "♜";
	}

	public Rook(Square square, PieceColor owner, Chessboard chessboard) {
		super(square, owner, chessboard);
	}

	public Rook(Square square, PieceColor owner, ExistenceProbability existenceProbability, Chessboard chessboard) {
		super(square, owner, existenceProbability, chessboard);
	}

	public Rook(Piece piece) {
		super(piece);
	}

	protected Rook myClone() {
		return new Rook(this);
	}

	public Set<Square> getPossibleNextSquares() {
		Set<Square> results = new HashSet<Square>();
		int[] signs = { -1, 0, 1 };
		for (int dx : signs) {
			for (int dy : signs) {
				if (Math.abs(dx) + Math.abs(dy) == 1) {
					results.addAll(getAccesableSquaresOnRay(dx, dy));
				}
			}
		}
		return results;
	}
}
