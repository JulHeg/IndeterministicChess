package test.test.IndeterministicChess.IO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.*;

import com.google.common.collect.*;

import test.test.IndeterministicChess.Board.*;
import test.test.IndeterministicChess.Piece.*;

@SuppressWarnings("serial")
public class ResponseWindow extends JPanel {
	final private JProgressBar progressBar;
	final private ImmutableBiMap<Square, JButton> squares;
	final private JButton buttonMove, buttonSplit, buttonRedetermine, buttonEnd;
	final public PieceColor player;
	private WaitState waitState;
	private Piece selectedPiece;
	
	public WaitState getWaitState(){
		return waitState;
	}
	
	public void makeWaitState(WaitState newWaitState){
		switch(newWaitState){
		case IS_WAITING_FOR_OPPONENT:
			buttonMove.setEnabled(false);
			buttonSplit.setEnabled(false);
			buttonRedetermine.setEnabled(false);
			buttonEnd.setEnabled(false);
			for(Square square : squares.keySet()){
				JButton button = squares.get(square);
				setButtonEnabled(button, false);
			}
			break;
		case IS_WAITING_MOVE_OPTION:
			buttonMove.setEnabled(true);
			buttonSplit.setEnabled(true);
			buttonRedetermine.setEnabled(true);
			buttonEnd.setEnabled(false);
			for(Square square : squares.keySet()){
				JButton button = squares.get(square);
				setButtonEnabled(button, false);
			}
			break;
		case IS_WAITING_FOR_MOVE_PIECE_SELCETION:
			buttonMove.setEnabled(false);
			buttonSplit.setEnabled(false);
			buttonRedetermine.setEnabled(false);
			buttonEnd.setEnabled(false);
			for(Square square : squares.keySet()){
				JButton button = squares.get(square);
				if(Chessboard.getInstance().getPiecesOnSquare(square, player).stream().filter(piece -> piece.getExistanceProbability().asDouble() * 100 <= progressBar.getValue()).filter(piece -> piece.canMove()).collect(Collectors.toList()).isEmpty()){
					setButtonEnabled(button, false);
				}
				else{
					setButtonEnabled(button, true);
				}
			}
			break;
		case IS_WAITING_FOR_MOVE_TARGET_SELCETION:
			Set<Square> possibleSquares = selectedPiece.getPossibleNextSquares();
			for(Square square : squares.keySet()){
				JButton button = squares.get(square);
				if(possibleSquares.contains(square)){
					setButtonEnabled(button, true);
				}
				else{
					setButtonEnabled(button, false);
				}
			}
			break;
		default:
			break;
		}
		waitState = newWaitState;
	}
	
