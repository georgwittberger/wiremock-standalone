FROM maven:3.6.3-jdk-13 AS builder

WORKDIR /tmp

COPY extensions/ extensions/
COPY libs/ libs/
COPY build-libs.sh .

RUN chmod +x ./build-libs.sh
RUN ./build-libs.sh

FROM openjdk:13

RUN mkdir /wiremock
WORKDIR /wiremock

COPY --from=builder /tmp/libs/*.jar libs/
COPY __files/ __files/
COPY mappings/ mappings/
COPY *.conf .
COPY run-wiremock.sh .

RUN chmod +x ./run-wiremock.sh
CMD [ "./run-wiremock.sh" ]
