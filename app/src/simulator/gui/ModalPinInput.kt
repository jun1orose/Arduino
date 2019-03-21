package simulator.gui

import simulator.core.Pin
import java.awt.GridLayout
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowEvent
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JTextField

class ModalPinInput(chosenPin: Pin, parentWindow: JFrame): JDialog(parentWindow, true) {
  init {
    this.title = "Input"
    setSize(200, 100)
    setLocationRelativeTo(parentWindow)

    val text = JLabel("Binary pin value: ")
    val inputField = JTextField()
    inputField.text = chosenPin.value?.toString() ?: "empty"

    inputField.setSize(20, 10)
    this.layout = GridLayout(1, 2)
    this.add(text)
    this.add(inputField)

    inputField.addKeyListener(object : KeyListener {
      override fun keyReleased(p0: KeyEvent?) {}

      override fun keyPressed(p0: KeyEvent?) {
        if(p0?.keyCode == KeyEvent.VK_ENTER) {
          chosenPin.value = getInputFromField(inputField)
          dispatchEvent(WindowEvent(this@ModalPinInput, WindowEvent.WINDOW_CLOSING))
          dispose()
        }
      }

      override fun keyTyped(p0: KeyEvent?) {}
    })

    this.isVisible = true
  }

  fun getInputFromField(inputField: JTextField) = inputField.text.toInt()
}
