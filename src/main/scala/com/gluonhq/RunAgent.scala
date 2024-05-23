package com.gluonhq

import java.io.File
import scala.util.Using
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import scala.sys.process.Process
import java.nio.file.Path

object RunAgent {
  val AGENTLIB_EXCLUSION_RULES = List(
    "com.sun.glass.ui.mac.*",
    "com.sun.glass.ui.gtk.*",
    "com.sun.glass.ui.win.*",
    "com.sun.prism.es2.*",
    "com.sun.prism.d3d.*",
    "com.sun.scenario.effect.impl.es2.*",
    "com.sun.scenario.effect.impl.hw.d3d.*",
    "com.gluonhq.attach.**"
  )

  def createFilterFile(agentDir: String): Unit = {
    val agentFilter = s"${agentDir}/filter-file.json"
    val agentDirFilter = new File(agentFilter)
    if (agentDirFilter.exists()) {
      agentDirFilter.delete()
    }

    Using(
      new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(agentDirFilter))
      )
    ) { bw =>
      bw.write("{ \"rules\": [\n")
      var ruleHasBeenWritten = false
      AGENTLIB_EXCLUSION_RULES.foreach { rule =>
        if (ruleHasBeenWritten) {
          bw.write(",\n");
        } else {
          ruleHasBeenWritten = true;
        }
        bw.write("    {\"excludeClasses\" : \"" + rule + "\"}");
      }
      bw.write("\n  ]\n")
      bw.write("}\n")
    }
  }

  def run(javaPath: String, jar: File, baseDir: File, agentDir: String): Int = {
    val nativeImageAgentArgs =
      s"-agentlib:native-image-agent=access-filter-file=${agentDir}/filter-file.json,config-merge-dir=${agentDir}";
    val agentDirFile = new File(agentDir)
    if (!agentDirFile.exists()) {
      agentDirFile.mkdirs()
    }
    createFilterFile(agentDir)
    Process(
      s"${javaPath} ${nativeImageAgentArgs} -jar ${jar}",
      cwd = baseDir
    ).!
  }
}
