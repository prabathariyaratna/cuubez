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
package com.cuubez.core.context;


import com.cuubez.core.util.AnnotationUtil;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class InterceptorRequestContext {

    private HttpServletRequest request;
    private String httpMethod;
    private Class<?> clazz;
    private Method method;

    public InterceptorRequestContext(HttpServletRequest request, String httpMethod, Class<?> clazz, Method method) {
        this.request = request;
        this.httpMethod = httpMethod;
        this.clazz = clazz;
        this.method = method;
    }

    public String getHeader(final String name) {
        return request.getHeader(name);
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public Method getMethod() {
        return method;
    }

    public List<Annotation> getAnnotations() {
        return AnnotationUtil.getMethodLevelAnnotations(clazz, method);
    }

    public boolean isAnnotationContain(final Class<?> annotation) {

        List<Annotation> annotations = AnnotationUtil.getMethodLevelAnnotations(clazz, method);

        for (final Annotation ann: annotations) {

            if(ann.annotationType().equals(annotation)) {
                return  true;
            }

        }
        return false;
    }

    public Annotation getAnnotation(final Class<?> annotation) {

        List<Annotation> annotations = AnnotationUtil.getMethodLevelAnnotations(clazz, method);

        for (final Annotation ann: annotations) {

            if(ann.annotationType().equals(annotation)) {
                return  ann;
            }

        }
        return null;

    }
}
