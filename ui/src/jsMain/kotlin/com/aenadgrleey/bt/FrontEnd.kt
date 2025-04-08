package com.aenadgrleey.bt

import androidx.compose.runtime.*
import com.aenadgrleey.bt.model.Breakpoint
import com.aenadgrleey.bt.utils.json
import com.aenadgrleey.bt.utils.localFrontendServerWs
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.WebSocket

fun main() {
    renderComposable(rootElementId = "root") {
        var breakpoints by remember { mutableStateOf(emptyList<Breakpoint>()) }
        var debug by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            val ws = WebSocket("$localFrontendServerWs/breakpoints-updates")
            ws.onclose = { debug = "Connection closed" }
            ws.onerror = { debug = "Error occurred" }
            ws.onmessage = { event ->
                val rawData = event.data.toString()
                breakpoints = json.decodeFromString(rawData)
            }

        }

        H1 { Text("Breakpoints") }
        if (debug.isNotEmpty()) {
            H1 { Text(debug) }
        }
        Ul { breakpoints.forEach { Li { Text(it.toString()) } } }
    }
}
