package org.cosmosatlas.core.astromath

/**
 * Standard NASA / JPL Keplerian elements (Standish 1992 / NASA Planetary Fact Sheets)
 * for planets, dwarf planets, moons, and deep space probes at epoch J2000.
 */
data class CelestialBodyOrbitData(
    val id: String,
    val name: String,
    val bodyType: CelestialBodyType,
    val orbit: KeplerianOrbit,
    val physicalTelemetry: PhysicalTelemetry
)

enum class CelestialBodyType {
    STAR,
    TERRESTRIAL_PLANET,
    GAS_GIANT,
    ICE_GIANT,
    DWARF_PLANET,
    MOON,
    SPACECRAFT,
    ASTEROID_COMET
}

data class PhysicalTelemetry(
    val massKg: Double,
    val meanRadiusKm: Double,
    val surfaceGravityMs2: Double,
    val escapeVelocityKmS: Double,
    val axialTiltDeg: Double,
    val rotationPeriodHours: Double,
    val orbitalPeriodDays: Double,
    val meanTemperatureKelvin: Double,
    val atmosphereSummary: String,
    val colorHex: Long
)

object SolarSystemCatalog {

    val SUN = CelestialBodyOrbitData(
        id = "sun",
        name = "Sun (Sol)",
        bodyType = CelestialBodyType.STAR,
        orbit = KeplerianOrbit(
            semiMajorAxisAU = 0.0,
            eccentricity = 0.0,
            inclinationDeg = 0.0,
            meanLongitudeDeg = 0.0,
            longitudeOfPerihelionDeg = 0.0,
            longitudeOfAscendingNodeDeg = 0.0
        ),
        physicalTelemetry = PhysicalTelemetry(
            massKg = 1.98847e30,
            meanRadiusKm = 696340.0,
            surfaceGravityMs2 = 274.0,
            escapeVelocityKmS = 617.7,
            axialTiltDeg = 7.25,
            rotationPeriodHours = 609.12, // ~25.4 days at equator
            orbitalPeriodDays = 0.0,
            meanTemperatureKelvin = 5778.0,
            atmosphereSummary = "73.4% Hydrogen, 25.0% Helium, 0.9% Oxygen, Carbon, Neon, Iron (Corona: 1-3 MK)",
            colorHex = 0xFFFFD54F
        )
    )

    val MERCURY = CelestialBodyOrbitData(
        id = "mercury",
        name = "Mercury",
        bodyType = CelestialBodyType.TERRESTRIAL_PLANET,
        orbit = KeplerianOrbit(
            semiMajorAxisAU = 0.38709927,
            semiMajorAxisRate = 0.00000037,
            eccentricity = 0.20563593,
            eccentricityRate = 0.00002524,
            inclinationDeg = 7.00497902,
            inclinationRate = -0.00594749,
            meanLongitudeDeg = 252.25032350,
            meanLongitudeRate = 149472.67411175,
            longitudeOfPerihelionDeg = 77.45779628,
            longitudeOfPerihelionRate = 0.16047689,
            longitudeOfAscendingNodeDeg = 48.33076593,
            longitudeOfAscendingNodeRate = -0.12534081
        ),
        physicalTelemetry = PhysicalTelemetry(
            massKg = 3.3011e23,
            meanRadiusKm = 2439.7,
            surfaceGravityMs2 = 3.7,
            escapeVelocityKmS = 4.25,
            axialTiltDeg = 0.034,
            rotationPeriodHours = 1407.6,
            orbitalPeriodDays = 87.97,
            meanTemperatureKelvin = 440.0,
            atmosphereSummary = "Trace exosphere: 42% Oxygen, 29% Sodium, 22% Hydrogen, 6% Helium, 0.5% Potassium",
            colorHex = 0xFFB0BEC5
        )
    )

