package com.aenadgrleey.bt

import com.aenadgrleey.bt.model.Breakpoint
import com.aenadgrleey.bt.utils.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

// it's unlikely, but we should make data personalized for each of websockets subscribers
suspend fun startFrontEndServer(
    assets: File,
    dataFlow: () -> Flow<List<Breakpoint>>
) {
    println(assets.listFiles())
    embeddedServer(Netty, port = 9999) {

        install(WebSockets)
        routing {
            staticFiles(
                remotePath = "/",
                dir = assets,
                index = "index.html",
            )
            webSocket("/breakpoints-updates") {
                dataFlow().collect {
                    val text = json.encodeToString(it)
                    send(Frame.Text(text))
                }
            }

        }
    }.startSuspend(wait = false)
        .environment.config
}

suspend fun main() {
    startFrontEndServer(
        assets = File("build/distributions/assets/js"),
        dataFlow = { fakeBreakpoints() }
    )
}


fun fakeBreakpoints() = flow {
    var counter = 0
    while (true) {
        delay(1000)
        emit(
            buildList {
                repeat(counter) {
                    add(
                        Breakpoint(
                            type = "test",
                            filePath = "test",
                            line = counter,
                            column = counter
                        )
                    )
                }
            }
        )

    }
}