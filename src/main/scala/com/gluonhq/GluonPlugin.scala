package com.gluonhq

import java.io.File
import java.nio.file.Paths

import com.gluonhq.substrate.model.Triplet
import com.gluonhq.substrate.{ProjectConfiguration, SubstrateDispatcher}
import sbt.Keys._
import sbt.{Def, _}

import scala.collection.JavaConverters._
import com.gluonhq.substrate.util.Strings
import sbtassembly.AssemblyKeys
import com.gluonhq.substrate.model.ReleaseConfiguration

object GluonPlugin extends AutoPlugin {
  val defaultJavaStaticSdkVersion = "24-1"
  val defaultJavafxStaticSdkVersion = "24-ea+7.1"

  object autoImport extends GluonTasks with GluonKeys {}

  import autoImport._

  def makeDispatcher(): Def.Initialize[Task[SubstrateDispatcher]] = Def.task {
    val mainClassValue = (Compile / mainClass).value.get
    val classPath = (Compile / fullClasspath).value
      .map(_.data)
      .map(_.getAbsolutePath)
      .mkString(File.pathSeparator)

    val clientConfig =
      new ProjectConfiguration(
        mainClassValue,
        classPath
      )

    clientConfig.setGraalPath(Paths.get(System.getenv("GRAALVM_HOME")))
    clientConfig.setTarget(Triplet.fromCurrentOS)
    clientConfig.setReflectionList(
      Strings.split(System.getProperty("reflectionlist"))
    )
    clientConfig.setJniList(Strings.split(System.getProperty("jnilist")))
    clientConfig.setBundlesList(
      Strings.split(System.getProperty("bundleslist"))
    )
    clientConfig.setAppId(organization.value + "." + name.value)
    clientConfig.setAppName(gluonAppName.value)
    clientConfig.setUsePrecompiledCode(true)
    clientConfig.setVerbose(gluonVerbose.value)
    clientConfig.setCompilerArgs(nativeImageArgs.value.asJava)
    clientConfig.setLinkerArgs(nativeLinkerArgs.value.asJava)
    clientConfig.setJavaStaticSdkVersion(defaultJavaStaticSdkVersion)
    clientConfig.setJavafxStaticSdkVersion(defaultJavafxStaticSdkVersion)

    val releaseConfig = clientConfig.getReleaseConfiguration()
    releaseConfig.setMacAppStore(macAppStore.value)
    releaseConfig.setMacAppCategory(macAppCategory.value)
    if (!macAppStore.value) {
      releaseConfig.setSkipSigning(true)
    }
    releaseConfig.setMacSigningUserName(macSigningUserName.value)
    val directory = target.value.toPath.resolve("gluon")
    new SubstrateDispatcher(directory, clientConfig)
  }

  override val projectSettings = Seq(
    gluonCompile := {
      val dispatcher = makeDispatcher.value
      val result = dispatcher.nativeCompile()
      require(result, "Compilation failed.")
    },
    gluonLink := {
      val dispatcher = makeDispatcher.value
      val result = dispatcher.nativeLink()
      require(result, "Linking failed.")
    },
    gluonPackage := {
      val dispatcher = makeDispatcher.value
      val result = dispatcher.nativePackage()
      require(result, "Package failed.")
    },
    gluonRunAgent := {
      val javaPath = System.getenv("GRAALVM_HOME") + "/bin/java"
      val code =
        RunAgent.run(
          javaPath,
          AssemblyKeys.assembly.value,
          baseDirectory.value,
          nativeAgentDir.value
        )
      require(code == 0, "Run agent failed")
    },
    gluonBuild := Def.sequential(gluonCompile, gluonLink).value,
    gluonRepackage := Def.sequential(gluonBuild, gluonPackage).value,
    nativeLinkerArgs := Seq(),
    nativeImageArgs := Seq(
      "--no-fallback",
      "-Djava.awt.headless=false",
      "-H:ConfigurationFileDirectories=../../../../../native-agent"
    ),
    nativeAgentDir := "native-agent",
    gluonVerbose := false,
    macAppStore := false,
    macSigningUserName := "",
    macAppCategory := ReleaseConfiguration.DEFAULT_MAC_APP_CATEGORY,
    gluonAppName := name.value
  )
}
