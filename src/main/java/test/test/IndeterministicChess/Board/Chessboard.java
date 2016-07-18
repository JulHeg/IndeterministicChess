package test.test.IndeterministicChess.Board;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.math3.fraction.BigFraction;

import com.google.common.collect.*;

import test.test.IndeterministicChess.Piece.*;

/**
 * A singleton class, the one chessboard where the game happens.
 */
public class Chessboard {

	private static Chessboard currentChessboard = getInstance();
	
	private Random random = new Random();

	private final int size;

	public int getSize() {
		return size;
	}

	private final Set<Piece> allPieces;

	private final Table<Integer, Integer, Square> squares;

	private Chessboard(int size) {
		this.size = size;
		ImmutableTable.Builder<Integer, Integer, Square> builder = ImmutableTable.builder();
		for (int x = 1; x <= size; x++) {
			for (int y = 1; y <= size; y++) {
				builder.put(x, y, new Square(x, y));
			}
		}
		squares = builder.build();
		allPieces = new HashSet<Piece>();
	}

	public Square getSquareAt(int xPosition, int yPosition) throws Exception {
		Square result = squares.get(xPosition, yPosition);
		if (result == null) {
			throw new Exception("The square is outside of the playing field!");
		}
		return result;
	}

	public Set<Piece> getPiecesOnSquare(Square square, PieceColor color) {
		return getPiecesOnSquare(square).stream().filter(piece -> piece.getPieceColor() == color)
				.collect(Collectors.toSet());
	}

	public Set<Piece> getPiecesOnSquare(Square square) {
		Set<Piece> result = new HashSet<Piece>();
		for (Piece piece : getAllPieces()) {
			if (piece.getPosition().equals(square)) {
				result.add(piece);
			}
		}
		return result;
	}

	public boolean isInBoard(Square square) {
		return squares.contains(square.getXPosition(), square.getYPosition());
	}

	public ExistenceProbability ProbabilityOn(Square square, PieceColor color) {
		Set<ExistenceProbability> existanceProbabilities = getPiecesOnSquare(square, color).stream()
				.map(Piece::getExistanceProbability).collect(Collectors.toSet());
		return ExistenceProbability.sumProbability(existanceProbabilities);
	}

	public ExistenceProbability ProbabilityLeft(Square square, PieceColor color) {
		return ProbabilityOn(square, color).getRest();
	}

	public void addChessPiece(Piece piece) throws Exception {
		if (piece == null) {
			throw new NullPointerException("Chess piece cannot be null when added!");
		}
		if (!isInBoard(piece.getPosition())) {
			throw new Exception("Chess piece cannot be outside of the chessboard when added!");
		}
		allPieces.add(piece);
	}

	public static Chessboard getInstance() {
		if (currentChessboard == null) {
			currentChessboard = new Chessboard(8);
		}
		return currentChessboard;
	}

	public Set<Piece> getAllPieces() {
		return new HashSet<Piece>(allPieces);
	}

	public void movePiece(Piece piece, Square target) throws Exception {
		if (!piece.getPossibleNextSquares().contains(target)) {
			// Just to be sure, shouldn't be necessary.
			throw new Exception("Kein gültiger Zug!");
		}
		if (isInBoard(target)) {
			throw new Exception("Ziel liegt nicht im Brett!");
		}
		ExistenceProbability totalProbabilityOfThisColorBefore = ProbabilityOn(target, piece.getPieceColor());
		BigFraction mulitplier = BigFraction.ONE.subtract(piece.getExistanceProbability().getProbability()
				.divide(totalProbabilityOfThisColorBefore.getRest().getProbability()));// 1-(newestPiece/(1-oldPieces))
		// Partially take enemy pieces
		for (Piece enemyPiece : getPiecesOnSquare(target, piece.getPieceColor().otherColor())) {
			enemyPiece.getExistanceProbability().multiply(mulitplier);
		}
		for (Piece enemyPiece : getPiecesOnSquare(target, piece.getPieceColor())) {
			enemyPiece.getExistanceProbability().multiply(mulitplier);
		}
		piece.setPosition(target);
	}
	
	public Set<Pawn> getPawnsToBePromotion() {
		Set<Pawn> result = new HashSet<Pawn>();
		for(Piece piece : getAllPieces()){
			Pawn asPawn = (Pawn) piece;
			int xPosition = asPawn.getPosition().getYPosition();
			if(piece instanceof Pawn && (xPosition == 1 || xPosition == getSize())){
				result.add(asPawn);
			}
		}
		return result;
	}
	
	public void redetermine() {
		Set<Piece> newPieces = new HashSet<Piece>();
		for(Piece piece : getAllPieces()){
			if(random.nextDouble() < piece.getExistanceProbability().getProbabilityAsDouble()){
				piece.setProbabilityToFull();
				newPieces.add(piece);
			}
		}
		allPieces.removeAll(allPieces);
		allPieces.addAll(newPieces);
	}
}