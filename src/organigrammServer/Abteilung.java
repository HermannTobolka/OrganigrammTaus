package organigrammServer;

public class Abteilung {

	private int id;
	private String name;
	private String boss;
	private int headId; 
	
	public Abteilung() { }

	public Abteilung(int id, String name, String head, int headId) {
		super();
		this.id = id;
		this.name = name;
		this.boss = head;
		this.headId = headId;
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

	public String getBoss() {
		return boss;
	}

	public void setBoss(String head) {
		this.boss = head;
	}
	
	public int getHeadId() {
		return headId;
	}

	public void setHeadId(int headId) {
		this.headId = headId;
	}
	
}
