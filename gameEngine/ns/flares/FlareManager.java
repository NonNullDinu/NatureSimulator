package ns.flares;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import ns.camera.ICamera;
import ns.derrivedOpenGLObjects.FlareTexture;
import ns.entities.Light;
import ns.renderers.QuadRenderer;
import ns.utils.Maths;

public class FlareManager {
	private static final float DIST = 0.3f;
	private static final Vector2f CENTER = new Vector2f(0.5f, 0.5f);

	private FlareTexture[] textures;
	private float brightness;

	public FlareManager(FlareTexture... textures) {
		this.textures = textures;
	}

	public void updateFlares(Light light, ICamera camera) {
		Vector2f sunCoords = convertToScreenSpace(
				new Vector3f(camera.getPosition().x + -light.getDir().x, camera.getPosition().y + -light.getDir().y,
						camera.getPosition().z + -light.getDir().z),
				Maths.createViewMatrix(camera), camera.getProjectionMatrix());
		if (sunCoords == null) {
			return;
		}
		Vector2f sunToCenter = Vector2f.sub(CENTER, sunCoords, null);
		brightness = 1 - (sunToCenter.length() / 0.9f);
		if (brightness > 0) {
			calcFlarePositions(sunToCenter, sunCoords);
		}
	}

	private void calcFlarePositions(Vector2f sunToCenter, Vector2f sunCoords) {
		for (int i = 0; i < textures.length; i++) {
			Vector2f direction = new Vector2f(sunToCenter);
			direction.scale(i * DIST);
			Vector2f flarePos = Vector2f.add(sunCoords, direction, null);
			flarePos.x = flarePos.x * -2.0f + 1.0f;
			flarePos.y = flarePos.y * 2.0f - 1.0f;
			textures[i].setPosition(flarePos);
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
		for (FlareTexture tex : textures) {
			QuadRenderer.render(tex.getPosition(), new Vector2f(tex.getScale(), tex.getScale()), tex.getTexture(),
					GL11.GL_ONE, brightness);
		}
	}
}