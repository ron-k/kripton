package kripton74;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.abubusoft.kripton.BinderOptions;
import com.abubusoft.kripton.BinderWriter;
import com.abubusoft.kripton.KriptonBinder;
import com.abubusoft.kripton.binder2.BinderType;
import com.abubusoft.kripton.binder2.KriptonBinder2;
import com.abubusoft.kripton.binder2.context.PropertiesBinderContext;
import com.abubusoft.kripton.binder2.context.XmlBinderContext;
import com.abubusoft.kripton.binder2.context.YamlBinderContext;

import base.BaseProcessorTest;

public class TestKripton74 extends BaseProcessorTest {

	public BeanElement74 createBean() {
		BeanElement74 bean = new BeanElement74();
		
		BeanElement74 bean1 = new BeanElement74();
		bean1.valueString="nice to meet you";

		bean.valueMapStringInteger = new HashMap<>();
		bean.valueMapStringInteger.put("key1", 10);
		bean.valueMapStringInteger.put("key2", 20);
		
		bean.valueMapEnumBean=new HashMap<>();
		bean.valueMapEnumBean.put(BeanEnum74.VALUE_1, bean1);		
		bean.valueMapEnumBean.put(BeanEnum74.VALUE_2, null);
		
		bean.valueMapIntByteArray=new HashMap<>();
		byte[] a=new byte[23];
		bean.valueMapIntByteArray.put(20, null);
		//bean.valueMapIntByteArray.put(23, a);
		bean.valueMapIntByteArray.put(27, null);
		
		//bean.valueString="hello";

		return bean;
	}

	@Before
	public void setup() {
		KriptonBinder2.registryBinder(new YamlBinderContext());
		KriptonBinder2.registryBinder(new PropertiesBinderContext());
		KriptonBinder2.registryBinder(new XmlBinderContext());
	}

	@Test
	public void testCompatibility() throws IOException, InstantiationException, IllegalAccessException {
		BeanElement74 bean = createBean();
		{
			BinderWriter writer = KriptonBinder.getXmlWriter(BinderOptions.build().indent(false));
			System.out.println(writer.write(bean));
		}
		serializeAndParse(bean, BinderType.XML);
		
		{
			BinderWriter writer = KriptonBinder.getJsonWriter();
		//	System.out.println(writer.write(bean));
		}
	}

	@Test
	public void testCompile() throws IOException, InstantiationException, IllegalAccessException {
		buildBindProcessorTest(BeanElement74.class, BeanEnum74.class);
	}

	@Test
	public void testRun() throws IOException, InstantiationException, IllegalAccessException {
		Assert.assertNotNull(new BeanElement74BindMap());

		BeanElement74 bean = createBean();

		serializeAndParse(bean, BinderType.XML);
		serializeAndParse(bean, BinderType.JSON);
		serializeAndParse(bean, BinderType.YAML);
		serializeAndParse(bean, BinderType.PROPERTIES);
	}

	/**
	 * @param bean
	 * @param type
	 */
	public void serializeAndParse(Object bean, BinderType type) {
		String output1 = KriptonBinder2.getBinder(type).serialize(bean);
		System.out.println(output1);

		Object bean2 = KriptonBinder2.getBinder(type).parse(output1, bean.getClass());

		String output2 = KriptonBinder2.getBinder(type).serialize(bean2);
		System.out.println(output2);

		Assert.assertTrue(output1.equals(output2));
	}

}
