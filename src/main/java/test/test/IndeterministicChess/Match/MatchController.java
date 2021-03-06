package test.test.IndeterministicChess.Match;

import javax.swing.SwingUtilities;

import test.test.IndeterministicChess.Board.Chessboard;
import test.test.IndeterministicChess.IO.GeneralUI;
import test.test.IndeterministicChess.Piece.PieceColor;

public class MatchController extends Thread{
	private final Chessboard chessboard;
	private final GeneralUI blackIO;
	private final GeneralUI whiteIO;
	
	public MatchController(GeneralUI blackIO, GeneralUI whiteIO, Chessboard chessboard){
		this.blackIO = blackIO;
		this.whiteIO = whiteIO;
		this.chessboard = chessboard;
	}
	
	@Override	
	public void run(){
		SwingUtilities.invokeLater(new Thread() {
			public void run() {
				blackIO.showGUI();
				whiteIO.showGUI();
			}
		});
		PieceColor activePlayer = PieceColor.WHITE;
		boolean isLost = false;
		boolean isDraw = false;
		while(!isLost && !isDraw){
			switch(activePlayer){
			case WHITE:
				whiteIO.getResponse();
				break;
			case BLACK:
				blackIO.getResponse();
				break;
			}
			activePlayer = activePlayer.otherColor();
			isLost = chessboard.probabilisticHasLost(activePlayer);
			isDraw = chessboard.probabilisticHasLost(activePlayer);
		}
		if(activePlayer == PieceColor.BLACK){
			blackIO.showLose();
			whiteIO.showWin();
		}
		else{
			blackIO.showWin();
			whiteIO.showLose();
		}
	}
}
