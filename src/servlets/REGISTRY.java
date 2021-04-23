package servlets;

final class REGISTRY {
    static enum RequestCode {
        ID("login_id"),PWD("login_pwd"),EMAIL("email");
        
        final String s;
        private RequestCode(final String s) {this.s = s;}
    }
    static final String ERR_ATTR = "error_message";
    static final String ERR_NAME = "Please enter username and password.";
    static final String ERR_DUPE = "An account with these credentials already exist.";
    static final String SUCCESS = "Success!";
}