    val VENUS = CelestialBodyOrbitData(
        id = "venus",
        name = "Venus",
        bodyType = CelestialBodyType.TERRESTRIAL_PLANET,
        orbit = KeplerianOrbit(
            semiMajorAxisAU = 0.72333566,
            semiMajorAxisRate = 0.00000493,
            eccentricity = 0.00677672,
            eccentricityRate = -0.00004107,
            inclinationDeg = 3.39467605,
            inclinationRate = -0.00078890,
            meanLongitudeDeg = 181.97909950,
            meanLongitudeRate = 58517.81538729,
            longitudeOfPerihelionDeg = 131.60246718,
            longitudeOfPerihelionRate = 0.00268329,
            longitudeOfAscendingNodeDeg = 76.67984255,
            longitudeOfAscendingNodeRate = -0.27769418
        ),
        physicalTelemetry = PhysicalTelemetry(
            massKg = 4.8675e24,
            meanRadiusKm = 6051.8,
            surfaceGravityMs2 = 8.87,
            escapeVelocityKmS = 10.36,
            axialTiltDeg = 177.36, // retrograde
            rotationPeriodHours = -5832.5,
            orbitalPeriodDays = 224.70,
            meanTemperatureKelvin = 737.0,
            atmosphereSummary = "Supercritical runaway greenhouse: 96.5% Carbon Dioxide, 3.5% Nitrogen, clouds of Sulfuric Acid (93 bar)",
            colorHex = 0xFFFFCC80
        )
    )

    val EARTH = CelestialBodyOrbitData(
        id = "earth",
        name = "Earth",
        bodyType = CelestialBodyType.TERRESTRIAL_PLANET,
        orbit = KeplerianOrbit(
            semiMajorAxisAU = 1.00000261,
            semiMajorAxisRate = 0.00000562,
            eccentricity = 0.01671123,
            eccentricityRate = -0.00004392,
            inclinationDeg = 0.00001531,
            inclinationRate = -0.01294668,
            meanLongitudeDeg = 100.46457166,
            meanLongitudeRate = 35999.37244981,
            longitudeOfPerihelionDeg = 102.93768193,
            longitudeOfPerihelionRate = 0.32327364,
            longitudeOfAscendingNodeDeg = 0.0,
            longitudeOfAscendingNodeRate = 0.0
        ),
        physicalTelemetry = PhysicalTelemetry(
            massKg = 5.97237e24,
            meanRadiusKm = 6371.0,
            surfaceGravityMs2 = 9.80665,
            escapeVelocityKmS = 11.186,
            axialTiltDeg = 23.4393,
            rotationPeriodHours = 23.9344,
            orbitalPeriodDays = 365.256,
            meanTemperatureKelvin = 288.0,
            atmosphereSummary = "78.08% Nitrogen, 20.95% Oxygen, 0.93% Argon, 0.04% Carbon Dioxide, trace water vapor (1.013 bar)",
            colorHex = 0xFF42A5F5
        )
    )

    val MARS = CelestialBodyOrbitData(
        id = "mars",
        name = "Mars",
        bodyType = CelestialBodyType.TERRESTRIAL_PLANET,
        orbit = KeplerianOrbit(
            semiMajorAxisAU = 1.52371034,
            semiMajorAxisRate = 0.00001847,
            eccentricity = 0.09339410,
            eccentricityRate = 0.00007882,
            inclinationDeg = 1.84969142,
            inclinationRate = -0.00813131,
            meanLongitudeDeg = -4.55343205,
            meanLongitudeRate = 19140.30268499,
            longitudeOfPerihelionDeg = -23.94362959,
            longitudeOfPerihelionRate = 0.44441088,
            longitudeOfAscendingNodeDeg = 49.55953891,
            longitudeOfAscendingNodeRate = -0.29257343
        ),
        physicalTelemetry = PhysicalTelemetry(
            massKg = 6.4171e23,
            meanRadiusKm = 3389.5,
            surfaceGravityMs2 = 3.72,
            escapeVelocityKmS = 5.027,
            axialTiltDeg = 25.19,
            rotationPeriodHours = 24.6229,
            orbitalPeriodDays = 686.98,
            meanTemperatureKelvin = 210.0,
            atmosphereSummary = "95.3% Carbon Dioxide, 2.6% Nitrogen, 1.9% Argon, 0.16% Oxygen (0.006 bar, active dust devils)",
            colorHex = 0xFFFF7043
        )
    )

