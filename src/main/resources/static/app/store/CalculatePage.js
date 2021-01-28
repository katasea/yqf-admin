Ext.define("MyApp.store.CalculatePage", {
    extend: "Ext.data.Store",
    autoLoad:true,
    model: "MyApp.model.CalculatePage",
    data: [
        {
            id: "第一步",
            name: "原始收入数据转换"
        },
        {
            id: "第二步",
            name: "根据转换后的收入数据计算工作量分"
        },
        {
            id: "第三步",
            name: "计算辅助数据"
        },
        {
            id: "第四步",
            name: "计算绩效考核成绩"
        },
        {
            id: "第五步",
            name: "根据绩效方案计算最终绩效结果"
        }

    ]
});
