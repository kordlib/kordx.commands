package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.internal.cast

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext>
        CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(execute: suspend
EVENTCONTEXT.() -> Unit) {
    metaData[Arguments] = emptyList()
    metaData[Execution] = { context, arguments -> execute(context.cast()) }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A>
        CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(first: Argument<A,
        ARGUMENTCONTEXT>, execute: suspend EVENTCONTEXT.(first: A) -> Unit) {
    metaData[Arguments] = listOf(first)
    metaData[Execution] = { context, arguments -> execute(context.cast(), arguments[0].cast()) }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B>
        CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(first: A, second: B) -> Unit
) {
    metaData[Arguments] = listOf(first,second)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast()) }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C>
        CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
                first: A,
                second: B,
                third: C
        ) -> Unit
) {
    metaData[Arguments] = listOf(first,second,third)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast()) }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D>
        CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D
        ) -> Unit
) {
    metaData[Arguments] = listOf(first,second,third,fourth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast()) }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E>
        CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E
        ) -> Unit
) {
    metaData[Arguments] = listOf(first,second,third,fourth,fifth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F>
        CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F
        ) -> Unit
) {
    metaData[Arguments] = listOf(first,second,third,fourth,fifth,sixth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G>
        CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
                first: A,
                second: B,
                third: C,
                fourth: D,
                fifth: E,
                sixth: F,
                seventh: G
        ) -> Unit
) {
    metaData[Arguments] = listOf(first,second,third,fourth,fifth,sixth,seventh)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G, H>
        CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        eighth: Argument<H, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
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
    metaData[Arguments] = listOf(first,second,third,fourth,fifth,sixth,seventh,eighth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G, H,
        I> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        eighth: Argument<H, ARGUMENTCONTEXT>,
        ninth: Argument<I, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
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
    metaData[Arguments] = listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast(),arguments[8].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G, H,
        I, J> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        eighth: Argument<H, ARGUMENTCONTEXT>,
        ninth: Argument<I, ARGUMENTCONTEXT>,
        tenth: Argument<J, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
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
    metaData[Arguments] = listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast(),arguments[8].cast(),arguments[9].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G, H,
        I, J, K> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        eighth: Argument<H, ARGUMENTCONTEXT>,
        ninth: Argument<I, ARGUMENTCONTEXT>,
        tenth: Argument<J, ARGUMENTCONTEXT>,
        eleventh: Argument<K, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
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
    metaData[Arguments] =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast(),arguments[8].cast(),arguments[9].cast(),arguments[10].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G, H,
        I, J, K, L> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        eighth: Argument<H, ARGUMENTCONTEXT>,
        ninth: Argument<I, ARGUMENTCONTEXT>,
        tenth: Argument<J, ARGUMENTCONTEXT>,
        eleventh: Argument<K, ARGUMENTCONTEXT>,
        twelfth: Argument<L, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
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
    metaData[Arguments] =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast(),arguments[8].cast(),arguments[9].cast(),arguments[10].cast(),arguments[11].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        eighth: Argument<H, ARGUMENTCONTEXT>,
        ninth: Argument<I, ARGUMENTCONTEXT>,
        tenth: Argument<J, ARGUMENTCONTEXT>,
        eleventh: Argument<K, ARGUMENTCONTEXT>,
        twelfth: Argument<L, ARGUMENTCONTEXT>,
        thirteenth: Argument<M, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
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
    metaData[Arguments] =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast(),arguments[8].cast(),arguments[9].cast(),arguments[10].cast(),arguments[11].cast(),arguments[12].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        eighth: Argument<H, ARGUMENTCONTEXT>,
        ninth: Argument<I, ARGUMENTCONTEXT>,
        tenth: Argument<J, ARGUMENTCONTEXT>,
        eleventh: Argument<K, ARGUMENTCONTEXT>,
        twelfth: Argument<L, ARGUMENTCONTEXT>,
        thirteenth: Argument<M, ARGUMENTCONTEXT>,
        fourteenth: Argument<N, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
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
    metaData[Arguments] =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast(),arguments[8].cast(),arguments[9].cast(),arguments[10].cast(),arguments[11].cast(),arguments[12].cast(),arguments[13].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N, O> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        eighth: Argument<H, ARGUMENTCONTEXT>,
        ninth: Argument<I, ARGUMENTCONTEXT>,
        tenth: Argument<J, ARGUMENTCONTEXT>,
        eleventh: Argument<K, ARGUMENTCONTEXT>,
        twelfth: Argument<L, ARGUMENTCONTEXT>,
        thirteenth: Argument<M, ARGUMENTCONTEXT>,
        fourteenth: Argument<N, ARGUMENTCONTEXT>,
        fifteenth: Argument<O, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
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
    metaData[Arguments] =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth,fifteenth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast(),arguments[8].cast(),arguments[9].cast(),arguments[10].cast(),arguments[11].cast(),arguments[12].cast(),arguments[13].cast(),arguments[14].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N, O, P> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        eighth: Argument<H, ARGUMENTCONTEXT>,
        ninth: Argument<I, ARGUMENTCONTEXT>,
        tenth: Argument<J, ARGUMENTCONTEXT>,
        eleventh: Argument<K, ARGUMENTCONTEXT>,
        twelfth: Argument<L, ARGUMENTCONTEXT>,
        thirteenth: Argument<M, ARGUMENTCONTEXT>,
        fourteenth: Argument<N, ARGUMENTCONTEXT>,
        fifteenth: Argument<O, ARGUMENTCONTEXT>,
        sixteenth: Argument<P, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
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
    metaData[Arguments] =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth,fifteenth,sixteenth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast(),arguments[8].cast(),arguments[9].cast(),arguments[10].cast(),arguments[11].cast(),arguments[12].cast(),arguments[13].cast(),arguments[14].cast(),arguments[15].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N, O, P, Q> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        eighth: Argument<H, ARGUMENTCONTEXT>,
        ninth: Argument<I, ARGUMENTCONTEXT>,
        tenth: Argument<J, ARGUMENTCONTEXT>,
        eleventh: Argument<K, ARGUMENTCONTEXT>,
        twelfth: Argument<L, ARGUMENTCONTEXT>,
        thirteenth: Argument<M, ARGUMENTCONTEXT>,
        fourteenth: Argument<N, ARGUMENTCONTEXT>,
        fifteenth: Argument<O, ARGUMENTCONTEXT>,
        sixteenth: Argument<P, ARGUMENTCONTEXT>,
        seventeenth: Argument<Q, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
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
    metaData[Arguments] =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth,fifteenth,sixteenth,seventeenth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast(),arguments[8].cast(),arguments[9].cast(),arguments[10].cast(),arguments[11].cast(),arguments[12].cast(),arguments[13].cast(),arguments[14].cast(),arguments[15].cast(),arguments[16].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N, O, P, Q, R> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT,
        EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        eighth: Argument<H, ARGUMENTCONTEXT>,
        ninth: Argument<I, ARGUMENTCONTEXT>,
        tenth: Argument<J, ARGUMENTCONTEXT>,
        eleventh: Argument<K, ARGUMENTCONTEXT>,
        twelfth: Argument<L, ARGUMENTCONTEXT>,
        thirteenth: Argument<M, ARGUMENTCONTEXT>,
        fourteenth: Argument<N, ARGUMENTCONTEXT>,
        fifteenth: Argument<O, ARGUMENTCONTEXT>,
        sixteenth: Argument<P, ARGUMENTCONTEXT>,
        seventeenth: Argument<Q, ARGUMENTCONTEXT>,
        eighteenth: Argument<R, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
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
    metaData[Arguments] =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth,fifteenth,sixteenth,seventeenth,eighteenth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast(),arguments[8].cast(),arguments[9].cast(),arguments[10].cast(),arguments[11].cast(),arguments[12].cast(),arguments[13].cast(),arguments[14].cast(),arguments[15].cast(),arguments[16].cast(),arguments[17].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N, O, P, Q, R, S> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT,
        EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        eighth: Argument<H, ARGUMENTCONTEXT>,
        ninth: Argument<I, ARGUMENTCONTEXT>,
        tenth: Argument<J, ARGUMENTCONTEXT>,
        eleventh: Argument<K, ARGUMENTCONTEXT>,
        twelfth: Argument<L, ARGUMENTCONTEXT>,
        thirteenth: Argument<M, ARGUMENTCONTEXT>,
        fourteenth: Argument<N, ARGUMENTCONTEXT>,
        fifteenth: Argument<O, ARGUMENTCONTEXT>,
        sixteenth: Argument<P, ARGUMENTCONTEXT>,
        seventeenth: Argument<Q, ARGUMENTCONTEXT>,
        eighteenth: Argument<R, ARGUMENTCONTEXT>,
        nineteenth: Argument<S, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
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
    metaData[Arguments] =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth,fifteenth,sixteenth,seventeenth,eighteenth,nineteenth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast(),arguments[8].cast(),arguments[9].cast(),arguments[10].cast(),arguments[11].cast(),arguments[12].cast(),arguments[13].cast(),arguments[14].cast(),arguments[15].cast(),arguments[16].cast(),arguments[17].cast(),arguments[18].cast())
    }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B, C, D, E, F, G, H,
        I, J, K, L, M, N, O, P, Q, R, S, T> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT,
        EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        third: Argument<C, ARGUMENTCONTEXT>,
        fourth: Argument<D, ARGUMENTCONTEXT>,
        fifth: Argument<E, ARGUMENTCONTEXT>,
        sixth: Argument<F, ARGUMENTCONTEXT>,
        seventh: Argument<G, ARGUMENTCONTEXT>,
        eighth: Argument<H, ARGUMENTCONTEXT>,
        ninth: Argument<I, ARGUMENTCONTEXT>,
        tenth: Argument<J, ARGUMENTCONTEXT>,
        eleventh: Argument<K, ARGUMENTCONTEXT>,
        twelfth: Argument<L, ARGUMENTCONTEXT>,
        thirteenth: Argument<M, ARGUMENTCONTEXT>,
        fourteenth: Argument<N, ARGUMENTCONTEXT>,
        fifteenth: Argument<O, ARGUMENTCONTEXT>,
        sixteenth: Argument<P, ARGUMENTCONTEXT>,
        seventeenth: Argument<Q, ARGUMENTCONTEXT>,
        eighteenth: Argument<R, ARGUMENTCONTEXT>,
        nineteenth: Argument<S, ARGUMENTCONTEXT>,
        twentieth: Argument<T, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(
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
    metaData[Arguments] =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth,fifteenth,sixteenth,seventeenth,eighteenth,nineteenth,twentieth)
    metaData[Execution] = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast(),arguments[8].cast(),arguments[9].cast(),arguments[10].cast(),arguments[11].cast(),arguments[12].cast(),arguments[13].cast(),arguments[14].cast(),arguments[15].cast(),arguments[16].cast(),arguments[17].cast(),arguments[18].cast(),arguments[19].cast())
    }
}