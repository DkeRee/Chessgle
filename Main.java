import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean playing = true;
		
		Board board = new Board();
		board.parseFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
		
		//PERFT
		
		Perft perft = new Perft();
		perft.run(board, 0);
		System.out.println("Nodes: " + perft.getNodes());
		System.out.println("Captures: " + perft.getCaptures());
		System.out.println("Rookings: " + perft.getRookings());
		System.out.println(perft.getChecks());
		
		//perft.printPerftLog();
		
		/*
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
		*/
	}
}