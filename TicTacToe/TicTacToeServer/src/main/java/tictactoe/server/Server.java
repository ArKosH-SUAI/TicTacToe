package tictactoe.server;

import tictactoe.game.*;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;

public class Server {
	private int idCounter = 0;
	private ServerSocket serverSocket;

	private HashMap<Integer, Socket> clients = new HashMap<Integer, Socket>();
	private HashMap<Integer, Integer> IDs = new HashMap<Integer, Integer>();
	private HashMap<Integer, TicTacToeModel> boards = new HashMap<Integer, TicTacToeModel>();
	private TicTacToeView view = new TicTacToeView();

	private DataOutputStream logFile = new DataOutputStream(new FileOutputStream("logfile.log"));

	public Server(int portNumber) throws IOException {
		serverSocket = new ServerSocket(portNumber);
	}

	public void connect() throws IOException {
		while (true) {
			Socket clientSocket = serverSocket.accept();
			logFile.writeChars((new Date()) + ": client with ID=" + idCounter + " is connected\n");
		    clients.put(idCounter, clientSocket);
		    (new DataOutputStream(clientSocket.getOutputStream())).writeInt(idCounter);

		    ClientThread clientThread = new ClientThread(clientSocket, idCounter);
		    idCounter++;
		    clientThread.start();
		}
	}

	class ClientThread extends Thread {
		private Socket clientSocket;
		private int ID = -100;
		private int opponentID = -100;

		private int watchingID = 0;
		private boolean watching = false;
		private boolean move = false;

		public ClientThread(Socket clientSocket, int ID) {
			this.clientSocket = clientSocket;
			this.ID = ID;
		}

		public void run() {
			try {
				while(true) {
					DataInputStream in = new DataInputStream(clientSocket.getInputStream());
					DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

					this.opponentID = in.readInt();
					this.watching = in.readBoolean();
					if (this.opponentID == -200) {
						synchronized (clients) {
							clients.remove(ID);
						}
						synchronized (logFile) {
							logFile.writeChars((new Date()) + ": client with ID=" + ID + " is unconnected\n");
						}
						return;
					}				

					if (this.opponentID == -100) {
						synchronized (IDs) {
							IDs.put(ID, -100);
							boards.put(ID, new TicTacToeModel());
						}
						synchronized (logFile) {
							logFile.writeChars((new Date()) + ": client with ID=" + ID + " have created a new board\n");
						}
						this.move = true;

						this.opponentID = in.readInt();
						synchronized(logFile) {
							logFile.writeChars((new Date()) + ": clients with ID=" + ID + "and ID=" + opponentID + " have started playing a new game\n");
						}
						synchronized (IDs) {
							IDs.remove(ID);
							IDs.put(ID, opponentID);
						}
					} else if (this.opponentID == -1) {
						synchronized (logFile) {
							logFile.writeChars((new Date()) + ": client with ID=" + ID + " is searhing a free board\n");
						}
						this.opponentID = -100;
						synchronized (IDs) {
							for (Map.Entry<Integer, Integer> it : IDs.entrySet()) {
								if (it.getValue() == -100) {
									this.opponentID = it.getKey();
									break;
								}
							}
						}
						if (this.opponentID == -100) {
							synchronized(logFile) {
								logFile.writeChars((new Date()) + ": no free boards found for client with ID=" + ID + "\n");
							}
							out.writeInt(-50);
							continue;
						}
						else {
							synchronized (IDs) {
								IDs.put(ID, opponentID);
								boards.put(ID, new TicTacToeModel());
							}
							out.writeInt(opponentID);
							(new DataOutputStream(clients.get(this.opponentID).getOutputStream())).writeInt(ID);
						}
					}

					if(watching && this.opponentID == 100) {
						synchronized(logFile) {
							logFile.writeChars((new Date()) + ": client with ID=" + ID + "wants to watch the game\n");
						}
						if (IDs.size() != 0) {	
							out.writeInt(IDs.size());
							for (Map.Entry<Integer, Integer> it : IDs.entrySet()) {
								out.writeInt(it.getKey());
							}
							this.watchingID = in.readInt();
							synchronized(logFile) {
								logFile.writeChars((new Date()) + ": client with ID=" + ID + "will watch the client’s game with  ID=" + watchingID + "\n");
							}
						} else {
							synchronized(logFile) {
								logFile.writeChars((new Date()) + ": No one is playing at the moment.\n");
							}
							out.writeInt(0);
						}
					}

					int countStep = 0;	
					boolean endOfGameFlag = false;
					boolean endOfWatchGameFlag = false;

					while (true) {
						if (move && !watching) {
							DataOutputStream opponentOutputStream = new DataOutputStream(clients.get(opponentID).getOutputStream());
							DataInputStream opponentInputStream = new DataInputStream(clients.get(opponentID).getInputStream());
							int x = in.readInt();
							int y = in.readInt();
							int activeUser = in.readInt();
							synchronized (boards) {		
								TicTacToeModel board = boards.get(ID);
								TicTacToeModel opponentBoard = boards.get(this.opponentID);
								board.setActiveUser(activeUser);
								opponentBoard.setActiveUser(activeUser);
								board.setUserCoors(x, y);			
								board.setStep();
								opponentBoard.setUserCoors(x, y);			
								opponentBoard.setStep();
								boards.put(ID, board);
								boards.put(this.opponentID, opponentBoard);
							}
							synchronized (logFile) {
								logFile.writeChars((new Date()) + ": client with ID=" + ID + " made a move on: x=" + x + "; y=" + y + "\n");
							}
							opponentOutputStream.writeInt(x);
							opponentOutputStream.writeInt(y);
							if (move == true) 
								move = false;
						} else if (!move && !watching){
							if (move == false) 
								move = true;
						} else if (watching) {
							synchronized (boards) {	
								int counter = 0;
								TicTacToeModel board = boards.get(watchingID);
								for (int i = 0; i < 10; i++) {
									for (int j = 0; j < 10; j++) {
										if (board.getField(i, j) == 'X' || board.getField(i, j) == 'O')
											counter++;
									}
								}
								if (countStep != counter) {
									countStep = counter;
									for (int i = 0; i < 10; i++) {
										for (int j = 0; j < 10; j++) {
											if (board.getField(i, j) == 'X'){
												out.writeInt(i);
												out.writeInt(j);
												out.writeInt(1);
											} else if (board.getField(i, j) == 'O') {
												out.writeInt(i);
												out.writeInt(j);
												out.writeInt(2);
											} else {
												out.writeInt(-1);
												out.writeInt(-1);
												out.writeInt(-1);
											}
										}
									}
									endOfWatchGameFlag = in.readBoolean();
								} 
							}
						}
						if (!watching) {	
							endOfGameFlag = in.readBoolean();
						}
						if (endOfGameFlag) {
							synchronized (logFile) {
								logFile.writeChars((new Date()) + ": ID=" + ID + ": game is finished\n");
							}
							watching = false;
							synchronized (IDs) {
								this.move = false;
								IDs.remove(ID);
								break;
							}
						}
						if (endOfWatchGameFlag) {
							this.move = false;
							watching = false;
						}
					}	
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}