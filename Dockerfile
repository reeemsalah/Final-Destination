# base docker image
FROM maven:3.8.1-openjdk-15-slim AS build


COPY pom.xml /Final-Destination/

WORKDIR /Final-Destination

COPY recommendations-app/pom.xml recommendations-app/pom.xml
COPY NettyServer NettyServer
COPY Shared Shared
#ADD ../Shared Shared
#ADD Apps Apps
#
#RUN mvn -q -ntp -B -pl common -am dependency:go-offline
#COPY NettyServer NettyServer
#
#RUN mvn -q -B -pl NettyServer install



#RUN mvn clean package -pl :${APP_NAME} -am -DskipTests

FROM openjdk:15
ENV APP_NAME=recommendations-app

COPY --from=build Final-Destination/recommendations-app/target/recommendations-app-1.0-SNAPSHOT.jar /usr/local/lib/recommendations-app.jar

ENTRYPOINT ["java","-jar","/usr/local/lib/recommendations-app.jar"]