package ns.openglObjects;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;

import java.nio.ByteBuffer;

public class FBO implements IOpenGLObject {
	public static final int COLOR_TEXTURE = 1; // 00000001
	public static final int DEPTH_TEXTURE = 2; // 00000010
	public static final int DEPTH_RENDERBUFFER = 4; // 00000100

	private final int id;
	private int width;
	private int height;
	private int config;
	private Texture tex;
	private Texture depthTex;
	private int depthRenderBufferId = -1;
	private boolean created = false;

	public FBO(int width, int height, int config) {
		this.id = GL30.glGenFramebuffers();
		this.width = width;
		this.height = height;
		this.config = config;
	}

	public void init(int width, int height, int config) {
		this.width = width;
		this.height = height;
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		if ((config & COLOR_TEXTURE) != 0) {
			int texId = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE,
					(ByteBuffer) null);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texId, 0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			tex = new Texture(texId, width, height);
		}
		if ((config & DEPTH_TEXTURE) != 0) {
			int depthTexId = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexId);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0,
					GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, depthTexId, 0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			depthTex = new Texture(depthTexId, width, height);
		}
		if ((config & DEPTH_RENDERBUFFER) != 0) {
			depthRenderBufferId = GL30.glGenRenderbuffers();
			GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthRenderBufferId);
			GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
			GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
					depthRenderBufferId);
			GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
		}
		unbind();
		this.config = config;
	}

	public void createNewTexture() {
		int texId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE,
				(ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texId, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		tex = new Texture(texId, width, height);
	}
	
	public void setTextureNull() {
		tex = null;
	}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
		GL11.glViewport(0, 0, width, height);
	}

	public static void unbind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public Texture getTex() {
		return tex;
	}

	public Texture getDepthTex() {
		return depthTex;
	}

	public int getConfig() {
		return config;
	}

	public void cleanUp() {
		if (tex != null)
			tex.delete();
		if (depthTex != null)
			depthTex.delete();
		if (depthRenderBufferId != -1)
			GL30.glDeleteRenderbuffers(depthRenderBufferId);
		GL30.glDeleteFramebuffers(id);
	}

	public Vector2f getSize() {
		return new Vector2f(width, height);
	}

	public void blitToScreen() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL11.glDrawBuffer(GL11.GL_BACK);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, id);
		GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
		GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, Display.getWidth(), Display.getHeight(),
				GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	public void clear(int mask) {
		GL11.glClear(mask);
	}

	@Override
	public FBO create() {
		this.init(width, height, config);
		created = true;
		return this;
	}

	@Override
	public void delete() {
		if (tex != null)
			tex.delete();
		if (depthTex != null)
			depthTex.delete();
		if (depthRenderBufferId != -1)
			GL30.glDeleteRenderbuffers(depthRenderBufferId);
		GL30.glDeleteFramebuffers(id);
		created = false;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public boolean isCreated() {
		return created;
	}
}