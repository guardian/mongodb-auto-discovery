# mongodb-auto-discovery

[![Build Status](https://travis-ci.org/guardian/mongodb-auto-discovery.svg?branch=master)](https://travis-ci.org/guardian/mongodb-auto-discovery)
 [ ![Download](https://api.bintray.com/packages/guardian/platforms/mongodb-auto-discovery/images/download.svg) ](https://bintray.com/guardian/platforms/mongodb-auto-discovery/_latestVersion)

## Motivation

Auto-discover your MongoDB instances in AWS and generate connection configuration during run-time.

Reserving private IP addresses in an AWS VPC is tricky and relying on a privately hosted Route 53 is a bit of a pain. By using instance tags we can instead auto-discover which hosts that are designated to run MongoDB and generate the configuration using that.

## Using the library

### Configure SBT to use BinTray

Set up your project for [resolving dependencies in BinTray](https://github.com/softprops/bintray-sbt#resolving-bintray-artifacts) and add the following resolver:

`resolvers ++= Seq(Resolver.bintrayRepo("guardian", "platforms"))`

### Make sure your instances have the right permissions

Your EC2 instances need to be able to read EC2 meta-data in order to find the MongoDB servers. The easiest way to set this up is to grant the following permissions to the instances in your stack(s) as part of your CloudFormation:

```
{
  "Effect": "Allow",
  "Action": "ec2:Describe*",
  "Resource": "*"
}
```

### Use it in your

### Play example

```
import com.gu.aws.{EC2, EC2DiscoveryService}
import com.gu.mongodb.AutoDiscovery
import play.api.Play
import play.api.Play.current

object MongoConfig {

  val mongoAutoDiscovery = new AutoDiscovery(EC2DiscoveryService, EC2)

  def uri: String = {
    Play.configuration.getString("mongodb.uri") match {
      case Some(uriFromConfig) => uriFromConfig
      case _ => {
        val uri = for {
          username <- Play.configuration.getString("mongodb.username")
          password <- Play.configuration.getString("mongodb.password")
          database <- Play.configuration.getString("mongodb.database")
        } yield mongoAutoDiscovery.mongoUri(username, password, database, Config.stage, Config.stack)
        uri.getOrElse(throw new RuntimeException("Could not construct Mongo URI. Missing mongodb.username, mongodb.password or mongodb.database.")).toString
      }
    }
  }

}
```
