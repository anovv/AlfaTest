package com.example.alphatest;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MainActivity extends ActionBarActivity {

	private static boolean isCancelled = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	@Override 
	protected void onStop(){
		isCancelled = true;
		super.onStop();
	}
	
	@Override
	protected void onResume(){
		isCancelled = false;
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(MainActivity.this, AboutActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class PlaceholderFragment extends ListFragment {

		private ItemListAdapter adapter;
        private PullToRefreshListView pullToRefreshListView;

		public PlaceholderFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_main, container, false);
			
			ListView lv = (ListView) view.findViewById(android.R.id.list);
            ViewGroup parent = (ViewGroup) lv.getParent();
            int lvIndex = parent.indexOfChild(lv);
    		
            pullToRefreshListView = new PullToRefreshListView(getActivity());
            pullToRefreshListView.setLayoutParams(lv.getLayoutParams());

            pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>(){
                @Override
                public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                	
                	new UpdateItemsTask(new OnUpdateItemsListener(){

						@Override
						public void onPostUpdate(List<Item> items) {

							if(pullToRefreshListView != null){
								pullToRefreshListView.onRefreshComplete();
							}
							
							if(!isCancelled){
								if(items != null){
									//populate adapter
									adapter = new ItemListAdapter(getActivity(), android.R.id.list, items);
					            	setListAdapter(adapter);
								}else{
									Toast.makeText(getActivity(), "Can't connect to a server", Toast.LENGTH_LONG).show();
								}
							}
						}
                		
                	}, getActivity()).execute();
                }
            });
            
            parent.addView(pullToRefreshListView, lvIndex, lv.getLayoutParams());

			return view;
		}
		
		@Override
		public void onActivityCreated(Bundle b){
			super.onActivityCreated(b);
			
			new FetchItemsFromDbTask(new OnUpdateItemsListener(){

				@Override
				public void onPostUpdate(List<Item> items) {
					if(!isCancelled){
						if(items != null){
							//populate adapter
							adapter = new ItemListAdapter(getActivity(), android.R.id.list, items);
			            	setListAdapter(adapter);
						}else{
							Toast.makeText(getActivity(), "Can't connect to a server", Toast.LENGTH_LONG).show();
						}
					}
				}
				
			}, getActivity()).execute();
		}
		
		 
		@Override
		
		public void onListItemClick(ListView l, View v, int position, long id) {
			super.onListItemClick(l, v, position, id);
			Item item = (Item) getListAdapter().getItem(position - 1);
			Bundle bundle = new Bundle();
			bundle.putString("link", item.getLink());
			bundle.putString("title", item.getTitle());
			Intent intent = new Intent(getActivity(), ViewItemActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		} 
	}
}
