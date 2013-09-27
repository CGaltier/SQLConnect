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
  TextView labelLogin;
  TextView labelError;
  EditText txtUsername;
  EditText txtPassword;
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
      labelLogin = (TextView)findViewById(R.id.labelLogin);
      labelError = (TextView)findViewById(R.id.labelError);
      txtUsername = (EditText)findViewById(R.id.txtUsername);
      txtPassword = (EditText)findViewById(R.id.txtPassword);
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



          String connString = "jdbc:jtds:sqlserver://MY_SERVER_NAME:24923/Phone_Test;user=testLogin;password=xxxxxxxx;instance=MYINSTANCE";
          String username = "testLogin";
          String password = "xxxxxxxx";
          con = DriverManager.getConnection(connString,username,password);
          PreparedStatement stmt = null;
          try {
              //Prepared statement
              stmt = con.prepareStatement("SELECT * FROM Logins WHERE Username = ? AND Password = ?");
              stmt.setString(1, txtUsername.toString());
              stmt.setString(2, txtPassword.toString());
              stmt.execute();

              ResultSet rs = stmt.getResultSet();
              if(rs.next()) {
                  //Start new activity
                  AlertDialog ad = new AlertDialog.Builder(this).create();
                  ad.setTitle("Success!");
                  ad.setMessage("You have logged in successfully!");
                  ad.setButton("OK", new DialogInterface.OnClickListener() {

                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();

                      }
                  });
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
