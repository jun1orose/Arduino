package simulator.gui

import simulator.shapes.MCU
import simulator.shapes.Pin
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.JComponent

class CircuitComponent(private val editor: Editor) : JComponent() {

  private var buffer: BufferedImage? = null
  private var antiAlias = true

  private val pins = mutableSetOf<Pin>()
  private val controllers = mutableListOf<MCU>()

  init {
    this.addMouseListener(object : MouseAdapter() {
      override fun mouseClicked(e: MouseEvent) {
        val pos = Point(e.x, e.y)
        for (pin in pins) {
          if (pin.isPin(pos)) {
            editor.getModel().getMCU("atmega328")?.getPinByPos(pin.pos)?.apply {
              ModalPinInput(this, editor)
            }
          }
        }
      }
    })
  }

  override fun paintComponent(g: Graphics?) {
    super.paintComponent(g)
    pins.clear()
    controllers.clear()

    val needsNewBuffer = buffer == null
      || width != buffer?.width
      || height != buffer?.height

    if (needsNewBuffer) {

      buffer = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.defaultConfiguration.createCompatibleImage(width, height)

      val gr2 = buffer!!.createGraphics()
      enableAntiAlias(gr2)
      gr2.color = Color.WHITE
      gr2.fillRect(0, 0, width, height)

      val startPoint = Point((width - 400) / 2, (height - 400) / 2)
      addMCU("atmega328", 400, startPoint)

      controllers.forEach { it.drawTo(gr2) }
      pins.forEach { it.drawTo(gr2) }
    }

    g?.drawImage(buffer, 0, 0, null)

  }

  private fun enableAntiAlias(gr2: Graphics2D?) {
    if (antiAlias) {
      gr2?.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      gr2?.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
      gr2?.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)
    }
  }

  fun addPin(pinName: String, pinImage: Pin, relElem: String = "") {
    pins.add(pinImage)
    editor.getModel().addPin(pinName, pinPos = pinImage.pos, relativeElement = relElem)
  }

  private fun addMCU(mcuName: String, mcuSize: Int, mcuPos: Point) {
    controllers.add(MCU(mcuName, mcuSize, this@CircuitComponent, mcuPos))
    editor.getModel().addMCU(mcuName)
  }
}
