package test.test.IndeterministicChess.UnitTests;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import test.test.IndeterministicChess.Board.*;
import test.test.IndeterministicChess.Piece.*;

public class CompleteChessboardTestCase {
	
	//The example that is tested
	Chessboard chessboard = Chessboard.getInstance();
	Pawn pawn = new Pawn(new Square(4, 3), PieceColor.WHITE, ExistenceProbability.ONE.getHalf());
	Queen queen = new Queen(new Square(3, 4), PieceColor.BLACK, ExistenceProbability.ONE);
	Knight knight = new Knight(new Square(4, 4), PieceColor.BLACK, ExistenceProbability.ONE.getHalf());
	
	@Before
	public void setUp() throws Exception {
		chessboard.addChessPiece(pawn);
		chessboard.addChessPiece(queen);
		chessboard.addChessPiece(knight);
	}

	@Test
	public void pawnMoveTest() {
		Set<Square> pawnMoves = new HashSet<Square>();
		pawnMoves.add(new Square(3,4));
		pawnMoves.add(new Square(4,4));
		pawnMoves.add(new Square(4,5));
		assertTrue(pawnMoves.equals(pawn.getPossibleNextSquares()));
	}

	@Test
	public void queenMoveTest() {
		Set<Square> queenMoves = new HashSet<Square>();
		queenMoves.add(new Square(3,3));
		queenMoves.add(new Square(2,3));
		queenMoves.add(new Square(3,2));
		queenMoves.add(new Square(4,5));
		queenMoves.add(new Square(6,7));
		queenMoves.add(new Square(3,1));
		queenMoves.add(new Square(1,2));
		queenMoves.add(new Square(5,6));
		queenMoves.add(new Square(3,7));
		queenMoves.add(new Square(1,4));
		queenMoves.add(new Square(3,6));
		queenMoves.add(new Square(2,4));
		queenMoves.add(new Square(3,5));
		queenMoves.add(new Square(2,5));
		queenMoves.add(new Square(1,6));
		queenMoves.add(new Square(4,3));
		queenMoves.add(new Square(3,8));
		queenMoves.add(new Square(7,8));
		assertTrue(queenMoves.equals(queen.getPossibleNextSquares()));
	}

	@Test
	public void rookMoveTest() {
		Set<Square> knightMoves = new HashSet<Square>();
		knightMoves.add(new Square(2,3));
		knightMoves.add(new Square(3,2));
		knightMoves.add(new Square(6,5));
		knightMoves.add(new Square(5,6));
		knightMoves.add(new Square(6,3));
		knightMoves.add(new Square(3,6));
		knightMoves.add(new Square(2,5));
		knightMoves.add(new Square(5,2));
		assertTrue(knightMoves.equals(knight.getPossibleNextSquares()));
	}
}
