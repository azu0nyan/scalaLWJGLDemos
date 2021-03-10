package examples


import lwjglutils.LwjglApp
import org.lwjgl._
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.{glDrawArrays, glDrawElements}
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30.{glBindVertexArray, glGenVertexArrays}

import java.nio.{FloatBuffer, IntBuffer}
import scala.io.Source

object SquareEBOExample extends LwjglApp {
  val vertices: Array[Float] = Array(
    0.5f,  0.5f, 0.0f,  // top right
    0.5f, -0.5f, 0.0f,  // bottom right
    -0.5f, -0.5f, 0.0f,  // bottom left
    -0.5f,  0.5f, 0.0f   // top left
  )

  val indices: Array[Int] = Array( // note that we start from 0!
    0, 1, 3, // first triangle
    1, 2, 3) // second triangle)

  var shaderProgram = 0
  var ebo = 0
  var vbo = 0
  var vao = 0

  override def userInit(): Unit = {
    //create and setup vao
    vao = glGenVertexArrays()
    vbo = glGenBuffers()
    ebo = glGenBuffers()

    //vao
    glBindVertexArray(vao)

    //bind and fill vbo
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

    //bind and fill ebo
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

    //vao stores ebo / vbo state
    glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * 4, 0)
    glEnableVertexAttribArray(0) //enable vao

    //unbind
    // note that this is allowed, the call to glVertexAttribPointer registered VBO as the vertex attribute's bound vertex buffer object so afterwards we can safely unbind
    glBindBuffer(GL_ARRAY_BUFFER, 0)
//    glBindVertexArray(0)


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
    //bind compiled shader
    glUseProgram(shaderProgram)
    //bind configured vao
    glBindVertexArray(vao)
    //draw using bided vao
    glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0)
    //unbind vao
    glBindVertexArray(0)

  }

}

