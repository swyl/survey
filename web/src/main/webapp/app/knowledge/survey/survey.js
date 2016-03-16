/**
 * 调查问卷
 * Created by Michael on 2015/3/27.
 */
(function (angular) {
    var app = angular.module("eccrm.knowledge.survey", [
        'eccrm.angular',
        'eccrm.angularstrap',
        'eccrm.base.param'
    ]);

    /**
     * 调查问卷
     */
    app.service('SurveyService', function ($resource, CommonUtils) {

        return $resource(CommonUtils.contextPathURL('/survey/:method'), {}, {

            // 保存
            save: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 更新
            update: {method: 'POST', params: {method: 'update'}, isArray: false},

            // 发布
            // 必须参数：
            //  id：
            publish: {method: 'POST', params: {method: 'publish', id: '@id'}, isArray: false},

            // 根据id查询新增调查问卷信息
            get: {method: 'GET', params: {id: '@id', method: 'get'}, isArray: false},

            //分页查询，返回{total:,data:[{},{}]}
            pageQuery: {
                method: 'POST',
                params: {method: 'pageQuery', limit: '@limit', start: '@start'},
                isArray: false
            },

            // 根据id字符串（使用逗号分隔多个值）删除
            // 必须参数：
            //  ids：使用逗号分隔的多个id字符串
            deleteByIds: {method: 'DELETE', params: {method: 'delete', ids: '@ids'}, isArray: false},

            // -------------------------------------- 关联题目操作 --------------------

            // 添加题目
            // 必须参数：
            //  surveyId：
            //  subjectIds:题目的id列表，多个id使用逗号进行分隔
            addSubjects: {method: 'POST', params: {method: 'addSubjects'}, isArray: false},

            // 查询指定问卷的所有题目
            // 必须参数：
            //  surveyId：问卷id
            querySubjects: {method: 'POST', params: {method: 'querySubjects', surveyId: '@surveyId'}, isArray: false},

            // 查询指定问卷的所有题目，并同时返回题目的所有选项
            // 必须参数
            //  surveyId：问卷ID
            querySubjectWithItems: {
                method: 'GET',
                params: {method: 'querySubjectWithItems', surveyId: '@surveyId'},
                isArray: false
            },

            // 给指定问卷的指定题目设置为第一题
            // 必须参数：
            //  surveyId:问卷ID
            //  subjectId:题目ID
            setToFirst: {
                method: 'POST',
                params: {method: 'setToFirst', surveyId: '@surveyId', subjectId: '@subjectId'},
                isArray: false
            },

            // 更新题目在问卷的序号
            // 接收参数：{id：'问卷题目id',sequenceNo:1}
            updateSubjectSequence: {
                method: 'POST',
                params: {method: 'updateSubjectSequence'},
                isArray: false
            },
            // 移除题目
            // 必须参数：
            //  surveyId：问卷ID
            //  subjectIds：题目ID（多个值使用逗号进行分隔）
            deleteSubjects: {
                method: 'DELETE',
                params: {method: 'deleteSubjects', surveyId: '@surveyId', subjectIds: '@subjectIds'},
                isArray: false
            }


        })
    });

    /**
     * 用户答题结果
     */
    app.service('SurveyAnswerService', function ($resource, CommonUtils) {

        return $resource(CommonUtils.contextPathURL('/survey/answer/:method'), {}, {

            // 批量保存
            batchSave: {method: 'POST', params: {method: 'save'}, isArray: false},

            // 查询用户的答题结果
            // 必须参数：
            //  surveyId：问卷ID
            //  businessId：业务ID
            //  userId：答题人ID
            queryResult: {
                method: 'GET',
                params: {method: 'result', surveyId: '@surveyId', businessId: '@businessId', userId: '@userId'},
                isArray: false
            },
            // 查询指定问卷的所有答题
            // 必须参数：
            //  surveyId：问卷ID
            queryBySurveyId: {method: 'GET', params: {method: 'queryBySurveyId', surveyId: '@surveyId'}, isArray: false}

        })
    });

    /**
     * 提供参数的接口
     */
    app.service('Survey', function (ParameterLoader) {
        return {
            status: function (callback) {
                ParameterLoader.loadSysParam('SP_SURVEY_STATUS', callback, '加载数据字典...');
            }
        };
    });


    /**
     * 提供弹出层服务
     */
    app.service('SurveyModal', function ($modal, CommonUtils, ModalFactory, AlertFactory, SurveyService) {
        return {
            /**
             * 单选问卷
             * 配置项
             *  callback:推荐项,function，选择一个问卷后的回调，该方法接收一个参数，为被选择的问卷对象
             * @param options
             */
            pickOne: function (options) {
                var modal = $modal({
                    template: CommonUtils.contextPathURL('/app/knowledge/survey/template/survey-pickone-modal.html')
                });
                var $scope = modal.$scope;

                $scope.query = function () {
                    $scope.pager.query();
                };

                // 预览
                $scope.preview = function (id) {
                    CommonUtils.addTab({
                        title: '预览问卷',
                        url: "/survey/preview?id=" + id
                    });
                };

                // 选定
                $scope.confirm = function () {
                    var callback = options && options.callback;
                    if (angular.isFunction(callback)) {
                        callback($scope.selected);
                    }
                    $scope.$hide();
                };

                // 初始化分页插件
                $scope.pager = {
                    fetch: function () {
                        // 只查询已发布的问卷
                        var param = angular.extend({status: 'PUBLISHED'}, $scope.condition, {
                            start: this.start,
                            limit: this.limit
                        });
                        return CommonUtils.promise(function (defer) {
                            var promise = SurveyService.pageQuery(param);
                            CommonUtils.loading(promise, '加载中...', function (data) {
                                data = data.data || {total: 0};
                                defer.resolve(data);
                                $scope.beans = data;
                            });
                        });
                    }
                };

                /**
                 * 初始化
                 */
                var init = function () {

                };
                ModalFactory.afterShown(modal, init);
            }
        }
    });
})(angular);