package organigrammClient;

import organigrammClient.OrganigrammServer.*;
//organigrammClient.organigrammServer.Abteilung;


public class ClientAbteilung extends Abteilung {

	public ClientAbteilung(int id, String name, String head, int headId) {
		super();
		this.id = id;
		this.name = name;
		this.boss = head;
		this.headId = headId;
	}
	
	public ClientAbteilung() { }

	@Override
	public String toString() {
		return name + " (" + boss + ")";
	}	

}
