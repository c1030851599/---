package weibo.mapper;

import weibo.pojo.User;
import weibo.pojo.weibo;
import weibo.pojo.weiboCustom;
import weibo.pojo.zfweibo;

import java.util.List;

public interface weiboMapper {
    int deleteByPrimaryKey(Integer weiboId);

    int insert(weibo record);

    int insertSelective(weibo record);

    int updateByPrimaryKeySelective(weibo record);

    int updateByPrimaryKey(weibo record);

    List<weiboCustom> queryAllWeibo();

    List<weiboCustom> queryAllWeiboByImg();

    List<weiboCustom> queryAllWeiboByVideo();

    List<weiboCustom> queryAllWeiboByMusic();

    List<weiboCustom> queryAllWeiboByArticle();

    List<weiboCustom> queryMyWeibo(User user);

    List<weiboCustom> queryCollectWeibo(User user);

    void updateZanByPrimaryKey(weibo record);

    weiboCustom queryWeiboByID(String id);

    zfweibo queryweiboById(String id);

    List<weiboCustom> queryByKey(String content);

    List<weiboCustom> queryAllWeiboByImgKey(String content);

    List<weiboCustom> queryAllWeiboByVideoKey(String content);

    List<weiboCustom> queryAllWeiboByMusicKey(String content);

    List<weiboCustom> queryAllWeiboByArticleKey(String content);

    Integer getUserIDByWeiboID(String weiboid);

}