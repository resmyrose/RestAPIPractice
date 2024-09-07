package utilities;

import java.util.ArrayList;

 
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.sl.usermodel.Sheet;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
 
 
public class ExcelReader {
	public static List<Map<String, String>> getExcelDataWithFilloAPI(String FilePath, String querry) {
		Map<String, String> testData = null;
		List<Map<String, String>> allRowsOfTestData = null;

		Fillo fillo = new Fillo();
		Connection con = null;
		Recordset recordset = null;
		
		try {
			con = fillo.getConnection(FilePath);
			recordset = con.executeQuery(querry);
			 
		    
			allRowsOfTestData = new ArrayList<Map<String, String>>();
			while (recordset.next()) {
				testData = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
				for (String field : recordset.getFieldNames()) {
					testData.put(field, recordset.getField(field));
				}
				allRowsOfTestData.add(testData);
			}
			 
		} catch (FilloException e) {
			System.out.println("catch block from excelreader class");
			e.printStackTrace();
		}
		return allRowsOfTestData;
	}

	public List<Integer> getExpectedStatusCodeCloumnFromExcel(String querry, String excelFilePath, String columnName) {
		List<Integer> expectedStatusCodes = new ArrayList<Integer>();
		Fillo fillo = new Fillo();
		Connection con = null;
		Recordset recordset = null;
		try {
			con = fillo.getConnection(excelFilePath);
			recordset = con.executeQuery(querry);
			while (recordset.next()) {
				String expectedCode = recordset.getField(columnName);
				int data = Integer.parseInt(expectedCode);
				expectedStatusCodes.add(data);
			}
		} catch (FilloException e) {

			e.printStackTrace();
		}
		return expectedStatusCodes;
	}


}
