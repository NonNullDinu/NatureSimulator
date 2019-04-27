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

package variables;

public enum DATA_TYPE {
	INT("int", "resd"), STRING("[Ss]tring", "resb"), BOOL("bool(ean)?", "resb"), BYTE_STREAM("[^.]*", "resb"), SHORT_INT("short(\\sint)?", "resw");
	public String asm_type;
	public String pattern;

	DATA_TYPE(String regex, String asm_type) {
		this.pattern = regex;
		this.asm_type = asm_type;
	}
}
