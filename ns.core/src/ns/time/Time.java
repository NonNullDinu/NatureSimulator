package ns.time;

import ns.display.DisplayManager;

public final class Time {
	public float t;
	private final DayNightCycle dayNightCycle;
	private boolean isDay = false, isNight = false, isMorning = false, isEveneing = false;

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
		t += DisplayManager.getFrameTimeSeconds();
		isDay = dayNightCycle.isDay(t);
		isNight = dayNightCycle.isNight(t);
		isMorning = dayNightCycle.isMorning(t);
		isEveneing = dayNightCycle.isEvening(t);
	}
}