package com.gu.mongodb

import java.net.{URISyntaxException, URI}

import scala.util.{Failure, Success, Try}

class MongoURI(username: String, password: String, database: String, servers: Seq[String], port: Int = 27017) {

  private val hostString = servers.map(s => s"$s:$port").mkString(",")

  private val credentials = (for {
    validUsername <- Option(username).filter(_.trim.nonEmpty)
    validPassword <- Option(password).filter(_.trim.nonEmpty)
  } yield s"$validUsername:$validPassword").getOrElse("")

  private val uri = Try(new URI(s"mongodb://$credentials@$hostString/$database")) match {
    case Success(validUri) => validUri
    case Failure(ex: URISyntaxException) => throw new RuntimeException ("Invalid characters in MongoDB URI")
    case Failure(ex: Throwable) => throw new RuntimeException ("Failed to construct MongoDB URI")
  }

  def userInfo: String = uri.getUserInfo
  def host: String = uri.getHost
  def path: String = uri.getPath

  override def toString: String = uri.toString
}
