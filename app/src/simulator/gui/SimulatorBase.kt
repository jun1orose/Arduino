package simulator.gui

import simulator.backend.PythonModule
import java.io.File

class SimulatorBase  {

  companion object {
    fun createSimulatorEditor() {
      Editor.createSimulator()
    }

    fun uploadAndRun(buildPath: String, sketchName: String): String {

      if (!Editor.isSimulatorExist()) {
        return "Simulator not active!"
      }

      try {

        val firmwareSrc = File("$buildPath/$sketchName.ino.elf")
        if(!firmwareSrc.exists()) {
          return "$sketchName.ino.elf doesn't exist!"
        }

        val firmwareDest = File("backend-sim-core/${firmwareSrc.name}")
        if (firmwareDest.exists()) {
          firmwareDest.delete()
          firmwareSrc.copyTo(firmwareDest)
        }

        val mcu = CircuitComponent.getMCU().getCore()

        PythonModule.initTable(mcu)
        val status = PythonModule.uploadFirmwareAndRun(mcu, "$sketchName.ino.elf")

        return status

      } catch (e: Exception) {
        e.printStackTrace()
        return "Upload error!"
      }
    }
  }
}
