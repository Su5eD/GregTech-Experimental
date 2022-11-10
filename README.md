<p align="center">
  <img src="https://raw.githubusercontent.com/Su5eD/GregTech-Experimental/forge-1.12.2/src/main/resources/GTE_Logo_medium.png" alt="Logo" align="center"/> 
</p>
<p align="center">
  <a href="https://ci.su5ed.dev/buildConfiguration/GregTechExperimental_BuildBranch">
      <img src="https://ci.su5ed.dev/app/rest/builds/buildType:id:GregTechExperimental_BuildBranch,branch:name:unstable/statusIcon.svg" align="center" alt="TeamCity build status">
  </a>
  <a href="https://www.curseforge.com/minecraft/mc-mods/gregtech-experimental">
    <img src="https://cf.way2muchnoise.eu/full_650005_downloads.svg" alt="Download Count" align="center">
  </a>
  <a href="https://www.curseforge.com/minecraft/mc-mods/gregtech-experimental">
    <img src="https://cf.way2muchnoise.eu/versions/650005.svg" alt="Available MC Versions" align="center">
  </a>
  <a href="https://discord.gg/JPvmNbe">
    <img src="https://discord.com/api/guilds/728217881514606612/widget.png?style=shield" alt="Discord Server" align="center"/>
  </a>
  <img src="https://img.shields.io/github/license/Su5eD/GregTech-Experimental?color=brightgreen" align="center" alt="License"/>
</p>  

# GregTech Experimental

## Table of Contents
- [About](#about)
  - [Gallery](#gallery)
- [Development Status](#development-status)
- [Documentation](#documentation)
- [Licensing](#licensing)
- [Downloads](#downloads)
    - [Users](#users)
    - [Gradle dependency](#gradle-dependency)
- [Building from source](#building-from-source)
    - [Prerequisites](#prerequisites)
    - [Compiling](#compiling)
    - [Importing into an IDE](#importing-into-an-ide)

## About
**GregTech**, originally created by Gregorious Technecities, is a mod that overhauls 
[IndustrialCraft2](https://www.curseforge.com/minecraft/mc-mods/industrial-craft), 
adding several new machines, fluids, tools, and adjusting recipes to make them fit with its system, 
and to make everything work well together.

**GregTech Experimental** is a fully-featured port of GregTech 3 for modern minecraft versions.
It offers all features of GT3, in their original form, unchanged. Machines, covers, upgrades, tools, as you know them.

### Gallery

Check out in-game images of GTE [here](https://gist.github.com/Su5eD/e1240f16335ec14d69d3eb671f588e2c).

## Development Status
GTE is currently nearing a full release, with the vast majority of machines implemented. The last few remaining
tasks are mainly code cleanup and bugfixes.  
Join our discord to keep up with the progress!

## Documentation
All documentation is available on the [wiki](https://github.com/Su5eD/GregTech-Experimental/wiki)

## Licensing
GTE is licensed under the [GNU Lesser General Public License](LICENSE).
All assets, except for logos, are licensed under the [CC0 1.0 Universal Public Domain Dedication](src/main/resources/LICENSE.assets).
The [GregTech Experimental logo](src/main/resources/GTE_Logo.png) is a derivate of the GregTech logo, and is licensed under the
[Creative Commons Attribution-NonCommercial 4.0 International Public License](https://creativecommons.org/licenses/by-nc/4.0).

## Downloads

### Users
Get the latest release on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/gregtech-experimental)!

### Gradle dependency

#### Declaring the repository

First, declare the Su5eD maven repository in your build.gradle:  
```groovy
repositories {
    maven {
        name = "Su5eD"
        url = "https://maven.su5ed.dev/releases"
    }
}
```

#### Declaring the dependency

Then, add the dependency to your project:
```groovy
dependencies {
    // The full jar
    implementation fg.deobf("mods.su5ed:gregtechmod:<version>")
    
    // The API
    implementation fg.deobf("mods.su5ed:gregtechmod:<version>:api")
}
```

## Building from source

### Prerequisites
Make sure you have [Git](https://git-scm.com/) and JDK 8 
([OracleJDK](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) or 
[OpenJDK](https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot)) installed

### Compiling
Note: Make sure to prepend `./` to every `gradlew` command you execute if you're on macOS / Linux
1. Open a Command Prompt (Windows) or Shell (macOS / Linux)
2. Clone the repository: `git clone https://github.com/Su5eD/GregTech-Experimental.git`
3. Open the cloned folder: `cd GregTech-Experimental`
4. Compile the mod: `gradlew build`. The resulting jars will be located in `build/libs`

### Importing into an IDE
If you're a developer and/or you want to set up a dev workspace, import the project into an IDE of your choice:

- IntelliJ IDEA (Recommended)  
    1. Import the `build.gradle` file as a project
    2. Execute the `genIntellijRuns` gradle task, either using a command prompt, 
       or the Gradle tab on the right.
- Eclipse
    1. Run `gradlew genEclipseRuns`
    2. Open Eclipse and go to Import > Gradle > Existing Gradle Project, 
       or run `gradlew eclipse` and then import the project
