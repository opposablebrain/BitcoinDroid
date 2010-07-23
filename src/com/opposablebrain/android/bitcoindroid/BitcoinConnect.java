package com.opposablebrain.android.bitcoindroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpHost;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BitcoinConnect extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	private EditText rPass, rPort, rServer;
	private Button btnLogin;
	private TextView tv;

	private int port = -1;
	private String password = "";
	private String server = "";

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnLogin = (Button) findViewById(R.id.login_button);
		rPass = (EditText) findViewById(R.id.password);
		rServer = (EditText) findViewById(R.id.server);
		rPort = (EditText) findViewById(R.id.port);
		tv = (TextView) findViewById(R.id.result);


		try {
			port = Integer.parseInt(rPort.getText().toString().trim());
			password = rPass.getText().toString();
			server = rServer.getText().toString();
		} catch (Exception e) {
//			tv.setText("Exception while parsing one of the input parameters:\n"
//					+ e.getMessage());
		}

		// Set Click Listener
		btnLogin.setOnClickListener(this);
	}

	public void getStatus(String server, int port, String password) {
		HttpHost host = new HttpHost(server, port, "https");
		JSONRPCClient client = JSONRPCClient.create(host, "/");
		client.setConnectionTimeout(2000);
		client.setSoTimeout(2000);

		String[] params = { password };

		try {
			String out = client.callString("getinfo", (Object[]) params);
			org.json.JSONObject object = new JSONObject(out);
			double balance = object.getDouble("balance");
			tv.setText("Balance: " + balance + "\n");
			double khps = object.getDouble("KHPS");
			tv.setText(tv.getText() + "KHPS: " + khps + "\n");
			int blocks = object.getInt("blocks");
			tv.setText(tv.getText() + "Blocks: " + blocks + "\n");
			double difficulty = object.getDouble("difficulty");
			tv.setText(tv.getText() + "Difficulty: " + difficulty + "\n");
			boolean generate = object.getBoolean("generate");
			tv.setText(tv.getText() + "Generate: " + generate + "\n");

		} catch (Exception e) {
			tv.setText(e.getMessage());
		}

	}

	@Override
	public void onClick(View v) {
		getStatus(server, port, password);
	}

	public String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				is.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}

}