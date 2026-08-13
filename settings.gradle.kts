pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "MinecraftForge"
            url = uri("https://maven.minecraftforge.net/")
        }
        maven {
            name = "Garden of Fancy"
            url = uri("https://maven.gofancy.wtf/releases")
        }
        maven {
            url = uri("https://maven.maxhenkel.de/repository/public")
        }
    }
}

rootProject.name = "GregTech-Experimental"
