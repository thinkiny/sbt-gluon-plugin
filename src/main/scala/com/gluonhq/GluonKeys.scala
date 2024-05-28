package com.gluonhq

import sbt.settingKey
import java.io.File

trait GluonKeys {
  lazy val nativeImageArgs = settingKey[Seq[String]]("Native image args")
  lazy val nativeAgentDir = settingKey[String]("Native agent config dir")
  lazy val gluonVerbose = settingKey[Boolean]("Show more logs")
  lazy val gluonAppName = settingKey[String]("App name")

  lazy val macAppStore =
    settingKey[Boolean](
      "If the macOS bundle is intended for the Mac App Store"
    )
  lazy val macSigningUserName =
    settingKey[String]("Team or user name portion in Apple signing identities")

  lazy val macAppCategory =
    settingKey[String](
      "The category that best describes the app for the Mac App Store"
    )
}
