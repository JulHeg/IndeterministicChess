package test.test.IndeterministicChess.IO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.*;

import com.google.common.collect.*;

import test.test.IndeterministicChess.Board.*;
import test.test.IndeterministicChess.Piece.*;

public class LocalGUIWindow extends generalIO{
	final private JProgressBar progressBar;
	final private ImmutableBiMap<Square, JButton> squares;
	final private JButton buttonMove, buttonSplit, buttonRedetermine, buttonEnd;
	final public JPanel panel;
	ResourceBundle bundle;
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
	
	public LocalGUIWindow(PieceColor player, Chessboard chessboard) {
		super(player, chessboard);
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
    	bundle = ResourceBundle.getBundle("i18n");
		bundle.getString("applicationTitle");
		bundle.getString(player == PieceColor.BLACK ? "blackName" : "whiteName");
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

	@Override
	protected moveOptions getMoveOption() {
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
		return moveOption;
	}

	@Override
	protected Square selectOneOfTheseSquares(Set<Square> theseSquares) {
		selectOnly(theseSquares);
		waitForGUI();
		deselectAll();
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
		deselectAll();
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
	
	protected String selectPromotionOption(Set<String> options, Square postion){
		List<String> translations = options.stream().map(bundle::getString).collect(Collectors.toList());
		System.out.println(translations);
		Object userChoice = JOptionPane.showInputDialog(frame, String.format(bundle.getString("promotionDialogueQuestion"), postion), bundle.getString("promotionDialogueTitle"), JOptionPane.PLAIN_MESSAGE, null, translations.toArray(), translations.get(0));
		if(userChoice == null) {
			//If the user cancels the dialogue, just ask again
			userChoice = selectPromotionOption(options, postion);
		}
		final String finalUserChoice = (String)userChoice;
		return options.stream().filter(option -> bundle.getString(option).equals(finalUserChoice)).findAny().get();
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
	protected Piece selectAPieceOf(Set<Piece> pieces){
		if(pieces.size() == 1){
			return pieces.iterator().next();
		}
		else if(pieces.size() > 1){
			String[] options = pieces.stream().map(Piece::getTypeName).map(bundle::getString).toArray(String[]::new);
			Object userChoice = JOptionPane.showInputDialog(frame, bundle.getString("pieceSelectionDialogueQuestion"), bundle.getString("pieceSelectionDialogueTitle"), JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if(userChoice == null){
				return selectAPieceOf(pieces);
			}
			else {
				String selectedPiece =	(String)userChoice;
				return pieces.stream().filter(piece -> bundle.getString(piece.getTypeName()).equals(selectedPiece)).findAny().get();
			}
		}
		else {
			throw new Error("There has to be a movable piece on the selected Square!");
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
			String Symbol = chessboard.getProbabilisticSymbolOn(square);
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

	@Override
	protected void disableMoveEnding() {
		buttonEnd.setEnabled(false);
	}
}
