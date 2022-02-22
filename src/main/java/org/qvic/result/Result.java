package org.qvic.result;

import java.util.function.Consumer;

public sealed interface Result<T> permits Result.Ok, Result.Err {

    void consume(Consumer<T> consumeOk, Consumer<String> consumerErr);

    static <T> Result<T> ok(T t) {
        return new Ok<>(t);
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
