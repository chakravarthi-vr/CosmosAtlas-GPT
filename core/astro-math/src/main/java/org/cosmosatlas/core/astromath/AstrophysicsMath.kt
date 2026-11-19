package org.cosmosatlas.core.astromath

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 3D Cartesian coordinates in Astronomical Units (AU) or km,
 * and optional velocity vectors.
 */
data class Vector3D(
    val x: Double,
    val y: Double,
    val z: Double,
    val vx: Double = 0.0,
    val vy: Double = 0.0,
    val vz: Double = 0.0
) {
    val magnitude: Double get() = sqrt(x * x + y * y + z * z)
    val velocityMagnitude: Double get() = sqrt(vx * vx + vy * vy + vz * vz)

    operator fun plus(other: Vector3D) = Vector3D(x + other.x, y + other.y, z + other.z, vx + other.vx, vy + other.vy, vz + other.vz)
    operator fun minus(other: Vector3D) = Vector3D(x - other.x, y - other.y, z - other.z, vx - other.vx, vy - other.vy, vz - other.vz)
    operator fun times(scalar: Double) = Vector3D(x * scalar, y * scalar, z * scalar, vx * scalar, vy * scalar, vz * scalar)
}

/**
 * Classical Keplerian orbital elements at epoch J2000.0,
 * with optional secular variation rates per Julian century.
 */
data class KeplerianOrbit(
    val semiMajorAxisAU: Double,              // a (AU)
    val semiMajorAxisRate: Double = 0.0,      // da/dt (AU/century)
    val eccentricity: Double,                  // e (dimensionless)
    val eccentricityRate: Double = 0.0,        // de/dt (1/century)
    val inclinationDeg: Double,                // I (degrees)
    val inclinationRate: Double = 0.0,         // dI/dt (degrees/century)
    val meanLongitudeDeg: Double,              // L (degrees)
    val meanLongitudeRate: Double = 0.0,       // dL/dt (degrees/century)
    val longitudeOfPerihelionDeg: Double,      // varpi (degrees)
    val longitudeOfPerihelionRate: Double = 0.0,// dvarpi/dt (degrees/century)
    val longitudeOfAscendingNodeDeg: Double,   // Omega (degrees)
    val longitudeOfAscendingNodeRate: Double = 0.0 // dOmega/dt (degrees/century)
)

/**
 * Core mathematical engine derived from OpenAstronomy / Astropy principles,
 * implementing orbital mechanics and computational astrophysics.
 */
object AstrophysicsMath {

    // Fundamental Physical Constants (CODATA 2018 / IAU)
    const val SPEED_OF_LIGHT = 299792458.0 // c (m/s)
    const val GRAVITATIONAL_CONSTANT = 6.67430e-11 // G (m^3 kg^-1 s^-2)
    const val PLANCK_CONSTANT = 6.62607015e-34 // h (J s)
    const val BOLTZMANN_CONSTANT = 1.380649e-23 // k_B (J / K)
    const val WIEN_CONSTANT = 2.897771955e-3 // b (m K)
    const val STEFAN_BOLTZMANN = 5.670374419e-8 // sigma (W m^-2 K^-4)
    const val SOLAR_MASS_KG = 1.98847e30 // M_sun (kg)
    const val SOLAR_RADIUS_M = 6.957e8 // R_sun (m)
    const val SOLAR_LUMINOSITY_WATTS = 3.828e26 // L_sun (W)
    const val AU_IN_METERS = 149597870700.0 // 1 AU in meters
    const val AU_IN_KM = 149597870.7 // 1 AU in kilometers
    const val PARSEC_IN_METERS = 3.085677581e16 // 1 pc in meters
    const val LIGHT_YEAR_IN_METERS = 9.460730472e15 // 1 ly in meters
    const val PARSEC_TO_LIGHT_YEARS = 3.261563777

    /**
     * Converts calendar UTC time to Julian Date (JD).
     * Valid for Gregorian calendar dates.
     */
    fun calendarToJulianDate(
        year: Int,
        month: Int,
        day: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Double = 0.0
    ): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2.0 - a + floor(a / 4.0)
        val dayFraction = (hour + (minute + second / 60.0) / 60.0) / 24.0

