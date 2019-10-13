package Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserDAO {
    public static boolean userLoginAndPasswordValidation(String login){

        Pattern pattern = Pattern.compile("(?m)^\\w{3,10}$");
        Matcher matcher = pattern.matcher(login);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
}
