import com.v6ak.experimental.safeOption.{SafeOption, SafeSome, SafeNone}
import org.scalatest._

class SafeOptionSpec extends FlatSpec with Matchers {

	val someString = SafeSome("hello")

	val someUppercaseString = SafeSome("HELLO")

	"SafeSome" should "not accept null" in {
		a [NullPointerException] should be thrownBy {
			SafeSome(null)
		}
	}

	"SafeOption" should "accept null" in {
		SafeOption(null) should be (SafeNone[String])
	}

	"SafeNone" should "be empty" in {
		SafeNone[AnyRef].isEmpty should be (true)
		SafeNone[AnyRef].isDefined should be (false)
	}

	"SafeSome" should "be defined" in {
		someString.isEmpty should be (false)
		someString.isDefined should be (true)
	}

	"SafeSome" should "give the value" in {
		someString.get should be("hello")
		someUppercaseString.get should be("HELLO")
	}

	"SafeNone" should "not give the value" in {
		a [NoSuchElementException] should be thrownBy {
			SafeNone[String].get
		}
	}

	"SafeNone" should "have a hashCode value" in {
		(SafeNone[String]: Any).## should be (0)
		SafeNone.## should be (0)
		(SafeNone[String]: Any).hashCode should be (0)
		SafeNone.hashCode should be (0)
	}

	"SafeNone" should "be assignable to other SafeOptions" in {
		val a: SafeOption[String] = SafeNone[String]
		val b: SafeOption[Object] = SafeNone[Object]
	}

	"SafeNone" should "be equal to other SafeNone-s regardless of the underlying type" in {
		SafeNone[String] should be (SafeNone[List[Int]])
		SafeNone[List[Int]] should be (SafeNone[String])
		SafeNone[java.lang.Integer] should be (SafeNone[String])
		SafeNone[String] should be (SafeNone[java.lang.Integer])
		SafeNone[String] should be (SafeNone[java.lang.Object])
		SafeNone[java.lang.Object] should be (SafeNone[String])
	}

	"SafeNone" should "be assignable to other SafeOptions even in a generic method" in {
		def f[T <: AnyRef](x: T) {
			val a: SafeOption[T] = SafeNone[T]
		}
	}

	"map" should "return correct type" in {
		val x: SafeOption[String] = someUppercaseString.map(_.toLowerCase)
	}

	"map" should "return modified value for SafeSome" in {
		someUppercaseString.map(_.toLowerCase) should be (someString)
	}

	"map" should "return SafeNone for SafeNone" in {
		SafeNone[String].map(_.toLowerCase) should be (SafeNone[Object])
	}

	"map" should "be irelevant for SafeNone" in {
		SafeNone[String].map(_ => fail()) should be (SafeNone[Object])
	}

	"filter" should "not be relevant for SafeNone" in {
		SafeNone[String].map(_ => fail()) should be (SafeNone[String])
	}

	"filter" should "be relevant for SafeSome" in {
		someString.filter(_(0).isUpper) should be (SafeNone[String])
		someUppercaseString.filter(_(0).isUpper) should be (someUppercaseString)
	}

	"flatMap" should "not be relevant for SafeNone" in {
		SafeNone[String].flatMap(_ => fail()) should be (SafeNone[String])
	}

	"flatMap" should "return SafeNone for function returning SafeNone" in {
		someString.flatMap(_ => SafeNone[String]) should be (SafeNone[String])
	}

	"flatMap" should "return SafeSome for function returning SafeSome" in {
		someString.flatMap(x => SafeSome(x.toSeq)) should be (SafeSome(Seq('h', 'e', 'l', 'l', 'o')))
	}

	"SafeNone" should "be well pattern matched" in {
		val goodMatch = SafeNone[String] match {
			case SafeSome(x) => false
			case SafeNone() => true
		}
		goodMatch should be (true)
	}

	"SafeSome" should "be well pattern matched" in {
		val goodMatch = someString match {
			case SafeNone() => false
			case SafeSome(x) if x == "hello" => true
		}
		goodMatch should be (true)
	}

}