        return floor(365.25 * (y + 4716.0)) +
                floor(30.6001 * (m + 1.0)) +
                day.toDouble() + dayFraction + b - 1524.5
    }

    /**
     * Julian centuries elapsed since epoch J2000.0 (2000-01-01 12:00:00 TT, JD 2451545.0).
     */
    fun julianCenturiesSinceJ2000(julianDate: Double): Double {
        return (julianDate - 2451545.0) / 36525.0
    }

    /**
     * Solves Kepler's Equation: M = E - e * sin(E)
     * using Newton-Raphson iteration with cubic starter approximation for high eccentricity.
     *
     * @param meanAnomalyRad Mean Anomaly in radians (normalized to [0, 2*PI))
     * @param eccentricity Orbital eccentricity (0 <= e < 1 for elliptical orbits)
     * @param tolerance Convergence criterion in radians (default 1e-12)
     * @param maxIterations Maximum iteration depth
     * @return Eccentric Anomaly E in radians
     */
    fun solveKepler(
        meanAnomalyRad: Double,
        eccentricity: Double,
        tolerance: Double = 1e-12,
        maxIterations: Int = 30
    ): Double {
        val m = normalizeAngle(meanAnomalyRad)

        // Initial estimate E0
        var eAnomaly = when {
            eccentricity < 0.8 -> m + eccentricity * sin(m)
            else -> {
                // Good initial seed for higher eccentricities
                val sign = if (sin(m) > 0) 1.0 else -1.0
                m + sign * 0.85 * eccentricity
            }
        }

        for (i in 0 until maxIterations) {
            val f = eAnomaly - eccentricity * sin(eAnomaly) - m
            val fPrime = 1.0 - eccentricity * cos(eAnomaly)
            val delta = f / fPrime
            eAnomaly -= delta
            if (abs(delta) < tolerance) {
                break
            }
        }

        return normalizeAngle(eAnomaly)
    }

    /**
     * Computes the 3D Heliocentric Cartesian Position (in AU) and velocity (AU/day)
     * for given Keplerian orbital elements at a specified Julian Date.
     */
    fun computeHeliocentricCoordinates(
        orbit: KeplerianOrbit,
        julianDate: Double
    ): Vector3D {
        val t = julianCenturiesSinceJ2000(julianDate)

        // Propagate orbital elements to date T
        val a = orbit.semiMajorAxisAU + orbit.semiMajorAxisRate * t
        val e = orbit.eccentricity + orbit.eccentricityRate * t
        val iDeg = orbit.inclinationDeg + orbit.inclinationRate * t
        val lDeg = orbit.meanLongitudeDeg + orbit.meanLongitudeRate * t
        val varpiDeg = orbit.longitudeOfPerihelionDeg + orbit.longitudeOfPerihelionRate * t
        val omegaNodeDeg = orbit.longitudeOfAscendingNodeDeg + orbit.longitudeOfAscendingNodeRate * t

        val iRad = Math.toRadians(iDeg)
        val omegaNodeRad = Math.toRadians(omegaNodeDeg)
        val argPerihelionDeg = varpiDeg - omegaNodeDeg
        val argPerihelionRad = Math.toRadians(argPerihelionDeg)

        // Mean anomaly M = L - varpi
        val mDeg = lDeg - varpiDeg
        val mRad = Math.toRadians(mDeg)

        // Solve Kepler equation for eccentric anomaly E
        val eRad = solveKepler(mRad, e)

        // Coordinates in orbital plane
        val xPrime = a * (cos(eRad) - e)
        val yPrime = a * sqrt(1.0 - e * e) * sin(eRad)

        // Rotation to heliocentric ecliptic frame (J2000)
        val cosNode = cos(omegaNodeRad)
        val sinNode = sin(omegaNodeRad)
        val cosOmega = cos(argPerihelionRad)
        val sinOmega = sin(argPerihelionRad)
        val cosInc = cos(iRad)
        val sinInc = sin(iRad)

        val px = cosNode * cosOmega - sinNode * sinOmega * cosInc
        val py = sinNode * cosOmega + cosNode * sinOmega * cosInc
        val pz = sinOmega * sinInc

        val qx = -cosNode * sinOmega - sinNode * cosOmega * cosInc
        val qy = -sinNode * sinOmega + cosNode * cosOmega * cosInc
        val qz = cosOmega * sinInc

        val x = xPrime * px + yPrime * qx
        val y = xPrime * py + yPrime * qy
        val z = xPrime * pz + yPrime * qz

        // Orbital speed calculation (vis-viva approximation for instantaneous velocity)
        // Standard gravitational parameter of Sun mu = 4 * PI^2 AU^3 / yr^2 ~ 0.0002959122082855911 AU^3 / day^2
        val muSun = 0.0002959122082855911
        val r = sqrt(x * x + y * y + z * z)
        val orbitalSpeed = sqrt(muSun * (2.0 / r - 1.0 / a))

        // Direction tangent in orbital plane
        val vxPrime = -sin(eRad) * orbitalSpeed
        val vyPrime = cos(eRad) * orbitalSpeed
        val vx = vxPrime * px + vyPrime * qx
        val vy = vxPrime * py + vyPrime * qy
        val vz = vxPrime * pz + vyPrime * qz

        return Vector3D(x = x, y = y, z = z, vx = vx, vy = vy, vz = vz)
    }

    /**
     * Normalizes an angle in radians to the range [0, 2*PI).
     */
    fun normalizeAngle(rad: Double): Double {
        val twoPi = 2.0 * PI
        var result = rad % twoPi
        if (result < 0.0) result += twoPi
        return result
    }

    /**
     * Schwarzschild Radius (Event Horizon radius for a non-rotating Schwarzschild black hole).
     * r_s = 2 * G * M / c^2
     *
     * @param massKg Mass of object in kilograms
     * @return Radius in meters
     */
    fun calculateSchwarzschildRadius(massKg: Double): Double {
        return (2.0 * GRAVITATIONAL_CONSTANT * massKg) / (SPEED_OF_LIGHT.pow(2))
    }

    /**
     * Planck's Spectral Radiance Law for a blackbody:
     * B_lambda(lambda, T) = (2 * h * c^2 / lambda^5) / (exp(h * c / (lambda * k_B * T)) - 1)
     *
     * @param wavelengthMeters Wavelength in meters
     * @param temperatureKelvin Surface equilibrium temperature in Kelvin
     * @return Spectral radiance in W * sr^-1 * m^-3
     */
    fun planckSpectralRadiance(wavelengthMeters: Double, temperatureKelvin: Double): Double {
        if (temperatureKelvin <= 0.0 || wavelengthMeters <= 0.0) return 0.0
        val numerator = 2.0 * PLANCK_CONSTANT * SPEED_OF_LIGHT.pow(2) / wavelengthMeters.pow(5)
        val exponent = (PLANCK_CONSTANT * SPEED_OF_LIGHT) / (wavelengthMeters * BOLTZMANN_CONSTANT * temperatureKelvin)
        if (exponent > 700.0) return 0.0 // avoid numerical overflow
        val denominator = exp(exponent) - 1.0
        return numerator / denominator
    }

    /**
     * Wien's Displacement Law:
     * lambda_max = b / T
     *
     * @param temperatureKelvin Temperature in Kelvin
     * @return Peak wavelength in nanometers
     */
    fun wienPeakWavelengthNm(temperatureKelvin: Double): Double {
        if (temperatureKelvin <= 0.0) return 0.0
        return (WIEN_CONSTANT / temperatureKelvin) * 1e9
    }

    /**
     * Distance Modulus formula:
     * m - M = 5 * log10(d / 10 pc) => d = 10^((m - M + 5) / 5)
     *
     * @param apparentMag Apparent magnitude (m)
     * @param absoluteMag Absolute magnitude (M)
     * @return Distance in light-years
     */
    fun distanceModulusToLightYears(apparentMag: Double, absoluteMag: Double): Double {
        val parsecs = 10.0.pow((apparentMag - absoluteMag + 5.0) / 5.0)
        return parsecs * PARSEC_TO_LIGHT_YEARS
    }

    /**
     * Habitable Zone boundaries according to Kopparapu et al. (2013/2014) stellar flux approximations.
     * Computes conservative inner edge (Runaway Greenhouse) and outer edge (Maximum Greenhouse) in AU.
     *
     * @param stellarLuminositySolar Stellar luminosity relative to Sun (L / L_sun)
     * @param effectiveTempKelvin Host star effective temperature (K)
     * @return Pair(innerBoundaryAU, outerBoundaryAU)
     */
    fun calculateHabitableZoneAU(
        stellarLuminositySolar: Double,
        effectiveTempKelvin: Double
    ): Pair<Double, Double> {
        val tStar = effectiveTempKelvin - 5780.0
        // Coefficients for Runaway Greenhouse
        val sEffInner = 1.0519 + 1.332e-4 * tStar + 1.58e-8 * tStar * tStar
        // Coefficients for Maximum Greenhouse
        val sEffOuter = 0.3438 + 5.894e-5 * tStar + 1.65e-9 * tStar * tStar

        val rInner = sqrt(stellarLuminositySolar / sEffInner)
        val rOuter = sqrt(stellarLuminositySolar / sEffOuter)
        return Pair(rInner, rOuter)
    }

    /**
     * Surface gravity: g = G * M / R^2
     *
     * @param massKg Mass in kg
     * @param radiusMeters Radius in meters
     * @return Acceleration in m/s^2
     */
    fun calculateSurfaceGravity(massKg: Double, radiusMeters: Double): Double {
        if (radiusMeters <= 0.0) return 0.0
        return (GRAVITATIONAL_CONSTANT * massKg) / (radiusMeters * radiusMeters)
    }

    /**
     * Escape velocity: v_e = sqrt(2 * G * M / R)
     *
     * @param massKg Mass in kg
     * @param radiusMeters Radius in meters
     * @return Velocity in km/s
     */
    fun calculateEscapeVelocityKmS(massKg: Double, radiusMeters: Double): Double {
        if (radiusMeters <= 0.0) return 0.0
        val vMps = sqrt((2.0 * GRAVITATIONAL_CONSTANT * massKg) / radiusMeters)
        return vMps / 1000.0
    }
}
