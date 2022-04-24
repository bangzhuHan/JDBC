package HW;

import Bean.Student;
import Util.JDBCUtil;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.events.Event;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @author xh
 * @date 2022/4/21
 * @apiNote
 */
public class Exer2 {
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

    //针对于不同的表的通用的查询操作，返回表中的一条记录
    public static <T>T getInstance(Class<T> clazz, String sql, Object... args){
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

   public static void main(String[] args) {
       //问题1
//        String sql = "insert into examstudent (Type,IDCard,ExamCard,StudentName,Location,Grade) values(?,?,?,?,?,?)";
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("四级/六级:");
//        int type = scanner.nextInt();
//        System.out.print("身份证号:");
//        String IDCard = scanner.next();
//        System.out.print("准考证号:");
//        String ExamCard = scanner.next();
//        System.out.print("学生姓名:");
//        String name = scanner.next();
//        System.out.println("家庭住址:");
//        String location = scanner.next();
//        System.out.println("成绩:");
//        int Grade = scanner.nextInt();
//
//        int a = update(sql,type,IDCard,ExamCard,name,location,Grade);
//        if(a > 0)
//            System.out.println("添加成功!");

       //问题2
//       System.out.println("请选择您要输入的类型:");
//       System.out.println("a.准考证号");
//       System.out.println("b.身份证号");
//       Scanner scanner = new Scanner(System.in);
//       String letter = scanner.next();
//       if ("a".equalsIgnoreCase(letter)) {
//           System.out.println("请输入准考证号:");
//            String sql = "select flowID,type,IDCard,examCard,studentName,location,grade from examstudent where examCard = ?";
//            Scanner scanner1 = new Scanner(System.in);
//            String examCard = scanner.next();
//            Student student = getInstance(Student.class, sql, examCard);
//            if(student != null)
//                System.out.println(student);
//            else System.out.println("准考证号有误！");
//       } else if ("b".equalsIgnoreCase(letter)) {
//           System.out.println("请输入身份证号:");
//           String sql = "select flowID,type,IDCard,examCard,studentName,location,grade from examstudent where IDCard = ?";
//           Scanner scanner1 = new Scanner(System.in);
//           String IDCard = scanner1.next();
//           Student student = getInstance(Student.class, sql, IDCard);
//           if(student != null)
//               System.out.println(student);
//           else System.out.println("身份证号有误！");
//       } else {
//           System.out.println("您的输入有误!请重新进入程序.");
//       }

       //问题三:删除指定学生信息
       System.out.println("请输入准考证号:");
       Scanner scanner = new Scanner(System.in);
       String examCard = scanner.next();
       String sql = "delete from examstudent where examCard = ?";
       int a = update(sql,examCard);
       if(a > 0)
           System.out.println("删除成功!");
       else System.out.println("查无此人,重新输入!");
   }
}
