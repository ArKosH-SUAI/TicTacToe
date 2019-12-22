package tictactoe.client;

import tictactoe.game.*;

import java.net.Socket;
import java.net.InetSocketAddress;

import java.util.Scanner;
import java.util.Vector;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Client {

	private int ID = 0;
	private int opponentID = -100;

	private boolean watching = false;
	private boolean move = false;

	private TicTacToeModel model = new TicTacToeModel();
	private TicTacToeView view = new TicTacToeView();

	private int portNumber = 10000;
	private String IP = "127.0.0.1";

    private Socket clientSocket;	

	public Client() throws IOException {
		this.startMenuLaunch();
	}

	private void startMenuLaunch() throws IOException {
        Vector<String> answer = this.view.startMenu();

		if (answer.get(0).equals("1"))
			return;

		if (!answer.get(1).isEmpty())
			portNumber = Integer.valueOf(answer.get(1));

		if (!answer.get(2).isEmpty())
			IP = answer.get(2);
        
        clientSocket = new Socket(IP, portNumber);
        ID = (new DataInputStream(clientSocket.getInputStream())).readInt();

        this.startGameMenuLaunch();
	}

	private void startGameMenuLaunch() throws IOException {
		DataInputStream in = new DataInputStream(this.clientSocket.getInputStream());
		DataOutputStream out = new DataOutputStream(this.clientSocket.getOutputStream());
		watching = false;
        int answer = this.view.startGameMenu(ID);
        if (answer == -1) {
        	out.writeInt(-200);
        	return;
        }

        if (answer == 1) {
        	move = true;        	
        	out.writeInt(-100);
        	out.writeBoolean(watching);
        	this.view.println();
        	System.out.println("Waiting for other player...");
        	this.view.println();
        	this.opponentID = in.readInt();
        	out.writeInt(this.opponentID);
        	this.startGame();
        } else if (answer == 2) {
        	move = false;
        	out.writeInt(-1);
        	out.writeBoolean(watching);
        	this.opponentID = in.readInt();
        	if (this.opponentID == -50) {
        		model = new TicTacToeModel();
        		this.startGameMenuLaunch();
        		return;
        	}
        	this.startGame();
        } else if (answer == 3){
        	watching = true;
        	int input = -1;
        	out.writeInt(100);
        	out.writeBoolean(watching);
        	int size = in.readInt();
        	if (size == 0) {
        		System.out.println();
        		System.out.println("No one is playing at the moment.");
        		System.out.println();
        		this.startGameMenuLaunch();
        	}
        	int idd = 0;
        	System.out.println();
        	System.out.println("Currently players with id data");
        	System.out.println();
        	for (int i = 0; i < size; i++) {
        		idd = in.readInt();
        		System.out.println(idd);
        	}
        	System.out.println();
        	System.out.println("Do you want to watch someones game?");
        	System.out.println();
        	System.out.println();
        	System.out.println("1 - Yes");
        	System.out.println("2 - No");
        	System.out.println();
        	Scanner scanner = new Scanner(System.in);
        	while (input == -1) {
	        	input = scanner.nextInt();
	        	switch(input) {
	        		case 1:
		        		System.out.println();
	        			System.out.println("Which user game do you want to watch? Enter his id:");
	        			System.out.println();
	        			input = scanner.nextInt();
	        			System.out.println();
	        			System.out.println("Game loading ...");
	        			System.out.println();
	        			out.writeInt(input);
					break;

					case 2:
						this.startGameMenuLaunch();
					break;

					default:
						System.out.println("Wrong input. Try again.");
						System.out.println();
						input = -1;
					break;
	        	}
	        }
	        this.watchGame();
        }
	}

	private void watchGame() throws IOException {
		DataInputStream in = new DataInputStream(clientSocket.getInputStream());
		DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
		boolean endOfGameFlag1 = false, endOfGameFlag2 = false;
		while (true) {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					int x = in.readInt(), y = in.readInt(), activeUser = in.readInt();
					if (x == -1 && y == -1 && activeUser == -1)
						continue;
					model.setActiveUser(activeUser);
					model.setUserCoors(y, x);			
					model.setStep();
				}
			}
			model.setActiveUser(1);
			endOfGameFlag1 = model.checkStop();
			model.setActiveUser(2);
			endOfGameFlag2 = model.checkStop();
			if (endOfGameFlag1)
				out.writeBoolean(endOfGameFlag1);
			if (endOfGameFlag2)
				out.writeBoolean(endOfGameFlag2);
			out.writeBoolean(endOfGameFlag1);
			this.view.println();
			this.view.show(model);
			this.view.println();
			if (endOfGameFlag1) {
				this.view.println();
				this.view.printlnMessage("Game over. The player who played for X won");
				this.view.println();
				break;
			} else if (endOfGameFlag2) {
				this.view.println();
				this.view.printlnMessage("Game over. The player who played for O won");
				this.view.println();
				break;
			} else {
				this.view.println();
				System.out.println("Waiting for a move ...");
				this.view.println();
			}
		}
		model = new TicTacToeModel();
		this.startGameMenuLaunch();
	}

	private void startGame() throws IOException {
		DataInputStream in = new DataInputStream(clientSocket.getInputStream());
		DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
		boolean endOfGameFlag = false;
		this.view.show(model);
		while (true) {	
			if(move) {
				this.view.println();
				this.view.printlnMessage("You turn");
				this.view.println();
				Vector<Integer> answer = view.makeMove(model);
				this.model.setUserCoors(answer.get(0), answer.get(1));			
				this.model.setStep();
				endOfGameFlag = this.model.checkStop();
				out.writeInt(answer.get(0));
				out.writeInt(answer.get(1));
				out.writeInt(model.getActiveUser());
				if (model.getActiveUser() == 1){
					model.setActiveUser(2);
				} else {
					model.setActiveUser(1);
				}
				move = false;
				if(endOfGameFlag){  
					move = true;
				} 
				out.writeBoolean(endOfGameFlag);
				this.view.println();
				this.view.show(model);
				this.view.println();
			}
			if(!move) {
				this.view.println();
				this.view.printlnMessage("Opponent turn");
				this.view.println();
				int x = in.readInt(), y = in.readInt();
				this.model.setUserCoors(x, y);
				this.model.setStep();
				endOfGameFlag = this.model.checkStop();
				if (model.getActiveUser() == 1){
					model.setActiveUser(2);
				} else {
					model.setActiveUser(1);
				}
				this.move = true;
				if(endOfGameFlag){  
					this.move = false;
				}
				out.writeBoolean(endOfGameFlag);
				
			}
			this.view.println();
			this.view.show(model);
			this.view.println();
			if (endOfGameFlag) {
				if (move) {
					this.view.println();
					this.view.printlnMessage("Congratulations! You won!:)");
					this.view.println();
				}
				else {
					this.view.println();
					this.view.printlnMessage("You lose :(");
					this.view.println();
				}
				break;
			}
		}
		model = new TicTacToeModel();
		this.startGameMenuLaunch();
	}
}