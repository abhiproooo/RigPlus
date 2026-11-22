package com.rigplus.app

import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.SurfaceView
import android.view.TextureView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CanvasViewWithRenderer(
    modifier: Modifier = Modifier,
    renderer: CanvasRenderer = CanvasRenderer()
) {
    var surfaceCreated by remember { mutableStateOf(false) }
    var surfaceWidth by remember { mutableStateOf(0) }
    var surfaceHeight by remember { mutableStateOf(0) }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                SurfaceView(ctx).apply {
                    setZOrderOnTop(true)
                    holder.setFormat(android.graphics.PixelFormat.RGBA_8888)
                    holder.addCallback(object : android.view.SurfaceHolder.Callback {
                        override fun surfaceCreated(holder: android.view.SurfaceHolder) {
                            surfaceCreated = true
                            surfaceWidth = holder.surfaceFrame.width()
                            surfaceHeight = holder.surfaceFrame.height()
                            val surface = holder.surface
                            if (surface.isValid) {
                                renderer.nativeInit(surface, surfaceWidth, surfaceHeight)
                            }
                        }

                        override fun surfaceChanged(holder: android.view.SurfaceHolder, format: Int, width: Int, height: Int) {
                            surfaceWidth = width
                            surfaceHeight = height
                            renderer.nativeResize(width, height)
                        }

                        override fun surfaceDestroyed(holder: android.view.SurfaceHolder) {
                            surfaceCreated = false
                            renderer.nativeDestroy()
                        }
                    })
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (surfaceCreated) {
            LaunchedEffect(Unit) {
                while (surfaceCreated) {
                    renderer.nativeDraw()
                    try {
                        Thread.sleep(16)
                    } catch (e: InterruptedException) {
                        break
                    }
                }
            }
        }
    }
}

@Composable
fun CanvasViewTexture(
    modifier: Modifier = Modifier,
    renderer: CanvasRenderer = CanvasRenderer()
) {
    var surfaceCreated by remember { mutableStateOf(false) }
    var surfaceWidth by remember { mutableStateOf(0) }
    var surfaceHeight by remember { mutableStateOf(0) }
    var surfaceTexture by remember { mutableStateOf<SurfaceTexture?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                TextureView(ctx).apply {
                    setOpaque(false)
                    surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                            surfaceCreated = true
                            surfaceWidth = width
                            surfaceHeight = height
                            surfaceTexture = surface
                            val surfaceObj = Surface(surface)
                            renderer.nativeInit(surfaceObj, width, height)
                        }

                        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                            surfaceWidth = width
                            surfaceHeight = height
                            renderer.nativeResize(width, height)
                        }

                        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                            surfaceCreated = false
                            surfaceTexture = null
                            renderer.nativeDestroy()
                            return true
                        }

                        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (surfaceCreated) {
            LaunchedEffect(Unit) {
                while (surfaceCreated) {
                    renderer.nativeDraw()
                    try {
                        Thread.sleep(16)
                    } catch (e: InterruptedException) {
                        break
                    }
                }
            }
        }
    }
}