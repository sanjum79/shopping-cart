FROM openjdk:17-alpine
ADD target/shopping-cart-0.0.1-SNAPSHOT.jar shopping-cart.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/urandom","-jar","shopping-cart.jar"]