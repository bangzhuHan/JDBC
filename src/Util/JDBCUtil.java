package Util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @Description:操作数据库的工具类
 * @author xh
 * @date 2022/4/19
 * @apiNote
 */
public class JDBCUtil {
    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws Exception {
        //1.读取配置文件中的四个基本信息
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

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
        //System.out.println(connection);
        return connection;
    }

    /**
     * 关闭连接和statement
     */
    public static void closeResource(Connection connection, Statement s) {
        try {
            if (s != null)
                s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResource(Connection connection, Statement s, ResultSet resultSet){
        try {
            if (s != null)
                s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(resultSet != null)
                resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
