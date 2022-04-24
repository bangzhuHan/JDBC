package Connection;

import org.testng.annotations.Test;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author xh
 * @date 2022/4/18
 * @apiNote
 */

/**
 * jdbc编程6步
 * 1.注册驱动
 * (通知Java程序即将要连接的是哪个品牌的数据库)
 *
 * 2.获取数据库连接
 * (java进程和mysql进程的通道开启)
 *
 * 3.获取数据库操作对象
 *
 *
 * 4.执行操作SQL语句
 * (执行crud操作)
 *
 * 5.处理查询结果集
 * 只针对select语句
 *
 * 6.释放资源
 * (jdbc是进程间的通信,会占用资源，需要关闭)
 */
public class ConnectionTest {
    //方式一
    @Test
    public void testConnection1() throws SQLException {
        //获取Driver实现类对象
        Driver driver = new com.mysql.cj.jdbc.Driver();

        //url:http://localhost:8080/gmall/keyboard.jpg
        //jdbc:mysql:协议
        //localhost:ip地址
        //3306:端口号
        //test:数据库名
        String url = "jdbc:mysql://localhost:3306/test";

        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","123456");

        Connection connection = driver.connect(url,info);

        System.out.println(connection);
    }

    //方式二,对方式一的迭代,在程序中不出现第三方的API,使程序有更好的可移植性
    @Test
    public void testConnection2() throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //1.获取Driver实现类对象,使用反射
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();

        //2.提供URL和用户名密码
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8";
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","123456");

        Connection connection = driver.connect(url,info);

        System.out.println(connection);
    }

   //方式三:使用DriverManager替换Driver
   @Test
    public void testConnection() throws Exception{
        //1.获取Driver实现类对象
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();

        //提供URL和用户名密码
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123456";

        //注册驱动
        DriverManager.registerDriver(driver);

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
       System.out.println(connection);
    }

    //方式四:可省略注册驱动步骤，在mysql的driver实现类中，有静态代码块实现注册驱动
    @Test
    public void testConnection4() throws Exception{

        //1.提供URL和用户名密码
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123456";

        //2.获取Driver实现类对象
        Class.forName("com.mysql.cj.jdbc.Driver");
        //Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();



//        //注册驱动
//        DriverManager.registerDriver(driver);

        //3.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    //方式五:最终版,增强可移植性，实现了数据和代码的分离
    @Test
    public void testConnection5() throws Exception{
        //1.读取配置文件中的四个基本信息
        InputStream inputStream = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties properties = new Properties();
        properties.load(inputStream);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");

        //2.加载驱动
        Class.forName(driverClass);

        //3.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);




    }
}
