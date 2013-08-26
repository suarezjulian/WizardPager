/*
 * Copyright 2013 str4d
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

package com.tech.freak.wizardpager.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Conditional implements ModelCallbacks {
    private Object mData = null;
    private List<Page> mConditionalPages = new ArrayList<Page>();

    public void onPageDataChanged(Page page) {
        mData = page.getData().get(Page.SIMPLE_DATA_KEY);
        for (Page p : mConditionalPages)
            p.isSatisfied();
    }

    public void onPageTreeChanged() {
    }

    public interface Condition {
        public boolean isSatisfied();
    }

    public class EqualCondition<T> implements Condition {
        private T mCompValue;

        public EqualCondition(Page page, T compValue) {
            mCompValue = compValue;
            mConditionalPages.add(page);
        }

        public boolean isSatisfied() {
            return mCompValue.equals(mData);
        }
    }

    public class NotEqualCondition<T> implements Condition {
        private T mCompValue;

        public NotEqualCondition(Page page, T compValue) {
            mCompValue = compValue;
            mConditionalPages.add(page);
        }

        public boolean isSatisfied() {
            return !(mCompValue.equals(mData));
        }
    }

    public class EqualAnyCondition<T> implements Condition {
        private ArrayList<T> mChoices = new ArrayList<T>();

        public EqualAnyCondition(Page page, T... choices) {
            mChoices.addAll(Arrays.asList(choices));
            mConditionalPages.add(page);
        }

        public boolean isSatisfied() {
            return mChoices.contains(mData);
        }
    }
}
