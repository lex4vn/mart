package com.freelancer.xwatch.utils.strings;

import java.util.*;

public final class EndsWithFilter extends StringFilter
{

	public EndsWithFilter(String filter)
	{
		super(filter, FilterOption.ENDS_WITH);
	}

	public EndsWithFilter(Collection<String> filters)
	{
		super(filters, FilterOption.ENDS_WITH);
	}

	@Override
	protected boolean acceptAfterValid(String search)
	{
		if (this.filter == null)
		{
			for (final String s : this.filters)
			{
				if (search.endsWith(s))
				{
					return true;
				}
			}
		}
		else
		{
			return search.endsWith(this.filter);
		}
		return false;
	}
}
