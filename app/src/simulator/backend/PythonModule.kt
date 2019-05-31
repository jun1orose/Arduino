package simulator.backend

import simulator.gui.Editor


class PythonModule(editor: Editor) {

  private val socket = Socket(editor)
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

  @Synchronized fun sendMsg(newMsg: String) = socket.sendMsg(newMsg)

  fun closeSocket() = this.socket.closeSocket()

  fun getQueue() = this.socket.getQueue()
}
