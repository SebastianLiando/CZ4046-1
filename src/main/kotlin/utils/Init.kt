package utils

/**
 * Loads the maze from the text file.
 *
 * @param mazeFileName The text file name.
 * @return The maze from the text file.
 */
fun loadMaze(mazeFileName: String): List<String> {
    val mazeString = getResourceAsString(mazeFileName)
    return mazeString.split(newLine).flatMap { line -> line.split(" ") }
}

/**
 * Reads a resource as [String].
 *
 * @param path The relative path to the resource file.
 */
private fun getResourceAsString(path: String) = object {}.javaClass.classLoader.getResource(path).readText()

/** Platform-dependent new line character. */
private val newLine
    get() = System.getProperty("line.separator")