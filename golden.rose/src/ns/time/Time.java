package ns.time;

import ns.display.DisplayManager;

import java.io.Serializable;

public final class Time implements Serializable {
	public float t;
	private final DayNightCycle dayNightCycle;
	private boolean isDay = false, isNight = false, isMorning = false, isEveneing = false, prevDay = false;
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