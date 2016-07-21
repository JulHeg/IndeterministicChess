package test.test.IndeterministicChess.IO;

import javax.swing.SwingUtilities;

import test.test.IndeterministicChess.Board.*;
import test.test.IndeterministicChess.Piece.*;


/**
 * The main class that lets the player make one move after the other and checks for wins or a draw.
 */

public class Main{
	
	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		Chessboard chessboard = Chessboard.getStandardChessboard();
		generalIO blackWindow = new LocalGUIWindow(PieceColor.BLACK, chessboard);
		generalIO whiteWindow = new LocalGUIWindow(PieceColor.WHITE, chessboard);
		SwingUtilities.invokeLater(new Thread() {
			public void run() {
				blackWindow.showGUI();
				whiteWindow.showGUI();
			}
		});
		PieceColor activePlayer = PieceColor.WHITE;
		boolean isLost = false;
		boolean isDraw = false;
		while(!isLost && !isDraw){
			switch(activePlayer){
			case WHITE:
				whiteWindow.getResponse();
				break;
			case BLACK:
				blackWindow.getResponse();
				break;
			}
			activePlayer = activePlayer.otherColor();
			isLost = chessboard.probabilisticHasLost(activePlayer);
			isDraw = chessboard.probabilisticHasLost(activePlayer);
		}
		if(activePlayer == PieceColor.BLACK){
			blackWindow.showLose();
			whiteWindow.showWin();
		}
		else{
			blackWindow.showWin();
			whiteWindow.showLose();
		}
	}
}
