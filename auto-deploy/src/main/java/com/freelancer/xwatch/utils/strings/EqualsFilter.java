package com.freelancer.xwatch.utils.strings;

public final class EqualsFilter extends StringFilter
{

	public EqualsFilter(String filter)
	{
		super(filter, FilterOption.EQUALS);
	}

	@Override
	protected boolean acceptAfterValid(String search)
	{
		return search.equals(this.filter);
	}
}
