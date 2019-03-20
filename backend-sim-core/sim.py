from mock import Mock
from pyavrutils import AvrGcc
from pysimavr.avr import Avr
from pysimavr.connect import connect_pins_by_rule
from pysimavr.firmware import Firmware
from pysimavr.swig.simavr import avr_raise_irq
from pysimavr.vcdfile import VcdFile
import sys


def create_avr(mcu, f_cpu, code):
    cc = AvrGcc(mcu=mcu)
    cc.build(code)
    fw = Firmware(cc.output)
    return Avr(mcu=mcu, firmware=fw, f_cpu=f_cpu)


def test_avr():
    mcu = 'atmega128'
    code = '''
    #include <avr/io.h>
    #include <util/delay.h>

    int main(void)
    {  
        DDRA = 0xFF;
        DDRB = 0x00;
        
        while(1)
        {
            PORTA = PINB;
        }
    }
    '''

    def port_callback(irq, new_val):
        print irq.name, new_val

    callback_mock = Mock(side_effect=port_callback)

    avr = create_avr(mcu, 16000000, code)

    vcd = VcdFile(avr, period=1000, filename='test.vcd')
    connect_pins_by_rule('''
                                        avr.A0 ==> vcd
                                        avr.A1 ==> vcd
                                        avr.A3 ==> vcd
                                        avr.A4 ==> vcd
                                        avr.A5 ==> vcd
                                        avr.A6 ==> vcd
                                        
                                        avr.B0 ==> vcd
                                        avr.B1 ==> vcd
                                        avr.B2 ==> vcd
                                        ''',
                         dict(
                             avr=avr,
                         ),
                         vcd=vcd,
                         )
    vcd.start()

    avr.irq.ioport_register_notify(callback_mock, ('A', 0))
    avr.irq.ioport_register_notify(callback_mock, ('B', 0))

    n = 100000000
    while n > 0:
        avr_raise_irq(avr.irq.getioport(('B', 0)), 1)
        avr_raise_irq(avr.irq.getioport(('B', 1)), 1)
        avr_raise_irq(avr.irq.getioport(('B', 2)), 1)
        i = 10000000
        avr.step(i)
        avr_raise_irq(avr.irq.getioport(('B', 0)), 0)
        avr_raise_irq(avr.irq.getioport(('B', 1)), 0)
        avr_raise_irq(avr.irq.getioport(('B', 2)), 0)
        n -= i

    avr.terminate()


def parse_input_table(file_name):
    with open(file_name) as f:
        input_table = [tuple(i.split()) for i in f]


    return input_table


if __name__ == '__main__':
    table = parse_input_table('atmega328')
    test_avr()
