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

	public abstract String getTypeName();//Unique Identifier
	
	public abstract String getSymbol();

	public final Chessboard chessboard;

	public Piece(Square position, PieceColor owner, Chessboard chessboard) {
		this(position, owner, ExistenceProbability.ONE, chessboard);
	}

	public Piece(Piece original) {
		this(original.position, original.owner, original.existanceProbability, original.chessboard);
	}

	public Piece(Square position, PieceColor owner, ExistenceProbability existanceProbability, Chessboard chessboard) {
		this.position = position;
		this.owner = owner;
		this.existanceProbability = existanceProbability;
		this.chessboard = chessboard;
	}
	
	public void setProbabilityToFull(){
		existanceProbability = ExistenceProbability.ONE;
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
			ExistenceProbability friendlyProbability = chessboard.ProbabilityOn(square, getPieceColor());
			ExistenceProbability enemyProbability = chessboard.ProbabilityOn(square, getPieceColor().otherColor());
			runningMin = runningMin.cap(friendlyProbability.getRest());
			if (runningMin.greaterEqual(getExistanceProbability())) {
				results.add(square);
			} else {
				break;
			}
			runningMin = runningMin.cap(enemyProbability.add(friendlyProbability).getRest());
		}
		return results;
	}
	
	public boolean canSplit(){
		return !chessboard.isDead(existanceProbability.getHalf()) && !(this instanceof King);
	}
	
	public boolean canMove(){
		return !getPossibleNextSquares().isEmpty();
	}
	
	protected abstract Piece myClone();

	public void incorporatePiece(Piece other) {
		existanceProbability = existanceProbability.add(other.getExistanceProbability());
	}

	public Piece splitOfHalf() throws Exception {
		if(!canSplit()){
			throw new Exception("Piece too small to split!");
		}
		existanceProbability = existanceProbability.getHalf();
		Piece otherHalf = myClone();
		return otherHalf;
	}
	
	public Piece cloneOfHalf(){
		try{
		return myClone().splitOfHalf();
		}
		catch(Exception e){
			e.printStackTrace();
			throw new Error(e);
		}
	}
	
	@Override
	public boolean equals(Object o){
		return this == o;
	}
}