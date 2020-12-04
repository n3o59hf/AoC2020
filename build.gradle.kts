
plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.4.20"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
}

application {
    mainClass.set("lv.n3o.aoc2020.Main")
}