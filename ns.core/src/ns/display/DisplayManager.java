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

package ns.display;

import data.GameData;
import ns.configuration.Config;
import ns.configuration.GameConfig;
import ns.utils.GU;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.vector.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    public static float time_rate = 1 / 1000f;
    public static long window;
    private static long lastFrameTime;
    private static float delta;
    private static float ingametime;

    public static void createDisplay() {
        BufferedImage icon = null;
        try {
            icon = ImageIO.read(GameData.getResourceAt("textures/ns_icon.png").asInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (GameConfig.getConfig(GameConfig.FULLSCREEN) == Config.TRUE) {
        }
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        // Create the window
        window = glfwCreateWindow(GU.WIDTH, GU.HEIGHT, "Nature Simulator", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        glfwSetScrollCallback(window, GU::scroll_callback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
        lastFrameTime = getCurrentTime();
    }

    public static void updateDisplay() {
        glfwSwapBuffers(window);
        glfwPollEvents();
        long currentTime = getCurrentTime();
        delta = (currentTime - lastFrameTime) / 1000f;
        ingametime = (currentTime - lastFrameTime) * time_rate;
        lastFrameTime = currentTime;
        Vector2f x = GU.normalizedMousePos();
    }

    public static void closeDisplay() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static float getInGameTimeSeconds() {
        return ingametime;
    }

    public static float getDelta() {
        return delta;
    }

    public static boolean isCloseRequested() {
        return glfwWindowShouldClose(window);
    }

    private static ByteBuffer loadIconInstance(BufferedImage image, int dimension) {
        BufferedImage scaledIcon = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledIcon.createGraphics();
        double ratio;
        if (image.getWidth() > scaledIcon.getWidth()) {
            ratio = (double) (scaledIcon.getWidth()) / image.getWidth();
        } else {
            ratio = scaledIcon.getWidth() / (double) image.getWidth();
        }
        if (image.getHeight() > scaledIcon.getHeight()) {
            double r2 = (double) (scaledIcon.getHeight()) / image.getHeight();
            if (r2 < ratio) {
                ratio = r2;
            }
        } else {
            double r2 = (scaledIcon.getHeight() / (double) image.getHeight());
            if (r2 < ratio) {
                ratio = r2;
            }
        }
        double width = image.getWidth() * ratio;
        double height = image.getHeight() * ratio;
        g.drawImage(image, (int) ((scaledIcon.getWidth() - width) / 2), (int) ((scaledIcon.getHeight() - height) / 2),
                (int) (width), (int) (height), null);
        g.dispose();

        byte[] imageBuffer = new byte[dimension * dimension * 4];
        int counter = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int colorSpace = scaledIcon.getRGB(j, i);
                imageBuffer[counter] = (byte) ((colorSpace >> 16) & 0xFF);
                imageBuffer[counter + 1] = (byte) ((colorSpace >> 8) & 0xFF);
                imageBuffer[counter + 2] = (byte) (colorSpace & 0xFF);
                imageBuffer[counter + 3] = (byte) (colorSpace >> 24);
                counter += 4;
            }
        }
        return ByteBuffer.wrap(imageBuffer);
    }

    public static void time_rate(float time_rate) {
        DisplayManager.time_rate = time_rate / 1000f;
    }
}