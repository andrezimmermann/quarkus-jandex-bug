import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.7.22"
    jacoco
    id("io.gitlab.arturbosch.detekt")
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
}

subprojects {

    apply<JacocoPlugin>()
    apply<JavaPlugin>()
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.gitlab.arturbosch.detekt")


    detekt {
        autoCorrect = true
        buildUponDefaultConfig = true // preconfigure defaults
        allRules = false // activate all available (even unstable) rules.
    }


    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        systemProperty("cucumber.junit-platform.naming-strategy", "long")
        this.testLogging {
            this.showStandardStreams = true
            this.exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }        
    }

    tasks.jacocoTestReport {
        // Give jacoco the file generated with the cucumber tests for the coverage.
        executionData(files("$buildDir/jacoco/test.exec", "$buildDir/results/jacoco/cucumber.exec"))
        reports {
            xml.required.set(true)
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    val compileKotlin: KotlinCompile by tasks
    val compileTestKotlin: KotlinCompile by tasks

    compileKotlin.kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        javaParameters = true
    }

    compileTestKotlin.kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }


    dependencies{
        testImplementation("org.hamcrest:hamcrest-all:1.3")
        testImplementation("org.junit.platform:junit-platform-suite")
        testImplementation("org.junit.jupiter:junit-jupiter")        
        testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")

        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
    }


}
