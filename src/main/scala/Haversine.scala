import math._

object Haversine {
    //radius in km

  def haversine(lat1:Double, lon1:Double, lat2:Double, lon2:Double ): Double ={
    val R = 6372.8
    val dLat=(lat2 - lat1).toRadians
    val dLon=(lon2 - lon1).toRadians
    val a = pow(sin(dLat/2),2) + pow(sin(dLon/2),2) * cos(lat1.toRadians) * cos(lat2.toRadians)
    val c = 2 * asin(sqrt(a))
    R * c * 1000
  }

  def main(args: Array[String]): Unit = {
    println(haversine(33.87, -84.33, 33.85, -84.34))
    //println("yolo")
  }
}