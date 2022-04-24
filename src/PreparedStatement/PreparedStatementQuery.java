package PreparedStatement;

import Bean.Customer;
import Bean.Order;
import Util.JDBCUtil;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:实现通用的查询操作
 * @author xh
 * @date 2022/4/20
 * @apiNote
 */
public class PreparedStatementQuery {
    @Test
    public void testGetForList(){
        String sql = "select id,name,email, birth from customers where id < ?";
        List<Customer> customerList = getForList(Customer.class,sql,13);
        customerList.forEach(System.out::println);
        //System.out.println(customerList);
        System.out.println("****************************************");
        String sql1 = "select order_id orderId, order_name orderName,  order_date orderDate from `order`";
        List<Order> orderList = getForList(Order.class, sql1);
        orderList.forEach(System.out::println);
    }


    public <T> List<T> getForList(Class<T> clazz, String sql, Object...args){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
                connection = JDBCUtil.getConnection();
                ps = connection.prepareStatement(sql);
                for (int i = 0; i < args.length; i++) {
                    ps.setObject(i + 1, args[i]);
                }

                rs = ps.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                //创建集合对象
                ArrayList<T> list = new ArrayList<>();
                while(rs.next()){
                    T t = clazz.newInstance();
                    // 处理结果集一行数据中的每一个列:给t对象指定的属性赋值
                    for(int i = 0; i < columnCount; i++){
                        // 获取列值
                        Object columnValue = rs.getObject(i + 1);

                        // 获取每个列的列名
                        String columnLabel = rsmd.getColumnLabel(i + 1);

                        //给t对象的指定的columnName属性,赋值为columnValue
                        Field field = clazz.getDeclaredField(columnLabel);
                        field.setAccessible(true);
                        field.set(t,columnValue);
                    }
                    list.add(t);
                }
                return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(connection,ps,rs);
        }


        return null;
    }


   @Test
    public void testGetInstance(){
        String sql = "select id,name,email from customers where id = ?";
        Customer customer = getInstance(Customer.class,sql,12);
        System.out.println(customer);

        String sql1 = "select order_id orderId,order_name orderName from `order` where order_id = ?";
        Order order = getInstance(Order.class, sql1, 1);
        System.out.println(order);
    }

    /**
     *
     * @Description 针对于不同的表的通用的查询操作，返回表中的一条记录
     * @author xh
     * @date 上午11:42:23
     * @param clazz
     * @param sql
     * @param args
     * @return
     */
    public <T>T getInstance(Class<T> clazz, String sql, Object...args){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtil.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //执行,获取结果集
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount =  rsmd.getColumnCount();
            if(rs.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值
                    Object columnValue = rs.getObject(i + 1);
                    //获取列名,通过rsmd
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射,将对象指定名columnName的属性赋值为指定的值columnValue
                    Field columnField = clazz.getDeclaredField(columnLabel);
                    columnField.setAccessible(true);
                    columnField.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(connection,ps,rs);
        }
        return null;
    }



}
