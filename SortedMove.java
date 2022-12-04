public class SortedMove {
	private Move move;
	private int score;
	
	public SortedMove(Move move, int score) {
		this.move = move;
		this.score = score;
	}
	
	public Move getMove() {
		return this.move;
	}
	
	public int getScore() {
		return this.score;
	}
}