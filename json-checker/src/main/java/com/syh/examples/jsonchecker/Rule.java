package com.syh.examples.jsonchecker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Rule {

    public Rule(@JsonProperty(value = "conditions", required = true) Map<String, List<Condition>> conditions,
                @JsonProperty(value = "window", required = false) Window window) {
        if (conditions == null ||
                conditions.values()
                        .stream()
                        .mapToLong(List::size)
                        .sum() == 0) {
            throw new IllegalArgumentException("need at least one condition");
        }
        this.conditions = conditions;
        this.window = window;
    }

    private Map<String, List<Condition>> conditions;

    private Window window;

    @Data
    public static class Condition {

        public Condition(@JsonProperty(value = "field", required = true) String field,
                         @JsonProperty(value = "target", required = true) Object target) {
            if (!field.startsWith("$.")) {
                throw new IllegalArgumentException("Bad field format");
            }
            this.field = field;
            this.target = target;
        }

        private String field;

        private Object target;
    }

    @Data
    public static class Window {

        public Window(@JsonProperty(value = "windowMinute", required = true) Integer windowMinute,
                      @JsonProperty(value = "limit", required = true) Integer limit) {
            this.windowMinute = windowMinute;
            this.limit = limit;
        }

        private Integer windowMinute;

        private Integer limit;
    }
}
