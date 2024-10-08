package site.achun.tools.transfer.utils;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * CompareUtil
 * 对比工具
 * 会产生三部分数据，
 * 1. 相同部分的数据
 * 2. A集合不存在于B集合的数据
 * 3. B集合不存在于A集合的数据
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-18 15:09:13
 */
public class CompareUtil {

    public static <T> Result<T> cal(Collection<T> source, Collection<T> target, Function<T,Object> comparator){
        // 判断source是否为空
        if(CollUtil.isEmpty(source)){
            return new Result<>(null,null,target);
        }
        // 判断target是否为空
        if(CollUtil.isEmpty(target)){
            return new Result<>(source,null,null);
        }
        // 转换为map类型，偏于接下来的步骤用key值对比
        Map<Object, T> sourceMap = source.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(comparator, v -> v));
        Map<Object, T> targetMap = target.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(comparator, v -> v));
        // 取并集
        List<T> intersection = sourceMap.entrySet().stream()
                .filter(s -> targetMap.containsKey(s.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        // 取 source 的差集
        List<T> sourceDifference = sourceMap.entrySet().stream()
                .filter(s -> !targetMap.containsKey(s.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        // 取 target 的差集
        List<T> targetDifference = targetMap.entrySet().stream()
                .filter(t -> !sourceMap.containsKey(t.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        // 返回结果
        return new Result<>(intersection,sourceDifference,targetDifference);
    }

    @Data
    @AllArgsConstructor
    public static class Result<T>{
        private Collection<T> intersection;
        private Collection<T> sourceDifference;
        private Collection<T> targetDifference;
    }

}
