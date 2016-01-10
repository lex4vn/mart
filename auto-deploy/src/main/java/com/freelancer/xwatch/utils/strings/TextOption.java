package com.freelancer.xwatch.utils.strings;

public interface TextOption {

	public String process(String line);

	public enum ACTION {
		ADD_TEXT(AddText.class), REPLACE(ReplaceContent.class), REMOVE_PARAGRAPH(Paragraph.class);

		private final Class<? extends TextOption> textOption;

		ACTION(Class<? extends TextOption> textOptionClass) {
			textOption = textOptionClass;
		}

		public Class<? extends TextOption> getTextOption() {
			return textOption;
		}

		public static ACTION parse(String text) {
			if (ADD_TEXT.toString().equals(text)) {
				return ADD_TEXT;
			}
			if (REPLACE.toString().equals(text)) {
				return REPLACE;
			}
			if (REMOVE_PARAGRAPH.toString().equals(text)) {
				return REMOVE_PARAGRAPH;
			}
			return null;
		}

		public boolean isAddText() {
			return this == ADD_TEXT;
		}

		public boolean isReplace() {
			return this == REPLACE;
		}

		public boolean isRemoveParagraph() {
			return this == REMOVE_PARAGRAPH;
		}

	}
}
