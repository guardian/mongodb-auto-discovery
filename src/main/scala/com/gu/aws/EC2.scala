package com.gu.aws

import java.net.InetAddress

import com.amazonaws.regions.{Regions, Region}
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.{DescribeInstancesRequest, DescribeInstancesResult}

import scala.util.Try

trait EC2Client {

  def describeInstances(request: DescribeInstancesRequest): DescribeInstancesResult

}

object EC2 extends EC2Client {

  lazy val isAWS = Try(InetAddress.getByName("instance-data")).isSuccess

  def awsOption[T](f: => T): Option[T] = if (isAWS) Option(f) else None

  private val region: Region = awsOption(Regions.getCurrentRegion).getOrElse(Region.getRegion(Regions.EU_WEST_1))

  private val client: AmazonEC2Client = region.createClient(classOf[AmazonEC2Client], null, null)

  def describeInstances(request: DescribeInstancesRequest): DescribeInstancesResult = client.describeInstances(request)

}
