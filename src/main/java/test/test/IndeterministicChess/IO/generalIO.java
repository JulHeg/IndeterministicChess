package test.test.IndeterministicChess.IO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import com.google.common.collect.Sets;

import test.test.IndeterministicChess.Board.*;
import test.test.IndeterministicChess.Piece.*;

public abstract class generalIO {
	
	final public PieceColor player;

	public abstract void getResponse();
	
	protected abstract Square selectOneOfTheseSquares(Set<Square> theseSquares);
	
	public abstract void showGUI();
	
	public abstract void showWin();
	
	public abstract void showLose();
	
	public abstract void showDraw();

	protected abstract String selectPromotionOption(Set<String> options, Square square);
	
	protected abstract void enableMoveEnding();

	protected void checkForPromotion(){
		for(Pawn pawn : Chessboard.getInstance().getPawnsToBePromoted()){
			Set<String> options = new HashSet<String>(Arrays.asList(Chessboard.promotionPositions));
			String userChoice =	selectPromotionOption(options, pawn.getPosition());
			try {
				Chessboard.getInstance().changeRole(pawn, userChoice);
			} catch (Exception e) {
				throw new Error(e);
			}
		}
	}
	
	/*
	 * There may not be multiple pieces of the same type in "pieces".
	 */
	protected abstract Piece selectAPieceOf(Set<Piece> pieces);	
	
	public generalIO(PieceColor player){
		this.player = player;
	}
	
	//In percent, therefore between 0 and 100.
	protected int amountOfMoveLeft;
	
	protected int getAmountOfMoveLeft(){
		return amountOfMoveLeft;
	}

	protected void setAmountOfMoveLeft(int target){
		amountOfMoveLeft = target;
	}

	protected Set<Piece> getActuallyMovablePieces(){
		return Chessboard.getInstance().getAllPiecesOf(player).stream().filter(piece -> piece.getExistanceProbability().asDouble() * 100 <= getAmountOfMoveLeft() && piece.canMove()).collect(Collectors.toSet());
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
			Square thisSelection = selectOneOfTheseSquares(getOccupiedSquares(Sets.difference(movablePieces, alreadyMovedPieces)));
			if(thisSelection == null){//i.e. the exit button was pushed
				break;
			}
			Piece pieceToMove = selectAPieceOf(Sets.difference(getActuallyMovablePiecesOn(thisSelection), alreadyMovedPieces));
			Set<Square> nextSquares = pieceToMove.getPossibleNextSquares();
			//Get the piece's target square
			thisSelection = selectOneOfTheseSquares(nextSquares);
			//Move
			try {
				Set<Piece> piecesOnSelectionBefore = Chessboard.getInstance().getPiecesOnSquare(thisSelection);
				Chessboard.getInstance().movePiece(pieceToMove, thisSelection);
				Double amountOfMoveLeft = getAmountOfMoveLeft() - (100 * pieceToMove.getExistanceProbability().asDouble());
				setAmountOfMoveLeft(amountOfMoveLeft.intValue());
				checkForPromotion();
				//Adds all pieces that are new on the square, to account for promoting
				alreadyMovedPieces.addAll(Sets.difference(Chessboard.getInstance().getPiecesOnSquare(thisSelection), piecesOnSelectionBefore));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			enableMoveEnding();//At least one move required
		}
		Chessboard.getInstance().combinePieces();
	}
	
	protected void makeSplittingMove(){
		Set<Piece> splittablePieces = Chessboard.getInstance().getAllPiecesOf(player).stream().filter(Piece::canSplit).filter(Piece::canMove).collect(Collectors.toSet());
		//Get the piece that is to be moved
		Square thisSelection = selectOneOfTheseSquares(getOccupiedSquares(splittablePieces));
		Set<Piece> splittablePiecesThere = Chessboard.getInstance().getPiecesOnSquare(thisSelection,player).stream().filter(Piece::canSplit).collect(Collectors.toSet());
		Piece pieceToMove = selectAPieceOf(splittablePiecesThere);
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
	}
