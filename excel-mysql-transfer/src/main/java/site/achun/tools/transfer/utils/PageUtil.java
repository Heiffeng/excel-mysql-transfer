package site.achun.tools.transfer.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import site.achun.tools.transfer.common.RspPage;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * PageUtil
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-18 15:09:13
 */
public class PageUtil {

    public static <F,T> RspPage<T> parse(IPage<F> pageResult, Function<F,T> convert, Integer pageIndex,Integer pageSize){
        RspPage<T> rspPage = new RspPage<>();
        rspPage.setPage(pageIndex);
        rspPage.setSize(pageSize);
        rspPage.setRows(pageResult.getRecords().stream()
                .map(convert)
                .collect(Collectors.toList())
        );
        rspPage.setTotal(pageResult.getTotal());
        return rspPage;
    }

}
