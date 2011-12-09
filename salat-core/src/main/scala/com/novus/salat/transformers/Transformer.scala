/** Copyright (c) 2010, 2011 Novus Partners, Inc. <http://novus.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  For questions and comments about this product, please see the project page at:
 *
 *  http://github.com/novus/salat
 *
 */
package com.novus.salat.transformers

import com.novus.salat._
import scala.tools.scalap.scalax.rules.scalasig._
import scala.math.{ BigDecimal => ScalaBigDecimal }
import com.novus.salat.util.Logging

object `package` {
  def isBigDecimal(path: String) = path match {
    case "scala.math.BigDecimal"    => true
    case "scala.package.BigDecimal" => true
    case "scala.Predef.BigDecimal"  => true
    case "scala.BigDecimal"         => true
    case _                          => false
  }

  def isFloat(path: String) = path match {
    case "scala.Float"     => true
    case "java.lang.Float" => true
    case _                 => false
  }

  def isChar(path: String) = path match {
    case "scala.Char"          => true
    case "java.lang.Character" => true
    case _                     => false
  }

  def isBigInt(path: String) = path match {
    case "scala.package.BigInt" => true
    case "scala.math.BigInt"    => true
    case "java.math.BigInteger" => true
    case _                      => false
  }

  def isJodaDateTime(path: String) = path match {
    case "org.joda.time.DateTime" => true
    case "org.scala_tools.time.TypeImports.DateTime" => true
    case _ => false
  }

  def isInt(path: String) = path match {
    case "java.lang.Integer" => true
    case "scala.Int"         => true
    case _                   => false
  }
}

abstract class Transformer(val path: String, val t: TypeRefType)(implicit val ctx: Context) extends Logging {
  def transform(value: Any)(implicit ctx: Context): Any = value
  def before(value: Any)(implicit ctx: Context): Option[Any] = Some(value)
  def after(value: Any)(implicit ctx: Context): Option[Any] = Some(value)
  def transform_!(dir: String, x: Any)(implicit ctx: Context): Option[Any] = {
    before(x).flatMap {
      x =>
        val t = transform(x)
        val a = after(t)
        log.info("""

transform_!: %s
  path: %s
  before: %s
  transform: %s
  after: %s

        """, dir, path, x, t, a)
        a
    }
  }
}

trait InContextTransformer {
  self: Transformer =>
  val grater: Option[Grater[_ <: AnyRef]]
}

