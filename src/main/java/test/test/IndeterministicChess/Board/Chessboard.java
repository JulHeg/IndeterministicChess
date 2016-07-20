package test.test.IndeterministicChess.Board;

import java.util.*;
import java.util.stream.Collectors;

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
		return squares.values().contains(square);
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
	
	public void clear(){
		allPieces.clear();
	}
	
	public void removePiece(Piece piece){
		allPieces.remove(piece);
	}

	public static Chessboard getInstance() {
		if (currentChessboard == null) {
			try{
				currentChessboard = new Chessboard(8);
				//Add pawns
				for(int i = 1; i <= 8; i++){
					currentChessboard.addChessPiece(new Pawn(new Square(i,2), PieceColor.WHITE));
					currentChessboard.addChessPiece(new Pawn(new Square(i,7), PieceColor.BLACK));
				}
				//Add rest
				currentChessboard.addChessPiece(new Queen(new Square(4,1), PieceColor.WHITE));
				currentChessboard.addChessPiece(new Queen(new Square(4,8), PieceColor.BLACK));
				currentChessboard.addChessPiece(new King(new Square(5,1), PieceColor.WHITE));
				currentChessboard.addChessPiece(new King(new Square(5,8), PieceColor.BLACK));
				currentChessboard.addChessPiece(new Bishop(new Square(3,1), PieceColor.WHITE));
				currentChessboard.addChessPiece(new Bishop(new Square(3,8), PieceColor.BLACK));
				currentChessboard.addChessPiece(new Bishop(new Square(6,1), PieceColor.WHITE));
				currentChessboard.addChessPiece(new Bishop(new Square(6,8), PieceColor.BLACK));
				currentChessboard.addChessPiece(new Knight(new Square(2,1), PieceColor.WHITE));
				currentChessboard.addChessPiece(new Knight(new Square(2,8), PieceColor.BLACK));
				currentChessboard.addChessPiece(new Knight(new Square(7,1), PieceColor.WHITE));
				currentChessboard.addChessPiece(new Knight(new Square(7,8), PieceColor.BLACK));
				currentChessboard.addChessPiece(new Rook(new Square(1,1), PieceColor.WHITE));
				currentChessboard.addChessPiece(new Rook(new Square(1,8), PieceColor.BLACK));
				currentChessboard.addChessPiece(new Rook(new Square(8,1), PieceColor.WHITE));
				currentChessboard.addChessPiece(new Rook(new Square(8,8), PieceColor.BLACK));
			}
			catch(Exception e){
				throw new Error("The chessboard couldn't be properly constructed.");
			}
		}
		return currentChessboard;
	}

	public Set<Piece> getAllPieces() {
		return new HashSet<Piece>(allPieces);
	}

	public Set<Piece> getAllPiecesOf(PieceColor player) {
		return getAllPieces().stream().filter(piece -> piece.getPieceColor() == player).collect(Collectors.toSet());
	}

	public void movePiece(Piece piece, Square target) throws Exception {
		if (!piece.getPossibleNextSquares().contains(target)) {
			// Just to be sure, shouldn't be necessary.
			throw new Exception("No valid move!!");
		}
		if (!isInBoard(target)) {
			throw new Exception("Target is not on the board!");
		}
		ExistenceProbability totalProbabilityOfOtherColorBefore = ProbabilityOn(target, piece.getPieceColor().otherColor());
		//=1-max{0,1-newPiece/oldPieces}=1-min{1,newPiece/totalProbabilityOfOtherColorBefore}
		if(!totalProbabilityOfOtherColorBefore.isDead())
		{
			ExistenceProbability mulitplier = piece.getExistanceProbability()
					.divide(totalProbabilityOfOtherColorBefore).cap(ExistenceProbability.ONE).getRest();
			// Partially take enemy pieces
			for (Piece enemyPiece : getPiecesOnSquare(target, piece.getPieceColor().otherColor())) {
				reducePiece(enemyPiece, mulitplier);
			}
		}
		piece.setPosition(target);
	}
	
	public void reducePiece(Piece piece, ExistenceProbability multiplier) throws Exception{
		if(!getAllPieces().contains(piece)){
			throw new Exception("Taken piece is not actually on the board!");
		}
		ExistenceProbability newProbability = piece.getExistanceProbability().multiply(multiplier);
		if(newProbability.isDead()){
			removePiece(piece);
		}
		else{
			piece.setExistanceProbability(newProbability);
		}
	}
	
	public Set<Pawn> getPawnsToBePromoted() {
		Set<Pawn> result = new HashSet<Pawn>();
		for(Piece piece : getAllPieces()){
			int yPosition = piece.getPosition().getYPosition();
			if(piece instanceof Pawn && (yPosition == 1 || yPosition == getSize())){
				Pawn asPawn = (Pawn) piece;
				result.add(asPawn);
			}
		}
		return result;
	}
	
	public void combinePieces() {
		for(Square square : squares.values()){
			for(PieceColor color : PieceColor.values()){
				Set<Piece> pieces = getPiecesOnSquare(square, color);
				for(String type : pieces.stream().map(Piece::getTypeName).collect(Collectors.toSet())){
					List<Piece> fittingPieces = pieces.stream().filter(piece -> piece.getTypeName() == type).collect(Collectors.toList());
					if(fittingPieces.size() > 1){
						for(int i = 1; i < fittingPieces.size(); i++){//Iterate without first element
							fittingPieces.get(0).incorporatePiece(fittingPieces.get(i));
							removePiece(fittingPieces.get(i));
						}
					}
				}
			}
		}
	}
	
	public void redetermine() {
		Set<Piece> newPieces = new HashSet<Piece>();
		for(Square square : squares.values()){
			Piece probabilisticPiece = getProbabilisticPieceOn(square);
			if(probabilisticPiece != null){
				probabilisticPiece.setProbabilityToFull();
				newPieces.add(probabilisticPiece);
			}
		}
		clear();
		allPieces.addAll(newPieces);
	}
	
	public static final String[] promotionPositions = {"Knight","Bishop","Rook","Queen"};

	public void changeRole(Pawn pawn, String position) throws Exception{
		if(!allPieces.contains(pawn)){
			throw new Exception("The promoted pawn is not atctually on the board!");
		}
		allPieces.remove(pawn);
		switch (position) {
		case "Knight":
			addChessPiece(new Knight(pawn));
			break;
		case "Bishop":
			addChessPiece(new Bishop(pawn));
			break;
		case "Rook":
			addChessPiece(new Rook(pawn));
			break;
		case "Pawn":
			addChessPiece(new Pawn(pawn));
			break;
		case "King":
			addChessPiece(new King(pawn));
			break;
		case "Queen":
			addChessPiece(new Queen(pawn));
			break;
		default:
			throw new Exception("The new type for the promoted pawn is not supported!");
		}
	}
	

	public String toProbabilisticString() {
		String result = "";
		for(int y = 1; y <= getSize(); y++){
			for(int x = 1; x <= getSize(); x++){
				result += getProbabilisticSymbolOn(new Square(x,y));
			}
			result += "\n";
		}
		return result;
	}
	
	public String getProbabilisticSymbolOn(Square square){
		Piece probabilisticPiece = getProbabilisticPieceOn(square);
		if(probabilisticPiece == null){
			return " ";
		}
		else{
			return probabilisticPiece.getSymbol();
		}
	}
	
	public Piece getProbabilisticPieceOn(Square square){
		double cumulativeProbability = 0;
		double randomValue = random.nextDouble();
		for(Piece piece : getPiecesOnSquare(square)){
			if(cumulativeProbability <= randomValue && randomValue <= cumulativeProbability + piece.getExistanceProbability().asDouble()){
				return piece;
			}
			cumulativeProbability += piece.getExistanceProbability().asDouble();
		}
		return null;
	}
	
	public boolean probabilisticHasLost(PieceColor player){
		double totalKingProbability = ExistenceProbability.sumProbability(getAllPiecesOf(player).stream().filter(piece -> piece instanceof King).map(Piece::getExistanceProbability).collect(Collectors.toSet())).asDouble();
		return random.nextDouble() > totalKingProbability;
	}
	
	public boolean canSomehowMove(PieceColor player){
		return getAllPiecesOf(player).stream().anyMatch(Piece::canMove);
	}
}