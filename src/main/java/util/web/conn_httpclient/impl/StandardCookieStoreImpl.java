package util.web.conn_httpclient.impl;

import java.util.Date;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

public class StandardCookieStoreImpl extends BasicCookieStore {
	    /**
	 * 
	 */
	private static final long serialVersionUID = 5626776543783089852L;

		/**
	     * Removes all of {@link Cookie cookies} in this HTTP state
	     * that have expired by the specified {@link java.util.Date date}.
	     *
	     * @return true if any cookies were purged.
	     *
	     * @see Cookie#isExpired(Date)
	     */
	    @Override
	    public boolean clearExpired(final Date date) {
	    	super.getCookies().forEach(System.out::println);
	        boolean res=super.clearExpired(date);
	        super.getCookies().forEach(System.out::println);
	        return res;
	    }

	    /**
	     * Clears all cookies.
	     */
	    @Override
	    public void clear() {
	    	System.out.println("cookie clear...");
	       super.clear();
	    }
}
