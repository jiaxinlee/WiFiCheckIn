
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

public class Start_a_new_checkin extends Activity {
	WifiApManager wifiApManager;
	
	private Timer mTimer;
	private TimerTask mTimerTask;
	private Handler mHandler;
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
		setContentView(R.layout.main);
		List<StuInfo> stu = new ArrayList<StuInfo>();

		StuInfo jx = new StuInfo("Jiaxin Lee","28:e1:4c:7c:35:a7","null","null");
		stu.add(jx);
		StuInfo xy = new StuInfo("Xiaoyang Wang", "68:96:7b:ed:a2:20","null","null");
		stu.add(xy);
		
		init_stu(stu);
		getListForSimpleAdapter(stu);

		GridView gv = (GridView)this.findViewById(R.id.StuList);
		
		sca = new SimpleAdapter(
				this,
				stuList,
				R.layout.item,
				new String[]{"col1", "col2", "col3"},
				new int[]{R.id.photo, R.id.namae, R.id.taimu}
				);
		gv.setAdapter(sca);
		
		wifiApManager = new WifiApManager(this);
		
		scan();
		
		mTimer = new Timer();

		mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 0:
					scan();
					sca.notifyDataSetChanged();
				break;
				}
			}
		};
		
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(0);
			}
		};
		
		mTimer.schedule(mTimerTask, 1000, 1000);
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
	
	public void init_stu(List<StuInfo> stu){
		String fileName = "myfile.txt";
		String fileTime = "myTime.txt";
		FileOutputStream outputStream;
		deleteFile(fileName);
		try {
			  outputStream = openFileOutput(fileName, Context.MODE_APPEND);
			  for (StuInfo ss : stu){
				  outputStream.write((ss.sAddr+";"+ss.sName+"\n").getBytes());
			  }
			  outputStream.close();
			} catch (Exception e) {
			  e.printStackTrace();
			}
		deleteFile(fileTime);
		try {
			  outputStream = openFileOutput(fileTime, Context.MODE_APPEND);
			  for (StuInfo ss : stu){
				  outputStream.write((ss.sAddr+";null;null\n").getBytes());
			  }
			  outputStream.close();
			} catch (Exception e) {
			  e.printStackTrace();
			}
	}

	private void scan() {
		String usrIpAddr;
		String usrHWAddr;
		String usrName;
		String comeTime;
		String leaveTime;
		ArrayList<ClientScanResult> clients = wifiApManager.getClientList(false);
		
		List<StuInfo> stu = new ArrayList<StuInfo>();

		String fileName = "myfile.txt";
		String fileTime = "myTime.txt";
		String line = null;
		String line1 = null;
		String[] parts = null;
		String[] parts1 = null;
		FileOutputStream outputStream;
		FileInputStream inStream;
		FileInputStream tmStream;
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());
		String tm = formatter.format(curDate);
		List<String> lst = new ArrayList<String>();

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
					usrIpAddr = "OffLine";
					usrName = parts[1];
					usrHWAddr = parts[0];
					comeTime = "null";
					leaveTime = "null";
					for (ClientScanResult clientScanResult : clients) {
						if (usrHWAddr.equals(clientScanResult.getHWAddr())){
							usrIpAddr = clientScanResult.getIpAddr();
							break;
						}
					}
					while ((line1 = br1.readLine()) != null) {
						parts1 = line1.split(";");
						if (usrHWAddr.equals(parts1[0])){
							comeTime = parts1[1];
							leaveTime = parts1[2];
							break;
						}
					}
					if(comeTime.equals("null") && !usrIpAddr.equals("OffLine")){
						comeTime = tm;
					}
					if(!comeTime.equals("null") && leaveTime.equals("null") && usrIpAddr.equals("OffLine")){
						leaveTime = tm;
					}
					if(!leaveTime.equals("null") && !usrIpAddr.equals("OffLine")){
						comeTime = tm;
						leaveTime = "null";
					}
					lst.add(usrHWAddr+";"+comeTime+";"+leaveTime+"\n");
					StuInfo si = new StuInfo(usrName,usrHWAddr,comeTime,leaveTime);
					stu.add(si);
				}
			} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
		}
		getListForSimpleAdapter(stu);

		deleteFile(fileTime);
		try {
		  outputStream = openFileOutput(fileTime, Context.MODE_APPEND);
		  for (String ss:lst){
			  outputStream.write(ss.getBytes());
		  }
		  outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}

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
			scan();
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