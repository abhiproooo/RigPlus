package com.rigplus.app

import android.view.Surface

class CanvasRenderer {
    companion object {
        System.loadLibrary("native-lib")
    }

    external fun nativeInit(surface: Surface, width: Int, height: Int)
    external fun nativeResize(width: Int, height: Int)
    external fun nativeDraw()
    external fun nativeDestroy()
}