#!/bin/sh
#
# This is a minimal gradlew bootstrap; it downloads wrapper if missing.
#

APP_HOME="$( cd "$(dirname "$0")" >/dev/null 2>&1 && pwd )"
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# If wrapper jar missing, download it once (so GitHub Actions can survive)
if [ ! -f "$WRAPPER_JAR" ]; then
  mkdir -p "$(dirname "$WRAPPER_JAR")"
  curl -L -o "$WRAPPER_JAR" \
    "https://github.com/gradle/gradle/raw/v8.2.0/gradle/wrapper/gradle-wrapper.jar"
fi

exec java -jar "$WRAPPER_JAR" "$@"
