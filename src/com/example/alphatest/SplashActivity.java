package com.example.alphatest;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SplashActivity extends ActionBarActivity {

	private static boolean isCancelled = false;
	private static final String SHARED_PREF_TAG = "alphatest_pref";
	private static final long SPLASH_TIME_MILLIS = 3*1000;
	private static Handler timer = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

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

	public static class PlaceholderFragment extends Fragment {
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
			return rootView;
		}
		
		@Override
		public void onActivityCreated(Bundle b){
			super.onActivityCreated(b);
			if(isStartedFirstTime()){
				new UpdateItemsTask(new OnUpdateItemsListener(){
	
					@Override
					public void onPostUpdate(List<Item> items) {
						if(!isCancelled){
							if(items != null){
								setStartedNotFirstTime();
								if(getActivity() != null){
									startActivity(new Intent(getActivity(), MainActivity.class));
									getActivity().finish();
								}
							}else{
								Toast.makeText(getActivity(), "Can't connect to a server", Toast.LENGTH_LONG).show();
								if(getActivity() != null){
									startActivity(new Intent(getActivity(), MainActivity.class));
									getActivity().finish();
								}
							}
						}
					}
					
				}, getActivity()).execute();
			}else{
				timer.postDelayed(new Runnable(){

					@Override
					public void run() {
						if(!isCancelled){
							if(getActivity() != null){
								startActivity(new Intent(getActivity(), MainActivity.class));
								getActivity().finish();
							}
						}
					}
					
				}, SPLASH_TIME_MILLIS);
			}
		}
		
		public boolean isStartedFirstTime(){
			SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_TAG, MODE_PRIVATE);
			return sp == null || !sp.contains("alphatest_ft");
		}
		
		public void setStartedNotFirstTime(){
			getActivity().getSharedPreferences(SHARED_PREF_TAG, MODE_PRIVATE).edit().putBoolean("alphatest_ft", true).commit();
		}
	}

}
