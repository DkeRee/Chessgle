import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean playing = true;
		
		Board board = new Board();
		System.out.println(board.getMoves(10));
		
		while (playing) {
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