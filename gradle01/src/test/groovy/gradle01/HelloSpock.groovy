package gradle01
import spock.lang.*

class HelloSpock extends spock.lang.Specification {

	def "length of Spock's and his friends' names"() {

		assert "name".size() == 4
	}

}
