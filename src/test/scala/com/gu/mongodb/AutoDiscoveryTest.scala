package com.gu.mongodb

import com.amazonaws.services.ec2.model.{DescribeInstancesRequest, DescribeInstancesResult, Instance, Reservation}
import com.gu.aws.{EC2Client, EC2DiscoveryService}
import org.scalatest._

import scala.collection.JavaConverters._

class AutoDiscoveryTest extends FlatSpec with Matchers {

  object TestEC2ClientThreeInstances extends EC2Client {
    def describeInstances(request: DescribeInstancesRequest): DescribeInstancesResult = {
      val instances = List("10.0.0.2", "10.0.0.3", "10.0.0.4").map(s => new Instance().withPrivateIpAddress(s)).asJavaCollection
      new DescribeInstancesResult().withReservations(new Reservation().withInstances(instances))
    }
  }

  object TestEC2ClientNoInstances extends EC2Client {
    def describeInstances(request: DescribeInstancesRequest): DescribeInstancesResult = {
      new DescribeInstancesResult().withReservations(new Reservation())
    }
  }

  "databaseHosts" should "produce a list of IP addresses if there are any database instances running" in {
    val autoDiscovery = new AutoDiscovery(EC2DiscoveryService, TestEC2ClientThreeInstances)
    autoDiscovery.databaseHosts("appstack", "PROD") should be(List("10.0.0.2", "10.0.0.3", "10.0.0.4"))
  }

  "databaseHosts" should "throw an exception if there are no database instances running" in {
    val autoDiscovery = new AutoDiscovery(EC2DiscoveryService, TestEC2ClientNoInstances)
    intercept[RuntimeException] {
      autoDiscovery.databaseHosts("appstack", "PROD")
    }
  }

  "mongoUri" should "produce a MongoDB URI" in {
    val autoDiscovery = new AutoDiscovery(EC2DiscoveryService, TestEC2ClientThreeInstances)
    val uri = autoDiscovery.mongoUri("username", "password", "database", "appstack", "PROD")
    uri.toString should be("mongodb://username:password@10.0.0.2:27017,10.0.0.3:27017,10.0.0.4:27017/database")
  }


}
