import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import fr.brouillard.oss.jgitver.GitVersionCalculator
import fr.brouillard.oss.jgitver.Strategies
import net.minecraftforge.gradle.user.UserBaseExtension
import java.time.LocalDateTime

buildscript {
    dependencies { 
        classpath("fr.brouillard.oss:jgitver:0.14.0")
    }
}

plugins {
    java
    `maven-publish`
    id("net.minecraftforge.gradle.forge") version "FG_2.3-SNAPSHOT"
    id("com.github.johnrengelman.shadow") version "6.1.0"
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

version = getGitVersion()
group = "mods.su5ed"
setProperty("archivesBaseName", "gregtechmod")

configure<UserBaseExtension> {
    version = "1.12.2-14.23.5.2847"
    runDir = "run"
    replace("@VERSION@", project.version)

    mappings = "stable_39"
}

sourceSets {
    api
    main {
        val output = sourceSets.api.get().output
        compileClasspath += output
        runtimeClasspath += output
    }
}

configurations {
    val shade = register("shade").get()
    val impl = implementation.get()

    impl.extendsFrom(shade)
    apiImplementation.get().extendsFrom(impl)
}

tasks {
    named("build") {
        dependsOn("reobfShadowJar", "devJar", "apiJar")
    }

    named<Jar>("jar") {
        from(sourceSets.api.get().output)

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
        dependsOn("classes", "extractAnnotationsJar")
        finalizedBy("reobfShadowJar")

        configurations = listOf(project.configurations["shade"])
        from(sourceSets.api.get().output)
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
        val api = sourceSets.api.get()
        from(api.output.classesDirs)
        from(api.output.resourcesDir)

        archiveClassifier.set("dev")
    }

    register<Jar>("apiJar") {
        finalizedBy("reobfApiJar")

        val api = sourceSets.api.get()
        from(api.output.classesDirs)
        exclude("META-INF/**")
        archiveClassifier.set("api")
    }

    named<Jar>("sourceJar") {
        from(sourceSets.api.get().allSource)
    }

    named<ProcessResources>("processResources") {
        inputs.properties(
            "version" to project.version,
            "mcversion" to project.minecraft.version
        )

        // replace stuff in mcmod.info, nothing else
        filesMatching("mcmod.info") {
            expand(
                "version" to project.version,
                "mcversion" to project.minecraft.version
            )
        }
    }
}

reobf {
    create("apiJar")
    create("shadowJar")
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
    deobfCompile(group = "net.industrial-craft", name = "industrialcraft-2", version = "$versionIC2-ex112")
    apiImplementation(group = "net.industrial-craft", name = "industrialcraft-2", version = "$versionIC2-ex112", classifier = "api") //GTE api depends on the ic2 api

    deobfCompile(group = "cofh", name = "RedstoneFlux", version = "1.12-$versionRF", classifier = "universal")
    deobfCompile(group = "cofh", name = "CoFHCore", version = "1.12.2-$versionCoFHCore", classifier = "universal") {
        exclude(group = "mezz.jei")
    }
    deobfCompile(group = "cofh", name = "CoFHWorld", version = "1.12.2-$versionCoFHWorld", classifier = "universal")
    deobfCompile(group = "cofh", name = "ThermalFoundation", version = "1.12.2-$versionThermalFoundation", classifier = "universal")
    deobfCompile(group = "codechicken", name = "CodeChickenLib", version = "1.12.2-$versionCodeChickenLib", classifier = "universal")
    deobfCompile(group = "cofh", name = "ThermalExpansion", version = "1.12.2-$versionThermalExpansion", classifier = "universal") {
        exclude(group = "mezz.jei")
    }
    deobfCompile(group = "mezz.jei", name = "jei_$versionMc", version = versionJEI)

    deobfCompile(group = "com.mod-buildcraft", name = "buildcraft-api", version = versionBuildCraft)
    deobfCompile(group = "curse.maven", name = "energy-control-373450", version = "3207144")
    deobfCompile(group = "curse.maven", name = "railcraft-51195", version = "2687757")
    deobfCompile(group = "curse.maven", name = "applied-energistics-2-223794", version = "2747063")
    deobfCompile(group = "slimeknights.mantle", name = "Mantle", version = versionMantle)
    deobfCompile(group = "slimeknights", name = "TConstruct", version = versionTConstruct)

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
