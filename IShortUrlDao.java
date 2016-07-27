package com.dao;

import com.model.ShortUrl;

/**
 * 
 * 短网址
 * 
 * @author LUOMINQING
 *
 */
public interface IShortUrlDao {

	/**
	 * 记录短链接
	 * 
	 * @param shortUrl
	 * @return
	 */
	public abstract Boolean insertShortUrl(ShortUrl shortUrl);

	/**
	 * 根据短连接查找长链接
	 * 
	 * @param shortUrl
	 * @return
	 */
	public abstract String findLongUrlByShortUrl(String shortUrl);

	/**
	 * 根据长连接查找短链接
	 * 
	 * @param longUrl
	 * @return
	 */
	public abstract String findShortUrlByLongUrl(String longUrl);
	
	/**
	 * 获取长短链接
	 * @param shortUrl 短链接
	 * @return
	 */
	public abstract ShortUrl findUrlMapsByShortUrl(String shortUrl);

	/**
	 * 统计短网址数量
	 * @return
	 */
	public abstract int countShortUrl();

}
