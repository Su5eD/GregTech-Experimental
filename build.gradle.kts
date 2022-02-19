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
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("wtf.gofancy.fancygradle") version "1.1.+"
}

val versionMc: String by project
val versionIC2: String by project
val versionJEI: String by project

group = "dev.su5ed"
version = getGitVersion()

val api: SourceSet by sourceSets.creating
val apiDep: Configuration by configurations.creating
val generated: SourceSet by sourceSets.creating {
    val main = sourceSets.main.get()

    java.srcDir(main.java.srcDirs)
    resources.srcDir(main.resources.srcDirs)
}
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
    mappings("parchment", "2022.01.23-1.18.1")

    runs {
        val config = Action<RunConfig> {
            property("forge.logging.console.level", "debug")
            sources(sourceSets.main.get(), api)
            workingDirectory = project.file("run").canonicalPath
            forceExit = false
        }

        create("client", config)
        create("server", config)

        create("data") {
            property("forge.logging.console.level", "debug")
            sources(generated, api)
            workingDirectory = project.file("run").canonicalPath
            forceExit = false
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
    "apiCompileOnly" {
        extendsFrom(apiDep, configurations.minecraft.get())
    }

    apiElements {
        setExtendsFrom(setOf(apiDep, shade))
    }

    implementation {
        extendsFrom(shade)
    }

    "generatedCompileOnly" {
        extendsFrom(apiDep, shade, compileOnly.get(), configurations.minecraft.get())
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
        name = "IC2"
        url = uri("https://maven.ic2.player.to")
    }
    maven {
        name = "Progwml6 maven"
        url = uri("https://dvs1.progwml6.com/files/maven")
    }
    ivy {
        val ic2build = versionIC2.split("+").first().substringAfterLast('.')

        name = "IC2 Jenkins"
        url = uri("https://jenkins.ic2.player.to/job/IC2/job/1.18/$ic2build/artifact/tmp/out")
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
    minecraft(group = "net.minecraftforge", name = "forge", version = "1.18.1-39.0.79")

    compileOnly(api.output)
    "generatedCompileOnly"(api.output)
    implementation(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2))
    apiDep(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2)) // TODO Use api jar when available

    runtimeOnly(fg.deobf(group = "mezz.jei", name = "jei-$versionMc", version = versionJEI))
    compileOnly(group = "mezz.jei", name = "jei-$versionMc", version = versionJEI, classifier = "api")

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
            groupId = project.group as String
            artifactId = "gregtechmod"
            version = project.version as String

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
        .setNonQualifierBranches("forge-1.18.1")
        .setStrategy(Strategies.SCRIPT)
        .setScript("print \"\${metadata.CURRENT_VERSION_MAJOR};\${metadata.CURRENT_VERSION_MINOR};\${metadata.CURRENT_VERSION_PATCH + metadata.COMMIT_DISTANCE}\"")
    return jgitver.version
}
