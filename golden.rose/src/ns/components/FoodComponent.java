package ns.components;

public class FoodComponent implements IComponent {
	private static final long serialVersionUID = -9048230879388446849L;

	float amount;

	FoodComponent(float amount) {
		this.amount = amount;
	}

	void eat(float val) {
		amount -= val;
//		boolean toRemove = false;
//		if (val == 0f)
//			toRemove = true;
	}

	@Override
	public IComponent copy() {
		return new FoodComponent(amount);
	}
}
