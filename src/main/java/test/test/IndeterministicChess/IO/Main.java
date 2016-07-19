package test.test.IndeterministicChess.IO;

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
		try {
			Chessboard.getInstance().addChessPiece(new Pawn(new Square(4,4), PieceColor.BLACK, ExistenceProbability.ONE.getHalf()));
			Chessboard.getInstance().addChessPiece(new Knight(new Square(4,4), PieceColor.BLACK, ExistenceProbability.ONE.getHalf()));
		} catch (Exception e1) {
			throw new Error();
		}
		ResponseWindow responseWindow = new ResponseWindow(PieceColor.BLACK);
		SwingUtilities.invokeLater(new Thread() {
			public void run() {
				responseWindow.showGUI();
			}
		});
	}
}