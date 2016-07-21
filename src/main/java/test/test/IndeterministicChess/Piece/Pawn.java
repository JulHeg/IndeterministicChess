package test.test.IndeterministicChess.Piece;

import java.util.*;

import test.test.IndeterministicChess.Board.Chessboard;
import test.test.IndeterministicChess.Board.Square;

public class Pawn extends Piece {
	public String getTypeName() {//Unique identifier of each subclass
		return "Pawn";
	}
	
	public String getSymbol() {
		return getPieceColor() == PieceColor.WHITE ? "♙" : "♟";
	}

	public Pawn(Square square, PieceColor owner, Chessboard chessboard) {
		super(square, owner, chessboard);
	}

	public Pawn(Square square, PieceColor owner, ExistenceProbability existenceProbability, Chessboard chessboard) {
		super(square, owner, existenceProbability, chessboard);
	}

	public Pawn(Piece piece) {
		super(piece);
	}

	protected Pawn myClone() {
		return new Pawn(this);
	}

	@Override
	public Set<Square> getPossibleNextSquares() {
		Set<Square> results = new HashSet<Square>();
		int direction = getPieceColor() == PieceColor.WHITE ? 1 : -1;
		int x = getPosition().getXPosition();
		int y = getPosition().getYPosition();
		Square square1 = new Square(x + 1, y + direction);
		Square square2 = new Square(x - 1, y + direction);
		Square square3 = new Square(x, y + direction);
		Square squareExtra = new Square(x, y + 2 * direction);
		ExistenceProbability probability1 = chessboard.ProbabilityLeft(square1, getPieceColor())
				.cap(chessboard.ProbabilityOn(square1, getPieceColor().otherColor()));
		ExistenceProbability probability2 = chessboard.ProbabilityLeft(square2, getPieceColor())
				.cap(chessboard.ProbabilityOn(square2, getPieceColor().otherColor()));
		ExistenceProbability probability3 = chessboard.ProbabilityOn(square3, getPieceColor()).add(chessboard.ProbabilityOn(square3, getPieceColor().otherColor())).getRest();
		ExistenceProbability probabilityExtra = chessboard.ProbabilityOn(squareExtra, getPieceColor()).add(chessboard.ProbabilityOn(squareExtra, getPieceColor().otherColor())).getRest().cap(probability3);

		if (probability1.greaterEqual(getExistanceProbability())) {
			results.add(square1);
		}
		if (probability2.greaterEqual(getExistanceProbability())) {
			results.add(square2);
		}
		if (probability3.greaterEqual(getExistanceProbability())) {
			results.add(square3);
		}
		if (probabilityExtra.greaterEqual(getExistanceProbability()) && ((getPieceColor() == PieceColor.WHITE && getPosition().getYPosition() == 2) || (getPieceColor() == PieceColor.BLACK && getPosition().getYPosition() == 7))) {
			results.add(squareExtra);
		}
		return results;
	}
}