
from queue import Queue
import zmq
from zmq import ZMQError


class Socket:
    def __init__(self, port=5600):
        self.port = port
        self.socket = None
        self.context = zmq.Context()

    def init_socket(self):
        self.socket = self.context.socket(zmq.PAIR)
        self.socket.connect("tcp://localhost:%s" % self.port)

    def send_msg(self, msg):
        self.socket.send_string(msg)

    def recv_msgs(self, msg_queue):
        # type: (Queue) -> None
        while True:
            if True:
                try:
                    msg = self.socket.recv_string(zmq.NOBLOCK)
                    msg_queue.put(msg)
                except ZMQError:
                    pass
            else:
                self.socket.close()
                return
