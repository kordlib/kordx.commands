package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.internal.cast

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext>
        CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(execute: suspend
EVENTCONTEXT.() -> Unit) {
    arguments = emptyList()
    execution = { context, arguments -> execute(context.cast()) }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A>
        CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(first: Argument<A,
        ARGUMENTCONTEXT>, execute: suspend EVENTCONTEXT.(first: A) -> Unit) {
    arguments = listOf(first)
    execution = { context, arguments -> execute(context.cast(), arguments[0].cast()) }
}

operator fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext, A, B>
        CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.invoke(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        execute: suspend EVENTCONTEXT.(first: A, second: B) -> Unit
) {
    arguments = listOf(first,second)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments = listOf(first,second,third)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments = listOf(first,second,third,fourth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments = listOf(first,second,third,fourth,fifth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments = listOf(first,second,third,fourth,fifth,sixth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments = listOf(first,second,third,fourth,fifth,sixth,seventh)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments = listOf(first,second,third,fourth,fifth,sixth,seventh,eighth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments = listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments = listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth,fifteenth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth,fifteenth,sixteenth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth,fifteenth,sixteenth,seventeenth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth,fifteenth,sixteenth,seventeenth,eighteenth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth,fifteenth,sixteenth,seventeenth,eighteenth,nineteenth)
    execution = { context, arguments -> execute(context.cast(),
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
    arguments =
            listOf(first,second,third,fourth,fifth,sixth,seventh,eighth,ninth,tenth,eleventh,twelfth,thirteenth,fourteenth,fifteenth,sixteenth,seventeenth,eighteenth,nineteenth,twentieth)
    execution = { context, arguments -> execute(context.cast(),
            arguments[0].cast(),arguments[1].cast(),arguments[2].cast(),arguments[3].cast(),arguments[4].cast(),arguments[5].cast(),arguments[6].cast(),arguments[7].cast(),arguments[8].cast(),arguments[9].cast(),arguments[10].cast(),arguments[11].cast(),arguments[12].cast(),arguments[13].cast(),arguments[14].cast(),arguments[15].cast(),arguments[16].cast(),arguments[17].cast(),arguments[18].cast(),arguments[19].cast())
    }
}