package com.segdx.game.entity;

public class GameOver {
	//GAMEOVER TYPES
	public static final int OUT_OF_FUEL = 0;
	public static final int OUT_OF_FOOD = 1;
	public static final int OUT_OF_HP = 2;
	public static final int JOINED_THE_SBA = 3;
	public static final int GOT_DRAFTED = 4;
	public static final int SOLD_FATHERS_SHIP = 5;
	
	
	public static GameOver currentGameOver;
	
	public static GameOver getGameOver(){
		return currentGameOver;
	}
	
	public static void setCurrentGameOver(GameOver go){
		currentGameOver = go;
	}
	
	private int type;
	private Player player;
	private int difficulty;
	private int size;
	
	public GameOver(int type,Player player,int difficulty,int size){
		this.type = type;
	}
	public int getType(){
		return this.type;
	}
	public Player getPlayer(){
		return player;
	}
	public int getDifficulty(){
		return difficulty;
	}
	public int getSize(){
		return size;
	}

}
