/*
 * CO2017 Coursework 3
 * By Liam Wilcox (LW306)
 */

package CO2017.exercise3.lw306.server;

import java.io.IOException;
import java.net.SocketException;
import java.util.Random;

public class GameState implements Runnable {

	static final int MINVAL = 1;
	static final Random RANDGEN = new Random();
	int target;
	int numOfGuesses;
	boolean gameOver;
	String state = "NOT_STARTED";
	int max;
	int guess;
	long timeLimit;
	GuessGameServerHandler handler;
	long startTime;

	public GameState(int mv, long tl, GuessGameServerHandler ggsh) {
		max = mv;
		target = RANDGEN.nextInt(max) + 1;
		numOfGuesses = 0;
		gameOver = false;
		timeLimit = tl;
		handler = ggsh;
		startTime = System.currentTimeMillis();
	}

	int getTarget() {
		return target;
	}

	int getGuesses() {
		return numOfGuesses;
	}

	boolean finished() {
		return gameOver;
	}

	long getTimeRemaining() {
		return ((startTime+timeLimit) - System.currentTimeMillis())/1000;
	}

	void guess(int guess) {
		if (guess == target) {
			state = "WON";
			gameOver = true;
			numOfGuesses++;
			this.guess = guess;
		}
		else if (guess > max || guess < 1) {
			state = "ERROR";
		}
		else if (guess > target && guess <= max) {
			state = "HIGH";
			this.guess = guess;
			numOfGuesses++;
		}
		else {
			state = "LOW";
			this.guess = guess;
			numOfGuesses++;
		}
	}

	@Override
	public String toString() {
		switch (state) {
		case "NOT_STARTED":
			return "";
		case "WON":
			try {
				handler.shutdownInput();
			} catch (IOException e) {e.printStackTrace();}
			return "Target was " + target+", you WIN";
		case "HIGH":
			return "Turn "+numOfGuesses+": "+guess+ " was HIGH, "+getTimeRemaining()+ "s remaining.";
		case "LOW":
			return "Turn "+numOfGuesses+": "+guess+ " was LOW, "+getTimeRemaining()+ "s remaining.";
		case "ERROR":
			return "ERROR: Turn "+numOfGuesses+": "+getTimeRemaining()+ "s remaining.";
		default:
			return "Bug";
		}
	}

	@Override
	public void run() {
		while (true) {
			if (getTimeRemaining() == 0) {
				try {
					gameOver = true;
					handler.shutdownInput();
					handler.send("Time is up!");
					break;
				} catch (SocketException e) {break;
				} catch (IOException e) {e.printStackTrace();}
			}
		}
	}
}