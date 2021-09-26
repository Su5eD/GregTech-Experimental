import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import fr.brouillard.oss.jgitver.GitVersionCalculator
import fr.brouillard.oss.jgitver.Strategies
import net.minecraftforge.gradle.common.util.RunConfig
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
    id("net.minecraftforge.gradle") version "5.1.+"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("wtf.gofancy.fancygradle") version "1.1.+"
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

group = "mods.su5ed"
version = getGitVersion()
setProperty("archivesBaseName", "gregtechmod")

minecraft {
    mappings("stable", "39-1.12")

    runs {
        val config = Action<RunConfig> {
            properties(
                mapOf(
                    "forge.logging.markers" to "SCAN,REGISTRIES",
                    "forge.logging.console.level" to "debug"
                )
            )
            workingDirectory = project.file("run").canonicalPath
            source(sourceSets.main.get())
            forceExit = false
        }

        create("client", config)
        create("server", config)
    }
}

idea.module.inheritOutputDirs = true

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))

fancyGradle {
    patches {
        resources
        coremods
        codeChickenLib
        asm
    }
}

val api: SourceSet by sourceSets.creating

sourceSets {
    main {
        compileClasspath += api.output
        runtimeClasspath += api.output
    }
}

val shade: Configuration by configurations.creating

configurations {
    implementation {
        extendsFrom(shade)
        
        getByName("apiImplementation").extendsFrom(this)
    }
}

val manifestAttributes = mapOf(
    "Specification-Title" to "gregtechmod",
    "Specification-Vendor" to "Su5eD",
    "Specification-Version" to "1",
    "Implementation-Title" to project.name,
    "Implementation-Version" to project.version,
    "Implementation-Vendor" to "Su5eD",
    "Implementation-Timestamp" to LocalDateTime.now()
)

tasks {
    jar {
        from(api.output)

        manifest.attributes(manifestAttributes)
    }

    shadowJar {
        dependsOn("jar")
        finalizedBy("reobfJar")
        
        configurations = listOf(shade)
        manifest.attributes(manifestAttributes)
        
        from(api.output)
        relocate("com.fasterxml", "mods.gregtechmod.repack.fasterxml")
        relocate("org.yaml", "mods.gregtechmod.repack.yaml")

        archiveClassifier.set("")
    }

    register<ShadowJar>("devJar") {
        dependsOn("classes")
        
        configurations = listOf(shade)
        manifest.attributes(manifestAttributes)

        relocate("com.fasterxml", "mods.gregtechmod.repack.fasterxml")
        relocate("org.yaml", "mods.gregtechmod.repack.yaml")

        from(sourceSets.main.get().output)
        from(api.output.classesDirs)
        from(api.output.resourcesDir)

        archiveClassifier.set("dev")
    }

    register<Jar>("apiJar") {
        finalizedBy("reobfApiJar")

        from(api.allSource, api.output.classesDirs)
        exclude("META-INF/**")
        archiveClassifier.set("api")
    }

    register<Jar>("sourceJar") {
        from(api.allSource)
    }

    processResources {
        inputs.properties(
            "version" to project.version,
            "mcversion" to versionMc
        )

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
    mavenCentral()
    maven {
        name = "IC2"
        url = uri("https://maven.ic2.player.to")
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
        url = uri("https://dvs1.progwml6.com/files/maven")
    }
    maven {
        name = "CoFH Maven"
        url = uri("https://maven.covers1624.net")
    }
}

dependencies {
    minecraft(group = "net.minecraftforge", name = "forge", version = "1.12.2-14.23.5.2855")
    
    implementation(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2))
    api(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2, classifier = "api")) //GTE api depends on the ic2 api
    
    compileOnly(fg.deobf(group = "cofh", name = "RedstoneFlux", version = versionRF, classifier = "universal"))
    compileOnly(fg.deobf(group = "cofh", name = "CoFHCore", version = versionCoFHCore, classifier = "universal")) {
        exclude(group = "mezz.jei")
    }
    compileOnly(fg.deobf(group = "cofh", name = "CoFHWorld", version = versionCoFHWorld, classifier = "universal"))
    compileOnly(fg.deobf(group = "cofh", name = "ThermalFoundation", version = versionThermalFoundation, classifier = "universal"))
    compileOnly(fg.deobf(group = "codechicken", name = "CodeChickenLib", version = versionCodeChickenLib, classifier = "universal"))
    compileOnly(fg.deobf(group = "cofh", name = "ThermalExpansion", version = versionThermalExpansion, classifier = "universal")) { 
        exclude(group = "mezz.jei")
    }
    runtimeOnly(fg.deobf(group = "mezz.jei", name = "jei_$versionMc", version = versionJEI))
    compileOnly(group = "mezz.jei", name = "jei_$versionMc", version = versionJEI, classifier = "api")
    compileOnly(fg.deobf(group = "com.mod-buildcraft", name = "buildcraft-main", version = versionBuildCraft))
    compileOnly(fg.deobf(curse(mod = "energy-control", projectId = 51195, fileId = versionEnergyControl.toLong())))
    compileOnly(fg.deobf(curse(mod = "railcraft", projectId = 51195, fileId = versionRailcraft.toLong())))
    compileOnly(fg.deobf(curse(mod = "applied-energistics-2", projectId = 223794, fileId = versionAE2.toLong())))
    compileOnly(fg.deobf(curse(mod = "thaumcraft", projectId = 223628, fileId = versionThaumcraft.toLong())))
    compileOnly(fg.deobf(group = "slimeknights.mantle", name = "Mantle", version = versionMantle))
    compileOnly(fg.deobf(group = "slimeknights", name = "TConstruct", version = versionTConstruct))
    compileOnly(fg.deobf(curse(mod = "spark", projectId = 361579, fileId = 3245793)))

    shade(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.9.0")
    shade(group = "com.fasterxml.jackson.dataformat", name = "jackson-dataformat-yaml", version = "2.9.0")
}

val projectArtifacts = sequenceOf("shadowJar", "apiJar", "devJar")
    .map(tasks::getByName)

artifacts { 
    projectArtifacts.forEach(::archives)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = project.findProperty("archivesBaseName") as String
            version = project.version as String
            
            projectArtifacts.forEach(::artifact)
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
        .setStrategy(Strategies.SCRIPT)
        .setScript("print \"\${metadata.CURRENT_VERSION_MAJOR};\${metadata.CURRENT_VERSION_MINOR};\${metadata.CURRENT_VERSION_PATCH + metadata.COMMIT_DISTANCE}\"")
    return jgitver.version
}
