name := "hack-the-tower-reactive"

version := "1.0"

scalaVersion := "2.11.6"

name := "Hack the tower. Sample project"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.typesafe.akka" %% "akka-http-scala-experimental" % "1.0-RC2",
  "com.typesafe.akka" %% "akka-http-core-experimental" % "1.0-RC2",
  "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-RC2",
  "com.typesafe.akka" %% "akka-http-testkit-scala-experimental" % "1.0-RC2",
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.scala-lang.modules" %% "scala-async" % "0.9.2",
  "com.h2database" % "h2" % "1.3.148"

)