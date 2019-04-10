package simulator.model

import simulator.model.core.MCU
import simulator.model.core.Pin

class Model {

  private val pins = mutableListOf<Pin>()
  private val MCUs = mutableListOf<MCU>()
  private val socket = Socket()

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
      socket.sendMsg(newMsg)
    }
  }
}
