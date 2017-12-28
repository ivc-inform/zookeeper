
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

lazy val zookeeper = (project in file("."))
  .aggregate(
      `zookeeper-client`
  )
  .settings(
      inThisBuild(Seq(
          publishMavenStyle := true,
          organization := "com.loopfor.zookeeper",
          version := "1.5-SNAPSHOT",
          description := "Scala API for ZooKeeper",
          homepage := Some(url("https://github.com/davidledwards/zookeeper")),
          licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
          publishTo := {
              val corporateRepo = "http://toucan.simplesys.lan/"
              if (isSnapshot.value)
                  Some("snapshots" at corporateRepo + "artifactory/libs-snapshot-local")
              else
                  Some("releases" at corporateRepo + "artifactory/libs-release-local")
          },
          credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

      )))
  .settings(compilerSettings: _*)

lazy val `zookeeper-client` = (project in file("zookeeper-client")).
  settings(
      name := "zookeeper-client",
      libraryDependencies ++= Seq(
          "org.apache.zookeeper" % "zookeeper" % "3.4.11" exclude("jline", "jline"),
          "org.scalatest" %% "scalatest" % "3.0.4" % Test
      )
  )
