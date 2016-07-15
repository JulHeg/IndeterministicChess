package test.test.IndeterministicChess.Piece;

import java.util.Set;

import org.apache.commons.math3.fraction.*;
import org.apache.commons.math3.util.FastMath;

public class ExistenceProbability {
	private final BigFraction probability;

	private ExistenceProbability(BigFraction probability) {
		this.probability = probability;
	}

	public static final ExistenceProbability ONE = new ExistenceProbability(BigFraction.ONE);
	public static final ExistenceProbability ZERO = new ExistenceProbability(BigFraction.ZERO);

	public static final BigFraction ONE_TENTH = new BigFraction(1, 10);

	public ExistenceProbability formBigFraction(BigFraction bigFraction) throws Exception {
		if (ONE.greaterEqual(bigFraction) && ZERO.lessEqual(bigFraction)) {
			throw new Exception("An ExistanceProbability must be between one and zero!");
		}
		return new ExistenceProbability(probability);
	}

	public ExistenceProbability getHalf() {
		return new ExistenceProbability(probability.multiply(BigFraction.ONE_HALF));
	}

	public BigFraction getProbability() {
		return probability;
	}

	public BigFraction getProbabilityAsDouble() {
		return probability;
	}

	public boolean isDead() {
		return lessEqual(ONE_TENTH);
	}

	@Override
	public String toString() {
		return String.valueOf(FastMath.floor(probability.doubleValue() * 100)) + "%";
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ExistenceProbability)) {
			return false;
		}
		return ((ExistenceProbability) o).getProbability().equals(probability);
	}

	public static ExistenceProbability sumProbability(Set<ExistenceProbability> summands) {
		BigFraction result = BigFraction.ONE;
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

	public ExistenceProbability multiply(BigFraction other) {
		return new ExistenceProbability(probability.multiply(other));
	}

	public ExistenceProbability add(BigFraction other) {
		return new ExistenceProbability(probability.add(other));
	}
}