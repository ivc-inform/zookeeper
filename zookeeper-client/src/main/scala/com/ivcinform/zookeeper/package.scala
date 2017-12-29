
package com.ivcinform

import java.net.InetSocketAddress

import org.apache.zookeeper.KeeperException

import scala.language._

/**
  * A Scala API for ZooKeeper.
  */
package object zookeeper {
    type KeeperException = org.apache.zookeeper.KeeperException
    type APIErrorException = KeeperException.APIErrorException
    type AuthFailedException = KeeperException.AuthFailedException
    type BadArgumentsException = KeeperException.BadArgumentsException
    type BadVersionException = KeeperException.BadVersionException
    type ConnectionLossException = KeeperException.ConnectionLossException
    type DataInconsistencyException = KeeperException.DataInconsistencyException
    type InvalidACLException = KeeperException.InvalidACLException
    type InvalidCallbackException = KeeperException.InvalidCallbackException
    type MarshallingErrorException = KeeperException.MarshallingErrorException
    type NoAuthException = KeeperException.NoAuthException
    type NoChildrenForEphemeralsException = KeeperException.NoChildrenForEphemeralsException
    type NodeExistsException = KeeperException.NodeExistsException
    type NoNodeException = KeeperException.NoNodeException
    type NotEmptyException = KeeperException.NotEmptyException
    type NotReadOnlyException = KeeperException.NotReadOnlyException
    type OperationTimeoutException = KeeperException.OperationTimeoutException
    type RuntimeInconsistencyException = KeeperException.RuntimeInconsistencyException
    type SessionExpiredException = KeeperException.SessionExpiredException
    type SessionMovedException = KeeperException.SessionMovedException
    type SystemErrorException = KeeperException.SystemErrorException
    type UnimplementedException = KeeperException.UnimplementedException

    /**
      * Converts the tuple (''host'',''port'') to an Internet socket address.
      *
      * @return an `InetSocketAddress` composed from the given `addr` tuple
      */
    implicit def tupleToInetSocketAddress(addr: (String, Int)): InetSocketAddress = new InetSocketAddress(addr._1, addr._2)
    implicit def tuplesToInetSocketAddress(addr: (String, Int)*): Seq[InetSocketAddress] = addr.map(tupleToInetSocketAddress)
}
