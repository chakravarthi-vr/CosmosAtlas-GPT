package org.cosmosatlas.feature.orrery.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import org.cosmosatlas.core.astromath.AstrophysicsMath
import org.cosmosatlas.core.astromath.CelestialBodyOrbitData
import org.cosmosatlas.core.astromath.CelestialBodyType
import org.cosmosatlas.core.designsystem.theme.NeonCyan
import org.cosmosatlas.core.designsystem.theme.SpaceBlack
import org.cosmosatlas.core.designsystem.theme.SpaceBorder
import org.cosmosatlas.core.designsystem.theme.StellarAmber
import org.cosmosatlas.feature.orrery.viewmodel.ProjectedPlanet
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun SolarSystemCanvas(
    projectedBodies: List<ProjectedPlanet>,
    zoomScale: Float,
    panOffsetX: Float,
    panOffsetY: Float,
    onBodyClicked: (String) -> Unit,
    onTransform: (zoomDelta: Float, panDx: Float, panDy: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    onTransform(zoom, pan.x, pan.y)
                }
            }
            .pointerInput(projectedBodies, zoomScale, panOffsetX, panOffsetY) {
                detectTapGestures { tapOffset ->
                    // Hit testing for planets
                    val center = Offset(size.width / 2f + panOffsetX, size.height / 2f + panOffsetY)
                    val baseScale = (size.minDimension / 10f) * zoomScale

                    var closestBody: String? = null
                    var minDistance = Float.MAX_VALUE

                    for (body in projectedBodies) {
                        if (body.data.bodyType == CelestialBodyType.STAR) continue
                        // Non-linear scaling for outer planets readability
                        val rAu = sqrt(body.positionAU.x * body.positionAU.x + body.positionAU.y * body.positionAU.y)
                        val scaledR = mapAuToScreenRadius(rAu, baseScale)
                        val angle = Math.atan2(body.positionAU.y, body.positionAU.x)
                        val screenX = center.x + (scaledR * cos(angle)).toFloat()
                        val screenY = center.y + (scaledR * sin(angle)).toFloat()

                        val dist = (Offset(screenX, screenY) - tapOffset).getDistance()
                        if (dist < 48f && dist < minDistance) { // 48px touch target
                            minDistance = dist
                            closestBody = body.data.id
                        }
                    }
                    closestBody?.let { onBodyClicked(it) }
                }
            }
    ) {
        val center = Offset(size.width / 2f + panOffsetX, size.height / 2f + panOffsetY)
        val baseScale = (size.minDimension / 10f) * zoomScale

        // Draw starfield background ambient glow
        drawRect(color = SpaceBlack)

        // Draw Keplerian orbital paths
        for (body in projectedBodies) {
            if (body.data.bodyType == CelestialBodyType.STAR) continue
            drawOrbitalEllipse(body.data, center, baseScale)
        }

        // Draw Central Star (Sun)
        drawSun(center)

        // Draw planets and probes
        for (body in projectedBodies) {
            if (body.data.bodyType == CelestialBodyType.STAR) continue
            val rAu = sqrt(body.positionAU.x * body.positionAU.x + body.positionAU.y * body.positionAU.y)
            val scaledR = mapAuToScreenRadius(rAu, baseScale)
            val angle = Math.atan2(body.positionAU.y, body.positionAU.x)
            val bodyCenter = Offset(
                x = center.x + (scaledR * cos(angle)).toFloat(),
                y = center.y + (scaledR * sin(angle)).toFloat()
            )

            drawPlanetBody(body, bodyCenter)
        }
    }
}

/**
 * Logarithmic-linear compression mapping so Neptune (30 AU) and Pluto (39 AU)
 * remain visible alongside inner terrestrial planets (0.38 - 1.5 AU).
 */
private fun mapAuToScreenRadius(au: Double, baseScale: Float): Float {
    if (au <= 0.0) return 0f
    val compressed = Math.pow(au, 0.65)
    return (compressed * baseScale).toFloat()
}

private fun DrawScope.drawSun(center: Offset) {
    // Outer corona glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0x80FFD54F),
                Color(0x33FFB300),
                Color(0x00FF8F00)
            ),
            center = center,
            radius = 36f
        ),
        radius = 36f,
        center = center
    )
    // Core Sun
    drawCircle(
        color = StellarAmber,
        radius = 14f,
        center = center
    )
    drawCircle(
        color = Color(0xFFFFF9C4),
        radius = 7f,
        center = center
    )
}

private fun DrawScope.drawOrbitalEllipse(
    body: CelestialBodyOrbitData,
    center: Offset,
    baseScale: Float
) {
    val semiMajorAU = body.orbit.semiMajorAxisAU
    val screenRadius = mapAuToScreenRadius(semiMajorAU, baseScale)

    val color = when (body.bodyType) {
        CelestialBodyType.SPACECRAFT -> NeonCyan.copy(alpha = 0.4f)
        CelestialBodyType.DWARF_PLANET -> Color.White.copy(alpha = 0.15f)
        else -> SpaceBorder.copy(alpha = 0.6f)
    }

    val strokeWidth = if (body.bodyType == CelestialBodyType.SPACECRAFT) 1.5f else 1.0f
    val pathEffect = if (body.bodyType == CelestialBodyType.SPACECRAFT) {
        PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    } else null

    drawCircle(
        color = color,
        radius = screenRadius,
        center = center,
        style = Stroke(width = strokeWidth, pathEffect = pathEffect)
    )
}

private fun DrawScope.drawPlanetBody(
    planet: ProjectedPlanet,
    bodyCenter: Offset
) {
    val bodyColor = Color(planet.data.physicalTelemetry.colorHex)

    val radius = when (planet.data.bodyType) {
        CelestialBodyType.GAS_GIANT -> 8f
        CelestialBodyType.ICE_GIANT -> 7f
        CelestialBodyType.TERRESTRIAL_PLANET -> 5.5f
        CelestialBodyType.DWARF_PLANET -> 4f
        CelestialBodyType.SPACECRAFT -> 4f
        else -> 5f
    }

    // Glow halo
    drawCircle(
        color = bodyColor.copy(alpha = 0.35f),
        radius = radius * 2.2f,
        center = bodyCenter
    )

    // Solid disk
    drawCircle(
        color = bodyColor,
        radius = radius,
        center = bodyCenter
    )

    // Saturn ring rendering
    if (planet.data.id == "saturn") {
        drawCircle(
            color = Color(0x66FFE082),
            radius = radius * 2.8f,
            center = bodyCenter,
            style = Stroke(width = 2.5f)
        )
    }
}
