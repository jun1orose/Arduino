package simulator.gui

class SimulatorBase  {

  var editor: Editor? = null
  var arduinoEditor: processing.app.Editor? = null

  fun createSimulatorEditor(arduinoEditor: processing.app.Editor) {
    if(this.editor == null) {
      this.editor = Editor(this@SimulatorBase, arduinoEditor)
    }

    if(this.arduinoEditor == null) {
      this.arduinoEditor = arduinoEditor
    }
  }

  // only atmega328 for prototype
  @JvmOverloads fun uploadFirmware(sketchPath: String,
                     mcuName: String = "atmega328"
  ): String {

    if(this.editor != null) {
      val mcu = this.editor?.getModel()?.getMCU(mcuName)
      if(mcu != null) {
        return this.editor?.getBackend()?.uploadFirmware(mcuName, sketchPath) ?: "Simulator backend not active"
      }
      return "MCU doesnt't exist!"
    }
    else {
      return "Simulator not active!"
    }
  }

  fun getControllers(): MutableList<String> {
    val controllers = this.editor?.getModel()?.getControllers()

    val names = mutableListOf<String>()
    controllers?.forEach {
      names.add(it.getName())
    }

    return names
  }
}
