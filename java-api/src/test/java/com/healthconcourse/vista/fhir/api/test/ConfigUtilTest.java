/* Created by Perspecta http://www.perspecta.com */
/*
(c) 2017-2019 Perspecta

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.healthconcourse.vista.fhir.api.test;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ConfigUtilTest {

	@Test
	public void testConfigUtil() throws Exception {
		//this is not a proper test, since there is no tomcat context available
		//but if project is ever migrated to springboot, it should be modified
		//to test functionality properly
		String CONFIG_VISTA_URL_NAME = "VistaBaseUrl";
		String value = "test_value";
		Map<String, String> oldEnv = System.getenv();
		Map<String,String> newEnv = new HashMap<>(oldEnv);
		newEnv.put(CONFIG_VISTA_URL_NAME, value);
		setEnv(newEnv);
		assertEquals(value, System.getenv(CONFIG_VISTA_URL_NAME));
 	}

	//from https://stackoverflow.com/questions/318239/how-do-i-set-environment-variables-from-java
	private void setEnv(Map<String, String> newenv) throws Exception {
		try {
			Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
			Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
			theEnvironmentField.setAccessible(true);
			Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
			env.putAll(newenv);
			Field theCaseInsensitiveEnvironmentField = processEnvironmentClass
					.getDeclaredField("theCaseInsensitiveEnvironment");
			theCaseInsensitiveEnvironmentField.setAccessible(true);
			Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
			cienv.putAll(newenv);
		} catch (NoSuchFieldException e) {
			Class[] classes = Collections.class.getDeclaredClasses();
			Map<String, String> env = System.getenv();
			for (Class cl : classes) {
				if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
					Field field = cl.getDeclaredField("m");
					field.setAccessible(true);
					Object obj = field.get(env);
					Map<String, String> map = (Map<String, String>) obj;
					map.clear();
					map.putAll(newenv);
				}
			}
		}
	}
}
