package com.opposablebrain.android.bitcoindroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpHost;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class BitcoinConnect extends Activity implements OnClickListener {
	public static final String PREFS_NAME = "BitcoinDroidPrefs";

	private String mk = "IKVxEhN1RbVUMVnOtFGmxqn6TZlScmhF7py86TkksSjOsxEb0EjaT9VClhZkZrx";
	
	private EditText rPass, rPort, rServer;
	private Button btnLogin;
	private TextView tv;

	private int port = -1;
	private String password = "";
	private String server = "";
	private CryptoHelper ch = new CryptoHelper();
	private String cryptopass, cryptoserver, cryptoport, cryptostatus;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String decryptopass, decryptoserver, decryptoport, decryptostatus;
		
		btnLogin = (Button) findViewById(R.id.login_button);
		rPass = (EditText) findViewById(R.id.password);
		rServer = (EditText) findViewById(R.id.server);
		rPort = (EditText) findViewById(R.id.port);
		tv = (TextView) findViewById(R.id.result);

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		cryptopass = settings.getString("password", "");
		cryptoserver = settings.getString("server", "");
		cryptoport = settings.getString("port", "");
		cryptostatus = settings.getString("status", "");

		ch.setPassword(mk);
		try{
			decryptopass = ch.decrypt(cryptopass);
		}catch (CryptoHelperException e){
			decryptopass = "";
		}
		try{
			decryptoserver = ch.decrypt(cryptoserver);
		}catch (CryptoHelperException e){
			decryptoserver = "";
		}
		try{
			decryptoport = ch.decrypt(cryptoport);
		}catch (CryptoHelperException e){
			decryptoport = "";
		}
		try{
			decryptostatus = ch.decrypt(cryptostatus);
		}catch (CryptoHelperException e){
			decryptostatus = "";
		}
		
		rPass.setText(decryptopass);
		rPort.setText(decryptoport);
		rServer.setText(decryptoserver);
		tv.setText(decryptostatus);
       
       
		try {
			port = Integer.parseInt(rPort.getText().toString().trim());
			password = rPass.getText().toString();
			server = rServer.getText().toString();
			getStatus(server, port, password);
		} catch (Exception e) {
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
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss a zzz");
		Date currentTime_1 = new Date();
		String dateString = formatter.format(currentTime_1);
				
		tv.setText(dateString + "\n------------\n");
		try {
			String out = client.callString("getinfo", (Object[]) params);
			org.json.JSONObject object = new JSONObject(out);
			double balance = object.getDouble("balance");
			tv.setText(tv.getText() + "Balance: " + balance + "\n");
			double khps = object.getDouble("KHPS");
			tv.setText(tv.getText() + "KHPS: " + khps + "\n");
			int blocks = object.getInt("blocks");
			tv.setText(tv.getText() + "Blocks: " + blocks + "\n");
			double difficulty = object.getDouble("difficulty");
			tv.setText(tv.getText() + "Difficulty: " + difficulty + "\n");
			boolean generate = object.getBoolean("generate");
			tv.setText(tv.getText() + "Generate: " + generate + "\n");
		} catch (Exception e) {
			tv.setText(tv.getText() + "Error: " + e.getMessage());
		}

	}

	@Override
	public void onClick(View v) {
		storeSettings();
		getStatus(server, port, password);
	}

	public void storeSettings(){
	      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      
	      ch.setPassword(mk);
	      try{
				cryptopass = ch.encrypt(rPass.getText().toString());
				cryptoserver = ch.encrypt(rServer.getText().toString());
				cryptoport = ch.encrypt(rPort.getText().toString());
				cryptostatus = ch.encrypt(tv.getText().toString());
		  }catch (CryptoHelperException e){}

	      
	      editor.putString("password", cryptopass );
	      editor.putString("server", cryptoserver );
	      editor.putString("port", cryptoport);
	      editor.putString("status", cryptostatus );
		  editor.commit();
	}
	
	 @Override
	    protected void onStop(){
	       super.onStop();
	       storeSettings();
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