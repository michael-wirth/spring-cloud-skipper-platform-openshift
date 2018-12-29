# Spring Cloud Skipper Platform for OpenShift

A [Spring Cloud Skipper](https://github.com/spring-cloud/spring-cloud-skipper) 
platform module that provides support for OpenShift.

This platform uses the [Spring Cloud Deployer for OpenShift](https://github.com/donovanmuller/spring-cloud-deployer-openshift)
.

## Usage

Simply add this platform as a dependency to your Skipper server project:

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-skipper-server-core</artifactId>
        </dependency>
        ...
        <dependency>
            <groupId>io.switchbit</groupId>
            <artifactId>spring-cloud-skipper-platform-openshift</artifactId>
            <version>2.0.0.BUILD-SNAPSHOT</version>
        </dependency>
        ...
    </dependencies>        
```
