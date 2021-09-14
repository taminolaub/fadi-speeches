package software.pt.fashiondigitalspeech.analyzer

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import software.pt.fashiondigitalspeech.analyzer.model.AnalyzedFile
import software.pt.fashiondigitalspeech.analyzer.model.AnalyzedLine
import software.pt.fashiondigitalspeech.analyzer.model.Result
import software.pt.fashiondigitalspeech.analyzer.model.SpeakerData
import java.io.BufferedReader
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class SpeechEvaluationService {
    private val delimiter = ','

    fun evaluate(streams: List<InputStream>): Result {
        val files = mutableListOf<AnalyzedFile>()

        runBlocking {
            for (stream in streams) {
                // Starts a coroutine for the evaluation of each stream
                launch {
                    files += processStream(stream.bufferedReader())
                }
            }
        }
        // Combines the data gathered by the coroutines and does some final evaluation
        return analyzeFiles(files)
    }

    private fun processStream (reader: BufferedReader): AnalyzedFile {
        val analyzedFile = AnalyzedFile()
        var headerLineSkipped = false
        try {
            var line = reader.readLine()
            while (line != null) {
                if (!headerLineSkipped) {
                    // Skipping the first header line
                    headerLineSkipped = true
                    line = reader.readLine()
                    continue
                }
                if (line.indexOf(delimiter) >= 0) {
                    // Only analyze lines that contain semicolons
                    analyzedFile.addLine(processLine(line))
                }
                line = reader.readLine()
            }
        } finally {
            reader.close()
        }
        return analyzedFile
    }

    private fun processLine (line: String) =
        line.let {
            val parts = it.split(delimiter)
            AnalyzedLine(
                author = parts[0].trim(),
                topic = parts[1].trim(),
                date = LocalDate.parse(parts[2].trim(), DateTimeFormatter.ISO_DATE),
                words = parts[3].trim().toInt()
            )
        }

    private fun analyzeFiles (files: List<AnalyzedFile>): Result {
        // Will contain speaker data for all files
        val map = mutableMapOf<String, SpeakerData>()
        files.forEach {
            for ((speaker, data) in it.speakers) {
                // Add new speakerdata for speaker in case the speaker is not contained in map
                val speakerData = if (map.containsKey(speaker)) {
                    map.get(speaker)!!
                } else {
                    val d = SpeakerData()
                    map.put(speaker, d)
                    d
                }
                // Add data from the currently processed file to the authors data across all files
                speakerData.words += data.words
                speakerData.quantity2013 += data.quantity2013
                speakerData.quantitySecurity += data.quantitySecurity
            }
        }

        var minWordCount: Int = Int.MAX_VALUE
        var minWordSpeaker: String? = null
        var minWordSpeakerCount = 0

        var q13Count = 0
        var q13Speaker: String? = null
        var q13SpeakerCount = 0

        var qSecurityCount = 0
        var qSecuritySpeaker: String? = null
        var qSecuritySpeakerCount = 0


        // This part determines the person who fits the specified rules (least words, most security speeches and most speeches in 2013)
        // it also counts the amount of people having the exact same number
        for ((speaker, data) in map) {
            if (data.words < minWordCount) {
                minWordCount = data.words
                minWordSpeaker = speaker
                minWordSpeakerCount = 1
            } else if (data.words == minWordCount) {
                minWordSpeakerCount++
            }

            if (data.quantity2013 > q13Count) {
                q13Count = data.quantity2013
                q13Speaker = speaker
                q13SpeakerCount = 1
            } else if (data.quantity2013 == q13Count) {
                q13SpeakerCount++
            }

            if (data.quantitySecurity > qSecurityCount) {
                qSecurityCount = data.quantitySecurity
                qSecuritySpeaker = speaker
                qSecuritySpeakerCount = 1
            } else if (data.quantitySecurity == qSecurityCount) {
                qSecuritySpeakerCount++
            }
        }

        return Result(
            leastWordy = if (minWordSpeakerCount == 1) {
                minWordSpeaker
            } else {
                null
            },
            mostSpeeches = if (q13SpeakerCount == 1) {
                q13Speaker
            } else {
                null
            },
            mostSecurity = if (qSecuritySpeakerCount == 1) {
                qSecuritySpeaker
            } else {
                null
            }
        )
    }
}