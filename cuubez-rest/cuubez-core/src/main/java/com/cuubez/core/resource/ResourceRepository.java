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
package com.cuubez.core.resource;

import com.cuubez.core.exception.CuubezException;
import com.cuubez.core.resource.metaData.PathMetaData;
import com.cuubez.core.resource.metaData.SelectedResourceMetaData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;
import java.util.List;

public class ResourceRepository {

    private static Log log = LogFactory.getLog(ResourceRepository.class);

    private List<RootResource> rootResources = null;
    private static ResourceRepository instance = null;
	
	public static ResourceRepository getInstance() {

        if(instance == null) {
            log.trace("resource repository created");
            instance = new ResourceRepository();
        }

        return instance;
    }

	private ResourceRepository() {
		super();
		this.rootResources = new LinkedList<RootResource>();
	}

	public void addRootResource(RootResource rootResource) {
        this.rootResources.add(rootResource);
	}


    public SelectedResourceMetaData findResource(String path, String httpMethod) throws CuubezException {

        if (rootResources == null || path == null || httpMethod == null) {
            log.debug("resource not found");
            throw new CuubezException(CuubezException.RESOURCE_NOT_FOUND);
        }

        for (RootResource rootResource : rootResources) {

            PathMetaData rootPathMetaData = rootResource.getUriTemplate().match(path);

            if (rootPathMetaData != null) {

                log.debug("root resource found");
                List<SubResource> subResources = rootResource.getSubResources();

                for (SubResource subResource : subResources) {

                    PathMetaData subPathMetaData = null;

                    if (subResource.getUriTemplate() != null) {
                        subPathMetaData = subResource.getUriTemplate().match(rootPathMetaData.getTail());
                    }

                    if(subPathMetaData != null) {


                        String subResourceHttpMethod = subResource.getMethodMetaData().getHttpMethod();

                        if (subResourceHttpMethod.equals(httpMethod)) {

                            SelectedResourceMetaData selectedResourceMetaData = new SelectedResourceMetaData();
                            selectedResourceMetaData.setSelectedMethodMetaData(subResource.getMethodMetaData());
                            selectedResourceMetaData.addPathVariableMetaData(rootPathMetaData.getPathVariables());
                            selectedResourceMetaData.addPathVariableMetaData(subPathMetaData.getPathVariables());

                            log.debug("sub resource found");
                            return selectedResourceMetaData;
                        }
                    }
                }

            }
        }

        log.debug("resource not found");
        throw new CuubezException(CuubezException.RESOURCE_NOT_FOUND);
    }


}
