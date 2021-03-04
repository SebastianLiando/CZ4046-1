package utils

import java.io.File

/**
 * Used to write CSV files.
 *
 * @param E The type of the data.
 * @property targetFile The file to write to.
 */
class CsvWriter<E>(private val targetFile: File) {
    init {
        // Create file directories if required
        File(targetFile.parent).mkdirs()
    }

    /**
     * Writes the data into the CSV file.
     *
     * @param data The data.
     * @param transform The transform function to transform a data into a [String]. By default
     * it calls the [toString] method.
     */
    fun write(data: List<List<E>>, transform: (E) -> String = { it.toString() }) {
        targetFile.createNewFile()

        var strToWrite = ""

        data.forEach { row ->
            strToWrite += row.joinToString(",") { transform(it) } + "\n"
        }

        targetFile.writeText(strToWrite)
    }
}