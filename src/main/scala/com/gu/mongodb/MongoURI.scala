package com.gu.mongodb

import java.net.URI

import scala.util.Try

class MongoURI(username: String, password: String, database: String, servers: Seq[String], port: Int = 27017) {

  private val hostString = servers.map(s => s"$s:$port").mkString(",")

  private val credentials = (for {
    validUsername <- Option(username).filter(_.trim.nonEmpty)
    validPassword <- Option(password).filter(_.trim.nonEmpty)
  } yield s"$validUsername:$validPassword").getOrElse("")

  private val uri = new URI(s"mongodb://$credentials@$hostString/$database")

  def userInfo: String = uri.getUserInfo
  def host: String = uri.getHost
  def path: String = uri.getPath

  override def toString: String = uri.toString
}
