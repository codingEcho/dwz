# dwz
基于Java的短网址服务

## 算法

算法来源于网络，请参考[这里](http://www.tuicool.com/articles/BjMFFzI)！

使用62进制递增序列

```java
 // 0表示从0开始自增，在实现时该数值取决于数据库中记录的总数
 private AtomicLong sequence = new AtomicLong(0);

  @Override
  protected String shorten(String longUrl) {
    long myseq = sequence.incrementAndGet();
    String shortUrl = to62RadixString(myseq);
    return shortUrl;
  }

  private String to62RadixString(long seq) {
    StringBuilder sBuilder = new StringBuilder();
    while (true) {
      int remainder = (int) (seq % 62);
      sBuilder.append(DIGITS[remainder]);
      seq = seq / 62;
      if (seq == 0) {
        break;
      }
    }
    return sBuilder.toString();
  }
```



## 持久化

使用MySQL数据库存储

```mysql
CREATE TABLE `hsh_dwz` (
  `ShortUrl` varchar(255) NOT NULL COMMENT '短链接',
  `LongUrl` text NOT NULL COMMENT '长链接',
  `CreateTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ShortUrl`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短网址'
```



## 处理多并发请求

短链接生成过程：

- 客户端请求（传入所要转化为短链接的URL）；
- 查询是否存在URL；
- 存在URL，返回对应的短链接（TinyURL）;
- 不存在URL，生成短链接（TinyURL），并存储到数据库并返回给客户端。

多个并发请求可能同时取到相同的统计值（数据库中短链接的数量），造成生成相同的短链接，从而导致写入DB时，由于主键（ShortUrl）重复而失败，当前解决的方法是采用同步锁，控制只有当当前请求执行完成之后，下一个请求才能执行。

```java
	// 获取单例
	private AbstractMapStoreShortUrlService shortUrlService = 		SequenceBaseShortUrlService.getInstance();

	/**
	 * 从站内获取短链接
	 * 
	 * @return
	 */
	public String getShortUrlFromLocal() {
		super.init();
		JSONObject jsonResult = null;
		Boolean isExe = true;
		if (Utility.isNullOrEmpty(url)) {
			jsonResult = ActionHelper.getFailResult("url参数不能为空");
			isExe = false;
		}
		if (isExe) {
			synchronized (shortUrlService) {
				String strShortUrl = "";
				jsonResult = ActionHelper.getDefaultSuccessResult();
				try {
					strShortUrl = shortUrlService.convertShort(url);
					jsonResult.put(VariableFactory.DATAKEY, strShortUrl);
				} catch (Exception e) {
					log.error(e.getMessage());
					jsonResult.put(VariableFactory.DATAKEY, strShortUrl);
				}
			}
		}
		
		setAttributeToSession(jsonResult);
		
		return SUCCESS;
	}
```
