/**
 * 
 */
package test.test.IndeterministicChess.Piece;

import java.util.*;

import test.test.IndeterministicChess.Board.Chessboard;
import test.test.IndeterministicChess.Board.Square;

/**
 * An abstract class for all chess pieces.
 */
public abstract class Piece {

	private PieceColor owner;

	private Square position;

	private ExistenceProbability existanceProbability;

	public abstract String getTypeName();

	public final Chessboard chessboard = Chessboard.getInstance();

	public Piece(Square position, PieceColor owner) {
		this(position, owner, ExistenceProbability.ONE);
	}

	public Piece(Piece original) {
		this(original.position, original.owner, original.existanceProbability);
	}

	public Piece(Square position, PieceColor owner, ExistenceProbability existanceProbability) {
		this.position = position;
		this.owner = owner;
		this.existanceProbability = existanceProbability;
	}
	
	public void setProbabilityToFull(){
		existanceProbability = ExistenceProbability.ONE;
	}

	@Override
	public Piece clone() {
		return this;
	}

	public Square getPosition() {
		return position;
	}

	public void setPosition(Square position) {
		this.position = position;
	}

	public PieceColor getPieceColor() {
		return owner;
	}

	public ExistenceProbability getExistanceProbability() {
		return existanceProbability;
	}

	public void setExistanceProbability(ExistenceProbability existanceProbability) {
		this.existanceProbability = existanceProbability;
	}

	public void moveTo(Square destination) {
		if (!getPossibleNextSquares().contains(destination)) {
			throw new IndexOutOfBoundsException("Piece cannot move there!");
		}
		position = destination;
	}

	public abstract Set<Square> getPossibleNextSquares();

	protected boolean canMoveOnByProbability(Square square) {
		return chessboard.ProbabilityLeft(square, getPieceColor()).lessEqual(getExistanceProbability());
	}

	protected Set<Square> getAccesableSquaresOnRay(int dx, int dy) {
		Set<Square> results = new HashSet<Square>();
		ExistenceProbability runningMin = ExistenceProbability.ONE;
		for (int i = 1; true; i++) {
			Square square = new Square(getPosition().getXPosition() + i * dx, getPosition().getYPosition() + i * dy);
			if (!chessboard.isInBoard(square)) {
				break;
			}
			runningMin = runningMin.cap(chessboard.ProbabilityLeft(square, getPieceColor()));
			if (runningMin.greaterEqual(getExistanceProbability())) {
				results.add(square);
			} else {
				break;
			}
			ExistenceProbability enemyProbability = chessboard.ProbabilityOn(square, getPieceColor().otherColor());
			if(!enemyProbability.equals(ExistenceProbability.ZERO)){
				runningMin = runningMin.cap(enemyProbability);
			}
		}
		return results;
	}

	public void incorporatePiece(Piece other) {
		existanceProbability = existanceProbability.add(other.getExistanceProbability());
	}
}