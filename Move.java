public class Move {
	private Square from;
	private Square to;
	private int piece;
	private boolean loud;
	private int promotion = -1;

	
	public Move(Square from, Square to, int piece, boolean loud) {
		this.from = from;
		this.to = to;
		this.piece = piece;
		this.loud = loud;
	}
	
	public Move(Square from, Square to, int piece, boolean loud, int promotion) {
		this(from, to, piece, loud);
		this.promotion = promotion;
	}
	
	public Square getFrom() {
		return this.from;
	}
	
	public Square getTo() {
		return this.to;
	}
	
	public int getPiece() {
		return this.piece;
	}
	
	public boolean getLoud() {
		return this.loud;
	}
	
	public int getPromotion() {
		return this.promotion;
	}
	
	public String toString() {
		int pieceNum = 0;
		
		String pieceOutput = "";
		String color = "";
		
		if (this.piece % 10 == 0) {
			pieceNum = this.piece / 10;
			color = "White";
		} else {
			pieceNum = this.piece / 13;
			color = "Black";
		}
				
		switch (pieceNum) {
			case 1:
				pieceOutput = "Pawn";
				break;
			case 2:
				pieceOutput = "Knight";
				break;
			case 3:
				pieceOutput = "Bishop";
				break;
			case 4:
				pieceOutput = "Rook";
				break;
			case 5:
				pieceOutput = "Queen";
				break;
			case 6:
				pieceOutput = "King";
				break;
		}
		
		return "From: " + this.from + ", To: " + this.to + ";" + "Piece: " + pieceOutput + "; Color: " + color + "; " + "Promotion: " + this.promotion + "; Loud: " + this.loud;
	}
}