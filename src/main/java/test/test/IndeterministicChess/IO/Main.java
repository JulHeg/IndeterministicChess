package test.test.IndeterministicChess.IO;

import test.test.IndeterministicChess.Board.*;
import test.test.IndeterministicChess.Match.MatchController;
import test.test.IndeterministicChess.Piece.*;


/**
 * The main class that starts a new MatchController to do the actual work.
 */

public class Main{
	//Starts the actual program
	public static void main(String[] args) {
		Chessboard chessboard = Chessboard.getGeneralizedFischerRandomChessboard(2);
		GeneralUI whiteIO = new LocalGUIWindow(PieceColor.WHITE, chessboard);
		GeneralUI blackIO = new LocalGUIWindow(PieceColor.BLACK, chessboard);
		MatchController match = new MatchController(blackIO, whiteIO, chessboard);
		match.start();
	}
}
