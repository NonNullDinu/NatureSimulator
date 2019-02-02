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

import variables.Variable;

import java.util.Map;

public class DeleteVariableStatement extends Statement {
	private final String name;

	public DeleteVariableStatement(String name) {
		super(Statement_TYPE.DELETE_VAR);
		this.name = name;
	}

	@Override
	public void run(Map<String, Variable> variables) {
		variables.remove(name);
	}
}
