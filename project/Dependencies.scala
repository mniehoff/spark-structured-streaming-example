import sbt._

object Version {
  final val Scala      = "2.11.6"
  final val ScalaCheck = "1.12.5"
  final val Spark      = "2.1.0"
  final val SparkCassandra = "2.0.0-M3"
}

object Library {
  val scalaCheck      = "org.scalacheck"      %% "scalacheck"                 % Version.ScalaCheck
  val spark           = "org.apache.spark"    %% "spark-core"                 % Version.Spark
  val sparkStreaming  = "org.apache.spark"    %% "spark-streaming"            % Version.Spark
  val sparkSQL        = "org.apache.spark"    %% "spark-sql"                  % Version.Spark
  val sparkKafka      = "org.apache.spark"    %% "spark-streaming-kafka-0-10" % Version.Spark
  val sparkSQLKafka   = "org.apache.spark"    %% "spark-sql-kafka-0-10"       % Version.Spark
  val sparkCassandra  = "com.datastax.spark"  %% "spark-cassandra-connector"  % Version.SparkCassandra
}
