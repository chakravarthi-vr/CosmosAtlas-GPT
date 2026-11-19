package org.cosmosatlas.core.astromath

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

class AstrophysicsMathTest {

    @Test
    fun testJulianDateJ2000() {
        val jd = AstrophysicsMath.calendarToJulianDate(
            year = 2000,
            month = 1,
            day = 1,
            hour = 12,
            minute = 0,
            second = 0.0
        )
        assertEquals(2451545.0, jd, 1e-5)
    }

    @Test
    fun testKeplerSolverConvergence() {
        // High eccentricity test e = 0.8
        val e = 0.8
        val m = PI / 3.0 // 60 degrees

        val eAnomaly = AstrophysicsMath.solveKepler(meanAnomalyRad = m, eccentricity = e)
        // Verify Kepler equation: M = E - e * sin(E)
        val computedM = eAnomaly - e * sin(eAnomaly)
        val error = abs(computedM - m)
        assertTrue("Kepler solver residual error too large: $error", error < 1e-10)
    }

    @Test
    fun testSchwarzschildRadiusSolarMass() {
        val solarMass = AstrophysicsMath.SOLAR_MASS_KG
        val rs = AstrophysicsMath.calculateSchwarzschildRadius(solarMass)
        // 1 Solar mass Schwarzschild radius is ~2953.2 meters
        assertEquals(2953.2, rs, 1.0)
    }

    @Test
    fun testWienPeakSolar() {
        // Solar surface temperature ~5778 K
        val peakNm = AstrophysicsMath.wienPeakWavelengthNm(5778.0)
        // Peak is in the visible green spectrum ~501 nm
        assertEquals(501.5, peakNm, 2.0)
    }

    @Test
    fun testEarthSurfaceGravityAndEscapeVelocity() {
        val earthMass = 5.97237e24
        val earthRadius = 6371.0e3 // meters

        val g = AstrophysicsMath.calculateSurfaceGravity(earthMass, earthRadius)
        assertEquals(9.82, g, 0.05)

        val ve = AstrophysicsMath.calculateEscapeVelocityKmS(earthMass, earthRadius)
        assertEquals(11.18, ve, 0.05)
    }

    @Test
    fun testEarthOrbitEphemerisComputation() {
        // Compute Earth's position at J2000.0
        val pos = AstrophysicsMath.computeHeliocentricCoordinates(
            SolarSystemCatalog.EARTH.orbit,
            2451545.0
        )
        // Earth should be ~1.0 AU from Sun
        assertEquals(1.0, pos.magnitude, 0.05)
    }
}
