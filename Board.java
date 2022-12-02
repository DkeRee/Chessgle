import java.util.Vector;

public class Board extends BoardBackbone {
	private int colorPlaying = WHITE;
	private int[][] board;
	
	public Board() {
		board = new int[8][8];
		this.setup();
	}
	
	public void setup() {	
		/*
		super.whiteKingRights = false;
		super.whiteLeftRookRights = false;
		super.whiteRightRookRights = false;
		
		super.blackKingRights = false;
		super.blackLeftRookRights = false;
		super.blackRightRookRights = false;
		
		this.board[1][0] = PAWN * BLACK;
		this.board[4][1] = PAWN * WHITE;
		*/
		//first back rank of black
		this.board[0][0] = ROOK * BLACK;
		this.board[0][1] = KNIGHT * BLACK;
		this.board[0][2] = BISHOP * BLACK;
		this.board[0][3] = QUEEN * BLACK;
		this.board[0][4] = KING * BLACK;
		this.board[0][5] = BISHOP * BLACK;
		this.board[0][6] = KNIGHT * BLACK;
		this.board[0][7] = ROOK * BLACK;
		
		//set up pawns of black
		for (int i = 0 ; i < 8; i++) {
			this.board[1][i] = PAWN * BLACK;
		}
		
		//set up pawns of white
		for (int i = 0; i < 8; i++) {
			this.board[6][i] = PAWN * WHITE;
		}
		
		//first back rank of white
		this.board[7][0] = ROOK * WHITE;
		this.board[7][1] = KNIGHT * WHITE;
		this.board[7][2] = BISHOP * WHITE;
		this.board[7][3] = QUEEN * WHITE;
		this.board[7][4] = KING * WHITE;
		this.board[7][5] = BISHOP * WHITE;
		this.board[7][6] = KNIGHT * WHITE;
		this.board[7][7] = ROOK * WHITE;
	}
	
	public Board clone() {
		Board newBoard = new Board();
		newBoard.setBoard(this.board);
		newBoard.setRights(
			super.whiteKingRights,
			super.whiteRightRookRights,
			super.whiteLeftRookRights,
			super.blackKingRights,
			super.blackLeftRookRights,
			super.blackRightRookRights,
			super.canEnPassant,
			super.enPassantRow,
			super.enPassantColumn,
			this.colorPlaying
		);
				
		return newBoard;
	}
	
	public int[][] getBoard() {
		return this.board;
	}
	
	public void setBoard(int[][] newBoard) {		
		for (int y = 0; y < newBoard.length; y++) {
			for (int x = 0; x < newBoard.length; x++) {
				int piece = newBoard[y][x];
				this.board[y][x] = piece;
			}
		}
	}
	
	public int getState() {
		Vector<Move> totalMoves = this.getMoves();
			
		boolean inCheck = this.isThisChecked();
		if (totalMoves.size() == 0 && inCheck) {
			return CHECKMATE;
		} else if (totalMoves.size() == 0 && !inCheck) {
			return STALEMATE;
		}
		
		return ONGOING;
	}
	
	public boolean printState() {
		if (this.getState() == 100) {
			System.out.println("Checkmate!");
			return false;
		} else if (this.getState() == 90) {
			System.out.println("Stalemate!");
			return false;
		}
		
		return true;
	}
	
	public void setRights(boolean wks, boolean wrr, boolean wlr, boolean bks, boolean brr, boolean blr, boolean canEnPassant, int enPassantRow, int enPassantColumn, int colorPlaying) {
		super.whiteKingRights = wks;
		super.whiteRightRookRights = wrr;
		super.whiteLeftRookRights = wlr;
		
		super.blackKingRights = bks;
		super.blackLeftRookRights = brr;
		super.blackRightRookRights = blr;
		super.canEnPassant = canEnPassant;
		super.enPassantColumn = enPassantColumn;
		super.enPassantRow = enPassantRow;
		this.colorPlaying = colorPlaying;
	}
 	
