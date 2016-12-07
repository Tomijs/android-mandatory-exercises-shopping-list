package tomsdevelopment.mandatoryexercises_shoppinglistapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.design.widget.CoordinatorLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import firebase.FirebaseHelper;
import model.Item;

public class ShoppingCartActivity extends MenuActivity {
	Context context;
	SharedPreferences preferences;

	DatabaseReference databaseRef;
	FirebaseHelper firebaseHelper;

	CoordinatorLayout coordinatorLayout;


	Item lastDeletedItem = null;


	Button btnDeleteAllitems;

	ListView itemList;

	TextView itemCountNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		preferences = getSharedPreferences("tomsdevelopment.shoppinglist", MODE_PRIVATE);

		databaseRef = FirebaseDatabase.getInstance().getReference();
		firebaseHelper = new FirebaseHelper(databaseRef);

		setContentView(R.layout.shopping_cart);

		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(context, AddItemActivity.class));
			}
		});


		itemCountNo = (TextView) findViewById(R.id.itemCountNo);

		firebaseHelper.retrieveItemCount(itemCountNo);


		itemList = (ListView) findViewById(R.id.itemList);

		firebaseHelper.retrieveItems(context, itemList, preferences.getString("sortBy", "amount"));

		itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
				AlertDialog.Builder adb = new AlertDialog.Builder(context);

				adb.setTitle("Confirm Item Deletion from Shopping List");
				adb.setMessage("Are you sure you want to delete " + position + ". Item?");

				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Item deletingItem = (Item) itemList.getItemAtPosition(position);

						if (firebaseHelper.removeItem(deletingItem)) {
							lastDeletedItem = deletingItem;

							Snackbar snackbarDeleted = Snackbar.make(coordinatorLayout, "Item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									if (firebaseHelper.saveItem(lastDeletedItem)) {
										Snackbar snackbarRestored = Snackbar.make(coordinatorLayout, "Message is restored!", Snackbar.LENGTH_SHORT);

										snackbarRestored.show();

										firebaseHelper.retrieveItems(context, itemList, preferences.getString("sortBy", "amount"));
									}
								}
							});

							snackbarDeleted.show();
						}
					}
				});

				adb.show();
			}
		});


		btnDeleteAllitems = (Button) findViewById(R.id.btnDeleteAllitems);

		btnDeleteAllitems.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				AlertDialog.Builder adb = new AlertDialog.Builder(context);

				adb.setTitle("Confirm Item Deletion from Shopping List");
				adb.setMessage("Are you sure you want to delete all Items?");

				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						firebaseHelper.removeAllItems();
					}
				});

				adb.show();
			}
		});
	}
}
