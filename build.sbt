lazy val compilerSettings = Seq(
  scalaVersion := "2.12.4",
  scalacOptions ++= Seq(
    "-target:jvm-1.8",
    "-deprecation",
    "-unchecked",
    "-feature",
    "-encoding", "UTF-8"
  )
)

lazy val dependencySettings = Seq(
  libraryDependencies ++= Seq(
    "org.apache.zookeeper" % "zookeeper" % "3.4.11" exclude("jline", "jline"),
    "org.scalatest" %% "scalatest" % "3.0.4" % Test
  )
)


lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  publishTo := Some(
    if (version.value endsWith "SNAPSHOT")
      "Sonatype Nexus Snapshot Repository" at "https://oss.sonatype.org/content/repositories/snapshots/"
    else
      "Sonatype Nexus Release Repository" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
  )
)

lazy val rootProject = (project in file("zookeeper-client")).
  settings(
    name := "zookeeper-client",
    organization := "com.loopfor.zookeeper",
    version := "1.5-SNAPSHOT",
    description := "Scala API for ZooKeeper",
    homepage := Some(url("https://github.com/davidledwards/zookeeper")),
    licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
  ).
  settings(compilerSettings: _*).
  settings(dependencySettings: _*).
  settings(publishSettings: _*)
