name := "sbt-gluon-plugin"
organization := "io.github.thinkiny"
version := "0.1.1"
scalaVersion := "2.12.19"

resolvers += "Gluon Snapshots" at "https://nexus.gluonhq.com/nexus/content/repositories/public-snapshots"
resolvers += "Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

enablePlugins(SbtPlugin, AssemblyPlugin)

libraryDependencies += "com.gluonhq" % "substrate" % "0.0.62-SNAPSHOT"

// publish
homepage := Some(url("https://github.com/thinkiny/sbt-gluon-plugin"))
description := "Gluon Client plugin for SBT"
versionScheme := Some("early-semver")
scmInfo := Some(
  ScmInfo(
    url("https://github.com/thinkiny/sbt-gluon-plugin"),
    "scm:git@github.com:thinkiny/sbt-gluon-plugin.git"
  )
)

licenses := List(
  "Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")
)

developers := List(
  Developer(
    id = "001",
    name = "Aaron An",
    email = "thinkiny@gmail.com",
    url = url("https://github.com/thinkiny/sbt-gluon-plugin")
  )
)
