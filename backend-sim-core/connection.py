from queue import Queue
import zmq
from zmq import ZMQError


class Socket:
    def __init__(self, port=5555):
        self.port = port
        self.socket = None
        self.msg_queue = Queue()
        self.context = zmq.Context()

    def init_socket(self):
        self.socket = self.context.socket(zmq.PAIR)
        self.socket.bind("tcp://*:%s" % self.port)

    def send_msg(self, msg):
        self.socket.send_string(msg)

    def recv_msgs(self):

        self.socket.RCVTIMEO = 3000 * 1

        while True:
            try:
                msg = self.socket.recv()
                self.msg_queue.put(msg)

            except ZMQError:
                self.send_msg("check")

                try:
                    msg = self.socket.recv()
                    self.msg_queue.put(msg)

                except ZMQError:
                    self.socket.close()
                    return




