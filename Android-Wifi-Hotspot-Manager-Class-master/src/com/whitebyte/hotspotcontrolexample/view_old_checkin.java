
package com.whitebyte.hotspotcontrolexample;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.whitebyte.hotspotclients.R;
import com.whitebyte.wifihotspotutils.ClientScanResult;
import com.whitebyte.wifihotspotutils.WifiApManager;

public class View_old_checkin extends Activity {
	WifiApManager wifiApManager;
	
	private SimpleAdapter sca;
	private int[] photoId = {R.drawable.jx, R.drawable.xy};
	private ArrayList<Map<String, Object>> stuList = new ArrayList<Map<String, Object>>();
	
	private class StuInfo{
		String sName;
		String sAddr;
		String sArrive;
		String sLeave;
		StuInfo(String s, String t, String u, String v){
			sName = s;
			sAddr = t;
			sArrive = u;
			sLeave = v;
		}
	}


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_main);

		init_stu();

		GridView gv = (GridView)this.findViewById(R.id.OldStuList);
		
		sca = new SimpleAdapter(
				this,
				stuList,
				R.layout.item,
				new String[]{"col1", "col2", "col3"},
				new int[]{R.id.photo, R.id.namae, R.id.taimu}
				);
		gv.setAdapter(sca);

	}

	private void getListForSimpleAdapter(List<StuInfo> stu){
		stuList.clear();
		int i = 0;
		for (StuInfo ss : stu){
			HashMap<String, Object> hmap = new HashMap<String, Object>();
			hmap.put("col1", photoId[i]);
			hmap.put("col2", ss.sName);
			hmap.put("col3", "µ½´ï£º\n"+ss.sArrive+"\nÀë¿ª£º\n"+ss.sLeave);
			i++;
			stuList.add(hmap);
		}
	}
	
	public void init_stu(){
		List<StuInfo> stu = new ArrayList<StuInfo>();
		String fileName = "myfile.txt";
		String fileTime = "myTime.txt";
		String usrHWAddr;
		String usrName;
		String comeTime;
		String leaveTime;
		
		String line = null;
		String line1 = null;
		String[] parts = null;
		String[] parts1 = null;
		FileInputStream inStream;
		FileInputStream tmStream;

		try {
			inStream = openFileInput(fileName);
			DataInputStream in = new DataInputStream(inStream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
			tmStream = openFileInput(fileTime);
			DataInputStream in1 = new DataInputStream(tmStream);
		    BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
		    try {
				while ((line = br.readLine()) != null) {
					parts = line.split(";");
					usrName = parts[1];
					usrHWAddr = parts[0];
					comeTime = "null";
					leaveTime = "null";
					while ((line1 = br1.readLine()) != null) {
						parts1 = line1.split(";");
						if (usrHWAddr.equals(parts1[0])){
							comeTime = parts1[1];
							leaveTime = parts1[2];
							break;
						}
					}
					StuInfo si = new StuInfo(usrName,usrHWAddr,comeTime,leaveTime);
					stu.add(si);
				}
			} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
		}
		getListForSimpleAdapter(stu);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Get Clients");
		menu.add(0, 1, 0, "Open AP");
		menu.add(0, 2, 0, "Close AP");
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			break;
		case 1:
			wifiApManager.setWifiApEnabled(null, true);
			break;
		case 2:
			wifiApManager.setWifiApEnabled(null, false);
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}
}