package HW;

import Util.JDBCUtil;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * @author xh
 * @date 2022/4/20
 * @apiNote
 */
public class Exer1 {
    //通用的增删改操作
    public static int update(String sql, Object... args){
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = JDBCUtil.getConnection();

            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1,args[i]);
            }

           return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(connection,ps);
        }
        return 0;
    }

    //@Test
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名:");
        String name = scanner.next();
        System.out.print("请输入邮箱:");
        String email = scanner.next();
        System.out.print("请输入生日:");
        String birth = scanner.next();

        String sql = "insert into customers(name, email,birth) values(?,?,?)";
        int a = update(sql,name,email,birth);
        if(a > 0)
            System.out.println("添加成功!");

    }


}
