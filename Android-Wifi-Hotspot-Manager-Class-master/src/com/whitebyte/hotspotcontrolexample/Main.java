
package com.whitebyte.hotspotcontrolexample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.whitebyte.hotspotclients.R;
import com.whitebyte.wifihotspotutils.ClientScanResult;
import com.whitebyte.wifihotspotutils.WifiApManager;

public class Main extends Activity {
	TextView textView1;
	WifiApManager wifiApManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		textView1 = (TextView) findViewById(R.id.textView1);
		wifiApManager = new WifiApManager(this);
		
		scan();
	}

	private void scan() {
		String usrIpAddr;
		String usrDevice;
		String usrHWAddr;
		String usrName = new String("Nobody");
		ArrayList<ClientScanResult> clients = wifiApManager.getClientList(false);

		textView1.setText("WifiApState: " + wifiApManager.getWifiApState() + "\n\n");

		textView1.append("Clients: \n");
		for (ClientScanResult clientScanResult : clients) {
			usrIpAddr = clientScanResult.getIpAddr();
			usrDevice = clientScanResult.getDevice();
			usrHWAddr = clientScanResult.getHWAddr();

			File fin = new File("UsrInfo.txt");
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(fin);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			try {
				while ((line = br.readLine()) != null) {
					if (line.equals(usrHWAddr)){
						usrName = br.readLine();
						break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			textView1.append("####################\n");
			textView1.append("IpAddr: " + usrIpAddr + "\n");
			textView1.append("Device: " + usrDevice + "\n");
			textView1.append("HWAddr: " + usrHWAddr + "\n");
			textView1.append("UsrName: " + usrName + "\n");
			
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