    val JUPITER = CelestialBodyOrbitData(
        id = "jupiter",
        name = "Jupiter",
        bodyType = CelestialBodyType.GAS_GIANT,
        orbit = KeplerianOrbit(
            semiMajorAxisAU = 5.20288700,
            semiMajorAxisRate = -0.00011607,
            eccentricity = 0.04838624,
            eccentricityRate = -0.00013253,
            inclinationDeg = 1.30439695,
            inclinationRate = -0.00183714,
            meanLongitudeDeg = 34.39644051,
            meanLongitudeRate = 3034.74612775,
            longitudeOfPerihelionDeg = 14.72847983,
            longitudeOfPerihelionRate = 0.21252668,
            longitudeOfAscendingNodeDeg = 100.47390909,
            longitudeOfAscendingNodeRate = 0.20469106
        ),
        physicalTelemetry = PhysicalTelemetry(
            massKg = 1.8982e27,
            meanRadiusKm = 69911.0,
            surfaceGravityMs2 = 24.79,
            escapeVelocityKmS = 59.5,
            axialTiltDeg = 3.13,
            rotationPeriodHours = 9.925,
            orbitalPeriodDays = 4332.59,
            meanTemperatureKelvin = 165.0,
            atmosphereSummary = "89.8% Molecular Hydrogen, 10.2% Helium, ammonia cloud decks, metallic hydrogen mantle with massive magnetosphere",
            colorHex = 0xFFFFB74D
        )
    )

    val SATURN = CelestialBodyOrbitData(
        id = "saturn",
        name = "Saturn",
        bodyType = CelestialBodyType.GAS_GIANT,
        orbit = KeplerianOrbit(
            semiMajorAxisAU = 9.53667594,
            semiMajorAxisRate = -0.00125060,
            eccentricity = 0.05386179,
            eccentricityRate = -0.00050991,
            inclinationDeg = 2.48599187,
            inclinationRate = 0.00193609,
            meanLongitudeDeg = 49.95424423,
            meanLongitudeRate = 1222.49362201,
            longitudeOfPerihelionDeg = 92.59887831,
            longitudeOfPerihelionRate = -0.41897216,
            longitudeOfAscendingNodeDeg = 113.66242448,
            longitudeOfAscendingNodeRate = -0.28867794
        ),
        physicalTelemetry = PhysicalTelemetry(
            massKg = 5.6834e26,
            meanRadiusKm = 58232.0,
            surfaceGravityMs2 = 10.44,
            escapeVelocityKmS = 35.5,
            axialTiltDeg = 26.73,
            rotationPeriodHours = 10.656,
            orbitalPeriodDays = 10759.22,
            meanTemperatureKelvin = 134.0,
            atmosphereSummary = "96.3% Hydrogen, 3.25% Helium, 0.45% Methane. Extensive water ice ring system extending 282,000 km",
            colorHex = 0xFFFFE082
        )
    )

