package patch.nsUpdateScript;

import java.util.HashMap;
import java.util.Map;

public class LANG_DEF {
	public static final char FORMAT_SPACE_OR_TAB_OR_NOTHING = '`';

	protected static final Map<String, TokenPattern> patterns = new HashMap<>();

	static {
		patterns.put("LANG_CODE_BLOCK_OPEN_BRACKET_TOKEN", new TokenPattern("{"));// CodeBlockOpenBracketToken
		patterns.put("LANG_CODE_BLOCK_CLOSE_BRACKET_TOKEN", new TokenPattern("}"));// CodeBlockCloseBracketToken
		patterns.put("LANG_CODE_BLOCK_TOKEN", new TokenPattern("${LANG_CODE_BLOCK_OPEN_BRACKET_TOKEN}*${LANG_CODE_BLOCK_CLOSE_BRACKET_TOKEN}"));// CodeBlockToken
		patterns.put("LANG_IF_DECLARATION", new TokenPattern("if" + FORMAT_SPACE_OR_TAB_OR_NOTHING + "(*)" + FORMAT_SPACE_OR_TAB_OR_NOTHING + "${LANG_CODE_BLOCK_TOKEN}"));
		//Method call
		patterns.put("LANG_METHOD_CALL_ARGUMENTS_OPEN_DECLARATION_TOKEN", new TokenPattern("("));
		patterns.put("LANG_METHOD_CALL_ARGUMENTS_CLOSE_DECLARATION_TOKEN", new TokenPattern(")"));
		patterns.put("LANG_METHOD_CALL_ARGUMENT_TOKEN", new TokenPattern("*"));
		patterns.put("LANG_METHOD_CALL_ARGUMENTS_TOKEN", new TokenPattern("${LANG_METHOD_CALL_ARGUMENT_TOKEN}...\",\""));
		patterns.put("LANG_METHOD_CALL_NAME_TOKEN", new TokenPattern("*"));
		patterns.put("LANG_METHOD_CALL_TYPE_TOKEN", new TokenPattern("*_CALL"));
		patterns.put("LANG_METHOD_CALL_TOKEN", new TokenPattern("${LANG_METHOD_CALL_TYPE_TOKEN} ${LANG_METHOD_CALL_NAME_TOKEN}${LANG_METHOD_CALL_ARGUMENTS_OPEN_DECLARATION_TOKEN}${LANG_METHOD_CALL_ARGUMENTS_TOKEN}${LANG_METHOD_CALL_ARGUMENTS_CLOSE_DECLARATION_TOKEN}"));

		//Method declaration
		patterns.put("LANG_METHOD_NAME_DECLARATION_TOKEN", new TokenPattern("*"));
		patterns.put("LANG_METHOD_DECLARATION_TOKEN", new TokenPattern("method ${LANG_METHOD_NAME_DECLARATION_TOKEN}()"));
		patterns.put("LANG_METHOD_BODY_TOKEN", new TokenPattern("{*}"));
		patterns.put("LANG_METHOD_TOKEN", new TokenPattern("${LANG_METHOD_DECLARATION_TOKEN}${LANG_METHOD_BODY_TOKEN}"));
	}

	public static MethodDeclarationToken breakToTokensMDT(String string) {
		String name = string.substring(8, string.indexOf('('));
		return new MethodDeclarationToken(name);
	}
}