package data;

public class LoginInfo {
	private String user;
	private String pass;
	public LoginInfo(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}
	public String getUser() {
		return user;
	}
	public String getPass() {
		return pass;
	}
	

    @Override
    public boolean equals(Object o) {
  
        // If the object is compared with itself then return true  
        if (o == this) {
            return true;
        }
  
        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof LoginInfo)) {
            return false;
        }
          
        // typecast o to LoginInfo so that we can compare data members 
        LoginInfo c = (LoginInfo) o;
          
        // Compare the data members and return accordingly 
        return user.equals(c.user) && pass.equals(c.pass);
    }
    
    public static void main(String args[]) {
    	LoginInfo a = new LoginInfo("a", "b");
    	LoginInfo b = new LoginInfo("a", "b");
    	System.out.println(a.equals(b));
    }
    
    
}
