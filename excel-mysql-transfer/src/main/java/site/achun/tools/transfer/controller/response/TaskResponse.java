package site.achun.tools.transfer.controller.response;

import lombok.*;
import site.achun.tools.transfer.generator.domain.ImportTask;

import java.io.Serial;
import java.io.Serializable;

/**
 * TaskResponse
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-20 14:00:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskResponse extends ImportTask implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 最后更新记录数
    private Long lastImportCount;

}
