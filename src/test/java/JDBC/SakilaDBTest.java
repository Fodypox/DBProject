package JDBC;

import Utilities.DBUtilities;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SakilaDBTest extends DBUtilities {
    @Test
    public void tablePresence() throws SQLException {
        String tableName = "actor";
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);
        Assert.assertTrue(resultSet.next());
    }
    @Test
    public void tableConvention() throws SQLException {
        String tableName = "actor";
        String[] types = {"TABLE"};
        ResultSet resultSet = metaData.getTables(null, null, tableName, types);
        String actualTableName = "";
        while (resultSet.next()) {
            actualTableName = resultSet.getString("TABLE_NAME");
        }
        Assert.assertFalse(actualTableName.contains(" "));
    }

    @Test
    public void numberOfColumns() throws SQLException {
        ResultSet resultSet = statement.executeQuery("select count(*) as Number_Of_Columns from information_schema.columns where table_name='actor'");
        resultSet.next();
        int cityName = resultSet.getInt("Number_Of_Columns");
        Assert.assertEquals(cityName,4);
    }

    @Test
    public void namesOfColumns() throws SQLException {
        List<String> expectedColumnNames = Arrays.asList("actor_id", "first_name", "last_name", "last_update");
        ResultSet resultSet = statement.executeQuery("select column_name from information_schema.columns where table_name='actor'");
        int columnCount = resultSet.getMetaData().getColumnCount();
        List<String> actualColumnNames = new ArrayList<>();
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                actualColumnNames.add(resultSet.getString(i));
            }
        }
        Assert.assertTrue(actualColumnNames.containsAll(expectedColumnNames));

    }

    @Test
    public void dataTypeOfColumns() throws SQLException {
        List<String> expectedColumnNames = Arrays.asList("smallint", "varchar", "varchar", "timestamp");
        List<List<String>> actualColumnNames = getData("select column_name, data_type from information_schema.columns where table_name='actor'");
        for (int i = 0; i < expectedColumnNames.size(); i++) {
            Assert.assertEquals(actualColumnNames.get(i).get(1),expectedColumnNames.get(i));
        }

    }

    @Test
    public void sizeOfColumns() throws SQLException {
        List<String> expectedColumnNames = Arrays.asList("smallint unsigned", "varchar(45)", "varchar(45)", "timestamp");
        List<List<String>> actualColumnNames = getData("select column_name, column_type from information_schema.columns where table_name='actor'");
        System.out.println(actualColumnNames);
        for (int i = 0; i < expectedColumnNames.size(); i++) {
            Assert.assertEquals(actualColumnNames.get(i).get(1),expectedColumnNames.get(i));
        }

    }

    @Test
    public void nullsFieldsOfTable() throws SQLException {
        List<List<String>> actualColumnNames = getData("select column_name, is_nullable from information_schema.columns where table_name='actor'");
        System.out.println(actualColumnNames);
        for (int i = 0; i < actualColumnNames.get(0).size(); i++) {
            Assert.assertEquals(actualColumnNames.get(i).get(1),"NO");
        }

    }

    @Test
    public void columnKeys() throws SQLException {
        List<List<String>> actualColumnNames = getData("select column_name, column_key from information_schema.columns where table_name='actor'");
        System.out.println(actualColumnNames);

        Assert.assertEquals(actualColumnNames.get(0).get(1),"PRI");

    }
}
