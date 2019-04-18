package simulator.model.core

import java.awt.Point

class MCU(private val name: String) {

  private  val pins: MutableSet<Pin> = mutableSetOf()

  fun addPin(newPin: Pin) {

    pins.find { it.name == newPin.name}?.apply {
      this.pos = newPin.pos
      return
    }

    pins.add(newPin)
  }

  fun getPinByPos(pinPos: Point) = pins.find { it.pos == pinPos }

  fun getPins() = this.pins

  fun getPin(pinName: String) = pins.find { it.name == pinName }

  fun getName() = this.name
}
