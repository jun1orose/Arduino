package simulator.backend

class PythonModule {

  private val socket = Socket()
  private val mcuProcess: Process
  private val path = "backend-sim-core/"

  init {
    this.mcuProcess = Runtime.getRuntime().exec("python2.7 ${path}sim.py ")
    this.socket.start()
  }

  fun stopProcExec() {
    mcuProcess.destroy()
  }

  fun uploadFirmware(mcuName: String, sketchPath: String): String {
    return this.socket.sendMsg("upload $mcuName $sketchPath")
  }

  fun sendMsg(newMsg: String) = socket.sendMsg(newMsg)

  fun getMsgQueue() = this.socket.getMsgQueue()

  fun closeSocket() = this.socket.closeSocket()
}
