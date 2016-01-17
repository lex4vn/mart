package com.freelancer.xwatch.utils.strings;

public enum FilterOption {
    CONTAINS,
    ENDS_WITH,
    EQUALS,
    EQUALS_INGORE_CASE,
    INCLUDES,
    MATCHES,
    NOT_CONTAINS,
    NOT_EQUALS,
    NOT_INCLUDES,
    STARTS_WITH;

    public static FilterOption parse(String filterOption) {
        if (CONTAINS.name().equalsIgnoreCase(filterOption)) {
            return CONTAINS;
        }
        if (ENDS_WITH.name().equalsIgnoreCase(filterOption)) {
            return ENDS_WITH;
        }
        if (EQUALS.name().equalsIgnoreCase(filterOption)) {
            return EQUALS;
        }
        if (EQUALS_INGORE_CASE.name().equalsIgnoreCase(filterOption)) {
            return EQUALS_INGORE_CASE;
        }
        if (INCLUDES.name().equalsIgnoreCase(filterOption)) {
            return INCLUDES;
        }
        if (MATCHES.name().equalsIgnoreCase(filterOption)) {
            return MATCHES;
        }
        if (NOT_CONTAINS.name().equalsIgnoreCase(filterOption)) {
            return NOT_CONTAINS;
        }
        if (NOT_EQUALS.name().equalsIgnoreCase(filterOption)) {
            return NOT_EQUALS;
        }
        if (NOT_INCLUDES.name().equalsIgnoreCase(filterOption)) {
            return NOT_INCLUDES;
        }
        if (STARTS_WITH.name().equalsIgnoreCase(filterOption)) {
            return STARTS_WITH;
        }
        return null;
    }

    public boolean isContains() {
        return this == CONTAINS;
    }

    public boolean isEndsWith() {
        return this == ENDS_WITH;
    }

    public boolean isEquals() {
        return this == EQUALS;
    }

    public boolean isEqualsIgnoreCase() {
        return this == EQUALS_INGORE_CASE;
    }

    public boolean isIncludes() {
        return this == INCLUDES;
    }

    public boolean isMatches() {
        return this == MATCHES;
    }

    public boolean isNotContains() {
        return this == NOT_CONTAINS;
    }

    public boolean isNotEquals() {
        return this == NOT_EQUALS;
    }

    public boolean isNotIncludes() {
        return this == NOT_INCLUDES;
    }

    public boolean isStartsWith() {
        return this == STARTS_WITH;
    }

}
