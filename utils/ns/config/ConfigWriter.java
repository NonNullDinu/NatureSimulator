package ns.config;

import java.io.IOException;
import java.io.OutputStream;

import ns.utils.GU;
import resources.WritingResource;

public class ConfigWriter {

	public static void main(String[] args) throws IOException {
		OutputStream stream = new WritingResource().withLocation("gameData/config/gameConfiguration.config")
				.create().asOutputStream();
		while (true) {
			System.out.println("Full screen?Y/N");
			int in = System.in.read();
			if (in == 89 || in == 121) {
				stream.write(GU.binaryInt("0001 0001"));
				stream.write(GU.binaryInt("0000 0000"));
				break;
			}
			if (in == 78 || in == 110) {
				stream.write(GU.binaryInt("0001 0000"));
				stream.write(GU.binaryInt("0000 0000"));
				break;
			}
		}
		stream.close();
	}
}