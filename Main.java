import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean playing = true;
		
		Board board = new Board();
		
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
			System.out.println(board.getMoves());
			board.printBoard();
			
			String input = scanner.nextLine();
			
			if (!input.equals("stop")) {
				board.playMove(input);
			} else {
				playing = false;
			}
		}
	}
}