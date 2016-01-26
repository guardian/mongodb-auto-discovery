package com.gu.aws

import com.amazonaws.services.ec2.model.{DescribeInstancesRequest, DescribeInstancesResult, Instance, Reservation}
import org.scalatest._

import scala.collection.JavaConverters._

class EC2DiscoveryClientTest extends FlatSpec with Matchers {

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

  "addressesFromTags" should "produce a list of IP addresses if there are any instances matching tags" in {
    val tags = List(("Stack", "appstack"), ("Stage", "PROD"), ("App", "db"))
    EC2DiscoveryService.addressesFromTags(tags, TestEC2ClientThreeInstances) should be(Some(List("10.0.0.2", "10.0.0.3", "10.0.0.4")))
  }

  "addressesFromTags" should "produce a None if there are no instances matching tags" in {
    val tags = List(("Stack", "appstack"), ("Stage", "PROD"), ("App", "db"))
    EC2DiscoveryService.addressesFromTags(tags, TestEC2ClientNoInstances) should be(None)
  }


}
