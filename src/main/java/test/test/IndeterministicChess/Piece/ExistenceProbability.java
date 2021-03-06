package test.test.IndeterministicChess.Piece;

import java.util.List;

import org.apache.commons.math3.fraction.*;
import org.apache.commons.math3.util.FastMath;

public class ExistenceProbability {
	private final BigFraction probability;

	private ExistenceProbability(BigFraction probability) {
		this.probability = probability;
	}

	public static final ExistenceProbability ONE = new ExistenceProbability(BigFraction.ONE);
	public static final ExistenceProbability ZERO = new ExistenceProbability(BigFraction.ZERO);

	public static final ExistenceProbability ONE_TENTH = new ExistenceProbability(new BigFraction(1,10));

	public static ExistenceProbability fromBigFraction(BigFraction bigFraction) throws Exception {
		if (!ONE.greaterEqual(bigFraction) || !ZERO.lessEqual(bigFraction)) {
			throw new Exception("An ExistanceProbability must be between one and zero!");
		}
		return new ExistenceProbability(bigFraction);
	}

	public ExistenceProbability getHalf() {
		return new ExistenceProbability(probability.multiply(BigFraction.ONE_HALF));
	}

	public BigFraction getProbability() {
		return probability;
	}

	public double asDouble() {
		return probability.doubleValue();
	}

	@Override
	public int hashCode() {
		return probability.getNumerator().hashCode() ^ probability.getDenominator().hashCode();
	}

	@Override
	public String toString() {
		return String.valueOf(FastMath.floor(asDouble() * 100)) + "%";
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ExistenceProbability)) {
			return false;
		}
		return ((ExistenceProbability) o).getProbability().equals(probability);
	}

	public static ExistenceProbability sumProbability(List<ExistenceProbability> summands) {
		BigFraction result = BigFraction.ZERO;
		for (ExistenceProbability summand : summands) {
			result = result.add(summand.getProbability());
		}
		return new ExistenceProbability(result);
	}

	public ExistenceProbability getRest() {
		return new ExistenceProbability(BigFraction.ONE.subtract(probability));
		// One minus the probability
	}

	public ExistenceProbability cap(ExistenceProbability maximum) {
		// min(a,b)=(a+b-|a-b|)/2
		BigFraction a = maximum.getProbability();
		BigFraction b = probability;
		return new ExistenceProbability(a.add(b).subtract(a.subtract(b).abs()).multiply(BigFraction.ONE_HALF));
	}

	/*
	 * True iff this is greater than the other.
	 */
	public boolean greaterEqual(ExistenceProbability other) {
		return greaterEqual(other.getProbability());
	}

	public boolean lessEqual(ExistenceProbability other) {
		return greaterEqual(other.getProbability());
	}

	public boolean greaterEqual(BigFraction other) {
		return probability.compareTo(other) > -1;
	}

	public boolean lessEqual(BigFraction other) {
		return probability.compareTo(other) < +1;
	}

	public ExistenceProbability multiply(ExistenceProbability other) {
		return new ExistenceProbability(probability.multiply(other.getProbability()));
	}

	public ExistenceProbability add(ExistenceProbability other) {
		return new ExistenceProbability(probability.add(other.getProbability()));
	}

	public ExistenceProbability divide(ExistenceProbability other) {
		return new ExistenceProbability(probability.divide(other.getProbability()));
	}
}