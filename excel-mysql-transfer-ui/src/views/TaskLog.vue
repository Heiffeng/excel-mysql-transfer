<template>
  <el-container>
    <el-main>
      <el-table :data="logData.rows" style="width: 100%">
        <el-table-column prop="id" label="ID" width="100"></el-table-column>
        <el-table-column prop="taskId" label="任务ID" width="150"></el-table-column>
        <el-table-column prop="count" label="数量" width="100"></el-table-column>
        <el-table-column prop="fileName" label="文件名" width="250"></el-table-column>
        <el-table-column prop="creator" label="创建者" width="150"></el-table-column>
        <el-table-column prop="ctime" label="创建时间" width="180"></el-table-column>
      </el-table>

      <el-pagination
          :current-page="pageIndex"
          :page-size="pageSize"
          :total="logData.total"
          @current-change="handlePageChange"
          layout="total, prev, pager, next, jumper"
          class="pagination"
      />
    </el-main>
  </el-container>
</template>

<script lang="ts" setup>
import { ref, watch, onMounted } from 'vue';
import axios from 'axios';
import { defineProps } from 'vue';

const props = defineProps<{ taskId: number ,visible: boolean}>();

const logData = ref({
  rows: [],
  total: 0
});
const pageIndex = ref(1);
const pageSize = ref(10);

const fetchData = async () => {
  try {
    const response = await axios.post('/log/query-log-page', {
      taskId: props.taskId,
      pageIndex: pageIndex.value,
      pageSize: pageSize.value
    });

    if (response.data.code === 0) {
      logData.value = response.data.data;
    } else {
      console.error(response.data.info);
    }
  } catch (error) {
    console.error('请求失败:', error);
  }
};

// 处理分页变化
const handlePageChange = (newPageIndex: number) => {
  pageIndex.value = newPageIndex;
  fetchData();
};

// 监听 taskId 的变化
// watch(() => props.taskId, fetchData, { immediate: true });

watch(
    () => props.visible,
    () => {
      if(props.visible){
        fetchData();
      }
    },
    { immediate: true }
);
// 组件挂载时获取数据
onMounted(fetchData);
</script>

<style scoped>
.el-header {
  background-color: #f0f2f5;
  padding: 20px;
  text-align: center;
}
</style>
