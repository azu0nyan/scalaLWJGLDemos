package examples

import lwjglutils.LwjglApp
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glDrawArrays
import org.lwjgl.opengl.GL15.{GL_ARRAY_BUFFER, GL_STATIC_DRAW, glBindBuffer, glBufferData, glGenBuffers}
import org.lwjgl.opengl.GL20.{GL_COMPILE_STATUS, GL_FRAGMENT_SHADER, GL_LINK_STATUS, GL_VERTEX_SHADER, glAttachShader, glCompileShader, glCreateProgram, glCreateShader, glEnableVertexAttribArray, glGetProgramInfoLog, glGetProgramiv, glGetShaderInfoLog, glGetShaderiv, glLinkProgram, glShaderSource, glUseProgram, glVertexAttribPointer}
import org.lwjgl.opengl.GL30.{glBindVertexArray, glGenVertexArrays}

import scala.io.Source

object ShaderExample extends LwjglApp {
  val vertices: Array[Float] = Array(
    // positions         // colors
    0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, // bottom right
    -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, // bottom left
    0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f // top
  )

  var shaderProgram = 0
  var vao = 0
  var vbo = 0

  override def userInit(): Unit = {
    vao = glGenVertexArrays()
    vbo = glGenBuffers()


    // 2. copy our vertices array in a buffer for OpenGL to use
    glBindBuffer(GL_ARRAY_BUFFER, vbo)

    // 3. then set our vertex attributes pointers
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

    //using VAO
    glBindVertexArray(vao)
    glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * 4, 0)
    glEnableVertexAttribArray(0)
    glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 6 * 4, 3 * 4)
    glEnableVertexAttribArray(1)


    val vertexShader: Int = glCreateShader(GL_VERTEX_SHADER)
    val vertexShaderString: String = Source.fromResource("shaders/shaderExample.vert").getLines().mkString("\n")

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
    val fragmentShaderString: String = Source.fromResource("shaders/shaderExample.frag").getLines().mkString("\n")

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

    //drawing vao, already bound in init  glBindVertexArray(vao)
    glDrawArrays(GL11.GL_TRIANGLES, 0, 3)


  }

}
