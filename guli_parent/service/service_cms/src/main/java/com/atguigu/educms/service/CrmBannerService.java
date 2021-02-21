package com.atguigu.educms.service;

import com.atguigu.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author xiaocai
 * @since 2021-01-28
 */
public interface CrmBannerService extends IService<CrmBanner> {

    List<CrmBanner> selectAllBanner();
    void pageBanner(Page<CrmBanner> pageParam, Object o);
    CrmBanner getBannerById(String id);
    void saveBanner(CrmBanner banner);
    void updateBannerById(CrmBanner banner);
    void removeBannerById(String id);

}
