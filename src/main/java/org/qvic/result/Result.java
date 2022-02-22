package org.qvic.result;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface Result<T> permits Result.Ok, Result.Err {

    void consume(Consumer<T> consumeOk, Consumer<String> consumerErr);

    static <T> Result<T> ok(T t) {
        return new Ok<>(t);
    }

    static <A, B, C, R> Result<R> coalesce3(Result<A> ra, Result<B> rb, Result<C> rc,
                                            TriFunction<A, B, C, R> mapOk,
                                            Function<List<String>, String> mapErr) {
        var errors = new ArrayList<String>();
        if (ra instanceof Err<A> err) {
            errors.add(err.message());
        }
        if (rb instanceof Err<B> err) {
            errors.add(err.message());
        }
        if (rc instanceof Err<C> err) {
            errors.add(err.message());
        }
        if (errors.isEmpty()) {
            return Result.ok(mapOk.apply(((Ok<A>) ra).value(), ((Ok<B>) rb).value(), ((Ok<C>) rc).value()));
        } else {
            return Result.err(mapErr.apply(errors));
        }
    }

    static <T> Result<T> err(String message) {
        return new Err<>(message);
    }

    record Ok<T>(T value) implements Result<T> {
        @Override
        public void consume(Consumer<T> consumeOk, Consumer<String> consumerErr) {
            consumeOk.accept(value);
        }
    }

    record Err<T>(String message) implements Result<T> {
        @Override
        public void consume(Consumer<T> consumeOk, Consumer<String> consumerErr) {
            consumerErr.accept(message);
        }
    }
}
