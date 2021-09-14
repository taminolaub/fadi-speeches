package software.pt.fashiondigitalspeech.analyzer.model

import java.time.LocalDate

data class AnalyzedLine(val author: String, val topic: String, val date: LocalDate, val words: Int)
