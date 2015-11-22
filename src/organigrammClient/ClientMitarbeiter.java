package organigrammClient;

import  organigrammClient.OrganigrammServer.*;// organigrammClient.organigrammServer.Mitarbeiter;


public class ClientMitarbeiter extends Mitarbeiter {

	public ClientMitarbeiter() { }

	public ClientMitarbeiter(int id, String name, String eMail, String persNr) {
		super();
		this.id = id;
		this.name = name;
		this.eMail = eMail;
		this.persNr = persNr;
	}

	@Override
	public String toString() {
		return name + " (" + persNr + ")";
	}	

}
