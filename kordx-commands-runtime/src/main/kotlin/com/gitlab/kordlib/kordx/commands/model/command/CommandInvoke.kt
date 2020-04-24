package com.gitlab.kordlib.kordx.commands.model.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.internal.cast

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext>
        CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        execute: suspend CCONTEXT.() -> Unit
) {
    arguments = emptyList()
    execution = { context, _ -> execute(context.cast()) }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A>
        CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(first: Argument<A,
        ACONTEXT>, execute: suspend CCONTEXT.(first: A) -> Unit) {
    arguments = listOf(first)
    execution = { context, arguments -> execute(context.cast(), arguments[0].cast()) }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B>
        CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        execute: suspend CCONTEXT.(first: A, second: B) -> Unit
) {
    arguments = listOf(first, second)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C>
        CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C
        ) -> Unit
) {
    arguments = listOf(first, second, third)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D>
        CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D
        ) -> Unit
) {
    arguments = listOf(first, second, third, fourth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E>
        CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E
        ) -> Unit
) {
    arguments = listOf(first, second, third, fourth, fifth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F>
        CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F
        ) -> Unit
) {
    arguments = listOf(first, second, third, fourth, fifth, sixth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G>
        CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G
        ) -> Unit
) {
    arguments = listOf(first, second, third, fourth, fifth, sixth, seventh)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G, H>
        CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        eighth: Argument<H, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G,
                eighth: H
        ) -> Unit
) {
    arguments = listOf(first, second, third, fourth, fifth, sixth, seventh, eighth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast(), arguments[7].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G, H,
        I> CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        eighth: Argument<H, ACONTEXT>,
        ninth: Argument<I, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G,
                eighth: H,
                ninth: I
        ) -> Unit
) {
    arguments = listOf(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast(), arguments[7].cast(), arguments[8].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G, H,
        I, J> CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        eighth: Argument<H, ACONTEXT>,
        ninth: Argument<I, ACONTEXT>,
        tenth: Argument<J, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G,
                eighth: H,
                ninth: I,
                tenth: J
        ) -> Unit
) {
    arguments = listOf(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast(), arguments[7].cast(), arguments[8].cast(), arguments[9].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G, H,
        I, J, K> CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        eighth: Argument<H, ACONTEXT>,
        ninth: Argument<I, ACONTEXT>,
        tenth: Argument<J, ACONTEXT>,
        eleventh: Argument<K, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G,
                eighth: H,
                ninth: I,
                tenth: J,
                eleventh: K
        ) -> Unit
) {
    arguments =
            listOf(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast(), arguments[7].cast(), arguments[8].cast(), arguments[9].cast(), arguments[10].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G, H,
        I, J, K, L> CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        eighth: Argument<H, ACONTEXT>,
        ninth: Argument<I, ACONTEXT>,
        tenth: Argument<J, ACONTEXT>,
        eleventh: Argument<K, ACONTEXT>,
        twelfth: Argument<L, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G,
                eighth: H,
                ninth: I,
                tenth: J,
                eleventh: K,
                twelfth: L
        ) -> Unit
) {
    arguments =
            listOf(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast(), arguments[7].cast(), arguments[8].cast(), arguments[9].cast(), arguments[10].cast(), arguments[11].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M> CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        eighth: Argument<H, ACONTEXT>,
        ninth: Argument<I, ACONTEXT>,
        tenth: Argument<J, ACONTEXT>,
        eleventh: Argument<K, ACONTEXT>,
        twelfth: Argument<L, ACONTEXT>,
        thirteenth: Argument<M, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G,
                eighth: H,
                ninth: I,
                tenth: J,
                eleventh: K,
                twelfth: L,
                thirteenth: M
        ) -> Unit
) {
    arguments =
            listOf(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth, thirteenth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast(), arguments[7].cast(), arguments[8].cast(), arguments[9].cast(), arguments[10].cast(), arguments[11].cast(), arguments[12].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N> CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        eighth: Argument<H, ACONTEXT>,
        ninth: Argument<I, ACONTEXT>,
        tenth: Argument<J, ACONTEXT>,
        eleventh: Argument<K, ACONTEXT>,
        twelfth: Argument<L, ACONTEXT>,
        thirteenth: Argument<M, ACONTEXT>,
        fourteenth: Argument<N, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G,
                eighth: H,
                ninth: I,
                tenth: J,
                eleventh: K,
                twelfth: L,
                thirteenth: M,
                fourteenth: N
        ) -> Unit
) {
    arguments =
            listOf(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth, thirteenth, fourteenth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast(), arguments[7].cast(), arguments[8].cast(), arguments[9].cast(), arguments[10].cast(), arguments[11].cast(), arguments[12].cast(), arguments[13].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N, O> CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        eighth: Argument<H, ACONTEXT>,
        ninth: Argument<I, ACONTEXT>,
        tenth: Argument<J, ACONTEXT>,
        eleventh: Argument<K, ACONTEXT>,
        twelfth: Argument<L, ACONTEXT>,
        thirteenth: Argument<M, ACONTEXT>,
        fourteenth: Argument<N, ACONTEXT>,
        fifteenth: Argument<O, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G,
                eighth: H,
                ninth: I,
                tenth: J,
                eleventh: K,
                twelfth: L,
                thirteenth: M,
                fourteenth: N,
                fifteenth: O
        ) -> Unit
) {
    arguments =
            listOf(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth, thirteenth, fourteenth, fifteenth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast(), arguments[7].cast(), arguments[8].cast(), arguments[9].cast(), arguments[10].cast(), arguments[11].cast(), arguments[12].cast(), arguments[13].cast(), arguments[14].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N, O, P> CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        eighth: Argument<H, ACONTEXT>,
        ninth: Argument<I, ACONTEXT>,
        tenth: Argument<J, ACONTEXT>,
        eleventh: Argument<K, ACONTEXT>,
        twelfth: Argument<L, ACONTEXT>,
        thirteenth: Argument<M, ACONTEXT>,
        fourteenth: Argument<N, ACONTEXT>,
        fifteenth: Argument<O, ACONTEXT>,
        sixteenth: Argument<P, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G,
                eighth: H,
                ninth: I,
                tenth: J,
                eleventh: K,
                twelfth: L,
                thirteenth: M,
                fourteenth: N,
                fifteenth: O,
                sixteenth: P
        ) -> Unit
) {
    arguments =
            listOf(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth, thirteenth, fourteenth, fifteenth, sixteenth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast(), arguments[7].cast(), arguments[8].cast(), arguments[9].cast(), arguments[10].cast(), arguments[11].cast(), arguments[12].cast(), arguments[13].cast(), arguments[14].cast(), arguments[15].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N, O, P, Q> CommandBuilder<SCONTEXT, ACONTEXT, CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        eighth: Argument<H, ACONTEXT>,
        ninth: Argument<I, ACONTEXT>,
        tenth: Argument<J, ACONTEXT>,
        eleventh: Argument<K, ACONTEXT>,
        twelfth: Argument<L, ACONTEXT>,
        thirteenth: Argument<M, ACONTEXT>,
        fourteenth: Argument<N, ACONTEXT>,
        fifteenth: Argument<O, ACONTEXT>,
        sixteenth: Argument<P, ACONTEXT>,
        seventeenth: Argument<Q, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G,
                eighth: H,
                ninth: I,
                tenth: J,
                eleventh: K,
                twelfth: L,
                thirteenth: M,
                fourteenth: N,
                fifteenth: O,
                sixteenth: P,
                seventeenth: Q
        ) -> Unit
) {
    arguments =
            listOf(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth, thirteenth, fourteenth, fifteenth, sixteenth, seventeenth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast(), arguments[7].cast(), arguments[8].cast(), arguments[9].cast(), arguments[10].cast(), arguments[11].cast(), arguments[12].cast(), arguments[13].cast(), arguments[14].cast(), arguments[15].cast(), arguments[16].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N, O, P, Q, R> CommandBuilder<SCONTEXT, ACONTEXT,
        CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        eighth: Argument<H, ACONTEXT>,
        ninth: Argument<I, ACONTEXT>,
        tenth: Argument<J, ACONTEXT>,
        eleventh: Argument<K, ACONTEXT>,
        twelfth: Argument<L, ACONTEXT>,
        thirteenth: Argument<M, ACONTEXT>,
        fourteenth: Argument<N, ACONTEXT>,
        fifteenth: Argument<O, ACONTEXT>,
        sixteenth: Argument<P, ACONTEXT>,
        seventeenth: Argument<Q, ACONTEXT>,
        eighteenth: Argument<R, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G,
                eighth: H,
                ninth: I,
                tenth: J,
                eleventh: K,
                twelfth: L,
                thirteenth: M,
                fourteenth: N,
                fifteenth: O,
                sixteenth: P,
                seventeenth: Q,
                eighteenth: R
        ) -> Unit
) {
    arguments =
            listOf(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth, thirteenth, fourteenth, fifteenth, sixteenth, seventeenth, eighteenth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast(), arguments[7].cast(), arguments[8].cast(), arguments[9].cast(), arguments[10].cast(), arguments[11].cast(), arguments[12].cast(), arguments[13].cast(), arguments[14].cast(), arguments[15].cast(), arguments[16].cast(), arguments[17].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N, O, P, Q, R, S> CommandBuilder<SCONTEXT, ACONTEXT,
        CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        eighth: Argument<H, ACONTEXT>,
        ninth: Argument<I, ACONTEXT>,
        tenth: Argument<J, ACONTEXT>,
        eleventh: Argument<K, ACONTEXT>,
        twelfth: Argument<L, ACONTEXT>,
        thirteenth: Argument<M, ACONTEXT>,
        fourteenth: Argument<N, ACONTEXT>,
        fifteenth: Argument<O, ACONTEXT>,
        sixteenth: Argument<P, ACONTEXT>,
        seventeenth: Argument<Q, ACONTEXT>,
        eighteenth: Argument<R, ACONTEXT>,
        nineteenth: Argument<S, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G,
                eighth: H,
                ninth: I,
                tenth: J,
                eleventh: K,
                twelfth: L,
                thirteenth: M,
                fourteenth: N,
                fifteenth: O,
                sixteenth: P,
                seventeenth: Q,
                eighteenth: R,
                nineteenth: S
        ) -> Unit
) {
    arguments =
            listOf(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth, thirteenth, fourteenth, fifteenth, sixteenth, seventeenth, eighteenth, nineteenth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast(), arguments[7].cast(), arguments[8].cast(), arguments[9].cast(), arguments[10].cast(), arguments[11].cast(), arguments[12].cast(), arguments[13].cast(), arguments[14].cast(), arguments[15].cast(), arguments[16].cast(), arguments[17].cast(), arguments[18].cast())
    }
}

