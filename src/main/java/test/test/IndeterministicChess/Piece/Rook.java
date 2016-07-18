package test.test.IndeterministicChess.Piece;

import java.util.*;

import test.test.IndeterministicChess.Board.Square;

public class Rook extends Piece {
	
	public String getTypeName() {
		return "Rook";
	}

	public Rook(Square square, PieceColor owner) {
		super(square, owner);
	}

	public Rook(Square square, PieceColor owner, ExistenceProbability existenceProbability) {
		super(square, owner, existenceProbability);
	}

	public Rook(Piece piece) {
		super(piece);
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
