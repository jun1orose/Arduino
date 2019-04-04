package simulator.backend

import simulator.model.core.MCU
import java.io.File

class PythonModule {

  private object Executor { var INSTANCE: Process? = null}
  private object Path { const val INSTANCE = "backend-sim-core/"}

  companion object {

    fun initTable(chosenMCU: MCU) {

      val pins = chosenMCU.getPins().filter { it.getValue() != null }
      val mcuName = chosenMCU.getName()

      if (File(Path.INSTANCE + mcuName).isFile) {
        File(Path.INSTANCE + mcuName).delete()
      }

      val pinsTable = pins
        .filter { it.name[1].isDigit() }
        .map { Triple(it.name[0], it.name.substring(1), it.getValue()) }

      pinsTable.forEach {
        File(Path.INSTANCE + mcuName).appendText("${it.first} ${it.second} ${it.third}\n")
      }
    }

    fun uploadFirmwareAndRun(chosenMCU: MCU, firmwareName: String = ""): String {
      val mcuName = chosenMCU.getName()

      return if (File(Path.INSTANCE + mcuName).isFile) {
        Executor.INSTANCE = Runtime.getRuntime().exec("python2.7 ${Path.INSTANCE}sim.py " +
          "${Path.INSTANCE}$mcuName ${Path.INSTANCE}$firmwareName")

        /*
        For testing purposes only

        val exec = Executor.INSTANCE
        val reader = BufferedReader(InputStreamReader(exec?.errorStream))
        for(line in reader.lines()) {
          System.out.println(line)
        }

        */

        "Successful uploading!"
      } else {
        "Error while uploading: input pin's table doesn't exist!"
      }
    }

    fun isProcAlive() = Executor.INSTANCE?.isAlive ?: false

    fun stopExec() {
      Executor.INSTANCE?.destroy()
    }

    fun getPath() = Path.INSTANCE
  }
}
