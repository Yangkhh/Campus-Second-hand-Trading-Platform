UPDATE users SET nickname = '小艾', campus = '主校区' WHERE username = 'alice';
UPDATE users SET nickname = '小博', campus = '北校区' WHERE username = 'bob';
UPDATE users SET nickname = '管理员', campus = '主校区' WHERE username = 'admin';

UPDATE products
SET title = '数据结构教材',
    description = '二手数据结构教材，书页干净，仅第一章有少量笔记。',
    condition_text = '成色良好',
    trade_location = '图书馆门口'
WHERE id = 1;

UPDATE products
SET title = '87键机械键盘',
    description = '紧凑型青轴键盘，适合宿舍书桌使用。',
    condition_text = '几乎全新',
    trade_location = '三号宿舍楼'
WHERE id = 2;

UPDATE products
SET title = '宿舍台灯',
    description = '可调节护眼台灯，支持三档亮度。',
    condition_text = '成色良好',
    trade_location = '食堂门口'
WHERE id = 3;

UPDATE products
SET title = '篮球',
    description = '室外篮球，使用过一个学期，弹性正常。',
    condition_text = '正常使用痕迹',
    trade_location = '操场'
WHERE id = 4;
