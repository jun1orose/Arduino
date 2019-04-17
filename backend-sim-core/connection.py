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
        self.socket.connect("tcp://localhost:%s" % self.port)

    def send_msg(self, msg):
        self.socket.send_string(msg)

    def recv_msgs(self):
        lock = Lock()

        while True:
            try:
                msg = self.socket.recv(zmq.NOBLOCK)

                lock.acquire()
                self.msg_queue.put(msg)
                lock.release()

            except ZMQError:
                self.socket.RCVTIMEO = 10000 * 1
                self.send_msg("check")

                try:
                    msg = self.socket.recv()
                    self.socket.RCVTIMEO = -1

                    lock.acquire()
                    self.msg_queue.put(msg)
                    lock.release()

                except ZMQError:
                    self.socket.close()
                    return




