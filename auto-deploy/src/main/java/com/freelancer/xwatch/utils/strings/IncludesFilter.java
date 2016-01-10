package com.freelancer.xwatch.utils.strings;

import java.util.*;

public final class IncludesFilter extends StringFilter
{

	public IncludesFilter(Collection<String> filters)
	{
		super(filters, FilterOption.INCLUDES);
	}

	@Override
	protected boolean acceptAfterValid(String search)
	{
		return this.filters.contains(search);
	}
}
