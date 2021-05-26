dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://kotlin.bintray.com/kotlinx")
        }
    }
}
rootProject.name = "ShortTasks"
include(":app")
