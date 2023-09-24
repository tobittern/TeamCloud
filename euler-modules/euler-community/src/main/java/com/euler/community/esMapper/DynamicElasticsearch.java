package com.euler.community.esMapper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.config.CommonCommunityConfig;
import com.euler.community.domain.dto.IndexDto;
import com.euler.community.domain.dto.ShieldPageDto;
import com.euler.community.domain.entity.Dynamic;
import com.euler.community.domain.entity.DynamicEs;
import com.euler.community.domain.entity.DynamicFrontEs;
import com.euler.community.domain.entity.DynamicOperationErrorLog;
import com.euler.community.domain.vo.ShieldVo;
import com.euler.community.enums.DynamicOperationEnum;
import com.euler.community.enums.DynamicStatusEnum;
import com.euler.community.mapper.DynamicMapper;
import com.euler.community.mapper.DynamicOperationErrorLogMapper;
import com.euler.community.mapper.DynamicTopicMapper;
import com.euler.community.service.IShieldService;
import com.euler.community.service.IUserBehaviorService;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.MemberProfile;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DynamicElasticsearch {

    private static String indexName = "dynamic";

    @Autowired
    private DynamicMapper dynamicMapper;
    @Autowired
    private DynamicOperationErrorLogMapper dynamicOperationErrorLogMapper;
    @DubboReference
    private RemoteMemberService remoteMemberService;
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private DynamicTopicMapper dynamicTopicMapper;
    @Autowired
    private CommonCommunityConfig commonCommunityConfig;
    @Autowired
    private IUserBehaviorService iUserBehaviorService;
    @Autowired
    private IShieldService shieldService;

    /**
     * 获取动态的详细信息
     *
     * @param insertId 动态ID
     * @return
     */
    private DynamicEs getDynamicInfo(Long insertId) {
        // 首先查询出当前数据的详细信息
        Dynamic dynamic = dynamicMapper.selectOne(Wrappers.<Dynamic>lambdaQuery().eq(Dynamic::getId, insertId));
        if (dynamic == null) {
            return new DynamicEs();
        }
        // 进行对象的拷贝操作
        DynamicEs dynamicEs = BeanCopyUtils.copy(dynamic, DynamicEs.class);
        // 第一步 数据拷贝完之后 我们需要修改一些字段 时间和状态
        dynamicEs.setCreateTime(dynamic.getCreateTime() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dynamic.getCreateTime()) : null);
        dynamicEs.setUpdateTime(dynamic.getUpdateTime() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dynamic.getUpdateTime()) : null);
        dynamicEs.setCreateTimeOrder(DateUtil.format(dynamic.getCreateTime(), "yyyyMMdd"));
        dynamicEs.setContent(HtmlUtil.cleanHtmlTag(dynamic.getContent()));
        // 设置又拍云域名前缀
        if (dynamic.getCover() != null && StringUtils.isNotBlank(dynamic.getCover())) {
            dynamicEs.setCover(commonCommunityConfig.getYunDomain() + dynamic.getCover());
        }
        if (dynamic.getResourceUrl() != null && StringUtils.isNotBlank(dynamic.getResourceUrl())) {
            String[] split = dynamic.getResourceUrl().split(",");
            List<String> tempResource = new ArrayList<>();
            for (String s : split) {
                tempResource.add(commonCommunityConfig.getYunDomain() + s);
            }
            String join = StringUtils.join(tempResource, ",");
            dynamicEs.setResourceUrl(join);
        }
        // 量的 一些东西使用虚拟的
        dynamicEs.setForwardNum(dynamic.getVirtualForwardNum());
        dynamicEs.setPraiseNum(dynamic.getVirtualPraiseNum());
        dynamicEs.setCommentNum(dynamic.getVirtualCommentNum());
        dynamicEs.setCollectNum(dynamic.getVirtualCollectNum());
        dynamicEs.setHitNum(dynamic.getVirtualHitNum());
        // 第二步 获取一下当前用户的基础数据直接存放到es中
        MemberProfile memberByUserId = remoteMemberService.getMemberByUserId(dynamic.getMemberId());
        dynamicEs.setNickname(memberByUserId.getNickName());
        dynamicEs.setAvatar(iUserBehaviorService.getAvatar(memberByUserId.getSex(), memberByUserId.getAvatar()));
        // 获取专题的基础信息
        QueryWrapper<IdNameDto<Long>> orderWrapper = Wrappers.query();
        // 这个结束时间可能需要获取当前时间
        orderWrapper.eq("dt.dynamic_id", dynamic.getId())
            .eq("t.status", 1);
        List<IdNameDto<Long>> topicList = dynamicTopicMapper.selectTopicNameByDynamicId(orderWrapper);
        if (topicList.size() > 0) {
            List<String> collect = topicList.stream().map(IdNameDto::getName).collect(Collectors.toList());
            String topicName = StringUtils.join(collect, ",");
            dynamicEs.setTopic(topicName);
        }
        return dynamicEs;
    }


    /**
     * 数据入库
     */
    public Boolean insertDataToElasticSearch(Long insertId) throws Exception {
        // 查询出当前动态的信息信息
        DynamicEs dynamicInfo = getDynamicInfo(insertId);
        if (dynamicInfo.getId() == null) {
            return false;
        }
        log.info("入ES前的数据:{}", JsonUtils.toJsonString(dynamicInfo));
        try {
            // 这个地方需要判断一下当前执行的是入库操作还是更新操作 可能存在一个动态 先审核成功 之后失败 之后再次审核成功的情况
            Boolean found = existsDocument(dynamicInfo.getId().toString());
            log.info("数据查询是否存在的结果:{}", found);
            if (!found) {
                // 创建添加文档的请求
                return createDocument(dynamicInfo);
            } else {
                // 存在 我们需要执行的是更新操作
                return updateDocument(dynamicInfo);
            }
        } catch (Exception e) {
            log.error("数据添加到Es的时候报错了,ID:{}", insertId, e);
        }
        return false;
    }

    /**
     * 操作更新入Es
     */
    public Boolean operationDataToElasticSearch(IdNameTypeDicDto dto) throws Exception {
        try {
            DynamicOperationEnum.find(dto.getType());
        } catch (RuntimeException runtimeException) {
            return false;
        }
        // 判断进行筛选的类型是否正确
        Long needUpdateId = Convert.toLong(dto.getId());
        // 查询出当前动态的信息信息
        DynamicEs dynamicEs = getDynamicInfo(needUpdateId);
        // 判断获取数据是否为空
        if (dynamicEs.getId() == null) {
            log.info("添加或者更新动态ID：{}的时候 动态为空", dto.getId());
            return false;
        }
        // 根据操作类型修改指定字段的值
        if (dto.getType().equals(DynamicOperationEnum.AI_FAIL.getCode())) {
            // AI审核拒绝
            dynamicEs.setStatus(DynamicStatusEnum.AI_FAIL.getCode());
        } else if (dto.getType().equals(DynamicOperationEnum.PEOPLE_FAIL.getCode())) {
            // 人工审核拒绝
            dynamicEs.setStatus(DynamicStatusEnum.PEOPLE_FAIL.getCode());
        } else if (dto.getType().equals(DynamicOperationEnum.ONLINE.getCode())) {
            // 上线
            dynamicEs.setIsUp("0");
            dynamicEs.setStatus(DynamicStatusEnum.RELEASE.getCode());
            dynamicEs.setOnlineTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } else if (dto.getType().equals(DynamicOperationEnum.DOWN.getCode())) {
            // 下线
            dynamicEs.setIsUp("1");
            dynamicEs.setStatus(DynamicStatusEnum.DOWN.getCode());
        } else if (dto.getType().equals(DynamicOperationEnum.DEL.getCode())) {
            // 删除
            dynamicEs.setDelFlag("2");
        } else if (dto.getType().equals(DynamicOperationEnum.RECOVERY.getCode())) {
            // 恢复删除
            dynamicEs.setDelFlag("0");
        } else if (dto.getType().equals(DynamicOperationEnum.ALL_SEE_ME.getCode())) {
            // 全部可见
            dynamicEs.setIsOnlyMeSee("0");
        } else if (dto.getType().equals(DynamicOperationEnum.ONLY_SEE_ME.getCode())) {
            // 仅我可见
            dynamicEs.setIsOnlyMeSee("1");
        } else if (dto.getType().equals(DynamicOperationEnum.CAN_FAV.getCode())) {
            // 可以点赞
            dynamicEs.setIsCanFav("0");
        } else if (dto.getType().equals(DynamicOperationEnum.NO_CAN_FAV.getCode())) {
            // 不能点赞
            dynamicEs.setIsCanFav("1");
        } else if (dto.getType().equals(DynamicOperationEnum.TOP.getCode())) {
            // 置顶
            dynamicEs.setIsTop("1");
        } else if (dto.getType().equals(DynamicOperationEnum.NO_TOP.getCode())) {
            // 取消置顶
            dynamicEs.setIsTop("0");
        }
        try {
            // 这个地方需要判断一下当前执行的是入库操作还是更新操作 可能存在一个动态 先审核成功 之后失败 之后再次审核成功的情况
            Boolean found = existsDocument(needUpdateId.toString());
            if (found) {
                // 存在在进行更新
                return updateDocument(dynamicEs);
            }
        } catch (Exception e) {
            log.error("进行状态更新的时候 es报错了", e);
            //  出现异常的时候
            DynamicOperationErrorLog dynamicOperationErrorLog = new DynamicOperationErrorLog();
            dynamicOperationErrorLog.setDynamicId(needUpdateId);
            dynamicOperationErrorLog.setOperationType("B-" + dto.getType());
            dynamicOperationErrorLog.setErrorContent(e.toString());
            dynamicOperationErrorLogMapper.insert(dynamicOperationErrorLog);
        }
        return false;
    }

    /**
     * 创建文档
     *
     * @param insertDocument 创建的文档对象
     * @return
     * @throws IOException
     */
    private Boolean createDocument(DynamicEs insertDocument) throws IOException {
        // 创建添加文档的请求
        IndexRequest indexRequest = new IndexRequest(indexName);
        // 设置ID
        indexRequest.id(insertDocument.getId().toString());
        // 将我们的数据放入请求 json
        String json = JsonUtils.toJsonString(insertDocument);

        indexRequest.source(json, XContentType.JSON);
        // 客户端发送请求
        IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

        log.info("添加到es成功，index:{}, indexStatus：{}", index, index.status());
        return true;
    }

    /**
     * 判断一个所以是否存在
     *
     * @throws IOException
     */
    private Boolean existsIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        return exists;
    }

    /**
     * DELETE /book/_doc/1
     * 删除文档信息
     *
     * @throws IOException
     */
    public Boolean deleteDocument(String searchId) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        log.info("从es里面删除动态成功, 删除信息:{}", delete);
        return true;
    }

    /**
     * 更新文档
     * post /index/_doc/1/_update
     *
     * @param updateDocument
     * @throws IOException
     */
    public Boolean updateDocument(DynamicEs updateDocument) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(indexName, updateDocument.getId().toString());
        // 修改内容
        String json = JsonUtils.toJsonString(updateDocument);
        updateRequest.doc(json, XContentType.JSON);
        // 执行更新操作
        UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        log.info("更新es数据成功：{}", update);
        return true;
    }


    /**
     * 更新文档指定字段
     * post /index/_doc/1/_update
     *
     * @param updateDocument
     * @throws IOException
     */
    public Boolean updateAppointDocument(Long id, HashMap<String, Object> updateDocument) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(indexName, id.toString());
        // 修改内容
        String json = JsonUtils.toJsonString(updateDocument);
        updateRequest.doc(json, XContentType.JSON);
        // 执行更新操作
        UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        log.info("更新es指定字段数据成功：{}", update);
        return true;
    }


    /**
     * 判断指定文档是否存在
     *
     * @throws IOException
     */
    public Boolean existsDocument(String searchId) throws IOException {
        String[] includes = {"id", "memberId"}; // 需要返回那些字段
        String[] excludes = {}; // 排除那些字段
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        GetRequest getRequest = new GetRequest(indexName, searchId).fetchSourceContext(fetchSourceContext);
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        DynamicEs dynamicEs = JsonUtils.parseObject(getResponse.getSourceAsString(), DynamicEs.class);
        if (dynamicEs != null) {
            return true;
        }
        return false;
    }


    /**
     * 获取文档
     * get /index/_doc/1
     *
     * @throws IOException
     */
    public DynamicFrontEs getDocument(String searchId) throws IOException {
        GetRequest getRequest = new GetRequest(indexName, searchId);
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        DynamicFrontEs dynamicFrontEs = JsonUtils.parseObject(getResponse.getSourceAsString(), DynamicFrontEs.class);
        return dynamicFrontEs;
    }

    /**
     * 分页获取文档
     */
    public TableDataInfo<DynamicFrontEs> getDocumentByPage(IndexDto indexDto) throws IOException {
//        log.info("查询的条件是：{}", JsonUtils.toJsonString(indexDto));
        // 手动设置分页 适用于ES
        if (indexDto.getPageNum() == null || indexDto.getPageNum().equals(1)) {
            indexDto.setPageNum(0);
        } else {
            int i = (indexDto.getPageNum() - 1) * indexDto.getPageSize();
            indexDto.setPageNum(i);
        }
        if (indexDto.getPageSize() == null) {
            indexDto.setPageSize(10);
        }
        SearchRequest searchRequest = new SearchRequest(indexName);
        // 使用搜索条件构造器，构造搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // ###### 查询条件，使用QueryBuilders 工具类，来实现
        // 设置必要条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("delFlag", "0"));
        // 首页搜索的时候需要设置状态
        if (indexDto.getPosition().equals(1) || indexDto.getPosition().equals(2) || indexDto.getPosition().equals(6)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("status", DynamicStatusEnum.RELEASE.getCode()))
                .must(QueryBuilders.termQuery("isUp", "0"));
            // 视频动态列表需要设置类型
            if (indexDto.getPosition().equals(6)) {
                boolQueryBuilder.must(QueryBuilders.termQuery("type", 1));
                // 判断是否存在需要剔除的动态ID
                if (indexDto.getExcludeDynamicIds() != null && indexDto.getExcludeDynamicIds().size() > 0) {
                    boolQueryBuilder.mustNot(QueryBuilders.termsQuery("id", indexDto.getExcludeDynamicIds()));
                }
            }
            // 判断是否登录
            if (LoginHelper.isLogin()) {
                //获取屏蔽用户列表
                ShieldPageDto pageDt = new ShieldPageDto();
                pageDt.setMemberId(LoginHelper.getUserId());
                var shieldVos = shieldService.queryList(pageDt);
                if (shieldVos != null && !shieldVos.isEmpty()) {

                    var memberList = shieldVos.stream().filter(a -> a.getBusinessType() == 1).map(ShieldVo::getBusinessId).collect(Collectors.toList());
                    var dyList = shieldVos.stream().filter(a -> a.getBusinessType() == 2).map(ShieldVo::getBusinessId).collect(Collectors.toList());
                    if (memberList != null && !memberList.isEmpty()) {
                        boolQueryBuilder.mustNot(QueryBuilders.termsQuery("memberId", memberList));
                    }
                    if (dyList != null && !dyList.isEmpty()) {
                        boolQueryBuilder.mustNot(QueryBuilders.termsQuery("id", dyList));
                    }
                }
            }
        }
        // 设置查询获取一批指定用户的数据
        if (indexDto.getPosition().equals(2) && indexDto.getSearchMemberIds() != null) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("memberId", indexDto.getSearchMemberIds()));
        }
        // 设置用户ID
        if (indexDto.getPosition().equals(3) && indexDto.getMemberId() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("memberId", indexDto.getMemberId()));
            // 用户的动态列表需要判断如果查询的动态是本人的需要展示出全部的
            if (LoginHelper.isLogin()) {
                if (!LoginHelper.getUserId().equals(indexDto.getMemberId())) {
                    boolQueryBuilder.must(QueryBuilders.termQuery("status", DynamicStatusEnum.RELEASE.getCode()));
                }
            }
        }
        if ((indexDto.getPosition().equals(4) || indexDto.getPosition().equals(5)) && indexDto.getSearchDynamicIds() != null) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("id", indexDto.getSearchDynamicIds()));
        }
        // 按照关键词进行搜索的条件
        if (StringUtils.isNotBlank(indexDto.getKeyword())) {
            if (indexDto.getKeyword().startsWith("#")) {
                boolQueryBuilder.must(QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("topic", indexDto.getKeyword()).minimumShouldMatch("100%")));
            } else {
                boolQueryBuilder.must(QueryBuilders.boolQuery()
                    .should(QueryBuilders.matchQuery("topic", indexDto.getKeyword()).minimumShouldMatch("100%"))
                    .should(QueryBuilders.matchQuery("title", indexDto.getKeyword()).minimumShouldMatch("100%"))
                    .should(QueryBuilders.matchQuery("content", indexDto.getKeyword()).minimumShouldMatch("100%")));
            }
        }
