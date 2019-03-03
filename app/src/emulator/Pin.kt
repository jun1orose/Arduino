package emulator

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Point2D

class Pin(val pos: Point2D, val rad: Int) {

  private var inputValue: Int? = null

  fun drawTo(gr2: Graphics2D) {
    gr2.color = Color.blue
    gr2.drawOval(pos.x.toInt(), pos.y.toInt(), rad, rad)
  }

  fun setValue(value: Int) {
    this.inputValue = value
  }

  fun isPin(point: Point2D): Boolean {
    return Math.sqrt((point.x - pos.x) * (point.x - pos.x) + (point.y - pos.y) * (point.y - pos.y)) < rad
  }
}
