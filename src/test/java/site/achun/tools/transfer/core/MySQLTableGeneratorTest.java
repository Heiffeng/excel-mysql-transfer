package site.achun.tools.transfer.core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-18 13:52:12
 */
public class MySQLTableGeneratorTest {

    @Test
    public void generator(){
        // 创建 TableMappingInfo 对象
        TableMappingInfo tableMappingInfo = new TableMappingInfo();
        tableMappingInfo.setTableName("user");

        // 设置字段映射
        List<TableMappingInfo.Mapping> fields = new ArrayList<>();
        fields.add(new TableMappingInfo.Mapping("id","ID","INT PRIMARY KEY AUTO_INCREMENT"));
        fields.add(new TableMappingInfo.Mapping("name","Name","VARCHAR(255)"));

        tableMappingInfo.setFields(fields);

        // 调用方法生成 SQL
        String createTableSQL = MySQLTableGenerator.generateCreateTableSQL(tableMappingInfo);
        System.out.println(createTableSQL);
    }
}
