// APL 2.0 / MIT dual license.
/*
 * Copyright 2017 "Masahiko Sakamoto" <sakamoto.gsyc.3s@gmail.com>
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
/*
 * MIT License
 * 
 * Copyright (c) 2017 "Masahiko Sakamoto" <sakamoto.gsyc.3s@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.glamenvseptzen.javadifflibsbenchmark;

import java.util.Objects;

/**
 * works > JDK 1.5 :)
 * 
 * @author sakamoto.gsyc.3s@gmail.com
 */
public class HexDumper {

    String prefix = "";

    public void setPrefix(String s) {
        this.prefix = s;
    }

    boolean toUpperCase = false;

    public void setToUpperCase(boolean b) {
        this.toUpperCase = b;
    }

    String separator = "";

    public void setSeparator(String s) {
        this.separator = s;
    }

    public String dump(final byte[] srcbytes) {
        if (Objects.isNull(srcbytes)) {
            return "";
        }
        if (srcbytes.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(srcbytes.length * 4);
        final int len = srcbytes.length;
        final int seplimit = len - 1;
        for (int i = 0; i < len; i++) {
            byte b = srcbytes[i];
            sb.append(prefix);
            if (toUpperCase) {
                sb.append(String.format("%02X", b));
            } else {
                sb.append(String.format("%02x", b));
            }
            if (i < seplimit) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }
}
