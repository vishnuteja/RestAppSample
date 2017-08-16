//package details forbidden

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import app.dbtransactions.DBTransactions;
import app.utils.ContactBookBean;
import app.utils.MyLeavesBean;
import app.utils.ServicesBean;
import app.utils.UserDataBean;

import com.sun.jersey.spi.resource.Singleton;

@Produces("application/xml")
@Path("loginvalidation")
@Singleton
public class LoginValidation
{

	public LoginValidation()
	{

	}

	@POST
	@Path("validate")
	public List<UserDataBean> validateUser(@FormParam("user_id") String userid, @FormParam("password") String password)
	{
		System.out.println("VALIDATING USER");

		System.out.println("Server side"+userid+"-"+password);

		DBTransactions db = new DBTransactions();
		List<UserDataBean> user_data = db.validate_Login_Fetch_UserData(userid, password);
		System.out.println(user_data.toString());
		return user_data;

	}

	@POST
	@Path("services")
	public List<ServicesBean> getServices(@FormParam("role_id") String role_id)
	{
		System.out.println("Fetching user services");

		System.out.println("Server side"+role_id);

		DBTransactions db = new DBTransactions();
		List<ServicesBean> services_data = db.myServices(role_id);
		System.out.println(services_data.toString());
		return services_data;
	}

	@GET
	@Path("contactbook")
	public List<ContactBookBean> getContacts(@QueryParam("userid") String userid)
	{
		DBTransactions db = new DBTransactions();
		List<ContactBookBean> studentsList1 = db.getContacts(userid);
		return studentsList1;
	}

	@GET
	@Path("viewleaves")
	public List<MyLeavesBean> getLeavesData(@QueryParam("userid") String userid)
	{
		DBTransactions db = new DBTransactions();
		List<MyLeavesBean> leavesData = db.getLeaves(userid);
		return leavesData;
	}
}
