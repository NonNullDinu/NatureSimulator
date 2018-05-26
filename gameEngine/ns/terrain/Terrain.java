package ns.terrain;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ns.components.BiomeSpreadComponent;
import ns.entities.Entity;
import ns.openglObjects.VAO;
import ns.openglWorkers.DataPacking;
import ns.openglWorkers.VBOData;
import ns.utils.Maths;
import ns.worldSave.SerializableWorldObject;
import ns.worldSave.TerrainData;

public class Terrain implements SerializableWorldObject {
	public static final float SIZE = 2400;
	public static final int VERTEX_COUNT = (int) (256f * (SIZE / 2400f));
	private VAO model;
	private float x, z;
	private HeightsGenerator generator;
	private float[][] heights;
	private List<TerrainVertex> vertices;

	public Terrain() {
		x = -(SIZE / 2f);
		z = -(SIZE / 2f);
		vertices = new ArrayList<>();
		generator = new HeightsGenerator();
		model = initModel();
	}

	public Terrain(int seed) {
		x = -(SIZE / 2f);
		z = -(SIZE / 2f);
		vertices = new ArrayList<>();
		generator = new HeightsGenerator(seed);
		model = initModel();
	}

	public void initColors(List<Entity> entities) {
		for (TerrainVertex vertex : vertices) {
			for (Entity e : entities) {
				BiomeSpreadComponent comp = e.getBiomeSpreadComponent();
				if (comp != null) {
					Vector3f pos = posRelToTerrain(e.getPosition());
					float len = Vector3f.sub(pos, vertex.position, null).length();
					Vector2f minMax = comp.getMinMax();
					Vector3f cl = new Vector3f();
					if (len < minMax.x) {
						cl = comp.getBiome().getColor();
					} else if (len < minMax.y) {
						Vector3f bcl = new Vector3f(comp.getBiome().getColor());
						float fac = (len - minMax.x) / (minMax.y - minMax.x);
						bcl.scale(1.0f - fac);
						Vector3f prevC = new Vector3f(vertex.color);
						prevC.scale(fac);
						cl = new Vector3f(Vector3f.add(bcl, prevC, null));
					}
					if (!(cl.x == 0 && cl.y == 0 && cl.z == 0)) {
						if (vertex.color.x == 1.0f && vertex.color.y == 0.766f && vertex.color.z == 0.061f)
							vertex.color = cl;
						else
							Vector3f.add(vertex.color, cl, vertex.color).scale(0.5f); // Average colors
					}
				}
			}
		}
		float[] cls = new float[VERTEX_COUNT * VERTEX_COUNT * 3];
		int ptr = 0;
		for (TerrainVertex vertex : vertices) {
			cls[ptr++] = vertex.color.x;
			cls[ptr++] = vertex.color.y;
			cls[ptr++] = vertex.color.z;
		}
		DataPacking.replace(model, 2, cls);
	}

	public void updateColors(Entity e) {
		float[] cls = new float[VERTEX_COUNT * VERTEX_COUNT * 3];
		int ptr = 0;
		List<Integer> changes = new ArrayList<>();
		for (TerrainVertex vertex : vertices) {
			BiomeSpreadComponent comp = e.getBiomeSpreadComponent();
			if (comp != null) {
				Vector3f pos = posRelToTerrain(e.getPosition());
				float len = Vector3f.sub(pos, vertex.position, null).length();
				Vector2f minMax = comp.getMinMax();
				Vector3f cl = new Vector3f();
				if (len < minMax.x) {
					cl = comp.getBiome().getColor();
				} else if (len < minMax.y) {
					Vector3f bcl = new Vector3f(comp.getBiome().getColor());
					float fac = (len - minMax.x) / (minMax.y - minMax.x);
					bcl.scale(1.0f - fac);
					Vector3f prevC = new Vector3f(vertex.color);
					prevC.scale(fac);
					cl = new Vector3f(Vector3f.add(bcl, prevC, null));
				}
				if (!(cl.x == 0 && cl.y == 0 && cl.z == 0)) {
					if (vertex.color.x == 1.0f && vertex.color.y == 0.766f && vertex.color.z == 0.061f) {
						cls[ptr * 3] = cl.x;
						cls[ptr * 3 + 1] = cl.y;
						cls[ptr * 3 + 2] = cl.z;
					} else {
						cls[ptr * 3] = (vertex.color.x + cl.x) / 2f;
						cls[ptr * 3 + 1] = (vertex.color.y + cl.y) / 2f;
						cls[ptr * 3 + 2] = (vertex.color.z + cl.z) / 2f;
					}
					changes.add(ptr);
				}
			}
			ptr++;
		}
		List<Integer> ch = new ArrayList<>();
		for (int c : changes) {
			ptr = c;
			TerrainVertex vertex = vertices.get(ptr);
			if(vertex.color.x != cls[ptr * 3] || vertex.color.y != cls[ptr * 3 + 1] || vertex.color.z != cls[ptr * 3 + 2]) {
				ch.add(ptr);
			}
		}
		DataPacking.replace(model, 2, cls, ch, 3);
	}

