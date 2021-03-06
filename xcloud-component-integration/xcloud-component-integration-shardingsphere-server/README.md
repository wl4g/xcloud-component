## Shardingsphere Server
> It's an enhanced package that integrates shardingsphere-proxy and shardingsphere-scaling


#### Developer's guide

- Step1: First, building of `shardingsphere`

```
git clone https://github.com/apache/shardingsphere.git
cd shardingsphere
git checkout 4.1.1
mvn clean install -DskipTests -Dmaven.test.skip=true -T 2C
```

- Step2: Building of `xcloud-component`

```
# git clone https://github.com/wl4g/xcloud-component.git
cd xcloud-component
mvn clean install -DskipTests -Dmaven.test.skip=true -T 2C
```

- Step3: Importion demo data

Directories:

```
├── demo_data
│   ├── group_sharding # Alpha
│   │   ├── sharding1.jpg
│   │   ├── sharding2.jpg
│   │   └── userdb-sharding.sql
│   └── sharding # Recommends
│       └── userdb-sharding.sql
```

> Notes: The example of non average slicing is not recommended for production (scenario: slicing according to different machine performance weight), because shardingsphere:5.0.0-alpha, It is recommended to use average sharding.


- Step4: Startup shardingsphere proxy(v4 and v5 Choose one)  

Startup classes:

```
com.wl4g.ShardingsphereProxy4
com.wl4g.ShardingsphereProxy5
```

