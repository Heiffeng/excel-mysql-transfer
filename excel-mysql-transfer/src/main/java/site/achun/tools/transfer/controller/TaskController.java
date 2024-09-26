package site.achun.tools.transfer.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.achun.tools.transfer.common.Rsp;
import site.achun.tools.transfer.common.RspPage;
import site.achun.tools.transfer.controller.request.AddTaskRequest;
import site.achun.tools.transfer.controller.request.QueryTaskPage;
import site.achun.tools.transfer.controller.response.TaskResponse;
import site.achun.tools.transfer.generator.domain.ImportLog;
import site.achun.tools.transfer.generator.domain.ImportTask;
import site.achun.tools.transfer.generator.service.ImportLogService;
import site.achun.tools.transfer.generator.service.ImportTaskService;
import site.achun.tools.transfer.service.TaskAddService;
import site.achun.tools.transfer.utils.PageUtil;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Task CURD Controller
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-18 15:09:13
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskAddService taskAddService;
    private final ImportTaskService importTaskService;
    private final ImportLogService importLogService;

    /**
     * 新增任务
     * @param task ImportTask 实体
     * @return 新增任务的结果
     */
    @PostMapping("/task/add")
    public Rsp<Void> addTask(@RequestBody AddTaskRequest addTaskRequest) {
        log.info("add task request: {}", JSON.toJSONString(addTaskRequest));
        if(StringUtils.isEmpty(addTaskRequest.getTableName())){
            return Rsp.error("table name is empty");
        }
        if(StringUtils.isEmpty(addTaskRequest.getTaskName())){
            return Rsp.error("task name is empty");
        }
        try {
            taskAddService.addTask(addTaskRequest);
        } catch (Exception ex) {
            return Rsp.error(ex.getMessage());
        }
        return Rsp.success(null);
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

    /**
     * 查询任务分页数据
     * @param query 查询条件
     * @return 返回分页
     */
    @PostMapping("/task/query-task-page")
    public Rsp<RspPage<TaskResponse>> getTaskPage(@RequestBody QueryTaskPage query) {
        Page<ImportTask> pageResult = importTaskService.lambdaQuery()
                .eq(StrUtil.isNotEmpty(query.getCreator()), ImportTask::getCreator, query.getCreator())
                .orderByDesc(ImportTask::getCtime)
                .page(Page.of(query.getPageIndex(), query.getPageSize()));

        List<Integer> taskIds = pageResult.getRecords().stream()
                .map(ImportTask::getId)
                .toList();

        Map<Integer, Long> map = getTaskLastImportCountMap(taskIds);
        RspPage<TaskResponse> result = PageUtil.parse(pageResult, v -> BeanUtil.toBean(v, TaskResponse.class), query.getPageIndex(), query.getPageSize());
        result.getRows()
                .forEach(task->{
                    if(map.containsKey(task.getId())){
                        task.setLastImportCount(map.get(task.getId()));
                    }
                });
        return Rsp.success(result);
    }

    /**
     * 获取任务的最后一次导入数量的 Map 映射
     *
     * @param taskIds 任务 ID 列表
     * @return Map<taskId, lastImportCount>
     */
    private Map<Integer, Long> getTaskLastImportCountMap(List<Integer> taskIds) {
        // 查询导入日志中每个 taskId 的最大导入数量
        List<ImportLog> importLogs = importLogService.lambdaQuery()
                .select(ImportLog::getTaskId, ImportLog::getCount)
                .in(ImportLog::getTaskId, taskIds)
                .orderByDesc(ImportLog::getCtime)
                .list();

        // 构建 taskId -> lastImportCount 的映射
        Map<Integer, Long> lastImportCountMap = new HashMap<>();
        for (ImportLog log : importLogs) {
            // 只保留最后一次的导入数量
            lastImportCountMap.putIfAbsent(log.getTaskId(), log.getCount().longValue());
        }

        return lastImportCountMap;
    }

}
