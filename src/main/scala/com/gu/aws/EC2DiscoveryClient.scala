package com.gu.aws

import java.util

import com.amazonaws.services.ec2.model.{DescribeInstancesRequest, Filter}

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

trait EC2DiscoveryClient {

  def addressesFromTags(tags: List[(String, String)], eC2Client: EC2Client): Option[Seq[String]]

}

object EC2DiscoveryService extends EC2DiscoveryClient {

  def tagsAsFilters(tags: List[(String, String)]): util.Collection[Filter] = tags.map{
    case(name, value) => new Filter("tag:" + name).withValues(value)
  }.asJavaCollection

  def addressesFromTags(tags: List[(String, String)], eC2Client: EC2Client): Option[Seq[String]] = {
    val filters = tagsAsFilters(tags)

    Try(eC2Client.describeInstances(new DescribeInstancesRequest().withFilters(filters))) match {
      case Success(describeInstancesResult) => {
        val reservation = describeInstancesResult.getReservations.asScala.toList
        val instances = reservation.flatMap(r => r.getInstances.asScala)
        instances.flatMap(i => Option(i.getPrivateIpAddress)) match {
          case addresses if addresses.nonEmpty => Some(addresses)
          case _ => None
        }

      }
      case Failure(ex) => throw ex
    }
  }

}