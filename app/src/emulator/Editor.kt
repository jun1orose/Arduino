package emulator

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame

class Editor private constructor(): JFrame() {

  init {
    initWindow("Emulator")
    setCustomCloseOperation()
  }

  private object Holder { var INSTANCE : Editor? = null }

  companion object {
    fun createEmulator() {
      if (Holder.INSTANCE == null) {
        Holder.INSTANCE = Editor()
        Holder.INSTANCE?.isVisible = true
        Holder.INSTANCE?.toFront()
      }
      else {
        Holder.INSTANCE?.isVisible = true
        Holder.INSTANCE?.toFront()
      }
    }
  }

  private fun initWindow(title: String) {
    setTitle(title)
    setSize(400, 400)
    setLocationRelativeTo(null)
  }

  private fun setCustomCloseOperation() {

    defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE

    this.addWindowListener(object : WindowAdapter() {
      override fun windowClosing(e: WindowEvent?) {
        Editor.Holder.INSTANCE?.dispose()
        Editor.Holder.INSTANCE = null
      }
    })
  }

}
