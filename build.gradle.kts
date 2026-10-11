import java.security.MessageDigest

plugins { java }
group = "org.doraji"
version = "3.0.3-paper26.3.1"
repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}
dependencies {
    compileOnly("io.papermc.paper:paper-api:26.3.build.159-beta")
    testImplementation("io.papermc.paper:paper-api:26.2.build.129-stable")
    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v26.2:4.116.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
// MockBukkit v26.2 embeds 26.2 registry data. Runtime compatibility with 26.3
// is checked separately by verification/PortSmoke on a real Paper server.
configurations.matching { it.name == "testCompileClasspath" || it.name == "testRuntimeClasspath" }
    .configureEach { resolutionStrategy.force("io.papermc.paper:paper-api:26.2.build.129-stable") }
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    withSourcesJar()
}
tasks.withType<JavaCompile>().configureEach { options.encoding = "UTF-8" }
tasks.processResources {
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") { expand("version" to project.version) }
}
tasks.test { useJUnitPlatform() }
tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
tasks.jar {
    from(listOf("LICENSE.txt", "NOTICE.md")) { into("META-INF") }
}
tasks.named<Jar>("sourcesJar") {
    from(listOf("LICENSE.txt", "NOTICE.md")) { into("META-INF") }
}
val sourceZip = tasks.register<Zip>("sourceZip") {
    archiveFileName.set("NetherRatio-${project.version}-source.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
    from(projectDir) {
        include("src/**", "gradle/**", ".github/**", "publishing/**", "assets/**", "verification/**")
        include("*.md", "LICENSE.txt", "*.gradle.kts", "gradlew", "gradlew.bat", "build.ps1")
        include("checkstyle.xml", "spotbugs-exclude.xml", ".gitignore", ".gitattributes", "cliff.toml", "release.sh")
        exclude("verification/runtime/**", "verification/results/**", "verification/classes/**")
    }
}
val downloadZip = tasks.register<Zip>("downloadZip") {
    dependsOn(tasks.jar, sourceZip)
    archiveFileName.set("NetherRatio-${project.version}-download.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
    from(tasks.jar)
    from(sourceZip) { into("source") }
    from("LICENSE.txt", "NOTICE.md", "README.md", "INSTALL.ru.md", "CHANGELOG.md", "TESTING.md")
    from("src/main/resources/config.yml") { into("examples") }
}
val publicationZip = tasks.register<Zip>("publicationZip") {
    dependsOn(tasks.jar, tasks.named("sourcesJar"), sourceZip, downloadZip)
    archiveFileName.set("NetherRatio-${project.version}-publication.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
    from(tasks.jar, tasks.named("sourcesJar"), sourceZip, downloadZip)
    from("publishing") { into("publishing") }
    from("LICENSE.txt", "NOTICE.md", "README.md", "INSTALL.ru.md", "CHANGELOG.md", "TESTING.md")
}
tasks.register("releaseBundle") {
    dependsOn(tasks.build, tasks.named("sourcesJar"), sourceZip, downloadZip, publicationZip)
    doLast {
        val destination = projectDir.resolve("dist").apply { mkdirs() }
        val files = listOf(tasks.jar.get().archiveFile.get().asFile,
            tasks.named<Jar>("sourcesJar").get().archiveFile.get().asFile,
            sourceZip.get().archiveFile.get().asFile, downloadZip.get().archiveFile.get().asFile,
            publicationZip.get().archiveFile.get().asFile)
        destination.resolve("SHA256SUMS.txt").writeText(files.joinToString("\n", postfix = "\n") { file ->
            file.copyTo(destination.resolve(file.name), overwrite = true)
            MessageDigest.getInstance("SHA-256").digest(file.readBytes())
                .joinToString("") { "%02x".format(it.toInt() and 255) } + "  " + file.name
        }, Charsets.UTF_8)
    }
}
