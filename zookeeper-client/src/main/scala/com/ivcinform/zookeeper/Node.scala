package com.ivcinform.zookeeper

/**
  * Represents a ''node'' in ZooKeeper.
  */
trait Node {
    /**
      * Returns the name of this node.
      *
      * @return the name of this node
      */
    def name: String

    /**
      * Returns the normalized path of this node.
      *
      * @return the normalized path of this node
      */
    def nodePath: Path

    /**
      * Returns the parent node.
      *
      * @return the parent node
      * @throws NoSuchElementException if removal of [[name]] from [[nodePath]] yields `""` or `"/"`
      */
    def parent: Node

    /**
      * Returns the parent node wrapped in an `Option`.
      *
      * @return a `Some` containing the parent node or `None` if removal of [[name]] from [[nodePath]] yields `""` or `"/"`
      */
    def parentOption: Option[Node]

    /**
      * Resolves the given `path` relative to this node.
      *
      * @param path the path to resolve relative to this node
      * @return a new node in which the given `path` is resolved relative to this node
      * @see [[Path]], method `resolve`, for details on path resolution
      */
    def resolve(path: String): Node

    /**
      * Resolves the given `path` relative to this node.
      *
      * @param path the path to resolve relative to this node
      * @return a new node in which the given `path` is resolved relative to this node
      * @see [[Path]], method `resolve`, for details on path resolution
      */
    def resolve(path: Path): Node

    /**
      * Creates this node.
      *
      * @param data the data to associate with this node, which may be empty, but not `null`
      * @param acl  an access control list to apply to this node, which must not be empty
      * @param disp the disposition of this node
      * @return a new node whose [[nodePath]] will differ is `disp` is either [[PersistentSequential]] or [[EphemeralSequential]]
      * @see [[SynchronousZookeeperClient.create]] for further details
      */
    def create(data: Array[Byte], acl: Seq[ACL], disp: Disposition): Node

    /**
      * Deletes this node.
      *
      * @param version a `Some` containing the expected version of the node or `None` if a version match is not required
      * @see [[SynchronousZookeeperClient.delete]] for further details
      */
    def delete(version: Option[Int] = None)

    /**
      * Returns the data and status of this node.
      *
      * @return a tuple containing the data and status of this node
      * @see [[SynchronousZookeeperClient.get]] for further details
      */
    def get(): (Array[Byte], Status)

    /**
      * Returns the data and status of this node and additionally sets a watch for any changes.
      *
      * @param fn a partial function invoked when applicable events occur
      * @return a tuple containing the data and status of this node
      * @see [[SynchronousWatchableZookeeperClient.get]] for further details
      */
    def get(fn: PartialFunction[Event, Unit]): (Array[Byte], Status)

    /**
      * Sets the data for this node.
      *
      * @param data    the data to associate with this node, which may be empty, but not `null`
      * @param version a `Some` containing the expected version of this node or `None` if a version match is not required
      * @return the status of the node
      * @see [[SynchronousZookeeperClient.set]] for further details
      */
    def set(data: Array[Byte], version: Option[Int]): Status

    /**
      * Returns the status of this node if it exists.
      *
      * @return a `Some` containing this node status or `None` if this node does not exist
      * @see [[SynchronousZookeeperClient.exists]] for further details
      */
    def exists(): Option[Status]

    /**
      * Returns the status of this node if it exists and additionally sets a watch for any changes.
      *
      * @param fn a partial function invoked when applicable events occur
      * @return a `Some` containing this node status or `None` if this node does not exist
      * @see [[SynchronousWatchableZookeeperClient.exists]] for further details
      */
    def exists(fn: PartialFunction[Event, Unit]): Option[Status]

    /**
      * Returns the children of this node.
      *
      * @return an unordered sequence containing each child node
      * @see [[SynchronousZookeeperClient.children]] for further details
      */
    def children(): Seq[Node]

    /**
      * Returns the children of this node and additionally sets a watch for any changes.
      *
      * @param fn a partial function invoked when applicable events occur
      * @return an unordered sequence containing each child node
      * @see [[SynchronousWatchableZookeeperClient.children]] for further details
      */
    def children(fn: PartialFunction[Event, Unit]): Seq[Node]

    /**
      * Returns the ACL and status of this node.
      *
      * @return a tuple containing the ACL and status of this node
      * @see [[SynchronousZookeeperClient.getACL]] for further details
      */
    def getACL(): (Seq[ACL], Status)

    /**
      * Sets the ACL for this node.
      *
      * @param acl an access control list to apply to this node, which must not be empty
      * @return the status of this node
      * @see [[SynchronousZookeeperClient.setACL]] for further details
      */
    def setACL(acl: Seq[ACL], version: Option[Int]): Status
}

/**
  * Constructs and deconstructs [[Node]] values.
  */
object Node {
    def apply(path: String)(implicit zk: ZookeeperClient): Node = apply(Path(path))(zk)

    def apply(path: Path)(implicit zk: ZookeeperClient): Node = new ImplSynchronousZookeeper(zk.syncZookeeper, path.normalize)

    def unapply(node: Node): Option[Path] =
        if (node == null) None else Some(node.nodePath)

    private class ImplSynchronousZookeeper(zk: SynchronousZookeeperClient, val nodePath: Path) extends Node {
        private implicit val _zk = zk

        lazy val name: String = nodePath.name

        lazy val parent: Node = Node(nodePath.parent)

        lazy val parentOption: Option[Node] = nodePath.parentOption match {
            case Some(p) => Some(Node(p))
            case _ => None
        }

        def resolve(path: String): Node = Node(this.nodePath resolve path)

        def resolve(path: Path): Node = resolve(path.path)

        def create(data: Array[Byte], acl: Seq[ACL], disp: Disposition): Node = Node(zk.create(nodePath.path, data, acl, disp))

        def delete(version: Option[Int]) = zk.delete(nodePath.path, version)

        def get(): (Array[Byte], Status) = zk.get(nodePath.path)

        def get(fn: PartialFunction[Event, Unit]): (Array[Byte], Status) = zk.watch(fn).get(nodePath.path)

        def set(data: Array[Byte], version: Option[Int]): Status = zk.set(nodePath.path, data, version)

        def exists(): Option[Status] = zk.exists(nodePath.path)

        def exists(fn: PartialFunction[Event, Unit]): Option[Status] = zk.watch(fn).exists(nodePath.path)

        def children(): Seq[Node] = zk.children(nodePath.path) map { c => Node(nodePath resolve c) }

        def children(fn: PartialFunction[Event, Unit]): Seq[Node] = zk.watch(fn).children(nodePath.path) map { c => Node(nodePath resolve c) }

        def getACL(): (Seq[ACL], Status) = zk.getACL(nodePath.path)

        def setACL(acl: Seq[ACL], version: Option[Int]): Status = zk.setACL(nodePath.path, acl, version)
    }
}
