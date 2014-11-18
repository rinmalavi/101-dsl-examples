import models.WonderCrud
import org.specs2.mutable.Specification

class ModelSpec extends Specification {

  "Wonder model" should {
    "get some wonders" in {
      (new WonderCrud{}).readAllC.map(_.length) must beGreaterThanOrEqualTo(3).await
    }
  }

}