	private class buttonMoveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			makeWaitState(WaitState.IS_WAITING_FOR_MOVE_PIECE_SELCETION);
		}
	}
	
	private class buttonRedetermineListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Chessboard.getInstance().redetermine();
			makeWaitState(WaitState.IS_WAITING_FOR_OPPONENT);
		}
	}
	
	private class squareButtonsListener implements ActionListener {
		Square square;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(waitState){
			case IS_WAITING_FOR_MOVE_PIECE_SELCETION:
				Set<Piece> piecesOn = Chessboard.getInstance().getPiecesOnSquare(square, player);
				if(piecesOn.size() == 1){
					selectedPiece = piecesOn.iterator().next();
				}
				else if(piecesOn.size() > 1){
					Object[] options = piecesOn.stream().map(Piece::getTypeName).toArray(String[]::new);
					String userChoice =	(String)JOptionPane.showInputDialog(frame, "Which piece do you want to move?", "Piece Selection", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
					selectedPiece = piecesOn.stream().filter(piece -> piece.getTypeName().equals(userChoice)).findAny().get();
				}
				else {
					throw new Error("A Square without pieces on it was selectable for moving.");
				}
				makeWaitState(WaitState.IS_WAITING_FOR_MOVE_TARGET_SELCETION);
				break;
			case IS_WAITING_FOR_MOVE_TARGET_SELCETION:
				try {
					Chessboard.getInstance().movePiece(selectedPiece, square);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				Double amountOfMoveLeft = progressBar.getValue() - (100 * selectedPiece.getExistanceProbability().asDouble());
				progressBar.setValue(amountOfMoveLeft.intValue());
				makeWaitState(WaitState.IS_WAITING_FOR_OPPONENT);
				break;
			default:
				break;
			}
		}
	}
	
	public ResponseWindow(PieceColor player) {
		super(new BorderLayout());
		this.player = player;
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(100);
		progressBar.setStringPainted(true);
		add(progressBar, BorderLayout.PAGE_START);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel boardPanel = new JPanel();
		GridLayout boardOutput = new GridLayout(8, 8, 0, 0);
		boardPanel.setLayout(boardOutput);
		ImmutableBiMap.Builder<Square, JButton> builder = ImmutableBiMap.builder();
		for (int unmirroredY = 1; unmirroredY <= 8; unmirroredY++) {
			//Order matters while adding buttons to GridLayout
			int y = player == PieceColor.BLACK ? unmirroredY : 9-unmirroredY;
			for (int unmirroredX = 1; unmirroredX <= 8; unmirroredX++) {
				int x = player == PieceColor.BLACK ? 9 - unmirroredX : unmirroredX;
				Square square = new Square(x, y);
				JButton button = new JButton();
				button.setFont(new Font("monospaced", Font.PLAIN, 50));
				builder.put(square, button);
				button.setBorderPainted(false);
				button.setOpaque(true);
				button.setMargin(new Insets(0, 0, 0, 0));
				button.setBorder(null);
				squareButtonsListener listener = new squareButtonsListener();
				listener.square = square;
				button.addActionListener(listener);
				boardPanel.add(button);
			}
		}
		squares = builder.build();
		boardPanel.setSize(200, 200);
		add(boardPanel, BorderLayout.CENTER);
		add(new JSeparator(JSeparator.HORIZONTAL),BorderLayout.LINE_START);
		JPanel buttonPanel =  new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonMove = new JButton("Move");
		buttonMove.addActionListener(new buttonMoveListener());
		buttonSplit = new JButton("Split");
		buttonRedetermine = new JButton("Redetermine");
		buttonRedetermine.addActionListener(new buttonRedetermineListener());
		buttonEnd = new JButton("End Move");
		buttonPanel.add(buttonMove);
		buttonPanel.add(buttonSplit);
		buttonPanel.add(buttonRedetermine);
		buttonPanel.add(buttonEnd);
		add(buttonPanel,BorderLayout.PAGE_END);
		makeWaitState(player == PieceColor.WHITE ? WaitState.IS_WAITING_MOVE_OPTION: WaitState.IS_WAITING_FOR_OPPONENT);
	}
	
	JFrame frame;
	
	/**
	 * Create the GUI and show it.
	 */
	public void showGUI() {
		frame = new JFrame("IndeterministicChess");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		viewUpdater.start();
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		makeWaitState(WaitState.IS_WAITING_MOVE_OPTION);
	}
	
	String message = "";
	
	public void refreshLog(){
		for(JButton button : squares.values()){
			Square square = squares.inverse().get(button);
			button.setText(Chessboard.getInstance().getProbabilisticSymbolOn(square));
		}
	}
	
	Thread viewUpdater = new Thread() {
		public void run() {
			while(true) {
				refreshLog();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	};
	
	private void setButtonEnabled(JButton button, boolean enabled){
		Square square = squares.inverse().get(button);
		if(enabled){
			button.setBackground(Color.GREEN);
		}
		else{
			button.setBackground(square.getSquareColor() == SquareColor.BLACK ? Color.LIGHT_GRAY : Color.WHITE);
		}
		button.setEnabled(enabled);
	}
}
