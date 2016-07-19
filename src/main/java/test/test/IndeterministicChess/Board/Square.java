package test.test.IndeterministicChess.Board;

public class Square {
	private int xPosition, yPosition;

	public int getXPosition() {
		return xPosition;
	}

	public int getYPosition() {
		return yPosition;
	}

	public Square(int xPosition, int yPosition) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}

	public SquareColor getSquareColor() {
		if ((xPosition + yPosition) % 2 == 0) {
			return SquareColor.BLACK;
		} else {
			return SquareColor.WHITE;
		}
	}

	@Override
	public int hashCode() {
		return ((Integer)getXPosition()).hashCode() ^ ((Integer)getYPosition()).hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Square) {
			Square otherSquare = (Square) other;
			if (otherSquare.getXPosition() == getXPosition() && otherSquare.getYPosition() == getYPosition()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return String.valueOf("abcdefgh".charAt(yPosition-1)) + xPosition;
	}
}