//        log.info("boolQueryBuilder：{}", boolQueryBuilder.toString());
        // 存放到query中
        sourceBuilder.query(boolQueryBuilder);
        // 分页
        sourceBuilder.from(indexDto.getPageNum()); // 默认 0
        sourceBuilder.size(indexDto.getPageSize()); // 默认 10
        // 排序
        sourceBuilder.sort("isTop", SortOrder.DESC);
        sourceBuilder.sort("createTime", SortOrder.DESC);
        searchRequest.source(sourceBuilder);
//        log.info("searchRequest: {}", searchRequest);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 数据获取完毕之后进行拼接
        TableDataInfo<DynamicFrontEs> returnTableList = new TableDataInfo<>();
        returnTableList.setTotal(Convert.toLong(search.getHits().getTotalHits()));
        returnTableList.setCode(200);
        returnTableList.setMsg("数据获取成功");
        List<DynamicFrontEs> tempList = new ArrayList<>();
        if (search.getHits().getHits() != null) {
            for (SearchHit hits : search.getHits().getHits()) {
                DynamicFrontEs dynamicFrontEs = JsonUtils.parseObject(hits.getSourceAsString(), DynamicFrontEs.class);
                tempList.add(dynamicFrontEs);
            }
        }
        returnTableList.setRows(tempList);
        return returnTableList;
    }


}
