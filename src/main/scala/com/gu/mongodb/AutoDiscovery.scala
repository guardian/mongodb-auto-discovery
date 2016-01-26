package com.gu.mongodb

import com.gu.aws.{EC2DiscoveryClient, EC2Client}

class AutoDiscovery(discoveryClient: EC2DiscoveryClient, ec2Client: EC2Client) {

  def databaseHosts(stage: String, stack: String): Seq[String] = {
    val tags = List(("Stack", stack), ("Stage", stage), ("App", "db"))
    val instanceAddressList = discoveryClient.addressesFromTags(tags, ec2Client)
    instanceAddressList.getOrElse(throw new RuntimeException("No mongo instances discovered"))
  }

  def mongoUri(username: String, password: String, database: String, stage: String, stack: String, port: Integer = 27017): MongoURI = {
    val servers = databaseHosts(stage, stack)
    new MongoURI(username, password, database, servers, port)
  }

}
