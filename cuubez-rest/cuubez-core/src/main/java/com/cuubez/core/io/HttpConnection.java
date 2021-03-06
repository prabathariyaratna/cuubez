/**
 *	Copyright [2013] [www.cuubez.com]
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.cuubez.core.io;

import com.cuubez.core.context.ResponseContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class HttpConnection implements Connection {


    public void write(HttpServletRequest request, HttpServletResponse response, ResponseContext responseContext) {

        String contentType = "Application/XML";

        if (responseContext.getMediaType() != null) {
            contentType = responseContext.getMediaType();
        }

        response.setContentType(contentType);
        response.setStatus(responseContext.getResponseCode());

        String encoding = request.getHeader("Accept-Encoding");

        if (encoding != null) {

            if (encoding.indexOf("gzip") > 0) {

                Compressor compressor = new GzipCompressor();
                compressor.compressAndWrite(response, responseContext);

            } else {
                Compressor compressor = new DefaultCompressor();
                compressor.compressAndWrite(response, responseContext);
            }

        } else {

            Compressor compressor = new DefaultCompressor();
            compressor.compressAndWrite(response, responseContext);
        }

    }


}