	public void parseFen(String fen) {
		String[] fenParts = fen.split(" ");
		String fenBoard = fenParts[0];
		String fenPlay = fenParts[1];
		String fenRooking = fenParts[2];
		String fenPassant = fenParts[3];
		
		//set side playing
		if (fenPlay.equals("w")) {
			this.colorPlaying = WHITE;
		} else {
			this.colorPlaying = BLACK;
		}
		
		super.whiteKingRights = false;
		super.whiteRightRookRights = false;
		super.whiteLeftRookRights = false;
		
		super.blackKingRights = false;
		super.blackLeftRookRights = false;
		super.blackRightRookRights = false;
		//set rooking rights
		if (!fenRooking.equals("-")) {
			//there is rooking			
			for (int i = 0; i < fenRooking.length(); i++) {
				char rookingInfo = fenRooking.charAt(i);
				if (Character.isUpperCase(rookingInfo)) {
					//is white
					super.whiteKingRights = true;
					
					if (rookingInfo == 'K') {
						super.whiteRightRookRights = true;
					} else if (rookingInfo == 'Q') {
						super.whiteLeftRookRights = true;
					}
				} else {
					//is black
					super.blackKingRights = true;
					
					if (rookingInfo == 'k') {
						super.blackRightRookRights = true;
					} else if (rookingInfo == 'q') {
						super.blackLeftRookRights = true;
					}
				}
			}
		} else {
			super.whiteKingRights = false;
			super.whiteRightRookRights = false;
			super.whiteLeftRookRights = false;
			
			super.blackKingRights = false;
			super.blackLeftRookRights = false;
			super.blackRightRookRights = false;
		}
				
		//set en passant
		if (!fenPassant.equals("-")) {
			//there is en passant!
			super.canEnPassant = true;
			
			String letters = "abcdefgh";
			int offset = 0;
			if (this.colorPlaying == WHITE) {
				offset = 1;
			} else {
				offset = -1;
			}
			
			super.enPassantColumn = letters.indexOf(String.valueOf(fenPassant.charAt(0)));
			super.enPassantRow =  (7 - (Integer.parseInt(String.valueOf(fenPassant.charAt(1))) - 1)) + offset;
		} else {
			super.canEnPassant = false;
			super.enPassantColumn = -1;
			super.enPassantRow = -1;
		}
		
		//set board with fen
		this.board = new int[8][8];
		
		String[] fenBoardRows = fenBoard.split("/");
		String numbers = "12345678";
		
		int boardY = 0;
		int boardX = 0;
		for (int i = 0; i < fenBoardRows.length; i++) {
			String[] tilesArr = fenBoardRows[i].split("");
			
			for (int o = 0; o < tilesArr.length; o++) {
				if (numbers.indexOf(tilesArr[o]) == -1) {
					//it is a piece and not a number of empty tiles
					int color = 0;
					int piece = 0;
					
					if (Character.isUpperCase(tilesArr[o].charAt(0))) {
						color = WHITE;
					} else {
						color = BLACK;
					}
					
					switch (tilesArr[o].toLowerCase()) {
						case "p":
							piece = PAWN;
							break;
						case "n":
							piece = KNIGHT;
							break;
						case "b":
							piece = BISHOP;
							break;
						case "r":
							piece = ROOK;
							break;
						case "q":
							piece = QUEEN;
							break;
						case "k":
							piece = KING;
							break;
					}
					
					this.board[boardY][boardX] = piece * color;
					
					boardX += 1;
				} else {
					boardX += Integer.parseInt(tilesArr[o]);
				}
			}
			
			boardX = 0;
			boardY += 1;
		}
	}
	
	public boolean isThisChecked() {
		return super.isChecked(this.board, WHITE);
	}
	
	public void pushPieceMoves(int[][] board, int piece, Vector<Move> moves, Square from) {
		//0 -> piece
		//1 -> color
		int[] pieceInfo = super.getPieceInfo(piece);
		
		switch (pieceInfo[0]) {
			case PAWN:
				super.genPawnMoves(board, moves, from, pieceInfo);
				break;
			case KNIGHT:
				super.genKnightMoves(board, moves, from, pieceInfo);
				break;
			case BISHOP:
				super.genBishopMoves(board, moves, from, pieceInfo);
				break;
			case ROOK:
				super.genRookMoves(board, moves, from, pieceInfo);
				break;
			case QUEEN:
				super.genQueenMoves(board, moves, from, pieceInfo);
				break;
			case KING:
				super.genKingMoves(board, moves, from, pieceInfo);
				break;
		}
	}
	
