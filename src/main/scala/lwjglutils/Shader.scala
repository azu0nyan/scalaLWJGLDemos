package lwjglutils

import examples.ShaderExample.shaderProgram
import org.lwjgl.opengl.GL20.{GL_COMPILE_STATUS, GL_FRAGMENT_SHADER, GL_LINK_STATUS, GL_VERTEX_SHADER, glAttachShader, glCompileShader, glCreateProgram, glCreateShader, glDeleteShader, glGetProgramInfoLog, glGetProgramiv, glGetShaderInfoLog, glGetShaderiv, glGetUniformLocation, glLinkProgram, glShaderSource, glUniform1f, glUniform1i, glUseProgram}

import scala.io.Source

class Shader(
            vertexPath:String,
            fragmentPath:String,
            ) {
  val vertexShader: Int = glCreateShader(GL_VERTEX_SHADER)
  val vertexShaderString: String = Source.fromResource(vertexPath).getLines().mkString("\n")

  {
    glShaderSource(vertexShader, vertexShaderString)
    glCompileShader(vertexShader)

    val success: Array[Int] = Array(0)

    glGetShaderiv(vertexShader, GL_COMPILE_STATUS, success);
    if (success(0) == 0) {
      val msg = glGetShaderInfoLog(vertexShader, 512)
      throw new Exception(s"Error compiling shaders: $msg")
      System.exit(0)
    } else {
      val msg = glGetShaderInfoLog(vertexShader, 512)
      println(s"Successfully compiled: $msg")
    }
  }

  val fragmentShader: Int = glCreateShader(GL_FRAGMENT_SHADER)
  val fragmentShaderString: String = Source.fromResource(fragmentPath).getLines().mkString("\n")

  {
    glShaderSource(fragmentShader, fragmentShaderString)
    glCompileShader(fragmentShader)

    val success: Array[Int] = Array(0)

    glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, success);
    if (success(0) == 0) {
      val msg = glGetShaderInfoLog(fragmentShader, 512)
      throw new Exception(s"Error compiling shaders: $msg")
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
      throw new Exception(s"Error linking program $res")
    }
  }

  // delete the shaders as they're linked into our program now and no longer necessary
  glDeleteShader(vertexShader)
  glDeleteShader(fragmentShader)





  def use():Unit = {
    glUseProgram(shaderProgram)
  }

  def setBool(name:String,value:Boolean):Unit = {
    glUniform1i(glGetUniformLocation(shaderProgram, name), if(value) 1 else 0)
  }

  def setInt(name:String,value:Int):Unit = {
    glUniform1i(glGetUniformLocation(shaderProgram, name), value)
  }

  def setFloat(name:String,value:Float):Unit = {
    glUniform1f(glGetUniformLocation(shaderProgram, name), value)
  }

}
