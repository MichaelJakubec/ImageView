/*
 * Copyright 2018 Michael Jakubec
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
 */

package net.jakubec.view.exif;

import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ExifReaderTest {

	@Test
	public void testParseImage(){
		ExifReader reader = new ExifReader();
		assertThat(reader, is(not(nullValue())));

		InputStream stream = ExifReaderTest.class.getResourceAsStream("/Bsp1.jpg");
		ExifInfo info = reader.parse(stream);
		assertThat(info, is(notNullValue()));
		assertThat(info.isValid(), is(true));


	}

}