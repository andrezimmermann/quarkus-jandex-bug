VERSION 0.7
FROM registry.gitlab.com/simpleps/graalvm-native-image-builder:ubuntu-22.3.0.1


BUILD_MODULE:
  COMMAND
  ARG --required MODULE
  COPY  . .
  WITH DOCKER \
        --allow-privileged 
       RUN --secret CI_JOB_TOKEN ./gradlew -Dquarkus.package.type=native-sources ${MODULE}:build jacocoTestReport
  END
  SAVE ARTIFACT ${MODULE}/build/reports/jacoco/test/jacocoTestReport.xml /artifacts/${MODULE}/jacocoTestReport.xml AS LOCAL ./artifacts/${MODULE}/build/reports/jacoco/test/jacocoTestReport.xml

build:
  WORKDIR /quarkustemplate/quarkustemplate/
  DO +BUILD_MODULE --MODULE=domain
  DO +BUILD_MODULE --MODULE=infrastructure
  DO +BUILD_MODULE --MODULE=app
  RUN ./gradlew jacocoTestReport jacocoTestReport


build-native:
    FROM +build
    ARG  MODULE=app 
    BUILD +code-cov
    WITH DOCKER \
          --allow-privileged 
       RUN --secret CI_JOB_TOKEN ./gradlew ${MODULE}:build  ${MODULE}:quarkusIntTest jacocoTestReport -Dquarkus.package.type=native 
    END
    RUN ls -l ${MODULE}/build/
    SAVE ARTIFACT ${MODULE}/build/${MODULE}-1.0.0-runner /application
    SAVE IMAGE --cache-hint


code-cov:
    FROM alpine/git

    RUN wget https://uploader.codecov.io/latest/alpine/codecov -O /usr/local/bin/codecov
    RUN chmod +x /usr/local/bin/codecov

    RUN --no-cache --secret CI_REPOSITORY_URL git clone $CI_REPOSITORY_URL /quarkustemplate

    WORKDIR /quarkustemplate
    RUN --secret CI_COMMIT_SHA git checkout $CI_COMMIT_SHA

    RUN mkdir -p /quarkustemplate/app/build/reports/jacoco/test/
    RUN mkdir -p /quarkustemplate/artifacts/build/reports/jacoco/test/
    RUN mkdir -p /quarkustemplate/domain/build/reports/jacoco/test/

    COPY +build/artifacts/app/jacocoTestReport.xml /quarkustemplate/app/build/reports/jacoco/test/jacocoTestReport.xml
    COPY +build/artifacts/infrastructure/jacocoTestReport.xml /quarkustemplate/infrastructure/build/reports/jacoco/test/jacocoTestReport.xml
    COPY +build/artifacts/domain/jacocoTestReport.xml /quarkustemplate/domain/build/reports/jacoco/test/jacocoTestReport.xml

    RUN ls /quarkustemplate/app/build/reports/jacoco/test/
    RUN ls /quarkustemplate/domain/build/reports/jacoco/test/

    RUN --secret CODECOV_TOKEN \
        --secret CI_BUILD_ID \
        --secret CI_BUILD_REF \
        --secret CI_BUILD_REF_NAME \
        --secret CI_COMMIT_REF_NAME \
        --secret CI_COMMIT_SHA \
        --secret CI_JOB_ID \
        --secret CI_PROJECT_PATH \
        --secret CI_REPOSITORY_URL \
        --secret GITLAB_CI \
        codecov --verbose

distroless-native:
    FROM quay.io/quarkus/quarkus-distroless-image:2.0
    ARG --required MODULE
    ARG --required TAG

    COPY  (+build-native/application --MODULE=${MODULE}) /application
    EXPOSE 8080
    USER nonroot

    ENTRYPOINT ["./application", "-Dquarkus.http.host=0.0.0.0"]

    SAVE IMAGE --push registry.gitlab.com/simpleps/quarkustemplate/${MODULE}:latest
    SAVE IMAGE --push registry.gitlab.com/simpleps/quarkustemplate/${MODULE}:${TAG}
