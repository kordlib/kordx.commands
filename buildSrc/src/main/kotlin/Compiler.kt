object CompilerArguments {
    const val inlineClasses = "-XXLanguage:+InlineClasses"
    const val coroutines = "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    const val experimental = "-opt-in=kotlin.Experimental"
    const val experimentalStd = "-opt-in=kotlin.ExperimentalStdlibApi"
}

object Jvm {
    const val target = "1.8"
}