public class EmployeeValidator {
    public boolean isValidPps(String pps) {
        if (pps.length() == 8 || pps.length() == 9) {
            if (Character.isDigit(pps.charAt(0)) 
                && Character.isDigit(pps.charAt(1)) 
                && Character.isDigit(pps.charAt(2)) 
                && Character.isDigit(pps.charAt(3)) 
                && Character.isDigit(pps.charAt(4)) 
                && Character.isDigit(pps.charAt(5)) 
                && Character.isDigit(pps.charAt(6)) 
                && Character.isLetter(pps.charAt(7))) {
                return true;
            }
        }
        return false;
    }
}
