package simulator.gui

class SimulatorBase  {

  var editor: Editor? = null

  fun createSimulatorEditor() {
    if(this.editor == null) {
      this.editor = Editor(this@SimulatorBase)
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
}
