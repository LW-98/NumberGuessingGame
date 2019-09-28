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
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ClientState implements Runnable {

	Scanner sc;
	boolean _finished;
	static BufferedReader _tty;
	String message;
	PrintWriter writer;
	Socket socket;

	ClientState(PrintWriter o) {
		writer = o;
	}

	void setSocket(Socket s) {
		socket = s;
	}

	void userPrint(boolean end, String msg) {
		message = msg;
		if (end) System.out.println(msg);
		if (!end) System.out.print("\n" + msg + "\nEnter Guess: ");
	}

	public String readLineTimeout(BufferedReader reader, long timeout) throws TimeoutException, IOException {
		long starttime = System.currentTimeMillis();
		while (!reader.ready()) {
			if (System.currentTimeMillis() - starttime >= timeout)
				throw new TimeoutException();
			try { Thread.sleep(50); } catch (Exception ignore) {}
		}
		return reader.readLine();
	}

	String getLastInput() {
		return message;
	}

	boolean isFinished() {
		return _finished;
	}

	@Override
	public void run() {
		try {
			writer = new PrintWriter(socket.getOutputStream(),true);
			sc = new Scanner(System.in);
		} catch (IOException e1) {e1.printStackTrace();	}
		while (true) {
			_tty = new BufferedReader(new InputStreamReader(System.in));
			try {
				writer.println(readLineTimeout(_tty,10000));
			} catch (TimeoutException | IOException e) {
				System.out.println("\nTime has ran out, game over");
				_finished = true;
				break;
			}
		}
	}

}
