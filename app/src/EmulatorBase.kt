package processing.app

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame

class EmulatorBase private constructor(): JFrame() {

  init {
    createUI("Emulator")
    setCustomCloseOperation()
  }

  private object Holder { var INSTANCE : EmulatorBase? = null }

  companion object {
      fun createEmulator() {
        if (Holder.INSTANCE == null) {
          Holder.INSTANCE = EmulatorBase()
          Holder.INSTANCE?.isVisible = true
          Holder.INSTANCE?.toFront()
        }
        else {
          Holder.INSTANCE?.isVisible = true
          Holder.INSTANCE?.toFront()
        }
      }
  }

  private fun createUI(title: String) {
    setTitle(title)
    setSize(400, 400)
    setLocationRelativeTo(null)
  }

  private fun setCustomCloseOperation() {

    defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE

    this.addWindowListener(object : WindowAdapter() {
      override fun windowClosing(e: WindowEvent?) {
        Holder.INSTANCE?.dispose()
        Holder.INSTANCE = null
      }
    })
  }
}
