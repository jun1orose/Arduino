package emulator

import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JComponent

class CircuitComponent: JComponent() {

  private var buffer: BufferedImage? = null
  private var antiAlias = true

  override fun paintComponent(g: Graphics?) {
    super.paintComponent(g)

    val needsNewBuffer = buffer == null
        || width != buffer?.width
        || height != buffer?.height

    if(needsNewBuffer) {

      buffer = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.defaultConfiguration.createCompatibleImage(width, height)

      val gr2 = buffer?.createGraphics()
      enableAntiAlias(gr2)
      gr2?.color = Color.WHITE
      gr2?.fillRect(0, 0, width, height)

    }

    g?.drawImage(buffer, 0, 0, null)
    val mcuImage: BufferedImage = ImageIO.read(File("app/src/emulator/avr.png"))
    g?.drawImage(mcuImage, width / 2 - mcuImage.width / 2, height / 2 - mcuImage.height / 2, null)

  }

  private fun enableAntiAlias(gr2: Graphics2D?) {
    if (antiAlias) {
      gr2?.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      gr2?.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
      gr2?.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)
    }
  }
}
