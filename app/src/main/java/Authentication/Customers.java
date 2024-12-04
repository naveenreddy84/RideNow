package Authentication;

public class Customers {

private String email;
    private String cPassword;

     private String pswd;

     private  String uname;


    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUname() {
        return uname;
    }

    public Customers(String email, String cPassword, String pswd, String uname) {
        this.email = email;
        this.cPassword = cPassword;
        this.pswd = pswd;
        this.uname = uname;



    }

}
