#!/usr/bin/env sh
set -eu
mvn clean package
java -jar target/chronos-ledger.jar
