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
    this.socket.receiveTimeOut = 3000
    this.socket.sendTimeOut = 3000
    this.model = editor.getModel()
    Thread.sleep(500)
    this.socket.connect("tcp://*:$port")
  }

  override fun run() {
    try {
      while(!Thread.currentThread().isInterrupted) {
        var msg = this.socket.recvStr()

        if (msg == null) {
          sendMsg("check")
          msg = this.socket.recvStr()

          if (msg == null) {
            closeSocket()
          }
        }
        msgQueue.put(msg)
      }
    }
    catch (e: Exception) {
      //stop proc when it receiving msg
      e.printStackTrace()
    }
    finally {
        closeSocket()
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
    this.context.destroy()
  }

  fun getQueue() = this.msgQueue
}

