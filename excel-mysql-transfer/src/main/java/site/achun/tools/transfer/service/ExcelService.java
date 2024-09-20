package site.achun.tools.transfer.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Excel Service
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-20 13:32:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelService {

    public List<String> readRows(MultipartFile file) {
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
            Map<Integer, String> firstRow = rows.getFirst();
            return new ArrayList<>(firstRow.values());
        } catch (IOException e) {
            log.error("读取Excel文件失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
