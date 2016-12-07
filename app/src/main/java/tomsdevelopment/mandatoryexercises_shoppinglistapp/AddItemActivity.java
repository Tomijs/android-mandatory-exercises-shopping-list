package tomsdevelopment.mandatoryexercises_shoppinglistapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import firebase.FirebaseHelper;
import model.Item;

public class AddItemActivity extends MenuActivity {
	Context context;
	SharedPreferences preferences;

	DatabaseReference databaseRef;
	FirebaseHelper firebaseHelper;

	Button btnAddItem;
	Button btnCancel;

	TextView txtItemName;
	TextView txtItemAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		preferences = getSharedPreferences("tomsdevelopment.shoppinglist", MODE_PRIVATE);

		databaseRef = FirebaseDatabase.getInstance().getReference();
		firebaseHelper = new FirebaseHelper(databaseRef);

		setContentView(R.layout.add_item);


		btnAddItem = (Button) findViewById(R.id.btnAdd);
		txtItemName = (TextView) findViewById(R.id.txtName);

		btnAddItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String itemName = txtItemName.getText().toString().trim();

				String itemAmountString = txtItemAmount.getText().toString().trim();
				int itemAmount = Integer.parseInt(itemAmountString);

				if (itemName.length() <= 0) {
					Toast.makeText(context, "Please, provide Item Name.", Toast.LENGTH_SHORT).show();
				} else if (itemName.length() > 100) {
					Toast.makeText(context, "Item Name cannot contain more than 100 characters.", Toast.LENGTH_SHORT).show();
				} else if (itemAmountString.length() <= 0) {
					Toast.makeText(context, "Please, provide Item Amount.", Toast.LENGTH_SHORT).show();
				} else if (itemAmount <= 0) {
					Toast.makeText(context, "Item Amount must be more than 0.", Toast.LENGTH_SHORT).show();
				} else if (itemAmount > 10000) {
					Toast.makeText(context, "Maximum Item Amount value is 10'000.", Toast.LENGTH_SHORT).show();
				} else {
					Item item = new Item(itemName, itemAmount);

					if (firebaseHelper.saveItem(item)) {
						startActivity(new Intent(context, ShoppingCartActivity.class));
					}
				}
			}
		});


		btnCancel = (Button) findViewById(R.id.btnCancel);
		txtItemAmount = (TextView) findViewById(R.id.txtAmount);

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(context, ShoppingCartActivity.class));
			}
		});
	}
}
