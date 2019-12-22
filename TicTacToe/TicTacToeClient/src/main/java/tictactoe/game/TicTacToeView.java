package tictactoe.game;

import tictactoe.game.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Vector;

public class TicTacToeView {
	public void show (TicTacToeModel obj){
		System.out.print("  ");
		for(int i = 0; i < 10; i++){
			System.out.print("" + i);
		}
		System.out.println("");
		for(int i = 0; i < 10; i++){
			System.out.print(i + "|"); 	
			for(int j = 0; j < 10; j++){		 
				if(obj.getField(i, j) != 0){
					System.out.print("" + obj.getField(i, j));
				} else {
					System.out.print("_");
				}
			}	
			System.out.println("");
		}
	}

	public Vector<String> startMenu() {
		System.out.println();
		System.out.println("Welcome to TicTacToe!");
		System.out.println();
		System.out.println("Input necessary values to do something.");
		System.out.println();

		int input = 0;
		Vector<String> answer = new Vector<String>();

		for (int i = 0; i < 3; i++) {
			answer.add("");
		}
		
		answer.set(0, new String("heh"));

		Scanner scanner = new Scanner(System.in);

		while (input == 0) {
			System.out.println("1 - Connect;");
			System.out.println("2 - Change connection settings;");
			System.out.print("3 - Exit: ");

			input = scanner.nextInt();

			switch(input) {
				case 1:

				break;

				case 2:

				System.out.println();
				System.out.println("1 - Change port;");
				System.out.print("2 - Change server IP: ");

				input = scanner.nextInt();

				if (input == 1) {
				    answer.set(1, scanner.next());
					input = 0;
				}
                else if (input == 2) {
                  	input = 0; 
                  	answer.set(2, scanner.next());					 
                }
                else {  
                    System.out.println("Wrong input. Try again.");
				}

				System.out.println();

				break;

				case 3:

				answer.set(0, "1");
				break;

				default:

				System.out.println("Wrong input. Try again.");
				System.out.println();

				input = 0;
				break;
			}
		}

		return answer;
	}

	public int startGameMenu(int ID) {
		Scanner scanner = new Scanner(System.in);
		System.out.println();
		System.out.println("Your ID: " + ID);
		System.out.println();

		int answer = 0, input = 0;

		while (input == 0) {
			System.out.println("1 - Create new board;");
			System.out.println("2 - Join to existing board;");
			System.out.println("3 - Watch someone else's game;");
			System.out.print("4 - Exit: ");

			input = scanner.nextInt();

			switch(input) {
				case 1:

				answer = 1;
				break;

				case 2:

				answer = 2;
				break;

				case 3:

				answer = 3;
				break;

				case 4:

				answer = -1;
				break;

				default:

				System.out.println("Wrong input. Try again.");
				System.out.println();

				input = 0;
				break;
			}
		}
		return answer;
	}

	public Vector<Integer> makeMove(TicTacToeModel obj) {
		Vector<Integer> answer = new Vector<Integer>();
		Scanner scanner = new Scanner(System.in);

		int input = -1;
		System.out.println("Input coordinates to move.");
		System.out.println();
		while (input == -1) {
			System.out.print("Input x coordinate: ");
			input = scanner.nextInt();

			if (input < 0 || input >= 10) {
				System.out.println("Wrong x coordinate: x coordinate must be integer from 0 to 9. Try again.");
				System.out.println();
				input = -1;
				continue;
			}
			answer.add(input);

			System.out.print("Input y coordinate: ");
			input = scanner.nextInt();

			if (input < 0 || input >= 10) {
				System.out.println("Wrong y coordinate: y coordinate must be integer from 0 to 9. Try again.");
				System.out.println();
				input = -1;
				continue;
			}
			answer.add(input);

			if (obj.checkEmpty(answer.get(0), answer.get(1)) != 1) {
				System.out.println("The field you want is busy");
				System.out.println();
				answer.clear();
				input = -1;
				continue;
			}
		}
		return answer;
	} 

	public void printMessage(String message) {
		System.out.print(message);
	}

	public void printlnMessage(String message) {
		System.out.println(message);
	}

	public void println() {
		System.out.println();
	}
}
