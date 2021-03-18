package com.atguigu.gulimall.search.vo;

import lombok.Data;

import java.util.List;

@Data
public class SearchParam {
    private String keyword; //页面传递的全文匹配
    private Long catalog3Id;
    /**
     * sort = saleCount_asc/desc
     * sort = skuPrice_asc/desc
     * sort = hotScore_asc/desc
     */
    private String sort;
    /**
     * hasStock,skuPrice区间,brandId,catalog3ID,attrs
     */
    private Integer hasStock;//是否有货
    private String skuPrice;//价格区间
    private List<Long> brandId;//按照品牌多查
    private List<String> attrs;//按属性查询
    private Integer pageNum = 1;//页码

    private String _queryString;//原生的所有查询条件
}
