package ns.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

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
import ns.utils.Maths;
import ns.world.World;

public class MasterRenderer {
	private static final float FOV = 70;
	protected static final float NEAR_PLANE = 0.1f, FAR_PLANE = 740f;
	protected static final float RED = 0.435f, GREEN = 0.812f, BLUE = 1.0f;
	protected static final Vector2f FOG_VALUES = new Vector2f(0.0016f, 5.0f);

	private static final float TIME_SPEED = 0.15f;

	public static final List<VAO> standardModels = new ArrayList<>();
	public static MasterRenderer instance;

	public static void initStandardModels() {
		VAO vao = VAOLoader.storeDataInVAO(
				new VBOData(new float[] { -1, 1, -1, -1, 1, 1, 1, -1, }).withAttributeNumber(0).withDimensions(2));
		standardModels.add(vao);
	}

	private StaticShader shader = new StaticShader();
	private Renderer renderer;

	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;

	private Matrix4f projectionMatrix;

	private Map<VAO, List<Entity>> entities = new HashMap<>();

	private Terrain terrain;

	private float time;
	private boolean inc;

	public MasterRenderer() {
		createProjectionMatrix();
		renderer = new Renderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		instance = this;
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public static void prepare() {
		GL11.glClearColor(RED, GREEN, BLUE, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL32.glProvokingVertex(GL32.GL_FIRST_VERTEX_CONVENTION);
	}

	public void renderScene(World world, ICamera camera, Light light, Vector4f clipPlane, boolean updateTime) {
		prepare();
		for (Entity e : world.getEntities())
			process(e);
		process(world.getTerrain());
		render(camera, light, clipPlane, updateTime);
	}

	public void process(Entity e) {
		VAO model = e.getModel();
		List<Entity> bash = entities.get(model);
		if (bash == null) {
			bash = new ArrayList<>();
			entities.put(model, bash);
		}
		bash.add(e);
	}

	public void process(Terrain terrain) {
		this.terrain = terrain;
	}

	public void render(ICamera camera, Light sun, Vector4f clipPlane, boolean updateTime) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		shader.start();
		if (updateTime) {
			time += TIME_SPEED * DisplayManager.getFrameTimeSeconds() * (inc ? 1f : -1f);
			shader.time.load(time);
		}
		shader.skyColor.load(new Vector3f(RED, GREEN, BLUE));
		shader.fogValues.load(FOG_VALUES);
		shader.light.load(sun);
		shader.viewMatrix.load(viewMatrix);
		shader.clipPlane.load(clipPlane);
		renderer.render(entities);
		shader.stop();
		entities.clear();
		
		if (terrain != null) {
			terrainShader.start();
			terrainShader.skyColor.load(new Vector3f(RED, GREEN, BLUE));
			terrainShader.fogValues.load(FOG_VALUES);
			terrainShader.light.load(sun);
			terrainShader.viewMatrix.load(viewMatrix);
			terrainShader.clipPlane.load(clipPlane);
			terrainRenderer.render(terrain);
			terrainShader.stop();
			terrain = null;
		}
	}

	private void createProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();

		float y_scale = 1f / (float) Math.tan(FOV / 2f);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
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
}