<script lang="ts" setup>
import {reactive, ref, watch} from 'vue';
import {ElMessage} from "element-plus";
import axios from "axios";
import type {DialogInfo, Task} from "@/views/TaskView.vue";

// 定义 emit 用于触发事件通知父组件
const emit = defineEmits(['formSubmitted']);

const uploadRef = ref();

// Props 类型
interface Props {
  info: DialogInfo;
}

// 接收 Props
const props = defineProps<Props>();

const status = reactive({
  showUpload:true, // 是否显示上传组件
  showTable:false, // 是否显示表编辑组件
})

interface FieldMapping {
  fixed: false,
  checked: boolean,
  header: string,
  field: string,
  dataType: string,
  comment: string
}

const API_HOST = import.meta.env.VITE_API_HOST

const formRows = ref([] as Array<FieldMapping>);

const dataTypes = [
  { value: 'INT', label: 'INT' },
  { value: 'VARCHAR(50)', label: 'VARCHAR(50)' },
  { value: 'datetime', label: 'datetime' }
];

// 初始化任务对象
const task = ref<Task>({});
const uploadUrl = ref<string>('');

const importSuccess = ref<boolean>(false);
const importedCount = ref(0);

const handleUploadImportSuccess = (response: any) => {
  importSuccess.value = true;
  importedCount.value = 102;
}

const handleUploadSuccess = (response: any) => {
  status.showUpload = false;
  status.showTable = true;
  // 从后端返回的数据里解析Excel的行头信息
  const excelHeaders = response.data.headers; // 假设后端返回的格式中有 headers 字段
  const dynamicRows = excelHeaders.map((header: string, index: number) => ({
    fixed: false,
    checked: true,
    header: header,
    field: `a${index + 1}`,
    dataType: 'VARCHAR(50)',
    comment: header
  }));
  formRows.value = [
    {
      fixed: true,
      checked: true,
      header: '',
      field: 'id',
      dataType: 'INT UNSIGNED NOT NULL AUTO_INCREMENT',
      comment: 'Primary Key'
    },
    ...dynamicRows,
    {
      fixed: true,
      checked: true,
      header: '',
      field: 'ctime',
      dataType: 'datetime',
      comment: ''
    },
  ]
};

const handleUploadError = (error: any) => {
  ElMessage.error('文件上传失败');
  console.error(error);
};

const resetForm = () => {
  // 重置表单和状态
  task.value.tableName = '';
  task.value.name = '';
  formRows.value = [];
  status.showUpload = true;
  status.showTable = false;
  importSuccess.value = false;
  importedCount.value = 0;
  if (uploadRef.value) {
    uploadRef.value.clearFiles(); // 清除上传文件状态
  }
};

const whenClickSubmit = async () => {
  if(props.info.type == 1){
     await submitForm()
  }else{
    closeDialog()
  }
}

const submitForm = async () => {
  try {
    // formRows过滤没check的
    const checkFormRows = formRows.value.filter((t: FieldMapping) => t.checked)
    // 提交数据到后端接口
    const response = await axios.post('/task/add', {
      tableName: task.value.tableName,
      taskName: task.value.name,
      fields: checkFormRows
    });

    if (response.data.code == 0) {
      ElMessage.success('提交成功');
      resetForm();
      // 通过 emit 通知父组件表单提交成功
      emit('formSubmitted');
    } else {
      ElMessage.error(response.data.info);
    }
  } catch (error) {
    ElMessage.error('提交过程中出错');
    console.error(error);
  }
};

const closeDialog = () => {
  resetForm();
  // 通过 emit 通知父组件表单提交成功
  emit('formSubmitted');
}

const getUploadActionUrl = (type:number) => {
  console.log("type:",type);
  switch (type){
    case 1:
    case 2:
      return API_HOST +'/upload/init';
    case 3:
      return API_HOST +'/upload/import';
  }
}

// 监听 taskId 变化，模拟加载任务数据和表字段映射
watch(
    () => props.info,
    (newInfo) => {
      resetForm()
      console.log("newInfo:",newInfo);
      uploadUrl.value = getUploadActionUrl(newInfo.type)
      if(newInfo.task){
        task.value = newInfo.task;
      }
      if(newInfo.type == 2){
        status.showUpload = false;
        status.showTable = true;
        formRows.value = JSON.parse(<string>newInfo.task?.tableInfo).fields;
        formRows.value.map(a=>a.fixed = true)
      }
    },
    { immediate: true }
);

</script>

<template>
  <div>
    <!-- 这里可以根据 taskId 加载相应的任务详情 -->
    <!-- 表名输入框 -->
    <el-form :model="task" label-width="80px" inline>
      <el-form-item label="任务名" style="width: 45%;">
        <el-input v-model="task.name" :disabled="info.type != 1" placeholder="请输入任务名"></el-input>
      </el-form-item>
      <el-form-item label="表名" style="width: 45%;">
        <el-input v-model="task.tableName" :disabled="info.type != 1" placeholder="请输入表名"></el-input>
      </el-form-item>
    </el-form>
    <!-- 使用 el-divider 添加横线 -->
    <el-divider></el-divider>
    <!-- 文件上传区域 -->
    <el-upload
      v-if="status.showUpload"
      ref="uploadRef"
      class="upload-demo"
      drag
      :action="uploadUrl"
      :data="{taskId: task.id}"
      :on-success="info.type == 1 ? handleUploadSuccess : handleUploadImportSuccess"
      :on-error="handleUploadError"
      :limit="1"
      :auto-upload="true"
      accept=".xls,.xlsx,.csv"
    >
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">点击或者拖拽文件到此处上传</div>
      <div class="el-upload__tip">只支持Excel文件（.xls, .xlsx, .csv）</div>
    </el-upload>

    <div>
      <el-alert v-if="importSuccess" :title="'导入成功，添加了' + importedCount + '条数据'" type="success" show-icon></el-alert>
    </div>

    <div class="form-container" v-if="status.showTable">

      <!-- 添加列标题 -->
      <div class="form-row">
        <span style="width: 20px;"></span> <!-- 占位符，用于对齐 -->
        <span style="width: 170px;">Excel</span>
        <span style="width: 170px;">MySQL</span>
        <span style="width: 170px;">数据类型</span>
        <span style="width: 170px;">注释</span>
      </div>

      <div class="form-row" v-for="(row, index) in formRows" :key="index">
        <!-- Checkbox -->
        <el-checkbox v-model="row.checked" :disabled="row.fixed" :style="{ visibility: row.header ? 'visible' : 'hidden' }"></el-checkbox>

        <!-- Excel Column -->
        <el-input v-model="row.header" :disabled="row.fixed" :style="{ visibility: row.header ? 'visible' : 'hidden' ,width:'170px'}" placeholder="Excel" />

        <!-- MySQL Column -->
        <el-input v-model="row.field" :disabled="row.fixed" style="width: 170px" placeholder="MySQL" />

        <!-- Data Type -->
        <el-select v-model="row.dataType" :disabled="row.fixed" placeholder="数据类型" style="width: 170px">
          <el-option
              v-for="item in dataTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>

        <!-- Comment -->
        <el-input v-model="row.comment" :disabled="row.fixed" placeholder="注释" style="width: 170px" />
      </div>
    </div>

    <!-- 底部按钮 -->
    <div class="form-footer">
      <el-button @click="resetForm">重置</el-button>
      <el-button type="primary" @click="whenClickSubmit">{{info.type == 1 ? '提交' : '确认' }}</el-button>
    </div>

  </div>
</template>

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

.form-footer {
  margin-top: 20px;
  text-align: right;
}
</style>
