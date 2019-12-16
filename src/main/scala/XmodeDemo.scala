
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}

import scala.math._



object XmodeDemo {

  //case class POI(Name:String, Latitude:Double, Longitude:Double, Radius:Double)
  //broadcast:Broadcast[Array[Row]]

  def haversine(lat1:Double, lon1:Double, lat2:Double, lon2:Double ): Double ={
    val R = 6372.8
    val dLat=(lat2 - lat1).toRadians
    val dLon=(lon2 - lon1).toRadians
    val a = pow(sin(dLat/2),2) + pow(sin(dLon/2),2) * cos(lat1.toRadians) * cos(lat2.toRadians)
    val c = 2 * asin(sqrt(a))
    R * c * 1000
  }

  def process(broadcast:Broadcast[Array[Row]], advertiser_id: String, lat1:String, lon1:String): Array[(String, String, Double)] = {
    broadcast.value.map{ x =>
      (advertiser_id, x.get(0).toString, haversine(lat1.toDouble, lon1.toDouble, x.get(1).toString.toDouble, x.get(2).toString.toDouble))
    }.filter(_._3 <= 50)
  }

  def main(args: Array[String]): Unit = {
    if (args.length != 3) {
      System.err.println("Usage: BaseDataParquet, POI, outputParquet")
      System.exit(1)
    }

    var sparkConf = new SparkConf()
    sparkConf.setAppName("XmodeDemo")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new HiveContext(sc)


    val POIcsv = "s3://xmode-demo-bucket/poi.csv"
    val BaseDataSet = "s3://xmode-demo-bucket/file.parquet"
    val Output = "s3://xmode-demo-bucket/output"

    //Load Data
    val POI_RDD = sc.textFile(POIcsv).map(x => x.split(",")).map(x => Row(x(0), x(1), x(2), x(3)))
    val POI_broad = sc.broadcast(POI_RDD.collect())

    val BASEparquet = sqlContext.read.parquet(BaseDataSet)
    val BASErdd: RDD[Row] = BASEparquet.rdd

    val base_rdd = BASErdd.map{base =>
      process(POI_broad, base.get(0).toString, base.get(2).toString, base.get(3).toString)
    }

    val schemaString = "advertiser_id poi meters_distance"

    // Generate the schema based on the string of schema
    val fields = schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, nullable = true))
    val schema = StructType(fields)
    val rowRDD = base_rdd.map(attributes => Row(attributes(0), attributes(1), attributes(2)))
    //val base_df = sqlContext.createDataFrame(rowRDD, schema)


    rowRDD.saveAsTextFile(Output)

    sc.stop()


    }
}