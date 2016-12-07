package tomsdevelopment.mandatoryexercises_shoppinglistapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import firebase.FirebaseHelper;

public class MenuActivity extends AppCompatActivity {
	Context context;
	SharedPreferences preferences;

	DatabaseReference databaseRef;
	FirebaseHelper firebaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		preferences = getSharedPreferences("tomsdevelopment.shoppinglist", MODE_PRIVATE);

		databaseRef = FirebaseDatabase.getInstance().getReference();
		firebaseHelper = new FirebaseHelper(databaseRef);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
			case R.id.action_shopping_cart:
				startActivity(new Intent(context, ShoppingCartActivity.class));

				break;
			case R.id.action_settings:
				startActivity(new Intent(context, SettingsActivity.class));

				break;
			case R.id.action_share:
				firebaseHelper.shareShoppingListItems(context, preferences.getString("sortBy", "amount"));

				break;
			default:
				startActivity(new Intent(context, ShoppingCartActivity.class));

				break;
		}

		return super.onOptionsItemSelected(item);
	}
}
