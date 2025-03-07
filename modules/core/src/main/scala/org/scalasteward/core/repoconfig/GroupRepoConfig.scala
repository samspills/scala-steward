/*
 * Copyright 2018-2022 Scala Steward contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.scalasteward.core.repoconfig

import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.semiauto.deriveCodec
import org.scalasteward.core.data.Update

final case class GroupRepoConfig(
    pullRequests: PullRequestsConfig = PullRequestsConfig(),
    dependency: UpdatePattern
)

object GroupRepoConfig {
  implicit val groupPullConfigCodecConfig: Configuration =
    Configuration.default.withDefaults

  implicit val groupPullConfigCodec: Codec[GroupRepoConfig] =
    deriveCodec

  def configToSlowDownUpdatesFrequency(update: Update): String =
    update match {
      case s: Update.Single =>
        s"""dependencyOverrides = [{
           |  pullRequest = { frequency = "@monthly" },
           |  dependency = { groupId = "${s.groupId}", artifactId = "${s.artifactId.name}" }
           |}]""".stripMargin
      case g: Update.Group =>
        s"""dependencyOverrides = [{
           |  pullRequest = { frequency = "@monthly" },
           |  dependency = { groupId = "${g.groupId}" }
           |}]""".stripMargin
    }
}
