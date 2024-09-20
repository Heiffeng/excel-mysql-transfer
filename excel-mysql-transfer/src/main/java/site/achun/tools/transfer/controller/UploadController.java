package site.achun.tools.transfer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.achun.tools.transfer.common.Rsp;
import site.achun.tools.transfer.controller.response.UploadInitResponse;
import site.achun.tools.transfer.service.CSVService;
import site.achun.tools.transfer.service.ExcelService;

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

}
