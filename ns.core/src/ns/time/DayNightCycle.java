package ns.time;

import java.io.Serializable;

public interface DayNightCycle extends Serializable {
	float H_S_DURATION = 60f;

	boolean isDay(float t);

	boolean isNight(float t);

	boolean isMorning(float t);

	boolean isEvening(float t);

	float dayFactor(float t);

	float nightFactor(float t);
}