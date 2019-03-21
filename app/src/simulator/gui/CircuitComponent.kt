package emulator.gui

import emulator.shapes.MCU
import emulator.shapes.Pin
import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.image.BufferedImage
import javax.swing.JComponent
import javax.swing.JFrame

class CircuitComponent(parentWindow: JFrame): JComponent() {

  private var buffer: BufferedImage? = null
  private var antiAlias = true
  private object Pins { val INSTANCE = mutableListOf<Pin>() }
  private object MCU { val INSTANCE = MCU("atmega328", 400)}

  init {
      this.addMouseListener(object : MouseListener {
        override fun mouseClicked(p0: MouseEvent) {
          val pos = Point(p0.x, p0.y)
          for(pin in Pins.INSTANCE) {
            if (pin.isPin(pos)) {
              getMCU().getCore().getPinByPos(pin.pos)?.apply {
                ModalPinInput(this, parentWindow)
              }
            }
          }
        }

        override fun mouseEntered(p0: MouseEvent?) {}
        override fun mouseExited(p0: MouseEvent?) {}
        override fun mousePressed(p0: MouseEvent?) {}
        override fun mouseReleased(p0: MouseEvent?) {}
      })
  }

  override fun paintComponent(g: Graphics?) {
    super.paintComponent(g)
    Pins.INSTANCE.clear()

    val needsNewBuffer = buffer == null
        || width != buffer?.width
        || height != buffer?.height

    if(needsNewBuffer) {

      buffer = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.defaultConfiguration.createCompatibleImage(width, height)

      val gr2 = buffer!!.createGraphics()
      enableAntiAlias(gr2)
      gr2.color = Color.WHITE
      gr2.fillRect(0, 0, width, height)

      val startPoint = Point((width - getMCU().width) / 2, (height - getMCU().width) / 2)
      getMCU().drawTo(gr2, startPoint)
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

  companion object {
    fun addPin(pin: Pin) {
      Pins.INSTANCE.add(pin)
    }

    fun getPins() = Pins.INSTANCE

    fun getMCU() = MCU.INSTANCE
  }
}
