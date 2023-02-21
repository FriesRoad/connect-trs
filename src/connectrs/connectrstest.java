package connectrs;

import com.trs.client.TRSConnection;
import com.trs.client.TRSConstant;
import com.trs.client.TRSException;
import com.trs.client.TRSResultSet;

public class connectrstest {
    public static void main(String[] args) {
        try {
            Class.forName("com.trs.client.TRSConnection");
            System.out.println("成功与TRS服务器建立连接");
        } catch (Exception e) {
            System.out.println("与TRS服务器连接失败");
            e.printStackTrace();
        }

        TRSConnection trscon = null;
        TRSResultSet trsrs = null;
        String sHost = "127.0.0.1";
        String sPort = "8888";
        String sUserName = "system";
        String sPassWord = "manager";

        try {
            // 建立连接
            trscon = new TRSConnection();
            trscon.connect(sHost, sPort, sUserName, sPassWord, "T10");

            if (!trscon.isClosed())
                System.out.println("成功连接到TRS数据库");

            // 从demo3中检索标题或正文含有"中国"的记录
            trsrs = trscon.executeSelect("demo3", "标题=中国 or 正文=中国", "", "", "正文", 0, TRSConstant.TCE_OFFSET, false);

            // 输出记录数
            System.out.println("记录数:" + trsrs.getRecordCount());

            // 设置概览/细览字段, 提高读取记录的效率
            trsrs.setReadOptions("日期;版次;作者;标题", "正文", ";", TRSConstant.TCE_OFFSET, 0);

            // 输出前20条记录
            for (int i = 0; i < 20 && i < trsrs.getRecordCount(); i++)
            {
                trsrs.moveTo(0, i);
                System.out.println("第" + i + "条记录");

                System.out.println(trsrs.getString("日期"));
                System.out.println(trsrs.getString("版次"));
                System.out.println(trsrs.getString("作者"));
                System.out.println(trsrs.getString("标题", "red"));
            }


        } catch (TRSException ex)
        {
            // 输出错误信息
            System.out.println(ex.getErrorCode() + ":" + ex.getErrorString());
            ex.printStackTrace();
        }
        finally
        {
            // 关闭结果集
            if (trsrs != null) trsrs.close();
            trsrs = null;

            // 关闭连接
            if (trscon != null) trscon.close();
            trscon = null;
        }
    }
}
