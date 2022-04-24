package PreparedStatement;

import Bean.Order;
import Util.JDBCUtil;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @Description 针对于Order表的通用的查询操作
 * @author xh
 * @date 2022/4/19
 * @apiNote
 */

public class OrderForQuery {
    /*
     * 针对于表的字段名与类的属性名不相同的情况：
     * 1. 必须声明sql时，使用类的属性名来命名字段的别名
     * 2. 使用ResultSetMetaData时，需要使用getColumnLabel()来替换getColumnName(),
     *    获取列的别名。
     *  说明：如果sql中没有给字段其别名，getColumnLabel()获取的就是列名
     *
     *
     */
    public Order orderForQuery(String sql, Object...args){
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
               Order order = new Order();
               for (int i = 0; i < columnCount; i++) {
                   //获取每个列的列值
                   Object columnValue = rs.getObject(i + 1);
                   //获取列名,通过rsmd
                   String columnLabel = rsmd.getColumnLabel(i + 1);

                   //通过反射,将对象指定名columnName的属性赋值为指定的值columnValue
                   Field columnField = Order.class.getDeclaredField(columnLabel);
                   columnField.setAccessible(true);
                   columnField.set(order,columnValue);
               }
               return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(connection,ps,rs);
        }

        return null;
    }


    @Test
    public void testOrderForQuery(){
        String sql = "select order_name orderName, order_date orderDate from `order` where order_id = ?";
        System.out.println(orderForQuery(sql,4));
    }































}
