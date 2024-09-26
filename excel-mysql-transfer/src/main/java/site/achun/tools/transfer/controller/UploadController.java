package site.achun.tools.transfer.controller;

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
import site.achun.tools.transfer.service.CSVService;
import site.achun.tools.transfer.service.ExcelService;
import site.achun.tools.transfer.service.ImportService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Upload Controller
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-20 11:52:50
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UploadController {

    private final CSVService csvService;
    private final ExcelService excelService;

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

        // Check the file extension to determine the file type
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        try {
            if ("csv".equals(fileExtension)) {
                response.setHeaders(csvService.readHeaders(file));
                return Rsp.success(response);
            } else if ("xls".equals(fileExtension) || "xlsx".equals(fileExtension)) {
                response.setHeaders(excelService.readHeaders(file));
                return Rsp.success(response);
            } else {
                return Rsp.error("不支持的文件类型");
            }
        } catch (Exception e) {
            log.error("处理文件时出错: {}", e.getMessage(), e);
            return Rsp.error("文件处理失败");
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
        if(taskId == null || file == null) {
            return Rsp.error("params is null");
        }
        ImportTask task = taskService.lambdaQuery()
                .eq(ImportTask::getId, taskId)
                .one();
        if(task == null) {
            return Rsp.error("not exist task");
        }
        if(task.getStatus() != 1){
            return Rsp.error("illegal task status");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            return Rsp.error("文件名不能为空");
        }

        // TODO 保存文件到本地


        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        // 区分csv或者excel，获取数据内容
        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            if ("csv".equals(fileExtension)) {
                dataList = csvService.readRows(file);
            } else if ("xls".equals(fileExtension) || "xlsx".equals(fileExtension)) {
                dataList = excelService.readRows(file);
            } else {
                return Rsp.error("不支持的文件类型");
            }
        } catch (Exception e) {
            log.error("处理文件时出错: {}", e.getMessage(), e);
            return Rsp.error("文件处理失败");
        }

        TableMappingInfo mappingInfo = JSON.parseObject(task.getTableInfo(), TableMappingInfo.class);

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
