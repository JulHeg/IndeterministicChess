package test.test.IndeterministicChess.IO;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import test.test.IndeterministicChess.Board.*;
import test.test.IndeterministicChess.Piece.*;

/**
 * Hopefully this will become the start for the new back end of the new game!
 * 
 */
public class Main{
	
	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		ResponseWindow blackWindow = new ResponseWindow(PieceColor.BLACK);
		ResponseWindow whiteWindow = new ResponseWindow(PieceColor.WHITE);
		SwingUtilities.invokeLater(new Thread() {
			public void run() {
				blackWindow.showGUI();
				whiteWindow.showGUI();
			}
		});
		PieceColor activePlayer = PieceColor.WHITE;
		while(!Chessboard.getInstance().probabilisticHasLost(activePlayer)){
			switch(activePlayer){
			case WHITE:
				whiteWindow.getResponse();
				break;
			case BLACK:
				blackWindow.getResponse();
				break;
			}
			activePlayer = activePlayer.otherColor();
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