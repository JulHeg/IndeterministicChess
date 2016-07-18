package test.test.IndeterministicChess.Piece;

import java.util.*;

import test.test.IndeterministicChess.Board.Square;

public class Pawn extends Piece {
	public String getTypeName() {//Unique identifier of each subclass
		return "Pawn";
	}
	
	public String getSymbol() {
		return getPieceColor() == PieceColor.WHITE ? "♙" : "♟";
	}

	public Pawn(Square square, PieceColor owner) {
		super(square, owner);
	}

	public Pawn(Square square, PieceColor owner, ExistenceProbability existenceProbability) {
		super(square, owner, existenceProbability);
	}

	public Pawn(Piece piece) {
		super(piece);
	}

	@Override
	public Set<Square> getPossibleNextSquares() {
		Set<Square> results = new HashSet<Square>();
		int direction = getPieceColor() == PieceColor.WHITE ? 1 : -1;
		int x = getPosition().getXPosition();
		int y = getPosition().getYPosition();
		Square square1 = new Square(x, y + direction);
		Square square2 = new Square(x, y + 2 * direction);
		Square square3 = new Square(x + 1, y + direction);
		Square square4 = new Square(x - 1, y + direction);
		ExistenceProbability probability1 = chessboard.ProbabilityLeft(square1, getPieceColor());
		ExistenceProbability probability2 = chessboard.ProbabilityLeft(square2, getPieceColor()).cap(probability1);
		ExistenceProbability probability3 = chessboard.ProbabilityLeft(square3, getPieceColor())
				.cap(chessboard.ProbabilityOn(square3, getPieceColor().otherColor()));
		ExistenceProbability probability4 = chessboard.ProbabilityLeft(square4, getPieceColor())
				.cap(chessboard.ProbabilityOn(square4, getPieceColor().otherColor()));

		if (probability1.greaterEqual(getExistanceProbability())) {
			results.add(square1);
		}
		if (probability2.greaterEqual(getExistanceProbability())) {
			results.add(square2);
		}
		if (probability3.greaterEqual(getExistanceProbability())) {
			results.add(square3);
		}
		if (probability4.greaterEqual(getExistanceProbability())) {
			results.add(square4);
		}
		return results;
	}
}