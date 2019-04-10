package simulator.model.core

import java.awt.Point

class MCU(private val name: String) {

  private  val pins: MutableSet<Pin> = mutableSetOf()

  fun addPin(pinName: String, pinPos: Point) {

    pins.find { it.name == pinName}?.apply {
      this.pos = pinPos
      return
    }

    pins.add(Pin(pinName, null, pinPos, this@MCU))
  }

  fun getPinByPos(pinPos: Point) = pins.find { it.pos == pinPos }

  fun getPins() = this.pins

  fun getName() = this.name
}
