Ext.define('MyApp.view.midData.Tree', {
    extend: 'Ext.tree.Panel',
    requires: [
        'Ext.data.*',
        'Ext.tree.*'
        // 'Ext.ux.CheckColumn'
    ],
    xtype: 'midDataTree',
    width: 180,
    height:'100%',
    loadMask:{msg:"请稍后..."},
    store: 'MidDataDicTreeWithoutFormula',
    containerScroll: true,
    autoScroll : false,
    useArrows: true,
    split: 	true,
    // collapsible: true,
    rootVisible: false
});