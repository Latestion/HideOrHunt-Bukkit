package me.latestion.hoh.game;

public enum GameState {

	ON, OFF, PREPARE;

	private static GameState currentState = OFF;

	public static void setGameState(GameState state) {
		currentState = state;
	}

	public static GameState getCurrentGameState() {
		return currentState;
	}

}
