package com.freelancer.xwatch.utils.strings;

public final class ContainsFilter extends StringFilter {

    public ContainsFilter(String filter) {
        super(filter, FilterOption.CONTAINS);
    }

    @Override
    protected boolean acceptAfterValid(String search) {
        return search.contains(this.filter);
    }
}
