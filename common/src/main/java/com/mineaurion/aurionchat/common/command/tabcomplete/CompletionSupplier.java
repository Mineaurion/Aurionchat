package com.mineaurion.aurionchat.common.command.tabcomplete;

import com.mineaurion.aurionchat.common.misc.Predicates;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface CompletionSupplier {
    static CompletionSupplier startsWith(Supplier<Stream<String>> stringsSupplier) {
        return partial -> stringsSupplier.get().filter(Predicates.startsWithIgnoreCase(partial)).collect(Collectors.toList());
    }

    List<String> supplyCompletions(String partial);
}
