package test.test.IndeterministicChess.IO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.*;

import com.google.common.collect.*;

import test.test.IndeterministicChess.Board.*;
import test.test.IndeterministicChess.Piece.*;

public class ResponseWindow {
	final private JProgressBar progressBar;
	final private ImmutableBiMap<Square, JButton> squares;
	final private JButton buttonMove, buttonSplit, buttonRedetermine, buttonEnd;
	final public PieceColor player;
	final public JPanel panel;
	ResourceBundle bundle = ResourceBundle.getBundle("i18n");
	final private JFrame frame = new JFrame(bundle.getString("applicationTitle"));
	
	public void getResponse(){
		responseGetter =  new Thread() {
			public void run() {
				progressBar.setValue(100);
				buttonMove.setEnabled(true);
				buttonSplit.setEnabled(true);
				buttonRedetermine.setEnabled(true);
				buttonEnd.setEnabled(false);
				try {
					synchronized (wakeResponseGetter) {
						wakeResponseGetter.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				buttonMove.setEnabled(false);
				buttonSplit.setEnabled(false);
				buttonRedetermine.setEnabled(false);
				switch(moveOption){
				case MOVE:
					Set<Piece> alreadyMovedPieces = new HashSet<Piece>();
					while(true){
						Set<Piece> movablePieces = Chessboard.getInstance().getAllPiecesOf(player).stream().filter(piece -> piece.getExistanceProbability().asDouble() * 100 <= progressBar.getValue() && piece.canMove()).collect(Collectors.toSet());
						if(movablePieces.isEmpty()){
							break;
						}
						selectOnly(getOccupiedSquares(Sets.difference(movablePieces, alreadyMovedPieces)));
						//Get the piece that is to be moved
						waitForGUI();
						if(selection == null){//i.e. the exit button was pushed
							break;
						}
						Piece pieceToMove = selectAPieceOn(selection);
						Set<Square> nextSquares = pieceToMove.getPossibleNextSquares();
						selectOnly(nextSquares);
						//Get the piece's target square
						waitForGUI();
						//Move
						try {
							Chessboard.getInstance().movePiece(pieceToMove, selection);
							Double amountOfMoveLeft = progressBar.getValue() - (100 * pieceToMove.getExistanceProbability().asDouble());
							checkForPromotion();
							Chessboard.getInstance().combinePieces();
							progressBar.setValue(amountOfMoveLeft.intValue());
							alreadyMovedPieces.add(pieceToMove);
							} 
						catch (Exception e) {
							e.printStackTrace();
						}
						buttonEnd.setEnabled(true);//At least on move required
					}
					deselectAll();
					buttonEnd.setEnabled(false);
					break;
				case REDETERMINE:
					Chessboard.getInstance().redetermine();
					break;
				case SPLIT:
					Set<Piece> splittablePieces = Chessboard.getInstance().getAllPiecesOf(player).stream().filter(Piece::canSplit).filter(Piece::canMove).collect(Collectors.toSet());
					selectOnly(getOccupiedSquares(splittablePieces));
					//Get the piece that is to be moved
					waitForGUI();
					Piece pieceToMove = selectAPieceOn(selection);
					//Get the piece's target square
					Piece otherHalf;
					try {
						otherHalf = pieceToMove.splitOfHalf();
					} catch (Exception e1) {
						throw new Error(e1);
					}
					selectOnly(otherHalf.getPossibleNextSquares());
					waitForGUI();
					//Move one Half
					try {
						Chessboard.getInstance().movePiece(otherHalf, selection);
						checkForPromotion();
						Chessboard.getInstance().combinePieces();
					} catch (Exception e) {
						e.printStackTrace();
					}
					//Get the piece's target square
					selectOnly(pieceToMove.getPossibleNextSquares());
					waitForGUI();
					//Move remaining half
					try {
						Chessboard.getInstance().movePiece(pieceToMove, selection);
						checkForPromotion();
						Chessboard.getInstance().combinePieces();
					} catch (Exception e) {
						e.printStackTrace();
					}
					deselectAll();
					break;
				default:
					break;
				}
			}
		};
		responseGetter.start();
		synchronized (responseGetter) {
			try {
				responseGetter.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public enum moveOptions {
		MOVE, SPLIT, REDETERMINE
	}
	
	public Set<Square> getOccupiedSquares(Set<Piece> pieces) {
		return pieces.stream().map(Piece::getPosition).collect(Collectors.toSet());
	}
	
	private void waitForGUI(){
		try {
			synchronized (wakeResponseGetter) {
				wakeResponseGetter.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Object wakeResponseGetter = "";
	
	public moveOptions moveOption = moveOptions.MOVE;
	
	public Square selection = new Square(-1,-1);
	
	Thread responseGetter;
	
	private void checkForPromotion(){
		for(Pawn pawn : Chessboard.getInstance().getPawnsToBePromoted()){
			Object[] options = Chessboard.promotionPositions;
			String userChoice =	(String)JOptionPane.showInputDialog(frame, String.format(bundle.getString("promotionDialogueQuestion"), pawn.getPosition()), bundle.getString("promotionDialogueTitle"), JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			try {
				Chessboard.getInstance().changeRole(pawn, userChoice);
			} catch (Exception e) {
				//Try again until the user doesn't quit any more
				checkForPromotion();
			}
		}
	}
	
	private void deselectAll(){
		selectOnly(new HashSet<Square>());
	}
	
	private void selectOnly(Set<Square> those){
		for(Square square : squares.keySet()){
			JButton button = squares.get(square);
			setButtonEnabled(button, those.contains(square));
		}
	}
	
	private Piece selectAPieceOn(Square square){
		Set<Piece> piecesOn = Chessboard.getInstance().getPiecesOnSquare(square, player);
		if(piecesOn.size() == 1){
			return piecesOn.iterator().next();
		}
		else if(piecesOn.size() > 1){
			Object[] options = piecesOn.stream().map(Piece::getTypeName).toArray(String[]::new);
			Object userChoice = JOptionPane.showInputDialog(frame, bundle.getString("pieceSelectionDialogueQuestion"), bundle.getString("pieceSelectionDialogueTitle"), JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if(userChoice == null){
				return selectAPieceOn(square);
			}
			else {
				String selectedPiece =	(String)userChoice;
				return piecesOn.stream().filter(piece -> piece.getTypeName().equals(selectedPiece)).findAny().get();
			}
		}
		else {
			throw new Error("A Square without pieces on it was selectable for moving.");
		}
	}
	
	public void showWin(){
		JOptionPane.showMessageDialog(panel, bundle.getString("winMessage"));
	}
	
	public void showLose(){
		JOptionPane.showMessageDialog(panel, bundle.getString("loseMessage"));
	}
	
	private class buttonMoveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (wakeResponseGetter) {
				moveOption = moveOptions.MOVE;
				wakeResponseGetter.notifyAll();
			}
		}
	}
	
	private class buttonSplitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (wakeResponseGetter) {
				moveOption = moveOptions.SPLIT;
				wakeResponseGetter.notifyAll();
			}
		}
	}
	
	private class buttonRedetermineListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (wakeResponseGetter) {
				moveOption = moveOptions.REDETERMINE;
				wakeResponseGetter.notifyAll();
			}
		}
	}
	
	private class buttonEndListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (wakeResponseGetter) {
				selection = null;
				wakeResponseGetter.notifyAll();
			}
		}
	}
	
	private class squareButtonsListener implements ActionListener {
		Square square;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (wakeResponseGetter) {
				selection = square;
				wakeResponseGetter.notifyAll();
			}
		}
	}
	
	public ResponseWindow(PieceColor player) {
		panel = new JPanel(new BorderLayout());
		this.player = player;
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(100);
		progressBar.setStringPainted(true);
		panel.add(progressBar, BorderLayout.PAGE_START);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
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
		panel.add(boardPanel, BorderLayout.CENTER);
		panel.add(new JSeparator(JSeparator.HORIZONTAL),BorderLayout.LINE_START);
		JPanel buttonPanel =  new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonMove = new JButton(bundle.getString("moveButton"));
		buttonMove.addActionListener(new buttonMoveListener());
		buttonMove.setEnabled(false);
		buttonSplit = new JButton(bundle.getString("splitButton"));
		buttonSplit.addActionListener(new buttonSplitListener());
		buttonSplit.setEnabled(false);
		buttonRedetermine = new JButton(bundle.getString("redetermineButton"));
		buttonRedetermine.addActionListener(new buttonRedetermineListener());
		buttonRedetermine.setEnabled(false);
		buttonEnd = new JButton(bundle.getString("endButton"));
		buttonEnd.addActionListener(new buttonEndListener());
		buttonEnd.setEnabled(false);
		buttonPanel.add(buttonMove);
		buttonPanel.add(buttonSplit);
		buttonPanel.add(buttonRedetermine);
		buttonPanel.add(buttonEnd);
		panel.add(buttonPanel,BorderLayout.PAGE_END);
	}
	
	/**
	 * Create the GUI and show it.
	 */
	public void showGUI() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		viewUpdater.start();
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		squares.values().stream().forEach(button -> setButtonEnabled(button, false));
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
