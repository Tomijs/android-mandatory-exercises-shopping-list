package tomsdevelopment.mandatoryexercises_shoppinglistapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends MenuActivity {
	Context context;
	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);

		super.onCreate(savedInstanceState);

		context = this;
		preferences = getSharedPreferences("tomsdevelopment.shoppinglist", MODE_PRIVATE);

		if (!preferences.contains("sortBy")) {
			SharedPreferences.Editor editor = preferences.edit();

			editor.putString("sortBy", "amount");

			editor.commit();
		}

		startActivity(new Intent(context, ShoppingCartActivity.class));
	}
}
