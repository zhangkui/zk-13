# 宴席余餐再分配登记与风险留痕系统

## 项目简介

本系统旨在实现宴席剩余食品的规范化再分配管理，通过数字化登记、风险告知、去向追踪和签收留档等功能，确保余餐再分配过程的食品安全和合规性，减少食品浪费，同时保障领取人的知情权和健康安全。

### 核心价值

- **减少浪费**：将宴席剩余食品有序分配给有需要的群体
- **风险防控**：通过风险告知书和签收留档，明确食品安全责任
- **全程追溯**：余餐从登记、领取到使用的全流程可追踪
- **规范管理**：领取人资质审核、黑名单机制保障分配公平

---

## 功能模块

### 1. 数据仪表盘
- 余餐登记总数、待领取、已领取统计
- 领取人总数、活跃领取人统计
- 领取记录总数、运输中、已送达统计
- 签收档案总数统计
- 余餐状态分布饼图
- 领取状态分布饼图

### 2. 余餐分类管理
- 分类增删改查（热菜类、凉菜类、主食类、汤品类、水果类、糕点类、其他类）
- 分类编码、储存条件、建议保质期配置
- 分类排序和状态启用/禁用

### 3. 余餐登记管理
- 余餐信息登记（菜品名称、分类、宴席信息、数量、制作/过期时间等）
- 主要食材和过敏原信息记录
- 储存方式和保存温度管理
- 余餐状态管理（待领取→已领取→已过期→已销毁）
- **领取操作**：选择领取人→填写领取信息→阅读风险告知→确认签收

### 4. 领取人管理
- 个人/机构两种类型领取人管理
- 身份信息记录（身份证号/机构代码）
- 联系信息、紧急联系人管理
- 状态管理（待审核→正常→黑名单）
- 累计领取次数和最后领取时间统计

### 5. 领取记录追踪
- 领取记录分页查询（关键词搜索、状态筛选）
- 去向地址、运输方式追踪
- 预计送达/实际送达时间管理
- 使用状态流转（运输中→已送达→已使用→异常）
- 使用反馈和异常情况记录
- **一键查看关联的签收留档**

### 6. 风险告知管理
- 风险告知书多版本管理
- 版本号、生效/失效日期配置
- 风险条款明细（JSON数组，支持高/中/低风险等级）
- 告知书内容富文本编辑
- 设置当前生效版本
- 告知书预览功能

### 7. 签收留档查询
- 签收档案列表查询（领取人/菜品搜索、状态筛选）
- 签收流程步骤展示（已阅读→已理解→已接受→签字确认）
- 风险告知书内容快照存档
- 电子签名/纸质签字图片存档
- 见证人信息记录
- 签收IP、地点、时间完整留痕

---

## 技术栈

### 后端技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.5 | 核心框架 |
| MyBatis-Plus | 3.5.5 | ORM框架 |
| MySQL | 8.0 | 关系型数据库 |
| Druid | 1.2.22 | 数据库连接池 |
| Lombok | 1.18.32 | 代码简化工具 |
| Hutool | 5.8.27 | Java工具类库 |
| Knife4j | 4.4.0 | API文档（Swagger增强） |
| Validation | - | 参数校验 |

### 前端技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4.21 | 前端框架 |
| Vue Router | 4.3.0 | 路由管理 |
| Pinia | 2.1.7 | 状态管理 |
| Element Plus | 2.7.0 | UI组件库 |
| Axios | 1.6.8 | HTTP请求库 |
| ECharts | 5.5.0 | 图表库 |
| vue-echarts | 6.7.3 | ECharts Vue封装 |
| Day.js | 1.11.10 | 日期处理 |
| Vite | 5.2.0 | 构建工具 |

### 部署技术栈
| 技术 | 说明 |
|------|------|
| Docker | 容器化 |
| Docker Compose | 容器编排 |
| Nginx | 前端静态服务 + 反向代理 |

---

## 目录结构

