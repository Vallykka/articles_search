FROM clojure:openjdk-16-lein-alpine as builder

WORKDIR /usr/src/articles/
COPY project.clj /usr/src/articles/
COPY resources/ /usr/src/articles/resources
COPY src/ /usr/src/articles/src

RUN lein uberjar

FROM openjdk:16-jdk-alpine3.12

COPY --from=builder /usr/src/articles/target/articles-0.1.0-SNAPSHOT-standalone.jar /articles-0.1.0-SNAPSHOT-standalone.jar
ENV SCOPUS_API_KEY "df935555c5d4b2fff68d20ebfaa37693"
CMD ["java", "-jar", "/articles-0.1.0-SNAPSHOT-standalone.jar"]