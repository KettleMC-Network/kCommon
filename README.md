# © kCommon

## 📝 Description
A library containing different util methods and common code for KettleMC.net plugins.

## ⚙ Content
- LuckPerms Utils for getting prefixes and suffixes
- Adventure Utils for sending (mini)messages to players
- Wrappers for managing SLAMS messages
- Java Utils containing different methods for simplifying code
- Bukkit Utils for simpler registration and other stuff
- Version utils for server software and version checking

## Ⓜ Maven/Gradle
```groovy
repositories {
    maven { url "https://repo.kettlemc.net/repository/maven-public/" }
}

dependencies {
    implementation "net.kettlemc:kcommon:VERSION"
}
```

## © Credits
- Some features have been inspired by prior work of [Cuuky](https://github.com/CuukyOfficial) and his CFW.
- The library [base64-itemstack](https://github.com/sya-ri/base64-itemstack) has been shadowed into this library for compatibility reasons.