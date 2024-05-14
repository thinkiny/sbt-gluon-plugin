package com.gluonhq

import sbt.settingKey

trait GluonKeys {
  lazy val nativeImageArgs = settingKey[Seq[String]]("Native image args")
  lazy val javaFxOsName = settingKey[String]("OS Name")
}
