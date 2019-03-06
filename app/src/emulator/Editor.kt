package emulator

import java.awt.BorderLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

class Editor private constructor(): JFrame() {

  private object Holder { var INSTANCE : Editor? = null }

  init {
    initEmulatorUI("Emulator")
    setCustomCloseOperation()
  }

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

  private fun initEmulatorUI(title: String) {
    setTitle(title)
    setSize(800, 800)
    setLocationRelativeTo(null)

    val circuitComponent = CircuitComponent(this@Editor)
    contentPane.add(circuitComponent)

    val panel = JPanel()
    val debugButton = JButton("Debug")
    debugButton.addActionListener { val debugInfo = DebugInfo(this@Editor) }
    panel.add(debugButton)
    contentPane.add(panel, BorderLayout.PAGE_START)
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
