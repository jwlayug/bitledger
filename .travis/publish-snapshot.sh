#!/bin/bash
#
# Publish snapshot builds from master on cbeams/bitledger, otherwise run the build without publication.

if [ "$TRAVIS_REPO_SLUG" == "cbeams/bitledger" ] && [ "$TRAVIS_BRANCH" == "master" ]; then
    ./gradlew publishSnapshot
else
    ./gradlew assemble
fi
