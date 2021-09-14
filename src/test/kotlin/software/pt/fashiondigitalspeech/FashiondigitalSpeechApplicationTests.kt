package software.pt.fashiondigitalspeech

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import software.pt.fashiondigitalspeech.analyzer.SpeechEvaluationService

@SpringBootTest
class FashiondigitalSpeechApplicationTests {
	@Autowired
	lateinit var service: SpeechEvaluationService

	@Test
	fun contextLoads() {
	}

	@Test
	fun sameAmountOfSpeeches () {
		val result = service.evaluate(listOf(FashiondigitalSpeechApplicationTests::class.java.getResourceAsStream("/politics.csv")))

		assertNull(result.mostSpeeches)
		assertEquals("Alexander Abel", result.mostSecurity)
		assertEquals("Caesare Collins", result.leastWordy)
	}

	@Test
	fun noData () {
		val result = service.evaluate(listOf(FashiondigitalSpeechApplicationTests::class.java.getResourceAsStream("/noData.csv")))

		assertNull(result.mostSpeeches)
		assertNull(result.mostSecurity)
		assertNull(result.leastWordy)
	}

	@Test
	fun emptyLines () {
		val result = service.evaluate(listOf(FashiondigitalSpeechApplicationTests::class.java.getResourceAsStream("/emptyLines.csv")))

		assertEquals("Bernhard Belling", result.mostSpeeches)
		assertEquals("Alexander Abel", result.mostSecurity)
		assertEquals("Caesare Collins", result.leastWordy)
	}

	@Test
	fun sameQuantities () {
		val result = service.evaluate(listOf(FashiondigitalSpeechApplicationTests::class.java.getResourceAsStream("/sameQuantities.csv")))

		assertNull(result.mostSpeeches)
		assertNull(result.mostSecurity)
		assertNull(result.leastWordy)
	}

}
