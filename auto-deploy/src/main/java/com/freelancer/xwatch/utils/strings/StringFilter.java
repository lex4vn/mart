package com.freelancer.xwatch.utils.strings;

import java.util.*;

public abstract class StringFilter {

    protected final boolean ignoreBlank;
    protected final String filter;
    protected final Set<String> filters = new HashSet<String>();
    protected final FilterOption filterOption;

    protected StringFilter(String search, FilterOption filterOption) {
        this.ignoreBlank = false;
        this.filter = search;
        this.filterOption = filterOption;
    }

    protected StringFilter(Collection<String> filters, FilterOption filterOption) {
        this.ignoreBlank = false;
        this.filter = null;
        this.filters.addAll(filters);
        this.filterOption = filterOption;
    }

    public FilterOption getFilterOption() {
        return this.filterOption;
    }

    public Set<String> getFilters() {
        return this.filter == null ? this.filters : new HashSet<String>(Arrays.asList(this.filter));
    }

    private boolean isEmpty(String search) {
        return search == null || search.trim().isEmpty();
    }

    public boolean accept(String search) {
        return this.isEmpty(search) ? this.ignoreBlank : this.acceptAfterValid(search);
    }

    protected abstract boolean acceptAfterValid(String search);
}
