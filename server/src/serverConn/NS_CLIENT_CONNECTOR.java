/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package serverConn;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class NS_CLIENT_CONNECTOR {
	private ServerSocket server = new ServerSocket(40000);
	private InputStreamReader reader;
	private OutputStreamWriter writer;
	private int responseCode;
	private String msg;
	private Socket socket;

	public NS_CLIENT_CONNECTOR() throws IOException {
	}

	public void connectToServer() {
		try {
			socket = server.accept();
			reader = new InputStreamReader(socket.getInputStream());
			writer = new OutputStreamWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		if (socket != null) {
			try {
				reader.close();
				writer.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessageToServer(int code) {
		try {
			writer.write(code);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readResponse() {
		try {
			responseCode = reader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getResponseCode() {
		return responseCode;
	}
}