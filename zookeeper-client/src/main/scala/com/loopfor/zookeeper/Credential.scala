package com.loopfor.zookeeper

import org.apache.zookeeper.ZooKeeper
import scala.language._

/**
 * A ''credential'' with a ZooKeeper session.
 */
trait Credential {
  /**
   * Returns the session id.
   * 
   * @return the session id
   */
  def id: Long

  /**
   * Returns the session password.
   * 
   * @return the session password
   */
  def password: Array[Byte]
}

/**
 * Constructs and deconstructs [[Credential]] values.
 */
object Credential {
  /**
   * Constructs a new credential using the given session id and password.
   * 
   * @param id the session id
   * @param password the session password
   * @return a credential with the given `id` and `password`
   */
  def apply(id: Long, password: Array[Byte]): Credential = {
    val _id = id
    val _password = password
    new Credential {
      val id: Long = _id
      val password: Array[Byte] = _password

      override def toString: String = "Credential(id=" + id + ",password=" + convert(password) + ")"

      private[this] def convert(a: Array[Byte]) = {
        if (a == null)
          "null"
        else
          ("" /: a) {
            case (buf, b) if buf.isEmpty => "[" + hex(b)
            case (buf, b) => buf + " " + hex(b)
          } + "]"
      }

      private[this] def hex(b: Byte): String = {
        def hexChar(c: Int) = {
          (if (c < 10) '0' + c else 'a' + (c - 10)) toChar
        }
        hexChar((b >>> 4) & 0x0f).toString + hexChar(b & 0x0f)
      }
    }
  }

  /**
   * Used in pattern matching to deconstruct a credential.
   * 
   * @param cred selector value
   * @return a `Some` containing `id` and `password` if the selector value is not `null`, otherwise `None`
   */
  def unapply(cred: Credential): Option[(Long, Array[Byte])] =
    if (cred == null) None else Some(cred.id, cred.password)

  private[zookeeper] def apply(zk: ZooKeeper): Credential =
    apply(zk.getSessionId, zk.getSessionPasswd)
}
