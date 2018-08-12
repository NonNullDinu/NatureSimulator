package obj;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ByteMaterials extends ArrayList<ByteMaterial> {
	private static final long serialVersionUID = 7100141434010333203L;
	private static final int BYTES_PER_MATERIAL = 28; // 7 floats, or 28 bytes
	
	public ByteMaterials(List<Byte> bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(BYTES_PER_MATERIAL);
		for(int i = 0; i < bytes.size() / BYTES_PER_MATERIAL; i++) {
			for(int j = 0; j < BYTES_PER_MATERIAL; j++)
				buffer.put(bytes.get(i * BYTES_PER_MATERIAL + j));
			buffer.flip();
			super.add(new ByteMaterial(buffer));
			buffer.clear();
		}
	}
}