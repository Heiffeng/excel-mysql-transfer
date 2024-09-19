package site.achun.tools.transfer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.achun.tools.transfer.common.Rsp;
import site.achun.tools.transfer.generator.domain.ImportLog;
import site.achun.tools.transfer.generator.service.ImportLogService;

/**
 * Log CURD Controller
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-18 15:09:13
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class LogController {

    private final ImportLogService importLogService;

    @PostMapping("/log/query-log-page")
    public Rsp<ImportLog> getTaskPage(@RequestBody ImportLog task) {
        return null;
    }

}
