## Bitledger
[![Build Status](https://travis-ci.org/cbeams/bitledger.svg?branch=master)](https://travis-ci.org/cbeams/bitledger)

Welcome! Bitledger aims to provide a general-purpose API and toolkit for building Bitcoin-like systems on the JVM. Right now we're laying the groundwork for the project, so there's not much here just yet. There's plenty to come however, and we'll have a blog up shortly at http://bitledger.io to share more about what we're up to.


### Artifacts
Use the following coordinates to grab snapshots for use in your own applications:

#### Gradle
    repositories {
        maven { url 'http://oss.jfrog.org/libs-snapshot' }
    }
    dependencies {
        compile 'bit.ledger:bitledger:0.1.0-SNAPSHOT'
    }

#### Maven
    <repository>
        <id>snapshots</id>
        <name>libs-snapshot</name>
        <url>http://oss.jfrog.org/libs-snapshot</url>
    </repository>

    <dependency>
        <groupId>bit.ledger</groupId>
        <artifactId>bitledger</artifactId>
        <version>0.1.0-SNAPSHOT</version>
    </dependency>

When ready, GA releases will be published to Bintray/JCenter and Maven Central. All project artifacts will be versioned according to [Semantic Versioning 2.0.0](http://semver.org) will be used.


### Documentation
You'll be able to explore the API at <http://bitledger.io/api> and we'll be building out user documentation under the [doc]() directory. Again, not much there at the moment, but stay tuned...


### Discussion
If you have questions or just want to say hello, join us via IRC at [#bitledger](http://webchat.freenode.net/?channels=bitledger) on Freenode. If you think a project like this sounds interesting (or not!) we'd love to hear your thoughts and ideas.
