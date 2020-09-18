FROM maven:3.6.3-openjdk-8-slim as build-stage
COPY src /build/src
COPY pom.xml /build
WORKDIR /build
RUN mvn clean package

FROM openjdk:8-jdk-alpine
ARG DEPENDENCY=build/target/dependency
COPY --from=build-stage ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build-stage ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build-stage ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","br.com.zup.bootcamp.proposal.ProposalApplicationó"]