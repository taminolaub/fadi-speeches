package software.pt.fashiondigitalspeech.analyzer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.FileInputStream
import java.net.URL

@RestController
class SpeechController {
    @Autowired
    private lateinit var evaluator: SpeechEvaluationService

    @GetMapping("/evaluate")
    fun evaluate(@RequestParam("url") urls: Array<String>) =
        // Converts the passed list of urls to inputstreams and passes them to the service
        evaluator.evaluate(urls.map {
            URL(it).openStream()
        }.toList())
}