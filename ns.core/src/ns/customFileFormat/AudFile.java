package ns.customFileFormat;

import data.GameData;
import ns.exceptions.LoadingException;
import ns.openALObjects.Buffer;
import ns.utils.GU;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;

public class AudFile implements File {
	private String location;

	public AudFile(String location) {
		this.location = location;
	}

	@Override
	public Buffer load() throws LoadingException {
		int id = AL10.alGenBuffers();
		BufferedReader reader = GU.open(GameData.getResourceAt(location));
		String line;
		try {
			line = reader.readLine();
			String[] pts = line.split(" ");
			int format = Integer.parseInt(pts[0]);
			int size = Integer.parseInt(pts[1]);
			ByteBuffer data = BufferUtils.createByteBuffer(size);
			int freq = Integer.parseInt(pts[2]);
			line = reader.readLine();
			pts = line.split(" ");
			for (int idx = 0; idx < size; idx++)
				data.put(Byte.parseByte(pts[idx]));
			data.flip();
			AL10.alBufferData(id, format, data, freq);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		return new Buffer(id);
	}
}