package test.test.IndeterministicChess.Piece;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class WholePiece {
	public final UUID id = UUID.randomUUID();
	
	private Set<Piece> parts = new HashSet<Piece>();
	
	public WholePiece(Piece piece){
		parts.add(piece);
	}
	
	public void addPart(Piece piece){
		parts.add(piece);
		piece.setWholePiece(this);
	}
	
	public void removePart(Piece piece){
		parts.add(piece);
	}
	
	public ExistenceProbability getTotalProbability(){
		return ExistenceProbability.sumProbability(parts.stream().map(Piece::getExistanceProbability).collect(Collectors.toList()));
	}
	
	public boolean isFull(){
		return getTotalProbability().equals(ExistenceProbability.ONE);
	}
}
