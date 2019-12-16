organization := "com.highperformancespark"

name := "structured-streaming-examples"

publishMavenStyle := true

version := "0.0.1"

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.11.6")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")


parallelExecution in Test := false

fork := true

javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled", "-Djna.nosys=true")

// additional libraries
libraryDependencies ++= Seq(
  "com.opencsv" % "opencsv" % "5.0",
  "spark-csv_2.10" %% "spark-csv_2.11" % "1.5.0",
  "org.apache.spark" %% "spark-hive" % "2.0.0",
  "org.apache.spark" %% "spark-sql" % "2.0.0",
  "org.apache.spark" %% "spark-core" % "2.0.0")


scalacOptions ++= Seq("-deprecation", "-unchecked")

pomIncludeRepository := { x => false }

resolvers ++= Seq(
  "Cloudera Repository" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
  Resolver.sonatypeRepo("public"),
  "spark-2.0-snapshot" at "https://repository.apache.org/content/repositories/orgapachespark-1182/"
)

licenses := Seq("Apache License 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))
