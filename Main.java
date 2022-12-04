import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean playing = true;
		
		Board board = new Board();
		
		System.out.println("Welcome to Chessgle!");
		
		Engine engine = new Engine(4);
		
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
				boolean response = board.playMove(input);
				
				if (response) {
					playing = board.printState();

					//poor attempt at smart searching
					int size = board.getMoves().size();
					if (size <= 10) {
						engine = new Engine(4);
					} else if (size <= 33) {
						engine = new Engine(3);
					} else {
						engine = new Engine(2);
					}
					
					System.out.println("Searching...");
					board.playMoveSelf(engine.searchRoot(board));
					playing = board.printState();	
				}
			} else {
				playing = false;
			}
		}
	}
}