package ns.renderers;

import ns.camera.ICamera;
import ns.components.Blueprint;
import ns.display.DisplayManager;
import ns.entities.Entity;
import ns.entities.Light;
import ns.openglObjects.VAO;
import ns.openglWorkers.VAOLoader;
import ns.openglWorkers.VBOData;
import ns.shaders.StaticShader;
import ns.shaders.TerrainShader;
import ns.terrain.Terrain;
import ns.utils.GU;
import ns.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {
	public static final float FAR_PLANE = 1000f;
	private static final float RED = 0.435f;
	private static final float GREEN = 0.812f;
	private static final float BLUE = 1.0f;
	static final Vector2f FOG_VALUES = new Vector2f(0.0018f, 5.0f);
	static Vector3f CLEAR_COLOR;

	private static final float TIME_SPEED = 0.15f;

	public static final List<VAO> standardModels = new ArrayList<>();
	public static MasterRenderer instance;

	public static void initStandardModels() {
		VAO vao = VAOLoader.storeDataInVAO(
				new VBOData(new float[] { -1, 1, -1, -1, 1, 1, 1, -1, }).withAttributeNumber(0).withDimensions(2));
		standardModels.add(vao);
		int ang = 6;
		float[] positions = new float[ang * ang * 18];
		int vertexPointer = 0;
		int dlt = 360 / ang;
		for(int a = 0; a < 360; a += dlt) { // horizontal
			for(int b = 0; b < 360; b += dlt) { // vertical
				positions[vertexPointer++] = (float) (Math.sin(Math.toRadians(a)) * Math.cos(Math.toRadians(b)));
				positions[vertexPointer++] = (float) (Math.sin(Math.toRadians(a)) * Math.sin(Math.toRadians(b)));
				positions[vertexPointer++] = (float) Math.cos(Math.toRadians(a));
				
				positions[vertexPointer++] = (float) (Math.sin(Math.toRadians(a + dlt)) * Math.cos(Math.toRadians(b)));
				positions[vertexPointer++] = (float) (Math.sin(Math.toRadians(a + dlt)) * Math.sin(Math.toRadians(b)));
				positions[vertexPointer++] = (float) Math.cos(Math.toRadians(a + dlt));

				positions[vertexPointer++] = (float) (Math.sin(Math.toRadians(a)) * Math.cos(Math.toRadians(b + dlt)));
				positions[vertexPointer++] = (float) (Math.sin(Math.toRadians(a)) * Math.sin(Math.toRadians(b + dlt)));
				positions[vertexPointer++] = (float) Math.cos(Math.toRadians(a));

				positions[vertexPointer++] = (float) (Math.sin(Math.toRadians(a)) * Math.cos(Math.toRadians(b + dlt)));
				positions[vertexPointer++] = (float) (Math.sin(Math.toRadians(a)) * Math.sin(Math.toRadians(b + dlt)));
				positions[vertexPointer++] = (float) Math.cos(Math.toRadians(a));

				positions[vertexPointer++] = (float) (Math.sin(Math.toRadians(a + dlt)) * Math.cos(Math.toRadians(b)));
				positions[vertexPointer++] = (float) (Math.sin(Math.toRadians(a + dlt)) * Math.sin(Math.toRadians(b)));
				positions[vertexPointer++] = (float) Math.cos(Math.toRadians(a + dlt));

				positions[vertexPointer++] = (float) (Math.sin(Math.toRadians(a + dlt)) * Math.cos(Math.toRadians(b + dlt)));
				positions[vertexPointer++] = (float) (Math.sin(Math.toRadians(a + dlt)) * Math.sin(Math.toRadians(b + dlt)));
				positions[vertexPointer++] = (float) Math.cos(Math.toRadians(a + dlt));
			}
		}
		vao = VAOLoader.storeDataInVAO(
				new VBOData(positions).withAttributeNumber(0).withDimensions(3));
		standardModels.add(vao);
	}

	private final StaticShader shader = new StaticShader();
	private final EntityRenderer renderer;

	private final TerrainShader terrainShader = new TerrainShader();
	private final TerrainRenderer terrainRenderer;

	private final Map<VAO, List<Entity>> entities = new HashMap<>();

	private Terrain terrain;

	private float time;
	private boolean inc;

	public MasterRenderer(ICamera camera) {
		renderer = new EntityRenderer(shader, camera.getProjectionMatrix());
		terrainRenderer = new TerrainRenderer(terrainShader, camera.getProjectionMatrix());
		instance = this;
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		CLEAR_COLOR = new Vector3f();
	}

	public static void prepare() {
		Vector3f color = GU.mix(new Vector3f(RED, GREEN, BLUE), new Vector3f(0f, 0f, 0.2f), GU.time.nightFactor());
		GL11.glClearColor(color.x, color.y, color.z, 1.0f);
		CLEAR_COLOR = color;
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL32.glProvokingVertex(GL32.GL_FIRST_VERTEX_CONVENTION);
	}

	public static void disableBackfaceCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public static void enableBackfaceCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public void renderScene(World world, ICamera camera, Light sun, Light moon, Vector4f clipPlane,
	                        boolean updateTime) {
		prepare();
		for (int i = 0; i < world.getEntities().size(); i++)
			process(world.getEntities().get(i));
		process(world.getTerrain());
		render(camera, sun, moon, clipPlane, updateTime);
	}

	private void process(Entity e) {
		VAO model = e.getModel();
		List<Entity> bash = entities.computeIfAbsent(model, k -> new ArrayList<>());
		bash.add(e);
	}

	private void process(Terrain terrain) {
		this.terrain = terrain;
	}

	public void render(ICamera camera, Light sun, Light moon, Vector4f clipPlane, boolean updateTime) {
		Matrix4f viewMatrix = camera.getViewMatrix();

		if (terrain != null) {
			terrainShader.start();
			terrainShader.skyColor.load(CLEAR_COLOR);
			terrainShader.fogValues.load(FOG_VALUES);
			terrainShader.sun.load(sun);
			terrainShader.moon.load(moon);
			terrainShader.viewMatrix.load(viewMatrix);
			terrainShader.clipPlane.load(clipPlane);
			terrainRenderer.render(terrain);
			terrainShader.stop();
			terrain = null;
		}

		shader.start();
		if (updateTime) {
			time += TIME_SPEED * DisplayManager.getInGameTimeSeconds() * (inc ? 1f : -1f);
			shader.time.load(time);
		}
		shader.skyColor.load(CLEAR_COLOR);
		shader.fogValues.load(FOG_VALUES);
		shader.sun.load(sun);
		shader.moon.load(moon);
		shader.viewMatrix.load(viewMatrix);
		shader.clipPlane.load(clipPlane);
		renderer.render(entities);
		shader.stop();
		entities.clear();
	}

	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}

	public void prepareAndProcess(World world) {
		prepare();
		for (Entity e : world.getEntities())
			process(e);
		process(world.getTerrain());
	}

	public void process(World world) {
		for (Entity e : world.getEntities())
			process(e);
		process(world.getTerrain());
	}

	public void render(Blueprint blueprint, Vector3f position) {
		renderer.render(blueprint, position);
	}

	public void render(Blueprint blueprint, Vector3f position, float rotX, float rotY, float rotZ, float scale,
	                   Vector3f color) {
		renderer.render(blueprint, position, rotX, rotY, rotZ, scale, color);
	}
}