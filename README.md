# mongodb-auto-discovery

[![Build Status](https://travis-ci.org/guardian/mongodb-auto-discovery.svg?branch=master)](https://travis-ci.org/guardian/mongodb-auto-discovery)
 [ ![Download](https://api.bintray.com/packages/guardian/platforms/mongodb-auto-discovery/images/download.svg) ](https://bintray.com/guardian/platforms/mongodb-auto-discovery/_latestVersion)

Auto-discover your MongoDB instances in AWS and generate connection configuration during run-time.

Reserving private IP addresses in an AWS VPC is tricky and relying on a privately hosted Route 53 is a bit of a pain. By using instance tags we can instead auto-discover which hosts that are designated to run MongoDB and generate the configuration using that.
