# Kekson
What happens if you order Gson on wish?<br>
<br>
Well me neither, but here you go!<br>
<br>

Maven:
```xml
<repositories>
    <repository>
        <id>kek</id>
        <url>https://mvn.kotw.dev/releases</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>dev.kotw</groupId>
        <artifactId>kekson</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

Gradle:
```groovy
repositories {
    maven {
        name = 'kek repo'
        url = 'https://mvn.kotw.dev/releases/'
    }
}

dependencies {
    implementation 'dev.kotw:kekson:1.0-SNAPSHOT'
}
```