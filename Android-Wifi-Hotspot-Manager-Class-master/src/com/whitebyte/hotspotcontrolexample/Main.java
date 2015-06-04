
package com.whitebyte.hotspotcontrolexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.whitebyte.hotspotclients.R;

public class Main extends Activity {
	private Button button1;
	private Button button2;
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init);
		
		button1=(Button)findViewById(R.id.bt1);
	    button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(Main.this, View_old_checkin.class);
				startActivity(intent);
				Main.this.finish();				
			}
		});
	   
	    button2=(Button)findViewById(R.id.bt2);
	    button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(Main.this, Start_a_new_checkin.class);
				startActivity(intent);
				Main.this.finish();
			}
		});
	   
	}
/*
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init);

		button1=(Button)findViewById(R.id.bt1);
	    button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(Main.this, start_a_new_checkin.class);
				startActivity(intent);
				Main.this.finish();
			}
		});
	    button2=(Button)findViewById(R.id.bt2);
	    button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(Main.this, view_old_checkin.class);
				startActivity(intent);
				Main.this.finish();
			}
		});
	}
*/
}