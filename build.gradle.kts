import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
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
    java
    `maven-publish`
    id("net.minecraftforge.gradle") version "5.1.+"
    id("org.parchmentmc.librarian.forgegradle") version "1.+"
    id("com.github.johnrengelman.shadow") version "7.1.+"
    id("wtf.gofancy.fancygradle") version "1.1.+"
}

val versionMc: String by project
val versionIC2: String by project
val versionJEI: String by project

group = "dev.su5ed"
version = getGitVersion()

val api: SourceSet by sourceSets.creating
val apiDep: Configuration by configurations.creating
val shade: Configuration by configurations.creating

val relocateTarget = "dev.gregtechmod.repack"
val manifestAttributes = mapOf(
    "Specification-Title" to project.name,
    "Specification-Vendor" to "Su5eD",
    "Specification-Version" to "1",
    "Implementation-Title" to project.name,
    "Implementation-Version" to project.version,
    "Implementation-Vendor" to "Su5eD",
    "Implementation-Timestamp" to LocalDateTime.now()
)

minecraft {
    mappings("parchment", "2022.07.10-1.18.2")

    runs {
        val config = Action<RunConfig> {
            property("forge.logging.console.level", "debug")
            workingDirectory = project.file("run").canonicalPath
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

        all {
            lazyToken("minecraft_classpath") {
                shade.resolve().joinToString(separator = File.pathSeparator, transform = File::getAbsolutePath)
            }
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

configurations {
    "apiCompileClasspath" {
        extendsFrom(apiDep, configurations.minecraft.get())
    }

    apiElements {
        setExtendsFrom(setOf(apiDep))
    }

    implementation {
        extendsFrom(shade)
    }
}

val devJar by tasks.registering(ShadowJar::class) {
    dependsOn("classes", "apiClasses")

    configurations = listOf(shade)
    manifest.attributes(manifestAttributes)

    from(sourceSets.main.get().output)
    from(api.output)

    archiveClassifier.set("dev")
}

val apiJar by tasks.registering(Jar::class) {
    finalizedBy("reobfApiJar")

    from(api.allSource, api.output)
    exclude("META-INF/**")
    archiveClassifier.set("api")
}

tasks {
    jar {
        from(api.output)

        manifest.attributes(manifestAttributes)

        archiveClassifier.set("slim")
    }

    shadowJar {
        finalizedBy("reobfShadowJar")

        configurations = listOf(shade)
        manifest.attributes(manifestAttributes)

        from(api.output)

        archiveClassifier.set("")
    }

    named<Jar>("sourcesJar") {
        from(api.allSource)
    }

    withType<ShadowJar>() {
        sequenceOf("com.fasterxml", "org.yaml", "one.util")
            .forEach { relocate(it, "$relocateTarget.$it") }
    }

    assemble {
        dependsOn(shadowJar, devJar, apiJar)
    }
}

reobf {
    create("shadowJar")
    create("apiJar")
}

repositories {
    maven {
        name = "Progwml6 maven"
        url = uri("https://dvs1.progwml6.com/files/maven")
    }
    ivy {
        val build = versionIC2.split("+").first().substringAfterLast('.')
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
    "apiCompileOnly"(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2))
    // Manually change to compileOnly before running datagen. Thanks, IC2
    implementation(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2))

    compileOnly(fg.deobf(group = "mezz.jei", name = "jei-$versionMc-common-api", version = versionJEI))
    compileOnly(fg.deobf(group = "mezz.jei", name = "jei-$versionMc-forge-api", version = versionJEI))
    runtimeOnly(fg.deobf(group = "mezz.jei", name = "jei-$versionMc-forge", version = versionJEI))
    implementation(fg.deobf(curse(mod = "neat", projectId = 238372, fileId = 3593906)))

    apiDep(shade(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.13.1"))
    shade(group = "com.fasterxml.jackson.dataformat", name = "jackson-dataformat-yaml", version = "2.13.1")
    apiDep(shade(group = "one.util", name = "streamex", version = "0.8.1"))
}

afterEvaluate {
    val component = components["java"] as AdhocComponentWithVariants
    component.withVariantsFromConfiguration(configurations.runtimeElements.get(), ConfigurationVariantDetails::skip)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "gregtechmod"

            from(components["java"])

            artifact(devJar)
            artifact(apiJar)
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
        .setNonQualifierBranches("forge-1.18.2")
        .setStrategy(Strategies.SCRIPT)
        .setScript("print \"\${metadata.CURRENT_VERSION_MAJOR};\${metadata.CURRENT_VERSION_MINOR};\${metadata.CURRENT_VERSION_PATCH + metadata.COMMIT_DISTANCE}\"")
    return jgitver.version
}
