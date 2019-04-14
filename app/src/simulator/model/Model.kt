package simulator.model

import simulator.backend.PythonModule
import simulator.model.core.MCU
import simulator.model.core.Pin
import java.awt.Point

class Model(private val backend: PythonModule) {

  private val pins = mutableSetOf<Pin>()
  private val controllers = mutableSetOf<MCU>()

  fun changePinValue(pin: Pin, newValue: Int) {
    pin.setValue(newValue)
    val relativeElement = pin.getRelativeElement() as? MCU

    if(relativeElement != null) {
      notifyBackend(relativeElement.getName(), pin.name, newValue)
    }
  }

  private fun notifyBackend(mcuName:String, pinName: String, newValue: Int) {
    if(pinName[1].isDigit()) {
      val newMsg = "$mcuName ${pinName[0]} ${pinName.substring(1)} $newValue"
      sendMsg(newMsg)
    }
  }

  private fun sendMsg(msg: String) = backend.sendMsg(msg)

  fun getPins() = this.pins

  fun addPin(pinName: String, pinValue: Int? = null, pinPos: Point, relativeElement: String) {

    val relElem = getMCU(relativeElement)
    relElem?.addPin(pinName, pinPos)

    pins.find { it.name == pinName}?.apply {
      this.pos = pinPos
      return
    }

    this.pins.add(Pin(pinName, pinValue, pinPos, relativeElement))
  }

  fun clearPins() {
    pins.clear()
  }

  fun addMCU(mcuName: String) {
    controllers.add(MCU(mcuName))
  }

  fun getMCU(mcuName: String) = controllers.find { it.getName() == mcuName }
}
