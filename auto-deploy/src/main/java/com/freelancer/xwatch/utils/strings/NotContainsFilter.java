package com.freelancer.xwatch.utils.strings;

public final class NotContainsFilter extends StringFilter {

    public NotContainsFilter(String filter) {
        super(filter, FilterOption.NOT_CONTAINS);
    }

    @Override
    protected boolean acceptAfterValid(String search) {
        return !search.contains(this.filter);
    }
}
