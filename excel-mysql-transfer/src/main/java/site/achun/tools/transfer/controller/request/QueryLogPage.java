package site.achun.tools.transfer.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * query log page request
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-24 17:52:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryLogPage {

    private Integer pageSize;
    private Integer pageIndex;

    private Integer taskId;
}
