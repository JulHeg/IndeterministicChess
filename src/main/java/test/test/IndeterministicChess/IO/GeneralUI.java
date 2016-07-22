package test.test.IndeterministicChess.IO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import test.test.IndeterministicChess.Board.*;
import test.test.IndeterministicChess.Piece.*;

/*
 * An abstract class that represents some form of UI.
 * It also contains the logic for what constitutes a valid move in chess. 
 * At the moment GeneralUI is only extended by LocalGUIWindow.
 */
public abstract class GeneralUI {
	protected final Chessboard chessboard;
	
	final public PieceColor player;
	
	protected abstract Square selectOneOfTheseSquares(Set<Square> theseSquares);
	
	public abstract void showGUI();
	
	public abstract void showWin();
	
	public abstract void showLose();
	
	public abstract void showDraw();

	protected abstract String selectPromotionOption(Set<String> options, Square square);
	
	protected abstract void enableMoveEnding();
	
	protected abstract void disableMoveEnding();

	protected abstract moveOptions getMoveOption();
	
	public enum moveOptions {
		MOVE, SPLIT, REDETERMINE
	}

	public void getResponse(){
		setAmountOfMoveLeft(100);
		moveOptions chosenMoveOoption = getMoveOption();
		switch(chosenMoveOoption){
		case MOVE:
			makeMovingMove();
			break;
		case REDETERMINE:
			chessboard.redetermine();
			break;
		case SPLIT:
			makeSplittingMove();
			break;
		}
}

	protected void checkForPromotion(){
		for(Pawn pawn : chessboard.getPawnsToBePromoted()){
			Set<String> options = new HashSet<String>(Arrays.asList(Chessboard.promotionPositions));
			String userChoice =	selectPromotionOption(options, pawn.getPosition());
			try {
				chessboard.changeRole(pawn, userChoice);
			} catch (Exception e) {
				throw new Error(e);
			}
		}
	}
	
	/*
	 * There may not be multiple pieces of the same type in "pieces".
	 */
	protected abstract Piece selectAPieceOf(Set<Piece> pieces);	
	
	public GeneralUI(PieceColor player, Chessboard chessboard){
		this.player = player;
		this.chessboard = chessboard;
	}
	
	//In percent, therefore between 0 and 100.
	protected int amountOfMoveLeft;
	
	protected int getAmountOfMoveLeft(){
		return amountOfMoveLeft;
	}

	protected void setAmountOfMoveLeft(int target){
		if(target < 0 || target > 100){
			throw new Error("setAmountOfMoveLeft was called with a value not between 0 and 100.");
		}
		amountOfMoveLeft = target;
	}

	protected Set<Piece> getActuallyMovablePieces(){
		return chessboard.getAllPiecesOf(player).stream().filter(piece -> piece.getExistanceProbability().asDouble() * 100 <= getAmountOfMoveLeft() && piece.canMove()).collect(Collectors.toSet());
	}

	protected Set<Piece> getActuallyMovablePiecesOn(Square square){
		return getActuallyMovablePieces().stream().filter(piece -> piece.getPosition().equals(square)).collect(Collectors.toSet());
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
			System.out.println("test");
			Square thisSelection = selectOneOfTheseSquares(getOccupiedSquares(Sets.difference(movablePieces, alreadyMovedPieces)));
			if(thisSelection == null){//i.e. the exit button was pushed
				break;
			}
			Piece pieceToMove = selectAPieceOf(Sets.difference(getActuallyMovablePiecesOn(thisSelection), alreadyMovedPieces));
			System.out.println("test");
			Set<Square> nextSquares = pieceToMove.getPossibleNextSquares();
			//Get the piece's target square
			thisSelection = selectOneOfTheseSquares(nextSquares);
			//Move
			try {
				Set<Piece> piecesOnSelectionBefore = chessboard.getPiecesOnSquare(thisSelection);
				chessboard.movePiece(pieceToMove, thisSelection);
				Double amountOfMoveLeft = getAmountOfMoveLeft() - (100 * pieceToMove.getExistanceProbability().asDouble());
				setAmountOfMoveLeft(amountOfMoveLeft.intValue());
				checkForPromotion();
				//Adds all pieces that are new on the square, to account for promoting
				alreadyMovedPieces.addAll(Sets.difference(chessboard.getPiecesOnSquare(thisSelection), piecesOnSelectionBefore));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			enableMoveEnding();//At least one move required
		}
		disableMoveEnding();
		chessboard.combinePieces();
	}
	
	protected void makeSplittingMove(){
		Set<Piece> splittablePieces = chessboard.getAllPiecesOf(player).stream().filter(Piece::canSplit).filter(piece -> piece.cloneOfHalf().canMove()).collect(Collectors.toSet());
		//Get the piece that is to be moved
		Square thisSelection = selectOneOfTheseSquares(getOccupiedSquares(splittablePieces));
		Set<Piece> splittablePiecesThere = chessboard.getPiecesOnSquare(thisSelection,player).stream().filter(Piece::canSplit).collect(Collectors.toSet());
		Piece pieceToMove = selectAPieceOf(splittablePiecesThere);
		//Get the piece's target square
		Piece otherHalf;
		try {
			otherHalf = pieceToMove.splitOfHalf();
			chessboard.addChessPiece(otherHalf);
		} catch (Exception e1) {
			throw new Error(e1);
		}
		//Move one Half
		thisSelection = selectOneOfTheseSquares(otherHalf.getPossibleNextSquares());
		try {
			chessboard.movePiece(otherHalf, thisSelection);
			checkForPromotion();
			chessboard.combinePieces();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Move remaining half
		Set<Square> possibleTargets = pieceToMove.getPossibleNextSquares();
		possibleTargets.add(pieceToMove.getPosition());
		thisSelection = selectOneOfTheseSquares(possibleTargets);
		try {
			chessboard.movePiece(pieceToMove, thisSelection);
			checkForPromotion();
			chessboard.combinePieces();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	}
