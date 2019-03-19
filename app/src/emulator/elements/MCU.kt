package emulator.elements

import emulator.gui.CircuitComponent
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Point

class MCU(private val name: String, val width: Int) {

  private val pinNames = arrayOf("C6", "D0", "D1", "D2", "D3", "D4", "VCC", "GND", "B6", "B7", "D5", "D6",
    "D7", "B0", "B1", "B2", "B3", "B4", "B5", "AVCC", "Aref", "GND", "C0", "C1", "C2", "C3", "C4", "C5")

  fun drawTo(gr2: Graphics2D, pos: Point) {
    gr2.color = Color.BLACK
    gr2.drawRect(pos.x, pos.y, width, width)

    val namePos = Point(pos.x + width / 2 - name.length * 4 , pos.y + width / 2)
    gr2.drawString(name, namePos.x, namePos.y)

    val pinDiam = 12
    val dy = width / (pinNames.size / 2)

    for (i in 0 until pinNames.size / 2) {
      val y = pos.y + (i + 1) * dy - dy / 2
      CircuitComponent.addPin(Pin(Point(pos.x - pinDiam / 2, y), pinDiam))

      gr2.color = Color.BLACK
      gr2.drawString(pinNames[i], pos.x + pinDiam, y + dy / 4)
    }

    val x = pos.x + width - pinDiam / 2
    for (i in pinNames.size / 2 - 1 downTo  0) {
      val y = pos.y + (i + 1) * dy - dy / 2
      CircuitComponent.addPin(Pin(Point(x, y), pinDiam))

      gr2.color = Color.BLACK
      gr2.drawString(pinNames[pinNames.size - 1 - i], x - pinDiam - pinNames[pinNames.size - 1 - i].length * 6, y + dy / 4)
    }

    for (pin in CircuitComponent.getPins()) {
      pin.drawTo(gr2)
    }
  }
}
