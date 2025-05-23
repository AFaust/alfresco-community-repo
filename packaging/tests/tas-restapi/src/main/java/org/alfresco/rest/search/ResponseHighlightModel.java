/*-
 * #%L
 * alfresco-tas-restapi
 * %%
 * Copyright (C) 2005 - 2022 Alfresco Software Limited
 * %%
 * This file is part of the Alfresco software. 
 * If the software was purchased under a paid Alfresco license, the terms of 
 * the paid license agreement will prevail.  Otherwise, the software is 
 * provided under the following open source license terms:
 * 
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
/*
 * Copyright (C) 2017 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.rest.search;

import java.util.List;
import java.util.Objects;

import org.alfresco.rest.core.IRestModel;
import org.alfresco.utility.model.TestModel;

/**
 * Pojo which represents the search response that includes the highlighting info.
 * 
 * @author Michael Suzuki
 *
 */
public class ResponseHighlightModel extends TestModel implements IRestModel<ResponseHighlightModel>
{
    private ResponseHighlightModel model;
    private String field;
    private List<String> snippets;

    public String getField()
    {
        return field;
    }

    public void setField(String field)
    {
        this.field = field;
    }

    public List<String> getSnippets()
    {
        return snippets;
    }

    public void setSnippets(List<String> snippets)
    {
        this.snippets = snippets;
    }

    @Override
    public ResponseHighlightModel onModel()
    {
        return model;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        ResponseHighlightModel that = (ResponseHighlightModel) o;
        return Objects.equals(model, that.model) && Objects.equals(field, that.field) && Objects.equals(snippets, that.snippets);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(model, field, snippets);
    }

    @Override
    public String toString()
    {
        return "ResponseHighlightModel{model=%s, field=%s, snippets=%s}".formatted(model, field, snippets);
    }
}
