Global / onChangedBuildSource := ReloadOnSourceChanges

name := "sbt-gluon-plugin"
organization := "io.github.thinkiny"
version := "0.3.4"
resolvers += "Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

enablePlugins(SbtPlugin)

libraryDependencies += "com.gluonhq" % "substrate" % "0.0.63"

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.2.0")

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

sonatypeCredentialHost := Sonatype.sonatypeCentralHost
publishTo := sonatypePublishToBundle.value
publishMavenStyle := true
