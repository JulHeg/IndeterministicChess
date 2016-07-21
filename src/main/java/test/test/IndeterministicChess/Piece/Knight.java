package test.test.IndeterministicChess.Piece;

import java.util.*;

import test.test.IndeterministicChess.Board.Chessboard;
import test.test.IndeterministicChess.Board.Square;

public class Knight extends Piece {
	public String getTypeName() {
		return "Knight";
	}
	
	public String getSymbol() {
		return getPieceColor() == PieceColor.WHITE ? "♘" : "♞";
	}

	public Knight(Square square, PieceColor owner, Chessboard chessboard) {
		super(square, owner, chessboard);
	}

	public Knight(Square square, PieceColor owner, ExistenceProbability existenceProbability, Chessboard chessboard) {
		super(square, owner, existenceProbability, chessboard);
	}

	public Knight(Piece piece) {
		super(piece);
	}

	protected Knight quasiClone() {
		return new Knight(this);
	}

	public Set<Square> getPossibleNextSquares() {
		Set<Square> results = new HashSet<Square>();
		int[][] directions = { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 } };
		for (int[] dxy : directions) {
			Square square = new Square(getPosition().getXPosition() + dxy[0], getPosition().getYPosition() + dxy[1]);
			if (chessboard.isInBoard(square)) {
				ExistenceProbability probability = chessboard.ProbabilityLeft(square, getPieceColor());
				if (probability.greaterEqual(getExistanceProbability())) {
					results.add(square);
				}
			}
		}
		return results;
	}
}
