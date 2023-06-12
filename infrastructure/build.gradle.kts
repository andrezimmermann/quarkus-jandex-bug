plugins {
    id("org.jetbrains.kotlin.plugin.allopen").version("1.8.10")
    id ("org.kordamp.gradle.jandex") version "1.1.0"
    id("io.quarkus")
    id ("java-test-fixtures")
}


repositories {
    mavenCentral()
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.persistence.Entity")
}

tasks.compileTestKotlin {
    dependsOn("jandex")
}

tasks.jacocoTestReport {
    dependsOn("jandex")
}

dependencies {
    implementation(project(":domain"))
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    val quarkusPlatformGroupId: String by project
    val quarkusPlatformArtifactId: String by project
    val quarkusPlatformVersion: String by project
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.quarkus:quarkus-jdbc-h2")

    testFixturesApi(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
}
