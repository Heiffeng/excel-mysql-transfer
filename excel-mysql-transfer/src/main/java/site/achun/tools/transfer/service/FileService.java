package site.achun.tools.transfer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.achun.tools.transfer.common.Rsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel Service
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-26 10:40:22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final ExcelService excelService;
    private final CSVService csvService;

    public List<String> readHeaders(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        // 区分csv或者excel，获取数据内容
        try {
            if ("csv".equals(fileExtension)) {
                return csvService.readHeaders(file);
            } else if ("xls".equals(fileExtension) || "xlsx".equals(fileExtension)) {
                return excelService.readHeaders(file);
            } else {
                throw new RuntimeException("不支持的文件类型");
            }
        } catch (Exception ex) {
            log.error("处理文件时出错: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    public List<Map<String,String>> readRows(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        // 区分csv或者excel，获取数据内容
        try {
            if ("csv".equals(fileExtension)) {
                return csvService.readRows(file);
            } else if ("xls".equals(fileExtension) || "xlsx".equals(fileExtension)) {
                return excelService.readRows(file);
            } else {
                throw new RuntimeException("不支持的文件类型");
            }
        } catch (Exception ex) {
            log.error("处理文件时出错: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

}
