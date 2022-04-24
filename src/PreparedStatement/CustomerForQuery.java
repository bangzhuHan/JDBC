package PreparedStatement;

import Bean.Customer;
import Util.JDBCUtil;
import org.testng.annotations.Test;

import javax.swing.*;
import java.lang.reflect.Field;
import java.sql.*;

/**
 * @Description:针对Customers表的查询操作
 * @author xh
 * @date 2022/4/19
 * @apiNote
 */
public class CustomerForQuery {

    @Test
    public void testQueryForCustomers(){
        String sql = "select id, name, birth, email from customers where id = ?";
        Customer customer = queryForCustomers(sql,12);
        System.out.println(customer);
    }


    /**针对customers表的通用查询操作
     */
    @Test
    public Customer queryForCustomers(String sql, Object ...args){//args数组用于填充占位符,使用...将参数声明为可变长参数(args必须为最后一个参数),一个方法的参数中不能同时拥有两种类型的可变参数
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtil.getConnection();
            ps = connection.prepareStatement(sql);
            for(int i = 0; i < args.length; i++){
                ps.setObject(i + 1, args[i]);
            }
            //执行结果集查询
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            if(rs.next()){
                Customer customer = new Customer();
                //处理结果集中的每一列
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object value = rs.getObject(i +1);

                    //获取每个列的列名(别名)
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给customer对象指定的columnName属性，赋值为columValue：通过反射
                    Field field = customer.getClass().getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(customer,value);
                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(connection,ps,rs);
        }

        return null;
    }


    /**
     * @Descrition：查找某一条数据
     */
    @Test
    public void testQuery1(){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtil.getConnection();
            String sql = "select id, name, email, birth from customers where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1,1);

            //执行,返回结果集
            resultSet = ps.executeQuery();
            if(resultSet.next()){//判断结果集下一条是否有数据,如果有数据返回true,并且指针下移;否则返回false,并且不会下移
                //获取当前这一结果的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                //方式一：
    //         System.out.println("id = " + id + ",name = " + name + ",email = " + email + ",birth = " + birth);
                //方式二:
//                Object[] data = new Object[]{id, name, email, birth};
//                for (int i = 0; i < data.length; i++) {
//                    System.out.print(data[i] + " ");
//                }

                //方式三:将数据封装为一个对象
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtil.closeResource(connection, ps, resultSet);
        }

    }
}
