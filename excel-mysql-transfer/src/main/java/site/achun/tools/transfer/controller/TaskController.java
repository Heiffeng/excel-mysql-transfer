package site.achun.tools.transfer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.achun.tools.transfer.common.Rsp;
import site.achun.tools.transfer.generator.domain.ImportTask;
import site.achun.tools.transfer.generator.service.ImportTaskService;

import java.util.Arrays;
import java.util.List;

/**
 * Task CURD Controller
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-18 15:09:13
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TaskController {

    private final ImportTaskService importTaskService;

    /**
     * 新增任务
     * @param task ImportTask 实体
     * @return 新增任务的结果
     */
    @PostMapping("/task/add")
    public Rsp<Void> addTask(@RequestBody ImportTask task) {
        return null;
    }

    /**
     * 修改任务
     * @param id 任务ID
     * @param task ImportTask 实体
     * @return 修改任务的结果
     */
    @PostMapping("/task/update")
    public Rsp<Void> updateTask(@RequestBody ImportTask task) {
        return null;
    }

    /**
     * 删除任务
     * @param id 任务ID
     * @return 删除任务的结果
     */
    @PostMapping("/task/deleted")
    public Rsp<Void> deleteTask(@RequestBody ImportTask task) {
        return null;
    }

    /**
     * 查询任务详情
     * @param task 任务ID
     * @return 任务详情
     */
    @PostMapping("/task/query-task-detail")
    public Rsp<ImportTask> getTaskById(@RequestBody ImportTask task) {
        return null;
    }

    @PostMapping("/task/query-task-page")
    public Rsp<ImportTask> getTaskPage(@RequestBody ImportTask task) {
        return null;
    }

}
