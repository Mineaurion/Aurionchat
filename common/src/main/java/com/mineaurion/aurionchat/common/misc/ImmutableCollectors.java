package com.mineaurion.aurionchat.common.misc;

import com.google.common.collect.ImmutableMap;

import java.util.function.Function;
import java.util.stream.Collector;

public final class ImmutableCollectors {
    private ImmutableCollectors() {}

    public static <T, K, V> Collector<T, ImmutableMap.Builder<K, V>, ImmutableMap<K, V>> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        return Collector.of(
                ImmutableMap.Builder<K, V>::new,
                (r, t) -> r.put(keyMapper.apply(t), valueMapper.apply(t)),
                (l, r) -> l.putAll(r.build()),
                ImmutableMap.Builder::build
        );
    }
}