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

object GluonPlugin extends AutoPlugin {
  val defaultJavaStaticSdkVersion = "18-ea+prep18-9"
  val defaultJavafxStaticSdkVersion = "21-ea+11.2"

  object autoImport extends GluonTasks with GluonKeys {}

  import autoImport._

  def clientConfig(): Def.Initialize[Task[ProjectConfiguration]] = Def.task {
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
    clientConfig.setAppName(name.value)
    clientConfig.setVerbose(true)
    clientConfig.setUsePrecompiledCode(true)
    clientConfig.setCompilerArgs(nativeImageArgs.value.asJava)
    clientConfig.setJavaStaticSdkVersion(defaultJavaStaticSdkVersion)
    clientConfig.setJavafxStaticSdkVersion(defaultJavafxStaticSdkVersion)
    clientConfig
  }

  override val projectSettings = Seq(
    gluonCompile := {
      val directory = baseDirectory.value.toPath
      val dispatcher = new SubstrateDispatcher(directory, clientConfig().value)
      val result = dispatcher.nativeCompile()
      require(result, "Compilation failed.")
    },
    gluonLink := {
      val directory = baseDirectory.value.toPath
      val dispatcher = new SubstrateDispatcher(directory, clientConfig().value)
      val result = dispatcher.nativeLink()
      require(result, "Linking failed.")
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
      require(code == 0, "run agent failed")
    },
    gluonBuild := Def.sequential(gluonCompile, gluonLink).value,
    nativeImageArgs := Seq(
      "--no-fallback",
      "-H:ConfigurationFileDirectories=../../../native-agent"
    ),
    nativeAgentDir := "native-agent"
  )
}
