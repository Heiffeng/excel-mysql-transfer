package site.achun.tools.transfer.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * ImportResult
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-24 17:18:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer count;
    private Long cost;

}
