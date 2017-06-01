/*******************************************************************************
 * Copyright 2015, 2017 Francesco Benincasa (info@abubusoft.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package sqlite.grammars.contenturi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.abubusoft.kripton.android.sqlite.ContentProviderURIParamsExtractor;
import com.abubusoft.kripton.processor.exceptions.KriptonProcessorException;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQL;
import com.abubusoft.kripton.processor.sqlite.grammars.jql.JQLChecker;
import com.abubusoft.kripton.processor.sqlite.grammars.uri.UriChecker;
import com.abubusoft.kripton.processor.sqlite.grammars.uri.UriChecker.UriPlaceHolderReplacerListener;
import com.abubusoft.kripton.processor.sqlite.grammars.uri.UriPlaceHolder;

import base.BaseProcessorTest;

@RunWith(JUnit4.class)
public class TestUriChecker extends BaseProcessorTest {

	protected void checkList(List<UriPlaceHolder> actual, UriPlaceHolder... input) {
		List<UriPlaceHolder> aspected = new ArrayList<>();

		for (UriPlaceHolder item : input) {
			aspected.add(item);
		}

		checkCollectionExactly(actual, aspected);
	}

	@Test
	public void testVerify() {
		String[] inputs = { "content://androi.authority/test/${ input0 }",
				"content://androi.authority/test/a/${ input0 }", "content://androi.authority/_",
				"content://androi.authority/"

		};

		for (String input : inputs) {
			UriChecker.getInstance().verify(input);
		}
	}

	@Test
	public void testVerifyFail() {
		String[] inputs = { "content://androi.authority/test/${ input0 }/", "content://androi.authority/${ input0 }/",
				"content://androi.authority/{ input0 }", "content://androi.authority/test/a/{ input0 }",
				"content://androi.authority", "content://androi.authority/$", "content://androi.authority///" };

		for (String input : inputs) {

			try {
				UriChecker.getInstance().verify(input);
				fail();
			} catch (KriptonProcessorException e) {
				// Every cycle has to go here
			}

		}
	}

	@Test
	public void testExtractParameters() {
		String input = "content://androi.authority/test/${ input }/ test /${ detail.id}";

		UriChecker checker = UriChecker.getInstance();

		{
			List<UriPlaceHolder> result = checker.extract(input);

			for (UriPlaceHolder item : result) {
				log(item.toString());
			}

			checkList(result, new UriPlaceHolder(1, "input"), new UriPlaceHolder(3, "detail.id"));			
		}

		{
			List<UriPlaceHolder> result = checker.extract(input);

			for (UriPlaceHolder item : result) {
				log(item.toString());
			}

			checkList(result, new UriPlaceHolder(1, "input"), new UriPlaceHolder(3, "detail.id"));			
		}
	}

	/**
	 * <p>
	 * OK
	 * </p>
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testOK() throws Throwable {
		String input = "content://androi.authority/test/${ input }";
		log(input);

		UriChecker checker = UriChecker.getInstance();

		String actual=checker.replace(input, new UriPlaceHolderReplacerListener() {

			@Override
			public String onParameterName(int pathSegmentIndex, String name) {
				log("segment : %s, name: %s", pathSegmentIndex, name);				
				return "?";
			}
		});
		assertEquals(actual, "content://androi.authority/test/?");
	}
	
	@Test
	public void testExtractor() throws Throwable {
		String input = "content://androi.authority/master/${ master }/detail/${detail}/subdetail/${subdetail}";
		log(input);

		UriChecker checker = UriChecker.getInstance();
		
		Map<String, UriPlaceHolder> parameters = checker.extractAsMap(input);

		String actual=checker.replace(input, new UriPlaceHolderReplacerListener() {

			@Override
			public String onParameterName(int pathSegmentIndex, String name) {
				log("segment : %s, name: %s", pathSegmentIndex, name);				
				return "?";
			}
		});
		String expected="content://androi.authority/master/?/detail/?/subdetail/?";
		
		assertEquals(actual, expected);
		
		log(expected);
		//log(""+expected.split("/").length);
		
		ContentProviderURIParamsExtractor extractor = new ContentProviderURIParamsExtractor(expected, input.split("/").length);
		
		for (UriPlaceHolder item: parameters.values()) {
			assertTrue(extractor.getPathSegment(item.pathSegmentIndex).equals("?"));	
		}
		
		
	}
	
	

	/**
	 * <p>
	 * OK
	 * </p>
	 * 
	 * @throws Throwable
	 */
	@Test(expected=KriptonProcessorException.class)
	public void testERROR() throws Throwable {
		String input = "content://androi.authority/test/${ input }/";
		log(input);

		UriChecker checker = UriChecker.getInstance();
		checker.verify(input);		
	}

	@Test
	public void testProjectColumn() {
		String sql = "select count(*) as pippo ,fieldName1, composed.fieldName2 from table where id = ${bean.id}";
		JQL jql = new JQL();
		jql.value = sql;

		JQLChecker.getInstance().extractProjections(jql);
	}

	@Test
	public void testAuthorityWithVariableInPath() {
		String input = "content://androi.authority/test/${ input1 }/${input2   }";

		UriChecker checker = UriChecker.getInstance();

		// check bind parameters
		{
			List<UriPlaceHolder> aspected = new ArrayList<>();
			aspected.add(new UriPlaceHolder(1, "input1"));
			aspected.add(new UriPlaceHolder(2, "input2"));
			List<UriPlaceHolder> actual = checker.extract(input);

			checkCollectionExactly(actual, aspected);
		}

	}

	@Test(expected = KriptonProcessorException.class)
	public void testAuthorityWithVariableInPathError() {
		String input = "content://androi.authority/test/${ input0 }/";

		UriChecker checker = UriChecker.getInstance();

		// check bind parameters
		{
			List<UriPlaceHolder> aspected = new ArrayList<>();
			aspected.add(new UriPlaceHolder(1, "input0"));
			aspected.add(new UriPlaceHolder(2, "input1"));
			List<UriPlaceHolder> actual = checker.extract(input);

			checkCollectionExactly(actual, aspected);
		}
	}

	@Test
	public void testExtractParametersFromPath() {
		String input = "test/${ input }/ test /${ detail.id}";

		UriChecker checker = UriChecker.getInstance();

		{
			List<UriPlaceHolder> result = checker.extractFromPath(input);

			for (UriPlaceHolder item : result) {
				log(item.toString());
			}

			checkList(result, new UriPlaceHolder(1, "input"), new UriPlaceHolder(3, "detail.id"));			
		}

		{
			List<UriPlaceHolder> result = checker.extractFromPath(input);

			for (UriPlaceHolder item : result) {
				log(item.toString());
			}

			checkList(result, new UriPlaceHolder(1, "input"), new UriPlaceHolder(3, "detail.id"));			
		}
	}

	@Test(expected = AssertionError.class)
	public void testAuthorityWithVariableInPathError2() {
		String input = "content://androi.authority/test/#";

		UriChecker checker = UriChecker.getInstance();

		// verify sql
		checker.verify(input);

		// check bind parameters
		{
			List<UriPlaceHolder> aspected = new ArrayList<>();
			aspected.add(new UriPlaceHolder(1, "input0"));
			aspected.add(new UriPlaceHolder(2, "input1"));
			List<UriPlaceHolder> actual = checker.extract(input);

			checkCollectionExactly(actual, aspected);
		}
	}

	@Test
	public void testReplaceAuthorityWithVariable() {
		String input = "content://androi.authority/test/${field0}/${field1}";
		String expected = "content://androi.authority/test/#/*";

		log(input);

		UriChecker checker = UriChecker.getInstance();

		// verify sql
		checker.verify(input);

		// check bind parameters
		{

			String actual = checker.replace(input, new UriPlaceHolderReplacerListener() {

				@Override
				public String onParameterName(int pathSegmentIndex, String name) {
					log("segment :" + pathSegmentIndex);
					if (name.endsWith("0")) {
						return "#";
					}
					;

					return "*";
				}

			});
			assertEquals(actual, expected);

			{
				List<UriPlaceHolder> aspectedHolders = new ArrayList<>();
				aspectedHolders.add(new UriPlaceHolder(1, "field0"));
				aspectedHolders.add(new UriPlaceHolder(2, "field1"));
				List<UriPlaceHolder> actualHolders = checker.extract(input);

				checkCollectionExactly(aspectedHolders, actualHolders);
			}

		}
	}

}
