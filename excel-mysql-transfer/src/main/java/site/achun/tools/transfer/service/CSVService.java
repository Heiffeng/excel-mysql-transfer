package site.achun.tools.transfer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


/**
 * CSV Service
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-20 13:32:08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CSVService {

    public List<String> readHeaders(MultipartFile file) {
        List<String> firstRow = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // 读取CSV文件的第一行
            String line = reader.readLine();
            if (line != null) {
                // 使用逗号作为分隔符，可以根据实际情况调整分隔符
                String[] columns = line.split(",");
                firstRow.addAll(Arrays.asList(columns));
            }
        } catch (IOException e) {
            log.error("读取CSV文件失败: {}", e.getMessage(), e);
        }

        return firstRow;
    }



    public List<Map<String,String>> readRows(MultipartFile file) {
        List<Map<String, String>> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // 读取CSV文件的第一行作为表头
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return result; // 如果文件为空，则返回空列表
            }

            // 使用逗号作为分隔符，提取表头
            String[] headers = headerLine.split(",");

            // 逐行读取CSV文件数据
            String line;
            while ((line = reader.readLine()) != null) {
                // 将每一行的数据按照逗号分割
                String[] values = line.split(",");

                // 将每一行数据与表头匹配
                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    String key = headers[i];
                    String value = i < values.length ? values[i] : ""; // 防止越界，空列处理为空字符串
                    rowMap.put(key, value);
                }

                // 将匹配好的行数据添加到结果列表中
                result.add(rowMap);
            }
        } catch (IOException e) {
            log.error("读取CSV文件失败: {}", e.getMessage(), e);
        }

        return result;
    }
}
