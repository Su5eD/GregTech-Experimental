import fr.brouillard.oss.jgitver.GitVersionCalculator
import fr.brouillard.oss.jgitver.Strategies
import net.minecraftforge.gradle.common.util.RunConfig
import wtf.gofancy.fancygradle.script.extensions.deobf
import java.time.LocalDateTime
import java.util.function.Supplier
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
    id("wtf.gofancy.fancygradle") version "1.1.+"
}

group = "dev.su5ed"
version = getGitVersion()

val versionMc: String by project
val versionJackson: String by project
val versionIC2: String by project
val versionJEI: String by project

// Attribute used to mark select dependencies for api transformation
val apiArtifact = Attribute.of("apiArtifact", Boolean::class.javaObjectType)

// Create api source set
val api: SourceSet by sourceSets.creating
val apiCompileOnly: Configuration by configurations.getting
// Configuration used to add mod dependencies to non-data RunConfigs
val customRuntimeMod: Configuration by configurations.creating
// Configuration used for transformable dependencies that are added to datagen's runtime classpath
val generatedRuntimeApi: Configuration by configurations.creating {
    attributes {
        attribute(apiArtifact, false)
    }
}

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
            workingDirectory = project.file("run").canonicalPath
            // Don't exit the daemon when the game closes
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
            // Add datagen-only runtime dependencies to the minecraft_classpath
            lazyTokenConfig("minecraft_classpath", generatedRuntimeApi)
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
    
    // Add non-data runtime mods to the compile classpath
    compileClasspath {
        extendsFrom(customRuntimeMod)
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

    // Add dependencies from customRuntimeMod to the classpath to non-data runs
    whenTaskAdded {
        if (name == "runClient" || name == "runServer") {
            (this as JavaExec).classpath(customRuntimeMod.resolve())
        }
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
    // GTE api depends on IC2
    apiCompileOnly(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2))
    // Add IC2 to all non-data runs
    customRuntimeMod(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2))
    // Add the generated IC2 api to datagen
    generatedRuntimeApi(fg.deobf(group = "net.industrial-craft", name = "industrialcraft-2", version = versionIC2)) {
        attributes {
            // Request apiArtifact=true for this dependency, triggering the api transformer
            attribute(apiArtifact, true)
        }
    }

    compileOnly(fg.deobf(group = "mezz.jei", name = "jei-$versionMc-common-api", version = versionJEI))
    compileOnly(fg.deobf(group = "mezz.jei", name = "jei-$versionMc-forge-api", version = versionJEI))
    runtimeOnly(fg.deobf(group = "mezz.jei", name = "jei-$versionMc-forge", version = versionJEI))

    include(api(group = "com.fasterxml.jackson.core", name = "jackson-annotations", version = versionJackson))
    include(api(group = "com.fasterxml.jackson.core", name = "jackson-core", version = versionJackson))
    include(api(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = versionJackson))
    include(implementation(group = "com.fasterxml.jackson.dataformat", name = "jackson-dataformat-yaml", version = versionJackson))
    include(implementation(group = "org.yaml", name = "snakeyaml", version = "1.30"))

    include(api(group = "one.util", name = "streamex", version = "0.8.1"))

    // Register apiArtifact to the attribute schema
    attributesSchema {
        attribute(apiArtifact)
    }
    // All jars are not transformed by default
    artifactTypes.getByName("jar") {
        attributes.attribute(apiArtifact, false)
    }
    // Register api transformation
    registerTransform(ApiArtifactTransform::class) {
        from.attribute(apiArtifact, false)
        to.attribute(apiArtifact, true)
    }
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

/**
 * Append the contents of a configuration to a lazy token
 */
fun RunConfig.lazyTokenConfig(name: String, configuration: Configuration) {
    val oldToken: Supplier<String>? = lazyTokens["minecraft_classpath"]
    lazyToken(name) {
        val path = configuration.copyRecursive().resolve()
            .joinToString(separator = File.pathSeparator, transform = File::getAbsolutePath)
        
        if (oldToken == null) path
        else "${oldToken.get()}${File.pathSeparator}$path"
    }
}

fun getGitVersion(): String {
    val jgitver = GitVersionCalculator.location(rootDir)
        .setNonQualifierBranches("forge-1.18.2")
        .setStrategy(Strategies.SCRIPT)
        .setScript("print \"\${metadata.CURRENT_VERSION_MAJOR};\${metadata.CURRENT_VERSION_MINOR};\${metadata.CURRENT_VERSION_PATCH + metadata.COMMIT_DISTANCE}\"")
    return jgitver.version
}

/**
 * Due to an IC2 bug, IC2 will crash in datagen, and therefore the FML mod cannot be present at runtime.
 * On the other hand, GTE still requires IC2's api to be present. As a workaround, we transform and filter the IC2 jar,
 * leaving only API classes.
 */
@CacheableTransform
abstract class ApiArtifactTransform : TransformAction<TransformParameters.None> {
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:InputArtifact
    abstract val inputArtifact: Provider<FileSystemLocation>

    // Keep all files whose path starts with this string
    private val keepPackage = "ic2/api/"

    override fun transform(outputs: TransformOutputs) {
        val inputFile = inputArtifact.get().asFile
        val nameWithoutExtension = inputFile.name.substringBeforeLast('.')
        // Inherit the input file name, adding the api classifier to it
        val outputFile = outputs.file("${nameWithoutExtension}-api.jar")

        // Copy the input jar to output, filtering out entries that don't match keepPackage
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