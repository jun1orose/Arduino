package simulator.backend

import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import java.util.*

class Socket: Thread() {

  private val port = 5555
  private val incomingMsgQueue: Queue<String> = LinkedList<String>()
  private val socket: ZMQ.Socket
  private val context: ZContext = ZContext()

  init {
    this.socket = context.createSocket(SocketType.PAIR)
    this.socket.bind("tcp://*:$port")
  }

  override fun run() {
    while(!Thread.currentThread().isInterrupted) {
      val msg = this.socket.recvStr(ZMQ.NOBLOCK)
      if (msg != null) {
        this.incomingMsgQueue.add(msg)
      }
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

  fun getMsgQueue() = this.incomingMsgQueue

  fun closeSocket() {
    this.socket.close()
  }
}