	public Vector<Move> getMoves() {
		Vector<Move> moves = new Vector<Move>();
		
		for (int y = 0; y < this.board.length; y++) {
			for (int x = 0; x < this.board[y].length; x++) {
				int piece = this.board[y][x];
				int[] pieceInfo = super.getPieceInfo(piece);
				
				if (pieceInfo[1] == this.colorPlaying) {
					Square square = new Square(x, y);
					this.pushPieceMoves(this.board, piece, moves, square);	
				}
			}
		}
		
		return moves;
	}
	
	public Move findMove(Square from, Square to, String promotion) {
		Vector<Move> moves = this.getMoves();
		
		for (int i = 0; i < moves.size(); i++) {
			boolean matchingFrom = moves.elementAt(i).getFrom().getX() == from.getX() && moves.elementAt(i).getFrom().getY() == from.getY();
			boolean matchingTo = moves.elementAt(i).getTo().getX() == to.getX() && moves.elementAt(i).getTo().getY() == to.getY();
			boolean promotionMatching = true;			
			
			if (promotion != null) {
				promotionMatching = false;
						
				int promotionIndex = 0;
				
				switch (promotion) {
					case "q":
						promotionIndex = QUEEN;
						break;
					case "r":
						promotionIndex = ROOK;
						break;
					case "b":
						promotionIndex = BISHOP;
						break;
					case "n":
						promotionIndex = KNIGHT;
						break;
				}
								
				if (moves.elementAt(i).getPromotion() != -1) {
					int promotionPiece = moves.elementAt(i).getPromotion();
					promotionMatching = promotionPiece == promotionIndex;
				}
			}
						
			if (matchingFrom && matchingTo && promotionMatching) {
				return moves.elementAt(i);
			}
		}
		
		return null;
	}
	
	public void playMove(String input) {
		String[] inputArr = input.split("");
		
		String letters = "abcdefgh";
		
		Square from = new Square(letters.indexOf(inputArr[0]), 7 - (Integer.parseInt(inputArr[1]) - 1));
		Square to = new Square(letters.indexOf(inputArr[2]), 7 - (Integer.parseInt(inputArr[3]) - 1));
		String promotion = null;
		
		if (inputArr.length > 4) {
			promotion = inputArr[4];
		}
		
		Move move = this.findMove(from, to, promotion);
		
		if (move != null) {
			super.playChecked(this.board, move);
			this.colorPlaying = super.getOppositeColor(this.colorPlaying);
		} else {
			System.out.println("This move is invalid.");
		}
	}
	
	public void playMoveSelf(Move move) {
		super.playChecked(this.board, move);
		this.colorPlaying = super.getOppositeColor(this.colorPlaying);
	}
	
	public int getColorPlaying() {
		return this.colorPlaying;
	}
	
	public void printBoard() {
		for (int y = 0; y < this.board.length; y++) {
			for (int x = 0; x < this.board[y].length; x++) {
				int piece = this.board[y][x];
				
				int[] pieceInfo = super.getPieceInfo(piece);
				boolean isWhite = pieceInfo[1] == WHITE;
				
				switch (pieceInfo[0]) {
					case NONE:
						System.out.print(super.getNoneAscii());
						break;
					case PAWN:
						System.out.print(super.getPawnAscii(isWhite));
						break;
					case KNIGHT:
						System.out.print(super.getKnightAscii(isWhite));
						break;
					case BISHOP:
						System.out.print(super.getBishopAscii(isWhite));
						break;
					case ROOK:
						System.out.print(super.getRookAscii(isWhite));
						break;
					case QUEEN:
						System.out.print(super.getQueenAscii(isWhite));
						break;
					case KING:
						System.out.print(super.getKingAscii(isWhite));
						break;
				}
				
				System.out.print(" ");
			}
			System.out.println();
		}
		
		System.out.println();
	}
}