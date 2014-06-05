#!/bin/bash
#
# Publish Javadoc of successful CI builds to http://bitledger.io/api/snapshot
#
# See http://benlimmer.com/2013/12/26/automatically-publish-javadoc-to-gh-pages-with-travis-ci

if [ "$TRAVIS_REPO_SLUG" == "cbeams/bitledger" ] \
        && [ "$TRAVIS_JDK_VERSION" == "oraclejdk8" ] \
        && [ "$TRAVIS_PULL_REQUEST" == "false" ] \
        && [ "$TRAVIS_BRANCH" == "master" ]; then

    echo "Publishing javadoc..."

    git config --global user.email "travis@travis-ci.org"
    git config --global user.name "travis-ci"

    # Create a fresh clone in which to publish Javadoc to gh-pages
    cd $HOME
    git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/cbeams/bitledger gh-pages > /dev/null
    cd gh-pages
    git rm -rf ./api
    cp -Rf $HOME/api-temp ./api
    git add -f .
    git commit -m "Publish Javadoc from Travis CI build $TRAVIS_BUILD_NUMBER"
    git push -fq origin gh-pages > /dev/null

    echo "Published Javadoc to gh-pages. See http://bitledger.io/api"
fi
