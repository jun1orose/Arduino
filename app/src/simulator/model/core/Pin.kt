package simulator.model.core

import java.awt.Point

class Pin(
  val name: String,
  private var value: Int?,
  var pos: Point,
  private val relativeElement: Any?
) {

  fun getRelativeElement() = this.relativeElement

  @Synchronized fun setValue(newValue: Int) {
    this.value = newValue
  }

  fun getValue() = this.value
}
