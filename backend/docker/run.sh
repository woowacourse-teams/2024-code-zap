#!/usr/bin/env bash

abs_path=$(readlink -f "$0");
abs_dir=$(dirname "$abs_path");

backend_path=$(realpath "$abs_path/../..")

cd "$backend_path" || exit

./gradlew clean
./gradlew bootJar

jar_path="$backend_path/build/libs"
jar_filename=$(ls -tr "$jar_path"/*.jar | grep ".*\.jar")

mkdir "$abs_dir/app"
mv "$jar_filename" "$abs_dir/app/zap.jar"

cd "$abs_dir" || exit
docker compose up -d
