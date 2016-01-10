package com.freelancer.xwatch.utils.strings;

import java.util.*;

public final class MatchesFilter extends StringFilter
{

	public MatchesFilter(String filter)
	{
		super(filter, FilterOption.MATCHES);
	}

	public MatchesFilter(Collection<String> filters)
	{
		super(filters, FilterOption.MATCHES);
	}

	@Override
	protected boolean acceptAfterValid(String search)
	{
		if (this.filter == null)
		{
			for (String s : this.filters)
			{
				if (search.matches(s))
				{
					return true;
				}
			}
		}
		else
		{
			return search.matches(this.filter);
		}
		return false;
	}
}
