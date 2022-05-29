object CompilerArguments {
    const val inlineClasses = "-XXLanguage:+InlineClasses"
    const val coroutines = "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    const val experimental = "-Xopt-in=kotlin.Experimental"
    const val experimentalStd = "-Xuse-experimental=kotlin.ExperimentalStdlibApi"
}

object Jvm {
    const val target = "1.8"
}