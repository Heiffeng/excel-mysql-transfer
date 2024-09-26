<script setup lang="ts">
import {onMounted, ref} from 'vue';
import axios from 'axios';
import TaskDialog from "@/views/TaskDialog.vue";
import TaskLog from "@/views/TaskLog.vue";

export interface Task {
  id: number
  name: string
  status: string,
  tableName: string,
  tableInfo: string
}

export interface DialogInfo {
  type: number // 1. 新增任务， 2. 查看任务  3.数据导入
  title: string
  task: Task | null
}

const dialogVisible = ref(false);
const logDialogVisible = ref(false);
const logTaskId = ref<number>(0);

const dialogInfo = ref<DialogInfo>({
  type:1,
  title:'新建任务',
  task:null,
});

const tasks = ref<Task[]>([]);
const total = ref(0);
const pageSize = ref(10);
const currentPage = ref(1);

const openDialog = (info : DialogInfo) => {
  dialogInfo.value = info;
  dialogVisible.value = true;
};

const openLogDialog = (task:Task) => {
  logTaskId.value = task.id;
  logDialogVisible.value = true;
}

const handleClose = (done: () => void) => {
  done()
}

const handleLogDialogClose = (done: () => void) => {
  logDialogVisible.value = false;
  done();
}

const formSubmitted = () => {
  dialogVisible.value = false;
  fetchTasks(1,pageSize.value);
}

// 获取任务数据
const fetchTasks = async (page: number, size: number) => {
  try {
    const response = await axios.post('/task/query-task-page', {
      pageIndex:page,
      pageSize:size,
    });
    tasks.value = response.data.data.rows;
    total.value = response.data.data.total; // 假设接口返回总记录数
  } catch (error) {
    console.error('获取任务数据失败:', error);
  }
};

// 分页变化时触发
const handlePageChange = (newPage: number) => {
  currentPage.value = newPage;
  fetchTasks(currentPage.value, pageSize.value);
};

// 编辑任务（可以实现具体逻辑）
const importFile = (task: Task) => {
  // 实现具体的编辑逻辑
  openDialog({
    task: task,
    title: "导入数据" + "["+task.name+"]",
    type: 3,
  })
}

// 初次加载时获取数据
onMounted(() => {
  fetchTasks(currentPage.value, pageSize.value);
});

</script>

<template> <div class="task-page">
  <el-button type="primary" @click="openDialog({title:'新建任务',type:1, task:null})" class="create-btn">新建任务</el-button>
  <el-table :data="tasks" style="width: 100%" class="task-table">
    <el-table-column prop="id" label="ID" width="50"></el-table-column>
    <el-table-column prop="name" label="任务名称"></el-table-column>
    <el-table-column prop="tableName" label="表名"></el-table-column>
    <el-table-column prop="creator" label="负责人"></el-table-column>
    <el-table-column prop="lastImportCount" label="记录数"></el-table-column>
    <el-table-column prop="utime" label="更新时间" width="180"></el-table-column>
    <el-table-column prop="status" label="状态">
      <template v-slot="scope">
        {{ scope.row.status == 1 ? '正常' : '禁用' }}
      </template>
    </el-table-column>

    <el-table-column label="操作" width="250">
      <template v-slot="scope">
        <el-button @click="openDialog({type:2,title:'查看任务',task:scope.row})" type="text" class="action-btn">查看</el-button>
        <el-button @click="importFile(scope.row)" v-if="scope.row.status == 1" type="text" class="action-btn">导入</el-button>
        <el-button @click="openLogDialog(scope.row)" type="text" class="action-btn">日志</el-button>
      </template>
    </el-table-column>
  </el-table>

  <!-- 分页组件 -->
  <el-pagination
      v-model:current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      @current-change="handlePageChange"
      layout="total, prev, pager, next, jumper"
      class="pagination"
  />
</div>

  <!-- 新增和更新弹窗 -->
  <el-dialog
      v-model="dialogVisible"
      :title="dialogInfo.title"
      width="800"
      :before-close="handleClose"
  >
    <task-dialog
        :info="dialogInfo"
        @formSubmitted="formSubmitted"
    />
  </el-dialog>

  <!-- 查看日志的弹窗 -->
  <el-dialog
      v-model="logDialogVisible"
      title="查看日志"
      width="800"
      :before-close="handleLogDialogClose"
  >
    <task-log :task-id="logTaskId" :visible ="logDialogVisible" />
  </el-dialog>

</template>

<style scoped>

.task-page {
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 10px;
}

.create-btn {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.15);
}

.task-table {
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
}

.el-table th, .el-table td {
  text-align: center;
  padding: 12px;
}

.el-table__row {
  transition: background-color 0.3s ease;
}

.el-table__row:hover {
  background-color: #f2f6fc;
}

.action-btn {
  margin-right: 10px;
  color: #409eff;
  transition: color 0.3s ease;
}

.action-btn:hover {
  color: #66b1ff;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
