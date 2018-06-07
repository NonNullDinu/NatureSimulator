package obj;

import java.io.BufferedReader;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;
import ns.utils.GU;
import res.Resource;

public class OBJLoader {

	public static VAO loadObj(String file) {
		Resource asResource = new Resource().withLocation(file).withVersion(false).create();
		if (!asResource.exists())
			return null;
		BufferedReader reader = GU.open(asResource);
		Materials materials = null;
		{
			Resource materialsResource = new Resource().withLocation(file.replace(".obj", ".mtl")).withVersion(false).create();
			if (materialsResource.exists())
				materials = new Materials(materialsResource);
		}
		String line;
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		ArrayList<Vector2f> textures = new ArrayList<Vector2f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		float[] verticesArray = null;
		float[] texturesArray = null;
		float[] colorsArray = null;
		float[] colorsIndicators = null;
		float[] normalsArray = null;
		float[] materialsArray = null;
		int[] indicesArray = null;

		try {
			Material current = null;
			while (true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if (line.startsWith("mtllib") && materials == null) {
					String[] filepcs = file.split("/");
					String mtlFile = file.replace(filepcs[filepcs.length - 1], currentLine[1]);
					Resource materialsResource = new Resource().withLocation(mtlFile).withVersion(false).create();
					if (materialsResource.exists())
						materials = new Materials(materialsResource);
				}
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				} else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					normals.add(normal);
				} else if (line.startsWith("usemtl ")) {
					current = materials.get(line.split(" ")[1]);
				} else if (line.startsWith("f ")) {
					texturesArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					colorsArray = new float[vertices.size() * 3];
					colorsIndicators = new float[vertices.size() * 3];
					materialsArray = new float[vertices.size() * 4];
					break;
				}

			}
			while (line != null) {
				if (line.startsWith("usemtl ")) {
					current = materials.get(line.split(" ")[1]);
				}
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");

				processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray, current, colorsArray,
						colorsIndicators, materialsArray);
				processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray, current, colorsArray,
						colorsIndicators, materialsArray);
				processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray, current, colorsArray,
						colorsIndicators, materialsArray);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];

		int vertexPointer = 0;
		for (Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}

		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}

		return VAOLoader.storeDataInVAO(new VBOData(verticesArray).withAttributeNumber(0).withDimensions(3),
				new VBOData(normalsArray).withAttributeNumber(1).withDimensions(3),
				new VBOData(texturesArray).withAttributeNumber(2).withDimensions(2),
				new VBOData(colorsArray).withAttributeNumber(3).withDimensions(3),
				new VBOData(colorsIndicators).withAttributeNumber(4).withDimensions(3),
				new VBOData(materialsArray).withAttributeNumber(5).withDimensions(4),
				new VBOData(indicesArray).isIndices(true));
	}

	private static void processVertex(String[] vertexData, ArrayList<Integer> indices, ArrayList<Vector2f> textures,
			ArrayList<Vector3f> normals, float[] texturesArray, float[] normalsArray, Material material, float[] colors,
			float[] colorsIndicators, float[] materialsArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		if (!normals.isEmpty()) {
			Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
			normalsArray[currentVertexPointer * 3] = currentNorm.x;
			normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
			normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
		}
		if (!textures.isEmpty()) {
			Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
			texturesArray[currentVertexPointer * 2] = currentTex.x;
			texturesArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
		} else if (material != null) {
			Vector3f materialColor = material.getColor();
			colors[currentVertexPointer * 3] = materialColor.x;
			colors[currentVertexPointer * 3 + 1] = materialColor.y;
			colors[currentVertexPointer * 3 + 2] = materialColor.z;

			Vector3f matColorsIndicators = material.getIndicators();
			colorsIndicators[currentVertexPointer * 3] = matColorsIndicators.x;
			colorsIndicators[currentVertexPointer * 3 + 1] = matColorsIndicators.y;
			colorsIndicators[currentVertexPointer * 3 + 2] = matColorsIndicators.z;

			Vector4f matData = material.getData();
			materialsArray[currentVertexPointer * 4] = matData.x;
			materialsArray[currentVertexPointer * 4 + 1] = matData.y;
			materialsArray[currentVertexPointer * 4 + 2] = matData.z;
			materialsArray[currentVertexPointer * 4 + 3] = matData.w;
		}
	}

}
