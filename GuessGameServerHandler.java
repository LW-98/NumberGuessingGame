/*
 * CO2017 Coursework 3
 * By Liam Wilcox (LW306)
 */

package CO2017.exercise3.lw306.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;

public class GuessGameServerHandler implements Runnable {

	GameState gs;
	BufferedReader reader;
	PrintWriter writer;
	static char idAvailable = 'A';
	char id;
	int max;
	long tl;
	Socket socket;

	public GuessGameServerHandler(int mv, long tl, Socket cl) throws UnsupportedEncodingException, IOException {
		id = idAvailable;
		idAvailable++;
		socket = cl;
		reader = new BufferedReader(new InputStreamReader(cl.getInputStream(), "UTF-8"));
		writer = new PrintWriter(cl.getOutputStream(),true);
		gs = new GameState(mv,tl,this);
		Thread thread = new Thread(gs);
		thread.start();
		System.out.println("Handler is Connected");
	}

	void send(String msg) throws IOException {
		writer.println(msg);
	}

	void log(String msg) {
		System.out.println(id+": "+msg);
	}

	void shutdownInput() throws IOException{socket.shutdownInput();
	System.out.println(id + " has been shutdown.");
	}

	@Override
	public void run() {
		try {
			send("START");
			log("Target is "+gs.getTarget());
			while (!gs.finished()) {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				try {
					String message = reader.readLine();
					log(message);
					writer = new PrintWriter(socket.getOutputStream(),true);
					gs.guess(Integer.parseInt(message));
					send(gs.toString());
					if (gs.finished()) break;
				} catch (SocketException e) {}
			}
		} catch (IOException e) {e.printStackTrace();}
	}
}