#!/usr/bin/env bash

set -e

echo "Hello there"

sbt clean compile riffRaffNotifyTeamcity
