package simulator.gui

import simulator.backend.PythonModule
import simulator.model.Model
import java.awt.BorderLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

class Editor(val base: SimulatorBase): JFrame() {

  private val backend: PythonModule = PythonModule()
  private val model: Model

  init {
    this.model = Model(this.backend)
    this.model.start()

    initSimulatorUI("Emulator")
    setCustomCloseOperation()
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

    this@Editor.isVisible = true
  }

  private fun setCustomCloseOperation() {

    defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE

    this.addWindowListener(object : WindowAdapter() {
      override fun windowClosing(e: WindowEvent?) {
        this@Editor.isVisible = false
        this@Editor.dispose()
        this@Editor.base.editor = null
        this@Editor.backend.closeSocket()
        this@Editor.backend.stopProcExec()
      }
    })
  }

  fun getBackend() = this@Editor.backend

  fun getModel() = this@Editor.model
}
