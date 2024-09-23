package site.achun.tools.transfer.service;

/**
 * Description
 *
 * @Author sunao
 * @Date 2024-09-20 15:31:07
 */

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.achun.tools.transfer.controller.request.AddTaskRequest;
import site.achun.tools.transfer.core.CreateTableService;
import site.achun.tools.transfer.generator.domain.ImportTask;
import site.achun.tools.transfer.generator.service.ImportTaskService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Task Add Service
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-20 15:31:07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskAddService {

    private final ImportTaskService importTaskService;

    private final DataSource dataSource;

    @Transactional
    public void addTask(AddTaskRequest addTaskRequest) throws SQLException,RuntimeException {
        // 验证表名是否存在
        ImportTask existTask = importTaskService.lambdaQuery()
                .eq(ImportTask::getTableName, addTaskRequest.getTableName())
                .eq(ImportTask::getStatus, 1)
                .last("limit 1")
                .one();
        if(existTask != null){
            throw new RuntimeException("tableName already exist");
        }

        // 执行建表语句
        CreateTableService createTableService = new CreateTableService(dataSource.getConnection());
        boolean createResult = createTableService.createTable(addTaskRequest);
        log.info("create table result: {}", createResult);

        ImportTask importTask = ImportTask.builder()
                .name(addTaskRequest.getTaskName())
                .tableName(addTaskRequest.getTableName())
                .tableInfo(JSON.toJSONString(addTaskRequest))
                .creator("admin")
                .utime(LocalDateTime.now())
                .ctime(LocalDateTime.now())
                .build();
        importTaskService.save(importTask);
    }
}
