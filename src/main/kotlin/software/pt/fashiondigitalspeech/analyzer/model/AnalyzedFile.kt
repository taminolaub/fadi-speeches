package software.pt.fashiondigitalspeech.analyzer.model

class AnalyzedFile {
    val speakers: MutableMap<String, SpeakerData> = mutableMapOf()

    fun addLine(line: AnalyzedLine) {
        val speakerData = if (speakers.containsKey(line.author)) {
            // If the speaker is already contained in the map
            speakers.get(line.author)!! // We can use the !!-operator here since we are sure that the key exists and points to a SpeakerData-object
        } else {
            // If the speaker is unknown create a new SpeakerData object and add it to the map
            val d = SpeakerData()
            speakers.put(line.author, d)
            d
        }

        // Analyze the line and add to the information contained in SpeakerData
        speakerData.words += line.words
        if (line.topic == "Innere Sicherheit") {
            speakerData.quantitySecurity++
        }
        if (line.date.year == 2013) {
            speakerData.quantity2013++
        }
    }
}

data class SpeakerData(var words: Int = 0, var quantity2013: Int = 0, var quantitySecurity: Int = 0)