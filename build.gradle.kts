import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import fr.brouillard.oss.jgitver.GitVersionCalculator
import fr.brouillard.oss.jgitver.Strategies
import net.minecraftforge.gradle.common.util.RunConfig
import wtf.gofancy.fancygradle.patch.Patch
import wtf.gofancy.fancygradle.script.extensions.curse
import wtf.gofancy.fancygradle.script.extensions.deobf
import java.time.LocalDateTime

buildscript {
    dependencies { 
        classpath(group = "fr.brouillard.oss", name = "jgitver", version = "0.14.0")
    }
}

plugins {
    `java-library`
    `maven-publish`
    idea
    id("net.minecraftforge.gradle") version "5.0.11"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("wtf.gofancy.fancygradle") version "1.0.0"
}

val versionMc: String by project
val versionIC2: String by project
val versionBuildCraft: String by project
val versionJEI: String by project
val versionCodeChickenLib: String by project
val versionCoFHCore: String by project
val versionCoFHWorld: String by project
val versionRF: String by project
val versionThermalFoundation: String by project
val versionThermalExpansion: String by project
val versionAE2: String by project
val versionMantle: String by project
val versionTConstruct: String by project
val versionEnergyControl: String by project
val versionRailcraft: String by project
val versionThaumcraft: String by project

version = getGitVersion()
group = "mods.su5ed"
setProperty("archivesBaseName", "gregtechmod")

minecraft {
    mappings("stable", "39-1.12")

    runs {
        val config = Action<RunConfig> {
            properties(
                mapOf(
                    "forge.logging.markers" to "SCAN,REGISTRIES,REGISTRYDUMP,COREMODLOG",
                    "forge.logging.console.level" to "debug"
                )
            )
            workingDirectory = project.file("run").canonicalPath
            source(sourceSets["main"])
        }

        create("client", config)
        create("server", config)
    }
}

idea.module.inheritOutputDirs = true

java.toolchain { 
    languageVersion.set(JavaLanguageVersion.of(8))
}

fancyGradle {
    patches {
        patch(Patch.RESOURCES, Patch.COREMODS, Patch.CODE_CHICKEN_LIB, Patch.ASM)
    }
}

sourceSets {
    create("api")
    main {
        val output = sourceSets.getByName("api").output
        compileClasspath += output
        runtimeClasspath += output
    }
}

configurations {
    val shade = register("shade").get()
    val impl = implementation.get()

    impl.extendsFrom(shade)
    getByName("apiImplementation").extendsFrom(impl)
}

tasks {
    named("build") {
        dependsOn("reobfJar", "devJar", "apiJar")
    }

    named<Jar>("jar") {
        from(sourceSets.getByName("api").output)

        manifest {
            attributes(
                "Specification-Title" to "gregtechmod",
                "Specification-Vendor" to "Su5eD",
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Version" to archiveVersion.get(),
                "Implementation-Vendor" to "Su5eD",
                "Implementation-Timestamp" to LocalDateTime.now()
            )
        }
    }

    named<ShadowJar>("shadowJar") {
        dependsOn("classes")

        configurations = listOf(project.configurations["shade"])
        from(sourceSets.getByName("api").output)
        relocate("com.fasterxml", "mods.gregtechmod.repack.fasterxml")
        relocate("org.yaml", "mods.gregtechmod.repack.yaml")

        archiveClassifier.set("")
    }

    register<ShadowJar>("devJar") {
        dependsOn("classes")
        configurations = listOf(project.configurations["shade"])

        manifest {
            attributes(
                "Specification-Title" to "gregtechmod",
                "Specification-Vendor" to "Su5eD",
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Version" to archiveVersion.get(),
                "Implementation-Vendor" to "Su5eD",
                "Implementation-Timestamp" to LocalDateTime.now()
            )
        }

        relocate("com.fasterxml", "mods.gregtechmod.repack.fasterxml")
        relocate("org.yaml", "mods.gregtechmod.repack.yaml")

        from(sourceSets.main.get().output)
        val api = sourceSets.getByName("api")
        from(api.output.classesDirs)
        from(api.output.resourcesDir)

        archiveClassifier.set("dev")
    }

    register<Jar>("apiJar") {
        finalizedBy("reobfApiJar")

        val api = sourceSets.getByName("api")
        from(api.output.classesDirs)
        exclude("META-INF/**")
        archiveClassifier.set("api")
    }

    register<Jar>("sourceJar") {
        from(sourceSets.getByName("api").allSource)
    }

    named<ProcessResources>("processResources") {
        inputs.properties(
            "version" to project.version,
            "mcversion" to versionMc
        )

        // replace stuff in mcmod.info, nothing else
        filesMatching("mcmod.info") {
            expand(
                "version" to project.version,
                "mcversion" to versionMc
            )
        }
    }
}

