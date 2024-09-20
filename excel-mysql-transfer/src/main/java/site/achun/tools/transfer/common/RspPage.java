package site.achun.tools.transfer.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Common Page Response
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-20 16:41:26
 */
@Data
public class RspPage<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 6989962323304566323L;
    /**
     * 每页显示记录数
     */
    private int size = 20;
    /**
     * 当前页
     */
    private int page = 1;
    /**
     * 总记录数
     */
    private long total = 0;
    /**
     * 总页数
     */
    private long pages = 0;
    /**
     * 记录集合
     */
    private List<T> rows;

}
