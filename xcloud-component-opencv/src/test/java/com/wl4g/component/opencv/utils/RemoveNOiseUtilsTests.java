/*
 * Copyright 2017 ~ 2025 the original author or authors. <wanglsir@gmail.com, 983708408@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wl4g.component.opencv.utils;

import org.junit.Test;
import org.opencv.core.Mat;

import com.wl4g.component.opencv.utils.BinaryUtils;
import com.wl4g.component.opencv.utils.GeneralUtils;
import com.wl4g.component.opencv.utils.GrayUtils;
import com.wl4g.component.opencv.utils.RemoveNoiseUtils;

public class RemoveNOiseUtilsTests extends OpencvBasedTests {

	@Test
	public void testPre() {
		for (int i = 1; i <= 6; i++) {
			String imgPath = "C:/Users/X240/Desktop/opencv/web/binary/b-" + i + ".jpg";
			String destPath = "C:/Users/X240/Desktop/opencv/web/binary/";
			Mat src = GeneralUtils.matFactory(imgPath);
			src = GrayUtils.grayColByAdapThreshold(src);
			src = BinaryUtils.binaryzation(src);
			src = RemoveNoiseUtils.connectedRemoveNoise(src, 50);
			GeneralUtils.saveImg(src, destPath + "noise-" + i + ".jpg");
		}
	}

}