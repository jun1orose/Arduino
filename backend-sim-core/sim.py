from pysimavr.avr import Avr
from pysimavr.firmware import Firmware
from pysimavr.swig.simavr import avr_raise_irq
import sys
import os


class MCU:

    def __init__(self, mcu_path, fw_path=None, freq=16000000):
        self._parse_io_table(mcu_path)
        self.mcu = os.path.basename(mcu_path)
        self.fw_path = fw_path
        self.freq = freq

        self._init_avr()

    def _parse_io_table(self, file_name):
        with open(file_name) as f:
            io_table = [tuple(line.split()) for line in f]
            self.io_table = io_table

    def _init_avr(self):
        fw = Firmware(self.fw_path)
        self._avr = Avr(mcu=self.mcu, firmware=fw, f_cpu=self.freq)

    def _submit_values_to_pins(self, pins):
        for pin in pins:
            avr_raise_irq(self._avr.irq.getioport((pin[0], int(pin[1]))), int(pin[2]))

    def run(self):
        self._submit_values_to_pins(self.io_table)
        self._avr.run()

    def terminate(self):
        self._avr.terminate()

    def step(self, n=1):
        self._avr.step(n)


if __name__ == '__main__':
    avr = MCU(sys.argv[1], sys.argv[2])
    avr.step(4000000)
    avr.terminate()
