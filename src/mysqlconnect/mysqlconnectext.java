package mysqlconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class mysqlconnectext {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("成功加载驱动");
        }
        catch (Exception e){
            System.out.println("加载驱动失败");
            e.printStackTrace();
        }
        Connection con;
        String url = "jdbc:mysql://localhost:3306/datatest?useSSL=false";
        String user = "root";
        String password = "123456";
        try {
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed())
                System.out.println("成功连接数据库");

            Statement statement = con.createStatement();
            String sql = "select * from student";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                System.out.println(rs.getString("no"));
                System.out.println("");
                System.out.println(rs.getString("name"));
                System.out.println("");
                System.out.println(rs.getString("age"));
                System.out.println("");
            }
        }
        catch (Exception e){
            System.out.println("获取信息错误");
            e.printStackTrace();
        }
    }
}
