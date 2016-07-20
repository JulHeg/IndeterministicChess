package test.test.IndeterministicChess.IO;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import test.test.IndeterministicChess.Board.*;
import test.test.IndeterministicChess.Piece.*;

public abstract class generalIO {
	final public PieceColor player;
	
	public generalIO(PieceColor player){
		this.player = player;
	}
	
	protected int amountOfMoveLeft;
	
	protected int getAmountOfMoveLeft(){
		return amountOfMoveLeft;
	}
	
	protected void setAmountOfMoveLeft(int target){
		amountOfMoveLeft = target;
	}
	
	public abstract void getResponse();
	
	protected abstract Square selectOneOfTheseSquares(Set<Square> theseSquares);
	
	public abstract void showGUI();
	
	public abstract void showWin();
	
	public abstract void showLose();
	
	public abstract void showDraw();
	
	protected abstract Piece selectAPieceOn(Square square);	
	
	protected Set<Piece> getActuallyMovablePieces(){
		return Chessboard.getInstance().getAllPiecesOf(player).stream().filter(piece -> piece.getExistanceProbability().asDouble() * 100 <= getAmountOfMoveLeft() && piece.canMove()).collect(Collectors.toSet());
	}
	
	public Set<Square> getOccupiedSquares(Set<Piece> pieces) {
		return pieces.stream().map(Piece::getPosition).collect(Collectors.toSet());
	}
	
	protected void makeMovingMove(){
		Set<Piece> alreadyMovedPieces = new HashSet<Piece>();
		while(true){
			Set<Piece> movablePieces = getActuallyMovablePieces();
			if(movablePieces.isEmpty()){
				break;
			}
			Square thisSelection = selectOneOfTheseSquares(getOccupiedSquares(Sets.difference(movablePieces, alreadyMovedPieces)));
			if(thisSelection == null){//i.e. the exit button was pushed
				break;
			}
			Piece pieceToMove = selectAPieceOn(thisSelection);
			Set<Square> nextSquares = pieceToMove.getPossibleNextSquares();
			//Get the piece's target square
			thisSelection = selectOneOfTheseSquares(nextSquares);
			//Move
			try {
				Chessboard.getInstance().movePiece(pieceToMove, thisSelection);
				Double amountOfMoveLeft = getAmountOfMoveLeft() - (100 * pieceToMove.getExistanceProbability().asDouble());
				Chessboard.getInstance().combinePieces();
				setAmountOfMoveLeft(amountOfMoveLeft.intValue());
				alreadyMovedPieces.add(pieceToMove);
				} 
			catch (Exception e) {
				e.printStackTrace();
			}
			enableMoveEnding();//At least on move required
		}
	}
	
	protected void makeSplittingMove(){
		Set<Piece> splittablePieces = Chessboard.getInstance().getAllPiecesOf(player).stream().filter(Piece::canSplit).filter(Piece::canMove).collect(Collectors.toSet());
		//Get the piece that is to be moved
		Square thisSelection = selectOneOfTheseSquares(getOccupiedSquares(splittablePieces));
		Piece pieceToMove = selectAPieceOn(thisSelection);
		//Get the piece's target square
		Piece otherHalf;
		try {
			otherHalf = pieceToMove.splitOfHalf();
		} catch (Exception e1) {
			throw new Error(e1);
		}
		//Move one Half
		thisSelection = selectOneOfTheseSquares(otherHalf.getPossibleNextSquares());
		try {
			Chessboard.getInstance().movePiece(otherHalf, thisSelection);
			checkForPromotion();
			Chessboard.getInstance().combinePieces();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Move remaining half
		Set<Square> possibleTargets = pieceToMove.getPossibleNextSquares();
		possibleTargets.add(pieceToMove.getPosition());
		thisSelection = selectOneOfTheseSquares(possibleTargets);
		try {
			Chessboard.getInstance().movePiece(pieceToMove, thisSelection);
			checkForPromotion();
			Chessboard.getInstance().combinePieces();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void checkForPromotion();
	
	protected abstract void enableMoveEnding();
}
