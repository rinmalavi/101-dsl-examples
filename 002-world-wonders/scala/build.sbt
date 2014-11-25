name := "002 [Scala] World Wonders"

version := "0.1.0"

scalaVersion := "2.11.4"

//Revolver.settings

lazy val root = project in file(".") enablePlugins PlayScala

libraryDependencies ++= Seq(
  "com.dslplatform" %% "dsl-client-scala" % "0.9.2"
, "ch.qos.logback" % "logback-classic" % "1.1.2"
, "org.scalatest" %% "scalatest" % "2.2.2" % "test"
, "net.databinder.dispatch" %% "dispatch-core"  % "0.11.2"
)

unmanagedResources in Compile += baseDirectory.value / ".." / "wonders.json"

unmanagedResourceDirectories in Compile += baseDirectory.value / ".." / "html"