from mock import Mock
from pyavrutils import AvrGcc
from pysimavr.avr import Avr
from pysimavr.connect import connect_pins_by_rule
from pysimavr.firmware import Firmware
from pysimavr.swig.simavr import avr_raise_irq
from pysimavr.vcdfile import VcdFile
import pysimavr.swig.utils as utils
import sys


def create_avr(mcu, f_cpu, code):
    cc = AvrGcc(mcu=mcu)
    cc.build(code)
    fw = Firmware(cc.output)
    return Avr(mcu=mcu, firmware=fw, f_cpu=f_cpu)


def test_avr():
    mcu = 'atmega328'
    code = '''
    #include <avr/io.h>
    #include <util/delay.h>

    int main(void)
    {  
        DDRB = 0x00;
        PORTB = 0xFF;
        DDRC = 0x00;
        PORTC = 0xFF;
        
        while(1)
        {
            PORTB = PINB;
            PORTC = PINC;
            _delay_ms(1000);
        }
    }
    '''

    avr = create_avr(mcu, 16000000, code)
    table = parse_input_table(mcu)

    vcd = VcdFile(avr, period=1000, filename='test.vcd')
    connect_pins_by_rule('''
                                        avr.B8 ==> vcd
                                        avr.B1 ==> vcd
                                        avr.B2 ==> vcd
                                        avr.B3 ==> vcd
                                        avr.B4 ==> vcd
                                        avr.B5 ==> vcd
                                        avr.B6 ==> vcd
                                        avr.B7 ==> vcd
                                        
                                        avr.C0 ==> vcd
                                        avr.C1 ==> vcd
                                        avr.C2 ==> vcd
                                        avr.C3 ==> vcd
                                        avr.C4 ==> vcd
                                        avr.C5 ==> vcd
                                        avr.C6 ==> vcd
                                        avr.C7 ==> vcd
                                        ''',
                         dict(
                             avr=avr,
                         ),
                         vcd=vcd,
                         )
    vcd.start()

    def port_callback(irq, new_val):
        print irq.name, new_val

    callback_mock = Mock(side_effect=port_callback)

    avr.irq.ioport_register_notify(callback_mock, ('B', 8))
    avr.irq.ioport_register_notify(callback_mock, ('C', 8))

    avr.step(20000000)
    submit_values_to_pins(table, avr)

    n = 90000000
    while n > 0:
        i = 10000000
        avr.step(i)
        n -= i

    avr.terminate()


def parse_input_table(file_name):
    with open(file_name) as f:
        input_table = [tuple(line.split()) for line in f]

    return input_table


def submit_values_to_pins(pins, avr):
    for pin in pins:
        avr_raise_irq(avr.irq.getioport((pin[0], int(pin[1]))), int(pin[2]))


if __name__ == '__main__':
    test_avr()
