package site.achun.tools.transfer.core;

import java.util.List;

/**
 * 生成建表语句
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-18 13:52:12
 */
public class MySQLTableGenerator {

    // 定义建表语句的模板
    private static final String CREATE_TABLE_TEMPLATE = "CREATE TABLE %s (\n%s);";

    // 定义字段的模板
    private static final String FIELD_TEMPLATE = "    %s %s COMMENT '%s'";

    /**
     * 根据 TableMappingInfo 生成 MySQL 建表语句
     * @param tableMappingInfo 表和字段的映射信息
     * @return MySQL 建表语句
     */
    public static String generateCreateTableSQL(TableMappingInfo tableMappingInfo) {
        StringBuilder fieldsSQL = new StringBuilder();

        // 遍历字段并构建字段部分的 SQL
        List<TableMappingInfo.Mapping> fields = tableMappingInfo.getFields();
        String primaryKey = null;
        for (TableMappingInfo.Mapping field : fields) {
            // 使用字段模板构建每个字段的 SQL 语句，包括字段名、数据类型和备注
            fieldsSQL.append(String.format(FIELD_TEMPLATE,
                    field.getField(),
                    field.getDataType(),
                    field.getHeader()));
            fieldsSQL.append(",\n");
            if (field.getComment() != null && field.getComment().equals("Primary Key")) {
                primaryKey = field.getField();
            }
        }
        if(primaryKey == null){
            throw new RuntimeException("Primary Key is null");
        }
        fieldsSQL.append("PRIMARY KEY (`").append(primaryKey).append("`) \n");
        // 使用建表模板构建最终的 SQL 语句
        return String.format(CREATE_TABLE_TEMPLATE, tableMappingInfo.getTableName(), fieldsSQL.toString());
    }
}
