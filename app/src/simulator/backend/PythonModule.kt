package simulator.backend

import simulator.model.Socket

class PythonModule {

  private val mcuProcess: Process
  private val socket = Socket()
  private val path = "backend-sim-core/"

  init {
      this.mcuProcess = Runtime.getRuntime().exec("python2.7 ${path}sim.py ")
  }

  fun stopProcExec() {
    mcuProcess.destroy()
  }

  fun uploadFirmware(mcuName: String, sketchPath: String): String {
      return this.socket.sendMsg("$mcuName upload $sketchPath")
  }

  fun sendMsg(newMsg: String) = socket.sendMsg(newMsg)
}
