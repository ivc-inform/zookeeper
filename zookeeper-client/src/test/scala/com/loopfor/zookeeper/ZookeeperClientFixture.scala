package com.loopfor.zookeeper

import scala.language._

object ZookeeperClientFixture {
    def apply(port: Int): ZookeeperClient = {
        val config = ConfigurationZookeeperClient(("localhost", port) :: Nil).withWatcher(
                (state: StateEvent, session: Session) ⇒ {
                    println(s"state: $state")
                    println(s"session: $session")
                }
        )
        ZookeeperClient(config)
    }
}
