package test.test.IndeterministicChess.IO;

import java.awt.*;

import javax.swing.*;

import com.google.common.collect.*;

import test.test.IndeterministicChess.Board.*;

@SuppressWarnings("serial")
public class ResponseWindow extends JPanel {
	JTextArea input;
	JProgressBar progressBar;
	Table<Integer,Integer,JTextArea> squares;
	
	public ResponseWindow() {
		super(new BorderLayout());
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(100);
		progressBar.setStringPainted(true);
		add(progressBar, BorderLayout.PAGE_START);
		input = new JTextArea(1,100);
		input.setMargin(new Insets(5, 5, 5, 5));
		input.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		input.setFont(new Font("monospaced", Font.PLAIN, 12));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel boardPanel = new JPanel();
		GridLayout boardOutput = new GridLayout(8, 8, 0, 0);
		boardPanel.setLayout(boardOutput);
		ImmutableTable.Builder<Integer, Integer, JTextArea> builder = ImmutableTable.builder();
		for (int y = 1; y <= 8; y++) {
			for (int x = 1; x <= 8; x++) {
				JTextArea panel = new JTextArea();
				panel.setFont(new Font("monospaced", Font.PLAIN, 50));
				builder.put(x, y, panel);
				boardPanel.add(panel);
			}
		}
		squares = builder.build();
		boardPanel.setSize(200, 200);
		add(boardPanel, BorderLayout.CENTER);
		add(new JSeparator(JSeparator.HORIZONTAL),BorderLayout.LINE_START);
		add(input, BorderLayout.PAGE_END);
	}
	
	
	
	/**
	 * Create the GUI and show it.
	 */
	public void showGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("IndeterministicChess");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Add content to the window.
		frame.add(this);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
	String message = "";
	
	public void refreshLog(){
		for(int x : squares.rowKeySet()){
			for(int y : squares.columnKeySet()){
				squares.get(x, y).setText(Chessboard.getInstance().getRandomSymbolOn(new Square(x,y)));
			}
		}
	}
}
