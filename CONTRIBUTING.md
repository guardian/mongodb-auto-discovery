# Contributing to mongodb-auto-discovery

:+1::tada: First off, thanks for taking the time to contribute! :tada::+1:

## Publish a new version

Configure [your BinTray credentials](https://github.com/softprops/bintray-sbt#publishing) (you need the API key from [here](https://bintray.com/profile/edit), click on "API Key").

To get a change published:

1. Make your changes on a branch
2. Bump up the version number in `build.sbt`
3. Create a pull request
4. Make sure the [Travis CI build](https://travis-ci.org/guardian/mongodb-auto-discovery) passes
5. Get your pull request reviewed
6. Merge in to `master`
7. Publish (from your local machine for now) using `sbt publish`
