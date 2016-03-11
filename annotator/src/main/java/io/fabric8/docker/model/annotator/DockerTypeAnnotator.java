/*
 * Copyright (C) 2016 Original Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.fabric8.docker.model.annotator;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JEnumConstant;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import io.sundr.builder.annotations.Buildable;
import io.sundr.builder.annotations.Inline;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jsonschema2pojo.Jackson2Annotator;

public class DockerTypeAnnotator extends Jackson2Annotator {

    @Override
    public void propertyOrder(JDefinedClass clazz, JsonNode propertiesNode) {
        //We just want to make sure we avoid infinite loops
        clazz.annotate(JsonDeserialize.class)
                .param("using", JsonDeserializer.None.class);
        clazz.annotate(ToString.class);
        clazz.annotate(EqualsAndHashCode.class);
        try {
            JAnnotationArrayMember inlines = clazz.annotate(Buildable.class)
                    .param("editableEnabled", true)
                    .param("validationEnabled", true)
                    .param("generateBuilderPackage", true)
                    .param("builderPackage", "io.fabric8.docker.api.builder")
                    .paramArray("inline");

            inlines.annotate(Inline.class)
                    .param("type", new JCodeModel()._class("io.fabric8.docker.api.model.Doneable"))
                    .param("prefix", "Doneable")
                    .param("value", "done");

            //Let's get our hands dirty and add annotations so that we generate custom Inline methods
            if (clazz.name().equals("NetworkCreate")) {
                inlines.annotate(Inline.class)
                        .param("type", new JCodeModel()._class("io.fabric8.docker.api.model.Doneable"))
                        .param("returnType", new JCodeModel()._class("io.fabric8.docker.api.model.NetworkCreateResponse"))
                        .param("name", "InlineNetworkCreate")
                        .param("value", "done");
            }


            if (clazz.name().equals("AuthConfig")) {
                addInline(inlines, "InlineAuth", "java.lang.Boolean");
            }

            if (clazz.name().equals("NetworkCreate")) {
                addInline(inlines, "InlineNetworkCreate", "io.fabric8.docker.api.model.NetworkCreateResponse");
            }

            if (clazz.name().equals("VolumeCreateRequest")) {
                addInline(inlines, "InlineVolumeCreate", "io.fabric8.docker.api.model.Volume");
            }

            if (clazz.name().equals("ExecConfig")) {
                addInline(inlines, "InlineExecConfig", "io.fabric8.docker.api.model.ContainerExecCreateResponse");
            }


        } catch (JClassAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    private void addInline(JAnnotationArrayMember inlines, String name, String returnType) throws JClassAlreadyExistsException {
        inlines.annotate(Inline.class)
                .param("type", new JCodeModel()._class("io.fabric8.docker.api.model.Doneable"))
                .param("returnType", new JCodeModel()._class(returnType))
                .param("name", name)
                .param("value", "done");
    }

    @Override
    public void propertyInclusion(JDefinedClass clazz, JsonNode schema) {

    }

    @Override
    public void propertyField(JFieldVar field, JDefinedClass clazz, String propertyName, JsonNode propertyNode) {

    }

    @Override
    public void propertyGetter(JMethod getter, String propertyName) {

    }

    @Override
    public void propertySetter(JMethod setter, String propertyName) {

    }

    @Override
    public void anyGetter(JMethod getter) {

    }

    @Override
    public void anySetter(JMethod setter) {

    }

    @Override
    public void enumCreatorMethod(JMethod creatorMethod) {

    }

    @Override
    public void enumValueMethod(JMethod valueMethod) {

    }

    @Override
    public void enumConstant(JEnumConstant constant, String value) {

    }

    @Override
    public boolean isAdditionalPropertiesSupported() {
        return true;
    }

    @Override
    public void additionalPropertiesField(JFieldVar field, JDefinedClass clazz, String propertyName) {

    }
}
