package com.aenadgrleey.bt

import com.aenadgrleey.bt.model.Breakpoint
import com.aenadgrleey.bt.utils.localFrontendServer
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.xdebugger.breakpoints.XBreakpoint
import kotlinx.coroutines.flow.map
import java.io.File
import kotlin.collections.mapNotNull

class FrontendServerStartupActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        val assets = File(PathManager.getPluginsPath(), "tracker/frontend-assets")
        startFrontEndServer(
            assets = assets,
            dataFlow = {
                BreakpointsTracker
                    .projectBreakpoints(project)
                    .map { it.mapNotNull(::frontendBreakpoint) }
            }
        )
        browser?.loadURL(localFrontendServer)
    }
}

fun frontendBreakpoint(breakpoint: XBreakpoint<*>): Breakpoint? {
    val type = breakpoint.type.id
    val filePath = breakpoint.sourcePosition?.file?.path ?: return null
    val line = breakpoint.sourcePosition?.line ?: return null
    val column = breakpoint.sourcePosition?.offset ?: return null
    return Breakpoint(type, filePath, line, column)
}