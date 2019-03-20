package emulator.backend

import emulator.core.MCU
import java.io.File

class PythonModule {

  private object Executor { var INSTANCE: Process? = null}

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
        Executor.INSTANCE = Runtime.getRuntime().exec("python2.7 ${path}sim.py $mcuName")

      }
    }

    fun isProcAlive() = Executor.INSTANCE?.isAlive ?: false
  }
}
