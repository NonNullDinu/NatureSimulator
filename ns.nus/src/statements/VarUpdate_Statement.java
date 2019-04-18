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

import exceptions.WrongValueTypeException;
import tokens.Token;
import tokens.Value;
import tokens.ValueEvaluator;
import variables.Variable;
import variables.Variable_BOOL;
import variables.Variable_INT;
import variables.Variable_STRING;

import java.util.Map;

public class VarUpdate_Statement extends Statement {
	public final Token[] value;
	public final String name;

	public VarUpdate_Statement(String name, Token[] value) {
		super(Statement_TYPE.VAR_UPDATE);
		this.value = value;
		this.name = name;
	}

	@Override
	public void run(Map<String, Variable> variables) {
		Value v = ValueEvaluator.evaluate(value, variables);
		Variable var = variables.get(name);
		if (v == null)
			for (Token t : value)
				System.out.println(t);
		if (v.type != var.type)
			throw new WrongValueTypeException("The type " + var.type.name() + " cannot accept values of type " + v.type.name());
		else switch (v.type) {
			case INT:
				((Variable_INT) var).v = v.vi;
				break;
			case BOOL:
				((Variable_BOOL) var).v = v.vb;
				break;
			case STRING:
				((Variable_STRING) var).v = v.vs;
				break;
		}
	}
}
