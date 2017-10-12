/*
 * Copyright 2016 Matthias Niehoff
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

package de.codecentric.spark.structured.streaming

import java.sql.Timestamp
import java.time.Instant

import org.apache.spark.sql.functions._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{ForeachWriter, Row, SparkSession}

/**
  * Created by matthiasniehoff on 13.01.17.
  */
object StreamingExample {

  case class WindowWithValue(timestamp:Timestamp,value: String)

  def main(args: Array[String]) {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    val spark = SparkSession
      .builder
      .master("local[*]")
      .appName("StructuredNetworkWordCount")
      .getOrCreate()

    import spark.implicits._

    val ds = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "albumDownloads")
      .load()
    val parsed = ds.selectExpr("CAST(value AS STRING)").as[String]
    val splitted = parsed
      .map(_.split("#;#"))
      .map(split => (new Timestamp(Instant.parse(split(1)).toEpochMilli), split(0)))
      .toDF("timestamp", "value")
      .as[WindowWithValue]

    val albumCount = splitted
      .withWatermark("timestamp", "20 seconds")
      .groupBy(
        window($"timestamp", "2 minutes", "1 minutes")
//        ,$"value"
      )
      .count()
      .orderBy($"window")

    val query = albumCount.writeStream.outputMode("complete").foreach(
      new ForeachWriter[Row]() {
        override def open(partitionId: Long, version: Long): Boolean = { true }

        override def process(value: Row): Unit = System.out.println(value)

        override def close(errorOrNull: Throwable): Unit = {}
      }
    ).start()

    query.awaitTermination()
  }
}
