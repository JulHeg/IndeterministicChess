package test.test.IndeterministicChess.UnitTests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import test.test.IndeterministicChess.Board.Chessboard;
import test.test.IndeterministicChess.Board.Square;
import test.test.IndeterministicChess.Piece.Pawn;
import test.test.IndeterministicChess.Piece.PieceColor;

public class ChessboardTest {

	private Chessboard chessboard;

	@Before
	public void setUp() throws Exception {
		chessboard = Chessboard.getInstance();
		chessboard.addChessPiece(new Pawn(new Square(1, 1), PieceColor.WHITE));
	}

	@Test
	public void testGetSize() {
		assertTrue(chessboard.getSize() == 8);
	}

	/*@Test
	public void testGetPieceAt() throws Exception {
		for(int x = 1; x <= chessboard.getSize(); x++)
		{
			for(int y = 1; y <= chessboard.getSize(); y++)
			{
				PiecePart piece = chessboard.getPiecePartsAt(x, y);
				if(!chessboard.isSquareFree(new Square(x,y))){
					assertTrue(piece.getPosition().getXPosition() == x);
					assertTrue(piece.getPosition().getYPosition() == y);
				}
			}
		}
		try{
			chessboard.getPiecePartsAt(-1,3);
			//fail();
		}
		catch(Exception e) {}
	}*/

	/*@Test
	public void testGetPieceOnSquare() {
		for(int x = 1; x <= chessboard.getSize(); x++)
		{
			for(int y = 1; y <= chessboard.getSize(); y++)
			{
				PiecePart piece = chessboard.getPiecePartsOnSquare(new Square(x, y));
				if(!chessboard.isSquareFree(new Square(x,y))){
					assertTrue(piece.getPosition().getXPosition() == x);
					assertTrue(piece.getPosition().getYPosition() == y);
				}
			}
		}
		try{
			chessboard.getPiecePartsAt(-1,3);
			//fail();
		}
		catch(Exception e) {}
	}*/

	@Test
	public void testIsInBoard() {
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			int x = random.nextInt(5 * chessboard.getSize());
			int y = random.nextInt(5 * chessboard.getSize());
			assertTrue(!(chessboard.isInBoard(new Square(x, y))
					^ (x > 0 && y > 0 && x <= chessboard.getSize() && y <= chessboard.getSize())));
		}
		assertTrue(!chessboard.isInBoard(new Square(2, Integer.MAX_VALUE)));
		assertTrue(!chessboard.isInBoard(new Square(2, Integer.MIN_VALUE)));
	}
}
