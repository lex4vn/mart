package com.freelancer.xwatch.utils.strings;

public final class NotEqualsFilter extends StringFilter {

    public NotEqualsFilter(String filter) {
        super(filter, FilterOption.NOT_EQUALS);
    }

    @Override
    protected boolean acceptAfterValid(String search) {
        return !search.equals(this.filter);
    }
}
