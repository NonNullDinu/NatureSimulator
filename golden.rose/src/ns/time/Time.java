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

package ns.time;

import ns.display.DisplayManager;

import java.io.Serializable;

public final class Time implements Serializable {
	private static final long serialVersionUID = 2756200570484761165L;
	private final DayNightCycle dayNightCycle;
	public float t;
	private transient boolean isDay = false, isNight = false, isMorning = false, isEveneing = false, prevDay = false;
	private int day;

	public Time(DayNightCycle dayNightCycle) {
		t = 0f;
		this.dayNightCycle = dayNightCycle;
	}

	public boolean isDay() {
		return isDay;
	}

	public boolean isNight() {
		return isNight;
	}

	public boolean isEvening() {
		return isEveneing;
	}

	public boolean isMorning() {
		return isMorning;
	}

	public float dayFactor() {
		return dayNightCycle.dayFactor(t);
	}

	public float nightFactor() {
		return dayNightCycle.nightFactor(t);
	}

	public void update() {
		prevDay = isDay;
		t += DisplayManager.getInGameTimeSeconds();
		isDay = dayNightCycle.isDay(t);
		isNight = dayNightCycle.isNight(t);
		isMorning = dayNightCycle.isMorning(t);
		isEveneing = dayNightCycle.isEvening(t);
		if (isDay && !prevDay) {
			day++;
		}
	}

	public int day() {
		return day;
	}
}