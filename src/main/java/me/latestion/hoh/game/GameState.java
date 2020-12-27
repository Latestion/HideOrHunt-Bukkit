package me.latestion.hoh.game;

public enum GameState{
		
	ON, OFF, PREPARE;
	
	private static GameState currentstate;
	
	public static void setGamestate(GameState state){
		currentstate = state;
	}
	
	public static GameState getCurrentGamestate(){
		return currentstate;
	}

}
