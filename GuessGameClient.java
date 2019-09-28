/*
 * CO2017 Coursework 3
 * By Liam Wilcox (LW306)
 */

package CO2017.exercise3.lw306.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GuessGameClient {

	public GuessGameClient() {}

	public static void main(String[] args) throws IOException {

		// First command line argument (Hostname)
		String hostname = args[0];
		// Second command line argument (Port number)
		int port = Integer.parseInt(args[1]);

		Socket server = new Socket(hostname, port);

		System.out.println("Connected to " + server.getInetAddress());

		PrintWriter writer = null;

		ClientState cs = new ClientState(writer);
		cs.setSocket(server);

		Thread thread = new Thread(cs);
		thread.start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream(), "UTF-8"));
		String message;
		while (!cs.isFinished()) {
			message = reader.readLine();
			System.out.println(message);
			if (message.contains("WIN") || message.contains("Time is up!")) {
				System.exit(0);
			}
			System.out.print("Enter a guess: ");
		}
	}
}