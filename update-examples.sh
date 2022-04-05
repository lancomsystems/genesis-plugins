#!/bin/bash

version="$(cat build.gradle.kts | grep 'version = "' | cut -d '"' -f 2)"

sed -i -e 's#\([[:space:]]*id("de\.lancom\.genesis\.[^"]*") version "\).*#\1'$version'"#' */example/build.gradle.kts
