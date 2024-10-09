package site.achun.tools.transfer.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel Service
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-20 13:32:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelService {

    public List<String> readHeaders(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            // 读取 Excel 第一行
            List<Map<Integer, String>> rows = EasyExcel.read(inputStream)
                    .headRowNumber(0) // 设定头部行数为 0，表示不跳过任何行
                    .sheet(0) // 选择第一个 sheet
                    .doReadSync(); // 同步读取返回所有数据

            // 判断文件是否为空
            if (rows.isEmpty()) {
                return Collections.emptyList();
            }

            // 获取第一行
            return rows.getFirst().values().stream()
                    .filter(StrUtil::isNotEmpty)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("读取Excel文件失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Map<String,String>> readRows(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            // 读取 Excel 第一行
            List<Map<Integer, String>> rows = EasyExcel.read(inputStream)
                    .headRowNumber(0) // 设定头部行数为 0，表示不跳过任何行
                    .sheet(0) // 选择第一个 sheet
                    .doReadSync(); // 同步读取返回所有数据

            // 判断文件是否为空
            if (rows.isEmpty()) {
                return Collections.emptyList();
            }

            // 获取第一行作为表头
            Map<Integer, String> headerRow = rows.get(0);
            List<Map<String, String>> result = new ArrayList<>();

            // 从第二行开始读取数据
            for (int i = 1; i < rows.size(); i++) {
                Map<Integer, String> dataRow = rows.get(i);
                Map<String, String> mappedRow = new LinkedHashMap<>();

                // 将表头的列与当前行对应列的数据匹配
                for (Map.Entry<Integer, String> entry : dataRow.entrySet()) {
                    Integer columnIndex = entry.getKey();
                    String columnValue = entry.getValue();
                    String headerValue = headerRow.get(columnIndex);

                    if (headerValue != null) {
                        mappedRow.put(headerValue, columnValue);
                    }
                }
                result.add(mappedRow);
            }

            return result;
        } catch (IOException e) {
            log.error("读取Excel文件失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