reobf {
    create("jar") {
        dependsOn("shadowJar")
    }
    create("apiJar")
}

repositories {
    maven {
        name = "IC2"
        url = uri("https://maven.ic2.player.to/")
    }
    maven {
        name = "BuildCraft"
        url = uri("https://mod-buildcraft.com/maven")
    }
    maven {
        name = "CurseMaven"
        url = uri("https://www.cursemaven.com")
    }
    maven {
        name = "Progwml6 maven"
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }
    maven {
        name = "CoFH Maven"
        url = uri("https://maven.covers1624.net")
    }
}

dependencies {
    minecraft(group = "net.minecraftforge", name = "forge", version = "1.12.2-14.23.5.2855")
    
    implementation(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = "$versionIC2-ex112"))
    api(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = "$versionIC2-ex112", classifier = "api")) //GTE api depends on the ic2 api
    
    compileOnly(fg.deobf(group = "cofh", name = "RedstoneFlux", version = "1.12-$versionRF", classifier = "universal"))
    compileOnly(fg.deobf(group = "cofh", name = "CoFHCore", version = "1.12.2-$versionCoFHCore", classifier = "universal")) {
        exclude(group = "mezz.jei")
    }
    compileOnly(fg.deobf(group = "cofh", name = "CoFHWorld", version = "1.12.2-$versionCoFHWorld", classifier = "universal"))
    compileOnly(fg.deobf(group = "cofh", name = "ThermalFoundation", version = "1.12.2-$versionThermalFoundation", classifier = "universal"))
    implementation(fg.deobf(group = "codechicken", name = "CodeChickenLib", version = "1.12.2-$versionCodeChickenLib", classifier = "universal"))
    compileOnly(fg.deobf(group = "cofh", name = "ThermalExpansion", version = "1.12.2-$versionThermalExpansion", classifier = "universal")) { 
        exclude(group = "mezz.jei")
    }
    implementation(fg.deobf(group = "mezz.jei", name = "jei_$versionMc", version = versionJEI))
    compileOnly(fg.deobf(group = "com.mod-buildcraft", name = "buildcraft-api", version = versionBuildCraft))
    compileOnly(fg.deobf(curse(mod = "energy-control", projectId = 51195, fileId = versionEnergyControl.toLong())))
    implementation(fg.deobf(curse(mod = "railcraft", projectId = 51195, fileId = versionRailcraft.toLong())))
    compileOnly(fg.deobf(curse(mod = "applied-energistics-2", projectId = 223794, fileId = versionAE2.toLong())))
    compileOnly(fg.deobf(curse(mod = "thaumcraft", projectId = 223628, fileId = versionThaumcraft.toLong())))
    implementation(fg.deobf(group = "slimeknights.mantle", name = "Mantle", version = versionMantle))
    implementation(fg.deobf(group = "slimeknights", name = "TConstruct", version = versionTConstruct))

    val databind = "com.fasterxml.jackson.core:jackson-databind:2.9.0"
    implementation(databind)
    "shade"(databind)

    val yaml = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.0"
    implementation(yaml)
    "shade"(yaml)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = project.findProperty("archivesBaseName") as String
            version = project.version as String

            artifact(tasks.getByName("shadowJar"))
            artifact(tasks.getByName("apiJar"))
            artifact(tasks.getByName("devJar"))
        }
    }
    repositories {
        maven {
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Su5eD/GregTech-Experimental")
        }
    }
}

fun getGitVersion(): String {
    val jgitver = GitVersionCalculator.location(rootDir)
            .setNonQualifierBranches("forge-1.12.2")
            .setVersionPattern("\${M}\${<m}\${<meta.COMMIT_DISTANCE}\${-~meta.QUALIFIED_BRANCH_NAME}")
            .setStrategy(Strategies.PATTERN)
    return jgitver.version
}