	private Vector3f posRelToTerrain(Vector3f position) {
		return new Vector3f(position.x - this.x, position.y, position.z - this.z);
	}

	private VAO initModel() {
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		for (int i = 0; i < VERTEX_COUNT; i++)
			for (int j = 0; j < VERTEX_COUNT; j++)
				heights[i][j] = -100f;
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vao_vertices = new float[3 * count];
		float[] vao_normals = new float[3 * count];
		float[] vao_colors = new float[3 * count];
		int vertexPointer = 0;
		for (int z = 0; z < VERTEX_COUNT; z++) {
			for (int x = 0; x < VERTEX_COUNT; x++) {
				vao_vertices[3 * vertexPointer] = ((float) x / ((float) VERTEX_COUNT - 1)) * SIZE;
				vao_vertices[3 * vertexPointer + 1] = getHeight(x, z);
				vao_vertices[3 * vertexPointer + 2] = ((float) z / ((float) VERTEX_COUNT - 1)) * SIZE;

				Vector3f normal = getNormal(x, z);
				vao_normals[3 * vertexPointer] = normal.x;
				vao_normals[3 * vertexPointer + 1] = normal.y;
				vao_normals[3 * vertexPointer + 2] = normal.z;

				vao_colors[3 * vertexPointer] = 1.0f;
				vao_colors[3 * vertexPointer + 1] = 0.766f;
				vao_colors[3 * vertexPointer + 2] = 0.061f;

				vertices.add(new TerrainVertex(
						new Vector3f(vao_vertices[3 * vertexPointer], vao_vertices[3 * vertexPointer + 1],
								vao_vertices[3 * vertexPointer + 2]),
						new Vector3f(vao_colors[3 * vertexPointer], vao_colors[3 * vertexPointer + 1],
								vao_colors[3 * vertexPointer + 2])));

				vertexPointer++;
			}
		}
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int pointer = 0;
		for (int z = 0; z < VERTEX_COUNT - 1; z++) {
			for (int x = 0; x < VERTEX_COUNT - 1; x++) {
				int topLeft = z * VERTEX_COUNT + x;
				int topRight = topLeft + 1;
				int bottomLeft = (z + 1) * VERTEX_COUNT + x;
				int bottomRight = bottomLeft + 1;
				if ((x + z) % 2 == 0) {
					indices[pointer++] = topLeft;
					indices[pointer++] = bottomLeft;
					indices[pointer++] = bottomRight;
					indices[pointer++] = topRight;
					indices[pointer++] = topLeft;
					indices[pointer++] = bottomRight;
				} else {
					indices[pointer++] = topLeft;
					indices[pointer++] = bottomLeft;
					indices[pointer++] = topRight;
					indices[pointer++] = topRight;
					indices[pointer++] = bottomLeft;
					indices[pointer++] = bottomRight;
				}
			}
		}
		return DataPacking.storeDataInVAO(new VBOData(vao_vertices).withAttributeNumber(0).withDimensions(3),
				new VBOData(vao_normals).withAttributeNumber(1).withDimensions(3),
				new VBOData(vao_colors).withAttributeNumber(2).withDimensions(3).withUsage(GL15.GL_DYNAMIC_DRAW),
				new VBOData(indices).isIndices(true));
	}

	public float getHeight(float worldX, float worldZ) {
		float terrainx = worldX - this.x;
		float terrainz = worldZ - this.z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainx / gridSquareSize);
		int gridZ = (int) Math.floor(terrainz / gridSquareSize);
		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0)
			return 0;
		float xCoord = (terrainx % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainz % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1 - zCoord)) {
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		}
		return answer;
	}

	private float getHeight(int x, int z) {
		x = (x < 0 ? 0 : x);
		x = (x > VERTEX_COUNT - 1 ? VERTEX_COUNT - 1 : x);
		z = (z < 0 ? 0 : z);
		z = (z > VERTEX_COUNT - 1 ? VERTEX_COUNT - 1 : z);
		if (heights[x][z] == -100f) {
			float height = generator.generateHeight(x, z);
			heights[x][z] = height;
			return height;
		} else
			return heights[x][z];
	}

	private Vector3f getNormal(int x, int z) {
		float up = getHeight(x, z + 1);
		float down = getHeight(x, z - 1);
		float left = getHeight(x - 1, z);
		float right = getHeight(x + 1, z);
		Vector3f normal = new Vector3f(left - right, 2f, down - up);
		normal.normalise();
		return normal;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public VAO getModel() {
		return model;
	}

	@Override
	public TerrainData asData() {
		TerrainData data = new TerrainData();
		data.setSeed(generator.getSeed());
		return data;
	}
}