```
.
├── backend/                          # 后端 Spring Boot 项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/leftoverfood/
│   │       │   ├── common/           # 公共类（结果封装、分页、异常处理）
│   │       │   ├── config/           # 配置类（MyBatis-Plus、Web跨域、自动填充）
│   │       │   ├── controller/       # 控制器层
│   │       │   ├── dto/              # 数据传输对象（查询参数、请求体）
│   │       │   ├── entity/           # 实体类（数据库表映射）
│   │       │   ├── mapper/           # 数据访问层（MyBatis Mapper）
│   │       │   └── service/          # 业务逻辑层
│   │       │       └── impl/         # 业务实现类
│   │       └── resources/
│   │           └── application.yml   # 应用配置
│   ├── Dockerfile                    # 后端 Docker 构建文件
│   └── pom.xml                       # Maven 依赖配置
│
├── frontend/                         # 前端 Vue 3 项目
│   ├── src/
│   │   ├── api/                      # API 请求封装
│   │   ├── router/                   # 路由配置
│   │   ├── utils/                    # 工具类（Axios封装）
│   │   ├── views/                    # 页面组件
│   │   ├── App.vue                   # 根组件
│   │   └── main.js                   # 入口文件
│   ├── Dockerfile                    # 前端 Docker 构建文件
│   ├── nginx.conf                    # Nginx 配置
│   ├── vite.config.js                # Vite 构建配置
│   └── package.json                  # NPM 依赖配置
│
├── docker/                           # Docker 相关配置
│   └── mysql/
│       ├── conf/my.cnf               # MySQL 配置文件
│       ├── init/init.sql             # 数据库初始化脚本
│       └── data/                     # MySQL 数据目录（运行时生成）
│
├── docker-compose.yml                # Docker Compose 编排文件
├── .gitignore                        # Git 忽略配置
└── README.md                         # 项目说明文档
```

---

## 数据库设计

### 核心数据表

| 表名 | 说明 |
|------|------|
| `food_category` | 余餐分类表 |
| `leftover_food` | 余餐登记表 |
| `recipient` | 领取人信息表 |
| `risk_notice` | 风险告知书模板表 |
| `claim_record` | 领取记录表 |
| `sign_archive` | 签收留档表 |

### ER关系说明

```
food_category (1) ────< (N) leftover_food
leftover_food (1) ────< (N) claim_record
recipient (1)     ────< (N) claim_record
claim_record (1)  ──── (1) sign_archive
risk_notice (1)   ────< (N) sign_archive
```

---

## 本地开发指南

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.9+
- npm 9+ 或 pnpm/yarn

### 后端启动

1. **创建数据库**

```sql
CREATE DATABASE leftover_food DEFAULT CHARACTER SET utf8mb4;
```

2. **执行初始化脚本**

执行 `docker/mysql/init/init.sql` 中的 SQL 语句创建表和初始化数据。

3. **修改数据库配置**

编辑 `backend/src/main/resources/application.yml` 中的 dev 配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/leftover_food?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 你的密码
```

4. **编译运行**

```bash
cd backend
mvn clean compile
mvn spring-boot:run
```

后端服务默认启动在 `http://localhost:8080/api`

5. **访问API文档**

启动后访问 Knife4j 文档：`http://localhost:8080/api/doc.html`

### 前端启动

1. **安装依赖**

```bash
cd frontend
npm install
```

2. **启动开发服务器**

```bash
npm run dev
```

前端默认启动在 `http://localhost:5173`

Vite 已配置代理 `/api` → `http://localhost:8080`

---

## Docker 部署指南

### 前置要求
- Docker 24.0+
- Docker Compose v2+

### 一键部署

```bash
# 在项目根目录执行
docker-compose up -d
```

首次启动会自动：
1. 拉取 MySQL 8.0 镜像并初始化数据库
2. 构建后端 Spring Boot 镜像并启动
3. 构建前端 Vue 3 镜像（Nginx）并启动

### 服务访问

| 服务 | 地址 | 说明 |
|------|------|------|
| 前端 | http://localhost | 浏览器直接访问 |
| 后端API | http://localhost:8080/api | 后端接口 |
| MySQL | localhost:3306 | 数据库连接 |

### 部署配置说明

修改 `docker-compose.yml` 可自定义：

```yaml
services:
  mysql:
    environment:
      MYSQL_ROOT_PASSWORD: 你的root密码    # 修改数据库root密码
      MYSQL_DATABASE: leftover_food        # 数据库名
      MYSQL_USER: leftover                 # 应用用户名
      MYSQL_PASSWORD: leftover123          # 应用用户密码
    ports:
      - "3306:3306"                        # 修改宿主机映射端口

  backend:
    ports:
      - "8080:8080"                        # 修改后端端口

  frontend:
    ports:
      - "80:80"                            # 修改前端端口
```

### 常用运维命令

```bash
# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f mysql       # 查看MySQL日志
docker-compose logs -f backend     # 查看后端日志
docker-compose logs -f frontend    # 查看前端日志

# 重启服务
docker-compose restart

# 停止服务
docker-compose down

# 停止并清除数据卷（慎用！会删除数据库数据）
docker-compose down -v

# 重新构建并启动（代码更新后）
docker-compose up -d --build
```

