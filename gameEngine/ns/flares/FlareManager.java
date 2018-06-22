package ns.flares;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import ns.camera.ICamera;
import ns.derrivedOpenGLObjects.FlareTexture;
import ns.entities.Light;
import ns.openglObjects.Query;
import ns.openglObjects.Texture;
import ns.renderers.QuadRenderer;
import ns.utils.Maths;

public class FlareManager {
	private static final float DIST = 0.2f;
	private static final Vector2f CENTER = new Vector2f(0.5f, 0.5f);
	private static final int TOTAL_SAMPLES = 44476;

	private FlareTexture[] textures;
	private float brightness;
	private Vector2f sunToCenter;
	private Texture sunTex;
	private Query sunSamplePassed;
	private int lastResult;
	private float coverage;

	public FlareManager(Texture sunTex, FlareTexture... textures) {
		this.textures = textures;
		this.sunTex = sunTex;
		this.sunSamplePassed = new Query(GL15.GL_SAMPLES_PASSED);
		coverage = 1f;
	}

	public void updateFlares(Light light, ICamera camera) {
		Vector2f sunCoords = convertToScreenSpace(new Vector3f(camera.getPosition().x - light.getDir().x * 1000f,
				camera.getPosition().y - light.getDir().y * 1000f, camera.getPosition().z - light.getDir().z * 1000f),
				Maths.createViewMatrix(camera), camera.getProjectionMatrix());
		if (sunCoords == null) {
			return;
		}
		sunToCenter = Vector2f.sub(CENTER, sunCoords, null);
		brightness = 1 - sunToCenter.length() / 2.0f;
		if (brightness > 0) {
			calcFlarePositions(sunToCenter, sunCoords);
		}
	}

	private void calcFlarePositions(Vector2f sunToCenter, Vector2f sunCoords) {
		float rotation = (float) Math.toDegrees(Vector2f.angle(sunToCenter, new Vector2f(0, 1)));
		if (sunToCenter.x < 0)
			rotation = 360f - rotation;
		for (int i = 0; i < textures.length; i++) {
			Vector2f direction = new Vector2f(sunToCenter);
			direction.scale(i * DIST);
			Vector2f flarePos = Vector2f.add(sunCoords, direction, null);
			flarePos.x = flarePos.x * -2.0f + 1.0f;
			flarePos.y = flarePos.y * 2.0f - 1.0f;
			textures[i].setPosition(flarePos);
			if (textures[i].hasRotation())
				textures[i].setRotation(rotation);
		}
	}

	private Vector2f convertToScreenSpace(Vector3f worldPos, Matrix4f viewMat, Matrix4f projectionMat) {
		Vector4f coords = new Vector4f(worldPos.x, worldPos.y, worldPos.z, 1f);
		Matrix4f.transform(viewMat, coords, coords);
		Matrix4f.transform(projectionMat, coords, coords);
		if (coords.w <= 0) {
			return null;
		}
		float x = (coords.x / coords.w + 1) / 2f;
		float y = 1 - ((coords.y / coords.w + 1) / 2f);
		return new Vector2f(x, y);
	}

	public void render() {
		if (sunToCenter == null)
			return;
		if (sunSamplePassed.isResultAvailable()) {
			int result = sunSamplePassed.getResult();
			lastResult = result;
			coverage = Math.min((float) result / (float) TOTAL_SAMPLES, 1f);
			System.out.println(coverage);
		}
		if (!sunSamplePassed.isInUse()) {
			sunSamplePassed.beginQuery();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			QuadRenderer.renderMaxDepth(new Vector2f(-sunToCenter.x, sunToCenter.y), new Vector2f(0.3f, 0.3f), sunTex,
					true);
			GL11.glDisable(GL11.GL_BLEND);
			sunSamplePassed.endQuery();
		}
		if (lastResult > 0)
			for (FlareTexture tex : textures) {
				if (tex.hasRotation())
					QuadRenderer.render(tex.getPosition(), new Vector2f(tex.getScale(), tex.getScale()),
							tex.getTexture(), GL11.GL_ONE, brightness * coverage, true, tex.getRotation());
				else
					QuadRenderer.render(tex.getPosition(), new Vector2f(tex.getScale(), tex.getScale()),
							tex.getTexture(), GL11.GL_ONE, brightness * coverage, true);
			}
	}

	public void cleanUp() {
		sunSamplePassed.delete();
	}
}