    val URANUS = CelestialBodyOrbitData(
        id = "uranus",
        name = "Uranus",
        bodyType = CelestialBodyType.ICE_GIANT,
        orbit = KeplerianOrbit(
            semiMajorAxisAU = 19.18916464,
            semiMajorAxisRate = -0.00196176,
            eccentricity = 0.04725744,
            eccentricityRate = -0.00004397,
            inclinationDeg = 0.77263783,
            inclinationRate = -0.00242939,
            meanLongitudeDeg = 313.23810451,
            meanLongitudeRate = 428.48202785,
            longitudeOfPerihelionDeg = 170.95427630,
            longitudeOfPerihelionRate = 0.40805281,
            longitudeOfAscendingNodeDeg = 74.01692503,
            longitudeOfAscendingNodeRate = 0.04240589
        ),
        physicalTelemetry = PhysicalTelemetry(
            massKg = 8.6810e25,
            meanRadiusKm = 25362.0,
            surfaceGravityMs2 = 8.69,
            escapeVelocityKmS = 21.3,
            axialTiltDeg = 97.77, // Extreme roll
            rotationPeriodHours = -17.24,
            orbitalPeriodDays = 30685.4,
            meanTemperatureKelvin = 76.0,
            atmosphereSummary = "82.5% Hydrogen, 15.2% Helium, 2.3% Methane (giving cyan hue); complex ammonia/water supercritical fluid mantle",
            colorHex = 0xFF80DEEA
        )
    )

    val NEPTUNE = CelestialBodyOrbitData(
        id = "neptune",
        name = "Neptune",
        bodyType = CelestialBodyType.ICE_GIANT,
        orbit = KeplerianOrbit(
            semiMajorAxisAU = 30.06992276,
            semiMajorAxisRate = 0.00026291,
            eccentricity = 0.00860606,
            eccentricityRate = 0.00005105,
            inclinationDeg = 1.77004347,
            inclinationRate = 0.00035372,
            meanLongitudeDeg = -55.12002969,
            meanLongitudeRate = 218.45945325,
            longitudeOfPerihelionDeg = 44.96476224,
            longitudeOfPerihelionRate = -0.32241464,
            longitudeOfAscendingNodeDeg = 131.78422574,
            longitudeOfAscendingNodeRate = -0.00508664
        ),
        physicalTelemetry = PhysicalTelemetry(
            massKg = 1.02413e26,
            meanRadiusKm = 24622.0,
            surfaceGravityMs2 = 11.15,
            escapeVelocityKmS = 23.5,
            axialTiltDeg = 28.32,
            rotationPeriodHours = 16.11,
            orbitalPeriodDays = 60189.0,
            meanTemperatureKelvin = 72.0,
            atmosphereSummary = "80% Hydrogen, 19% Helium, 1.5% Methane. Highest supersonic wind speeds in the Solar System (>2,100 km/h)",
            colorHex = 0xFF3F51B5
        )
    )

    val PLUTO = CelestialBodyOrbitData(
        id = "pluto",
        name = "Pluto",
        bodyType = CelestialBodyType.DWARF_PLANET,
        orbit = KeplerianOrbit(
            semiMajorAxisAU = 39.48211675,
            semiMajorAxisRate = -0.00031596,
            eccentricity = 0.24882730,
            eccentricityRate = 0.00005170,
            inclinationDeg = 17.14001206,
            inclinationRate = 0.00004818,
            meanLongitudeDeg = 238.92903833,
            meanLongitudeRate = 145.20780515,
            longitudeOfPerihelionDeg = 224.06876777,
            longitudeOfPerihelionRate = -0.04062942,
            longitudeOfAscendingNodeDeg = 110.30393608,
            longitudeOfAscendingNodeRate = -0.01183482
        ),
        physicalTelemetry = PhysicalTelemetry(
            massKg = 1.303e22,
            meanRadiusKm = 1188.3,
            surfaceGravityMs2 = 0.62,
            escapeVelocityKmS = 1.21,
            axialTiltDeg = 122.53,
            rotationPeriodHours = -153.29,
            orbitalPeriodDays = 90560.0,
            meanTemperatureKelvin = 44.0,
            atmosphereSummary = "Sublimating tenuous Nitrogen, Methane, Carbon Monoxide atmosphere over convective Nitrogen ice glacier (Sputnik Planitia)",
            colorHex = 0xFFD7CCC8
        )
    )

