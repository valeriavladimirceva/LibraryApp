package com.example.libraryapp.domain.model

data class LessonTime(val hour: Int, val minute: Int) {

    override fun toString(): String = "%02d:%02d".format(hour, minute)

    companion object {
        private val FORMAT = Regex("""^([01]\d|2[0-3]):([0-5]\d)$""")

        fun parse(text: String): LessonTime? {
            val match = FORMAT.matchEntire(text.trim()) ?: return null
            return LessonTime(
                hour = match.groupValues[1].toInt(),
                minute = match.groupValues[2].toInt()
            )
        }
    }
}