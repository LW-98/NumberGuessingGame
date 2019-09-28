/*
 * CO2017 Coursework 3
 * By Liam Wilcox (LW306)
 */

package CO2017.exercise3.lw306.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GuessGameServer {
	private static long timeLimit;

	GuessGameServer() {}
	
	public static long getTimeLimit() {
		return timeLimit;
	}

	public static void main(String[] args) throws IOException {

		// First command line argument (Port number)
		int port = Integer.parseInt(args[0]);
		// Second command line argument (maximum number for guessing)
		int max = Integer.parseInt(args[1]);
		// Third command line argument (Time limit of game)
		timeLimit = Integer.parseInt(args[2]);
		
		System.out.println(timeLimit);

		ServerSocket server = new ServerSocket(port);

		while (true) {
			System.out.println("Waiting for client...");
			Socket client = server.accept();

			System.out.println("Client from "+client.getInetAddress()+" connected.");

			GuessGameServerHandler h = new GuessGameServerHandler(max,timeLimit,client);
			Thread thread = new Thread(h);
			thread.start(); 
		}
	}
}
