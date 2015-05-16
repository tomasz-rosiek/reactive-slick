import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Zip, FlowGraph, Flow, Source}
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext

object ZipExample extends App {

  implicit val system = ActorSystem()
  implicit val context : ExecutionContext= system.dispatcher
  implicit val executor = system.dispatcher
  implicit val materializer = ActorFlowMaterializer()

  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)

  //TODO: use database source
  val elements =  Source(1 to 10).map(_.toString)

  val graph: Flow[HttpRequest, HttpResponse, Unit] = Flow() { implicit builder =>
    import FlowGraph.Implicits._

    val zip = builder.add(Zip[HttpRequest, String]())
    elements ~> zip.in1

    val out = zip.out.map{ case (req, num) => HttpResponse(200, List(), num+": "+req.uri) }.outlet
    (zip.in0, out)
  }

  Http().bindAndHandle(graph, "0.0.0.0", 9000)

}
