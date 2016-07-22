package test.test.IndeterministicChess.Board;

import test.test.IndeterministicChess.Piece.ExistenceProbability;

/*
 * Options for a game. At the moment only the minimum amount of probability need to not disappear.
 */
public class ChessboardOptions {
	private ExistenceProbability minimalSurvivalProbability;
	
	public ExistenceProbability getMinimalSurvivalProbability(){
		return minimalSurvivalProbability;
	}
	
	public void setMinimalSurvivalProbability(ExistenceProbability existenceProbability){
		minimalSurvivalProbability = existenceProbability;
	}
	
	public ChessboardOptions(ExistenceProbability minimalSurvivalProbability){
		this.minimalSurvivalProbability = minimalSurvivalProbability;
	}
	
	public static ChessboardOptions standardValue = new ChessboardOptions(ExistenceProbability.ONE_TENTH);
}
