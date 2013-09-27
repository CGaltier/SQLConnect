package com.pmbtest.sqlconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;


import java.sql.PreparedStatement;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import net.sourceforge.jtds.jdbc.*;
import net.sourceforge.jtds.jdbcx.*;


public class MainActivity extends Activity
{

  EditText txtUsername;
  EditText txtPassword;
  EditText txtServerIP;
  EditText txtServerPort;
  EditText txtDBName;
  EditText txtDBTable;
  EditText txtInstance;
  
  TextView txtConnect;
  TextView txtRequest;
  TextView txtResult;
  
  Button btnLogin;

  
  public void Query()
  {
    Log.i("Android","Sql connect test");
    Connection conn = null ;
    try
    {
      String driver = "net.sourceforge.jtds.jbdc.Driver";
      Class.forName(driver).newInstance();
      String serverIP="server_ip_adress";
      String serverPort="1433";
      String dbName="DBNAME";
      String user="xxxxxxxxx";
      String passwd="xxxxxxxx";
      String userName="xxxxxxxx";
      String password="xxxxxxxx";      
      String tableName="tableName";
      int columnIndex = 3;
      String connString = "jbdc:jtds:sqlserver://"+serverIP+" :"+serverPort+"/"+dbName+";encrypt=false;user="+user+"password="+passwd+"instance=SQLEXPRESS;";
      txtRequest.setText(connString);
      conn=DriverManager.getConnection(connString,userName,password);
      Log.w("Connection","open");
      Statement stmt = conn.createStatement();
      ResultSet reset =stmt.executeQuery("select * from "+tableName);
      while (reset.next())
      {
        Log.w("Data :",reset.getString(columnIndex));
      }
      conn.close();
      }
      catch(Exception e)
      {
        Log.w("Error connecting",""+e.getMessage());
      }
  }
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      //Assign properties to login form views
      txtUsername =   (EditText)findViewById(R.id.txtUsername);
      txtPassword =   (EditText)findViewById(R.id.txtPassword);
      txtServerIP =   (EditText)findViewById(R.id.txtServerIP);
      txtServerPort = (EditText)findViewById(R.id.txtServerPort);
      txtDBName =     (EditText)findViewById(R.id.txtDBName);
      txtDBTable =    (EditText)findViewById(R.id.txtDBTable);      
      txtInstance=    (EditText)findViewById(R.id.txtInstance);
      
      txtRequest=(TextView)findViewById(R.id.txtRequest);
      txtConnect=(TextView)findViewById(R.id.txtConnect);
      txtResult=(TextView)findViewById(R.id.txtResult);
      
      btnLogin = (Button)findViewById(R.id.btnLogin);    
   
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
  
  @SuppressWarnings("deprecation")
  public void login_onClick(View view) {
      Connection con = null;
      try{
          Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
          String serverName = txtServerIP.getText().toString();
          String serverPort = txtServerPort.getText().toString();
          String dbName = txtDBName.getText().toString();
          String dbTable = txtDBTable.getText().toString();
          String userName = txtUsername.getText().toString();
          String userPwd = txtPassword.getText().toString();
          String instance = txtInstance.getText().toString();


          //String connString = "jdbc:jtds:sqlserver://MY_SERVER_NAME:24923/Phone_Test;user=testLogin;password=xxxxxxxx;instance=MYINSTANCE";
          String connString = "jdbc:jtds:sqlserver://"+serverName+":"+serverPort+"/"+dbName+";user="+userName+";password="+userPwd+";instance="+instance;
          //String username = "testLogin";
          //String password = "xxxxxxxx";
          txtConnect.setText(connString);
          //con = DriverManager.getConnection(connString,username,password);
          con = DriverManager.getConnection(connString,userName,userPwd);
          PreparedStatement stmt = null;
          try {
              //Prepared statement
              //stmt = con.prepareStatement("SELECT * FROM Logins WHERE Username = ? AND Password = ?");
              //stmt.setString(1, txtUsername.toString());
              //stmt.setString(2, txtPassword.toString());
        	  String statement = "SELECT * FROM "+dbTable;
        	  stmt= con.prepareStatement(statement);
        	  txtRequest.setText(statement);
              stmt.execute();

              ResultSet rs = stmt.getResultSet();
              if(rs.first()) 
              {
                  while (rs.next())
                  {
                	  txtRequest.append(rs.getString(3));
                  }
            	  
				  //Start new activity
				  AlertDialog ad = new AlertDialog.Builder(this).create();
				  ad.setTitle("Success!");
				  ad.setMessage("You have logged in successfully!");
				  ad.setButton("OK", new DialogInterface.OnClickListener()
								  {
									
									  @Override
									  public void onClick(DialogInterface dialog, int which) 
									  {
										  dialog.dismiss();
									  }
				                  } );
                  ad.show();
              }
              stmt.close();
          }
          catch (Exception e) {
              stmt.close();
              AlertDialog ad = new AlertDialog.Builder(this).create();
              ad.setTitle("Error");
              ad.setMessage("Prepared statement error: " + e.getMessage());
              ad.setButton("OK", new DialogInterface.OnClickListener() {

                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();

                  }
              });
              ad.show();
          }
          con.close();
      }
      catch (Exception e) {
    	  Log.w("Erreur connexion", e.getMessage(), e);
          AlertDialog ad = new AlertDialog.Builder(this).create();
          ad.setTitle("Error");
          ad.setMessage("Connection error: " + e.getMessage());
          ad.setButton("OK", new DialogInterface.OnClickListener() {

              @Override
              public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();

              }
          });
          ad.show();
      }
  }


}
