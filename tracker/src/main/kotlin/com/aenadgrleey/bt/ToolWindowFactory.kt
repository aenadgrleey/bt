package com.aenadgrleey.bt

import com.aenadgrleey.bt.utils.localFrontendServer
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.jcef.JBCefBrowser
import java.awt.BorderLayout
import javax.swing.JPanel


// global variables are world evil!!
var browser: JBCefBrowser? = null
class MyToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        browser = JBCefBrowser(localFrontendServer)
        val panel = JPanel(BorderLayout()).apply { add(browser!!.component, BorderLayout.CENTER) }
        val content = ContentFactory.getInstance().createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}