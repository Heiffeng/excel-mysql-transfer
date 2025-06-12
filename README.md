# Excel MySQL Transfer

**Excel MySQL Transfer** 是一个开箱即用的工具，帮助你将 Excel 或 CSV 文件快速导入 MySQL 数据库。项目基于 **Spring Boot + MyBatis Plus** 后端和 **Vue 3 + Element Plus** 前端构建，支持任务管理、日志查看和自动建表等功能。

---

## 📁 项目结构

* `excel-mysql-transfer`：Java 后端项目，负责文件解析、数据导入、任务调度等逻辑
* `excel-mysql-transfer-ui`：前端管理界面，支持任务创建、文件上传和日志查看

---

## 🗃️ 数据库配置

你需要一个 MySQL 实例，建表脚本位于：

```
docs/script/init.sql
```

数据库连接信息请配置在 `excel-mysql-transfer/src/main/resources/application.properties` 中：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_test?useSSL=false&useUnicode=true&characterEncoding=UTF8
spring.datasource.username=root
spring.datasource.password=
```

---

## 🚀 快速启动

### 启动后端服务：

```bash
cd excel-mysql-transfer
mvn spring-boot:run
```

### 启动前端界面：

```bash
cd excel-mysql-transfer-ui
npm install
npm run dev
```

前端通过环境变量 `VITE_API_HOST` 与后端通信，可在 `.env` 文件中配置。

---

## 🔧 功能亮点

* ✅ **自动建表**：根据 Excel/CSV 表头动态生成数据库表结构
* 📤 **文件上传**：支持上传并预览 Excel 或 CSV 文件
* 📝 **任务日志**：查看导入过程中的详细日志与执行结果
* 🔄 **任务管理**：支持多任务并行导入，便于批量处理数据

---

## 🧪 测试

项目包含基础的单元测试，执行如下命令即可运行：

```bash
mvn test
```

---

## 🪪 许可证

本项目基于 [MIT License](LICENSE) 开源发布，自由使用，欢迎贡献！

---

如需自定义导入逻辑或表结构生成规则，可扩展 `MySQLTableGenerator` 和相关 Service 类，满足更复杂的数据处理需求。

如果你觉得这个项目对你有帮助，欢迎 Star ⭐️ 支持！
