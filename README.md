# TicTacToe

# Program launch

Hello
This program is divided into 2 parts: client and server. For the program to work fully, you must first run the TicTacToeServer-1.0 file in the command line using the java -jar TicTacToeServer-1.0.jar command. After that, on a separate command line, run the TicTacToeClient-1.0 file using the java -jar TicTacToeClient-1.0.jar command.

# User manual

After starting the program, a menu will be displayed on the screen, in which you can choose: connect, change ip or port or exit.
After choosing a connection, you will be asked to: create a game, connect to an existing game or watch someone else's game.
When choosing to create a game, a field for the game will be created and you will expect an opponent to connect.
When choosing to connect to an existing game, you will connect to a player who is waiting for an opponent. If there are no existing games, the corresponding message will be displayed on the screen and you will be taken to the main menu.
When choosing to watch someone else’s game, you will be shown the ID of the players who are currently playing. You have a choice: if you want to watch someone’s game or not. If you want to post-mortem a player’s game from the list, you will need to enter his id. After that, the broadcast of the game begins. If there are no games, a corresponding message will be displayed on the screen and you will be taken to the main menu.

If a player who chose to connect to an existing game connects to a player who is waiting for an opponent, they will start the game.
The player who created the game will play for X, and the player who connected will play for O.
The first move for H. The player will need to enter the coordinates where he wants to put his piece. And so, in turn, the players will fill the playing field.
The game ends if one of the players draws a line of 5 identical figures or the entire field is filled.
After the end of the game, players will be moved to the main menu
