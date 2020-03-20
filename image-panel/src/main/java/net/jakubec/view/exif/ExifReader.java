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

import java.io.IOException;
import java.io.InputStream;

public class ExifReader {

	public static final int SOI = 0xFFD8;
	/**
	 * Marker for the TIFF header in JPG file format
	 */
	public static final int APP0 = 0xFFE0;
	/**
	 * Marker for the EXIF-Header in the JPG File
	 */
	public static final int APP1 = 0xFFE1;

	private InputStream stream;

	public ExifInfo parse(InputStream stream) {
		this.stream = stream;
		try {
			if (readWord() != SOI) {
				return ExifInfo.INVALD;
			}
			switch (readWord()) {
				case APP0:
					break;
				case APP1:
					break;
				default:
					scanFor(0);

			}
			return new ExifInfo();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ExifInfo.INVALD;
	}



	private void parseExifHeader(){

	}

	private void scanFor(int marker) throws IOException {
		int value = 0;
		boolean search = false;
		do {
			value += stream.read();
			if ((value & 0xFFFF) == marker){
				search = false;
			} else {
				value = value << 8;
			}

		} while (search);
	}

	private int readWord() throws IOException {
		int result = stream.read();
		result = result << 8;
		result += stream.read();
		return  result;
	}
}
