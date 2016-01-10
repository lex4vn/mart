package com.freelancer.xwatch.utils.strings;

public final class AddText implements TextOption
{
	private final boolean isBefore;
	private final String addingText;
	private final String signText;
	private final String signFilterOption;
	private StringFilter signFilter;

	public AddText(String text, String signFilterOption, String signText, boolean isBefore)
	{
		this.isBefore = isBefore;
		this.addingText = text;
		this.signFilterOption = signFilterOption;
		this.signText = signText;
		this.signFilter =
			StringFilterFactory.getFilter(FilterOption.parse(this.signFilterOption), this.signText);
	}

	public String process(String line)
	{
		if (this.signFilter == null)
		{
			this.signFilter =
				StringFilterFactory.getFilter(
					FilterOption.parse(this.signFilterOption),
					this.signText);
		}
		if (this.signFilter == null || !this.signFilter.accept(line))
		{
			return line;
		}
		final StringBuilder builder = new StringBuilder();
		if (this.isBefore)
		{
			builder.append(this.addingText);
			builder.append(line);
		}
		else
		{
			builder.append(line);
			builder.append(this.addingText);
		}
		return builder.toString();
	}

	@Override
	public String toString()
	{
		return "AddText [addingText=" + this.addingText + ", signText=" + this.signText
			+ ", signFilterOption=" + this.signFilterOption + ", isBefore=" + this.isBefore + "]";
	}

}
