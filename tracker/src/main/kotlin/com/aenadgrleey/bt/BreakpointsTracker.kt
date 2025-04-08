package com.aenadgrleey.bt


import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.XDebuggerUtil
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.channelFlow

object BreakpointsTracker {
    fun projectBreakpoints(
        project: Project
    ) = channelFlow {
        val xDebuggerManager = XDebuggerManager.getInstance(project)
        val breakpointManager = xDebuggerManager.breakpointManager

        trySendBlocking(breakpointManager.allBreakpoints)

        val types = XDebuggerUtil.getInstance().lineBreakpointTypes

        val disposable = Disposer.newDisposable("BreakpointsTracker")

        for (type in types) {
            breakpointManager.addBreakpointListener(
                type,
                MyLineBreakpointListener { trySendBlocking(breakpointManager.allBreakpoints) },
                disposable
            )
        }

        awaitClose { Disposer.dispose(disposable) }
    }
}

class MyLineBreakpointListener<T : XBreakpoint<*>>(
    private val onBreakpoint: (XBreakpoint<*>) -> Unit
) : XBreakpointListener<T> {
    override fun breakpointAdded(breakpoint: T) {
        onBreakpoint(breakpoint)
    }

    override fun breakpointChanged(breakpoint: T) {
        onBreakpoint(breakpoint)
    }

    override fun breakpointRemoved(breakpoint: T) {
        onBreakpoint(breakpoint)
    }

    override fun breakpointPresentationUpdated(breakpoint: T, session: XDebugSession?) {
        onBreakpoint(breakpoint)
    }
}

