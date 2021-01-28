Ext.define('MyApp.view.midDataFormulaDic.Tree', {
    extend: 'Ext.tree.Panel',
    //title:'辅助数据[自动计算]',
    requires: [
        'Ext.data.*',
        'Ext.tree.*'
        // 'Ext.ux.CheckColumn'
    ],
    xtype: 'midDataFormulaDicTree',
    width: 180,
    height:'100%',
    loadMask:{msg:"请稍后..."},
    store: 'MidDataDicTreeWithFormula',
    containerScroll: true,
    autoScroll : false,
    useArrows: true,
    split: 	true,
    // collapsible: true,
    rootVisible: true
});