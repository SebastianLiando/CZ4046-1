package utils

fun loadMaze(mazeFileName: String): List<String> {
    val mazeString = getResourceAsString(mazeFileName)
    return mazeString.split(newLine).flatMap { line -> line.split(" ") }
}

private fun getResourceAsString(path: String) = object {}.javaClass.classLoader.getResource(path).readText()

private val newLine
    get() = System.getProperty("line.separator")