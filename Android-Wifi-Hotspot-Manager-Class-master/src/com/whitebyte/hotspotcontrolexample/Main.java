
package com.whitebyte.hotspotcontrolexample;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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

			String filename = "myfile.txt";
//			String string = "28:e1:4c:7c:35:a7;Jiaxin Lee";
			String line = null;
			String[] parts = null;
//			FileOutputStream outputStream;
			FileInputStream inStream;

/*			try {
			  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
			  outputStream.write(string.getBytes());
			  outputStream.close();
			} catch (Exception e) {
			  textView1.append("haha\n");
			  e.printStackTrace();
			}
*/
			try {
				inStream = openFileInput(filename);
				DataInputStream in = new DataInputStream(inStream);
		        BufferedReader br = new BufferedReader(new InputStreamReader(in));
		        try {
					while ((line = br.readLine()) != null) {
						parts = line.split(";");
						if (parts[0].equals(usrHWAddr)){
							usrName = parts[1];
							break;
						}
						textView1.append(line+"\n");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					  textView1.append("why\n");
					e.printStackTrace();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				textView1.append("heh\n");
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