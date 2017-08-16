//package details forbidden
import java.sql.Connection;
import java.sql.DriverManager;

import xx.DatabasePatterns;

public class DBCon_EOffice{
  public static Connection getConnection(){
    Connection con=null;
    try{

    	Class.forName("org.postgresql.Driver");
    	con=DriverManager.getConnection("jdbc:postgresql://"+DatabasePatterns.DB_HOST_PORT+DatabasePatterns.DATABASE_NAME,DatabasePatterns.USERNAME,DatabasePatterns.PASSWORD);
    	System.out.println("DATABASE CONNECTION  <=========================================> "+con);

    }catch(Exception e)
      {
              System.out.println("error"+e);
              e.printStackTrace();
      }
    if(con!=null)
      {
          return con;
      }
   else
      {
          return null;
      }
  }
}
