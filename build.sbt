name := """openlegal-markdown"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.parboiled" %% "parboiled" % "2.1.3",
  "org.yaml" % "snakeyaml" % "1.17",
  "org.scalatest" %% "scalatest" % "3.0.0-RC1" % "test"
)


fork in run := true