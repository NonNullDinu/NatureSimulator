/*
 * Copyright (C) 2018  Dinu Blanovschi
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

package ns.exceptions;

public class GameException extends RuntimeException {
	private static final long serialVersionUID = 2404478978308821651L;
	private StackTraceElement[] stackTrace;

	public GameException(String message) {
		super(message);
	}

	public GameException(String message, StackTraceElement[] stackTrace) {
		super(message);
		this.stackTrace = stackTrace;
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return stackTrace == null ? super.getStackTrace() : stackTrace;
	}
}