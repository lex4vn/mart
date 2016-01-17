package com.freelancer.xwatch.utils.strings;

import java.util.*;

public final class StartsWithFilter extends StringFilter {

    public StartsWithFilter(String filter) {
        super(filter, FilterOption.STARTS_WITH);
    }

    public StartsWithFilter(Collection<String> filters) {
        super(filters, FilterOption.STARTS_WITH);
    }

    @Override
    protected boolean acceptAfterValid(String search) {
        if (this.filter == null) {
            for (final String s : this.filters) {
                if (search.startsWith(s)) {
                    return true;
                }
            }
        } else {
            return search.startsWith(this.filter);
        }
        return false;
    }
}
