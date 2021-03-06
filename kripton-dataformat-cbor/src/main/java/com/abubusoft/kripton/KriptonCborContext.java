package com.abubusoft.kripton;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;

/**
 * @author Francesco Benincasa (info@abubusoft.com)
 *
 */
public class KriptonCborContext extends AbstractJacksonContext {

	@Override
	public BinderType getSupportedFormat()
	{
		return BinderType.CBOR;
	}
	
	@Override
	protected JsonFactory createInnerFactory()
	{
		return new CBORFactory();
	}
}
