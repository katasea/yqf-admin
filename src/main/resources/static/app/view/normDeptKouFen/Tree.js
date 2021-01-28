Ext.define('MyApp.view.normDeptKouFen.Tree', {
    extend: 'Ext.tree.Panel',
    //title:'辅助数据[自动计算]',
    requires: [
        'Ext.data.*',
        'Ext.tree.*'
        // 'Ext.ux.CheckColumn'
    ],
    xtype: 'normDeptKouFenTree',
    width: 180,
    height:'100%',
    loadMask:{msg:"请稍后..."},
    store: 'NormTree',
    containerScroll: true,
    autoScroll : false,
    useArrows: true,
    split: 	true,
    // collapsible: true,
    rootVisible: false
});