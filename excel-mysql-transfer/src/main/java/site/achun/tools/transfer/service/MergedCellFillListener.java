package site.achun.tools.transfer.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.fastjson2.JSON;
import org.apache.poi.sl.usermodel.Sheet;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Description
 *
 * @Author Heiffeng
 * @Date 2025-06-12 15:30:07
 */
@Slf4j
public class MergedCellFillListener extends AnalysisEventListener<Map<Integer, String>> {

    /**
     * 合并单元格
     */
    private List<CellExtra> extraMergeInfoList = new ArrayList<>();

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        if (Objects.requireNonNull(extra.getType()) == CellExtraTypeEnum.MERGE) {
            log.info("extra:{}", JSON.toJSONString(extra));
            extraMergeInfoList.add(extra);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<CellExtra> getExtraMergeInfoList() {
        return extraMergeInfoList;
    }
}