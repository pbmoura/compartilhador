package business;

public class UserConfig {

//	private String name;
//	private String nameServer;
	private String repository;
	
	
	public UserConfig() {
	}

	public UserConfig(/*String name, String nameServer,*/ String repository) {
//		this.name = name;
//		this.nameServer = nameServer;
		this.repository = repository;
	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public String getNameServer() {
//		return nameServer;
//	}
//	public void setNameServer(String nameServer) {
//		this.nameServer = nameServer;
//	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	
	
}
