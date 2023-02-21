package connectrs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Enumeration;

import com.trs.infra.util.database.DBType;
import com.trs.infra.util.database.DBTypes;
import com.trs.infra.util.database.FieldInfo;
import com.trs.infra.util.database.TableInfo;
import com.trs.infra.util.database.TableInfos;


class	CompareDB{

public static TableInfos loadTableInfos(DBType _oDbType, String url, String user, String password,			
			String _sDBOwner) {
		Connection oConn = null;
		TableInfos tbinfos = new TableInfos();
		try {
			Class.forName(_oDbType.getDriverClass());
			oConn = DriverManager.getConnection(url, user, password);
			tbinfos.load(oConn, _oDbType, _sDBOwner);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oConn != null) {
				try {
					oConn.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return tbinfos;
	}

	public static void main(String[] args) {

		TableInfos oSrcTableInfos = loadTableInfos(DBTypes.getDBType(DBTypes.MYSQL),
				"jdbc:mysql://192.9.200.9/poll_mysql", "root", "trsadmin", "wcm");

		TableInfos oDstTableInfos = loadTableInfos(DBTypes.getDBType(DBTypes.KingBase), "jdbc:kkk:..........", "root",
				"trsadmin", "wcm");


		TableInfo oSrcTableInfo = null, oDstTableInfo = null;
		FieldInfo oSrcFieldInfo = null, oDstFieldInfo = null;
		Enumeration enumFields = null;
		Enumeration enumTables = oSrcTableInfos.getTableNames();
		while (enumTables.hasMoreElements()) {
			String tableName = enumTables.nextElement().toString();
			System.out.println("Start compare the table " + tableName + "  .......");

			oSrcTableInfo = oSrcTableInfos.getTableInfo(tableName);
			oDstTableInfo = oDstTableInfos.getTableInfo(tableName);
			if (oDstTableInfo == null) {
				System.err.println("The table[" + tableName + "] not exists!");
				continue;
			}

			enumFields = oSrcTableInfo.getFieldNames();
			while (enumFields.hasMoreElements()) {
				String sFieldName = enumFields.nextElement().toString();
				oSrcFieldInfo = oSrcTableInfo.getFieldInfo(sFieldName);
				oDstFieldInfo = oDstTableInfo.getFieldInfo(sFieldName);
				if (oDstFieldInfo == null) {
					System.err.println("The Field[" + tableName + "." + sFieldName + "] not exists!");
					continue;
				}

				if (!oDstFieldInfo.getDataType().equals(oSrcFieldInfo.getDataType())) {
					System.err.println("The Field[" + tableName + "." + sFieldName + "] type["
							+ oDstFieldInfo.getDataType().getName() + "] not same!");
					continue;
				}
			}
		}

	}
}