package model;

import com.google.firebase.database.Exclude;

public class Item {
	private String name;
	private int amount;

	private String key;

	public Item() {
		this.name = null;
		this.amount = 0;
	}

	public Item(String name, int amount) {
		this.name = name;
		this.amount = amount;
	}

	public Item(String name) {
		this.name = name;
		this.amount = 0;
	}

	@Exclude
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return name + " (" + amount + ')';
	}
}
