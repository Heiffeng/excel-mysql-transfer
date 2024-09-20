package site.achun.tools.transfer.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 表和excel字段的映射关系信息
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-18 13:43:30
 */
@Data
public class TableMappingInfo {

    // 表名
    private String tableName;
    // 表注释
    private String tableComment;
    // 表和excel的字段映射
    private List<Mapping> fields;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Mapping {
        // 数据库字段名
        private String field;
        // Excel字段名
        private String header;
        // 数据类型
        private String dataType;
        // 字段备注
        private String comment;
    }
}
