import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Sink, Source, Flow}

object SampleClass extends App {

  implicit val actorSystem = ActorSystem()
  implicit val flowMaterializer = ActorFlowMaterializer()

  val src = Source((1 to 10))
  val doublerFlow = Flow[Int].map(_ * 2)
  val sink = Sink.foreach[Int](println)
  val runnableFlow = src.via(doublerFlow).to(sink)

  runnableFlow.run
  runnableFlow.run

}
