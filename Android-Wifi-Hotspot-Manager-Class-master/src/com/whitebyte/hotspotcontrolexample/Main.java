
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.whitebyte.hotspotclients.R;
import com.whitebyte.wifihotspotutils.ClientScanResult;
import com.whitebyte.wifihotspotutils.WifiApManager;

public class Main extends Activity {
	TextView textView1;
	WifiApManager wifiApManager;
	
	private Timer mTimer;
	private TimerTask mTimerTask;
	private Handler mHandler;
	
	private class StuInfo{
		String sName;
		String sAddr;
		StuInfo(String s, String t){
			sName = s;
			sAddr = t;
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		textView1 = (TextView) findViewById(R.id.textView1);
		wifiApManager = new WifiApManager(this);
		
		List<StuInfo> stu = new ArrayList<StuInfo>();

		StuInfo jx = new StuInfo("Jiaxin Lee","28:e1:4c:7c:35:a7");
		stu.add(jx);
		StuInfo xy = new StuInfo("Xiaoyang Wang", "68:96:7b:ed:a2:20");
		stu.add(xy);
		
		init_stu(stu);
		
		scan();
		
		mTimer = new Timer();

		mTimer = new Timer();

		mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 0:
					textView1.setText("");
					scan();
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
		
		mTimer.schedule(mTimerTask, 10000, 1000);
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
			  textView1.append("haha\n");
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
			  textView1.append("haha\n");
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

		textView1.setText("签到详情  \n\n");

		textView1.append("学生: \n");
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

/*		try {
		  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
		  outputStream.write(string.getBytes());
		  outputStream.close();
		} catch (Exception e) {
		  textView1.append("haha\n");
		  e.printStackTrace();
		}
*/
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
					textView1.append("####################\n");
					textView1.append("姓名: " + usrName + "\n");
					textView1.append("IP: " + usrIpAddr + "\n");
					textView1.append("MAC: " + usrHWAddr + "\n");
					while ((line1 = br1.readLine()) != null) {
						parts1 = line1.split(";");
						if (usrHWAddr.equals(parts1[0])){
							comeTime = parts1[1];
							leaveTime = parts1[2];
							break;
						}
					}
					//textView1.append(line1+"\n");
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
					textView1.append("到达: " + comeTime + "\n");
					textView1.append("离开: " + leaveTime + "\n");
					lst.add(usrHWAddr+";"+comeTime+";"+leaveTime+"\n");
				}
			} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}

			textView1.append("\n");
		deleteFile(fileTime);
		try {
		  outputStream = openFileOutput(fileTime, Context.MODE_APPEND);
		  for (String ss:lst){
			  outputStream.write(ss.getBytes());
		  }
		  outputStream.close();
		} catch (Exception e) {
		  textView1.append("haha\n");
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