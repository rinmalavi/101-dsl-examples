import org.specs2.mutable.Specification
import play.api.http.Status._
import play.api.test.FakeRequest

class RoutesSpec extends Specification {

    "respond to get wonders" in {
      val result= controllers.Rest.readAll()(FakeRequest())
      result.map(_.header.status) must equalTo(OK).await
  }

}
