# Retrofit-Converter-MessagePack
Retrofit Converter for MessagePack

## Install

### Gradle

```groovy
dependencies {
    compile 'org.komamitsu:retrofit-converter-msgpack:1.0.0'
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

