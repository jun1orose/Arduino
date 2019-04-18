package simulator.model

import simulator.backend.PythonModule
import simulator.model.core.MCU
import simulator.model.core.Pin
import java.awt.Point

class Model(private val backend: PythonModule): Thread() {

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
      val newMsg = "change $mcuName ${pinName[0]} ${pinName.substring(1)} $newValue"
      sendMsg(newMsg)
    }
  }

  override fun run() {
    while(!Thread.currentThread().isInterrupted) {

      val msg = this.backend.getMsgQueue().poll()

      if (msg != null) {

        val splitMsg = msg.split(" ")
        val firstWord = splitMsg[0]

        when(firstWord) {
          "change" -> {
            val pin = getMCU(splitMsg[1])?.getPin(splitMsg[2] + splitMsg[3])
            pin?.setValue(splitMsg[4].toInt())
          }
          "check" -> sendMsg("ok")
        }
      }

    }
  }

  private fun sendMsg(msg: String) = backend.sendMsg(msg)

  fun getPins() = this.pins

  fun addPin(pinName: String, pinValue: Int? = null, pinPos: Point, relativeElement: String) {

    val relElem = getMCU(relativeElement)
    val newPin = Pin(pinName, pinValue, pinPos, relElem)
    relElem?.addPin(newPin)

    pins.find { it.name == pinName}?.apply {
      this.pos = pinPos
      return
    }

    this.pins.add(newPin)
  }

  fun clearPins() {
    pins.clear()
  }

  fun addMCU(mcuName: String) {
    controllers.add(MCU(mcuName))
  }

  fun getMCU(mcuName: String) = controllers.find { it.getName() == mcuName }
}
