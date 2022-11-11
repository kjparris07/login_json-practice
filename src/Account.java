import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Account {
    private Name name;
    private String email, password;

    public Account(Name name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean equals(Account acc) {
        if ((this.getName() == acc.getName()) && (email == acc.getEmail())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Account[name=" + name + ", email=" + email + ", password=" + password + "]";
    }

}
