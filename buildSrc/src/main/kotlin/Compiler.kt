object CompilerArguments {
    const val inlineClasses = "-XXLanguage:+InlineClasses"
    const val coroutines = "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    const val experimental = "-Xopt-in=kotlin.Experimental"
}

object Jvm {
    const val target = "1.8"
}