<template>
  <div>
    <!-- 这里可以根据 taskId 加载相应的任务详情 -->
    <!-- 表名输入框 -->
    <el-form :model="task">
      <el-form-item label="表名" style="display: block;">
        <el-input v-model="task.tableName" placeholder="请输入表名"></el-input>
      </el-form-item>
    </el-form>

    <!-- 文件上传区域 -->
    <el-upload
        class="upload-demo"
        drag
        :action="API_HOST +'/upload/init'"
    :on-success="handleUploadSuccess"
    :on-error="handleUploadError"
    :limit="1"
    :auto-upload="true"
    accept=".xls,.xlsx,.csv"
    >
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">点击或者拖拽文件到此处上传</div>
      <div class="el-upload__tip">只支持Excel文件（.xls, .xlsx, .csv）</div>
    </el-upload>

    <div class="form-container">
      <div class="form-row" v-for="(row, index) in formRows" :key="index">
        <!-- Checkbox -->
        <el-checkbox v-model="row.checked"></el-checkbox>

        <!-- Excel Column -->
        <el-input v-model="row.excel" placeholder="Excel" />

        <!-- MySQL Column -->
        <el-input v-model="row.mysql" placeholder="MySQL" />

        <!-- Data Type -->
        <el-select v-model="row.dataType" placeholder="数据类型">
          <el-option
              v-for="item in dataTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>

        <!-- Comment -->
        <el-input v-model="row.comment" placeholder="注释" />
      </div>
    </div>

  </div>
</template>

<script lang="ts" setup>
import { defineComponent, ref, watch } from 'vue';
import {ElMessage} from "element-plus";

interface Task {
  id: number;
  name: string;
  status: string;
  tableName: string;
}

interface FieldMapping {
  fieldName: string;
  excelHeader: string;
}

const API_HOST = import.meta.env.VITE_API_HOST

const formRows = ref([
  { checked: true, excel: '图书编码', mysql: 'a4', dataType: 'VARCHAR(50)', comment: '图书编码' },
  { checked: true, excel: '图书名称', mysql: 'a2', dataType: 'VARCHAR(50)', comment: '图书名称' },
  { checked: true, excel: '出版社ID', mysql: 'a3', dataType: 'VARCHAR(50)', comment: '出版社ID' },
  { checked: true, excel: '出版社名称', mysql: 'a4', dataType: 'VARCHAR(50)', comment: '出版社名称' },
  { checked: false, excel: '', mysql: '', dataType: 'VARCHAR(50)', comment: '' },
  { checked: false, excel: '', mysql: '', dataType: 'VARCHAR(50)', comment: '' },
  { checked: false, excel: 'file_name', mysql: 'file_name', dataType: 'VARCHAR(50)', comment: '文件名称' },
  { checked: false, excel: 'batch_time', mysql: 'batch_time', dataType: 'datetime', comment: '批次时间' }
]);

const dataTypes = [
  { value: 'INT', label: 'INT' },
  { value: 'VARCHAR(50)', label: 'VARCHAR(50)' },
  { value: 'datetime', label: 'datetime' }
];
// Props 类型
interface Props {
  taskId: number;
}

// 接收 Props
const props = defineProps<Props>();

// 初始化任务对象
const task = ref<Task>({
  id: props.taskId,
  name: '',
  status: '待开始',
  tableName: '',
});

// 动态表字段和 Excel 行头映射数组
const fieldMappings = ref<FieldMapping[]>([
  { fieldName: '', excelHeader: '' },
  { fieldName: '', excelHeader: '' },
]);

// 监听 taskId 变化，模拟加载任务数据和表字段映射
watch(
    () => props.taskId,
    (newTaskId) => {
      // 根据 taskId 加载任务的具体数据，这里可以使用 API 请求
      task.value = {
        id: newTaskId,
        name: `任务 ${newTaskId}`,
        status: '进行中',
        tableName: '',
      };

      // 模拟加载表字段和 Excel 行头映射
      fieldMappings.value = [
        { fieldName: '字段1', excelHeader: '行头1' },
        { fieldName: '字段2', excelHeader: '行头2' },
      ];
    },
    { immediate: true }
);

const handleUploadSuccess = (response: any) => {
  // 从后端返回的数据里解析Excel的行头信息
  const excelHeaders = response.data.headers; // 假设后端返回的格式中有 headers 字段
  // mappingData.value = excelHeaders.map((header: string) => ({
  //   excelHeader: header,
  //   fieldName: '',
  //   dataType: 'VARCHAR(50)' // 默认给一个初始值
  // }));
};

const handleUploadError = (error: any) => {
  ElMessage.error('文件上传失败');
  console.error(error);
};
</script>

<style scoped>
.form-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.form-row {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>
