package test.test.IndeterministicChess.IO;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.google.common.collect.*;

import test.test.IndeterministicChess.Board.*;
import test.test.IndeterministicChess.Piece.*;

public class ResponseWindow extends generalIO{
	final private JProgressBar progressBar;
	final private ImmutableBiMap<Square, JButton> squares;
	final private JButton buttonMove, buttonSplit, buttonRedetermine, buttonEnd;
	final public JPanel panel;
	ResourceBundle bundle = ResourceBundle.getBundle("i18n");
	final private JFrame frame;
	final private Color whiteSquareColor = new Color(255, 204, 128);
	final private Color blackSquareColor = new Color(194, 153, 112);
	final private Color whiteSquareColorSelected = new Color(230, 255, 153);
	final private Color blackSquareColorSelected = new Color(153, 194, 112);
	
	@Override
	protected void setAmountOfMoveLeft(int target){
		int oldValue = progressBar.getValue();
		Thread progressbarAnimator = new Thread() {
			public void run() {
				int stepCount = 40;
				int stepDuration = 10; //In milliseconds
				for(int i = 0; i <= stepCount; i++){
					progressBar.setValue(oldValue + (target-oldValue)*i/stepCount);
					try {
						sleep(stepDuration);
					} catch (InterruptedException e) { }
				}
			}
		};
		progressbarAnimator.start();
		super.setAmountOfMoveLeft(target);
	}
	
	public ResponseWindow(PieceColor player) {
		super(player);
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		frame = new JFrame(bundle.getString("applicationTitle") + " - " + bundle.getString(player == PieceColor.BLACK ? "blackName" : "whiteName"));
		panel = new JPanel(new BorderLayout());
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
	
	public void getResponse(){
		responseGetter =  new Thread() {
			public void run() {
				setAmountOfMoveLeft(100);
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
					makeMovingMove();
					break;
				case REDETERMINE:
					Chessboard.getInstance().redetermine();
					break;
				case SPLIT:
					makeSplittingMove();
					break;
				}
				buttonEnd.setEnabled(false);
				deselectAll();
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

	@Override
	protected Square selectOneOfTheseSquares(Set<Square> theseSquares) {
		selectOnly(theseSquares);
		waitForGUI();
		return selection;
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
	
	public enum moveOptions {
		MOVE, SPLIT, REDETERMINE
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
	
	@Override
	protected void checkForPromotion(){
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
	
	@Override
	protected Piece selectAPieceOn(Square square){
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

	@Override
	public void showWin(){
		showMessageInNewThread(bundle.getString("winMessage"));
	}

	@Override
	public void showLose(){
		showMessageInNewThread(bundle.getString("loseMessage"));
	}

	@Override
	public void showDraw(){
		showMessageInNewThread(bundle.getString("drawMessage"));
	}
	
	private void showMessageInNewThread(String message){
		new Thread() {
			public void run() {
	JOptionPane.showMessageDialog(panel,  message);
			}}.start();
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
	
	String message = "";
	
	public void refreshLog(){
		for(JButton button : squares.values()){
			Square square = squares.inverse().get(button);
			String Symbol = Chessboard.getInstance().getProbabilisticSymbolOn(square);
			button.setText(Symbol);
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
	
	private void setButtonEnabled(JButton button, boolean enabled){Square square = squares.inverse().get(button);
		if(enabled){
			button.setBackground(square.getSquareColor() == SquareColor.BLACK ? blackSquareColorSelected : whiteSquareColorSelected);
		}
		else{
			button.setBackground(square.getSquareColor() == SquareColor.BLACK ? blackSquareColor : whiteSquareColor);
		}
		button.setEnabled(enabled);
	}

	@Override
	protected void enableMoveEnding() {
		buttonEnd.setEnabled(true);
	}
}
