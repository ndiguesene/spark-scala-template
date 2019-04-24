package weatherapi

final case class Nulle(s: Int)
/*import gigahorse._
import support.okhttp.Gigahorse

import scala.concurrent._, duration._
import play.api.libs.json._
import java.util.concurrent.Executors

import com.typesafe.scalalogging.LazyLogging

object Weather extends LazyLogging {
  lazy val http = Gigahorse.http(Gigahorse.config)
  private implicit val ec: ExecutionContextExecutor =
    ExecutionContext.fromExecutor(
      Executors.newFixedThreadPool(1)
    )

  def weather: Future[String] = {
    val baseUrl    = "https://www.metaweather.com/api/location"
    val locUrl     = baseUrl + "/search/"
    val weatherUrl = baseUrl + "/%s/"
    val rLoc       = Gigahorse.url(locUrl).get.addQueryString("query" -> "Addis Ababa")
    for {
      loc <- http.run(rLoc, parse)
      woeid    = (loc \ 0 \ "woeid").get
      rWeather = Gigahorse.url(weatherUrl format woeid).get
      weather <- http.run(rWeather, parse)
    } yield (weather \\ "weather_state_name")(0).as[String].toLowerCase
  }

  private def parse = Gigahorse.asString andThen Json.parse
}

// $COVERAGE-OFF$Disabling highlighting by default until a workaround for https://issues.scala-lang.org/browse/SI-8596 is found
object WeatherMain extends App with LazyLogging {
  val w = Await.result(Weather.weather, 25.seconds)
  logger.info(s"Hello! The weather in Addis Ababa is $w.")
  Weather.http.close()
}
// $COVERAGE-ON$*/
