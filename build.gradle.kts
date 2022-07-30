import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.10"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    id("org.asciidoctor.convert") version "1.5.8"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "com.yullivan"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    // for rest assured
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured")

    asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor:2.0.1.RELEASE")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    project.property("snippetsDir")!!.let { outputs.dir(it) }
}

tasks.asciidoctor {
    project.property("snippetsDir")!!.let { inputs.dir(it) }
    dependsOn(tasks.test)
}
