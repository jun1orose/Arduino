package simulator.model

import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import java.util.*

class Socket: Thread() {

  private val port = 5555
  private val incomingMsgQueue: Queue<String> = LinkedList<String>()
  private lateinit var socket: ZMQ.Socket

  init {
    ZContext().use {
      this.socket = it.createSocket(SocketType.PAIR)
      this.socket.bind("tcp://*:$port")
    }
  }

  override fun run() {
    while(!Thread.currentThread().isInterrupted) {
      val msg = this.socket.recvStr()
      if(msg.isNotEmpty()) {
        incomingMsgQueue.add(msg)
      }
    }
  }

  fun sendMsg(msg: String) = this.socket.send(msg)
}

