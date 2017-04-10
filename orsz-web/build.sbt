name := """orsz-web"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

/******* Play plugins ********/
libraryDependencies ++= Seq(jdbc,
                            cache,
                            ws,
                            "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test)

/******* Util plugins ********/
libraryDependencies ++= Seq("com.github.nscala-time" %% "nscala-time" % "2.16.0",
                            "org.scala-lang.modules" %% "scala-async" % "0.9.6")

/******* Persistence plugins ********/
libraryDependencies ++= Seq("com.datastax.cassandra" % "cassandra-driver-core" % "3.1.4")
