package servlets;

enum RequestCode {
    ID("login_id"),PWD("login_pwd"),EMAIL("email");
    
    final String s;
    private RequestCode(final String s) {this.s = s;}
}