package emulator

import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JTable

class DebugInfo(parent: Editor) : JFrame() {

  init {
    initDebugInfoUI("Debug Info", parent, parent.height)
    initDebugTable()
  }

  private fun initDebugTable() {
    val table = JTable(3, 4)
    table.size = Dimension(200, 200)
    contentPane.add(table, BorderLayout.PAGE_END)
  }

  private fun initDebugInfoUI(title: String, parent: Editor, size: Int) {
    setTitle(title)
    setSize(size, size)
    setLocationRelativeTo(parent)
    isVisible = true
  }
}
