package simulator.model

import simulator.backend.PythonModule
import simulator.model.core.MCU
import simulator.model.core.Pin
import java.awt.Point

class Model(
  private val backend: PythonModule,
  private val arduinoEditor: processing.app.Editor
): Thread() {

  private val pins = mutableSetOf<Pin>()
  private val controllers = mutableSetOf<MCU>()
  private var state = STATE.NOT_ACTIVE

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

  fun addPin(pinName: String, pinValue: Int = 0, pinPos: Point, relativeElement: String) {

    val relElem = getMCU(relativeElement)
    val newPin = Pin(pinName, pinValue, pinPos, relElem)
    relElem?.addPin(newPin)

    pins.find { it.name == pinName}?.apply {
      this.pos = pinPos
      return
    }

    this.pins.add(newPin)
  }

  private fun clearPins() {
    pins.forEach {
      it.setValue(0)
    }
  }

  fun addMCU(mcuName: String) {
    controllers.add(MCU(mcuName))
    sendMsg("init $mcuName")
    arduinoEditor.simChanged()
  }

  fun getMCU(mcuName: String) = controllers.find { it.getName() == mcuName }

  fun getControllers() = this.controllers

  fun startModel() {
    this.state = STATE.ACTIVE
    sendMsg("start")
  }

  fun pauseModel() {
    this.state = STATE.PAUSED
    sendMsg("pause")
  }

  fun stopModel() {
    this.state = STATE.NOT_ACTIVE
    clearPins()
    sendMsg("stop")
  }
}

enum class STATE {
  PAUSED,
  NOT_ACTIVE,
  ACTIVE
}
