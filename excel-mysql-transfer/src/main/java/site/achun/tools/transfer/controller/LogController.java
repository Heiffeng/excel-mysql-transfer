package site.achun.tools.transfer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.achun.tools.transfer.common.Rsp;
import site.achun.tools.transfer.common.RspPage;
import site.achun.tools.transfer.controller.request.QueryLogPage;
import site.achun.tools.transfer.generator.domain.ImportLog;
import site.achun.tools.transfer.generator.service.ImportLogService;
import site.achun.tools.transfer.utils.PageUtil;

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
    public Rsp<RspPage<ImportLog>> getTaskPage(@RequestBody QueryLogPage query) {
        Page<ImportLog> pageResult = importLogService.lambdaQuery()
                .eq(ImportLog::getTaskId, query.getTaskId())
                .page(Page.of(query.getPageIndex(), query.getPageSize()));
        return Rsp.success(PageUtil.parse(pageResult,v->v, query.getPageIndex(), query.getPageSize()));
    }

}
