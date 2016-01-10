package com.freelancer.xwatch.utils.strings;

import java.util.*;

public final class NotIncludesFilter extends StringFilter
{

	public NotIncludesFilter(Collection<String> filters)
	{
		super(filters, FilterOption.NOT_INCLUDES);
	}

	@Override
	protected boolean acceptAfterValid(String search)
	{
		return !this.filters.contains(search);
	}
}
