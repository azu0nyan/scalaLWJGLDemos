name := "scalaLWJGL"

version := "0.1"

scalaVersion := "2.13.4"


libraryDependencies ++= {
  val version = "3.2.3"
  val os = "linux" // "windows"

  Seq(
    "lwjgl",
    "lwjgl-glfw",
    "lwjgl-opengl",
    "lwjgl-stb",

  ).flatMap {
    module => {
      Seq(
        "org.lwjgl" % module % version,
        "org.lwjgl" % module % version classifier s"natives-$os"
      )
    }
  }
}
