# Retrofit-Converter-MessagePack
[<img src="https://travis-ci.org/komamitsu/retrofit-converter-msgpack.svg?branch=master"/>](https://travis-ci.org/komamitsu/retrofit-converter-msgpack)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.komamitsu/retrofit-converter-msgpack/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.komamitsu/retrofit-converter-msgpack)
[![Coverage Status](https://coveralls.io/repos/komamitsu/retrofit-converter-msgpack/badge.svg?branch=master&service=github)](https://coveralls.io/github/komamitsu/retrofit-converter-msgpack?branch=master)

Retrofit Converter for MessagePack

## Prerequisites

- JDK 11 or later

## Install

### Gradle

```groovy
dependencies {
    implementation 'org.komamitsu:retrofit-converter-msgpack:x.x.x'
}
```

### Maven

```xml
<dependency>
    <groupId>org.komamitsu</groupId>
    <artifactId>retrofit-converter-msgpack</artifactId>
    <version>x.x.x</version>
</dependency>
```

## Usage

To use, supply an instance of this converter when building your Retrofit instance.
```
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.example.com")
    .addConverterFactory(MessagePackConverterFactory.create())
    .build();
```

