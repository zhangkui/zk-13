-- 宴席余餐再分配登记与风险留痕系统 数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS leftover_food DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE leftover_food;

-- 1. 余餐分类表
CREATE TABLE IF NOT EXISTS food_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    category_name VARCHAR(100) NOT NULL COMMENT '分类名称',
    category_code VARCHAR(50) NOT NULL UNIQUE COMMENT '分类编码',
    description VARCHAR(500) COMMENT '分类描述',
    storage_condition VARCHAR(200) COMMENT '储存条件',
    shelf_life_hours INT DEFAULT 4 COMMENT '建议保质期(小时)',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='余餐分类表';

-- 2. 余餐登记表
CREATE TABLE IF NOT EXISTS leftover_food (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    food_name VARCHAR(200) NOT NULL COMMENT '菜品名称',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    banquet_type VARCHAR(100) COMMENT '宴席类型',
    banquet_date DATE NOT NULL COMMENT '宴席日期',
    banquet_location VARCHAR(200) COMMENT '宴席地点',
    quantity DECIMAL(10,2) NOT NULL COMMENT '数量(份/公斤)',
    quantity_unit VARCHAR(20) DEFAULT '份' COMMENT '计量单位',
    produce_time DATETIME NOT NULL COMMENT '制作时间',
    expiry_time DATETIME NOT NULL COMMENT '过期时间',
    storage_method VARCHAR(200) COMMENT '储存方式',
    temperature VARCHAR(50) COMMENT '保存温度',
    food_status TINYINT DEFAULT 1 COMMENT '状态 1-待领取 2-已领取 3-已过期 4-已销毁',
    description VARCHAR(1000) COMMENT '菜品描述',
    ingredients VARCHAR(500) COMMENT '主要食材',
    allergen_info VARCHAR(500) COMMENT '过敏原信息',
    registrar VARCHAR(100) NOT NULL COMMENT '登记人',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    INDEX idx_category_id (category_id),
    INDEX idx_food_status (food_status),
    INDEX idx_banquet_date (banquet_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='余餐登记表';

-- 3. 领取人信息表
CREATE TABLE IF NOT EXISTS recipient (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    recipient_type VARCHAR(50) NOT NULL COMMENT '领取人类型 个人-个人 机构-机构',
    name VARCHAR(100) NOT NULL COMMENT '姓名/机构名称',
    id_card VARCHAR(50) COMMENT '身份证号(个人)',
    org_code VARCHAR(100) COMMENT '机构代码(机构)',
    phone VARCHAR(50) NOT NULL COMMENT '联系电话',
    email VARCHAR(100) COMMENT '电子邮箱',
    address VARCHAR(500) COMMENT '联系地址',
    emergency_contact VARCHAR(100) COMMENT '紧急联系人',
    emergency_phone VARCHAR(50) COMMENT '紧急联系电话',
    id_card_front VARCHAR(500) COMMENT '身份证正面照片',
    id_card_back VARCHAR(500) COMMENT '身份证反面照片',
    org_certificate VARCHAR(500) COMMENT '机构证明文件',
    recipient_status TINYINT DEFAULT 1 COMMENT '状态 0-待审核 1-正常 2-黑名单',
    total_claimed INT DEFAULT 0 COMMENT '累计领取次数',
    last_claim_time DATETIME COMMENT '最后领取时间',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    INDEX idx_phone (phone),
    INDEX idx_recipient_type (recipient_type),
    INDEX idx_recipient_status (recipient_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领取人信息表';

-- 4. 风险告知书模板表
CREATE TABLE IF NOT EXISTS risk_notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    notice_title VARCHAR(200) NOT NULL COMMENT '告知书标题',
    notice_content TEXT NOT NULL COMMENT '告知书内容',
    notice_version VARCHAR(50) NOT NULL COMMENT '版本号',
    is_current TINYINT DEFAULT 0 COMMENT '是否当前版本 0-否 1-是',
    effective_date DATE COMMENT '生效日期',
    expiry_date DATE COMMENT '失效日期',
    risk_items TEXT COMMENT '风险条款明细(JSON数组)',
    create_by VARCHAR(100) COMMENT '创建人',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    INDEX idx_is_current (is_current),
    INDEX idx_notice_version (notice_version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险告知书模板表';

-- 5. 领取记录表
CREATE TABLE IF NOT EXISTS claim_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    food_id BIGINT NOT NULL COMMENT '余餐ID',
    recipient_id BIGINT NOT NULL COMMENT '领取人ID',
    claim_quantity DECIMAL(10,2) NOT NULL COMMENT '领取数量',
    quantity_unit VARCHAR(20) DEFAULT '份' COMMENT '计量单位',
    claim_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
    claim_purpose VARCHAR(200) COMMENT '领取用途',
    destination VARCHAR(500) COMMENT '去向地址',
    transport_method VARCHAR(100) COMMENT '运输方式',
    expected_arrival_time DATETIME COMMENT '预计送达时间',
    actual_arrival_time DATETIME COMMENT '实际送达时间',
    usage_feedback VARCHAR(1000) COMMENT '使用反馈',
    usage_status TINYINT DEFAULT 1 COMMENT '使用状态 1-运输中 2-已送达 3-已使用 4-异常',
    handler VARCHAR(100) COMMENT '经办人',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    INDEX idx_food_id (food_id),
    INDEX idx_recipient_id (recipient_id),
    INDEX idx_claim_time (claim_time),
    INDEX idx_usage_status (usage_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领取记录表';

-- 6. 签收留档表
CREATE TABLE IF NOT EXISTS sign_archive (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    claim_record_id BIGINT NOT NULL COMMENT '领取记录ID',
    recipient_id BIGINT NOT NULL COMMENT '领取人ID',
    notice_id BIGINT NOT NULL COMMENT '风险告知书ID',
    notice_version VARCHAR(50) NOT NULL COMMENT '告知书版本',
    notice_content_snapshot TEXT COMMENT '告知书内容快照',
    sign_type VARCHAR(50) NOT NULL COMMENT '签收类型 电子签名-电子签名 纸质签字-纸质签字',
    signature_data TEXT COMMENT '电子签名数据(Base64)',
    signature_image VARCHAR(500) COMMENT '签名图片URL',
    paper_sign_image VARCHAR(500) COMMENT '纸质签字照片URL',
    sign_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '签收时间',
    sign_ip VARCHAR(50) COMMENT '签收IP地址',
    sign_location VARCHAR(200) COMMENT '签收地点',
    is_read TINYINT DEFAULT 1 COMMENT '是否已阅读 0-否 1-是',
    is_understood TINYINT DEFAULT 1 COMMENT '是否已理解 0-否 1-是',
    is_accepted TINYINT DEFAULT 1 COMMENT '是否接受风险 0-否 1-是',
    witness_name VARCHAR(100) COMMENT '见证人姓名',
    witness_phone VARCHAR(50) COMMENT '见证人电话',
    archive_status TINYINT DEFAULT 1 COMMENT '存档状态 1-正常 2-已作废',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    INDEX idx_claim_record_id (claim_record_id),
    INDEX idx_recipient_id (recipient_id),
    INDEX idx_sign_time (sign_time),
    INDEX idx_archive_status (archive_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签收留档表';

-- 插入初始数据
-- 余餐分类
INSERT INTO food_category (category_name, category_code, description, storage_condition, shelf_life_hours, sort_order) VALUES
('热菜类', 'HOT', '各类热菜、炖菜、炒菜等', '冷藏0-4℃', 48, 1),
('凉菜类', 'COLD', '各类凉菜、冷拼、沙拉等', '冷藏0-4℃', 24, 2),
('主食类', 'STAPLE', '米饭、馒头、面条、点心等', '常温或冷藏', 24, 3),
('汤品类', 'SOUP', '各类汤品、粥品等', '冷藏0-4℃', 24, 4),
('水果类', 'FRUIT', '各类水果、果盘等', '冷藏0-8℃', 48, 5),
('糕点类', 'PASTRY', '各类糕点、甜品等', '常温或冷藏', 72, 6),
('其他类', 'OTHER', '其他未分类食品', '按要求储存', 24, 99);

-- 风险告知书模板
INSERT INTO risk_notice (notice_title, notice_content, notice_version, is_current, effective_date, risk_items, create_by) VALUES
('余餐领取风险告知书', '
尊敬的领取人：

感谢您参与本次宴席余餐再分配活动。为保障食品安全，维护您的合法权益，请您仔细阅读以下风险告知内容：

一、食品性质说明
本次发放的食品为宴席剩余食品，虽然经过初步筛选和妥善储存，但仍存在一定的食品安全风险。

二、主要风险提示
1. 食品已超过最佳食用时间，口感、风味可能有所下降；
2. 食品储存过程中可能存在微生物繁殖的风险；
3. 食品中可能含有过敏原（如海鲜、花生、坚果、乳制品、小麦等）；
4. 食品在运输、二次加热过程中可能存在污染风险。

三、领取人义务
1. 领取前应仔细检查食品外观、气味是否正常；
2. 领取后应在建议保质期内尽快食用；
3. 食用前应充分加热至中心温度70℃以上；
4. 如发现食品异常，应立即停止食用并妥善处理；
5. 不得将领取的食品用于商业销售或转赠他人牟利。

四、免责声明
1. 余餐发放方已尽到食品筛选、储存和风险告知义务；
2. 领取人在充分知晓风险的前提下自愿领取；
3. 因领取人储存不当、加热不彻底或个人体质原因导致的健康问题，发放方不承担责任；
4. 领取人需自行承担食用后的一切后果。

五、其他说明
本告知书一式两份，领取人签字后即视为已充分理解并同意接受全部条款。
请您在确认无异议后签署本告知书。', 'V1.0', 1, '2026-01-01', 
'[{"itemCode":"RISK001","itemContent":"食品已超过最佳食用时间","level":"高"},{"itemCode":"RISK002","itemContent":"存在微生物繁殖风险","level":"高"},{"itemCode":"RISK003","itemContent":"可能含有过敏原","level":"中"},{"itemCode":"RISK004","itemContent":"运输和加热过程可能存在污染","level":"中"},{"itemCode":"RISK005","itemContent":"食品外观和口感可能下降","level":"低"}]',
'系统管理员');

-- 测试领取人数据
INSERT INTO recipient (recipient_type, name, id_card, phone, address, recipient_status, remark) VALUES
('个人', '张三', '110101199001011234', '13800138001', '北京市朝阳区某某街道123号', 1, '低保户，定期领取'),
('个人', '李四', '110101199002022345', '13800138002', '北京市海淀区某某路456号', 1, '孤寡老人'),
('机构', '阳光社区食堂', '91110000MA001ABC', '010-12345678', '北京市丰台区某某社区内', 1, '社区公益食堂，为困难群体提供免费餐食');

-- 测试余餐数据
INSERT INTO leftover_food (food_name, category_id, banquet_type, banquet_date, banquet_location, quantity, quantity_unit, produce_time, expiry_time, storage_method, temperature, food_status, description, ingredients, allergen_info, registrar) VALUES
('红烧狮子头', 1, '婚宴', '2026-06-09', '北京大饭店宴会厅A', 20, '份', '2026-06-09 12:00:00', '2026-06-11 12:00:00', '冷藏', '0-4℃', 1, '精选五花肉制作，口感软糯', '五花肉、鸡蛋、葱姜、酱油', '含有鸡蛋、大豆', '王登记员'),
('凉拌黄瓜', 2, '婚宴', '2026-06-09', '北京大饭店宴会厅A', 15, '份', '2026-06-09 11:30:00', '2026-06-10 11:30:00', '冷藏', '0-4℃', 1, '清爽可口', '黄瓜、大蒜、醋、香油', '无', '王登记员'),
('白米饭', 3, '婚宴', '2026-06-09', '北京大饭店宴会厅A', 10, '公斤', '2026-06-09 11:00:00', '2026-06-10 11:00:00', '冷藏', '0-4℃', 1, '东北大米，香甜可口', '大米、水', '无', '王登记员'),
('水果拼盘', 5, '婚宴', '2026-06-09', '北京大饭店宴会厅A', 25, '份', '2026-06-09 10:30:00', '2026-06-11 10:30:00', '冷藏', '0-8℃', 2, '时令水果拼盘', '西瓜、苹果、橙子、葡萄', '无', '王登记员');
