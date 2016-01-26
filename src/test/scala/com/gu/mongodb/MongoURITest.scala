package com.gu.mongodb

import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, PropSpec}

class MongoURITest extends PropSpec with PropertyChecks with Matchers {

  val allowedPasswordsCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_~/?!$&'()*+,=."

  val allowedPasswordsCharactersGenerator = Gen.someOf(allowedPasswordsCharacters).suchThat(_.nonEmpty).map(_.mkString(""))

  val alphaNumericalCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

  val alphaNumericalCharactersGenerator = Gen.someOf(alphaNumericalCharacters).suchThat(_.nonEmpty).map(_.mkString(""))


  val ipAddressGenerator = for {
    a <- Gen.choose(0, 255)
    b <- Gen.choose(0, 255)
    c <- Gen.choose(0, 255)
    d <- Gen.choose(0, 255)
  } yield s"$a.$b.$c.$d"
  val ipAddressesGenerator = Gen.nonEmptyContainerOf[List, String](ipAddressGenerator)

  property("parse URIs") {
    forAll(alphaNumericalCharactersGenerator, allowedPasswordsCharactersGenerator, alphaNumericalCharactersGenerator, ipAddressesGenerator) { (username: String, password: String, database: String, servers: Seq[String]) =>
      new MongoURI(username, password, database, servers)
    }
  }

}
