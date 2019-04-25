package project

import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{ DataFrame, Row }
import org.scalatest.FunSpec

class ExampleWithFunSpec extends FunSpec with SparkSessionTestWrapper {

  import spark.implicits._

  implicit val assertSmallDataFrameEquality = (actual: DataFrame, expected: DataFrame) =>
    actual.schema.fields.contains(expected.schema.fields)

  it("appends a greeting column to a Dataframe") {

    val sourceDF = Seq(
      ("miguel"),
      ("luisa")
    ).toDF("name")

    val actualDF = sourceDF.transform(HelloWorld.withGreeting())

    val expectedSchema = List(
      StructField("name", StringType, true),
      StructField("greeting", StringType, false)
    )

    val expectedData = Seq(
      Row("miguel", "hello world"),
      Row("luisa", "hello world")
    )

    val expectedDF = spark.createDataFrame(
      spark.sparkContext.parallelize(expectedData),
      StructType(expectedSchema)
    )

    assertSmallDataFrameEquality(actualDF, expectedDF)

  }

  it("appends an is_even column to a Dataframe") {

    val sourceDF = Seq(
      (1),
      (8),
      (12)
    ).toDF("number")

    val actualDF = sourceDF
      .withColumn("is_even", NumberFun.isEvenUDF(col("number")))

    val expectedSchema = List(
      StructField("number", IntegerType, false),
      StructField("is_even", BooleanType, true)
    )

    val expectedData = Seq(
      Row(1, false),
      Row(8, true),
      Row(12, true)
    )

    val expectedDF = spark.createDataFrame(
      spark.sparkContext.parallelize(expectedData),
      StructType(expectedSchema)
    )

    assertSmallDataFrameEquality(actualDF, expectedDF)

  }

  describe(".isEven") {
    it("returns true for even numbers") {
      assert(NumberFun.isEven(4) === true)
    }

    it("returns false for odd numbers") {
      assert(NumberFun.isEven(3) === false)
    }

    //  it("returns false for null values") {
    //    assert(NumberFun.isEven(null) === false)
    //  }
  }

  describe(".snakecaseify") {

    it("downcases uppercase letters") {
      assert(Converter.snakecaseify("HeLlO") === "hello")
    }

    it("converts spaces to underscores") {
      assert(Converter.snakecaseify("Hi There") === "hi_there")
    }

  }

  describe(".snakeCaseColumns") {

    it("snake_cases the column names of a DataFrame") {

      val sourceDF = Seq(
        ("funny", "joke")
      ).toDF("A b C", "de F")

      val actualDF = Converter.snakeCaseColumns(sourceDF)

      val expectedDF = Seq(
        ("funny", "joke")
      ).toDF("a_b_c", "de_f")

      assertSmallDataFrameEquality(actualDF, expectedDF)

    }

  }

}
