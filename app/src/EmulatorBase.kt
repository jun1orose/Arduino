package processing.app

import javax.swing.JFrame

class EmulatorBase(title: String) : JFrame() {

  init {
    createUI(title)
  }

  private fun createUI(title: String) {

    setTitle(title)

    defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    setSize(300, 200)
    setLocationRelativeTo(null)
  }
}
