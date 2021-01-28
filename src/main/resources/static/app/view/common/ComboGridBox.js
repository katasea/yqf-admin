// [{
//     text: '#ID',
//     dataIndex: 'id'
// }, {
//     text: '名称',
//     dataIndex: 'name'
// }, {
//     text: '描述',
//     dataIndex: 'desc'
// }]
//title,width,columns
Ext.define('MyApp.view.common.ComboGridBox', {
    extend: 'Ext.form.field.ComboBox',
    xtype:'comboGridBox',
    multiSelect: true,
    triggerAction:'all',
    forceSelection:true,
    minChars:1,
    matchFieldWidth : false,
    queryParam:'keyword',
    typeAhead : true,
    queryMode : 'remote',
    listeners: {
        'focus': {
            fn: function(e) {
                e.expand();
                this.doQuery(this.allQuery, true);
            },
            buffer:200
        }
    },
    selectOnFocus:true,
    createPicker: function () {
        var me = this;

        var picker = Ext.create('Ext.grid.Panel', {
            title: me.gridCfg.title,
            store: me.store,
            border:1,
            title:me.gridCfg.title,
            frame: true,
            width:me.gridCfg.width,
            height:me.gridCfg.height,
            columns: me.gridCfg.columns,
            bodyStyle: 'z-index: 111111111;',
            selModel: {
                mode: me.multiSelect ? 'SIMPLE' : 'SINGLE'
            },
            floating: true,
            hidden: true,
            columnLines: true,
            focusOnToFront: false,
            // bbar: {
            //     xtype: 'pagingtoolbar',
            //     store: me.store,
            //     displayInfo: true
            // }
        });
        me.mon(picker, {
            itemclick: me.onItemClick,
            refresh: me.onListRefresh,
            scope: me
        });

        me.mon(picker.getSelectionModel(), {
            beforeselect: me.onBeforeSelect,
            beforedeselect: me.onBeforeDeselect,
            selectionchange: me.onListSelectionChange,
            scope: me
        });
        this.picker = picker;
        return picker;
    },

    onItemClick: function (picker, record) {
        /*
         * If we're doing single selection, the selection change events won't fire when
         * clicking on the selected element. Detect it here.
         */
        var me = this,
            selection = me.picker.getSelectionModel().getSelection(),
            valueField = me.valueField;

        if (!me.multiSelect && selection.length) {
            if (record.get(valueField) === selection[0].get(valueField)) {
                // Make sure we also update the display value if it's only partial
                me.displayTplData = [record.data];
                me.setRawValue(me.getDisplayValue());
                me.collapse();
            }
        }
    },

    matchFieldWidth: false,

    onListSelectionChange: function (list, selectedRecords) {
        var me = this,
            isMulti = me.multiSelect,
            hasRecords = selectedRecords.length > 0;
        // Only react to selection if it is not called from setValue, and if our list is
        // expanded (ignores changes to the selection model triggered elsewhere)
        if (!me.ignoreSelection && me.isExpanded) {
            if (!isMulti) {
                Ext.defer(me.collapse, 1, me);
            }
            /*
             * Only set the value here if we're in multi selection mode or we have
             * a selection. Otherwise setValue will be called with an empty value
             * which will cause the change event to fire twice.
             */
            if (isMulti || hasRecords) {
                me.setValue(selectedRecords, false);
            }
            if (hasRecords) {
                me.fireEvent('select', me, selectedRecords);
            }
            me.inputEl.focus();
        }
        // console.log(me.getValue());
    },

    doAutoSelect: function () {
        var me = this,
            picker = me.picker,
            lastSelected, itemNode;
        if (picker && me.autoSelect && me.store.getCount() > 0) {
            // Highlight the last selected item and scroll it into view
            lastSelected = picker.getSelectionModel().lastSelected;
            itemNode = picker.view.getNode(lastSelected || 0);
            if (itemNode) {
                picker.view.highlightItem(itemNode);
                picker.view.el.scrollChildIntoView(itemNode, false);
            }
        }
    }


});