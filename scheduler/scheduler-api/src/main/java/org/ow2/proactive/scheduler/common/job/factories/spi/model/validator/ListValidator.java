/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.ow2.proactive.scheduler.common.job.factories.spi.model.validator;

import org.ow2.proactive.scheduler.common.job.factories.spi.model.ModelValidatorContext;
import org.ow2.proactive.scheduler.common.job.factories.spi.model.exceptions.ValidationException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;


public class ListValidator implements Validator<String> {

    ImmutableList<String> listItems;

    public ListValidator(ImmutableList<String> items) {
        if (ImmutableSet.copyOf(items).size() < items.size()) {
            throw new IllegalArgumentException("Duplicate value in list : " + items);
        }
        this.listItems = items;
    }

    @Override
    public String validate(String parameterValue, ModelValidatorContext context, boolean isVariableHidden)
            throws ValidationException {
        if (!listItems.contains(parameterValue)) {
            throw new ValidationException("Expected value should be one of " + listItems + ", received '" +
                                          parameterValue + "'");
        }
        return parameterValue;
    }
}
