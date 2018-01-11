/*
 * Copyright 2013 David Edwards
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ivcinform.zookeeper

import org.apache.zookeeper.server.ZooKeeperServer
import org.scalatest.BeforeAndAfterAll
import org.scalatest.fixture.FunSuite

abstract class ZookeeperSuite extends FunSuite with BeforeAndAfterAll {
  type FixtureParam = Path

  private val zooKeeperServer: ZooKeeperServer = ZooKeeperServerFixture()
  implicit val zooKeeperClient = ZookeeperClientFixture(zooKeeperServer.getClientPort)

  override protected def afterAll {
    zooKeeperClient.close()
    zooKeeperServer.shutdown()
  }

  override protected def withFixture(test: OneArgTest) = {
    val root: String = zooKeeperClient.syncZookeeper.create("/test_", Array(), ACL.AnyoneAll, PersistentSequential)
    super.withFixture(test.toNoArgTest(Path(root)))
  }
}