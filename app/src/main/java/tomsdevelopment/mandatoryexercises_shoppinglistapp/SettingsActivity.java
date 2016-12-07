package tomsdevelopment.mandatoryexercises_shoppinglistapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends MenuActivity {
	Context context;
	SharedPreferences preferences;

	RadioGroup radioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		preferences = getSharedPreferences("tomsdevelopment.shoppinglist", MODE_PRIVATE);

		setContentView(R.layout.settings);


		radioGroup = (RadioGroup) findViewById(R.id.rbtnGrpSortBy);

		int selectingRadioButtonId;

		if (preferences.contains("sortBy") && preferences.getString("sortBy", "amount").toLowerCase().equals("name")) {
			selectingRadioButtonId = R.id.rbtnSortByName;
		} else {
			selectingRadioButtonId = R.id.rbtnSortByAmount;
		}

		radioGroup.check(selectingRadioButtonId);

		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);

				if (checkedRadioButton != null && checkedRadioButton.getText().length() > 0) {
					SharedPreferences.Editor editor = preferences.edit();

					editor.putString("sortBy", checkedRadioButton.getText().toString().toLowerCase());

					editor.commit();
				}
			}
		});
	}
}
