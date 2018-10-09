package patch.nsUpdateScript;

public class Pattern {
	private final String patt;

	public Pattern(String pattern) {
		this.patt = pattern;
	}

	public boolean isStringOfPattern(String string) {
		System.out.println(string);
		char[] str = string.toCharArray();
		char[] pattern = patt.toCharArray();
		int add = 0;
		for (int i = 0; i < pattern.length; i++) {
			if (pattern[i] == '*') {
				if (i == pattern.length - 2)
					return str[str.length - 1] == pattern[i + 1];
				else if (i == pattern.length - 1)
					return true;
				else {
					if (pattern[i + 2] != '*' && pattern[i + 2] != LANG_DEF.FORMAT_SPACE_OR_TAB_OR_NOTHING) {
						for (; !(pattern[i + 1] == str[i + add] && pattern[i + 2] == str[i + add + 1]); add++) {
							if (i + add + 1 == str.length - 1) {/*Reached the end but still not found the next chars in pattern*/
								return false;
							}
						}
						add--;
					} else {
						for (; pattern[i + 1] != str[i + add]; add++) {
							if (i + add == str.length - 1) {/*Reached the end but still not found the next char in pattern*/
								return false;
							}
						}
						add--;
					}
				}
			} else if (pattern[i] == LANG_DEF.FORMAT_SPACE_OR_TAB_OR_NOTHING) {
				if (i + add == str.length) {
					return true;
				}
				if (!(str[i + add] == ' ' || str[i + add] == '\t')) {
					add--;
				}
			} else {
				if (pattern[i] != str[i + add]) {
					return false;
				}
			}
		}
		return true;
	}
}
