/*
 * Tencent is pleased to support the open source community by making BK-JOB蓝鲸智云作业平台 available.
 *
 * Copyright (C) 2021 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * BK-JOB蓝鲸智云作业平台 is licensed under the MIT License.
 *
 * License for BK-JOB蓝鲸智云作业平台:
 * --------------------------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package com.tencent.bk.job.manage.api.web;

import com.tencent.bk.job.common.model.ServiceResponse;
import com.tencent.bk.job.manage.model.web.request.TagCreateReq;
import com.tencent.bk.job.manage.model.web.vo.TagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签管理API-前端调用
 *
 * @date 2019/10/10
 */
@Api(tags = {"job-manage:web:Tag_Management"})
@RequestMapping("/web/tag/app/{appId}")
@RestController
public interface WebTagResource {

    @ApiOperation(value = "根据条件获取业务下的所有标签", produces = "application/json")
    @GetMapping("/tag/list")
    ServiceResponse<List<TagVO>> listTags(
        @ApiParam("用户名，网关自动传入") @RequestHeader("username") String username,
        @ApiParam(value = "业务 ID", required = true, example = "2") @PathVariable(
            "appId") Long appId,
        @ApiParam("标签名称") @RequestParam(value = "tagName", required = false) String tagName);

    @ApiOperation(value = "更新标签名称", produces = "application/json")
    @PutMapping("/tag/{tagId}")
    ServiceResponse<Boolean> updateTagInfo(
        @ApiParam("用户名，网关自动传入") @RequestHeader("username") String username,
        @ApiParam(value = "业务 ID", required = true, example = "2") @PathVariable(
            "appId") Long appId,
        @ApiParam(value = "标签 ID", required = true) @PathVariable("tagId") Long tagId,
        @ApiParam(value = "标签名称", required = true) @RequestParam(value = "tagName") String tagName);

    @ApiOperation(value = "创建标签", produces = "application/json")
    @PostMapping("/tag")
    ServiceResponse<Long> saveTagInfo(
        @ApiParam("用户名，网关自动传入") @RequestHeader("username") String username,
        @ApiParam(value = "业务 ID", required = true, example = "2") @PathVariable("appId") Long appId,
        @ApiParam("标签名称请求体") @RequestBody TagCreateReq tagCreateReq);
}