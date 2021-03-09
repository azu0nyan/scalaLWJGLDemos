package examples

import lwjglutils.{LwjglApp, Shader}
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glDrawArrays
import org.lwjgl.opengl.GL15.{GL_ARRAY_BUFFER, GL_STATIC_DRAW, glBindBuffer, glBufferData, glGenBuffers}
import org.lwjgl.opengl.GL20.{glEnableVertexAttribArray, glVertexAttribPointer}
import org.lwjgl.opengl.GL30.{glBindVertexArray, glGenVertexArrays}

object GradientExample extends LwjglApp(windowWidth = 800, windowHeight = 600) {
  val vertices: Array[Float] = Array(
    // first triangle
    0.5f,  0.5f, 0.0f,  // top right
    0.5f, -0.5f, 0.0f,  // bottom right
    -0.5f,  0.5f, 0.0f,  // top left
    // second triangle
    -0.5f, -0.5f, 0.0f,  // bottom left
  )

  var vao1: Int = _

  var vbo1: Int = _

  var shader: Shader = _
  override def userInit(): Unit = {
    vao1 = glGenVertexArrays()
    vbo1 = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vbo1)
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
    glBindVertexArray(vao1)
    glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * 4, 0)
    glEnableVertexAttribArray(0)

    shader = new Shader("shaders/gradientExample.vert", "shaders/gradientExample.frag")
  }


  override def drawCall(): Unit = {
    shader.use()
    glBindVertexArray(vao1)
    glEnableVertexAttribArray(0)

    glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 6)
//    glDrawArrays(GL11.GL_TRIANGLES, 3, 3)


  }
}
