import slick.driver.H2Driver.api._
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

case class Person(name : String, surname : String)

class PersonDAO {

  class PersonTable(tag : Tag) extends Table[(String,String)](tag, "person") {
    def name = column[String]("NAME", O.PrimaryKey)
    def surname = column[String]("SURNAME")
    def * = (name, surname)
  }

  val people = TableQuery[PersonTable]

  val database = Database.forURL("jdbc:h2:mem:test1;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

  def createDdl = database.run(people.schema.create)

  def addPerson(person : Person) = database.run(people +=(person.name, person.surname))


  def streamPeople = {
    val q: Query[(Rep[String], Rep[String]), (String, String), Seq] = for (r <- people) yield (r.name, r.surname)
    database.stream(q.result).mapResult(p => Person(p._1, p._2))
  }

}

object test extends App {

  val dao = new PersonDAO()
  Await.ready(dao.createDdl, 5 second)
  Await.ready(dao.addPerson(Person("xxx", "A")), 5 second)
  Await.ready(dao.addPerson(Person("yyyy", "B")), 5 second)

  dao.streamPeople.foreach(println)

  readLine()

}

