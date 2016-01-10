package com.freelancer.xwatch.utils.strings;

import java.util.*;

public final class EqualsIgnoreCaseFilter extends StringFilter
{

	public EqualsIgnoreCaseFilter(String filter)
	{
		super(filter, FilterOption.EQUALS_INGORE_CASE);
	}

	public EqualsIgnoreCaseFilter(Collection<String> filters)
	{
		super(filters, FilterOption.EQUALS_INGORE_CASE);
	}

	@Override
	protected boolean acceptAfterValid(String search)
	{
		if (this.filter == null)
		{
			for (final String s : this.filters)
			{
				if (search.equalsIgnoreCase(s))
				{
					return true;
				}
			}
		}
		else
		{
			return search.equalsIgnoreCase(this.filter);
		}
		return false;
	}
}
