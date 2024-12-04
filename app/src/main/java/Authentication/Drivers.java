package Authentication;

import android.net.Uri;


public class Drivers {

        private String email;
        private String cPassword;
        private String pswd;

        private String driverusername;



        public Drivers(String email, String cPassword, String pswd, String driverusername) {
            this.email = email;
            this.cPassword = cPassword;
            this.pswd = pswd;
            this.driverusername = driverusername;

        }


    public void setDriverusername(String driverusername) {
        this.driverusername = driverusername;
    }

    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDriverusername() {
        return driverusername;
    }
}


