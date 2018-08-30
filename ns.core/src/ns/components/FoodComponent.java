package ns.components;

public class FoodComponent implements IComponent {
	public float amount;

	public FoodComponent(float amount) {
		this.amount = amount;
	}

	public void eat(float val) {
		amount -= val;
	}
}
