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

package ns.converting;

import obj.Material;
import obj.Materials;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

class OBJToMDLConvertor {
	public static void main(String[] args) throws IOException {
		byte[] buf = new byte[50];
		int len = System.in.read(buf);
		String location = "";
		for (int i = 0; i < len - 1; i++)
			location += (char) buf[i];
		Material.init(OBJToMDLConvertor::getBytes);
		if (location.equals("UPDATE ALL")) {
			write(new File("../gameData/models"));
		} else {
			File target = new File("../gameData/models/" + location);
			write(target);
		}
	}

	private static void write(File file) throws IOException {
		if (file.isDirectory()) {
			for (File fl : file.listFiles())
				if (!fl.getName().endsWith(".mtl") && !fl.getName().endsWith(".mdl")) {
					write(fl);
				}
		} else {
			File target = new File(file.getPath().replace(".obj", ".mdl"));
			target.createNewFile();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			Materials materials = null;
			{
				File materialsResource = new File(file.getPath().replace(".obj", ".mtl"));
				if (materialsResource.exists())
					materials = new Materials(new BufferedReader(new FileReader(materialsResource)));
			}
			String line;
			ArrayList<Vector3f> vertices = new ArrayList<>();
			ArrayList<Vector2f> textures = new ArrayList<>();
			ArrayList<Vector3f> normals = new ArrayList<>();
			ArrayList<Integer> indices = new ArrayList<>();
			float[] verticesArray;
			float[] texturesArray = null;
			float[] normalsArray = null;
			byte[] materialsArray = null;
			int[] indicesArray;

			try {
				Material current = null;
				while (true) {
					line = reader.readLine();
					String[] currentLine = line.split(" ");
					if (line.startsWith("mtllib") && materials == null) {
						String[] filepcs = file.getPath().split("/");
						String mtlFile = file.getPath().replace(filepcs[filepcs.length - 1], currentLine[1]);
						File materialsResource = new File(mtlFile);
						if (materialsResource.exists())
							materials = new Materials(new BufferedReader(new FileReader(materialsResource)));
					}
					if (line.startsWith("v ")) {
						Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
								Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
						vertices.add(vertex);
					} else if (line.startsWith("vt ")) {
						Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
								Float.parseFloat(currentLine[2]));
						textures.add(texture);
					} else if (line.startsWith("vn ")) {
						Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
								Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
						normals.add(normal);
					} else if (line.startsWith("usemtl ")) {
						current = materials.get(line.split(" ")[1]);
					} else if (line.startsWith("f ")) {
						texturesArray = new float[vertices.size() * 2];
						normalsArray = new float[vertices.size() * 3];
						materialsArray = new byte[vertices.size()];
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

					processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray, current,
							materialsArray);
					processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray, current,
							materialsArray);
					processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray, current,
							materialsArray);
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
			OutputStream outStr = new FileOutputStream(target);
			int version = 1;
			outStr.write(version);
			if (version == 1) {
				int size = vertices.size(), vertexCount = indices.size();
				outStr.write(getBytes(size));
				outStr.write(getBytes(vertexCount));
				if (materials != null) {
					outStr.write(0x7F);
					List<Byte> bts = materials.asByteMaterials();
					for (byte b : bts)
						outStr.write(b);
					outStr.write(0x7F);
				}
				for (int i = 0; i < verticesArray.length; i++) {
					outStr.write(getBytes(verticesArray[i]));
				}
				for (int i = 0; i < normalsArray.length; i++) {
					outStr.write(getBytes(normalsArray[i]));
				}
				for (int i = 0; i < texturesArray.length; i++) {
					outStr.write(getBytes(texturesArray[i]));
				}
//				for (int i = 0; i < materialsArray.length; i++) {
				outStr.write(materialsArray);
//				}
				for (int i = 0; i < indicesArray.length; i++) {
					outStr.write(getBytes(indicesArray[i]));
				}
			}
		}
	}

	private static void processVertex(String[] vertexData, ArrayList<Integer> indices, ArrayList<Vector2f> textures,
	                                  ArrayList<Vector3f> normals, float[] texturesArray, float[] normalsArray, Material material,
	                                  byte[] materialsArray) {
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
			materialsArray[currentVertexPointer] = material.getIndex();
		}
	}

	private static final ByteBuffer buffer = ByteBuffer.allocate(4);

	private static byte[] getBytes(float f) {
		buffer.clear();
		buffer.putFloat(f);
		return buffer.array();
	}

	private static byte[] getBytes(int i) {
		buffer.clear();
		buffer.putInt(i);
		return buffer.array();
	}
}