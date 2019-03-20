package emulator.backend

import emulator.core.MCU
import java.io.File

class PythonModule {
  companion object {

    const val path = "./app/src/emulator/backend/"

    fun initTable(chosenMCU: MCU) {
      val pins = chosenMCU.getPins()
      val mcuName = chosenMCU.getName()

      if (File(path + mcuName).isFile) {
        File(path + mcuName).delete()
      }

      pins.forEach {
        File(path + mcuName).appendText("${it.name} ${it.value}\n")
      }
    }

    fun execSim(chosenMCU: MCU) {
      val mcuName = chosenMCU.getName()

      if (File(path + mcuName).isFile) {
        val runtime = Runtime.getRuntime().exec("python2.7 ${path}sim.py $mcuName")

      }
    }
  }
}
