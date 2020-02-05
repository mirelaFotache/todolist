FROM openjdk:11-jdk
EXPOSE 8761
ADD /target/zuul-server-1.0-SNAPSHOT.jar zuul-server-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","zuul-server-1.0-SNAPSHOT.jar"]