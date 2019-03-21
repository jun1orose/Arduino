package simulator.backend

import simulator.core.MCU
import java.io.File

class PythonModule {

  private object Executor { var INSTANCE: Process? = null}

  companion object {

    const val path = "backend-sim-core/"

    fun initTable(chosenMCU: MCU) {

      val pins = chosenMCU.getPins().filter { it.value != null }
      val mcuName = chosenMCU.getName()

      if (File(path + mcuName).isFile) {
        File(path + mcuName).delete()
      }

      val pinsTable = pins
        .filter { it.name[1].isDigit() }
        .map { Triple(it.name[0], it.name.substring(1), it.value) }

      pinsTable.forEach {
        File(path + mcuName).appendText("${it.first} ${it.second} ${it.third}\n")
      }
    }

    fun execSim(chosenMCU: MCU) {
      val mcuName = chosenMCU.getName()

      if (File(path + mcuName).isFile) {
        Executor.INSTANCE = Runtime.getRuntime().exec("python2.7 " + path + "sim.py " + mcuName)
      }
    }

    fun isProcAlive() = Executor.INSTANCE?.isAlive ?: false

    fun stopExec() {
      Executor.INSTANCE?.destroy()
    }
  }
}
