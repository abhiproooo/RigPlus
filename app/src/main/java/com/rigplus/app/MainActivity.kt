package com.rigplus.app

import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RigPlusScreen()
                }
            }
        }
    }
}

@Composable
fun RigPlusScreen() {
    var activeTool by remember { mutableStateOf(Tool.Draw) }
    var activePanel by remember { mutableStateOf(Panel.None) }
    var showTimeline by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            onUndo = { /* TODO */ },
            onRedo = { /* TODO */ },
            onCenterCanvas = { /* TODO */ },
            onToggleAutoWeld = { /* TODO */ },
            onToggleAutoFill = { /* TODO */ },
            onToggleAutoStroke = { /* TODO */ }
        )

        Row(modifier = Modifier.fillMaxSize()) {
            LeftPanel(
                activeTool = activeTool,
                onToolSelected = { activeTool = it },
                onPanelToggle = { panel ->
                    activePanel = if (activePanel == panel) Panel.None else panel
                },
                activePanel = activePanel
            )

            Box(modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(Color(0xFF1E1E1E))
            ) {
                CanvasView(modifier = Modifier.fillMaxSize())
            }

            RightPanel(
                activePanel = activePanel,
                onPanelToggle = { panel ->
                    activePanel = if (activePanel == panel) Panel.None else panel
                }
            )
        }

        if (showTimeline) {
            BottomPanel(
                onPlayClick = { /* TODO */ },
                onAddKeyframe = { /* TODO */ },
                onToggleTimeline = { showTimeline = false }
            )
        }
    }
}

enum class Tool {
    Draw, Fill, Layer, Camera, Workspace
}

enum class Panel {
    None, Style, Swatches, Layers
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onCenterCanvas: () -> Unit,
    onToggleAutoWeld: () -> Unit,
    onToggleAutoFill: () -> Unit,
    onToggleAutoStroke: () -> Unit
) {
    var autoWeld by remember { mutableStateOf(false) }
    var autoFill by remember { mutableStateOf(false) }
    var autoStroke by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color(0xFF2D2D2D),
        title = {
            Text(
                text = "RigPlus",
                color = Color.White,
                fontSize = 18.sp
            )
        },
        actions = {
            IconButton(onClick = onToggleAutoWeld) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Auto Weld",
                    tint = if (autoWeld) Color.Green else Color.White
                )
            }
            IconButton(onClick = onToggleAutoFill) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Auto Fill",
                    tint = if (autoFill) Color.Green else Color.White
                )
            }
            IconButton(onClick = onToggleAutoStroke) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Auto Stroke",
                    tint = if (autoStroke) Color.Green else Color.White
                )
            }
            IconButton(onClick = onCenterCanvas) {
                Icon(Icons.Default.Add, contentDescription = "Center Canvas")
            }
            IconButton(onClick = onUndo) {
                Icon(Icons.Default.Undo, contentDescription = "Undo")
            }
            IconButton(onClick = onRedo) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Redo")
            }
        }
    )
}

@Composable
fun LeftPanel(
    activeTool: Tool,
    onToolSelected: (Tool) -> Unit,
    onPanelToggle: (Panel) -> Unit,
    activePanel: Panel
) {
    Column(
        modifier = Modifier
            .width(56.dp)
            .fillMaxHeight()
            .background(Color(0xFF2D2D2D))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToolButton(
            tool = Tool.Draw,
            activeTool = activeTool,
            onClick = { onToolSelected(Tool.Draw) },
            icon = Icons.Default.Edit,
            label = "Draw"
        )
        ToolButton(
            tool = Tool.Fill,
            activeTool = activeTool,
            onClick = { onToolSelected(Tool.Fill) },
            icon = Icons.Default.Check,
            label = "Fill"
        )
        ToolButton(
            tool = Tool.Layer,
            activeTool = activeTool,
            onClick = { onToolSelected(Tool.Layer) },
            icon = Icons.Default.Layers,
            label = "Layer"
        )
        ToolButton(
            tool = Tool.Camera,
            activeTool = activeTool,
            onClick = { onToolSelected(Tool.Camera) },
            icon = Icons.Default.CameraAlt,
            label = "Camera"
        )
ToolButton(
                tool = Tool.Workspace,
                activeTool = activeTool,
                onClick = { onPanelToggle(Panel.Layers) },
                icon = Icons.Filled.ContentCopy,
                label = "Workspace"
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolButton(
    tool: Tool,
    activeTool: Tool,
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    val isActive = (activeTool == tool)
    Card(
        modifier = Modifier.size(48.dp),
        onClick = onClick,
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = if (isActive) Color.Green.copy(alpha = 0.3f) else Color(0xFF3D3D3D)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) Color.Green else Color.White
            )
        }
    }
}

@Composable
fun RightPanel(
    activePanel: Panel,
    onPanelToggle: (Panel) -> Unit
) {
    if (activePanel != Panel.None) {
        Column(
            modifier = Modifier
                .width(240.dp)
                .fillMaxHeight()
                .background(Color(0xFF2D2D2D))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (activePanel) {
                        Panel.Style -> "Style"
                        Panel.Swatches -> "Swatches"
                        Panel.Layers -> "Layers"
                        else -> ""
                    },
                    color = Color.White,
                    fontSize = 16.sp
                )
                IconButton(onClick = { onPanelToggle(Panel.None) }) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${activePanel.name} Panel - TODO",
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun BottomPanel(
    onPlayClick: () -> Unit,
    onAddKeyframe: () -> Unit,
    onToggleTimeline: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color(0xFF2D2D2D))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Timeline", color = Color.White, fontSize = 16.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onPlayClick) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White)
                }
                IconButton(onClick = onAddKeyframe) {
                    Icon(Icons.Default.Add, contentDescription = "Add Keyframe", tint = Color.White)
                }
                IconButton(onClick = onToggleTimeline) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Hide Timeline", tint = Color.White)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Timeline Track - TODO", color = Color.White.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun CanvasView(modifier: Modifier = Modifier) {
    val renderer = remember { CanvasRenderer() }
    var surfaceHolder: SurfaceHolder? = null

    AndroidView(
        modifier = modifier,
        factory = { context ->
            SurfaceView(context).apply {
                setZOrderOnTop(true)
                holder.setFormat(android.graphics.PixelFormat.TRANSPARENT)
                holder.addCallback(object : SurfaceHolder.Callback {
                    override fun surfaceCreated(holder: SurfaceHolder) {
                        surfaceHolder = holder
                        val width = width
                        val height = height
                        if (width > 0 && height > 0) {
                            renderer.nativeInit(holder.surface, width, height)
                        }
                    }

                    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                        if (width > 0 && height > 0) {
                            renderer.nativeResize(width, height)
                        }
                    }

                    override fun surfaceDestroyed(holder: SurfaceHolder) {
                        renderer.nativeDestroy()
                        surfaceHolder = null
                    }
                })
            }
        },
        update = { surfaceView: SurfaceView ->
            if (surfaceHolder != null) {
                val canvas = surfaceHolder!!.lockCanvas()
                if (canvas != null) {
                    try {
                        renderer.nativeDraw()
                    } finally {
                        surfaceHolder!!.unlockCanvasAndPost(canvas)
                    }
                }
            }
        }
    )
}

@Composable
fun RigPlusPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RigPlusScreen()
        }
    }
}