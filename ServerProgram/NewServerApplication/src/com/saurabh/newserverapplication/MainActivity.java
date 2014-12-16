package com.saurabh.newserverapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity
{
	public Handler handle = new Handler();
	TextView text;
	public ServerSocket server_socket;
	Socket server = null;
	BufferedReader input;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		text = (TextView) findViewById(R.id.text_view);
		
		ServerThread server_thread = new ServerThread();
		server_thread.start();
	}

	public class ServerThread extends Thread
	{
		int port_number = 4444;
		ServerSocket server_socket = null;
		
		
		public void run()
		{
			try
			{
				server_socket = new ServerSocket(port_number);
			}
			catch(IOException e)
			{
				runOnUiThread( new Runnable()
				{
					public void run()
					{
						text.append("Unable to listen at port : "+ port_number);
					}
				});
				
				//text.append((CharSequence) e);
			}
				
			try
			{
				 server = server_socket.accept();
				 
				 runOnUiThread( new Runnable()
					{
						public void run()
						{
							text.append("Don't worry, atleast connection has been made.\n");
							//text.append((CharSequence) e);
						}
					});
			}
			catch(IOException e)
			{
				runOnUiThread( new Runnable()
				{
					public void run()
					{
						text.append("Unable to connect to the client.\n");
						//text.append((CharSequence) e);
					}
				});
				
			}
			
			
					
			try
			{
				 input = new BufferedReader(new InputStreamReader(server.getInputStream()) );
			}
			catch(IOException e)
			{
				runOnUiThread( new Runnable()
				{
					public void run()
					{
						text.append("Unable to get input.\n");
						//text.append((CharSequence) e);
					}
				});
				
			}
			
			  final String[] line = new String[1];
			  line[0] = "";
			  
			try
			{
					line[0] = line[0] + input.readLine();
				
					if(line[0] != null)
					{
						
						runOnUiThread( new Runnable()
						{
							public void run()
							{
								//text.append("Data is not null.\n");
										
								try
								{
									
									String[] mess = line[0].split(":::");
									
									int i=0;
									
									while(i < mess.length)
									{
										String[] mess_small = mess[i].split("::");
										
										int j=0;
										
										while(j < mess_small.length )
										{
											text.append("\n" + mess_small[j]);
											j++;
										}
										
										text.append("\n");
										i++;
									}
								}
								catch(Exception e)
								{
									text.append("Not appending data.");
								}
								
							}
						});
					
					}
			}
			catch(IOException e)
			{
				runOnUiThread( new Runnable()
				{
					public void run()
					{
						text.append("Unable to read from input stream.\n");
						//text.append((CharSequence) e);
					}
				});
				
			}

				
			try
			{
				input.close();	
				server.close();
				server_socket.close();
			}
			catch(IOException e)
			{
				runOnUiThread( new Runnable()
				{
					public void run()
					{
						text.append("Unable to close server and input stream.\n");
						//text.append((CharSequence) e);
					}
				});
				
			}
			
		}
			
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
	
	public void onStop()
	{
		try
		{

			input.close();	
			server.close();
			server_socket.close();
		}
		catch(Exception e)
		{
			text.append("Problem in closing socket...\n");
		}
	}
	
	
	
}
