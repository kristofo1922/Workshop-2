package pl.coderslab;


import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DbUtil.getConnection();
        DbUtil.getConnection().close();
    }
}
