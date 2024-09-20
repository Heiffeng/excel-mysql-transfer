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
import org.springframework.stereotype.Service;
import site.achun.tools.transfer.controller.request.AddTaskRequest;
import site.achun.tools.transfer.generator.domain.ImportTask;
import site.achun.tools.transfer.generator.service.ImportTaskService;

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

    public void addTask(AddTaskRequest addTaskRequest){
        ImportTask importTask = ImportTask.builder()
                .name(addTaskRequest.getTaskName())
                .tableName(addTaskRequest.getTableName())
                .tableInfo(JSON.toJSONString(addTaskRequest))
                .creator("Admin")
                .utime(LocalDateTime.now())
                .ctime(LocalDateTime.now())
                .build();
        importTaskService.save(importTask);
    }
}
