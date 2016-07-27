
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.dao.IShortUrlDao;
import com.dao.impl.ShortUrlDao;

/**
 * 基于自增序列号的短链接
 * 
 * @author LUOMINQING
 *
 */

public class SequenceBaseShortUrlService extends AbstractMapStoreShortUrlService {

	// this sequence count be a real database sequence or others
	private Logger logger = Logger.getLogger(this.getClass());
	private AtomicLong sequence = null;
	private IShortUrlDao shortUrlDao = null;

	/**
	 * 私有的构造函数
	 */
	private SequenceBaseShortUrlService() {
		System.out.println("SequenceBaseShortUrlService构造函数被调用");
		shortUrlDao = new ShortUrlDao();
	}

	/**
	 * 返回单例
	 * @return
	 */
	public static SequenceBaseShortUrlService getInstance() {
		return SequenceBaseShortUrlServiceHolder.instance;
	}

	/**
	 * 静态内部类（实例化单例，单例持有者）
	 * @author LUOMINQING
	 *
	 */
	private static class SequenceBaseShortUrlServiceHolder {
		private static SequenceBaseShortUrlService instance = new SequenceBaseShortUrlService();
	}

	@Override
	protected String shorten(String longUrl) {
		int count = 0;
		try {
			count = shortUrlDao.countShortUrl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("DB Sequence count:" + count);
		sequence = new AtomicLong(count);
		long myseq = sequence.incrementAndGet();
		String shortUrl = to62RadixString(myseq);
		
		return shortUrl;
	}

	/**
	 * 
	 * @param seq
	 * @return
	 */
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

}
