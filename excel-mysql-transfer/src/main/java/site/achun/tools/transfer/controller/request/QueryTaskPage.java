package site.achun.tools.transfer.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * add task request
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-20 16:36:59
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryTaskPage {
    private Integer pageSize;
    private Integer pageIndex;
    private String creator = "admin";
}
