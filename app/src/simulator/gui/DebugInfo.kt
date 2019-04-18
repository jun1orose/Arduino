package simulator.gui

import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTable


class DebugInfo(parent: Editor) : JFrame() {

  init {
    initDebugInfoUI("Debug Info", parent)
  }

  private fun initDebugTable(parent: Editor) {
    val pins = parent.getModel().getPins()
    val data = arrayListOf<Array<String>>()

    for (pin in pins) {
      data.add(arrayOf(pin.name, pin.getValue().toString()))
    }

    val columnNames = arrayOf("Register", "Value")

    val table = JTable(data.toTypedArray(), columnNames)
    table.fillsViewportHeight = true


    val scrollPane = JScrollPane(table)
    scrollPane.setBounds(200, 200, parent.width, parent.height)

    this.contentPane.add(scrollPane)
  }

  private fun initDebugInfoUI(title: String, parent: Editor) {
    initDebugTable(parent)
    pack()

    setTitle(title)
    setLocationRelativeTo(parent)
    isVisible = true
  }
}
