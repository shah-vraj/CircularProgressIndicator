package com.vraj.circularprogressindicator

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

const val MAXIMUM_SWEEP_ANGLE = 360f

@Preview
@Composable
private fun DemoScreen() {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ProgressCircle(
            modifier = Modifier.size(125.dp),
            arcForegroundColor = Color.Yellow,
            centerImageTint = Color.Black,
            centerImageBackgroundColor = Color.Yellow
        ) {
            Log.d("ProgressCircle", "1st Progress clicked - $it")
        }
        ProgressCircle(
            modifier = Modifier.size(125.dp),
            arcForegroundColor = Color.Red,
            arcBackgroundColor = Color.DarkGray,
            centerImageBackgroundColor = Color.Red
        ) {
            Log.d("ProgressCircle", "2nd Progress clicked - $it")
        }
        ProgressCircle(
            modifier = Modifier.size(125.dp),
            arcBackgroundColor = Color.Black
        ) {
            Log.d("ProgressCircle", "3rd Progress clicked - $it")
        }
    }
}

@Composable
fun ProgressCircle(
    modifier: Modifier = Modifier,
    totalProgressCount: Int = 10,
    arcStartAngle: Float = -90f,
    arcBackgroundThickness: Float = 4f,
    arcForegroundThickness: Float = 4f,
    arcBackgroundColor: Color = Color.Gray,
    arcForegroundColor: Color = Color.Green,
    centerImage: Int = R.drawable.ic_forward_arrow,
    centerImageTint: Color = Color.White,
    centerImageBackgroundColor: Color = Color.Green,
    onProgressChanged: (Int) -> Unit
) {
    val interactionSource = MutableInteractionSource()
    val currentProgress = remember { mutableIntStateOf(1) }
    val animatedProgress by animateFloatAsState(
        (currentProgress.intValue.toFloat() / totalProgressCount) * 360,
        tween(durationMillis = 1000, easing = LinearEasing),
        label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(10.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                if (currentProgress.intValue < totalProgressCount) {
                    currentProgress.intValue += 1
                    onProgressChanged(currentProgress.intValue)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawProgressArc(arcBackgroundColor, arcStartAngle, MAXIMUM_SWEEP_ANGLE, arcBackgroundThickness)
            drawProgressArc(arcForegroundColor, arcStartAngle, animatedProgress, arcForegroundThickness)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clip(CircleShape)
                .background(centerImageBackgroundColor)
        ) {
            Image(
                painter = painterResource(centerImage),
                contentDescription = "progressCircle",
                colorFilter = ColorFilter.tint(centerImageTint)
            )
        }
    }
}

private fun DrawScope.drawProgressArc(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    thickness: Float
) {
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = thickness.dp.toPx(),
            cap = StrokeCap.Round
        )
    )
}