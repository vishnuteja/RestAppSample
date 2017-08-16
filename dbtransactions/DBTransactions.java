//package details forbidden

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import app.dbutils.DBCon_EOffice;
import app.utils.ContactBookBean;
import app.utils.MyLeavesBean;
import app.utils.ServicesBean;
import app.utils.UserDataBean;

public class DBTransactions {

	private Connection con=null;
	private Statement stmt = null;
	private Statement stmt1 = null;
	private ResultSet rs = null;
	private ResultSet rs1 = null;
	private String sql = "";

	private TreeMap<Integer, UserDataBean> userData = new TreeMap<Integer, UserDataBean>();

	private TreeMap<Integer, ServicesBean> servicesData = new TreeMap<Integer, ServicesBean>();

	private TreeMap<Integer, MyLeavesBean> myLeavesData = new TreeMap<Integer, MyLeavesBean>();

	private TreeMap<Integer, ContactBookBean> contactsList = new TreeMap<Integer, ContactBookBean>();

	public DBTransactions()
	{

		try
		{
			con = DBCon_EOffice.getConnection();
			System.out.println("DATABASE CONNECTION <=========================================> "+con);
			stmt = con.createStatement();
			stmt1 = con.createStatement();
			System.out.println("Connection from eHostels :: "+con);
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public List<ContactBookBean> getContacts(String ddocode)
	{
		// TODO Auto-generated method stub
		List<ContactBookBean> entireList = new ArrayList<ContactBookBean>();
		ContactBookBean bean = new ContactBookBean();
		try
		{

			sql=	"select " +
					"		em.employee_id, " +
					"		em.employee_code, " +
					"		em.employee_name||' '||em.sur_name as employee_name,  " +
					"		dm.designation_name," +
					"		em.employee_type_id, " +
					"		hetm.employee_type_name," +
					"		coalesce(cinfo.personal_mobile,'') as personal_mobile, " +
					"		coalesce(cinfo.official_mobile,'') as office_mobile," +
					" 		coalesce(cinfo.extension_no,'') as extension_no, " +
					"		coalesce(cinfo.email,'') as email, " +
					"		omst.office_name " +
					"				from " +
					"					hrpr_employee_mst em left " +
					"							join hrpr_employee_post_map epm on (em.employee_id=epm.employee_id) " +
					"							left join hrpr_post_mst pm on (epm.post_id=pm.post_id) " +
					"							left join hrpr_office_mst omst on (pm.office_id=omst.office_id) " +
					"							left join hrpr_location_mst lm on (pm.location_id=lm.location_id and lm.is_active='Y')" +
					" 							left join hrpr_designation_mst dm on (pm.designation_id=dm.designation_id and dm.is_active='Y') " +
					"							left join hrpr_employee_status_mst hesm on (em.employee_status_id=hesm.employee_status_id) " +
					"							left join hrpr_post_type_mst hptm on (pm.post_type_id=hptm.post_type_id) " +
					"							left join hrpr_employee_type_mst hetm on (em.employee_type_id=hetm.employee_type_id) " +
					"							left join hrpr_employee_contact_info cinfo on (em.employee_id=cinfo.employee_id and cinfo.is_latest='Y') " +
					"									where " +
					"										em.is_active='Y' " +
					"										and " +
					"										dm.designation_id not in (53,54,56,55) " +
					"										and " +
					"										omst.office_id not in (29,30) " +
					"												order by " +
					"														dm.designation_order_no, " +
					"														dm.designation_name, " +
					"														em.employee_name";

			//System.out.println("Query for Students List "+sql);
			rs = stmt.executeQuery(sql);

			if(rs!=null && rs.next())
			{
				do
				{
					bean = new ContactBookBean();
					bean.setContactEmployeeID(rs.getString(1));
					bean.setContactName(rs.getString(3));
					bean.setContactDesignation(rs.getString(4));
					bean.setContactPhoneNumber(rs.getString(7));
					bean.setContactMail(rs.getString(10));

					int id = contactsList.size();
					contactsList.put(id, bean);

				}while(rs.next());
			}
			//System.out.println("Students List "+studentList);
			entireList.addAll(contactsList.values());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(stmt!=null)
				{
					stmt.close();
				}

				if(con!=null)
				{
					con.close();
				}
			}

			catch(Exception e)
			{

			}
		}

		return entireList;
	}

	public List<MyLeavesBean> getLeaves(String user_id) {
		// TODO Auto-generated method stub
		List<MyLeavesBean> entireList = new ArrayList<MyLeavesBean>();
		MyLeavesBean bean = new MyLeavesBean();

		try
		{

			sql=
					"SELECT  "+
							"PLT.LEAVE_TRANS_ID, "+
							"TO_CHAR(PLT.APPLY_DATE,'DD/MM/YY') AS APPLY_DATE, "+
							"LTM.leave_shname AS LEAVE_TYPE, "+
							"TO_CHAR(PLT.LEAVE_FROM_DATE,'DD/MM/YY') AS LEAVE_FROM_DATE, "+
							"TO_CHAR(PLT.LEAVE_TO_DATE,'DD/MM/YY') AS LEAVE_TO_DATE, "+
							"PLT.NO_DAYS, "+
							"COALESCE(PLT.COMMENT,'') AS COMMENT1, "+
							"STATUS.STATUS_NAME,  "+
							"COALESCE(EM.EMPLOYEE_NAME || ' ' || COALESCE(EM.SUR_NAME,' '),' ') AS EMPLOYEE_NAME, "+
							"case when from_day = 'F' then 'full day' when from_day='HFN' then 'HalfDay(FN)' when from_day='HAN' then 'HalfDay(AN)' end as from_day, "+
							"case when to_day = 'F' then 'full day' when to_day='HFN' then 'HalfDay(FN)' when to_day='HAN' then 'HalfDay(AN)' end as to_day,  "+
							"COALESCE(PST.POST_NAME,'') AS POST_NAME, "+
							"PLT.STATUS,  "+
							"coalesce(plt.purpose,'') as purpose,  "+
							"case when plt.status=4 then coalesce(plt.cancel_comment,'') else coalesce(plt.comment,'') end as comment ,  "+
							"TO_CHAR(plt.cancel_date,'DD/MM/YYYY') AS cancel_date,  "+
							"coalesce(plt.cncl_appr_post_id,0) as cncl_appr_post_id,  "+
							"case when plt.is_for_cncl is true then 'Y' else 'N' end as is_for_cncl,  "+
							"COALESCE(TO_CHAR(plt.cncl_appr_date,'DD/MM/YY'),'NA') AS cncl_appr_date  , "+
							"COALESCE(contact.personal_mobile,contact.official_mobile) as contactinfo ,  "+
							"appdesig.designation_name as app_designation ,  "+
							"canceldesig.designation_name as cancel_designation , "+
							"plt.status, "+
							"plt.cancel_reason, "+
							"to_char(saction_date,'dd/mm/yy')  as approve_date ,  "+
							"case when plt.leave_from_date > current_date then '1' else   COALESCE((select count(*) from emp_movement where empid=plt.employee_id::text and status=true and date(time) between plt.LEAVE_FROM_DATE and plt.LEAVE_TO_DATE ),0) end as attendance_in_leave , "+
							"COALESCE(( select count(*) from movement_register where empid=plt.employee_id::text and status=1 and mdate between plt.LEAVE_FROM_DATE and plt.LEAVE_TO_DATE ),0) as attendance_in_movement  "+
							""+
							"FROM  "+
							"PAYROLL_EMP_LEAVETRANS PLT   "+
							"inner join hrpr_employee_mst em ON PLT.employee_id=em.employee_id and em.is_active='Y'  "+
							"left join hrpr_employee_contact_info contact ON PLT.employee_id=contact.employee_id and is_latest='Y'  "+
							"INNER JOIN PAYROLL_LEAVE_STATUS_MST STATUS ON(STATUS.STATUS_ID=PLT.STATUS)   "+
							"LEFT JOIN PAYROLL_LEAVE_TYPE_MST LTM ON LTM.LEAVE_ID = PLT.LEAVE_ID  "+
							"inner JOIN HRPR_employee_post_map PSTM ON (PLT.employee_ID=PSTM.employee_id)  "+
							"inner JOIN HRPR_post_mst PST ON (PSTM.POST_ID=PST.POST_id)  "+
							"left join hrpr_employee_post_map appauthority ON plt.approver_id=appauthority.post_id  "+
							"left join hrpr_post_mst apppost ON appauthority.post_id=apppost.post_id  "+
							"left join hrpr_designation_mst appdesig ON apppost.designation_id=appdesig.designation_id  "+
							"left join hrpr_employee_post_map cancelappauthority ON plt.cncl_appr_post_id=cancelappauthority.post_id  "+
							"left join hrpr_post_mst cancelpost ON cancelappauthority.post_id=cancelpost.post_id  "+
							"left join hrpr_designation_mst canceldesig ON cancelpost.designation_id=canceldesig.designation_id  "+
							""+
							"WHERE  "+
							"PLT.EMPLOYEE_ID=1295  "+
							"AND  "+
							"PLT.STATUS=2  "+
							"AND  "+
							"to_char(leave_to_date,'yyyy')='2013'   "+
							""+
							"ORDER BY date(PLT.LEAVE_FROM_DATE) DESC";

			//System.out.println("Query for Students List "+sql);
			rs = stmt.executeQuery(sql);
			if(rs!=null && rs.next())
			{
				do
				{
					bean = new MyLeavesBean();
					bean.setLeave_type(rs.getString("leave_type"));
					bean.setLeave_from_date(rs.getString("leave_from_date"));
					bean.setLeave_to_date(rs.getString("leave_to_date"));
					bean.setNo_days(rs.getString("no_days"));
					bean.setApply_date(rs.getString("apply_date"));
					bean.setPurpose(rs.getString("purpose"));


					int id = myLeavesData.size();
					//System.out.println("Bean from database  "+bean);
					myLeavesData.put(id, bean);

				}while(rs.next());
			}
			//System.out.println("Students List "+studentList);
			entireList.addAll(myLeavesData.values());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		finally
		{
			try
			{
				if(stmt!=null)
				{
					stmt.close();
				}

				if(con!=null)
				{
					con.close();
				}
			}
			catch(Exception e)
			{

			}
		}

		return entireList;
	}

	public List<UserDataBean> validate_Login_Fetch_UserData(String username, String password)
	{
		// TODO Auto-generated method stub
		List<UserDataBean> userDataList = new ArrayList<UserDataBean>();
		UserDataBean bean = new UserDataBean();

		try
		{

			sql = "SELECT  UM.USER_ID,  "+
			        "UM.USER_DESC,  "+
			        "URM.ROLE_ID,  "+
			        "UI.DEPARTMENT_ID,  "+
			        "UI.EMPLOYEE_ID,  "+
			        "UI.POST_ID,  "+
			        "PM.OFFICE_ID,  "+
			        "HOM.LOCATION_ID, "+
			        "HLM.LOCATION_NAME,  "+
			        "HLM.DISTRICT_ID, "+
			        "DM.DEPARTMENT_NAME, "+
			        "HOM.CHILD_DATA_PERMISSION,  "+
			        "UI.OFFICE_IDS,  "+
			        "COALESCE(EM.EMPLOYEE_NAME,COALESCE(UM.USER_DESC,' ')) || ' ' || COALESCE(EM.SUR_NAME,' ') AS EMPLOYEE_NAME,  "+
			        "RM.ROLE_TYPE,  "+
			        "trim(RM.ROLE_FLAG) as ROLE_FLAG,  "+
			        "TRIM(HOM.OFFICE_NAME) as OFFICE_NAME, "+
			        "PM.DESIGNATION_ID   "+
						"FROM USER_MST UM   "+
			                "INNER JOIN USER_ROLE_MAP URM ON (UM.USER_ID=URM.USER_ID)   "+
			                "INNER JOIN ROLE_MST RM ON (URM.ROLE_ID=RM.ROLE_ID) "+
			                "LEFT JOIN USER_INFO UI ON (UM.USER_ID=UI.USER_ID)   "+
			                "LEFT JOIN HRPR_POST_MST PM ON (UI.POST_ID=PM.POST_ID)   "+
			                "LEFT JOIN HRPR_OFFICE_MST HOM ON (PM.OFFICE_ID=HOM.OFFICE_ID)   "+
			                "LEFT JOIN HRPR_LOCATION_MST HLM ON (HOM.LOCATION_ID=HLM.LOCATION_ID)   "+
			                "LEFT JOIN HRPR_DEPARTMENT_MST DM ON (UI.DEPARTMENT_ID=DM.DEPARTMENT_ID)   "+
			                "LEFT JOIN HRPR_EMPLOYEE_MST EM ON (UI.EMPLOYEE_ID=EM.EMPLOYEE_ID)   "+
					""+
			        "WHERE UM.USER_NAME='"+username+"' AND UM.PASSWORD='"+password+"' AND UM.IS_ACTIVE='Y'";

			rs = stmt.executeQuery(sql);

			if(rs!=null && rs.next())
			{
				do
				{
					bean = new UserDataBean();

					bean.setUSER_ID(rs.getString(1));;
					bean.setUSER_DESC(rs.getString(2));
					bean.setROLE_ID(rs.getString(3));
					bean.setDEPARTMENT_ID(rs.getString(4));
					bean.setEMPLOYEE_ID(rs.getString(5));
					bean.setPOST_ID(rs.getString(6));
					bean.setOFFICE_ID(rs.getString(7));
					bean.setLOCATION_ID(rs.getString(8));
					bean.setLOCATION_NAME(rs.getString(9));
					bean.setDISTRICT_ID(rs.getString(10));
					bean.setDEPARTMENT_NAME(rs.getString(11));
					bean.setCHILD_DATA_PERMISSION(rs.getString(12));
					bean.setOFFICE_IDS(rs.getString(13));
					bean.setEMPLOYEE_NAME(rs.getString(14));
					bean.setROLE_TYPE(rs.getString(15));
					bean.setROLE_FLAG(rs.getString(16));
					bean.setOFFICE_NAME(rs.getString(17)) ;
					bean.setDESIGNATION_ID(rs.getString(18));

					int id = userData.size();
					userData.put(id, bean);

				}while(rs.next());
			}
			//System.out.println("Students List "+studentList);
			userDataList.addAll(userData.values());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(stmt!=null)
				{
					stmt.close();
				}

				if(con!=null)
				{
					con.close();
				}
			}

			catch(Exception e)
			{

			}
		}

		return userDataList;
	}

	public List<ServicesBean> myServices(String role_id)
	{
		// TODO Auto-generated method stub
		List<ServicesBean> myServicesList = new ArrayList<ServicesBean>();
		ServicesBean bean = new ServicesBean();

		try
		{

			sql = 	"SELECT  	MM.ID, "+
					"        	MM.MODULE_NAME, "+
					"	        MM.SHORT_NAME "+
					"	            FROM MODULES_MST MM "+
					"	             	INNER JOIN ROLE_MODULE_MAP RMM ON (MM.ID=RMM.MODULE_ID AND RMM.ROLE_ID='"+role_id+"') "+
					"	                	ORDER BY display_order";

			rs = stmt.executeQuery(sql);

			if(rs!=null && rs.next())
			{
				do
				{
					bean = new ServicesBean();

					bean.setService_id(rs.getInt(1));
					bean.setService_name(rs.getString(2));
					bean.setService_short_name(rs.getString(3));

					int id = servicesData.size();
					servicesData.put(id, bean);

				}while(rs.next());
			}
			//System.out.println("Students List "+studentList);
			myServicesList.addAll(servicesData.values());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(stmt!=null)
				{
					stmt.close();
				}

				if(con!=null)
				{
					con.close();
				}
			}

			catch(Exception e)
			{

			}
		}

		return myServicesList;
	}
}
