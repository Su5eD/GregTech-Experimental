import fr.brouillard.oss.jgitver.GitVersionCalculator
import fr.brouillard.oss.jgitver.Strategies
import net.minecraftforge.gradle.common.util.RunConfig
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
    id("net.minecraftforge.gradle") version "5.1.+"
    id("org.parchmentmc.librarian.forgegradle") version "1.+"
    id("wtf.gofancy.fancygradle") version "1.1.+"
}

group = "dev.su5ed"
version = getGitVersion()

val versionMc: String by project
val versionJackson: String by project
val versionIC2: String by project
val versionJEI: String by project

// Create api source set
val api: SourceSet by sourceSets.creating
val apiCompileOnly: Configuration by configurations.getting

val manifestAttributes = mapOf(
    "Specification-Title" to project.name,
    "Specification-Vendor" to "Su5eD",
    "Specification-Version" to "1",
    "Implementation-Title" to project.name,
    "Implementation-Version" to project.version,
    "Implementation-Vendor" to "Su5eD",
    "Implementation-Timestamp" to LocalDateTime.now()
)

jarJar.enable()

minecraft {
    mappings("parchment", "2022.07.10-1.18.2")

    runs {
        // Common config used for all runs
        val config = Action<RunConfig> {
            property("forge.logging.console.level", "debug")
            property("forge.logging.markers", "COREMODLOG")
            workingDirectory = project.file("run").canonicalPath
            // Don't exit the daemon when the game closes
            forceExit = false

            mods {
                create("gregtechmod") {
                    sources(sourceSets.main.get(), api)
                }
            }
        }

        create("client", config)
        create("server", config)
        
        create("data") {
            config(this)
            args(
                "--mod", "gregtechmod",
                "--all",
                "--output", file("src/generated/resources/"),
                "--existing", file("src/main/resources/")
            )
        }
    }
}

sourceSets.main {
    resources {
        srcDir("src/generated/resources")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))

    withSourcesJar()
}

val apiJar by tasks.registering(Jar::class) {
    finalizedBy("reobfApiJar")

    from(api.allSource, api.output)
    exclude("META-INF/**")
    archiveClassifier.set("api")
}

configurations {
    "apiCompileClasspath" {
        extendsFrom(api.get(), configurations.minecraft.get())
    }
    
    runtimeElements {
        setExtendsFrom(emptySet())
        outgoing {
            artifacts.clear()
            artifact(tasks.jarJar)
            artifact(apiJar)
        }
    }
}

tasks {
    jar {
        from(api.output)

        manifest.attributes(manifestAttributes)

        archiveClassifier.set("slim")
    }

    this.jarJar {
        finalizedBy("reobfJarJar")
        from(api.output)

        archiveClassifier.set("")
    }

    named<Jar>("sourcesJar") {
        from(api.allSource)
    }
    
    processResources {
        exclude(".cache/**")
    }

    assemble {
        dependsOn(jarJar, apiJar)
    }
}

// Create reobfuscation tasks for the jarJar and api jars
reobf {
    create("apiJar")
    create("jarJar")
}

repositories {
    maven {
        name = "Progwml6 maven"
        url = uri("https://dvs1.progwml6.com/files/maven")
    }
    // 1.18 IC2 builds are not available on maven yet, so we grab them from Jenkins using ivy as a workaround
    ivy {
        val build = versionIC2.substringBefore('+').substringAfterLast('.')
        name = "IC2 Jenkins"
        url = uri("https://jenkins.ic2.player.to/job/IC2/job/1.18/$build/artifact/tmp/out")
        patternLayout {
            artifact("[module]-[revision]-$versionMc-forge.[ext]")
        }
        metadataSources {
            artifact()
        }
    }
    mavenCentral()
}

dependencies {
    minecraft(group = "net.minecraftforge", name = "forge", version = "1.18.2-40.1.60")

    implementation(api.output)
    // GTE api depends on IC2 api
    apiCompileOnly(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2))
    implementation(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2))

    compileOnly(fg.deobf(group = "mezz.jei", name = "jei-$versionMc-common-api", version = versionJEI))
    compileOnly(fg.deobf(group = "mezz.jei", name = "jei-$versionMc-forge-api", version = versionJEI))
    runtimeOnly(fg.deobf(group = "mezz.jei", name = "jei-$versionMc-forge", version = versionJEI))

    include(api(group = "com.fasterxml.jackson.core", name = "jackson-annotations", version = versionJackson))
    include(api(group = "com.fasterxml.jackson.core", name = "jackson-core", version = versionJackson))
    include(api(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = versionJackson))
    include(implementation(group = "com.fasterxml.jackson.dataformat", name = "jackson-dataformat-yaml", version = versionJackson))
    include(implementation(group = "org.yaml", name = "snakeyaml", version = "1.30"))

    include(api(group = "one.util", name = "streamex", version = "0.8.1"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "gregtechmod"
            suppressAllPomMetadataWarnings()

            from(components["java"])
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

/**
 * Include a non-minecraft dependency in JarJar, also adding it to the minecraft_classpath
 */
fun DependencyHandler.include(dependency: ModuleDependency) {
    dependency.isTransitive = false
    val nextMajor = dependency.version!!.substringBefore('.').toInt() + 1
    minecraftLibrary(dependency)
    jarJar(dependency) { 
        jarJar.ranged(dependency, "[${dependency.version}, $nextMajor.0)")   
    }
}

fun getGitVersion(): String {
    val jgitver = GitVersionCalculator.location(rootDir)
        .setNonQualifierBranches("forge-1.18.2")
        .setStrategy(Strategies.SCRIPT)
        .setScript("print \"\${metadata.CURRENT_VERSION_MAJOR};\${metadata.CURRENT_VERSION_MINOR};\${metadata.CURRENT_VERSION_PATCH + metadata.COMMIT_DISTANCE}\"")
    return jgitver.version
}
