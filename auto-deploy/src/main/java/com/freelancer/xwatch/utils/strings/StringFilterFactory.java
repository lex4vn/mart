package com.freelancer.xwatch.utils.strings;

import java.util.*;

public final class StringFilterFactory
{

	public static StringFilter getFilter(FilterOption filterOption, String... searches)
	{
		return getFilter(Arrays.asList(searches), filterOption);
	}

	public static StringFilter getFilter(Collection<String> searches, FilterOption filterOption)
	{
		if (filterOption == null || searches.isEmpty())
		{
			return null;
		}
		if (filterOption.isContains())
		{
			return new ContainsFilter(searches.iterator().next());
		}
		if (filterOption.isNotContains())
		{
			return new NotContainsFilter(searches.iterator().next());
		}
		if (filterOption.isEndsWith())
		{
			return new EndsWithFilter(searches);
		}
		if (filterOption.isEquals())
		{
			return new EqualsFilter(searches.iterator().next());
		}
		if (filterOption.isEqualsIgnoreCase())
		{
			return new EqualsIgnoreCaseFilter(searches);
		}
		if (filterOption.isIncludes())
		{
			return new IncludesFilter(searches);
		}
		if (filterOption.isMatches())
		{
			return new MatchesFilter(searches);
		}
		if (filterOption.isNotIncludes())
		{
			return new NotIncludesFilter(searches);
		}
		if (filterOption.isStartsWith())
		{
			return new StartsWithFilter(searches);
		}
		return null;
	}
}
