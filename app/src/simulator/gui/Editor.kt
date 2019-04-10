package simulator.gui

import simulator.model.Model
import java.awt.BorderLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

class Editor private constructor(): JFrame() {

  private object Holder { var INSTANCE : Editor? = null }
  val model = Model()

  init {
    initSimulatorUI("Emulator")
    setCustomCloseOperation()
  }

  companion object {
    fun createSimulator() {
      if (!isSimulatorExist()) {
        Holder.INSTANCE = Editor()
        Holder.INSTANCE?.isVisible = true
        Holder.INSTANCE?.toFront()
      }
      else {
        Holder.INSTANCE?.isVisible = true
        Holder.INSTANCE?.toFront()
      }
    }

    fun isSimulatorExist() = Holder.INSTANCE != null

    fun returnEditor() = Holder.INSTANCE
  }

  private fun initSimulatorUI(title: String) {
    setTitle(title)
    setSize(800, 800)
    setLocationRelativeTo(null)

    val circuitComponent = CircuitComponent(this@Editor)
    contentPane.add(circuitComponent)

    val panel = JPanel()
    val debugButton = JButton("Debug")
    debugButton.addActionListener { DebugInfo(this@Editor) }
    panel.add(debugButton)

    contentPane.add(panel, BorderLayout.PAGE_START)
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
