package com.freelancer.xwatch.utils.strings;

public final class ReplaceContent implements TextOption
{
	private final String regex;
	private final String replacement;

	public ReplaceContent(String regex, String replacement)
	{
		this.regex = regex;
		this.replacement = replacement;
	}

	public String process(String line)
	{
		return line.replaceAll(this.regex, this.replacement);
	}

	@Override
	public String toString()
	{
		return "ReplaceContent [regex=" + this.regex + ", replacement=" + this.replacement + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.regex == null ? 0 : this.regex.hashCode());
		result = prime * result + (this.replacement == null ? 0 : this.replacement.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		final ReplaceContent other = (ReplaceContent) obj;
		if (this.regex == null)
		{
			if (other.regex != null)
			{
				return false;
			}
		}
		else if (!this.regex.equals(other.regex))
		{
			return false;
		}
		if (this.replacement == null)
		{
			if (other.replacement != null)
			{
				return false;
			}
		}
		else if (!this.replacement.equals(other.replacement))
		{
			return false;
		}
		return true;
	}

}
