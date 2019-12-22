package tictactoe.game;

import tictactoe.game.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TicTacToeModel {
	
	private final int longStep = 5;
	private int x = 10;
	private int y = 10;	
	
	private int userX = 0;
	private int userY = 0; 
	
	private char[][] field;
	
	private int activeUser = 0;

	public TicTacToeModel(){ 	
		field = new char[this.y][this.x];		
		activeUser 	= 1;
	}
	
	public void setUserCoors(int x, int y){
		userX = x;
		userY = y;
	}

	public int getUserXCoors(){
		return userX;
	}

	public int getUserYCoors(){
		return userY;
	}

	public char getField(int x, int y) {
		return field[x][y];
	}

	public int getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(int au) {
		activeUser = au;
	}
	
	public char getUserSymbol(){
		if(activeUser == 1){
			return 'X'; 
		} else if(activeUser == 2){			
			return 'O'; 
		}
		
		return ' ';
	}
	
	public boolean countSteps(int x, int y, String way, int count){ 
		switch(way){
			case "up": 
				if(checkEmpty(x,y-1) >= 0){  
					if(field[y-1][x] == getUserSymbol()){  						
						if(count+1 == longStep-1){ 
							return true;
						} else {
							return countSteps(x, y-1, way, count+1);
						} 
					}
				}   
				break;
			case "up-left": 
				if(checkEmpty(x-1,y-1) >= 0){  
					if(field[y-1][x-1] == getUserSymbol()){  						
						if(count+1 == longStep-1){ 
							return true;
						} else {
							return countSteps(x-1, y-1, way, count+1);
						} 
					}
				}   
				break;
			case "up-right": 
				if(checkEmpty(x+1,y-1) >= 0){  
					if(field[y-1][x+1] == getUserSymbol()){  						
						if(count+1 == longStep-1){ 
							return true;
						} else {
							return countSteps(x+1, y-1, way, count+1);
						} 
					}
				}   
				break;	
			case "down": 
				if(checkEmpty(x,y+1) >= 0){  
					if(field[y+1][x] == getUserSymbol()){  						
						if(count+1 == longStep-1){
							return true;
						} else {
							return countSteps(x, y+1, way, count+1);
						} 
					}
				}   
				break;	
			case "down-left":   
				if(checkEmpty(x-1,y+1) >= 0){  
					if(field[y+1][x-1] == getUserSymbol()){  						
						if(count+1 == longStep-1){
							return true;
						} else {
							return countSteps(x-1, y+1, way, count+1);
						} 
					}
				}   
				break;	 
			case "down-right":  
				if(checkEmpty(x+1,y+1) >= 0){  
					if(field[y+1][x+1] == getUserSymbol()){  						
						if(count+1 == longStep-1){ 
							return true;
						} else {
							return countSteps(x+1, y+1, way, count+1);
						} 
					}
				}   
				break;
			case "left": 
				if(checkEmpty(x-1,y) >= 0){  
					if(field[y][x-1] == getUserSymbol()){  						
						if(count+1 == longStep-1){ 
							return true;
						} else {
							return countSteps(x-1, y, way, count+1);
						} 
					}
				}   
				break;	
			case "right": 
				if(checkEmpty(x+1,y) >= 0){  
					if(field[y][x+1] == getUserSymbol()){  						
						if(count+1 == longStep-1) {
							return true;
						} else {
							return countSteps(x+1, y, way, count+1);
						} 
					}
				}   
				break;
		}
		return false;
	}
	
	public boolean checkStop(){
		
		String [] arr = new String[]{"up", "down", "left", "right", "up-left", "up-right", "down-left", "down-right"};
		
		for(int i = 0; i < arr.length; i++){
			if(countSteps(userX, userY, arr[i], 0)){
				return true;
			}
		} 
		
		return false;
	}
	
	public int checkEmpty(int x, int y){
		int status = 0;  
		if((x >= 0 && y >= 0) && (x < this.x && y < this.y)){
			if(field[y][x] == 0){
				status = 1;
			} 	
		} else {	 
			status = -1;
		}
		return status;
	}
	
	public void setStep(){ 
		if(activeUser == 1){
			field[userY][userX] = getUserSymbol();
		} else if(activeUser == 2){			
			field[userY][userX] = getUserSymbol();
		}
	}
}
