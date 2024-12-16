package Authentication;


public class Drivers {

    private String email;
    private String cPassword;
    private String pswd;

    private String driverusername;
    private String driverphonenumber;


    public Drivers(String email, String cPassword, String pswd, String driverusername, String driverphonenumber) {
        this.email = email;
        this.cPassword = cPassword;
        this.pswd = pswd;
        this.driverusername = driverusername;
        this.driverphonenumber = driverphonenumber;


    }

    public String getDriverphonenumber() {
        return driverphonenumber;
    }

    public void setDriverphonenumber(String driverphonenumber) {
        this.driverphonenumber = driverphonenumber;
    }

    public String getDriverusername() {
        return driverusername;
    }

    public void setDriverusername(String driverusername) {
        this.driverusername = driverusername;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

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
}





