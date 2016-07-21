package test.test.IndeterministicChess.IO;

import test.test.IndeterministicChess.Board.*;
import test.test.IndeterministicChess.Match.MatchController;
import test.test.IndeterministicChess.Piece.*;


/**
 * The main class that lets the player make one move after the other and checks for wins or a draw.
 */

public class Main{
	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		Chessboard chessboard = Chessboard.getGeneralizedFischerRandomChessboard(2);
		generalIO whiteIO = new LocalGUIWindow(PieceColor.WHITE, chessboard);
		generalIO blackIO = new LocalGUIWindow(PieceColor.BLACK, chessboard);
		MatchController match = new MatchController(blackIO, whiteIO, chessboard);
		match.start();
	}
}
