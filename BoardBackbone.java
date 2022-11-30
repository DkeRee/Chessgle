import java.util.Vector;

public abstract class BoardBackbone {
	final static int WHITE = 10;
	final static int BLACK = 13;
	
	final static int NONE = 0;
	final static int PAWN = 1;
	final static int KNIGHT = 2;
	final static int BISHOP = 3;
	final static int ROOK = 4;
	final static int QUEEN = 5;
	final static int KING = 6;
	
	//rooking
	protected boolean whiteKingRights = true;
	protected boolean whiteLeftRookRights = true;
	protected boolean whiteRightRookRights = true;
	
	protected boolean blackKingRights = true;
	protected boolean blackLeftRookRights = true;
	protected boolean blackRightRookRights = true;
	
	public int getOppositeColor(int color) {
		return color == WHITE ? BLACK : WHITE;
	}
	
	public Board upperClone(int[][] board) {
		Board newBoard = new Board();
		newBoard.setBoard(board);
		newBoard.setRights(
			this.whiteKingRights,
			this.whiteRightRookRights,
			this.whiteLeftRookRights,
			this.blackKingRights,
			this.blackRightRookRights,
			this.blackLeftRookRights
		);
				
		return newBoard;
	}
	
	public int[] getPieceInfo(int piece) {
		int[] info = new int[2];
		
		if (piece % WHITE == 0) {
			info[0] = piece / WHITE;
			info[1] = WHITE;
		} else {
			info[0] = piece / BLACK;
			info[1] = BLACK;
		}
		
		return info;
	}
	
	public void playUnchecked(int[][] board, Move move) {
		int[] pieceInfo = this.getPieceInfo(move.getPiece());
		
		board[move.getFrom().getY()][move.getFrom().getX()] = NONE;
		
		if (move.getPromotion() != -1) {
			board[move.getTo().getY()][move.getTo().getX()] = move.getPromotion() * pieceInfo[1];
		} else {
			board[move.getTo().getY()][move.getTo().getX()] = move.getPiece();
		}
	}
	
