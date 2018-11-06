package ns.worldSave;

import ns.time.Time;

import java.io.Serializable;

public class EndObject implements Serializable {
	private static final long serialVersionUID = 512084690503224131L;

	private Time guTime;

	public EndObject(Time time) {
		this.guTime = time;
	}

	public Time getGuTime() {
		return guTime;
	}
}