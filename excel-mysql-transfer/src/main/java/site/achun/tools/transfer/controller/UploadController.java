package site.achun.tools.transfer.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.achun.tools.transfer.common.Rsp;
import site.achun.tools.transfer.controller.response.ImportResult;
import site.achun.tools.transfer.controller.response.UploadInitResponse;
import site.achun.tools.transfer.core.TableMappingInfo;
import site.achun.tools.transfer.generator.domain.ImportLog;
import site.achun.tools.transfer.generator.domain.ImportTask;
import site.achun.tools.transfer.generator.service.ImportLogService;
import site.achun.tools.transfer.generator.service.ImportTaskService;
import site.achun.tools.transfer.service.FileService;
import site.achun.tools.transfer.service.ImportService;
import site.achun.tools.transfer.utils.CompareUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Upload Controller
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-20 11:52:50
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UploadController {

    private final FileService fileService;

    private final ImportTaskService taskService;
    private final ImportService importService;

    private final ImportLogService importLogService;

    /**
     * 初始化导入模板
     * @param file 上传的excel文件
     * @return 返回内容
     */
    @PostMapping("/upload/init")
    public Rsp<UploadInitResponse> uploadInit(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        log.info("upload file name : {}", fileName);

        UploadInitResponse response = new UploadInitResponse();
        response.setFileName(fileName);

        if (fileName == null) {
            return Rsp.error("文件名不能为空");
        }

        try {
            List<String> headers = fileService.readHeaders(file);
            response.setHeaders(headers);
            return Rsp.success(response);
        } catch (Exception ex) {
            return Rsp.error(ex.getMessage());
        }
    }

    /**
     * 初始化导入模板
     * @param file 上传的excel文件
     * @return 返回内容
     */
    @PostMapping("/upload/import")
    public Rsp<ImportResult> uploadImport(
            @RequestParam("taskId")Integer taskId,
            @RequestParam("file") MultipartFile file
    ) {
        long begin = System.currentTimeMillis();

        // 校验必填参数
        if(taskId == null || file == null) {
            return Rsp.error("params is null");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return Rsp.error("文件名不能为空");
        }

        // 查询任务，并检查状态
        ImportTask task = taskService.lambdaQuery()
                .eq(ImportTask::getId, taskId)
                .one();
        if(task == null) {
            return Rsp.error("not exist task");
        }
        if(task.getStatus() != 1){
            return Rsp.error("illegal task status");
        }

        // 校验文件行头是否匹配
        TableMappingInfo mappingInfo = JSON.parseObject(task.getTableInfo(), TableMappingInfo.class);
        try{
            List<String> importHeaders = fileService.readHeaders(file);
            List<String> mappingHeaders = mappingInfo.getFields().stream()
                    .map(TableMappingInfo.Mapping::getHeader)
                    .filter(StrUtil::isNotEmpty)
                    .toList();
            CompareUtil.Result<String> result = CompareUtil.cal(importHeaders, mappingHeaders, String::valueOf);
            if(CollUtil.isNotEmpty(result.getTargetDifference()) || CollUtil.isNotEmpty(result.getSourceDifference())){
                String importDiffHeaders = String.join(",", result.getSourceDifference());
                String importDiffMappingHeaders = String.join(",", result.getTargetDifference());
                return Rsp.error(String.format("映射错误. <br/> headers:%s <br/> fields:%s",importDiffHeaders,importDiffMappingHeaders));
            }
        }catch (Exception ex){
            log.error("校验文件行头是否匹配异常",ex);
            return Rsp.error(ex.getMessage());
        }

        // TODO 保存文件到本地

        // 区分csv或者excel，获取数据内容
        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            dataList = fileService.readRows(file);
            // 默认字段的处理
            dataList.forEach(data->{
                data.put("ctime",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            });
        } catch (Exception ex) {
            log.error("处理文件时出错", ex);
            return Rsp.error(ex.getMessage());
        }


        try{
            importService.importData(mappingInfo,dataList);
        }catch (Exception ex){
            log.error("文件导入失败",ex);
            return Rsp.error(ex.getMessage());
        }

        // 插入日志
        ImportLog importLog = ImportLog.builder()
                .taskId(taskId)
                .count(dataList.size())
                .fileName(fileName)
                .creator("admin")
                .ctime(LocalDateTime.now())
                .build();
        boolean result = importLogService.save(importLog);
        log.info("log save result:{}",result);

        long end = System.currentTimeMillis();
        ImportResult importResult = ImportResult.builder()
                .cost(end - begin)
                .count(dataList.size())
                .build();
        return Rsp.success(importResult);
    }
}
