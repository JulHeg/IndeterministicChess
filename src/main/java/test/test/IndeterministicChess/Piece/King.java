package test.test.IndeterministicChess.Piece;

import java.util.*;

import test.test.IndeterministicChess.Board.Chessboard;
import test.test.IndeterministicChess.Board.Square;

public class King extends Piece {
	public String getTypeName() {
		return "King";
	}
	
	public String getSymbol() {
		return getPieceColor() == PieceColor.WHITE ? "♔" : "♚";
	}

	public King(Square square, PieceColor owner, Chessboard chessboard) {
		super(square, owner, chessboard);
	}

	public King(Square square, PieceColor owner, ExistenceProbability existenceProbability, Chessboard chessboard) {
		super(square, owner, existenceProbability, chessboard);
	}

	public King(Piece piece) {
		super(piece);
	}

	public King myClone() {
		return new King(this);
	}

	@Override
	public Set<Square> getPossibleNextSquares() {
		Set<Square> results = new HashSet<Square>();
		int[] signs = { -1, 0, 1 };
		for (int dx : signs) {
			for (int dy : signs) {
				if (Math.abs(dx) + Math.abs(dy) >= 1) {
					Square square = new Square(getPosition().getXPosition() + dx, getPosition().getYPosition() + dy);
					if (chessboard.isInBoard(square)) {
						ExistenceProbability probability = chessboard.ProbabilityLeft(square, getPieceColor());
						if (probability.greaterEqual(getExistanceProbability())) {
							results.add(square);
						}
					}
				}
			}
		}
		return results;
	}
}