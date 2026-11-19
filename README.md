# CosmosAtlas 🌌

**CosmosAtlas** is a production-grade, interactive Android application designed for deep astrophysics exploration, real-time Solar System orbital dynamics, high-resolution NASA celestial imagery, and space weather monitoring.

Built with **Kotlin, Jetpack Compose (Material 3), Clean Architecture (MVVM + MVI), Hilt, Retrofit, Room, and Coil**, designed around an OLED-friendly "Deep Space Void" dark theme (`#05070D`).

---

## 🏛 Architecture Overview

CosmosAtlas adheres to Google's official Android Architecture Guide with modularization by feature and core layer:

```
CosmosAtlas/
├── app/                       # Application entry point, Hilt dependency graph, Navigation host
├── core/
│   ├── astro-math/            # Keplerian solvers, orbital mechanics, Schwarzschild, Planck, Julian Date conversions
│   ├── model/                 # Pure domain models (Orbits, Ephemerides, Dossiers, Deep Space, Space Weather)
│   ├── network/               # Retrofit clients, OkHttp rate-limit/retry interceptors, NASA/JPL/SIMBAD endpoints
│   ├── database/              # Room database, APOD & Deep Space entities, DAOs, offline persistent cache
│   └── designsystem/          # "Deep Space Void" theme, typography, glassmorphism, glowing canvas components
└── feature/
    ├── orrery/                # 2D/3D Canvas Solar System Orrery, JPL Horizons engine, WebGL NASA Eyes mode
    ├── catalog/               # Deep Space Explorer (Multi-wavelength optical/IR/X-ray, SIMBAD astrometry)
    ├── solar/                 # Real-time SDO Helioviewer disk viewer, AIA wavelength switcher, NOAA space weather
    └── dailycosmos/           # APOD daily feed with pinch-to-zoom HD inspection, Room bookmarks
```

---

## 🔭 Data Sources & Computational Engines

| Engine / API | Provider | Usage in CosmosAtlas |
| :--- | :--- | :--- |
| **NASA Science APOD** | `science.nasa.gov/wp-json/wp/v2/apod-basic` | Daily celestial photography, high-resolution explanations with fallback to `api.nasa.gov` |
| **NASA Image & Video Library** | `images-api.nasa.gov` | High-res mission photography archive for JWST, Hubble, Juno, Cassini, New Horizons |
| **JPL Horizons On-Line System** | `ssd.jpl.nasa.gov/api/horizons.api` | Real-time heliocentric Cartesian state vectors $(X, Y, Z, V_x, V_y, V_z)$, distance from Sun, orbital velocities |
| **NASA Exoplanet Archive TAP** | `exoplanetarchive.ipac.caltech.edu/TAP` | Confirmed exoplanets, host star spectral types, equilibrium temperatures, habitable zone metrics |
| **SSD / CNEOS CAD API** | `ssd-api.jpl.nasa.gov/cad.api` | Real-time Near-Earth Objects (NEOs), close-approach distance in AU/km, relative velocity |
| **Helioviewer REST API** | `api.helioviewer.org` | Near-real-time Solar Dynamics Observatory (SDO) extreme ultraviolet disk snapshots (AIA 171, 193, 211, 304, 131, HMI) |
| **SIMBAD / CDS VizieR** | `simbad.u-strasbg.fr` | Deep sky astrometric coordinates (RA/Dec), spectral classification, redshift $z$ |
| **Native Kotlin Math Engine** | OpenAstronomy / Astropy | Local Keplerian solver ($M = E - e \sin E$), Julian Date calculation, Wien's law, Schwarzschild radius |

---

## 🚀 Native Orbital Mechanics Math Engine (`:core:astro-math`)

CosmosAtlas computes orbital state vectors entirely on-device, enabling 60fps orbital simulations offline:

1. **Julian Date & Epoch Propagation**:
   $$\text{JD} = \lfloor 365.25(Y + 4716) \rfloor + \lfloor 30.6001(M + 1) \rfloor + D + B - 1524.5$$
   Centuries since J2000.0:
   $$T = \frac{\text{JD} - 2451545.0}{36525.0}$$

2. **Kepler's Equation Solver (Newton-Raphson)**:
   $$M = E - e \sin E \implies E_{n+1} = E_n - \frac{E_n - e \sin E_n - M}{1 - e \cos E_n}$$
   Converges to $< 10^{-12}$ radians within 4-5 iterations.

3. **Heliocentric Coordinates in Ecliptic J2000 Frame**:
   $$x' = a(\cos E - e), \quad y' = a\sqrt{1 - e^2}\sin E$$
   Rotated via argument of perihelion $\omega$, ascending node $\Omega$, and inclination $i$.

4. **Schwarzschild Radius & Planck's Law**:
   $$r_s = \frac{2GM}{c^2}, \quad B_\lambda(\lambda, T) = \frac{2hc^2}{\lambda^5} \frac{1}{e^{\frac{hc}{\lambda k_B T}} - 1}$$

---

## 🎨 UI & Screens

1. **Solar System Live Orrery**:
   - Interactive 2D/3D Canvas rendering planetary positions for arbitrary timestamps.
   - Time-scrubber slider (fast forward/rewind $\pm 5$ years, 1x, 10x, 100x playback).
   - Celestial Body Dossier bottom sheet with physical telemetry, atmospheric composition, formation, internal structure, magnetosphere, exploration history, and NASA gallery.
   - Toggle button for NASA "Eyes on the Solar System" WebGL 3D mode.

2. **Deep Space & Celestial Catalog Explorer**:
   - Filterable catalog of Nebulae, Galaxies, Star Clusters, Black Holes, Supernova Remnants, and Exoplanets.
   - Multi-wavelength viewer (Optical, Infrared, X-Ray) with comparative astrophysical descriptions.
   - SIMBAD astrometric telemetry & relativistic calculations.

3. **Live Sun & Space Weather Dashboard**:
   - Real-time SDO disk viewer via Helioviewer with AIA wavelength switcher (AIA 171 Å, 193 Å, 211 Å, 304 Å, 131 Å, HMI).
   - NOAA Space Weather indices: Solar wind speed, proton density, planetary Kp index, flare warnings.
   - CNEOS Near-Earth Objects list with close-approach alerts.

4. **Daily Cosmos (APOD & Mission Feed)**:
   - Featured APOD with pinch-to-zoom HD inspection.
   - Bookmarking to local Room database for offline reading.

---

## 🔑 API Key Setup

CosmosAtlas defaults to `DEMO_KEY` for NASA Open APIs, with automatic exponential backoff on HTTP 429 rate limits.
To use your personal NASA API key:

1. Obtain a free key at [https://api.nasa.gov](https://api.nasa.gov).
2. Copy `local.properties.example` to `local.properties`:
   ```properties
   NASA_API_KEY=YOUR_NASA_API_KEY_HERE
   ```
3. Or set the environment variable:
   ```bash
   export NASA_API_KEY=YOUR_NASA_API_KEY_HERE
   ```

---

## 🛠 Build & Verification

```bash
# Run unit tests (including Kepler solver, Julian Date, Schwarzschild tests)
./gradlew test

# Assemble Debug APK
./gradlew assembleDebug
```
