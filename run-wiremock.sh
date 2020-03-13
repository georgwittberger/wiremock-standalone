#!/bin/bash

SCRIPTFILE=$(readlink -f "$0")
BASEPATH=$(dirname "$SCRIPTFILE")

if [ -z $PORT ]; then
  PORT=8080
fi

if [ -r "$BASEPATH/admin-users.conf" ]; then
  if [ -z $WIREMOCK_EXTENSIONS ]; then
    WIREMOCK_EXTENSIONS=""
  else
    WIREMOCK_EXTENSIONS+=","
  fi
  WIREMOCK_EXTENSIONS+="io.github.georgwittberger.wiremockstandalone.AdminBasicAuthRequestFilter"
fi

if [ -z $WIREMOCK_OPTS ]; then
  WIREMOCK_OPTS=""
fi

WIREMOCK_OPTS+=" --port $PORT"

if [ ! -z $WIREMOCK_EXTENSIONS ]; then
  WIREMOCK_OPTS+=" --extensions $WIREMOCK_EXTENSIONS"
fi

java $JAVA_OPTS \
  --class-path "libs/slf4j-simple.jar:libs/wiremock-jre8-standalone.jar:libs/wiremock-standalone-extensions.jar" \
  com.github.tomakehurst.wiremock.standalone.WireMockServerRunner \
  $WIREMOCK_OPTS
