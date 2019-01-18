# Overview
> Android SDK is a Java development kit for PlatON public chain provided by PlatON for Android developers.

# Build
```
    git clone https://github.com/PlatONnetwork/client-sdk-java.git
    cd client-sdk-java/
    ./gradlew clean jar            //Generate jar package
   
``` 

# Use

* config maven repository:  https://sdk.platon.network/nexus/content/groups/public/
* config maven or gradle in project

```
<dependency>
    <groupId>com.platon.client</groupId>
    <artifactId>core</artifactId>
    <version>0.3.0-android</version>
</dependency>
```

or

```
compile "com.platon.client:core:0.3.0-android"
```

* use in project

```
Web3j web3j = Web3jFactory.build(new HttpService("https://host:port"));
```


# Other
[more reference wiki](https://github.com/PlatONnetwork/wiki/wiki)