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

package tokens;

public class OperatorToken extends Token {
	Math_Operator mop;

	public OperatorToken(Math_Operator op) {
		this.mop = op;
	}

	public int result(int a, int b) {
		return mop.res.op(a, b);
	}

	public enum Math_Operator {
		ADD((a, b) -> a + b), SUBTRACT((a, b) -> a - b), MULTIPLY((a, b) -> a * b), DIVIDE((a, b) -> a / b), LOGIC_AND((a, b) -> a != 0 && b != 0 ? 1 : 0), LOGIC_OR((a, b) -> a != 0 || b != 0 ? 1 : 0), LOGIC_XOR((a, b) -> a != 0 ^ b != 0 ? 1 : 0),
		LOGIC_E((a, b) -> a == b ? 1 : 0), LOGIC_NE((a, b) -> a != b ? 1 : 0), LOGIC_G((a, b) -> a > b ? 1 : 0), LOGIC_GE((a, b) -> a >= b ? 1 : 0), LOGIC_S((a, b) -> a < b ? 1 : 0), LOGIC_SE((a, b) -> a <= b ? 1 : 0);
		Math_Result res;

		Math_Operator(Math_Result res) {
			this.res = res;
		}

		public float op(int a, int b) {
			return res.op(a, b);
		}
	}

	private interface Math_Result {
		int op(int a, int b);
	}
}