---

## API 接口概览

### 余餐管理接口 `/api/food`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/categories` | 获取所有余餐分类 |
| POST | `/category` | 新增余餐分类 |
| PUT | `/category` | 更新余餐分类 |
| DELETE | `/category/{id}` | 删除余餐分类 |
| GET | `/page` | 分页查询余餐登记 |
| GET | `/{id}` | 获取余餐详情 |
| POST | `` | 新增余餐登记 |
| PUT | `` | 更新余餐登记 |
| PUT | `/{id}/status` | 更新余餐状态 |
| DELETE | `/{id}` | 删除余餐登记 |
| GET | `/available` | 获取待领取余餐列表 |

### 领取人管理接口 `/api/recipient`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/page` | 分页查询领取人 |
| GET | `/{id}` | 获取领取人详情 |
| POST | `` | 新增领取人 |
| PUT | `` | 更新领取人 |
| PUT | `/{id}/status` | 更新领取人状态 |
| DELETE | `/{id}` | 删除领取人 |
| GET | `/list` | 获取正常状态领取人列表 |

### 领取记录管理接口 `/api/claim`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/page` | 分页查询领取记录 |
| GET | `/{id}` | 获取领取记录详情 |
| POST | `` | 领取余餐（含风险签收） |
| PUT | `/{id}/status` | 更新使用状态 |
| PUT | `` | 更新领取记录 |
| DELETE | `/{id}` | 删除领取记录 |

### 风险告知与签收接口 `/api/risk`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/notice/current` | 获取当前生效风险告知书 |
| GET | `/notice/list` | 获取风险告知书列表 |
| POST | `/notice` | 新增风险告知书 |
| PUT | `/notice` | 更新风险告知书 |
| PUT | `/notice/{id}/current` | 设置为当前版本 |
| DELETE | `/notice/{id}` | 删除风险告知书 |
| GET | `/archive/page` | 分页查询签收记录 |
| GET | `/archive/{id}` | 获取签收详情 |
| GET | `/archive/claim/{claimRecordId}` | 根据领取记录ID获取签收 |

### 仪表盘接口 `/api/dashboard`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/stats` | 获取系统统计数据 |

---

## 系统状态枚举

### 余餐状态 `food_status`
| 值 | 文本 | 颜色 |
|----|------|------|
| 1 | 待领取 | 绿色 |
| 2 | 已领取 | 橙色 |
| 3 | 已过期 | 红色 |
| 4 | 已销毁 | 灰色 |

### 领取使用状态 `usage_status`
| 值 | 文本 | 颜色 |
|----|------|------|
| 1 | 运输中 | 蓝色 |
| 2 | 已送达 | 绿色 |
| 3 | 已使用 | 橙色 |
| 4 | 异常 | 红色 |

### 领取人状态 `recipient_status`
| 值 | 文本 | 颜色 |
|----|------|------|
| 0 | 待审核 | 橙色 |
| 1 | 正常 | 绿色 |
| 2 | 黑名单 | 红色 |

### 存档状态 `archive_status`
| 值 | 文本 | 颜色 |
|----|------|------|
| 1 | 正常 | 绿色 |
| 2 | 已作废 | 灰色 |

---

## 常见问题

### 1. Windows 下 Docker 启动 MySQL 数据卷权限问题？
确保 `docker/mysql/data` 目录存在且有写入权限，或删除本地 data 目录让 Docker 自动创建。

### 2. 后端启动报 `Communications link failure`？
检查 MySQL 服务是否启动，以及 `application.yml` 中的数据库连接配置是否正确。

### 3. 前端开发环境跨域问题？
`vite.config.js` 已配置代理，所有 `/api` 请求会转发到 `http://localhost:8080`，无需额外配置。

### 4. Docker 构建失败提示找不到依赖？
确保网络通畅，可以尝试配置国内镜像源：
- Maven：修改 `settings.xml` 配置阿里云镜像
- npm：`npm config set registry https://registry.npmmirror.com`

### 5. 如何修改数据库密码？
1. 修改 `docker-compose.yml` 中 MySQL 的环境变量
2. 修改 `backend/src/main/resources/application.yml` 中 docker profile 的连接信息
3. 重新构建：`docker-compose up -d --build`

---

## 许可证

Copyright © 2026 宴席余餐再分配系统

---

## 联系方式

如有问题或建议，请联系项目维护者。
