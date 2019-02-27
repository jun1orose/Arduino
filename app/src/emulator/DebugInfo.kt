package emulator

import javax.swing.JFrame
import javax.swing.JTable

class DebugInfo(parent: Editor) : JFrame() {

  init {
    initDebugInfoUI("Debug Info", parent, parent.height)
    initDebugTable()
  }

  private fun initDebugTable() {
    val columnNames = arrayOf("Register", "Value")
    val data = arrayOf(arrayOf("A0", "0b0"), arrayOf("A1", "0b1"))
    val table = JTable(data, columnNames)
    
    contentPane.add(table)
  }

  private fun initDebugInfoUI(title: String, parent: Editor, size: Int) {
    setTitle(title)
    setSize(size, size)
    setLocationRelativeTo(parent)
    isVisible = true
  }
}
