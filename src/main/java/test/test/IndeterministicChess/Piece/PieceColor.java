package test.test.IndeterministicChess.Piece;

public enum PieceColor {
	BLACK, WHITE;

	public PieceColor otherColor() {
		switch (this) {
		case WHITE:
			return BLACK;
		case BLACK:
			return WHITE;
		default:
			return null;
		}
	}
}
