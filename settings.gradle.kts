pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CosmosAtlas"

include(":app")
include(":core:astro-math")
include(":core:model")
include(":core:network")
include(":core:database")
include(":core:designsystem")
include(":feature:orrery")
include(":feature:catalog")
include(":feature:solar")
include(":feature:dailycosmos")
