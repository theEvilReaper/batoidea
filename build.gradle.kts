plugins {
    java
    `maven-publish`
    alias(libs.plugins.shadow)
}

group = "net.theEvilReaper.batoidea"
version = "1.0.0"
description = "batoidea"


repositories {
    mavenLocal()
    maven("https://repo.maven.apache.org/maven2/")
}

dependencies {
    implementation(libs.batoideaApi)
    //Dependencies for testing
    testImplementation(libs.junitApi)
    testImplementation(libs.mockitoCore)
    testImplementation(libs.mockitoJunit)
    testImplementation(libs.batoideaApi)
    testRuntimeOnly(libs.junitEngine)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    jar {
        dependsOn("shadowJar")
        archiveFileName.set("${rootProject.name}.${archiveExtension.getOrElse("jar")}")
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.properties["group"] as String?
            artifactId = project.name
            version = project.properties["version"] as String?
            from(components["java"])
        }
    }
}
