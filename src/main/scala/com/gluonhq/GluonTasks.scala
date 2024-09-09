package com.gluonhq
import sbt._, Keys._

trait GluonTasks {
  lazy val gluonCompile = taskKey[Unit]("Compile gluon")
  lazy val gluonLink = taskKey[Unit]("Link gluon")
  lazy val gluonPackage = taskKey[Unit]("Package gluon")
  lazy val gluonBuild = taskKey[Unit]("Build gluon")
  lazy val gluonRepackage = taskKey[Unit]("Repackage gluon")
  lazy val gluonRunAgent = taskKey[Unit]("Run agent")
}
