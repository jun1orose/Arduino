package emulator

import java.awt.GridLayout
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowEvent
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JTextField

class ModalPinInput(chosenPin: Pin): JDialog() {

  init {
    this.title = "Input"
    setSize(200, 100)

    val text = JLabel("Binary pin value: ")
    val inputField = JTextField()
    JTextField().text = chosenPin.inputValue.toString()

    this.layout = GridLayout(1, 2)
    this.add(text)
    this.add(inputField)

    this.addKeyListener(object : KeyListener {
      override fun keyReleased(p0: KeyEvent?) {}

      override fun keyPressed(p0: KeyEvent?) {
        if(p0?.keyCode == KeyEvent.VK_ENTER) {
          chosenPin.inputValue = getInputFromField(inputField)
          dispatchEvent(WindowEvent(this@ModalPinInput, WindowEvent.WINDOW_CLOSING))
          dispose()
        }
      }

      override fun keyTyped(p0: KeyEvent?) {}
    })
  }

  fun getInputFromField(inputField: JTextField) = inputField.text.toInt()
}
