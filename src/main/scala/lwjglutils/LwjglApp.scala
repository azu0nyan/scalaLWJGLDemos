package lwjglutils

import org.lwjgl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._


class LwjglApp(windowTitle: String = "LWJGL application", windowWidth: Int = 300, windowHeight: Int = 300) {

  def main(args: Array[String]): Unit = run()

  private var window = 0L

  def run(): Unit = {
    System.out.println("Hello LWJGL " + Version.getVersion + "!")
    init()
    loop()
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)
    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }


  private def init(): Unit = { // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set
    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit) throw new IllegalStateException("Unable to initialize GLFW")
    // Configure GLFW
    glfwDefaultWindowHints() // optional, the current window hints are already the default

    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation

    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

    // Create the window
    window = glfwCreateWindow(windowWidth, windowHeight, windowTitle, NULL, NULL)
    if (window == NULL) throw new RuntimeException("Failed to create the GLFW window")
    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, keyPressed)
    glfwSetMouseButtonCallback(window, mouseKeyPressed)

    // Get the thread stack and push a new frame
    val stack = stackPush
    try {
      val pWidth = stack.mallocInt(1) // int*
      val pHeight = stack.mallocInt(1)
      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight)
      // Get the resolution of the primary monitor
      val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor)
      // Center the window
      glfwSetWindowPos(window, (vidmode.width - pWidth.get(0)) / 2, (vidmode.height - pHeight.get(0)) / 2)
    } finally if (stack != null) stack.close()
    // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(window)
    // Enable v-sync
    glfwSwapInterval(1)
    // Make the window visible
    glfwShowWindow(window)


  }

  private def loop(): Unit = { // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities
    // Set the clear color
    glClearColor(0.2f, 0.7f, 0.7f, 0.0f)


    userInit()
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (!glfwWindowShouldClose(window)) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer


      drawCall()

      glfwSwapBuffers(window) // swap the color buffers

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents()
    }
  }


  def keyPressed(window: Long, key: Int, scancode: Int, action: Int, mods: Int): Unit = {
    println(key)
    (key, action, mods) match {
      case (GLFW_KEY_ESCAPE, GLFW_RELEASE, _) => glfwSetWindowShouldClose(window, true)
      case _ => userKeyPressed(key, scancode, action, mods)
    }
  }


  /** Override */
  def userInit(): Unit = {}

  /** Override */
  def drawCall(): Unit = {}

  /** Override */
  def userKeyPressed(key: Int, scancode: Int, action: Int, mods: Int): Unit = {}


  def mousePosition: (Double, Double) = {
    import org.lwjgl.BufferUtils
    import java.nio.DoubleBuffer
    val xBuffer = BufferUtils.createDoubleBuffer(1)
    val yBuffer = BufferUtils.createDoubleBuffer(1)
    glfwGetCursorPos(window, xBuffer, yBuffer)
    val x = xBuffer.get(0)
    val y = yBuffer.get(0)
    (x, y)
  }

  def mouseKeyPressed: GLFWMouseButtonCallback = new GLFWMouseButtonCallback() {
    override def invoke(window: Long, button: Int, action: Int, mods: Int): Unit = {
      userMousePressed(button, action, mods)
    }
  }

  def userMousePressed(button: Long, action: Int, mods: Int): Unit = {
    println(mousePosition)
  }


}
