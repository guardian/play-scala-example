import com.gu.riffraff.artifact.BuildInfo

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, BuildInfoPlugin, RiffRaffArtifact, JDebPackaging, SystemdPlugin)
  .settings(
    name := """play-scala-example""",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      guice,
      "com.h2database" % "h2" % "1.4.199",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
    ),
    scalacOptions ++= List(
      "-encoding", "utf8",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xfatal-warnings"
    ),
    javaOptions in Universal ++= Seq(
      s"-Dpidfile.path=/dev/null",
      s"-J-Dlogs.home=/var/log/${packageName.value}",
      s"-J-Xloggc:/var/log/${packageName.value}/gc.log",
    ),
    riffRaffArtifactResources := Seq(
      (packageBin in Debian).value -> s"${name.value}/${name.value}.deb",
      baseDirectory.value / "riff-raff.yaml" -> "riff-raff.yaml",
      baseDirectory.value / "cfn.yaml" -> s"cloudformation/cfn.yaml"
    ),
    buildInfoKeys := {
      lazy val buildInfo = BuildInfo(baseDirectory.value)
      Seq[BuildInfoKey](
        BuildInfoKey.sbtbuildinfoConstantEntry("buildNumber", buildInfo.buildIdentifier),
        // so this next one is constant to avoid it always recompiling on dev machines.
        // we only really care about build time on teamcity, when a constant based on when
        // it was loaded is just fine
        BuildInfoKey.sbtbuildinfoConstantEntry("buildTime", System.currentTimeMillis),
        BuildInfoKey.sbtbuildinfoConstantEntry("gitCommitId", buildInfo.revision),
        BuildInfoKey.sbtbuildinfoConstantEntry("branch", buildInfo.branch)
      )
    },
)
