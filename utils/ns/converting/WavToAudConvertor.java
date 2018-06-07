package ns.converting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.util.WaveData;

import res.Resource;

public class WavToAudConvertor {

	public static void main(String[] args) throws IOException {
		byte[] buf = new byte[50];
		int len = System.in.read(buf);
		String location = "";
		for (int i = 0; i < len - 1; i++)
			location += (char) buf[i];
		File target = new File("resources/" + location.replace(".wav", ".aud"));
		System.out.println("resources/" + location.replace(".wav", ".aud"));
		target.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(target));
		WaveData data = WaveData.create(new Resource().withLocation(location).withVersion(false).create().asInputStream());
		int format = data.format;
		ByteBuffer bufferData = data.data;
		int samplerate = data.samplerate;
		writer.write(format + " " + bufferData.limit() + " " + samplerate + '\n');
		while(bufferData.hasRemaining()) {
			writer.write(bufferData.get() + (bufferData.position() == bufferData.capacity() ? "" : " "));
		}
		writer.close();
	}
}