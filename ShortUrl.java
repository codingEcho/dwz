package com.model;

/**
 * 短网址
 * 
 * @author LUOMINQING
 *
 */
public class ShortUrl {
	private String shortUrl;
	private String longUrl;
	private String createTime;

	/**
	 * 短网址构造函数
	 * 
	 * @param sequence
	 *            自增序列号
	 * @param shortUrl
	 *            生成的短网址
	 * @param longUrl
	 *            长网址
	 */
	public ShortUrl(String shortUrl, String longUrl) {
		super();
		this.shortUrl = shortUrl;
		this.longUrl = longUrl;
		//this.createTime = Utility.getDateTimeNow();
	}

	public ShortUrl() {
		super();
	}

	@Override
	public String toString() {
		return "ShortUrl [shortUrl=" + shortUrl + ", longUrl=" + longUrl + ", createTime="
				+ createTime + "]";
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