    // Dwarf Planets
    val CERES = CelestialBodyOrbitData(
        id = "ceres",
        name = "1 Ceres",
        bodyType = CelestialBodyType.DWARF_PLANET,
        orbit = KeplerianOrbit(
            semiMajorAxisAU = 2.7675,
            eccentricity = 0.0758,
            inclinationDeg = 10.59,
            meanLongitudeDeg = 153.0,
            longitudeOfPerihelionDeg = 73.0,
            longitudeOfAscendingNodeDeg = 80.3
        ),
        physicalTelemetry = PhysicalTelemetry(
            massKg = 9.3835e20,
            meanRadiusKm = 469.7,
            surfaceGravityMs2 = 0.28,
            escapeVelocityKmS = 0.51,
            axialTiltDeg = 4.0,
            rotationPeriodHours = 9.07,
            orbitalPeriodDays = 1682.0,
            meanTemperatureKelvin = 168.0,
            atmosphereSummary = "Transient water vapor exosphere driven by cryovolcanic vents and sublimation in Occator crater",
            colorHex = 0xFF9E9E9E
        )
    )

    // Deep Space Spacecraft
    val VOYAGER_1 = CelestialBodyOrbitData(
        id = "voyager1",
        name = "Voyager 1",
        bodyType = CelestialBodyType.SPACECRAFT,
        orbit = KeplerianOrbit(
            semiMajorAxisAU = 163.0, // Hyperbolic trajectory, modeled heliocentric distance
            eccentricity = 1.3,
            inclinationDeg = 35.0,
            meanLongitudeDeg = 260.0,
            longitudeOfPerihelionDeg = 100.0,
            longitudeOfAscendingNodeDeg = 67.0
        ),
        physicalTelemetry = PhysicalTelemetry(
            massKg = 825.5,
            meanRadiusKm = 0.0037, // 3.7m high-gain antenna
            surfaceGravityMs2 = 0.0,
            escapeVelocityKmS = 16.9, // Current asymptotic speed relative to Sun
            axialTiltDeg = 0.0,
            rotationPeriodHours = 0.0,
            orbitalPeriodDays = 0.0,
            meanTemperatureKelvin = 2.7,
            atmosphereSummary = "Active interstellar probe crossing interstellar plasma (~163 AU from Earth). Powered by Pu-238 RTG",
            colorHex = 0xFF00E5FF
        )
    )

    val EUROPA_CLIPPER = CelestialBodyOrbitData(
        id = "europa_clipper",
        name = "Europa Clipper",
        bodyType = CelestialBodyType.SPACECRAFT,
        orbit = KeplerianOrbit(
            semiMajorAxisAU = 1.35, // Interplanetary cruise to Jupiter
            eccentricity = 0.25,
            inclinationDeg = 2.1,
            meanLongitudeDeg = 120.0,
            longitudeOfPerihelionDeg = 40.0,
            longitudeOfAscendingNodeDeg = 30.0
        ),
        physicalTelemetry = PhysicalTelemetry(
            massKg = 6000.0,
            meanRadiusKm = 0.015, // 30.5m solar array wingspan
            surfaceGravityMs2 = 0.0,
            escapeVelocityKmS = 0.0,
            axialTiltDeg = 0.0,
            rotationPeriodHours = 0.0,
            orbitalPeriodDays = 570.0,
            meanTemperatureKelvin = 150.0,
            atmosphereSummary = "En route to Jupiter (arriving 2030) to investigate Europa's sub-surface ocean habitability with radar, magnetometer, and mass spectrometers",
            colorHex = 0xFF76FF03
        )
    )

    val ALL_BODIES = listOf(
        SUN, MERCURY, VENUS, EARTH, MARS, CERES, JUPITER, SATURN, URANUS, NEPTUNE, PLUTO, VOYAGER_1, EUROPA_CLIPPER
    )

    fun getById(id: String): CelestialBodyOrbitData? {
        return ALL_BODIES.firstOrNull { it.id.equals(id, ignoreCase = true) }
    }
}
