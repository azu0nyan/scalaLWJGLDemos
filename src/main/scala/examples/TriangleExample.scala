package examples

import lwjglutils.LwjglApp
import org.lwjgl._
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glDrawArrays
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30.{glBindVertexArray, glGenVertexArrays}

import java.nio.{FloatBuffer, IntBuffer}
import scala.io.Source

object TriangleExample extends LwjglApp {
  val vertices: Array[Float] = Array(
    -0.5f, -0.5f, 0.0f,
    0.5f, -0.5f, 0.0f,
    0.0f, 0.5f, 0.0f
  )

  var shaderProgram = 0
  var vao = 0
  var vbo = 0

  override def userInit(): Unit = {
    vao = glGenVertexArrays()
    vbo = glGenBuffers()

    glBindVertexArray(vao)

    // 2. copy our vertices array in a buffer for OpenGL to use
    glBindBuffer(GL_ARRAY_BUFFER, vbo)

    // 3. then set our vertex attributes pointers
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
    glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * 4, 0)
    glEnableVertexAttribArray(0)

//    glBindBuffer(GL_ARRAY_BUFFER, 0)




    //    val ip: IntBuffer = IntBuffer.allocate(1)


    //    val floatBuffer: FloatBuffer = BufferUtils.createFloatBuffer(vertices.size)
    //    floatBuffer.put(vertices)
    //    glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW)


    val vertexShader: Int = glCreateShader(GL_VERTEX_SHADER)
    val vertexShaderString: String = Source.fromResource("shaders/triangleExample.vert").getLines().mkString("\n")

    {
      glShaderSource(vertexShader, vertexShaderString)
      glCompileShader(vertexShader)

      val success: Array[Int] = Array(0)

      glGetShaderiv(vertexShader, GL_COMPILE_STATUS, success);
      if (success(0) == 0) {
        val msg = glGetShaderInfoLog(vertexShader, 512)
        println(s"Error compiling shaders: $msg")
        System.exit(0)
      } else {
        val msg = glGetShaderInfoLog(vertexShader, 512)
        println(s"Successfully compiled: $msg")
      }
    }

    val fragmentShader: Int = glCreateShader(GL_FRAGMENT_SHADER)
    val fragmentShaderString: String = Source.fromResource("shaders/triangleExample.frag").getLines().mkString("\n")

    {
      glShaderSource(fragmentShader, fragmentShaderString)
      glCompileShader(fragmentShader)

      val success: Array[Int] = Array(0)

      glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, success);
      if (success(0) == 0) {
        val msg = glGetShaderInfoLog(fragmentShader, 512)
        println(s"Error compiling shaders: $msg")
        System.exit(0)
      } else {
        val msg = glGetShaderInfoLog(fragmentShader, 512)
        println(s"Successfully compiled: $msg")
      }
    }


    shaderProgram = glCreateProgram()

    glAttachShader(shaderProgram, vertexShader)
    glAttachShader(shaderProgram, fragmentShader)
    glLinkProgram(shaderProgram)

    {
      val success: Array[Int] = Array(0)
      glGetProgramiv(shaderProgram, GL_LINK_STATUS, success);
      if (success(0) == 0) {
        val res = glGetProgramInfoLog(shaderProgram);
        println(s"Error linking program $res")
      }
    }


  }

  override def drawCall(): Unit = {

    // 2. use our shader program when we want to render an object

    glUseProgram(shaderProgram)

//    glBindVertexArray(vao)
    glDrawArrays(GL11.GL_TRIANGLES, 0, 3)


  }

}
