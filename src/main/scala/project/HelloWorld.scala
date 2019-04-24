package project

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

object HelloWorld {

  def withGreeting()(df: DataFrame): DataFrame =
    df.withColumn("greeting", lit("hello world"))

}

object NumberFun {

  def isEven(n: Integer): Boolean =
    n % 2 == 0

  val isEvenUDF = udf[Boolean, Integer](isEven)

}

object Converter {

  def snakecaseify(s: String): String =
    s.toLowerCase().replace(" ", "_")

  def snakeCaseColumns(df: DataFrame): DataFrame =
    df.columns.foldLeft(df) { (acc, cn) =>
      acc.withColumnRenamed(cn, snakecaseify(cn))
    }

}
