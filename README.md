# Retrofit-Converter-MessagePack
[<img src="https://travis-ci.org/komamitsu/retrofit-converter-msgpack.svg?branch=master"/>](https://travis-ci.org/komamitsu/retrofit-converter-msgpack)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.komamitsu/retrofit-converter-msgpack/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.komamitsu/retrofit-converter-msgpack)
[![Coverage Status](https://coveralls.io/repos/komamitsu/retrofit-converter-msgpack/badge.svg?branch=master&service=github)](https://coveralls.io/github/komamitsu/retrofit-converter-msgpack?branch=master)

Retrofit Converter for MessagePack

## Install

### Gradle

```groovy
dependencies {
    implementation 'org.komamitsu:retrofit-converter-msgpack:1.0.0'
}
```

### Maven

```xml
<dependency>
    <groupId>org.komamitsu</groupId>
    <artifactId>retrofit-converter-msgpack</artifactId>
    <version>1.0.0</version>
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

