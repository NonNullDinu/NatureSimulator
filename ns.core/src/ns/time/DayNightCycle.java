package ns.time;

public interface DayNightCycle {
	float H_S_DURATION = 1f;

	boolean isDay(float t);

	boolean isNight(float t);

	boolean isMorning(float t);

	boolean isEvening(float t);

	float dayFactor(float t);

	float nightFactor(float t);
}