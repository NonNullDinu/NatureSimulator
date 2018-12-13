package ns.customFileFormat;

import data.GameData;
import ns.exceptions.LoadingException;
import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;
import ns.utils.GU;
import obj.ByteMaterial;
import obj.ByteMaterials;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import resources.In;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MdlFile implements File {
	private final String location;

	public MdlFile(String location) {
		this.location = location;
	}

	@Override
	public VAO load() throws LoadingException {
		In resource = GameData.getResourceAt(location, true);
		InputStream ins = resource.asInputStream();
		byte[] data;
		try {
			data = ins.readAllBytes();
		} catch (IOException e) {
			throw new LoadingException(e);
		}
		if (resource.version() == 1) {
			int sz = GU.readInt(data[0], data[1], data[2], data[3]);
			int vc = GU.readInt(data[4], data[5], data[6], data[7]);
			float[] verticesArray = new float[sz * 3];
			float[] normalsArray = new float[sz * 3];
			float[] texturesArray = new float[sz * 2];
			float[] colorsArray = new float[sz * 3];
			float[] colorsIndicators = new float[sz * 3];
			float[] materialsArray = new float[sz * 4];
			int[] indicesArray = new int[vc];
			List<Byte> bytes = new ArrayList<>();
			int ptr = 8;
			ByteMaterials materials = null;
			if (data[8] == 0x7F) {
				ptr++;
				while (data[ptr] != 0x7F)
					bytes.add(data[ptr++]);
				materials = new ByteMaterials(bytes);
				ptr++;
			}
			for (int i = 0; i < verticesArray.length; i++) {
				verticesArray[i] = GU.readFloat(data[ptr++], data[ptr++], data[ptr++], data[ptr++]);
			}
			for (int i = 0; i < normalsArray.length; i++) {
				normalsArray[i] = GU.readFloat(data[ptr++], data[ptr++], data[ptr++], data[ptr++]);
			}
			for (int i = 0; i < texturesArray.length; i++) {
				texturesArray[i] = GU.readFloat(data[ptr++], data[ptr++], data[ptr++], data[ptr++]);
			}
			for (int i = 0; i < materialsArray.length / 4; i++) {
				int matIndex = data[ptr++];
				if (materials != null) {
					ByteMaterial mat = materials.get(matIndex);
					Vector4f matData = mat.getData();
					materialsArray[i * 4] = matData.x;
					materialsArray[i * 4 + 1] = matData.y;
					materialsArray[i * 4 + 2] = matData.z;
					materialsArray[i * 4 + 3] = matData.w;
					Vector3f matColor = mat.getColor();
					colorsArray[i * 3] = matColor.x;
					colorsArray[i * 3 + 1] = matColor.y;
					colorsArray[i * 3 + 2] = matColor.z;
					Vector3f matIndicators = mat.getIndicators();
					colorsIndicators[i * 3] = matIndicators.x;
					colorsIndicators[i * 3 + 1] = matIndicators.y;
					colorsIndicators[i * 3 + 2] = matIndicators.z;
				}
			}
			for (int i = 0; i < indicesArray.length; i++) {
				indicesArray[i] = GU.readInt(data[ptr++], data[ptr++], data[ptr++], data[ptr++]);
			}

			return VAOLoader.storeDataInVAO(new VBOData(verticesArray).withAttributeNumber(0).withDimensions(3),
					new VBOData(normalsArray).withAttributeNumber(1).withDimensions(3),
					new VBOData(texturesArray).withAttributeNumber(2).withDimensions(2),
					new VBOData(colorsArray).withAttributeNumber(3).withDimensions(3),
					new VBOData(colorsIndicators).withAttributeNumber(4).withDimensions(3),
					new VBOData(materialsArray).withAttributeNumber(5).withDimensions(4),
					new VBOData(indicesArray).isIndices(true));
		}
		return null;
	}
}