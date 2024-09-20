package site.achun.tools.transfer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
}
