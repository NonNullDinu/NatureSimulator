/*
 * Copyright (C) 2018  Dinu Blanovschi
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

package obj;

import data.GameData;
import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;
import ns.utils.GU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import resources.In;

import java.io.BufferedReader;
import java.util.ArrayList;

public class OBJLoader {

	public static VAO loadObj(String file) {
		In asResource = GameData.getResourceAt(file);
		if (!asResource.exists())
			return null;
		BufferedReader reader = GU.open(asResource);
		Materials materials = null;
		{
			In materialsResource = GameData.getResourceAt(file.replace(".obj", ".mtl"));
			if (materialsResource.exists())
				materials = new Materials(materialsResource);
		}
		String line;
		ArrayList<Vector3f> vertices = new ArrayList<>();
		ArrayList<Vector2f> textures = new ArrayList<>();
		ArrayList<Vector3f> normals = new ArrayList<>();
		ArrayList<Integer> indices = new ArrayList<>();
		float[] verticesArray;
		float[] texturesArray = null;
		float[] colorsArray = null;
		float[] colorsIndicators = null;
		float[] normalsArray = null;
		float[] materialsArray = null;
		int[] indicesArray;

		try {
			Material current = null;
			while (true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if (line.startsWith("mtllib") && materials == null) {
					String[] filepcs = file.split("/");
					String mtlFile = file.replace(filepcs[filepcs.length - 1], currentLine[1]);
					In materialsResource = GameData.getResourceAt(mtlFile);
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
