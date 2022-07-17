import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import fr.brouillard.oss.jgitver.GitVersionCalculator
import fr.brouillard.oss.jgitver.Strategies
import net.minecraftforge.gradle.common.util.RunConfig
import wtf.gofancy.fancygradle.script.extensions.deobf
import java.time.LocalDateTime
import java.util.jar.JarInputStream
import java.util.jar.JarOutputStream

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
    id("com.github.johnrengelman.shadow") version "7.1.+"
    id("wtf.gofancy.fancygradle") version "1.1.+"
}

group = "dev.su5ed"
version = getGitVersion()

val versionMc: String by project
val versionIC2: String by project
val versionJEI: String by project

val apiArtifact = Attribute.of("apiArtifact", Boolean::class.javaObjectType)

val api: SourceSet by sourceSets.creating
val apiCompileOnly: Configuration by configurations.getting
val customRuntimeMod: Configuration by configurations.creating
val generatedRuntimeApi: Configuration by configurations.creating {
    attributes {
        attribute(apiArtifact, false)
    }
}
val shade: Configuration by configurations.creating

val relocationTarget = "dev.su5ed.gregtechmod.repack"
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

            sources(sourceSets.main.get(), api)
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
            lazyToken("minecraft_classpath") {
                generatedRuntimeApi.resolve()
                    .joinToString(separator = File.pathSeparator, transform = File::getAbsolutePath)
            }
        }

        all {
            val existing = lazyTokens["minecraft_classpath"]
            lazyToken("minecraft_classpath") {
                (existing?.get()?.let(::listOf) ?: emptyList())
                    .plus(shade.resolve().map(File::getAbsolutePath))
                    .joinToString(separator = File.pathSeparator)
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
        extendsFrom(api.get(), configurations.minecraft.get())
    }
    compileClasspath {
        extendsFrom(customRuntimeMod)
    }
    implementation {
        extendsFrom(shade)
    }
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
            .forEach { relocate(it, "$relocationTarget.$it") }
    }

    whenTaskAdded {
        if (name == "runClient" || name == "runServer") {
            (this as JavaExec).classpath(customRuntimeMod.resolve())
        }
    }

    assemble {
        dependsOn(shadowJar, apiJar)
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
    apiCompileOnly(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2))
    customRuntimeMod(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2))
    generatedRuntimeApi(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2)) {
        attributes {
            attribute(apiArtifact, true)
        }
    }

    compileOnly(fg.deobf(group = "mezz.jei", name = "jei-$versionMc-common-api", version = versionJEI))
    compileOnly(fg.deobf(group = "mezz.jei", name = "jei-$versionMc-forge-api", version = versionJEI))
    runtimeOnly(fg.deobf(group = "mezz.jei", name = "jei-$versionMc-forge", version = versionJEI))

    api(shade(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.13.1"))
    shade(group = "com.fasterxml.jackson.dataformat", name = "jackson-dataformat-yaml", version = "2.13.1")
    api(shade(group = "one.util", name = "streamex", version = "0.8.1"))

    attributesSchema {
        attribute(apiArtifact)
    }
    artifactTypes.getByName("jar") {
        attributes.attribute(apiArtifact, false)
    }
    registerTransform(ApiArtifactTransform::class) {
        from.attribute(apiArtifact, false)
        to.attribute(apiArtifact, true)
    }
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

@CacheableTransform
abstract class ApiArtifactTransform : TransformAction<TransformParameters.None> {
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:InputArtifact
    abstract val inputArtifact: Provider<FileSystemLocation>

    private val keepPackage = "ic2/api/"

    override fun transform(outputs: TransformOutputs) {
        val inputFile = inputArtifact.get().asFile
        val nameWithoutExtension = inputFile.name.substringBeforeLast('.')
        val outputFile = outputs.file("${nameWithoutExtension}-api.jar")

        JarOutputStream(outputFile.outputStream()).use { out ->
            JarInputStream(inputFile.inputStream()).use { input ->
                var entry = input.nextEntry
                while (entry != null) {
                    if (entry.name.startsWith(keepPackage)) {
                        out.putNextEntry(entry)
                        out.write(input.readBytes())
                        out.closeEntry()
                    }

                    input.closeEntry()
                    entry = input.nextEntry
                }
            }
        }
    }
}
