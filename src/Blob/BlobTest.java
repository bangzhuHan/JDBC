package Blob;

import Bean.Customer;
import Util.JDBCUtil;
import org.testng.annotations.Test;

import java.io.*;
import java.sql.*;
import java.util.Collection;

/**
 * @author xh
 * @date 2022/4/21
 * @apiNote
 */
public class BlobTest {
    @Test
    public void testInsert(){
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtil.getConnection();
            String sql = "insert into customers(name,email,birth,photo) value(?,?,?,?)";
            ps = connection.prepareStatement(sql);

            ps.setObject(1,"wang5");
            ps.setObject(2,"wang@qq.com");
            ps.setObject(3,"1998-09-28");
            FileInputStream fs = new FileInputStream(new File("girl.jpg"));
            ps.setObject(4,fs);

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(connection,ps);
        }
    }

    @Test
    //查询数据表customers中Blob类型的字段
    public void testQuery(){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            connection = JDBCUtil.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setObject(1,23);

            rs = ps.executeQuery();
            if(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                //将Blob类型的字段下载下来，以文件的方式保存在本地
                Customer customer = new Customer(id,name,email,birth);
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("zhang3photo.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while((len = is.read(buffer)) != -1){
                    fos.write(buffer,0,len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtil.closeResource(connection,ps,rs);
        }


    }

}
