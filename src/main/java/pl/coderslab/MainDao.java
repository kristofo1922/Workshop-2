package pl.coderslab;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

import java.sql.SQLException;

public class MainDao {
    public static void main(String[] args) throws SQLException {
        //nowa klasa user
        User user = new User();
        user.setUserName("daria");
        user.setEmail("daria.lubakow@gmail.com");
        user.setPassword("daria92");
        //nowa klasa userDao
        UserDao userDao = new UserDao();
        userDao.create(user);

        //odczyt istniejacego
        User userExist = userDao.read(1);
        System.out.println(userExist);
        //odczyt null
        User userNotExist = userDao.read(5);
        System.out.println(userNotExist);

        //update
        User updateUser = userDao.read(2);
        updateUser.setUserName("Paweł");
        updateUser.setEmail("pablo123321123@vp.pl");
        updateUser.setPassword("Paweł666999");
        userDao.update(updateUser);

        //delete
        userDao.delete(1);

        //wszyscy użytkownicy
        User nextUser = new User();
        nextUser.setUserName("paweł");
        nextUser.setEmail("pavel123@wp.pl");
        nextUser.setPassword("paweł908074");
        userDao.create(nextUser);
        User[] allUser = userDao.allUsers();
        for (User us : allUser) {
            System.out.println(us);
        }

    }


}
