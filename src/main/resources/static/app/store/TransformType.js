Ext.define("MyApp.store.TransformType", {
    extend: "Ext.data.TreeStore",
    root: {
        text: "root",
        children: [
            {
                "id"   :  "1",
                "pid"  :  "0",
                "text" :  "门诊部门",
                "leaf" :  "1"
            },
            {
                "id"   :  "2",
                "pid"  :  "0",
                "text" :  "住院部门",
                "leaf" :  "1"
            },
            {
                "id"   :  "3",
                "pid"  :  "0",
                "text" :  "个人",
                "leaf" :  "1"
            },
            {
                "id"   :  "4",
                "pid"  :  "0",
                "text" :  "核算项目",
                "leaf" :  "1"
            },
            {
                "id"   :  "5",
                "pid"  :  "0",
                "text" :  "执行护士对照",
                "leaf" :  "1"
            },
            {
                "id"   :  "6",
                "pid"  :  "0",
                "text" :  "转换后调整",
                "leaf" :  "1"
            }
        ]
    }
});
