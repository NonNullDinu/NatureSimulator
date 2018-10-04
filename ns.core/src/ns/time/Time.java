package ns.time;

import ns.display.DisplayManager;

public final class Time {
	public float t;
	private final DayNightCycle dayNightCycle;

	public Time(DayNightCycle dayNightCycle) {
		t = 0f;
		this.dayNightCycle = dayNightCycle;
	}

	public boolean isDay() {
		return dayNightCycle.isDay(t);
	}

	public boolean isNight() {
		return dayNightCycle.isNight(t);
	}

	public boolean isEvening() {
		return dayNightCycle.isEvening(t);
	}

	public boolean isMorning() {
		return dayNightCycle.isMorning(t);
	}

	public float dayFactor() {
		return dayNightCycle.dayFactor(t);
	}

	public float nightFactor() {
		return dayNightCycle.nightFactor(t);
	}

	public void update() {
		t += DisplayManager.getFrameTimeSeconds();
	}
}