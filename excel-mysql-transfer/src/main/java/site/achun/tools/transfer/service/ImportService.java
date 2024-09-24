package site.achun.tools.transfer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.achun.tools.transfer.core.TableMappingInfo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Import data to mysql service
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-23 16:32:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImportService {

    private final DataSource dataSource;
    private static final int BATCH_SIZE = 1000; // 每次批量插入1000条

    /**
     * 使用JDBC导入数据到数据库
     * @param mappingInfo 字段映射关系
     * @param dataList 数据列表
     */
    @Transactional
    public void importData(TableMappingInfo mappingInfo, List<Map<String,String>> dataList){
        // TODO 实现代码，需要事务，要么全部导入成功，要么全部失败
        // 检查数据
        if (dataList == null || dataList.isEmpty()) {
            log.warn("数据列表为空，跳过导入");
            return;
        }

        // 获取表名
        String tableName = mappingInfo.getTableName();
        List<TableMappingInfo.Mapping> fields = mappingInfo.getFields();

        // 动态构建SQL语句的字段部分
        StringJoiner columnNames = new StringJoiner(", ");
        StringJoiner placeholders = new StringJoiner(", ");
        for (TableMappingInfo.Mapping field : fields) {
            columnNames.add(field.getField());  // 数据库字段
            placeholders.add("?");               // 占位符
        }

        // 动态生成INSERT SQL语句
        String sql = "INSERT INTO " + tableName + " (" + columnNames.toString() + ") VALUES (" + placeholders.toString() + ")";

        log.info("生成的SQL: {}", sql);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            int batchCount = 0;

            // 遍历数据列表并设置每条记录的参数
            for (int dataIndex = 0; dataIndex < dataList.size(); dataIndex++) {
                Map<String, String> data = dataList.get(dataIndex);
                for (int i = 0; i < fields.size(); i++) {
                    TableMappingInfo.Mapping field = fields.get(i);
                    pstmt.setString(i + 1, data.get(field.getHeader()));  // 根据Excel字段名获取值
                }
                pstmt.addBatch(); // 添加到批处理
                batchCount++;

                // 每BATCH_SIZE条数据执行一次批处理
                if (batchCount == BATCH_SIZE) {
                    pstmt.executeBatch();
                    pstmt.clearBatch();
                    batchCount = 0;
                    log.info("已成功插入{}条数据", (dataIndex + 1));
                }
            }

            // 插入剩余未达到BATCH_SIZE的数据
            if (batchCount > 0) {
                pstmt.executeBatch();
                log.info("剩余数据插入成功，共插入{}条数据", dataList.size());
            }

        } catch (SQLException e) {
            log.error("数据插入失败，回滚事务", e);
            throw new RuntimeException("数据插入失败", e);  // 捕获异常，触发事务回滚
        }
    }
}
