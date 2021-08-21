<p align="center">
  <img src="https://raw.githubusercontent.com/Su5eD/GregTech-Experimental/forge-1.12.2/src/main/resources/GTE_Logo_medium.png" align="center"/> 
</p>
<p align="center">
  <a href="https://ci.su5ed.dev/buildConfiguration/GregTechExperimental_BuildBranch">
      <img src="https://ci.su5ed.dev/app/rest/builds/buildType:id:GregTechExperimental_BuildBranch,branch:name:unstable/statusIcon.svg" align="center" alt="TeamCity build status">
  </a>
  <a href="https://discord.gg/JPvmNbe">
    <img src="https://discord.com/api/guilds/728217881514606612/widget.png?style=shield" alt="Discord Server" align="center"/>
  </a>
  <img src="https://www.gnu.org/graphics/lgplv3-88x31.png" align="center" alt="LGPL logo"/>
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

Peek at some in-game images of GTE [here](https://gist.github.com/Su5eD/e1240f16335ec14d69d3eb671f588e2c).

## Development Status
GTE is currently in early development, which is split into 2 main phases - the first one consists of adding the base game mechanics and
materials, and the second one involves adding all the machines. Progress can be tracked under the 
[Projects](https://github.com/Su5eD/GregTech-Experimental/projects) tab. If you have any questions, use Discussions or Discord.

## Documentation
All documentation is available on the [wiki](https://github.com/Su5eD/GregTech-Experimental/wiki)

## Licensing
GTE is licensed under the [GNU Lesser General Public License](LICENSE).
All assets, except for logos, are licensed under the [CC0 1.0 Universal Public Domain Dedication](src/main/resources/LICENSE.assets).
The [GregTech Experimental logo](src/main/resources/GTE_Logo.png) is a derivate of the GregTech logo, and is licensed under the
[Creative Commons Attribution-NonCommercial 4.0 International Public License](https://creativecommons.org/licenses/by-nc/4.0).

## Downloads

### Users
Downloads are currently available under the 
[packages](https://github.com/Su5eD/GregTech-Experimental/packages/299128) tab. You'll want the jar with no extension.
GTE will be uploaded to CurseForge upon release.

### Gradle dependency
GTE is available on maven via Github Packages, which requires additional authentication. This can be done in a few
simple steps listed below.

#### 1. Generate a Personal Access Token
Visit your github [token settings](https://github.com/settings/tokens) and generate a new token 
with the `read:packages` scope. Make sure to copy it, as you can only see it once.

#### 2. Storing your Github credentials
Create a properties file for storing your username and token. Let's call it `github.properties`.
```properties
gpr.user=<username>
gpr.key=<key>
```
Copy & paste the text above into the file, and replace `<username>` and `<key>` with your username
and your access token respectively.

#### 3. Importing your credentials into the gradle project
Create a `Properties` object and load the `github.properties` file into it:  
```groovy
Properties githubProps = new Properties();
file("github.properties").withInputStream {
    githubProps.load(it)
}
```

#### 4. Declaring the repository
Declare the Github Packages repository and authenticate using your credentials:  
```groovy
repositories {
    maven {
        name = "GitHubPackages"
        url = "https://maven.pkg.github.com/Su5eD/GregTech-Experimental"
        credentials {
            username = githubProps.getProperty("gpr.user")
            password = githubProps.getProperty("gpr.key")
        }
    }
}
```

#### 5. Declaring the dependency
GregTech Experimental offers 3 artifacts:  
- The complete, obfuscated jar. Use `deobfCompile` for this one
- The API jar. Just the API and nothing else
- A deobfuscated jar. You can `compile` it **if you're using the same mappings as GTE** (currently `stable_39`). 
  Otherwise use the full jar.

Example dependency declarations:
```groovy
dependencies {
    // The default jar
    deobfCompile "mods.su5ed:gregtechmod:1.0-26"
    
    // The API
    deobfCompile "mods.su5ed:gregtechmod:1.0-26:api"
    
    // The deobfuscated jar
    compile "mods.su5ed:gregtechmod:1.0-26:dev"
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
