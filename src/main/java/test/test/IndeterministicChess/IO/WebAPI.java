package test.test.IndeterministicChess.IO;

import test.test.IndeterministicChess.Board.Chessboard;

public class WebAPI {
    @SuppressWarnings("unused")
	private final String content;

    public WebAPI(String content) {
        this.content = content;
    }

	public String getContent() {
        return Chessboard.getInstance().toProbabilisticString();
    }
}
