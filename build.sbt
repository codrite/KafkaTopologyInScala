ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "KafkaDemoInScalaUsingStateStore"
  )

libraryDependencies += "org.apache.kafka" %% "kafka" % "3.2.1"
libraryDependencies += "org.apache.kafka" % "kafka-streams" % "3.2.1"


