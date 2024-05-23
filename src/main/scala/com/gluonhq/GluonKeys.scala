package com.gluonhq

import sbt.settingKey
import java.io.File

trait GluonKeys {
  lazy val nativeImageArgs = settingKey[Seq[String]]("Native image args")
  lazy val nativeAgentDir = settingKey[String]("Native agent config dir")
}
