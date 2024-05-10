package com.mineaurion.aurionchat.common.misc;

import java.util.List;

public enum ArgumentTokenizer {

    EXECUTE {
        @Override
        public List<String> tokenizeInput(String args) {
            return new QuotedStringTokenizer(args).tokenize(true);
        }
    },
    TAB_COMPLETE {
        @Override
        public List<String> tokenizeInput(String args) {
            return new QuotedStringTokenizer(args).tokenize(false);
        }
    };

    public abstract List<String> tokenizeInput(String args);

}