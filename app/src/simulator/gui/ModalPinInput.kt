package simulator.gui

import simulator.model.core.Pin
import java.awt.GridLayout
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowEvent
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JTextField

class ModalPinInput(chosenPin: Pin, editor: Editor): JDialog(editor, true) {
  init {
    this.title = "Input"
    setSize(200, 100)
    setLocationRelativeTo(editor)

    val text = JLabel("Binary pin value: ")
    val inputField = JTextField()
    inputField.text = chosenPin.getValue()?.toString() ?: "empty"

    inputField.setSize(20, 10)
    this.layout = GridLayout(1, 2)
    this.add(text)
    this.add(inputField)

    inputField.addKeyListener(object : KeyListener {
      override fun keyReleased(p0: KeyEvent?) {}

      override fun keyPressed(p0: KeyEvent?) {
        if(p0?.keyCode == KeyEvent.VK_ENTER) {
          editor.model.changePinValue(chosenPin, getInputFromField(inputField))
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
