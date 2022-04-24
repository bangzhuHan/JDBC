package PreparedStatement;

import Util.JDBCUtil;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * @author xh
 * @date 2022/4/19
 * @apiNote
 */
public class PreparedStatementUpdate {


//    //通用的增删改操作
//    public void update(String sql,Object ...args){
//        //1.获取数据库的连接
//        Connection connection = JDBCUtils
//        //2.预编译sql语句，返回PreparedStatement的实例
//
//        //3.填充占位符
//
//        //4.执行
//
//        //5.关闭资源
//
//    }

    // 向customers表中添加一条记录
    @Test
    public void testInsert()  {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
//            //1.读取配置文件信息
//            InputStream inputStream = PreparedStatementUpdate.class.getClassLoader().getResourceAsStream("jdbc.properties");
//
//            Properties properties = new Properties();
//            properties.load(inputStream);
//
//            String user = properties.getProperty("user");
//            String password = properties.getProperty("password");
//            String driverClass = properties.getProperty("driverClass");
//            String url = properties.getProperty("url");
//
//            //2.加载驱动
//            Class.forName(driverClass);
//
//            //3.获取连接
//            connection = DriverManager.getConnection(url, user, password);

            connection = JDBCUtil.getConnection();
            //4.预编译sql语句，返回PreparedStatement的实例
            String sql = "insert into customers(name,email,birth)values(?,?,?)";//?:占位符
            ps = connection.prepareStatement(sql);

            //5.填充占位符
            ps.setString(1,"刘浩存");
            ps.setString(2,"1243543@github.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1001-01-01");
            ps.setDate(3,new Date(date.getTime()));

            //6.执行操作
            ps.execute();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            //7.关闭资源
            JDBCUtil.closeResource(connection,ps);
        }

    }

    //修改customers表中的一条记录
    @Test
    public void testUpdate(){
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库连接
            connection = JDBCUtil.getConnection();

            //2.预编译sql语句，返回PreparedStatement的实例
            String sq = "update customers set birth = ? where id = ?";
            ps = connection.prepareStatement(sq);

            //3.填充占位符
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            java.util.Date date = sdf.parse("2001-02-01");
//            ps.setDate(1,new Date(date.getTime()));
            ps.setObject(1,"2000-03-14");
            ps.setInt(2,19);

            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtil.closeResource(connection,ps);
        }
    }

    //通用的增删改操作
    public void update(String sql, Object ...args){
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtil.getConnection();

            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1,args[i]);
            }

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(connection,ps);
        }

    }

    @Test
    public void testCommonUpdate(){
        String sql = "delete from customers where id = ?";
		update(sql,3);

    }

}
