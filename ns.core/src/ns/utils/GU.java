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

package ns.utils;

import data.GameData;
import data.SaveData;
import ns.components.Blueprint;
import ns.components.BlueprintCreator;
import ns.display.DisplayManager;
import ns.exceptions.GameException;
import ns.fontMeshCreator.FontType;
import ns.openglObjects.FBO;
import ns.openglObjects.Texture;
import ns.openglObjects.VAO;
import ns.parallelComputing.CreateVAORequest;
import ns.parallelComputing.Request;
import ns.parallelComputing.ThreadMaster;
import ns.renderers.MasterRenderer;
import ns.time.DayNightCycle;
import ns.time.Time;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.KHRDebug;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import resources.In;
import resources.Out;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class GU {
    public static final Random random = new Random();
    public static final int CURRENT_WORLD_FILE_VERSION = 4;
    public static final int TOTAL_NUMBER_OF_ENTITIES = 13;
    public static final String WORLD_SAVE_FILE_FORMAT = "nssv";
    public static final String MAIN_THREAD_NAME = "main thread";
    public static final boolean OS_WINDOWS;
    public static final boolean OS_LINUX;
    public static final int WIDTH = 1600, HEIGHT = 1000;
	private static final float[] mouseLengths = new float[20];
    private static final List<Texture> textures = new ArrayList<>();
    private static final List<ByteBuffer> textureBuffers = new ArrayList<>();
    private static final ByteBuffer buffer = ByteBuffer.allocateDirect(4);
    private static final DocumentBuilder documentBuilder;
    public static boolean prevFrameClicked;
    public static Vector3f mouseDelta;
    public static float lastFramesLengths;
    public static FontType Z003;
    public static FontType caladea;
    public static String path;
    public static Vector2b mouseButtons;
    public static Time time;
    private static double OLD_X, OLD_Y;
    private static double OLD_SCROLL;
    private static double scroll;
    private static long defaultC = 0;

	static {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setValidating(true);
        try {
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new GameException(e.getMessage(), e.getStackTrace());
        }
        String os = System.getProperty("os.name");
        OS_WINDOWS = os.equals("Windows");
        OS_LINUX = os.equals("Linux");
        time = new Time(new DayNightCycle() {
            private static final long serialVersionUID = -6093481760110971753L;

            @Override
            public boolean isDay(float t) {
                float h = (t / H_S_DURATION) % 24f;
                return h >= 8f && h < 17f;
            }

            @Override
            public boolean isNight(float t) {
                float h = (t / H_S_DURATION) % 24f;
                return (h >= 21f && h < 24f) || (h >= 0f && h < 5f);
            }

            @Override
            public boolean isMorning(float t) {
                float h = (t / H_S_DURATION) % 24f;
                return h >= 5f && h < 8f;
            }

            @Override
            public boolean isEvening(float t) {
                float h = (t / H_S_DURATION) % 24f;
                return h >= 17f && h < 21f;
            }

            @Override
            public float dayFactor(float t) {
                if (isDay(t))
                    return 1;
                else if (isEvening(t)) {
                    return 1.0f - (t / H_S_DURATION % 24f - 17f) / 4f;
                } else if (isMorning(t)) {
                    return (t / H_S_DURATION % 24f - 5f) / 3f;
                } else
                    return 0f;
            }

            @Override
            public float nightFactor(float t) {
                if (isNight(t))
                    return 1;
                else if (isEvening(t)) {
                    return ((t / H_S_DURATION) % 24f - 17f) / 4f;
                } else if (isMorning(t)) {
                    return 1.0f - ((t / H_S_DURATION) % 24f - 5f) / 3f;
                } else
                    return 0f;
            }
        });
    }

    public static void init() {
        GameData.init();
        SaveData.init();
    }

    public static BufferedReader open(In resource) {
        return new BufferedReader(new InputStreamReader(resource.asInputStream()));
    }

    public static PrintWriter open(Out resource) {
        return new PrintWriter(resource.asOutputStream());
    }

    public static void rn_update() {
        double x = mouseX(), y = mouseY(), wheel = mouseScroll();
        mouseDelta = new Vector3f((float) (x - OLD_X), (float) (y - OLD_Y), (float) (wheel - OLD_SCROLL));
        mouseButtons = new Vector2b(isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT), isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT));
        OLD_X = x;
        OLD_Y = y;
        OLD_SCROLL = wheel;
        time.update();
    }

    private static double mouseScroll() {
        return scroll;
    }

    public static void update() {
        prevFrameClicked = isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT);
        for (Key k : Key.values())
            k.setKeyPressedPrevFrame(k.isPressed());
        for (int i = 0; i < mouseLengths.length - 1; i++)
            mouseLengths[i] = mouseLengths[i + 1];
        mouseLengths[mouseLengths.length - 1] = mouseDelta.length();
        lastFramesLengths = 0;
        for (float l : mouseLengths)
            lastFramesLengths += l;
    }

    public static void updateWireFrame() {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, (Key.KEY_W.isPressed() ? GL11.GL_LINE : GL11.GL_FILL)); // Press
        // key 'W'
        // on
        // the
        // keyboard
        // for
        // wire-frame
    }

    public static void sendRequestToMainThread(Request r) {
        ns.parallelComputing.Thread th = ThreadMaster.getThread(MAIN_THREAD_NAME);
        while (th.isExecutingRequests)
            java.lang.Thread.yield();
        th.setToCarryOutRequest(r);
    }

    public static ns.parallelComputing.Thread currentThread() {
        return (ns.parallelComputing.Thread) java.lang.Thread.currentThread();
    }

    public static void setMouseCursor(long cursor) {
        GLFW.glfwSetCursor(DisplayManager.window, cursor);
    }

    public static synchronized void initMouseCursors(MasterRenderer renderer) {
        FBO fbo = new FBO(64, 64, (FBO.COLOR_TEXTURE | FBO.DEPTH_RENDERBUFFER)).create();
        fbo.bind();
        GL11.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        for (int i = 0; i < TOTAL_NUMBER_OF_ENTITIES; i++) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            Blueprint blueprint = BlueprintCreator.createModelBlueprintFor(Integer.toString(1000 + i))
                    .withDefaultCustomColors();
            boolean shouldScale = blueprint.getModel().shouldScale();
            renderer.render(blueprint, new Vector3f(0f, shouldScale ? -1f : -3f, shouldScale ? -6.6f : -20f));
            textures.add(i, fbo.getTex());
            textureBuffers.add(fbo.getTex().getAsByteBufferForGLFW());
            fbo.createNewTexture();
        }
        fbo.setTextureNull();
        FBO.unbind();
        fbo.delete();
    }

    public static String getGL20Type(int type) {
        Field[] fields = GL20.class.getFields();
        for (Field field : fields) {
            if (field.getType() == int.class) {
                try {
                    int i = (int) field.get(null);
                    if (i == type) {
                        return field.getName();
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static boolean hasVaoCreateRequestInMainThread(VAO vao) {
        for (CreateVAORequest req : currentThread().vaoCreateRequests)
            if (req.vao == vao) {
                return true;
            }
        return false;
    }

    public static String getKHR_DEBUG_CALLBACK_FIELD(int type) {
        Field[] fields = KHRDebug.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == int.class) {
                try {
                    int i = (int) field.get(null);
                    if (i == type) {
                        return field.getName();
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static long createCursor(int xHotspot, int yHotspot, ByteBuffer textures) {
        GLFWImage image = GLFWImage.create().pixels(textures).width(64).height(64);
        return GLFW.glfwCreateCursor(image, xHotspot, yHotspot);
    }

    public static ByteBuffer getMouseTexture(String modelFolder, int sub) {
        int idx = Integer.parseInt(modelFolder) - 1000;
        return textures.get(idx).getAsByteBufferForGLFW(sub, textureBuffers.get(idx));
    }

    public static long createCursor(int xHotspot, int yHotspot, ByteBuffer textures, int sub) {
        GLFWImage image = GLFWImage.create().pixels(textures).width(64 - sub).height(64 - sub);
        return GLFW.glfwCreateCursor(image, xHotspot, yHotspot);
    }

    public static int binaryInt(String string) {
        string = string.replaceAll(" ", "");
        return Integer.parseInt(string, 2);
    }

    public static int sizeof(int gl_type) {
        if (gl_type == GL11.GL_FLOAT)
            return 4;
        if (gl_type == GL11.GL_BYTE)
            return 1;
        if (gl_type == GL11.GL_INT)
            return 4;
        return 0;
    }

    public static FloatBuffer storeDataInFloatBuffer(float[] data) {
        return BufferUtils.createFloatBuffer(data.length).put(data).flip();
    }

    public static IntBuffer storeDataInIntBuffer(int[] data) {
        return BufferUtils.createIntBuffer(data.length).put(data).flip();
    }

    public static ByteBuffer storeDataInByteBuffer(byte[] data) {
        return BufferUtils.createByteBuffer(data.length).put(data).flip();
    }

    public static Vector2f normalizedMousePos() {
        double normalizedX = -1.0 + 2.0 * mouseX() / WIDTH;
        double normalizedY = (1.0 - 2.0 * mouseY() / HEIGHT);
        return new Vector2f((float) normalizedX, (float) normalizedY);
    }

    public static void setZ003(FontType font) {
        Z003 = font;
    }

    public static float clamp(float val, float min, float max) {
        if (min > max)
            return clamp(val, max, min);

        if (val < min)
            return min;
        else if (val > max)
            return max;
        return val;
    }

    public static void setCaladea(FontType caladea) {
        GU.caladea = caladea;
    }

    public static Matrix4f creteProjection(float FOV, float NEAR_PLANE, float FAR_PLANE) {
        Matrix4f projectionMatrix = new Matrix4f();
        float aspectRatio = (float) DisplayManager.WIDTH / (float) DisplayManager.HEIGHT;

        float y_scale = 1f / (float) Math.tan(FOV / 2f);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;

        return projectionMatrix;
    }

    public static String getGLErrorType(int err) {
        Field[] fields = GL11.class.getFields();
        for (Field field : fields) {
            if (field.getType() == int.class) {
                try {
                    int i = (int) field.get(null);
                    if (i == err)
                        return field.getName();
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static float readFloat(byte b1, byte b2, byte b3, byte b4) {
        return ByteBuffer.wrap(new byte[]{b1, b2, b3, b4}).order(ByteOrder.BIG_ENDIAN).getFloat();
    }

    public static float readFloat(int i) {
        return ByteBuffer.wrap(getBytes(i)).order(ByteOrder.BIG_ENDIAN).getFloat();
    }

    public static int readInt(byte b1, byte b2, byte b3, byte b4) {
        return ByteBuffer.wrap(new byte[]{b1, b2, b3, b4}).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    public static byte[] getBytes(float f) {
        buffer.clear();
        buffer.putFloat(f);
        return buffer.array();
    }

    public static byte[] getBytes(int i) {
        buffer.clear();
        buffer.putInt(i);
        return buffer.array();
    }

    @SuppressWarnings("exports")
    public static Document getDocument(InputStream in) {
        try {
            return documentBuilder.parse(in);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isStringOfPattern(String string, String pattern) {
        return string.matches(pattern);
    }

    public static Vector3f mix(Vector3f a, Vector3f b, float blend) {
        return new Vector3f(a.x * (1.0f - blend) + b.x * blend, a.y * (1.0f - blend) + b.y * blend,
                a.z * (1.0f - blend) + b.z * blend);
    }

    public static float smoothstep(float edge0, float edge1, float x) {
        // Scale, bias and saturate x to 0..1 range
        x = clamp((x - edge0) / (edge1 - edge0), 0.0f, 1.0f);
        // Evaluate polynomial
        return x * x * (3 - 2f * x);
    }

    public static boolean isButtonDown(int i) {
        return GLFW.glfwGetMouseButton(DisplayManager.window, i) == GLFW.GLFW_PRESS;
    }

    public static double mouseX() {
        double[] xbuf = new double[1];
        double[] ybuf = new double[1];
        GLFW.glfwGetCursorPos(DisplayManager.window, xbuf, ybuf);
        return xbuf[0];
    }

    public static double mouseY() {
        double[] xbuf = new double[1];
        double[] ybuf = new double[1];
        GLFW.glfwGetCursorPos(DisplayManager.window, xbuf, ybuf);
        return ybuf[0];
    }

    public static long defaultCursor() {
        return (defaultC == 0 ? (defaultC = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)) : defaultC);
    }

    public static void scroll_callback(long w, double x, double y) {
        scroll = y * 100 + scroll;
    }

    public enum Key {
        KEY_S(GLFW.GLFW_KEY_S), KEY_W(GLFW.GLFW_KEY_W), KEY_ESC(GLFW.GLFW_KEY_ESCAPE);

        private final int id;
        private boolean pressedPrevFrame;

        Key(int id) {
            this.id = id;
        }

        boolean isPressed() {
            return GLFW.glfwGetKey(DisplayManager.window, id) == GLFW.GLFW_PRESS;
        }

        public boolean pressedPreviousFrame() {
            return pressedPrevFrame;
        }

        private void setKeyPressedPrevFrame(boolean pressed) {
            pressedPrevFrame = pressed;
        }

        public boolean pressedThisFrame() {
            return isPressed() && !pressedPrevFrame;
        }
    }

    public static class Random {
        private final java.util.Random random = new java.util.Random();

        public int genInt(int max) {
            return random.nextInt(max);
        }

        public float genFloat() {
            return random.nextFloat();
        }

        public void setSeed(long seed) {
            random.setSeed(seed);
        }

        public double genDouble() {
            return random.nextDouble();
        }
    }
}