import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

public class Perft {
	private int maxPly = 7;
	private int nodes = 0;
	private int captures = 0;
	private int checks = 0;
	
	HashMap<String, Integer> perftLog = new HashMap<String, Integer>();
	
	public Perft() {}
	
	public String moveToUCI(Move move) {
		String output = "";
		String letters = "abcdefgh";
		
		output += letters.charAt(move.getFrom().getX());
		output += String.valueOf((7 - move.getFrom().getY()) + 1);
		output += letters.charAt(move.getTo().getX());
		output += String.valueOf((7 - move.getTo().getY()) + 1);
		
		if (move.getPromotion() != -1) {
			switch (move.getPromotion()) {
				case 5:
					output += "q";
					break;
				case 4:
					output += "r";
					break;
				case 3:
					output += "b";
					break;
				case 2:
					output += "n";
					break;
			}
		}
		
		return output;
	}
	
	public void run(Board board, int ply) {
		if (ply == 0) {			
			if (ply == this.maxPly) {
				this.nodes += 1;
			}
			
			this.run(board, ply + 1);
		} else {
			if (ply <= this.maxPly) {
				Vector<Move> moves = board.getMoves();
								
				for (int i = 0; i < moves.size(); i++) {
					//store for perft log					
					Board boardClone = board.clone();
					boardClone.playMoveSelf(moves.elementAt(i));
					
					if (ply == this.maxPly) {
						/*
							System.out.println(moves.elementAt(i));
							boardClone.printBoard();
						*/
						/*
						String UCIMove = this.moveToUCI(moves.elementAt(i));
						this.nodes += 1;
						
						if (this.perftLog.containsKey(UCIMove)) {
							int newValue = this.perftLog.get(UCIMove) + 1;
							this.perftLog.put(UCIMove, newValue);
						} else {
							this.perftLog.put(UCIMove, 1);
						}
						*/
						
						if (moves.elementAt(i).getLoud()) {
							this.captures += 1;
						}
						
						if (boardClone.isThisChecked()) {
							this.checks += 1;
						}
					}
					
					this.run(boardClone, ply + 1);
				}
			}
		}
	}
	
	public int getNodes() {
		return this.nodes;
	}
	
	public int getCaptures() {
		return this.captures;
	}
	
	public int getChecks() {
		return this.checks;
	}
	
	public void printPerftLog() {
		for (Map.Entry<String, Integer> set :
			this.perftLog.entrySet()) {
			System.out.println(set.getKey() + ": " + set.getValue());
		}
	}
}