operator fun <SCONTEXT, ACONTEXT, CCONTEXT: CommandContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N, O, P, Q, R, S, T> CommandBuilder<SCONTEXT, ACONTEXT,
        CCONTEXT>.invoke(
        first: Argument<A, ACONTEXT>,
        second: Argument<B, ACONTEXT>,
        third: Argument<C, ACONTEXT>,
        fourth: Argument<D, ACONTEXT>,
        fifth: Argument<E, ACONTEXT>,
        sixth: Argument<F, ACONTEXT>,
        seventh: Argument<G, ACONTEXT>,
        eighth: Argument<H, ACONTEXT>,
        ninth: Argument<I, ACONTEXT>,
        tenth: Argument<J, ACONTEXT>,
        eleventh: Argument<K, ACONTEXT>,
        twelfth: Argument<L, ACONTEXT>,
        thirteenth: Argument<M, ACONTEXT>,
        fourteenth: Argument<N, ACONTEXT>,
        fifteenth: Argument<O, ACONTEXT>,
        sixteenth: Argument<P, ACONTEXT>,
        seventeenth: Argument<Q, ACONTEXT>,
        eighteenth: Argument<R, ACONTEXT>,
        nineteenth: Argument<S, ACONTEXT>,
        twentieth: Argument<T, ACONTEXT>,
        execute: suspend CCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G,
                eighth: H,
                ninth: I,
                tenth: J,
                eleventh: K,
                twelfth: L,
                thirteenth: M,
                fourteenth: N,
                fifteenth: O,
                sixteenth: P,
                seventeenth: Q,
                eighteenth: R,
                nineteenth: S,
                twentieth: T
        ) -> Unit
) {
    arguments =
            listOf(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth, thirteenth, fourteenth, fifteenth, sixteenth, seventeenth, eighteenth, nineteenth, twentieth)
    execution = { context, arguments ->
        execute(context.cast(),
                arguments[0].cast(), arguments[1].cast(), arguments[2].cast(), arguments[3].cast(), arguments[4].cast(), arguments[5].cast(), arguments[6].cast(), arguments[7].cast(), arguments[8].cast(), arguments[9].cast(), arguments[10].cast(), arguments[11].cast(), arguments[12].cast(), arguments[13].cast(), arguments[14].cast(), arguments[15].cast(), arguments[16].cast(), arguments[17].cast(), arguments[18].cast(), arguments[19].cast())
    }
}