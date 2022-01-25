import java.text.SimpleDateFormat
import java.util.Date

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.3.3"
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

group = "pl.tuso"
version = SimpleDateFormat("yyyy-MM-dd HH.mm").format(Date())

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    paperDevBundle("1.18.1-R0.1-SNAPSHOT")
}

tasks {
    runServer {
        minecraftVersion("1.18.1")
    }
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("**/plugin.yml") {
            expand("version" to version)
        }
    }
}
