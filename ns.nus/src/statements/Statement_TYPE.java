/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package statements;

import lang.exceptions.ParsingError;
import tokens.*;

public enum Statement_TYPE {
	DELETE_VAR,
	VAR_DECLARE,
	VAR_UPDATE,
	CONDITIONAL,
	WHILE_LOOP,
	INCREMENT,
	METHOD_CALL,
	METHOD_RETURN,
	METHOD_DECLARE;

	public boolean fits(Token[] t, int ind) {
		boolean ret = false;
		switch (this) {
			case DELETE_VAR:
				break;
			case VAR_DECLARE: {
				int off = t[ind + 1] instanceof PointerToken ? 1 : 0;
				ret = (t[ind] instanceof CompositeTypeToken || t[ind] instanceof TypeToken) && (t[ind + off + 1] instanceof NameToken || t[ind + off + 1] instanceof IdentifierToken) && (t[ind + off + 2] instanceof AssignmentToken || t[ind + off + 2] instanceof SemicolonToken);
				break;
			}
			case VAR_UPDATE: {
				ret = t[ind] instanceof IdentifierToken && t[ind + 1] instanceof AssignmentToken;
				if (!ret && t[ind] instanceof UnaryOperatorToken && ((UnaryOperatorToken) t[ind]).op == UnaryOperatorToken.OP.DEREFERENCE) {
					ret = t[ind + 1] instanceof IdentifierToken && t[ind + 2] instanceof AssignmentToken;
					if (!ret && t[ind + 1] instanceof ParenthesisOpenedToken) {
						int i = ind + 2, d = 1;
						for (; i < t.length; i++) {
							if (t[i] instanceof ParenthesisOpenedToken)
								d++;
							else if (t[i] instanceof ParenthesisClosedToken) {
								d--;
								if (d == 0)
									break;
							}
						}
						if (d != 0)
							throw new ParsingError("Expected closed parenthesis");
						if (t[i + 1] instanceof AssignmentToken)
							ret = true;
					}
				}
				break;
			}
			case CONDITIONAL:
				ret = t[ind] instanceof IfToken && t[ind + 1] instanceof ParenthesisOpenedToken;
				break;
			case WHILE_LOOP:
				ret = t[ind] instanceof WhileToken && t[ind + 1] instanceof ParenthesisOpenedToken;
				break;
			case INCREMENT:
				ret = t[ind] instanceof IdentifierToken && t[ind + 1] instanceof IncrementToken;
				break;
			case METHOD_CALL:
				ret = t[ind] instanceof IdentifierToken && t[ind + 1] instanceof ParenthesisOpenedToken;
				break;
			case METHOD_RETURN:
				ret = t[ind] instanceof ControlToken && ((ControlToken) t[ind]).instr == CONTROL_INSTRUCTION.RETURN;
				break;
			case METHOD_DECLARE:
				ret = (t[ind] instanceof CompositeTypeToken || t[ind] instanceof TypeToken) && (t[ind + 1] instanceof NameToken || t[ind + 1] instanceof IdentifierToken) && (t[ind + 2] instanceof ParenthesisOpenedToken);
				break;
		}
		return ret;
	}
}