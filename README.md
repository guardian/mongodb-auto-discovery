# mongodb-auto-discovery

[![Build Status](https://travis-ci.org/guardian/mongodb-auto-discovery.svg?branch=master)](https://travis-ci.org/guardian/mongodb-auto-discovery)

[Releases at Maven Central](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.gu%22%20AND%20a%3A%22mongodb-auto-discovery_2.11%22)

## Motivation

Auto-discover your MongoDB instances in AWS and generate connection configuration during run-time.

Reserving private IP addresses in an AWS VPC is tricky and relying on a privately hosted Route 53 is a bit of a pain. By using instance tags we can instead auto-discover which hosts that are designated to run MongoDB and generate the configuration using that.

## Using the library

### Add it as a dependency in `build.sbt`

```
libraryDependencies += "com.gu" %% "mongodb-auto-discovery" % "1.6"
```

### Make sure your instances have the right permissions

Your EC2 instances need to be able to read EC2 meta-data in order to find the MongoDB servers. The easiest way to set this up is to grant the following permissions to the instances in your stack(s) as part of your CloudFormation:

```
{
  "Effect": "Allow",
  "Action": "ec2:Describe*",
  "Resource": "*"
}
```

### Example using the Play framework

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
