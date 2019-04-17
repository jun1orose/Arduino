from mock import Mock
from pysimavr.avr import Avr
from pysimavr.firmware import Firmware
from pysimavr.swig.simavr import avr_raise_irq
from connection import Socket
from threading import Thread

atmega_328_digit_pin_table = ['C6', 'D0', 'D1', 'D2', 'D3', 'D4', 'B6', 'B7', 'D5', 'D6',
                              'D7', 'B0', 'B1', 'B2', 'B3', 'B4', 'B5', 'C0',
                              'C1', 'C2', 'C3', 'C4', 'C5']


def upload_firmware(mcu, fw_path):
    # type: (Avr, str) -> None

    fw = Firmware(fw_path)
    mcu.load_firmware(fw)


def submit_value_to_pin(mcu, pin):
    # type: (Avr, str) -> None

    avr_raise_irq(mcu.irq.getioport((pin[0], int(pin[1]))), int(pin[2]))


def init_mcu(mcu_name, freq=16000000):
    # type: (str, int) -> Avr

    return Avr(mcu=mcu_name, f_cpu=freq)


def port_callback(irq, new_val, mcu_name, socket):
    # type: (..., int, str, Socket) -> None

    msg = mcu_name + " change " + irq.name[0] + " " + irq.name[1] + " " + new_val
    socket.socket.send_msg(msg)


def bind_callback_for_digit_pins(mcu, ports):
    # type: (Avr, str) -> None

    callback = Mock(side_effect=port_callback)

    for port in ports:
        mcu.irq.ioport_register_notify(callback, (port[0], int(port[1:])))


def new_pin_val(mcu, port_pin, new_val):
    # type: (Avr, (str, int), int) -> None

    irq = mcu.irq.getioport(port_pin)
    avr_raise_irq(irq, new_val)


def msg_parser(new_msg, socket, controllers):
    # type: (str, socket, [Avr]) -> None
    """ Prototype - mcu is only atmega328
        TODO: init, destroy, etc
        TODO: collection of controllers as set
    """

    split_msg = new_msg.split()
    first_word = split_msg[0]

    if first_word == 'check':
        socket.send_msg('ok')
    else:
        for controller in controllers:
            if controller.mcu == split_msg[1]:
                if first_word == 'change':
                    new_pin_val(controller, (split_msg[2], split_msg[3]), int(split_msg[4]))
                elif first_word == 'upload':
                    upload_firmware(controller, split_msg[2])


if __name__ == '__main__':
    socket = Socket()
    socket.init_socket()

    msg_recv_thread = Thread(target=socket.recv_msgs)
    msg_recv_thread.start()

    controllers = [init_mcu('atmega328')]

    while msg_recv_thread.is_alive():
        if not socket.msg_queue.empty():
            msg = socket.msg_queue.get()
            msg_parser(msg, socket, controllers)

    print "end"
    exit(0)