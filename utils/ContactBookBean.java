//package details forbidden

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactBookBean {

	public String contactEmployeeID;
	public String contactName;
	public String contactDesignation;
	public String contactPhoneNumber;
	public String contactMail;
	//public byte[] contactPhoto;

	/*public byte[] getContactPhoto() {
		return contactPhoto;
	}
	public void setContactPhoto(byte[] contactPhoto) {
		this.contactPhoto = contactPhoto;
	}*/
	public String getContactEmployeeID() {
		return contactEmployeeID;
	}
	public void setContactEmployeeID(String contactEmployeeID) {
		this.contactEmployeeID = contactEmployeeID;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactDesignation() {
		return contactDesignation;
	}
	public void setContactDesignation(String contactDesignation) {
		this.contactDesignation = contactDesignation;
	}
	public String getContactPhoneNumber() {
		return contactPhoneNumber;
	}
	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}
	public String getContactMail() {
		return contactMail;
	}
	public void setContactMail(String contactMail) {
		this.contactMail = contactMail;
	}

}
