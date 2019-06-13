package simulator.gui

import cc.arduino.view.StubMenuListener
import processing.app.Editor
import simulator.backend.PythonModule
import simulator.model.Model
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Image
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.event.MenuEvent

class Editor(
  val base: SimulatorBase,
  baseEditor: Editor
) : JFrame() {

  private val backend: PythonModule = PythonModule(this@Editor)
  private val model: Model

  init {
    this.model = Model(this.backend, baseEditor)
    this.model.start()
    Thread.sleep(500)

    initSimulatorUI("Emulator")
    setCustomCloseOperation()
  }

  private fun initSimulatorUI(title: String) {
    setTitle(title)
    setSize(800, 800)
    setLocationRelativeTo(null)

    val circuitComponent = CircuitComponent(this@Editor)
    contentPane.add(circuitComponent)

    val menuBar = JMenuBar()
    buildDebug(menuBar)
    jMenuBar = menuBar

    val panel = JPanel()
    buildControl(panel)
    panel.background = Color(0, 101, 105)
    contentPane.add(panel, BorderLayout.PAGE_START)


    this@Editor.isVisible = true
  }

  private fun buildDebug(menuBar: JMenuBar) {
    val debug = JMenu("Debug")
    debug.addMenuListener(object : StubMenuListener() {
      override fun menuSelected(e: MenuEvent?) {
        DebugInfo(this@Editor)
      }
    })
    menuBar.add(debug)
  }

  private fun buildControl(panel: JPanel) {
    try {
      val map = mutableMapOf<Image, JButton>()

      val play = ImageIO.read(File("resources/play.png"))
      val stop = ImageIO.read(File("resources/stop.png"))
      val pause = ImageIO.read(File("resources/pause.png"))

      val playButton = JButton()
      val stopButton = JButton()
      stopButton.isEnabled = false
      val pauseButton = JButton()
      pauseButton.isEnabled = false

      map[play] = playButton
      map[stop] = stopButton
      map[pause] = pauseButton

      map.forEach {
        val icon = it.key.getScaledInstance(30, 30, Image.SCALE_SMOOTH)
        it.value.icon = ImageIcon(icon)
        panel.add(it.value)
      }

      playButton.addActionListener {
        model.startModel()
        pauseButton.isEnabled = true
        playButton.isEnabled = false
        stopButton.isEnabled = true
      }

      pauseButton.addActionListener {
        model.pauseModel()
        pauseButton.isEnabled = false
        playButton.isEnabled = true
        stopButton.isEnabled = true
      }

      stopButton.addActionListener {
        model.stopModel()
        pauseButton.isEnabled = false
        playButton.isEnabled = true
        stopButton.isEnabled = false
      }

    } catch (e: Exception) {
      e.printStackTrace()
    }
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
