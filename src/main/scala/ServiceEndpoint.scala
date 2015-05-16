/**
 * Created by tzr on 16/05/15.
 */

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.IncomingConnection
import akka.http.scaladsl.model.HttpEntity.CloseDelimited
import akka.http.scaladsl.model._
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{FlattenStrategy, Sink, Flow, Source}
import akka.util.{ByteString, CompactByteString}

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps


import scala.concurrent.{Await, Future}

object ServiceEndpoint extends App {

  val dao = new PersonDAO()

  Await.ready(dao.createDdl, 5 second)
  Await.ready(dao.addPerson(Person("xxx", "A")), 5 second)
  Await.ready(dao.addPerson(Person("yyyy", "B")), 5 second)

  implicit val system = ActorSystem()
  implicit val materializer = ActorFlowMaterializer()

  val serverBinding = Http(system).bind(interface = "localhost", port = 8080)
  serverBinding.runForeach((connection: IncomingConnection) => {
    println("Accepted new connection from " + connection.remoteAddress)

    val src = Flow[HttpRequest].map(r => Source.apply(dao.streamPeople))
    val createResponse = Flow[Source[Person, Unit]].map((content: Source[Person, Unit]) => HttpResponse(entity = CloseDelimited(MediaTypes.`application/json`,
      content.map(x => ByteString.fromString(x.name)))))
    val totalFlow = src.via(createResponse)
    connection.handleWith(totalFlow)

  })

  readLine()
}
