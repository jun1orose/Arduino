package simulator.backend

import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import simulator.gui.Editor
import simulator.model.Model
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class Socket(editor: Editor): Thread() {

  private val port = 5555
  private val model: Model
  private val socket: ZMQ.Socket
  private val context: ZContext = ZContext()
  private val msgQueue: BlockingQueue<String> = LinkedBlockingQueue<String>()

  init {
    this.socket = context.createSocket(SocketType.PAIR)
    this.socket.receiveTimeOut = 5000
    this.model = editor.getModel()
    Thread.sleep(500)
    this.socket.connect("tcp://*:$port")
  }

  override fun run() {
    try {
      while(!Thread.currentThread().isInterrupted) {
        val msg = this.socket.recvStr()
        msgQueue.put(msg)
      }
    }
    catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun sendMsg(msg: String): String {
    return try {
      this.socket.send(msg)
      "Successful sanding!"
    }
    catch (e: Exception) {
      e.printStackTrace()
      "Another process does't bound"
    }
  }

  fun closeSocket() {
    this.socket.close()
  }

  fun getQueue() = this.msgQueue
}

