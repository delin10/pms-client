package util.web.conn_httpclient.impl;

import java.util.Date;

import org.apache.http.impl.cookie.BasicClientCookie;

public class StandardCookieImpl extends BasicClientCookie{

	/**
	 * 
	 */
	private static final long serialVersionUID = -649689239473795837L;


	public StandardCookieImpl(String name, String value) {
		super(name, value);
		// TODO Auto-generated constructor stub
	}

    /**
     * Sets expiration date.
     * <p><strong>Note:</strong> the object returned by this method is considered
     * immutable. Changing it (e.g. using setTime()) could result in undefined
     * behaviour. Do so at your peril.</p>
     *
     * @param expiryDate the {@link Date} after which this cookie is no longer valid.
     *
     * @see #getExpiryDate
     *
     */
    @Override
    public void setExpiryDate (final Date expiryDate) {
        super.setExpiryDate(expiryDate);
        System.out.println(expiryDate);
    }


    /**
     * Returns {@code false} if the cookie should be discarded at the end
     * of the "session"; {@code true} otherwise.
     *
     * @return {@code false} if the cookie should be discarded at the end
     *         of the "session"; {@code true} otherwise
     */
    @Override
    public boolean isPersistent() {
    	System.out.println("persistent="+super.isPersistent());
        return super.isPersistent();
    }
}
