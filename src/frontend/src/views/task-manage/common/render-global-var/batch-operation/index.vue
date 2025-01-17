<template>
    <div class="global-bariable-batch-operation">
        <table>
            <thead>
                <tr>
                    <th style="width: 130px;">{{ $t('template.变量类型') }}<span class="require-flag" /></th>
                    <th>{{ $t('template.变量名称') }}<span class="require-flag" /></th>
                    <th>
                        <span v-bk-tooltips="$t('template.请输入变量的初始值 [可选]')" class="hover-tips">
                            {{ $t('template.初始值') }}
                        </span>
                    </th>
                    <th style="width: 320px;">{{ $t('template.变量描述') }}</th>
                    <th style="width: 80px;">
                        <span v-bk-tooltips="$t('template.变量的值在执行中可变')" class="hover-tips">
                            {{ $t('template.赋值可变') }}
                        </span>
                    </th>
                    <th style="width: 100px;">{{ $t('template.执行时必填') }}</th>
                    <th style="width: 80px;">{{ $t('template.操作') }}</th>
                </tr>
            </thead>
            <template v-for="(variableItem, index) in variableList">
                <render-table-row
                    v-if="variableItem.id > 0 && variableItem.delete !== 1"
                    ref="variableEdit"
                    :list="calcExcludeList(index)"
                    :data="variableItem"
                    :key="variableItem.id"
                    @on-change="value => handleChange(index, value)"
                    @on-delete="handleDelete(index)"
                    @on-append="handleAppendVariable(index)" />
                <create-table-row
                    v-else-if="variableItem.id < 0"
                    ref="variableCreate"
                    :key="variableItem.id"
                    :list="calcExcludeList(index)"
                    :data="variableItem"
                    @on-change="value => handleChange(index, value)"
                    @on-delete="handleDelete(index)"
                    @on-append="handleAppendVariable(index)" />
            </template>
        </table>
    </div>
</template>
<script>
    import _ from 'lodash';
    import GlobalVariableModel from '@model/task/global-variable';
    import RenderTableRow from './render-table-row.vue';
    import CreateTableRow from './create-table-row.vue';
    import { createVariable } from '../util';

    export default {
        name: '',
        components: {
            RenderTableRow,
            CreateTableRow,
        },
        props: {
            variable: {
                type: Array,
                default: () => [],
            },
        },
        data () {
            return {
                variableList: _.cloneDeep(this.variable),
            };
        },
        created () {
            if (this.variable.length < 1) {
                this.variableList.push(createVariable());
            }
        },
        methods: {
            /**
             * @desc 不包含当前索引变量的变量列表
             * @returns { Array }
             */
            calcExcludeList (index) {
                const list = [...this.variableList];
                list.splice(index, 1);
                return list;
            },
            /**
             * @desc 更新变量信息
             * @param {Number} index 编辑的变量索引
             * @param {Object} variableData 全局变量数据
             */
            handleChange (index, variableData) {
                const variableList = [...this.variableList];
                const variable = new GlobalVariableModel(variableData);
                variableList.splice(index, 1, variable);
                this.variableList = variableList;
            },
            /**
             * @desc 删除指定索引的变量
             * @param {Number} index 编辑的变量索引
             */
            handleDelete (index) {
                const variableList = [...this.variableList];
                const editVariable = variableList[index];
                if (editVariable.id > 0) {
                    // 删除已存在的变量——设置delete
                    editVariable.delete = 1;
                } else {
                    // 删除新建的变量——直接删除
                    variableList.splice(index, 1);
                }
                this.variableList = variableList;
            },
            /**
             * @desc 在指定索引位置添加一个新变量
             * @param {Number} index 编辑的变量索引
             */
            handleAppendVariable (index) {
                this.variableList.splice(index + 1, 0, createVariable());
            },
            /**
             * @desc 提交编辑
             * @returns {Promise}
             */
            submit () {
                const queue = [];
                if (this.$refs.variableEdit) {
                    queue.push(...this.$refs.variableEdit.map(item => item.validate()));
                }
                if (this.$refs.variableCreate) {
                    queue.push(...this.$refs.variableCreate.map(item => item.validate()));
                }
                return Promise.all(queue)
                    .then(() => this.$emit('on-change', this.variableList));
            },
        },
    };
</script>
<style lang="postcss">
    .global-bariable-batch-operation {
        table {
            width: 100%;
            font-size: 12px;
            line-height: 18px;
            color: #63656e;
            border: 1px solid #dcdee5;
            border-radius: 2px;
            table-layout: fixed;

            thead {
                background: #fafbfd;
            }

            th,
            td {
                height: 41px;
                padding: 0 15px;
                text-align: left;
            }

            th {
                font-weight: normal;
                color: #313238;
            }

            td {
                padding-top: 5px;
                padding-bottom: 5px;
                border-top: 1px solid #dcdee5;
            }

            .hover-tips {
                padding-bottom: 2px;
                border-bottom: 1px dashed #c4c6cc;
            }

            .require-flag {
                &::after {
                    display: inline-block;
                    height: 8px;
                    font-size: 12px;
                    line-height: 1;
                    color: #ea3636;
                    vertical-align: middle;
                    content: "*";
                }
            }

            .action-row {
                user-select: none;

                .action-btn {
                    font-size: 18px;
                    color: #c4c6cc;
                    cursor: pointer;
                }
            }
        }
    }
</style>
