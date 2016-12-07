package firebase;

import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.Item;
import tomsdevelopment.mandatoryexercises_shoppinglistapp.R;

public class FirebaseHelper {
	DatabaseReference databaseRef;

	ArrayList<Item> items = new ArrayList<>();

	Boolean itemSaved;
	Boolean itemRemoved;
	Boolean allItemsRemoved;

	public FirebaseHelper(DatabaseReference databaseRef) {
		this.databaseRef = databaseRef;
	}

	public Boolean saveItem(Item item) {
		if (item == null) {
			itemSaved = false;
		} else {
			try {
				databaseRef.child("items").push().setValue(item);

				itemSaved = true;
			} catch (DatabaseException e) {
				e.printStackTrace();

				itemSaved = false;
			}
		}

		return itemSaved;
	}

	public void retrieveItems(final Context context, final ListView itemList, final String sortBy) {
		databaseRef.orderByValue().addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				fetchData(dataSnapshot, sortBy);

				populateListView(context, itemList);
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
				fetchData(dataSnapshot, sortBy);

				populateListView(context, itemList);
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {
				itemList.setAdapter(new ArrayAdapter<Item>(context, R.layout.list_item, R.id.item, new ArrayList<Item>()));
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	public void retrieveItemCount(final TextView itemCountNo) {
		databaseRef.child("items").addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				itemCountNo.setText(Integer.toString((int) dataSnapshot.getChildrenCount()));
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				itemCountNo.setText("0");
			}
		});
	}

	private void fetchData(DataSnapshot dataSnapshot, String sortBy) {
		items.clear();

		for (DataSnapshot ds : dataSnapshot.getChildren()) {
			Item item = ds.getValue(Item.class);

			item.setKey(ds.getKey());

			items.add(item);
		}

		if (sortBy.toLowerCase().equals("name")) {
			Collections.sort(items, new Comparator<Item>() {
				public int compare(Item item1, Item item2) {
					if (item2.getName() == item1.getName()) {
						return 0;
					} else if (item1.getName() == null) {
						return -1;
					} else if (item2.getName() == null) {
						return 1;
					}

					return item1.getName().compareTo(item2.getName());
				}
			});
		} else {
			Collections.sort(items, new Comparator<Item>() {
				public int compare(Item item1, Item item2) {
					return item2.getAmount() - item1.getAmount();
				}
			});
		}
	}

	private void populateListView(final Context context, final ListView itemList) {
		ArrayAdapter<Item> itemListAdapter = new ArrayAdapter<Item>(context, R.layout.list_item, R.id.item, items);

		itemList.setAdapter(itemListAdapter);
	}

	public Boolean removeItem(Item item) {
		if (item == null) {
			itemRemoved = false;
		} else {
			try {
				databaseRef.child("items").child(item.getKey()).removeValue();

				itemRemoved = true;
			} catch (DatabaseException e) {
				e.printStackTrace();

				itemRemoved = false;
			}
		}

		return itemRemoved;
	}

	public Boolean removeAllItems() {
		try {
			databaseRef.child("items").removeValue();

			allItemsRemoved = true;
		} catch (DatabaseException e) {
			e.printStackTrace();

			allItemsRemoved = false;
		}

		return allItemsRemoved;
	}

	public void shareShoppingListItems(final Context context) {
		databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Intent intent = new Intent(Intent.ACTION_SEND);

				intent.setType("text/plain");

				String shoppingListText = "My Shopping List:";

				fetchData(dataSnapshot, "name");

				for (DataSnapshot ds : dataSnapshot.getChildren()) {
					for (DataSnapshot dsChildren : ds.getChildren()) {
						Item item = dsChildren.getValue(Item.class);

						shoppingListText += System.lineSeparator() + item;
					}
				}

				intent.putExtra(Intent.EXTRA_TEXT, shoppingListText);

				Intent chooser = Intent.createChooser(intent, "Share Shopping List with");

				if (intent.resolveActivity(context.getPackageManager()) != null) {
					context.startActivity(chooser);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}
}
