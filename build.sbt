name := "sbt-gluon-plugin"

organization := "com.github.gurinderu"

resolvers += "Gluon Snapshots" at "https://nexus.gluonhq.com/nexus/content/repositories/public-snapshots"
resolvers += "Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

version := "0.1"

scalaVersion := "2.12.19"

enablePlugins(SbtPlugin)

libraryDependencies += "com.gluonhq" % "substrate" % "0.0.62-SNAPSHOT"
