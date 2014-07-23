package com.example.alphatest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ViewItemActivity extends ActionBarActivity {

	private static String link;
	private static String title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_item);
		
		Bundle bundle = getIntent().getExtras();
		link = bundle.getString("link");
		title = bundle.getString("title");
		
		getActionBar().setTitle(title);
		
		if (savedInstanceState == null) {

			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.view_item, menu);
		
		MenuItem item = menu.findItem(R.id.menu_item_share);
		ShareActionProvider shareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);
	    
		Intent myIntent = new Intent();
		myIntent.setAction(Intent.ACTION_SEND);
		myIntent.putExtra(Intent.EXTRA_TEXT, "AlphaBank news: \n" + link);
		myIntent.setType("text/plain");
		shareActionProvider.setShareIntent(myIntent);
		
		return true;
	}

	public static class PlaceholderFragment extends Fragment {
		
		private ProgressDialog progressDialog;
		private WebView webView;
		
		public PlaceholderFragment() {
			
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_view_item, container, false);
			webView = (WebView) rootView.findViewById(R.id.item_webview);
			return rootView;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState){
			super.onActivityCreated(savedInstanceState);
			webView.setWebViewClient(new WebViewClient(){
				
				public void onPageFinished(WebView view, String url) {
					getActivity().runOnUiThread(new Runnable(){
						
						@Override
						public void run() {
							if(progressDialog != null){
								progressDialog.dismiss();
							}
						}
					});
			    }
			});
			
		    webView.getSettings().setBuiltInZoomControls(true);	  
			getActivity().runOnUiThread(new Runnable(){

				@Override
				public void run() {
					progressDialog = new ProgressDialog(getActivity());
					progressDialog.setCancelable(true);
					progressDialog.setMessage("Loading...");
					progressDialog.show();
				}
				
			});

			webView.loadUrl(link);
		}
	}

}