	public boolean isChecked(int[][] board, int color) {				
		//set up method to get my piece and enemy piece
		int myPiece = color;
		int enemyPiece = this.getOppositeColor(color);

		//my king
		Square kingSquare = new Square(0, 0);
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[y].length; x++) {
				int piece = board[y][x];
				
				if (piece != NONE) {
					int[] pieceInfo = this.getPieceInfo(piece);
					if (pieceInfo[0] == KING && pieceInfo[1] == color) {
						kingSquare = new Square(x, y);
						break;
					}
				}
			}
		}
		
		//expand from king in all directions, checking for possible check
		
		//north
		//straight sliders: queen, rook
		for (int y = kingSquare.getY(); y > -1; y--) {
			int piece = board[y][kingSquare.getX()];
						
			//if there is something and it's not my piece
			if (piece != NONE && y != kingSquare.getY()) {
				if (piece % enemyPiece == 0) {
					int pieceType = this.getPieceInfo(piece)[0];
										
					if (pieceType == QUEEN || pieceType == ROOK) {
						return true;
					} else {
						break;
					}		
				} else {
					break;
				}
			}
		}
		
		//south
		//straight sliders: queen, rook
		for (int y = kingSquare.getY(); y < 8; y++) {
			int piece = board[y][kingSquare.getX()];
			
			//if there is something and it's not my piece
			if (piece != NONE && y != kingSquare.getY()) {
				if (piece % enemyPiece == 0) {
					int pieceType = this.getPieceInfo(piece)[0];
					
					if (pieceType == QUEEN || pieceType == ROOK) {
						return true;
					} else {
						break;
					}				
				} else {
					break;
				}
			}
		}
		
		//west
		//straight sliders: queen, rook
		for (int x = kingSquare.getX(); x > -1; x--) {
			int piece = board[kingSquare.getY()][x];
			
			//if there is something and it's not my piece
			if (piece != NONE && x != kingSquare.getX()) {
				if (piece % enemyPiece == 0) {
					int pieceType = this.getPieceInfo(piece)[0];
					
					if (pieceType == QUEEN || pieceType == ROOK) {
						return true;
					} else {
						break;
					}
				} else {
					break;
				}
			}		
		}
		
		//east
		//straight sliders: queen, rook
		for (int x = kingSquare.getX(); x < 8; x++) {
			int piece = board[kingSquare.getY()][x];
			
			//if there is something and it's not my piece
			if (piece != NONE && x != kingSquare.getX()) {
				if (piece % enemyPiece == 0) {
					int pieceType = this.getPieceInfo(piece)[0];
					
					if (pieceType == QUEEN || pieceType == ROOK) {
						return true;
					} else {
						break;
					}
				} else {
					break;
				}
			}		
		}
		
		//north-east
		//diagnol sliders: queen, bishop
		int neInc = 1;
		while (kingSquare.getX() + neInc <= 7 && kingSquare.getY() - neInc >= 0) {			
			int incX = kingSquare.getX() + neInc;
			int incY = kingSquare.getY() - neInc;
			int piece = board[incY][incX];
			
			if (piece != NONE) {
				if (piece % enemyPiece == 0) {
					int pieceType = this.getPieceInfo(piece)[0];
					
					if (pieceType == QUEEN || pieceType == BISHOP) {
						return true;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			
			neInc++;
		}
		
		//north-west
		//diagnol sliders: queen, bishop
		int nwInc = 1;
		while (kingSquare.getX() - nwInc >= 0 && kingSquare.getY() - nwInc >= 0) {
			int incX = kingSquare.getX() - nwInc;
			int incY = kingSquare.getY() - nwInc;
			int piece = board[incY][incX];
			
			if (piece != NONE) {
				if (piece % enemyPiece == 0) {
					int pieceType = this.getPieceInfo(piece)[0];
					
					if (pieceType == QUEEN || pieceType == BISHOP) {
						return true;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			
			nwInc++;
		}
		
		//south-east
		//diagnol sliders: queen, bishop
		int seInc = 1;
		while (kingSquare.getX() + seInc <= 7 && kingSquare.getY() + seInc <= 7) {
			int incX = kingSquare.getX() + seInc;
			int incY = kingSquare.getY() + seInc;
			int piece = board[incY][incX];
			
			if (piece != NONE) {
				if (piece % enemyPiece == 0) {
					int pieceType = this.getPieceInfo(piece)[0];
					
					if (pieceType == QUEEN || pieceType == BISHOP) {
						return true;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			
			seInc++;
		}
		
		//south-west
		//diagnol sliders: queen, bishop
		int swInc = 1;
		while (kingSquare.getX() - swInc >= 0 && kingSquare.getY() + swInc <= 7) {
			int incX = kingSquare.getX() - swInc;
			int incY = kingSquare.getY() + swInc;
			int piece = board[incY][incX];
			
			if (piece != NONE) {
				if (piece % enemyPiece == 0) {
					int pieceType = this.getPieceInfo(piece)[0];
					
					if (pieceType == QUEEN || pieceType == BISHOP) {
						return true;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			
			swInc++;
		}
		
		//check for pawn checks
		Square[] offsets = new Square[2];

		if (color == WHITE) {
			offsets[0] = new Square(-1, -1);
			offsets[1] = new Square(1, -1);
		} else {
			offsets[0] = new Square(-1, 1);
			offsets[1] = new Square(1, 1);
		}
		
		for (int i = 0; i < offsets.length; i++) {
			int newX = kingSquare.getX() + offsets[i].getX();
			int newY = kingSquare.getY() + offsets[i].getY();
			
			if (0 <= newX && newX <= 7 && 0 <= newY && newY <= 7) {
				//in bounds
				int piece = board[newY][newX];
				
				if (piece != NONE && piece % enemyPiece == 0) {
					int pieceType = this.getPieceInfo(piece)[0];
					
					if (pieceType == PAWN) {
						return true;
					}
				}
			}
		}
		
		//check for king checks (walking into other king)
		Square[] kingOffsets = {new Square(-1, 0), new Square(-1, -1), new Square(0, -1), new Square(1, -1), new Square(1, 0), new Square(1, 1), new Square(0, 1), new Square(-1, 1)};
		for (int i = 0; i < kingOffsets.length; i++) {
			int newX = kingSquare.getX() + kingOffsets[i].getX();
			int newY = kingSquare.getY() + kingOffsets[i].getY();
			
			if (0 <= newX && newX <= 7 && 0 <= newY && newY <= 7) {
				//in bounds
				int piece = board[newY][newX];
				
				if (piece != NONE && piece % enemyPiece == 0) {
					int pieceType = this.getPieceInfo(piece)[0];
					
					if (pieceType == KING) {
						return true;
					}
				}
			}
		}
		
		Square[] knightOffsets = {new Square(-2, -1), new Square(-1, -2), new Square(1, -2), new Square(2, -1), new Square(-2, 1), new Square(-1, 2), new Square(1, 2), new Square(2, 1)};
		
		for (int i = 0; i < knightOffsets.length; i++) {
			int newX = kingSquare.getX() + knightOffsets[i].getX();
			int newY = kingSquare.getY() + knightOffsets[i].getY();
			
			if (0 <= newX && newX <= 7 && 0 <= newY && newY <= 7) {
				//in bounds
				int piece = board[newY][newX];
				
				if (piece != NONE && piece % enemyPiece == 0) {
					int pieceType = this.getPieceInfo(piece)[0];
					
					if (pieceType == KNIGHT) {
						return true;
					}
				}	
			}
		}
		
		return false;
	}
	
	public void genPawnMoves(int[][] board, Vector<Move> moves, Square from, int[] pieceInfo) {
		int piece = pieceInfo[0] * pieceInfo[1];
		int color = pieceInfo[1];
		
		int stepForwardDouble = 0;
		int firstRank = 0;
		int stepForward = 0;
		int promotionRank = 0;
		Square[] loudTo = new Square[2];
		
		if (color == WHITE) {
			stepForwardDouble = -2;
			firstRank = 6;
			stepForward = -1;
			promotionRank = 0;
			
			loudTo[0] = new Square(from.getX() - 1, from.getY() - 1);
			loudTo[1] = new Square(from.getX() + 1, from.getY() - 1);
		} else {
			stepForwardDouble = 2;
			firstRank = 1;
			stepForward = 1;
			promotionRank = 8;
			
			loudTo[0] = new Square(from.getX() - 1, from.getY() + 1);
			loudTo[1] = new Square(from.getX() + 1, from.getY() + 1);
		}
		
		//quiet moves
		Square quietTo = new Square(from.getX(), from.getY() + stepForward);
		if (board[quietTo.getY()][quietTo.getX()] == NONE) {
			if (quietTo.getY() == promotionRank) {
				//promotion!					

				for (int i = KNIGHT; i < KING; i++) {
					Move promotion = new Move(from, quietTo, piece, false, i);

					Board boardClone = this.upperClone(board);
					this.playUnchecked(boardClone.getBoard(), promotion);
					
					if (!this.isChecked(boardClone.getBoard(), color)) {
						moves.add(promotion);
					}
				}
			} else {
				int moveAmount = 1;
				
				if (from.getY() == firstRank) {
					moveAmount = 2;
				}
				
				for (int i = 0; i < moveAmount; i++) {
					int newY = 0;
					
					if (i == 0) {
						newY = from.getY() + stepForward;
					} else {
						newY = from.getY() + stepForwardDouble;
					}
					
					Square to = new Square(from.getX(), newY);
					
					if (board[to.getY()][to.getX()] == NONE) {
						Move move = new Move(from, to, piece, false);
						
						Board boardClone = this.upperClone(board);
						this.playUnchecked(boardClone.getBoard(), move);
						
						if (!this.isChecked(boardClone.getBoard(), color)) {
							moves.add(move);
						}
					} else {
						break;
					}
				}
			}
		}
		
		//loud moves
		for (int i = 0; i < loudTo.length; i++) {
			Square loudSquare = loudTo[i];
			
			if (0 <= loudSquare.getX() && loudSquare.getX() <= 7 && 0 <= loudSquare.getY() && loudSquare.getY() <= 7) {
				//within bounds
				
				int toPiece = board[loudSquare.getY()][loudSquare.getX()];
				
				if (toPiece != NONE) {
					//there is a piece
					if (loudSquare.getY() == promotionRank) {
						if (toPiece % this.getOppositeColor(color) == 0) {
							//it isn't mine!
							for (int o = KNIGHT; o < KING; o++) {
								Move promotion = new Move(from, loudSquare, piece, true, o);

								Board boardClone = this.upperClone(board);
								this.playUnchecked(boardClone.getBoard(), promotion);
								
								if (!this.isChecked(boardClone.getBoard(), color)) {
									moves.add(promotion);
								}
							}
						}
					} else {
						if (toPiece % this.getOppositeColor(color) == 0) {
							//it isn't mine!
							
							Move move = new Move(from, loudSquare, piece, true);
							
							Board boardClone = this.upperClone(board);
							
							this.playUnchecked(boardClone.getBoard(), move);
							
							if (!this.isChecked(boardClone.getBoard(), color)) {
								moves.add(move);
							}
						}	
					}
				}
			}
		}
	}
	
	public void genKnightMoves(int[][] board, Vector<Move> moves, Square from, int[] pieceInfo) {
		Square[] knightOffsets = {new Square(-2, -1), new Square(-1, -2), new Square(1, -2), new Square(2, -1), new Square(-2, 1), new Square(-1, 2), new Square(1, 2), new Square(2, 1)};
		
		int piece = pieceInfo[0] * pieceInfo[1];
		int ourColor = pieceInfo[1];
		
		for (int i = 0; i < knightOffsets.length; i++) {
			int newX = from.getX() + knightOffsets[i].getX();
			int newY = from.getY() + knightOffsets[i].getY();
			
			if (0 <= newX && newX <= 7 && 0 <= newY && newY <= 7) {
				//in bounds
				int targetPiece = board[newY][newX];
				int[] targetInfo = this.getPieceInfo(targetPiece);
				
				Square to = new Square(newX, newY);
				
				if (targetPiece == NONE) {
					//quiet move
					Move move = new Move(from, to, piece, false);
					
					Board boardClone = this.upperClone(board);
					this.playUnchecked(boardClone.getBoard(), move);
					
					if (!this.isChecked(boardClone.getBoard(), ourColor)) {
						moves.add(move);
					}
				} else {
					//loud move
					if (targetInfo[1] != ourColor) {
						//we aren't stepping on any of our own pieces
						Move move = new Move(from, to, piece, true);
						
						Board boardClone = this.upperClone(board);
						this.playUnchecked(boardClone.getBoard(), move);
						
						if (!this.isChecked(boardClone.getBoard(), ourColor)) {
							moves.add(move);
						}
					}
				}
			}
		}
	}
	
	public void genBishopMoves(int[][] board, Vector<Move> moves, Square from, int[] pieceInfo) {
		int piece = pieceInfo[0] * pieceInfo[1];
		int ourColor = pieceInfo[1];
		
		//north-east
		int neInc = 1;
		while (from.getX() + neInc <= 7 && from.getY() - neInc >= 0) {			
			int incX = from.getX() + neInc;
			int incY = from.getY() - neInc;
			int targetPiece = board[incY][incX];
			int[] targetInfo = this.getPieceInfo(targetPiece);
			
			Square to = new Square(incX, incY);
			
			if (targetPiece == NONE) {
				//quiet move
				Move move = new Move(from, to, piece, false);
				
				Board boardClone = this.upperClone(board);
				this.playUnchecked(boardClone.getBoard(), move);
				
				if (!this.isChecked(boardClone.getBoard(), ourColor)) {
					moves.add(move);
				}
			} else {
				//loud move
				if (targetInfo[1] != ourColor) {
					Move move = new Move(from, to, piece, true);
				
					Board boardclone = this.upperClone(board);
					this.playUnchecked(boardclone.getBoard(), move);
					
					if (!this.isChecked(boardclone.getBoard(), ourColor)) {
						moves.add(move);
					}
				}
				
				//stop looking since we have either ran into our own piece or taken a piece
				break;
			}
			
			neInc++;
		}
		
		//north-west
		int nwInc = 1;
		while (from.getX() - nwInc >= 0 && from.getY() - nwInc >= 0) {
			int incX = from.getX() - nwInc;
			int incY = from.getY() - nwInc;
			int targetPiece = board[incY][incX];
			int[] targetPieceInfo = this.getPieceInfo(targetPiece);
			
			Square to = new Square(incX, incY);
			
			if (targetPiece == NONE) {
				//quiet move
				Move move = new Move(from, to, piece, false);
				
				Board boardClone = this.upperClone(board);
				this.playUnchecked(boardClone.getBoard(), move);
				
				if (!this.isChecked(boardClone.getBoard(), ourColor)) {
					moves.add(move);
				}
			} else {
				//loud move
				if (targetPieceInfo[1] != ourColor) {
					Move move = new Move(from, to, piece, true);

					Board boardClone = this.upperClone(board);
					this.playUnchecked(boardClone.getBoard(), move);
					
					if (!this.isChecked(boardClone.getBoard(), ourColor)) {
						moves.add(move);
					}
				}

				//stop looking since we have either ran into our own piece or taken a piece
				break;
			}

			
			nwInc++;
		}
		
		//south-east
		int seInc = 1;
		while (from.getX() + seInc <= 7 && from.getY() + seInc <= 7) {
			int incX = from.getX() + seInc;
			int incY = from.getY() + seInc;
			int targetPiece = board[incY][incX];
			int[] targetPieceInfo = this.getPieceInfo(targetPiece);
			
			Square to = new Square(incX, incY);
			
			if (targetPiece == NONE) {
				//quiet move
				Move move = new Move(from, to, piece, false);
				
				Board boardClone = this.upperClone(board);
				this.playUnchecked(boardClone.getBoard(), move);
				
				if (!this.isChecked(boardClone.getBoard(), ourColor)) {
					moves.add(move);
				}
			} else {
				//loud move
				if (targetPieceInfo[1] != ourColor) {
					Move move = new Move(from, to, piece, true);
					
					Board boardClone = this.upperClone(board);
					this.playUnchecked(boardClone.getBoard(), move);
					
					if (!this.isChecked(boardClone.getBoard(), ourColor)) {
						moves.add(move);
					}
				}

				//stop looking since we have either ran into our own piece or taken a piece
				break;
			}
			
			seInc++;
		}
		
		//south-west
		int swInc = 1;
		while (from.getX() - swInc >= 0 && from.getY() + swInc <= 7) {
			int incX = from.getX() - swInc;
			int incY = from.getY() + swInc;
			int targetPiece = board[incY][incX];
			int[] targetPieceInfo = this.getPieceInfo(piece);
			
			Square to = new Square(incX, incY);
			
			if (targetPiece == NONE) {
				//quiet move
				Move move = new Move(from, to, piece, false);
				
				Board boardClone = this.upperClone(board);
				this.playUnchecked(boardClone.getBoard(), move);
				
				if (!this.isChecked(boardClone.getBoard(), ourColor)) {
					moves.add(move);
				}
			} else {
				//loud move
				if (targetPieceInfo[1] != ourColor) {
					Move move = new Move(from, to, piece, true);
					
					Board boardClone = this.upperClone(board);
					this.playUnchecked(boardClone.getBoard(), move);
					
					if (!this.isChecked(boardClone.getBoard(), ourColor)) {
						moves.add(move);
					}
				}
				
				//stop looking since we have either ran into our own piece or taken a piece
				break;
			}
			
			swInc++;
		}
	}
	
	public void genRookMoves(int[][] board, Vector<Move> moves, Square from, int[] pieceInfo) {
		int piece = pieceInfo[0] * pieceInfo[1];
		int ourColor = pieceInfo[1];
		
		//north
		for (int y = from.getY(); y > -1; y--) {
			int targetPiece = board[y][from.getX()];
			int[] targetPieceInfo = this.getPieceInfo(targetPiece);
			
			Square to = new Square(from.getX(), y);
			
			if (y != from.getY()) {
				if (targetPiece == NONE) {
					//quiet move
					Move move = new Move(from, to, piece, false);
					
					Board boardClone = this.upperClone(board);
					this.playUnchecked(boardClone.getBoard(), move);
					
					if (!this.isChecked(boardClone.getBoard(), ourColor)) {
						moves.add(move);
					}
				} else {
					//loud move
					if (targetPieceInfo[1] != ourColor) {
						Move move = new Move(from, to, piece, true);
						
						Board boardClone = this.upperClone(board);
						this.playUnchecked(boardClone.getBoard(), move);
						
						if (!this.isChecked(boardClone.getBoard(), ourColor)) {
							moves.add(move);
						}
					}
					
					//stop looking since we have either ran into our own piece or taken a piece
					break;
				}
			}
		}
		
		//south
		for (int y = from.getY(); y < 8; y++) {
			int targetPiece = board[y][from.getX()];
			int[] targetPieceInfo = this.getPieceInfo(targetPiece);
			
			Square to = new Square(from.getX(), y);
			
			if (y != from.getY()) {
				if (targetPiece == NONE) {
					//quiet move
					Move move = new Move(from, to, piece, false);
					
					Board boardClone = this.upperClone(board);
					this.playUnchecked(boardClone.getBoard(), move);
					
					if (!this.isChecked(boardClone.getBoard(), ourColor)) {
						moves.add(move);
					}
				} else {
					//loud move
					if (targetPieceInfo[1] != ourColor) {
						Move move = new Move(from, to, piece, true);
						
						Board boardClone = this.upperClone(board);
						this.playUnchecked(boardClone.getBoard(), move);
						
						if (!this.isChecked(boardClone.getBoard(), ourColor)) {
							moves.add(move);
						}
					}
					
					//stop looking since we have either ran into our own piece or taken a piece
					break;
				}	
			}
		}
		
		//west
		//straight sliders: queen, rook
		for (int x = from.getX(); x > -1; x--) {
			int targetPiece = board[from.getY()][x];
			int[] targetPieceInfo = this.getPieceInfo(targetPiece);
			
			Square to = new Square(x, from.getY());
			
			if (x != from.getX()) {
				if (targetPiece == NONE) {
					//quiet move
					Move move = new Move(from, to, piece, false);
					
					Board boardClone = this.upperClone(board);
					this.playUnchecked(boardClone.getBoard(), move);
					
					if (!this.isChecked(boardClone.getBoard(), ourColor)) {
						moves.add(move);
					}
				} else {
					//loud move
					if (targetPieceInfo[1] != ourColor) {
						Move move = new Move(from, to, piece, true);
						
						Board boardClone = this.upperClone(board);
						this.playUnchecked(boardClone.getBoard(), move);
						
						if (!this.isChecked(boardClone.getBoard(), ourColor)) {
							moves.add(move);
						}
					}
					
					//stop looking since we have either ran into our own piece or taken a piece
					break;
				}
			}
		}
		
		//east
		for (int x = from.getX(); x < 8; x++) {
			int targetPiece = board[from.getY()][x];
			int[] targetPieceInfo = this.getPieceInfo(targetPiece);
			
			Square to = new Square(x, from.getY());
			
			if (x != from.getX()) {
				if (targetPieceInfo[0] == NONE) {
					//quiet move
					Move move = new Move(from, to, piece, false);
					
					Board boardClone = this.upperClone(board);
					this.playUnchecked(boardClone.getBoard(), move);
					
					if (!this.isChecked(boardClone.getBoard(), ourColor)) {
						moves.add(move);
					}
				} else {
					//loud move
					if (targetPieceInfo[1] != ourColor) {
						Move move = new Move(from, to, piece, true);
						
						Board boardClone = this.upperClone(board);
						this.playUnchecked(boardClone.getBoard(), move);
						
						if (!this.isChecked(boardClone.getBoard(), ourColor)) {
							moves.add(move);
						}
					}
					
					//stop looking since we have either ran into our own piece or taken a piece
					break;
				}
			}
		}
	}
	
	public void genQueenMoves(int[][] board, Vector<Move> moves, Square from, int[] pieceInfo) {
		this.genBishopMoves(board, moves, from, pieceInfo);
		this.genRookMoves(board, moves, from, pieceInfo);
	}
	
	public void genKingMoves(int[][] board, Vector<Move> moves, Square from, int[] pieceInfo) {
		Square[] kingOffsets = {new Square(-1, 0), new Square(-1, -1), new Square(0, -1), new Square(1, -1), new Square(1, 0), new Square(1, 1), new Square(0, 1), new Square(-1, 1)};
		int piece = pieceInfo[0] * pieceInfo[1];
		int ourColor = pieceInfo[1];
		
		for (int i = 0; i < kingOffsets.length; i++) {
			int newX = from.getX() + kingOffsets[i].getX();
			int newY = from.getY() + kingOffsets[i].getY();
			
			if (0 <= newX && newX <= 7 && 0 <= newY && newY <= 7) {
				//in bounds
				int targetPiece = board[newY][newX];
				int[] targetPieceInfo = this.getPieceInfo(targetPiece);
				
				Square to = new Square(newX, newY);
				
				if (targetPieceInfo[1] != ourColor || targetPiece == NONE) {
					Move move = new Move(from, to, piece, targetPiece != NONE);
					
					Board newBoard = this.upperClone(board);
					this.playUnchecked(newBoard.getBoard(), move);
					
					if (!this.isChecked(newBoard.getBoard(), ourColor)) {
						moves.add(move);
					}
				}
			}
		}
	}
	
	public String getPawnAscii(boolean white) {
		if (white) {
			return "P";
		} else {
			return "p";
		}
	}
	
	public String getKnightAscii(boolean white) {
		if (white) {
			return "N";
		} else {
			return "n";
		}
	}
	
	public String getBishopAscii(boolean white) {
		if (white) {
			return "B";
		} else {
			return "b";
		}
	}
	
	public String getRookAscii(boolean white) {
		if (white) {
			return "R";
		} else {
			return "r";
		}
	}
	
	public String getQueenAscii(boolean white) {
		if (white) {
			return "Q";
		} else {
			return "q";
		}
	}
	
	public String getKingAscii(boolean white) {
		if (white) {
			return "K";
		} else {
			return "k";
		}
	}
	
	public String getNoneAscii() {
		return "*";
	}
}