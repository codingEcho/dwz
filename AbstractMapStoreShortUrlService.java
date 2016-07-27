

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dao.IShortUrlDao;
import com.dao.impl.ShortUrlDao;
import com.model.ShortUrl;

public abstract class AbstractMapStoreShortUrlService implements ShortUrlService {
	private Logger logger = Logger.getLogger(this.getClass());

	// this two maps are for simulate a database table to store short-long
	// mapping
	/*
	 * private Map<String, String> longShortUrlMap = new
	 * ConcurrentHashMap<String, String>(); private Map<String, String>
	 * shortLongUrlMap = new ConcurrentHashMap<String, String>();
	 */
	private IShortUrlDao shortUrlDao = new ShortUrlDao();

	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	@Override
	public String lookupLong(String shortUrl) {
		String longUrl = null;
		Lock readLock = readWriteLock.readLock();
		try {
			readLock.lock();
			// longUrl = shortLongUrlMap.get(shortUrl);
			longUrl = shortUrlDao.findLongUrlByShortUrl(shortUrl);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			readLock.unlock();
		}
		return longUrl;
	}

	@Override
	public String convertShort(String longUrl) {
		String shortUrl = "";
		try {
			shortUrl = lookupShort(longUrl);
			if (StringUtils.isEmpty(shortUrl)) {
				shortUrl = shorten(longUrl);
				if (StringUtils.isEmpty(shortUrl)) {
					System.err.println("Cannot convert long url to short url");
				} else {
					synchronizedResult(longUrl, shortUrl);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return new StringBuilder(DOMAIN_PREFIX).append(shortUrl).toString();
	}

	protected String lookupShort(String longUrl) {
		String shortUrl = null;
		Lock readLock = readWriteLock.readLock();
		try {
			readLock.lock();
			// shortUrl = longShortUrlMap.get(longUrl);
			shortUrl = shortUrlDao.findShortUrlByLongUrl(longUrl);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			readLock.unlock();
		}
		return shortUrl;
	}

	protected void synchronizedResult(String longUrl, String shortUrl) {
		Lock writeLock = readWriteLock.writeLock();
		try {
			writeLock.lock();
			Boolean isOk = synchronizedResultToDB(longUrl, shortUrl);
			if (isOk) {
				logger.info("存取短网址成功");
			} else {
				logger.info("存取短网址失败");
			}
			// longShortUrlMap.put(longUrl, shortUrl);
			// shortLongUrlMap.put(shortUrl, longUrl);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			writeLock.unlock();
		}
	}

	protected Boolean synchronizedResultToDB(String longUrl, String shortUrl) {
		String sequence = shortUrl;
		// String iShortUrl = new
		// StringBuilder(DOMAIN_PREFIX).append(shortUrl).toString();
		ShortUrl shortUrlInstance = new ShortUrl(shortUrl, longUrl);
		return shortUrlDao.insertShortUrl(shortUrlInstance);
	}

	protected abstract String shorten(String longUrl);

}
