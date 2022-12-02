import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean playing = true;
		
		int depth = 4;
		Board board = new Board();
		
		System.out.println("Welcome to Chessgle!");
		System.out.println("Searching at a depth of " + depth);
		
		Engine engine = new Engine(depth);
		
		//PERFT
		/*
		Perft perft = new Perft(5);
		perft.run(board, 0, null);
		System.out.println("Nodes: " + perft.getNodes());
		System.out.println("Captures: " + perft.getCaptures());
		System.out.println("Rookings: " + perft.getRookings());
		System.out.println(perft.getChecks());
		
		perft.printPerftLog();
		*/
		
		while (playing) {
			board.printBoard();
			
			String input = scanner.nextLine();
			
			if (!input.equals("stop")) {
				board.playMove(input);
				playing = board.printState();
				
				System.out.println("Searching...");
				board.playMoveSelf(engine.searchRoot(board));
				playing = board.printState();
				
			} else {
				playing = false;
			}
		}
	}
}