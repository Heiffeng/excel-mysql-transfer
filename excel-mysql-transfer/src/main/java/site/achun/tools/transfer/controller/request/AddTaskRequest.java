package site.achun.tools.transfer.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.achun.tools.transfer.core.TableMappingInfo;

/**
 * add task request
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-20 14:00:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddTaskRequest extends TableMappingInfo {

    private String taskName;

}
