from mock import Mock
from pysimavr.avr import Avr
from pysimavr.firmware import Firmware
from pysimavr.swig.simavr import avr_raise_irq
from connection import Socket
from threading import Thread

atmega_328_digit_pin_table = ['C6', 'D0', 'D1', 'D2', 'D3', 'D4', 'B6', 'B7', 'D5', 'D6',
                              'D7', 'B0', 'B1', 'B2', 'B3', 'B4', 'B5', 'C0',
                              'C1', 'C2', 'C3', 'C4', 'C5']


class Controller:

    def __init__(self, sock, mcu_name, freq=16000000):
        self.mcu_name = mcu_name
        self.mcu = Avr(mcu=mcu_name, f_cpu=freq)
        self.bind_callback_for_digit_pins(atmega_328_digit_pin_table)
        self.socket = sock

    def upload_firmware(self, fw_path):
        fw = Firmware(fw_path)
        self.mcu.load_firmware(fw)
        self.mcu.run()

    def bind_callback_for_digit_pins(self, ports):

        def port_callback(irq, new_val):
            msg = "change " + self.mcu_name + ' ' + irq.name[0] + " " + irq.name[1] + " " + str(new_val)
            self.socket.send_msg(msg)

        callback = Mock(side_effect=port_callback)

        for port in ports:
            p = self.mcu.irq.ioport_register_notify(callback, (port[0], int(port[1:])))
            p.get_irq().name = port

    def new_pin_val(self, port_pin, new_val):
        # type: ((str, int), int) -> None

        irq = self.mcu.irq.getioport(port_pin)
        avr_raise_irq(irq, new_val)

    def run(self):
        self.mcu.run()

    def terminate(self):
        self.mcu.terminate()


class Controllers:
    """
        TODO: move bind_callback_for_digit_pins from Controller
        TODO: delete self.socket from Controller
    """

    def __init__(self):
        self._controllers = []
        self.socket = Socket()

    def msg_parser(self, new_msg):

        split_msg = new_msg.split()
        first_word = split_msg[0]

        if first_word == 'check':
            self.socket.send_msg('ok')
        elif first_word == 'ok':
            pass
        elif first_word == 'init':
            self.add_mcu(split_msg[1])
        else:
            for controller in self._controllers:
                if controller.mcu_name == split_msg[1]:
                    if first_word == 'change':
                        controller.new_pin_val((split_msg[2], split_msg[3]), int(split_msg[4]))
                    elif first_word == 'upload':
                        controller.upload_firmware(split_msg[2])
                    elif first_word == 'terminate':
                        controller.terminate()

    def add_mcu(self, mcu_name, freq=16000000):
        self._controllers.append(Controller(self.socket, mcu_name, freq))

    def run(self):
        for controller in self._controllers:
            controller.run()

    def get_controller_by_name(self, mcu_name):
        for controller in self._controllers:
            if controller.mcu_name == mcu_name:
                return controller

    def terminate(self):
        for mcu in self._controllers:
            mcu.terminate()


if __name__ == '__main__':
    controllers = Controllers()
    controllers.socket.init_socket()

    msg_recv_thread = Thread(target=controllers.socket.recv_msgs)
    msg_recv_thread.start()

    controllers.add_mcu('atmega328')
    controllers.run()

    while msg_recv_thread.is_alive():
        if not controllers.socket.msg_queue.empty():
            msg = controllers.socket.msg_queue.get()
            controllers.msg_parser(msg)

    controllers.terminate()
    exit(0)
