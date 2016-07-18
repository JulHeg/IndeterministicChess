package test.test.IndeterministicChess.IO;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import test.test.IndeterministicChess.Board.Chessboard;
import test.test.IndeterministicChess.Board.Square;
import test.test.IndeterministicChess.Piece.ExistenceProbability;
import test.test.IndeterministicChess.Piece.Pawn;
import test.test.IndeterministicChess.Piece.PieceColor;

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
			Chessboard.getInstance().addChessPiece(new Pawn(new Square(4,4), PieceColor.WHITE, ExistenceProbability.ONE.getHalf()));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResponseWindow responseWindow = new ResponseWindow();
		SwingUtilities.invokeLater(new Thread() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("nimbusGreen", Boolean.FALSE);
				responseWindow.showGUI();
			}
		});
		
		new Thread() {
			public void run() {
				while(true) {
					responseWindow.refreshLog();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						break;
					}
				}
			}
		}.run();
		System.out.println("Hello World!");
		System.out.println("Hallo Welt!");
		System.out.println("Hello World!");
	}
}