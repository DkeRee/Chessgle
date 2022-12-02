import java.util.Vector;

public class Engine extends Evaluation {
	final static int CHECKMATE = 100;
	final static int STALEMATE = 90;
	final static int ONGOING = 80;
	
	private int CHECKMATE_BASE = 30000;
	private int STALEMATE_BASE = 0;
	private int fixedDepth;
	
	public Engine(int fixedDepth) {
		this.fixedDepth = fixedDepth;
	}
	
	public Vector<Move> getLoudMoves(Board board) {
		Vector<Move> moves = board.getMoves();
		
		Vector<Move> loudMoves = new Vector<Move>();
		
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.elementAt(i);
			
			if (move.getLoud()) {
				loudMoves.add(move);
			}
		}
		
		return loudMoves;
	}
	
	public Move searchRoot(Board board) {
		int alpha = -Integer.MAX_VALUE;
		int beta = Integer.MAX_VALUE;
		
		Vector<Move> moves = board.getMoves();
		
		Move bestMove = null;
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.elementAt(i);
			
			Board boardClone = board.clone();
			boardClone.playMoveSelf(move);
			
			int score = -this.search(boardClone, -beta, -alpha, this.fixedDepth - 1, 1);
			
			if (score > alpha) {
				alpha = score;
				bestMove = move;
			}
		}
		
		return bestMove;
	}
	
	public int search(Board board, int alpha, int beta, int depth, int ply) {
		int gameState = board.getState();
		
		//return checkmates
		switch (gameState) {
			case CHECKMATE:
				return -CHECKMATE_BASE + ply;
			case STALEMATE:
				return STALEMATE_BASE;
		}
		
		if (depth == 0) {
			return this.qSearch(board, alpha, beta, ply);
		}
		
		Vector<Move> moves = board.getMoves();
		
		int bestScore = -Integer.MAX_VALUE;
		
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.elementAt(i);
			
			Board boardClone = board.clone();
			boardClone.playMoveSelf(move);
			
			int score = -this.search(boardClone, -beta, -alpha, depth - 1, ply + 1);
			
			if (score > bestScore) {
				bestScore = score;
				
				if (bestScore > alpha) {
					alpha = bestScore;
					
					if (alpha >= beta) {
						//beta cutoff
						break;
					}
				}
			}
		}
		
		return bestScore;
	}
	
	public int qSearch(Board board, int alpha, int beta, int ply) {
		int gameState = board.getState();
		
		//return checkmates
		switch (gameState) {
			case CHECKMATE:
				return -CHECKMATE_BASE + ply;
			case STALEMATE:
				return STALEMATE_BASE;
		}
		
		int standPat = super.evaluate(board);
		
		if (standPat >= beta) {
			return beta;
		}
		
		if (standPat > alpha) {
			alpha = standPat;
		}
		
		Vector<Move> loudMoves = this.getLoudMoves(board);
		
		if (loudMoves.size() == 0) {
			return standPat;
		}
		
		int bestScore = -Integer.MAX_VALUE;
		for (int i = 0; i < loudMoves.size(); i++) {
			Move move = loudMoves.elementAt(i);
			
			Board boardClone = board.clone();
			boardClone.playMoveSelf(move);
			
			int score = -this.qSearch(boardClone, -beta, -alpha, ply + 1);
			
			if (score > bestScore) {
				bestScore = score;
				
				if (score > alpha) {
					alpha = score;
					
					if (alpha >= beta) {
						break;
					}
				}
			}
		}
		
		return bestScore;
	}
}