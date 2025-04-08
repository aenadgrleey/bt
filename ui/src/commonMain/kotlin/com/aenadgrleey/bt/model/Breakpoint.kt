package com.aenadgrleey.bt.model

import kotlinx.serialization.Serializable

@Serializable
data class Breakpoint(
    val type: String,
    val filePath: String,
    val line: Int,
    val column: Int
) {
    override fun toString(): String {
        return "Breakpoint(type='$type', filePath='$filePath', line=$line, column=$column)"
    }
    companion object {
//        fun fromXBreakpoint(breakpoint: XBreakpoint<*>): Breakpoint? {
//            val type = breakpoint.type.id
//            val filePath = breakpoint.sourcePosition?.file?.path ?: return null
//            val line = breakpoint.sourcePosition?.line ?: return null
//            val column = breakpoint.sourcePosition?.offset ?: return null
//            return Breakpoint(type, filePath, line, column)
//        }
    }
}