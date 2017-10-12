lazy val sparkStructuredStreamingExample = project
  .copy(id = "spark-structured-streaming-example")
  .in(file("."))
  .enablePlugins(AutomateHeaderPlugin, GitVersioning)

name := "spark-structured-streaming-example"

libraryDependencies ++= Vector(
  Library.scalaCheck % "test",
  Library.spark,
  Library.sparkStreaming,
  Library.sparkSQL,
  Library.sparkKafka,
  Library.sparkSQLKafka
)

initialCommands := """|import de.codecentric.spark.structured.streaming.example._
                      |""".stripMargin


mergeStrategy in assembly := {
  case PathList("META-INF", "ECLIPSEF.RSA") => MergeStrategy.discard
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

scalaVersion := Version.Scala
