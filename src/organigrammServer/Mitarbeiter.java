package organigrammServer;

public class Mitarbeiter {

	private int id;
	private String name;
	private String eMail;
	private String persNr;
	private int deptId;
	
	public Mitarbeiter() { }

	public Mitarbeiter(int id, String name, String eMail, String persNr, int deptId) {
		super();
		this.id = id;
		this.name = name;
		this.eMail = eMail;
		this.persNr = persNr;
		this.deptId = deptId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getPersNr() {
		return persNr;
	}

	public void setPersNr(String persNr) {
		this.persNr = persNr;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	
}
